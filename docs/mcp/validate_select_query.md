---
title: validate_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - validate_select_query
  - model context protocol
description: MCP tool parsing and planning a StackQL SELECT without executing it
image: "/img/stackql-featured-image.png"
---

Parses and plans a `SELECT` statement without executing it.  Returns `{valid, errors}`.  Useful when an agent wants to verify its query compiles and binds against the discovered method before committing to a real API call.

`SELECT` only.  Mutations and lifecycle operations are not accepted.

See also:
[[ MCP overview ]](/docs/command-line-usage/mcp) [[ `list_methods` ]](/docs/mcp/list_methods) [[ `run_select_query` ]](/docs/mcp/run_select_query)

* * *

## Inputs

| Argument | Required | Description |
|--|--|--|
|`sql`|Yes|A `SELECT` statement.|

* * *

## Output

| Renderer | Shape |
|--|--|
| KV | `valid` (bool) and `errors` (array of strings, empty when valid) |

* * *

## Gating

Allowed in every server mode.  No API calls are made.

* * *

## Example

Try this prompt with any elicitation-capable MCP client with the StackQL MCP server registered:

```
Can you validate this query before I run it:

select name, network  
from google.compute.subnetworks  
where project = 'my-project' and region = 'us-central1';
```

Here's an example in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.mp4" type="video/mp4" />
</video>
