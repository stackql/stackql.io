---
title: run_lifecycle_operation
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_lifecycle_operation
  - model context protocol
description: MCP tool executing a StackQL EXEC lifecycle operation
image: "/img/stackql-featured-image.png"
---

Executes a StackQL `EXEC` lifecycle operation -- the non-CRUD provider actions (start/stop a VM, rotate a key, trigger a workflow, etc.).  **Real side effects.**

Gated by the server [mode](/docs/command-line-usage/mcp#server-modes):

| Mode | Lifecycle `EXEC` |
|--|--|
|`read_only`|refused|
|`safe` (default)|needs approval (elicitation)|
|`delete_safe`|needs approval|
|`full_access`|allowed|

When approval is needed and the client did not advertise elicitation, the call is refused.

See also:
[[ MCP overview ]](/docs/command-line-usage/mcp) [[ `run_mutation_query` ]](/docs/mcp/run_mutation_query) [[ `describe_method` ]](/docs/mcp/describe_method) [[ EXEC language spec ]](/docs/language-spec/exec)

* * *

## Inputs

| Argument | Required | Description |
|--|--|--|
|`sql`|Yes|A StackQL `EXEC` statement targeting a lifecycle method.|

* * *

## Output

| Renderer | Shape |
|--|--|
| KV | `{messages: [...], timestamp}` |

* * *

## Example

Try this prompt with any elicitation-capable MCP client with the StackQL MCP server registered:

```
The Compute instance web-01 in us-central1-a  
(project my-project) is stuck.  Please stop it.
```

The MCP client will surface an approval prompt before the operation is dispatched, unless the server is running in `full_access` mode.

Here's an example in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.mp4" type="video/mp4" />
</video>
