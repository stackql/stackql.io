# CLAUDE.md - query-library

Instructions for Claude Code when working in `query-library/`. Read
`CONTRIBUTING.md` first; this file adds the non-obvious constraints.

## Hard contract

The stackql MCP server (see `pkg/mcp_server/query_library.go` in the core repo)
consumes the artifacts this directory compiles to. Breaking any of these breaks
deployed servers:

- URL paths under `/docs/query-library/` and the raw GitHub path
  `static/docs/query-library/` (default fallback tier)
- entry ids (path-derived, `provider/service/slug`, regexp `^[a-z0-9_-]+(/[a-z0-9_-]+)*$`)
- placeholder syntax `{{name}}` with names matching `[A-Za-z0-9_]+`
- param types: `string`, `number`, `boolean`, `identifier`, `enum`
- the `<id>.json` field set (see any file under `static/docs/query-library/queries/`)
- `build_id` semantics: content hash, changes only when library content changes

Keep the emitted JSON shape in lockstep with the reference implementation in the
core repo (`pkg/mcp_server/content/query_library/`).

## Authoring rules

- Front matter must NOT contain an `id` key. Docusaurus reserves `id` and rejects
  values containing slashes; the build derives the id from the file path.
- Front matter MUST contain `format: md`. Without it Docusaurus parses the file as
  MDX and braces in prose break the build.
- The first ` ```sql ` fence after `## Query` is extracted verbatim as the
  template. Exactly one statement, terminated with `;`.
- The `## Notes` section is flattened to a single prose string in the JSON. No
  lists, no code fences, no headings inside it.
- `verb` must match the statement: `select` for SELECT, `mutation` for
  INSERT/UPDATE/DELETE/REPLACE, `lifecycle` for EXEC.
- Only author queries you have verified against the provider docs or a live
  stackql instance (the stackql MCP tools `describe_resource` and
  `validate_select_query` are the fastest check). Wrong field names poison agent
  retrieval downstream. New unverified entries stay `status: draft`.
- `permissions` is provider-native IAM action syntax (`iam:ListUsers`,
  `Microsoft.Compute/virtualMachines/read`, `compute.instances.list`) and must
  list only what the template's wire calls require. Omit the field when
  unsure - agents relay it verbatim as 403 remediation, so a wrong list is
  worse than none. AWS entries backed by Cloud Control (methods like
  `create_resource`, `data__Identifier` keys) need `cloudcontrol:ListResources`
  or `cloudcontrol:GetResource` plus the underlying service actions.
- Write `intent_keywords` as user asks, not as descriptions ("list all s3
  buckets", not "s3 bucket enumeration").

## After any change under queries/

```bash
python query-library/scripts/validate.py
python query-library/scripts/build-artifacts.py
```

and commit the regenerated `static/docs/query-library/` files in the same commit.
Never hand-edit anything under `static/docs/query-library/`; it is script-owned
and regenerated wholesale.
