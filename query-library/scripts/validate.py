#!/usr/bin/env python3
"""Validate query library entries.

Checks (per PR CI gate):
- YAML front matter conforms to schema/front-matter.schema.json
- entry id (path-derived) is provider/service/slug and matches the MCP id regexp
- id segment 1 is in providers, id segment 2 is in services
- exactly one SQL statement in the '## Query' fence, terminated with ';'
- placeholder/param parity in both directions
- placeholder names are well formed ({{name}}, [A-Za-z0-9_]+)
- verb matches the statement's first keyword
- params without a default have an example; enum params' default/example are members
- unique ids (structural) and unique titles
- related ids resolve to existing entries
- no MDX import/export lines in the body (pages are CommonMark)

Exit code 0 when clean, 1 with a per-entry error report otherwise.
"""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))

import jsonschema

from qlib import (
    ANY_PLACEHOLDER_RE,
    ID_RE,
    PLACEHOLDER_RE,
    SCHEMA_PATH,
    VERB_TO_FIRST_KEYWORDS,
    Entry,
    load_entries,
)

MDX_LINE_RE = re.compile(r"^\s*(import|export)\s+", re.MULTILINE)


def validate_entry(entry: Entry, schema: dict) -> list[str]:
    errors = list(entry.errors)
    fm = entry.front_matter

    # Schema.
    validator = jsonschema.Draft7Validator(schema)
    for err in sorted(validator.iter_errors(fm), key=str):
        loc = "/".join(str(p) for p in err.absolute_path) or "front matter"
        errors.append(f"schema: {loc}: {err.message}")

    # Id shape.
    if not ID_RE.match(entry.id):
        errors.append(f"id {entry.id!r} does not match {ID_RE.pattern}")
    segments = entry.id.split("/")
    if len(segments) != 3:
        errors.append(f"id {entry.id!r} must have exactly 3 segments (provider/service/slug)")
    else:
        provider_seg, service_seg = segments[0], segments[1]
        providers = fm.get("providers") or []
        services = fm.get("services") or []
        if providers and providers[0] != provider_seg:
            errors.append(
                f"first provider {providers[0]!r} must equal id path segment {provider_seg!r}"
            )
        if services and service_seg not in services:
            errors.append(f"services must include id path segment {service_seg!r}")

    # No MDX in body.
    if MDX_LINE_RE.search(entry.body):
        errors.append("body contains MDX import/export lines; entries are CommonMark")

    # Template checks.
    if entry.template is not None:
        errors.extend(validate_template(entry))

    return errors


def validate_template(entry: Entry) -> list[str]:
    errors: list[str] = []
    fm = entry.front_matter
    template = entry.template or ""

    stripped = template.strip()
    if not stripped.endswith(";"):
        errors.append("template must end with ';'")
    if stripped.count(";") != 1:
        errors.append("template must contain exactly one statement")

    # Verb vs first keyword.
    first_word = stripped.split(None, 1)[0].lower() if stripped else ""
    verb = fm.get("verb")
    if verb in VERB_TO_FIRST_KEYWORDS and first_word not in VERB_TO_FIRST_KEYWORDS[verb]:
        allowed = ", ".join(sorted(VERB_TO_FIRST_KEYWORDS[verb]))
        errors.append(f"verb {verb!r} but statement starts with {first_word!r} (expected {allowed})")

    # Placeholder well-formedness: every {{...}} must be a valid placeholder.
    valid_spans = {m.span() for m in PLACEHOLDER_RE.finditer(template)}
    for m in ANY_PLACEHOLDER_RE.finditer(template):
        if m.span() not in valid_spans:
            errors.append(f"malformed placeholder {m.group(0)!r} (names are [A-Za-z0-9_]+, no spaces)")

    # Parity in both directions.
    placeholders = {m.group(1) for m in PLACEHOLDER_RE.finditer(template)}
    declared = {p.get("name") for p in fm.get("params", []) if isinstance(p, dict)}
    for name in sorted(placeholders - declared):
        errors.append(f"placeholder {{{{{name}}}}} is not declared in params")
    for name in sorted(declared - placeholders):
        errors.append(f"param {name!r} is declared but not used in the template")

    # Param-level rules.
    for p in fm.get("params", []):
        if not isinstance(p, dict):
            continue
        name = p.get("name", "?")
        if "default" not in p and "example" not in p:
            errors.append(f"param {name!r} needs an example (or a default)")
        if p.get("type") == "enum":
            allowed = p.get("enum") or []
            if not allowed:
                errors.append(f"enum param {name!r} is missing its enum list")
            for key in ("default", "example"):
                if key in p and str(p[key]) not in [str(v) for v in allowed]:
                    errors.append(f"enum param {name!r} {key} {p[key]!r} is not in enum")
        if p.get("type") != "enum" and p.get("enum"):
            errors.append(f"param {name!r} has an enum list but type {p.get('type')!r}")
        if p.get("pattern"):
            try:
                re.compile(p["pattern"])
            except re.error as e:
                errors.append(f"param {name!r} pattern does not compile: {e}")

    return errors


def main() -> int:
    schema = json.loads(SCHEMA_PATH.read_text(encoding="utf-8"))
    entries = load_entries()
    if not entries:
        print("no entries found under query-library/queries/", file=sys.stderr)
        return 1

    failures: dict[str, list[str]] = {}
    for entry in entries:
        errs = validate_entry(entry, schema)
        if errs:
            failures[entry.id] = errs

    # Cross-entry checks.
    known_ids = {e.id for e in entries}
    titles: dict[str, str] = {}
    for entry in entries:
        title = entry.front_matter.get("title", "")
        if title:
            if title in titles:
                failures.setdefault(entry.id, []).append(
                    f"title duplicates {titles[title]!r}"
                )
            else:
                titles[title] = entry.id
        for rel in entry.front_matter.get("related", []) or []:
            if rel not in known_ids:
                failures.setdefault(entry.id, []).append(f"related id {rel!r} does not resolve")
            if rel == entry.id:
                failures.setdefault(entry.id, []).append("entry lists itself as related")

    if failures:
        print(f"FAIL: {len(failures)} of {len(entries)} entries have errors\n", file=sys.stderr)
        for entry_id in sorted(failures):
            print(f"  {entry_id}", file=sys.stderr)
            for err in failures[entry_id]:
                print(f"    - {err}", file=sys.stderr)
        return 1

    print(f"OK: {len(entries)} entries validated")
    return 0


if __name__ == "__main__":
    sys.exit(main())
