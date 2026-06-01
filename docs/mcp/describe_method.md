---
title: describe_method
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - describe_method
  - model context protocol
description: MCP tool returning the full I/O contract for one StackQL access method
image: "/img/stackql-featured-image.png"
---

Returns the full I/O contract for a single access method -- always EXTENDED, meaning inputs, request body schema, and full output shape.  Use this when an agent needs to construct a non-trivial `WHERE` clause or build a request body for `INSERT`/`UPDATE`/`EXEC`.

See also:
[[ MCP overview ]](/docs/command-line-usage/mcp) [[ `list_methods` ]](/docs/mcp/list_methods) [[ `describe_resource` ]](/docs/mcp/describe_resource)

* * *

## Inputs

| Argument | Required | Description |
|--|--|--|
|`provider`|Yes|Provider name.|
|`service`|Yes|Service under that provider.|
|`resource`|Yes|Resource under `provider.service`.|
|`method`|Yes|Method name as returned by [`list_methods`](/docs/mcp/list_methods).|

* * *

## Output

| Renderer | Shape |
|--|--|
| KV | Required and optional parameters, request body schema (when applicable), and the output field set |

* * *

## Gating

Allowed in every server mode.  Read only.

* * *

## Example

Try this prompt with any elicitation-capable MCP client with the StackQL MCP server registered:

```
Show me everything I need to know to call the insert method  
on Google Compute networks -- required parameters,  
optional parameters, and the request body shape.
```

Here's an example in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.mp4" type="video/mp4" />
</video>
