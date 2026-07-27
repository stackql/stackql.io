"""Shared parsing for the StackQL query library scripts.

An entry is one Markdown file under query-library/queries/ with YAML front
matter. The entry id is the path under queries/ without the .md extension
(e.g. aws/ec2/regions-enabled); it is not declared in front matter because
Docusaurus reserves the id key.
"""

from __future__ import annotations

import re
from dataclasses import dataclass, field
from pathlib import Path

import yaml

REPO_ROOT = Path(__file__).resolve().parents[2]
LIBRARY_DIR = REPO_ROOT / "query-library"
QUERIES_DIR = LIBRARY_DIR / "queries"
SCHEMA_PATH = LIBRARY_DIR / "schema" / "front-matter.schema.json"
STATIC_OUT_DIR = REPO_ROOT / "static" / "docs" / "query-library"

SITE_BASE_URL = "https://stackql.io/docs/query-library"

# Mirrors of the constraints enforced by the MCP server (query_library.go).
ID_RE = re.compile(r"^[a-z0-9_-]+(/[a-z0-9_-]+)*$")
IDENTIFIER_RE = re.compile(r"^[A-Za-z_][A-Za-z0-9_-]*$")
PLACEHOLDER_RE = re.compile(r"\{\{([A-Za-z0-9_]+)\}\}")
# Any {{...}} occurrence, used to detect malformed placeholder names.
ANY_PLACEHOLDER_RE = re.compile(r"\{\{\s*([^}]*?)\s*\}\}")

FRONT_MATTER_RE = re.compile(r"\A---\r?\n(.*?)\r?\n---\r?\n", re.DOTALL)
QUERY_HEADING_RE = re.compile(r"^##\s+Query\s*$", re.MULTILINE)
NOTES_HEADING_RE = re.compile(r"^##\s+Notes\s*$", re.MULTILINE)
H2_RE = re.compile(r"^##\s+", re.MULTILINE)
SQL_FENCE_RE = re.compile(r"^```sql[^\n]*\r?\n(.*?)\r?\n```\s*$", re.DOTALL | re.MULTILINE)

VERB_TO_FIRST_KEYWORDS = {
    "select": {"select", "with", "show", "describe"},
    "mutation": {"insert", "update", "delete", "replace"},
    "lifecycle": {"exec"},
}


@dataclass
class Entry:
    id: str
    path: Path
    raw: str
    front_matter: dict
    body: str
    template: str | None
    notes: str
    errors: list[str] = field(default_factory=list)

    @property
    def doc_url(self) -> str:
        return f"{SITE_BASE_URL}/queries/{self.id}"

    @property
    def verb(self) -> str:
        return self.front_matter.get("verb", "select")

    @property
    def mutation(self) -> bool:
        return self.verb in ("mutation", "lifecycle")


def _flatten_notes(section: str) -> str:
    """Collapse a Markdown section to a single prose string."""
    text = section.strip()
    if not text:
        return ""
    paragraphs = [re.sub(r"\s+", " ", p).strip() for p in re.split(r"\n\s*\n", text)]
    return " ".join(p for p in paragraphs if p)


def _section_after(heading_re: re.Pattern, body: str) -> str | None:
    m = heading_re.search(body)
    if not m:
        return None
    rest = body[m.end():]
    nxt = H2_RE.search(rest)
    return rest[: nxt.start()] if nxt else rest


def parse_entry(path: Path) -> Entry:
    rel = path.relative_to(QUERIES_DIR).as_posix()
    entry_id = rel[: -len(".md")]
    raw = path.read_text(encoding="utf-8")
    errors: list[str] = []

    fm_match = FRONT_MATTER_RE.match(raw)
    front_matter: dict = {}
    body = raw
    if not fm_match:
        errors.append("missing YAML front matter block")
    else:
        body = raw[fm_match.end():]
        try:
            loaded = yaml.safe_load(fm_match.group(1))
            if isinstance(loaded, dict):
                front_matter = loaded
            else:
                errors.append("front matter is not a YAML mapping")
        except yaml.YAMLError as e:
            errors.append(f"front matter YAML error: {e}")

    template = None
    query_section = _section_after(QUERY_HEADING_RE, body)
    if query_section is None:
        errors.append("missing '## Query' section")
    else:
        sql = SQL_FENCE_RE.search(query_section)
        if not sql:
            errors.append("no ```sql fence found under '## Query'")
        else:
            template = sql.group(1).strip()

    notes_section = _section_after(NOTES_HEADING_RE, body)
    notes = _flatten_notes(notes_section) if notes_section else ""

    return Entry(
        id=entry_id,
        path=path,
        raw=raw,
        front_matter=front_matter,
        body=body,
        template=template,
        notes=notes,
        errors=errors,
    )


def load_entries() -> list[Entry]:
    if not QUERIES_DIR.is_dir():
        raise SystemExit(f"queries directory not found: {QUERIES_DIR}")
    files = sorted(QUERIES_DIR.rglob("*.md"))
    return [parse_entry(p) for p in files]


def entry_to_doc(entry: Entry) -> dict:
    """Compile one entry to the queries/<id>.json shape consumed by the MCP
    server (libDoc in query_library.go). Key order matters only for humans."""
    fm = entry.front_matter
    doc: dict = {
        "id": entry.id,
        "title": fm.get("title", ""),
        "description": fm.get("description", ""),
        "mutation": entry.mutation,
        "verb": entry.verb,
        "status": fm.get("status", "draft"),
        "providers": fm.get("providers", []),
        "services": fm.get("services", []),
    }
    if fm.get("auth"):
        doc["auth"] = fm["auth"]
    doc["params"] = [_param_to_json(p) for p in fm.get("params", [])]
    if fm.get("outputs"):
        doc["outputs"] = [
            {k: o[k] for k in ("name", "type", "description") if k in o}
            for o in fm["outputs"]
        ]
    if fm.get("cost"):
        cost = {"fan_out": fm["cost"].get("fan_out", "none")}
        cost["expensive"] = bool(fm["cost"].get("expensive", False))
        if fm["cost"].get("notes"):
            cost["notes"] = fm["cost"]["notes"]
        doc["cost"] = cost
    if fm.get("related"):
        doc["related"] = fm["related"]
    doc["template"] = entry.template or ""
    if entry.notes:
        doc["notes"] = entry.notes
    doc["doc_url"] = entry.doc_url
    if fm.get("last_verified"):
        doc["last_verified"] = str(fm["last_verified"])
    return doc


def _param_to_json(p: dict) -> dict:
    out = {"name": p["name"], "type": p["type"], "required": bool(p.get("required", False))}
    for key in ("default", "description", "example", "enum", "pattern"):
        if key in p and p[key] is not None:
            out[key] = p[key]
    return out


def render_value(param: dict, value) -> str:
    """Validate one supplied value against its declaration and return the
    literal text to interpolate. Mirrors validateParamValue in the MCP
    server (query_library.go): number/boolean parsing, identifier and
    pattern validation, enum membership, quote escaping for string
    positions."""
    name = param.get("name", "?")
    ptype = str(param.get("type", "string")).lower()
    s = str(value)
    if ptype == "number":
        float(s)  # raises ValueError on mismatch
        return s
    if ptype == "boolean":
        if s.lower() not in ("true", "false"):
            raise ValueError(f"param {name!r} must be a boolean, got {s!r}")
        return s.lower()
    if ptype == "identifier":
        if not IDENTIFIER_RE.match(s):
            raise ValueError(f"param {name!r} must match {IDENTIFIER_RE.pattern}, got {s!r}")
        return s
    if ptype == "enum":
        allowed = [str(v) for v in param.get("enum", [])]
        if s not in allowed:
            raise ValueError(f"param {name!r} must be one of {allowed}, got {s!r}")
        return s.replace("'", "''")
    # string
    pattern = param.get("pattern")
    if pattern and not re.fullmatch(pattern, s):
        raise ValueError(f"param {name!r} must match pattern {pattern}, got {s!r}")
    return s.replace("'", "''")


def entry_to_index_entry(entry: Entry) -> dict:
    """Compile one entry to the index.json catalogue row shape (libIndexEntry).

    verb is additive to the frozen contract: the MCP server's libIndexEntry
    ignores unknown fields, and the site's provider pages need it to badge
    entries without fetching every per-query document."""
    fm = entry.front_matter
    return {
        "id": entry.id,
        "title": fm.get("title", ""),
        "description": fm.get("description", ""),
        "providers": fm.get("providers", []),
        "services": fm.get("services", []),
        "tags": fm.get("tags", []),
        "keywords": fm.get("keywords", []),
        "intent_keywords": fm.get("intent_keywords", []),
        "mutation": entry.mutation,
        "verb": entry.verb,
        "status": fm.get("status", "draft"),
        "required_params": [
            p["name"] for p in fm.get("params", []) if p.get("required", False)
        ],
    }
