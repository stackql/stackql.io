---
title: pull_provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - pull_provider
  - model context protocol
description: MCP tool installing a StackQL provider from the registry into the local cache
image: "/img/stackql-featured-image.png"
---

Installs a single provider from the StackQL registry into the local approot cache.  When `version` is omitted, the latest published version is pulled.

Writes only local cache state -- there is no cloud control or data plane effect, so this tool is allowed in every server mode (including `read_only`).  Use [`list_registry`](/docs/mcp/list_registry) to discover the provider name and the versions available before pulling.

See also:
[[ MCP overview ]](/docs/command-line-usage/mcp) [[ `list_registry` ]](/docs/mcp/list_registry) [[ `list_providers` ]](/docs/mcp/list_providers) [[ Registry command ]](/docs/command-line-usage/registry)

* * *

## Inputs

| Argument | Required | Description |
|--|--|--|
|`provider`|Yes|Provider name as published in the registry (e.g., `aws`, `google`, `cloudflare`).|
|`version`|No|Published version to pull.  When omitted, the latest published version is installed.|

* * *

## Output

| Renderer | Shape |
|--|--|
| KV | Confirmation that the provider (and version) has been installed into the local cache |

* * *

## Gating

Allowed in every server mode.  No cloud-side effects; only local cache state changes.

* * *

## Example

Try this prompt with any elicitation-capable MCP client with the StackQL MCP server registered:

```
I want to start querying Google resources with StackQL.  
Can you install the Google provider for me?
```

Here's an example in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.mp4" type="video/mp4" />
</video>
