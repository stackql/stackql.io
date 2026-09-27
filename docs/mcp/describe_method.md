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

Returns the full I/O contract for a single access method -- always EXTENDED, meaning inputs, request body schema, and full output shape.  This is where an agent gets column names: each row carries a `param_type` of `input_required` (a mandatory, exact-match `WHERE` predicate), `input_optional`, or `output` (a field a `SELECT` or `RETURNING` can reference).  A resource has no single field list, because each method returns its own shape, so call this for the method chosen from [`list_methods`](/docs/mcp/list_methods) before the first query against it.

See also:
[[ MCP overview ]](/docs/command-line-usage/mcp) [[ `list_methods` ]](/docs/mcp/list_methods) [[ `run_select_query` ]](/docs/mcp/run_select_query)

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
| KV | One row per input and output field: name, type, `param_type` (`input_required`, `input_optional` or `output`), shape and description |

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
