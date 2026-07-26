#!/usr/bin/env python3
"""Compile query library sources into the published artifacts.

Reads query-library/queries/**/*.md and regenerates static/docs/query-library/
wholesale:

  index.json          tool catalogue (search runs over this)
  index.md            human/model readable catalogue
  manifest.json       build_id, timestamp, commit, entry count
  queries/<id>.json   parsed front matter + template (tool-facing)
  queries/<id>.md     verbatim copy of the source (raw GitHub fallback tier)

build_id is a content hash of the compiled library (raw sources + emitted JSON),
not the site build id: it is the MCP server's cache key and must change when and
only when library content changes. manifest.json's generated_at and
library_commit are only rewritten when build_id changes, so re-running this
script on unchanged content is byte-for-byte idempotent (CI relies on this).

Validation runs first; artifacts are never emitted from invalid sources.
"""

from __future__ import annotations

import hashlib
import json
import shutil
import subprocess
import sys
from datetime import datetime, timezone
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))

import validate
from qlib import (
    REPO_ROOT,
    SITE_BASE_URL,
    STATIC_OUT_DIR,
    entry_to_doc,
    entry_to_index_entry,
    load_entries,
)


def canonical_json(value) -> str:
    return json.dumps(value, indent=2, ensure_ascii=False) + "\n"


def compute_build_id(entries, docs, index_entries) -> str:
    h = hashlib.sha256()
    for entry, doc in zip(entries, docs):
        h.update(entry.id.encode("utf-8"))
        h.update(b"\0")
        h.update(entry.raw.encode("utf-8"))
        h.update(b"\0")
        h.update(canonical_json(doc).encode("utf-8"))
        h.update(b"\0")
    h.update(canonical_json(index_entries).encode("utf-8"))
    return "ql-" + h.hexdigest()[:16]


def git_short_commit() -> str:
    try:
        out = subprocess.run(
            ["git", "rev-parse", "--short", "HEAD"],
            cwd=REPO_ROOT,
            capture_output=True,
            text=True,
            check=True,
        )
        return out.stdout.strip()
    except (OSError, subprocess.CalledProcessError):
        return "unknown"


def build_manifest(build_id: str, entry_count: int) -> dict:
    manifest_path = STATIC_OUT_DIR / "manifest.json"
    if manifest_path.is_file():
        try:
            existing = json.loads(manifest_path.read_text(encoding="utf-8"))
        except json.JSONDecodeError:
            existing = {}
        if existing.get("build_id") == build_id:
            # Unchanged content: preserve the manifest verbatim so regeneration
            # is idempotent.
            existing["entry_count"] = entry_count
            return existing
    return {
        "build_id": build_id,
        "generated_at": datetime.now(timezone.utc).strftime("%Y-%m-%dT%H:%M:%SZ"),
        "library_commit": git_short_commit(),
        "entry_count": entry_count,
    }


def build_index_md(entries, build_id: str) -> str:
    lines = [
        "# StackQL Query Library",
        "",
        "> Curated, parameterized StackQL queries for common cloud inventory,",
        "> security and operations asks. Each entry has a rendered doc page (HTML),",
        "> a raw Markdown source (`<id>.md`) and a structured JSON document",
        "> (`<id>.json`) consumed by the stackql MCP server's `query_library_search`",
        "> and `query_library_get` tools.",
        "",
        f"Build `{build_id}` | {len(entries)} entries | machine catalogue:",
        f"[index.json]({SITE_BASE_URL}/index.json) |",
        f"[manifest.json]({SITE_BASE_URL}/manifest.json)",
        "",
    ]
    by_provider: dict[str, list] = {}
    for entry in entries:
        provider = entry.id.split("/")[0]
        by_provider.setdefault(provider, []).append(entry)
    for provider in sorted(by_provider):
        lines.append(f"## {provider}")
        lines.append("")
        for entry in by_provider[provider]:
            fm = entry.front_matter
            required = [p["name"] for p in fm.get("params", []) if p.get("required")]
            qualifiers = [fm.get("verb", "select")]
            if fm.get("status") != "stable":
                qualifiers.append(fm.get("status", "draft"))
            if required:
                qualifiers.append("params: " + ", ".join(required))
            lines.append(
                f"- [{fm.get('title', entry.id)}]({entry.doc_url}) "
                f"({'; '.join(qualifiers)}): {fm.get('description', '')}"
            )
        lines.append("")
    return "\n".join(lines).rstrip() + "\n"


def main() -> int:
    if validate.main() != 0:
        print("build-artifacts: validation failed, not emitting artifacts", file=sys.stderr)
        return 1

    entries = load_entries()
    docs = [entry_to_doc(e) for e in entries]
    index_entries = [entry_to_index_entry(e) for e in entries]
    build_id = compute_build_id(entries, docs, index_entries)
    manifest = build_manifest(build_id, len(entries))

    if STATIC_OUT_DIR.exists():
        shutil.rmtree(STATIC_OUT_DIR)
    (STATIC_OUT_DIR / "queries").mkdir(parents=True)

    (STATIC_OUT_DIR / "index.json").write_text(
        canonical_json({"build_id": build_id, "entries": index_entries}),
        encoding="utf-8",
        newline="\n",
    )
    (STATIC_OUT_DIR / "manifest.json").write_text(
        canonical_json(manifest), encoding="utf-8", newline="\n"
    )
    (STATIC_OUT_DIR / "index.md").write_text(
        build_index_md(entries, build_id), encoding="utf-8", newline="\n"
    )
    for entry, doc in zip(entries, docs):
        out_json = STATIC_OUT_DIR / "queries" / f"{entry.id}.json"
        out_md = STATIC_OUT_DIR / "queries" / f"{entry.id}.md"
        out_json.parent.mkdir(parents=True, exist_ok=True)
        out_json.write_text(canonical_json(doc), encoding="utf-8", newline="\n")
        out_md.write_text(entry.raw, encoding="utf-8", newline="\n")

    print(
        f"built {len(entries)} entries -> {STATIC_OUT_DIR.relative_to(REPO_ROOT)} "
        f"(build_id {build_id})"
    )
    return 0


if __name__ == "__main__":
    sys.exit(main())
