---
title: StackQL Query Library
description: Curated, parameterized StackQL queries for cloud inventory, security and operations, published for humans, raw markdown consumers and the stackql MCP server.
format: md
keywords: [stackql, query library, sql, cloud inventory, mcp]
---

The StackQL Query Library is a curated set of parameterized, known-good StackQL
queries for common cloud inventory, security and operations asks across AWS,
Google Cloud, Azure, GitHub, Cloudflare, Databricks and other providers.

Every entry is published in three forms at stable URLs:

- a rendered documentation page at `/docs/query-library/queries/<id>`
- the raw Markdown source with YAML front matter at
  `/docs/query-library/queries/<id>.md`
- a structured JSON document at `/docs/query-library/queries/<id>.json`,
  consumed by the stackql MCP server's `query_library_search` and
  `query_library_get` tools

The machine-readable catalogue lives at
[index.json](https://stackql.io/docs/query-library/index.json), with build
metadata in
[manifest.json](https://stackql.io/docs/query-library/manifest.json) and a
Markdown catalogue at
[index.md](https://stackql.io/docs/query-library/index.md) listing every entry
grouped by provider.

## How agents use the library

The stackql MCP server ships two read-only tools backed by these URLs.
`query_library_search` ranks entries against a natural-language intent
("list all s3 buckets") and returns candidate ids. `query_library_get`
retrieves one entry: without parameters it returns the raw template and
parameter declarations; with parameters the server validates the values,
renders the SQL and reports which execution tool to use. Templates are
interpolated server-side with type validation and escaping, so the model never
performs substitution itself.

The server caches the catalogue keyed by the `build_id` in manifest.json,
which is a content hash of the library: it changes when and only when library
content changes. When stackql.io is unreachable, servers fall back to the raw
GitHub copy of these artifacts, then to a snapshot embedded in the stackql
binary.

## Entry anatomy

Each entry declares its execution verb (`select`, `mutation` or `lifecycle`),
typed parameters with validation rules, expected output columns, cost hints
(fan-out dimension and expense flags), required credentials by environment
variable name, and retrieval keywords phrased the way users ask. The SQL
template uses double-brace placeholders that correspond one-to-one with the
declared parameters.

## Contributing

Entries are Markdown files under `query-library/queries/` in the
[stackql.io repository](https://github.com/stackql/stackql.io), compiled to
the published artifacts by a build step and verified nightly against live
providers. See
[CONTRIBUTING.md](https://github.com/stackql/stackql.io/blob/main/query-library/CONTRIBUTING.md)
for the authoring guide.
