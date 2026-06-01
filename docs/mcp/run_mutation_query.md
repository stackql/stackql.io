---
title: run_mutation_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_mutation_query
  - model context protocol
description: MCP tool executing INSERT, UPDATE, REPLACE, or DELETE against a provider via StackQL
image: "/img/stackql-featured-image.png"
---

Executes `INSERT`, `UPDATE`, `REPLACE`, or `DELETE` against the provider.  **Real side effects** -- a successful call changes cloud state.

Gated by the server [mode](/docs/command-line-usage/mcp#server-modes):

| Mode | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` |
|--|--|--|
|`read_only`|refused|refused|
|`safe` (default)|needs approval (elicitation)|needs approval|
|`delete_safe`|allowed|needs approval|
|`full_access`|allowed|allowed|
<br/>
When a mutation needs approval and the client did not advertise the MCP elicitation capability, the call is refused.

See also:
[[ MCP overview ]](/docs/command-line-usage/mcp) [[ `run_lifecycle_operation` ]](/docs/mcp/run_lifecycle_operation) [[ `describe_method` ]](/docs/mcp/describe_method) [[ INSERT language spec ]](/docs/language-spec/insert) [[ DELETE language spec ]](/docs/language-spec/delete)

* * *

## Inputs

| Argument | Required | Description |
|--|--|--|
|`sql`|Yes|An `INSERT`, `UPDATE`, `REPLACE`, or `DELETE` statement.|

* * *

## Output

| Renderer | Shape |
|--|--|
| KV | `{messages: [...], timestamp}` -- dispatch confirmation, not the API response body |

* * *

## Example

Try this prompt with any elicitation-capable MCP client with the StackQL MCP server registered:

```
The firewall rule allow-test-old in project my-project  
is no longer needed.  Please remove it.
```

The MCP client will surface an approval prompt before the delete is dispatched, unless the server is running in `full_access` mode.

The following clip shows the elicitation flow when the StackQL MCP server is running in the default `safe` mode -- the client is prompted to approve the `DELETE` before any cloud-side change is dispatched.  You can reproduce this against your own StackQL MCP server using the official [MCP Inspector](https://github.com/modelcontextprotocol/inspector) using `npx @modelcontextprotocol/inspector`:


<video autoPlay loop muted playsInline style={{width: 'calc(100% - 4rem)', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.webm" type="video/webm" />
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.mp4" type="video/mp4" />
</video>
