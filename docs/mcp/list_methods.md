---
title: list_methods
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_methods
  - model context protocol
description: MCP tool listing access methods for a StackQL resource
image: "/img/stackql-featured-image.png"
---

Returns the access methods (HTTP operations) bound to a resource -- the read methods, mutations, and lifecycle operations available.  This is the tool an agent should call **before writing any query**, because the required `WHERE` parameters are inferred from the chosen read method.

See also:
[[ MCP overview ]](/docs/command-line-usage/mcp) [[ `list_resources` ]](/docs/mcp/list_resources) [[ `describe_method` ]](/docs/mcp/describe_method) [[ `validate_select_query` ]](/docs/mcp/validate_select_query)

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
| Table | One row per access method, including the SQL verb it maps to and the required parameters |

* * *

## Gating

Allowed in every server mode.  Read only.

* * *

## Example

Try this prompt with any elicitation-capable MCP client with the StackQL MCP server registered:

```
For Google Compute instances, what methods are available  
and what parameters are required?
```

Here's an example in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.mp4" type="video/mp4" />
</video>
