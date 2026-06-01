---
title: list_services
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_services
  - model context protocol
description: MCP tool listing services under a StackQL provider
image: "/img/stackql-featured-image.png"
---

Returns the services exposed by a single provider -- the second level of the StackQL [resource hierarchy](/docs/getting-started/resource-hierarchy).  Call this after [`list_providers`](/docs/mcp/list_providers) to narrow the surface area before drilling into resources.

See also:
[[ MCP overview ]](/docs/command-line-usage/mcp) [[ `list_providers` ]](/docs/mcp/list_providers) [[ `list_resources` ]](/docs/mcp/list_resources)

* * *

## Inputs

| Argument | Required | Description |
|--|--|--|
|`provider`|Yes|Provider name (e.g., `google`, `aws`, `azure`, `github`).|

* * *

## Output

| Renderer | Shape |
|--|--|
| Table | One row per service under the given provider |

* * *

## Gating

Allowed in every server mode.  Read only.

* * *

## Example

Try this prompt with any elicitation-capable MCP client with the StackQL MCP server registered:

```
List the services available in Google Cloud
```

Here's an example in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_services_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_services_web.mp4" type="video/mp4" />
</video>
