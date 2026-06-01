---
title: server_info
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - server_info
  - model context protocol
description: MCP tool returning StackQL MCP server identity and runtime
image: "/img/stackql-featured-image.png"
---

Returns the StackQL MCP server's identity and runtime: version, backing SQL engine, provider registry location, server mode, and the legacy read-only flag.  Call this once at session start so the agent knows what it is connected to.

See also:
[[ MCP overview ]](/docs/command-line-usage/mcp) [[ `list_providers` ]](/docs/mcp/list_providers) [[ `list_registry` ]](/docs/mcp/list_registry)

* * *

## Inputs

None.

* * *

## Output

| Renderer | Shape |
|--|--|
| KV (markdown key/value block for the LLM) | Structured payload with the fields below |

<br/><br/>

| Field | Description |
|--|--|
|`version`|StackQL build version.|
|`commit`|Git commit short hash.|
|`build_date`|RFC3339 build timestamp.|
|`platform`|`os/arch` (e.g., `linux/amd64`).|
|`transport`|`http` or `stdio`.|
|`sql_backend`|Backing SQL engine (e.g., `sqlite3`).|
|`provider_registry`|Provider registry URL.|
|`mode`|Active server mode -- `read_only`, `safe`, `delete_safe`, or `full_access`.|
|`is_read_only`|Legacy boolean flag (`true` iff `mode == read_only`).|

* * *

## Gating

Always allowed in every server mode.  Not audited as a mutation.

* * *

## Example

Try this prompt with any elicitation-capable MCP client with the StackQL MCP server registered:

```
What StackQL server am I connected to?  
Show me the version, mode, and where it pulls providers from.
```

Here's an example in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_server_info_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_server_info_web.mp4" type="video/mp4" />
</video>
