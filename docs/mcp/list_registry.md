---
title: list_registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_registry
  - model context protocol
description: MCP tool listing providers and versions available in the StackQL provider registry
image: "/img/stackql-featured-image.png"
---

Returns the providers (and their versions) available in the configured StackQL provider registry.  Distinct from [`list_providers`](/docs/mcp/list_providers), which only reports providers already pulled into the local cache.

Pass the optional `provider` argument to list the published versions for a single provider.  Pair with [`pull_provider`](/docs/mcp/pull_provider) when the agent needs to install something that is not yet local.

See also:
[[ MCP overview ]](/docs/command-line-usage/mcp) [[ `list_providers` ]](/docs/mcp/list_providers) [[ `pull_provider` ]](/docs/mcp/pull_provider) [[ Registry command ]](/docs/command-line-usage/registry)

* * *

## Inputs

| Argument | Required | Description |
|--|--|--|
|`provider`|No|Provider name.  When supplied, the result is narrowed to that provider's published versions; when omitted, every registry entry is returned.|

* * *

## Output

| Renderer | Shape |
|--|--|
| Table | One row per registry entry (provider plus version metadata) |

* * *

## Gating

Allowed in every server mode.  No cloud control or data plane effect -- only the registry is read.

* * *

## Example

Try this prompt with any elicitation-capable MCP client with the StackQL MCP server registered:

```
What providers does the StackQL registry offer,  
and which versions of the aws provider are published?
```

Here's an example in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.mp4" type="video/mp4" />
</video>
