---
title: describe_resource
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - describe_resource
  - model context protocol
description: MCP tool describing the output fields of a StackQL resource
image: "/img/stackql-featured-image.png"
---

Returns the output fields exposed by a resource's primary read method -- the columns the agent can `SELECT`.  Use this when the agent needs to know what data shape will come back before writing the query.

For the full I/O contract of a specific method (inputs, output, request body), use [`describe_method`](/docs/mcp/describe_method).

See also:
[[ MCP overview ]](/docs/command-line-usage/mcp) [[ `list_resources` ]](/docs/mcp/list_resources) [[ `describe_method` ]](/docs/mcp/describe_method) [[ `run_select_query` ]](/docs/mcp/run_select_query)

* * *

## Inputs

| Argument | Required | Description |
|--|--|--|
|`provider`|Yes|Provider name.|
|`service`|Yes|Service under that provider.|
|`resource`|Yes|Resource under `provider.service`.|

* * *

## Output

| Renderer | Shape |
|--|--|
| KV | Field name, type, and description for each output column on the primary read method |

* * *

## Gating

Allowed in every server mode.  Read only.

* * *

## Example

Try this prompt with any elicitation-capable MCP client with the StackQL MCP server registered:

```
What fields does a Google Storage bucket  
return when I query it?
```

Here's an example in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_describe_resource_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_describe_resource_web.mp4" type="video/mp4" />
</video>
