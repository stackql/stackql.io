---
title: query_library_get
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_get
  - query library
  - model context protocol
description: MCP tool retrieving a StackQL query template and validating supplied parameters to return rendered SQL without execution
image: "/img/stackql-featured-image.png"
---

Retrieves a [query library](pathname:///docs/query-library/) entry by an `id` returned from [`query_library_search`](/mcp/query_library_search).

With `params` omitted or empty, it returns the raw template, parameter declarations and notes. With a non-empty `params` object, the server validates the values and substitutes them into the template. It returns SQL only when rendering succeeds. The model does not need to substitute values itself.

This tool does not execute SQL or check cloud credentials. Its parameter validation is distinct from [`validate_select_query`](/mcp/validate_select_query), which parses and plans a `SELECT` against the installed provider.

See also:
[[ MCP overview ]](/command-line-usage/mcp) [[ `query_library_search` ]](/mcp/query_library_search) [[ `run_select_query` ]](/mcp/run_select_query) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation)

* * *

## Inputs

| Argument | Required | Description |
|--|--|--|
|`id`|Yes|Query ID from search results, for example `aws/s3/buckets-list`.|
|`params`|No|Object mapping declared parameter names to values. Omit it or pass `{}` to inspect the template without rendering.|
|`format`|No|Text rendering: `markdown` or `json`. When omitted, uses the server's render setting (`markdown` by default).|

Unknown parameters are rejected. Required parameters without supplied values or defaults are reported in `missing_params`. Supplied values are checked against declared types and constraints; unresolved placeholders are errors.

* * *

## Output

The default text view is a KV block with identity, mutation status, the next tool, validity, source links and any template, rendered SQL or errors. Use the structured payload or `format: "json"` for the full metadata:

| Field | Description |
|--|--|
|`id`, `title`, `description`|Entry identity and summary.|
|`mutation`, `verb`|Whether the entry changes resources and, when declared, its operation type (`select`, `mutation` or `lifecycle`).|
|`next_tool`|Execution tool for the entry: `run_select_query`, `run_mutation_query` or `run_lifecycle_operation`. Retrieval does not call it.|
|`params`|Parameter declarations with names, types, required flags and any defaults, descriptions, examples, enums or patterns.|
|`template`, `notes`|Raw template and author notes, returned when `params` is omitted or empty.|
|`rendered`, `valid`|Whether rendering was attempted and succeeded. Both are `false` when inspecting the raw template.|
|`sql`|Rendered SQL, present only after successful rendering.|
|`missing_params`, `unknown_params`, `errors`|Validation details when rendering fails. Do not execute SQL until these are resolved.|
|`auth`, `permissions`|When declared, credential environment variables and provider-native permissions needed for execution. Retrieval itself requires neither.|
|`cost`, `outputs`, `related`|When declared, fan-out and expense hints, result column descriptions and related entry IDs.|
|`doc_url`|Human-readable library page.|
|`source_tier`, `source_url`, `stale`|Content provenance: `primary`, `fallback` or `snapshot`, its URL or embedded path, and whether content was served from cache or snapshot after a fetch failure.|

An unknown or malformed ID returns an error. Use `query_library_search` to find valid IDs.

* * *

## Gating

Allowed in every server mode, subject to the configured tool allowlist. Retrieving or rendering a mutation or lifecycle template has no cloud-side effect and needs no approval. Executing the returned SQL is a separate call governed by the server [mode](/command-line-usage/mcp#server-modes).

* * *

## Example

First inspect an entry:

```json
{
  "id": "aws/s3/buckets-list"
}
```

Then supply its required routing region:

```json
{
  "id": "aws/s3/buckets-list",
  "params": {
    "region": "us-east-1"
  }
}
```

After checking `rendered` and `valid`, validate the returned `SELECT` with [`validate_select_query`](/mcp/validate_select_query). Execute it with the tool named in `next_tool`, passing the returned `sql` and setting `source` to the library entry's `id` for attribution. Provider credentials and permissions are required at execution time.
