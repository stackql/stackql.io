---
title: list_resources
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_resources
  - model context protocol
description: MCP tool listing resources under a StackQL provider.service
image: "/img/stackql-featured-image.png"
---

Returns the resources under a `provider.service` -- the third level of the StackQL [resource hierarchy](/docs/getting-started/resource-hierarchy).  Resources are the targets that appear in the `FROM` clause of a StackQL query.

See also:
[[ MCP overview ]](/docs/command-line-usage/mcp) [[ `list_services` ]](/docs/mcp/list_services) [[ `list_methods` ]](/docs/mcp/list_methods) [[ `describe_resource` ]](/docs/mcp/describe_resource)

* * *

## Inputs

| Argument | Required | Description |
|--|--|--|
|`provider`|Yes|Provider name.|
|`service`|Yes|Service under that provider.|

* * *

## Output

| Renderer | Shape |
|--|--|
| Table | One row per resource under `provider.service` |

* * *

## Gating

Allowed in every server mode.  Read only.

* * *

## Example

Try this prompt with any elicitation-capable MCP client with the StackQL MCP server registered:

```
List the resources in the google.storage service
```

Here's an example in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.mp4" type="video/mp4" />
</video>
