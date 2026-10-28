---
title: query_library_search
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_search
  - query library
  - model context protocol
description: MCP tool searching curated StackQL query templates by natural-language intent, provider, service and tags
image: "/img/stackql-featured-image.png"
---

Searches the [StackQL query library](pathname:///docs/query-library/) for templates matching a natural-language intent. Consult it before composing SQL from scratch, then pass a result's `id` to [`query_library_get`](/mcp/query_library_get) to inspect the template and validate its parameters.

The tool retrieves library content only. It does not execute SQL, call cloud APIs or require provider credentials.

See also:
[[ MCP overview ]](/command-line-usage/mcp) [[ `query_library_get` ]](/mcp/query_library_get) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## Inputs

| Argument | Required | Description |
|--|--|--|
|`intent`|Yes|Natural-language description of the task.|
|`provider`|No|Filter by provider, for example `aws`.|
|`service`|No|Filter by service, for example `s3`.|
|`tags`|No|Array of tags. An entry must match every supplied tag.|
|`include_mutations`|No|Include mutation templates. Defaults to `false`; ignored in `read_only` mode, where mutation entries are always excluded.|
|`limit`|No|Maximum number of hits. Defaults to `5` when omitted or non-positive; capped at `20`.|
|`format`|No|Text rendering: `markdown` or `json`. When omitted, uses the server's render setting (`markdown` by default).|

* * *

## Output

The default text view is a ranked table of `id`, `title`, `mutation`, `required_params` and `score`, followed by the source citation and any miss guidance. The structured payload contains:

| Field | Description |
|--|--|
|`hits`|Ranked entries with `id`, `title`, optional `description`, `providers`, `services` and `required_params`, plus `mutation` and `score`. Follow up with `query_library_get` for the complete parameter declarations.|
|`miss`|`true` when there are no hits or the top score is below the match threshold. Low-scoring nearest matches can still be returned.|
|`guidance`|On a miss, directs the agent to the server instructions, discovery tools and `validate_select_query` to author a query.|
|`source_tier`|Content source: `primary`, `fallback` or `snapshot`.|
|`source_url`|URL or embedded path of the catalogue used, for citation.|
|`stale`|When `true`, content was served from cache or the embedded snapshot after a fetch failure.|

Deprecated entries are excluded. The server caches the catalogue and can fall back from the hosted library to its GitHub source and an embedded snapshot.

* * *

## Gating

Allowed in every server mode, subject to the configured tool allowlist. No approval or provider credentials are needed. Searching for mutation templates does not authorize their execution.

* * *

## Example

Tool arguments:

```json
{
  "intent": "List S3 bucket names and regions",
  "provider": "aws",
  "service": "s3",
  "limit": 5
}
```

Use an `id` from `hits`, such as `aws/s3/buckets-list`, with [`query_library_get`](/mcp/query_library_get). If `miss` is `true`, inspect the nearest templates for guidance or use [`list_methods`](/mcp/list_methods) and [`describe_method`](/mcp/describe_method) before authoring SQL.
