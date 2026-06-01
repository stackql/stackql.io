---
title: list_providers
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_providers
  - model context protocol
description: MCP tool listing cloud and SaaS providers available to the StackQL MCP server
image: "/img/stackql-featured-image.png"
---

Returns the providers already pulled into the local StackQL cache -- the top of the StackQL [resource hierarchy](/docs/getting-started/resource-hierarchy).  Use this as the entry point when an agent needs to discover what it can query.

For providers available in the *registry* (including ones not yet pulled), see [`list_registry`](/docs/mcp/list_registry).

See also:
[[ MCP overview ]](/docs/command-line-usage/mcp) [[ `list_services` ]](/docs/mcp/list_services) [[ `list_registry` ]](/docs/mcp/list_registry) [[ `pull_provider` ]](/docs/mcp/pull_provider)

* * *

## Inputs

None.

* * *

## Output

| Renderer | Shape |
|--|--|
| Table (markdown table for the LLM) | One row per provider |

<br />

Each row carries the provider name and version metadata as published by the registry.

* * *

## Gating

Allowed in every server mode (`read_only`, `safe`, `delete_safe`, `full_access`).  Read only.

* * *

## Example

Try this prompt with any elicitation-capable MCP client with the StackQL MCP server registered:

```
Which cloud providers are available for you to query right now?
```

Here's an example in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.mp4" type="video/mp4" />
</video>
