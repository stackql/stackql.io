#!/usr/bin/env python3
"""Render every template with its example/default values and check the result.

Mirrors the MCP server's rendering rules (validateParamValue in
query_library.go): number/boolean parsing, identifier and pattern validation,
enum membership, single-quote escaping for string positions, and rejection of
unresolved placeholders after substitution.

With --stackql-bin (or a stackql binary on PATH), each rendered statement is
additionally passed through `stackql exec --dryrun`, which runs the
preprocessor only: no provider calls, no credentials, no execution. Use
--require-binary in CI so a missing binary fails rather than degrades.
"""

from __future__ import annotations

import argparse
import re
import shutil
import subprocess
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))

from qlib import PLACEHOLDER_RE, Entry, load_entries, render_value


def render_entry(entry: Entry) -> str:
    """Render the template using example (or default) values."""
    values: dict[str, str] = {}
    for p in entry.front_matter.get("params", []):
        value = p.get("example", p.get("default"))
        if value is None:
            raise ValueError(f"param {p.get('name')!r} has neither example nor default")
        values[p["name"]] = render_value(p, value)

    sql = PLACEHOLDER_RE.sub(lambda m: values.get(m.group(1), m.group(0)), entry.template or "")
    leftover = PLACEHOLDER_RE.search(sql)
    if leftover:
        raise ValueError(f"unresolved placeholder {leftover.group(0)} after substitution")
    return sql


def dryrun(stackql_bin: str, sql: str) -> str | None:
    """Run the statement through the stackql preprocessor. Returns an error
    string or None."""
    try:
        proc = subprocess.run(
            [stackql_bin, "exec", sql, "--dryrun"],
            capture_output=True,
            text=True,
            timeout=60,
        )
    except (OSError, subprocess.TimeoutExpired) as e:
        return f"stackql invocation failed: {e}"
    if proc.returncode != 0:
        detail = (proc.stderr or proc.stdout or "").strip()
        return f"stackql exec --dryrun exited {proc.returncode}: {detail[:400]}"
    return None


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--stackql-bin", default=shutil.which("stackql"))
    parser.add_argument(
        "--require-binary",
        action="store_true",
        help="fail when no stackql binary is available instead of skipping the dryrun step",
    )
    args = parser.parse_args()

    if args.require_binary and not args.stackql_bin:
        print("render-check: no stackql binary found and --require-binary set", file=sys.stderr)
        return 1

    entries = load_entries()
    failures: dict[str, str] = {}
    for entry in entries:
        if entry.errors or entry.template is None:
            failures[entry.id] = "; ".join(entry.errors) or "no template"
            continue
        try:
            sql = render_entry(entry)
        except ValueError as e:
            failures[entry.id] = str(e)
            continue
        if args.stackql_bin:
            err = dryrun(args.stackql_bin, sql)
            if err:
                failures[entry.id] = err

    if failures:
        print(f"FAIL: {len(failures)} of {len(entries)} entries\n", file=sys.stderr)
        for entry_id, err in sorted(failures.items()):
            print(f"  {entry_id}: {err}", file=sys.stderr)
        return 1

    mode = "render + dryrun" if args.stackql_bin else "render only (no stackql binary)"
    print(f"OK: {len(entries)} entries rendered ({mode})")
    return 0


if __name__ == "__main__":
    sys.exit(main())
