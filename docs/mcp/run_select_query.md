---
title: run_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_select_query
  - model context protocol
description: MCP tool executing a StackQL SELECT against a provider
image: "/img/stackql-featured-image.png"
---

Executes a `SELECT` statement against the configured providers and returns the result rows.  Read only -- mutations and lifecycle operations are not accepted; use [`run_mutation_query`](/docs/mcp/run_mutation_query) or [`run_lifecycle_operation`](/docs/mcp/run_lifecycle_operation) for those.

Result rows are returned to the client but are deliberately excluded from the audit log.

See also:
[[ MCP overview ]](/docs/command-line-usage/mcp) [[ `validate_select_query` ]](/docs/mcp/validate_select_query) [[ `list_methods` ]](/docs/mcp/list_methods) [[ SELECT language spec ]](/docs/language-spec/select)

* * *

## Inputs

| Argument | Required | Description |
|--|--|--|
|`sql`|Yes|A `SELECT` statement.|
|`row_limit`|No|Cap on the number of rows returned to the client.  `0` (default) means no client-side cap.|

* * *

## Output

| Renderer | Shape |
|--|--|
| Table | `{rows: [...]}` -- one object per row, keyed by column name |

* * *

## Gating

Allowed in every server mode (`read_only`, `safe`, `delete_safe`, `full_access`).

* * *

## Example

Try this prompt with any elicitation-capable MCP client with the StackQL MCP server registered:

```
List the VPC networks in my Google Cloud project my-project  
and tell me which ones use auto subnet creation.
```

Here's an example in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.mp4" type="video/mp4" />
</video>
