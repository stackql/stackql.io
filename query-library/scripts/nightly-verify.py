#!/usr/bin/env python3
"""Execute read-only query library entries against live sandbox credentials.

For every entry with verb select and status stable or draft:

- resolve param values as override > declared default (examples are
  documentation, never executed); entries with uncovered params are skipped
- skip entries whose declared auth env vars are not present
- execute the rendered SQL with the stackql binary
- on success: promote draft to stable, and set last_verified when it is
  missing or older than 7 days (weekly granularity keeps build_id from
  churning every night)
- on failure: flip stable to draft (draft entries stay draft)

Overrides come from --params-json or the QUERY_LIBRARY_VERIFY_PARAMS env var:
a JSON object keyed by entry id (or "*" for all entries) mapping param name to
value, e.g. {"*": {"region": "us-east-1"}, "aws/s3/bucket-detail":
{"bucket_name": "my-sandbox-bucket"}}.

Writes a JSON report (--report) the CI workflow uses to open an issue on
failures. Mutation, lifecycle and deprecated entries are never executed.
The caller regenerates artifacts afterwards (build-artifacts.py).
"""

from __future__ import annotations

import argparse
import datetime
import json
import os
import re
import subprocess
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))

from qlib import PLACEHOLDER_RE, Entry, load_entries, render_value

STATUS_LINE_RE = re.compile(r"^status: .*$", re.MULTILINE)
LAST_VERIFIED_LINE_RE = re.compile(r"^last_verified: .*$", re.MULTILINE)


def resolve_values(entry: Entry, overrides: dict) -> dict[str, str] | None:
    """Return rendered literal per param, or None when a param is uncovered."""
    merged_overrides = {**overrides.get("*", {}), **overrides.get(entry.id, {})}
    values: dict[str, str] = {}
    for p in entry.front_matter.get("params", []):
        name = p["name"]
        raw = merged_overrides.get(name, p.get("default"))
        if raw is None:
            return None
        values[name] = render_value(p, raw)
    return values


def execute(stackql_bin: str, sql: str) -> str | None:
    """Run the query. Returns an error string or None on success."""
    try:
        proc = subprocess.run(
            [stackql_bin, "exec", sql, "--output", "json"],
            capture_output=True,
            text=True,
            timeout=300,
        )
    except (OSError, subprocess.TimeoutExpired) as e:
        return f"stackql invocation failed: {e}"
    stderr = (proc.stderr or "").strip()
    if proc.returncode != 0:
        return f"exit {proc.returncode}: {stderr[:400]}"
    if "error" in stderr.lower():
        return f"stderr reported an error: {stderr[:400]}"
    return None


def update_front_matter(entry: Entry, *, status: str | None, verified_today: bool) -> None:
    text = entry.path.read_text(encoding="utf-8")
    if status:
        text = STATUS_LINE_RE.sub(f"status: {status}", text, count=1)
    if verified_today:
        today = datetime.date.today().isoformat()
        line = f'last_verified: "{today}"'
        if LAST_VERIFIED_LINE_RE.search(text):
            text = LAST_VERIFIED_LINE_RE.sub(line, text, count=1)
        else:
            text = STATUS_LINE_RE.sub(lambda m: f"{m.group(0)}\n{line}", text, count=1)
    entry.path.write_text(text, encoding="utf-8", newline="\n")


def needs_verification_stamp(entry: Entry) -> bool:
    last = entry.front_matter.get("last_verified")
    if not last:
        return True
    try:
        last_date = datetime.date.fromisoformat(str(last))
    except ValueError:
        return True
    return (datetime.date.today() - last_date).days >= 7


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--stackql-bin", default="stackql")
    parser.add_argument("--params-json", default=os.environ.get("QUERY_LIBRARY_VERIFY_PARAMS", "{}"))
    parser.add_argument("--report", default="nightly-verify-report.json")
    args = parser.parse_args()

    try:
        overrides = json.loads(args.params_json)
    except json.JSONDecodeError as e:
        print(f"nightly-verify: bad params JSON: {e}", file=sys.stderr)
        return 1

    report = {"executed": [], "skipped": [], "failed": [], "promoted": [], "demoted": []}
    for entry in load_entries():
        fm = entry.front_matter
        if fm.get("verb") != "select" or fm.get("status") not in ("stable", "draft"):
            continue
        missing_auth = [v for v in fm.get("auth", []) if not os.environ.get(v)]
        if missing_auth:
            report["skipped"].append({"id": entry.id, "reason": f"missing auth env: {', '.join(missing_auth)}"})
            continue
        values = resolve_values(entry, overrides)
        if values is None:
            report["skipped"].append({"id": entry.id, "reason": "params not covered by defaults or overrides"})
            continue

        sql = PLACEHOLDER_RE.sub(lambda m: values.get(m.group(1), m.group(0)), entry.template or "")
        err = execute(args.stackql_bin, sql)
        if err is None:
            report["executed"].append(entry.id)
            promote = fm.get("status") == "draft"
            stamp = needs_verification_stamp(entry)
            if promote:
                report["promoted"].append(entry.id)
            if promote or stamp:
                update_front_matter(entry, status="stable" if promote else None, verified_today=stamp or promote)
        else:
            report["failed"].append({"id": entry.id, "error": err})
            if fm.get("status") == "stable":
                update_front_matter(entry, status="draft", verified_today=False)
                report["demoted"].append(entry.id)

    Path(args.report).write_text(json.dumps(report, indent=2) + "\n", encoding="utf-8")
    print(
        f"executed {len(report['executed'])}, failed {len(report['failed'])}, "
        f"skipped {len(report['skipped'])}, promoted {len(report['promoted'])}, "
        f"demoted {len(report['demoted'])}"
    )
    for f in report["failed"]:
        print(f"  FAILED {f['id']}: {f['error']}", file=sys.stderr)
    return 0


if __name__ == "__main__":
    sys.exit(main())
