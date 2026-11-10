---
title: server_info
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - server_info
  - model context protocol
description: MCP-Tool, das Identität und Laufzeitumgebung des StackQL-MCP-Servers zurückgibt
image: "/img/stackql-featured-image.png"
---

Gibt Identität und Laufzeitumgebung des StackQL-MCP-Servers zurück: Version, zugrunde liegende SQL-Engine, Speicherort der Provider-Registry, Servermodus und das veraltete Nur-Lese-Flag.  Rufen Sie dieses Tool einmal zu Sitzungsbeginn auf, damit der Agent weiß, womit er verbunden ist.

Siehe auch:
[[ MCP-Übersicht ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_registry` ]](/mcp/list_registry)

* * *

## Eingaben {#inputs}

Keine.

* * *

## Ausgabe {#output}

| Renderer | Struktur |
|--|--|
| KV (Markdown-Schlüssel/Wert-Block für das LLM) | Strukturierte Payload mit den folgenden Feldern |

<br/><br/>

| Feld | Beschreibung |
|--|--|
|`version`|StackQL-Build-Version.|
|`commit`|Kurzer Git-Commit-Hash.|
|`build_date`|Build-Zeitstempel im RFC3339-Format.|
|`platform`|`os/arch` (z. B. `linux/amd64`).|
|`transport`|`http` oder `stdio`.|
|`sql_backend`|Zugrunde liegende SQL-Engine (z. B. `sqlite3`).|
|`provider_registry`|URL der Provider-Registry.|
|`mode`|Aktiver Servermodus - `read_only`, `safe`, `delete_safe` oder `full_access`.|
|`is_read_only`|Veraltetes boolesches Flag (`true` genau dann, wenn `mode == read_only`).|

* * *

## Freigabe {#gating}

In jedem Servermodus immer erlaubt.  Wird nicht als Mutation auditiert.

* * *

## Beispiel {#example}

Probieren Sie diesen Prompt mit einem beliebigen Elicitation-fähigen MCP-Client aus, bei dem der StackQL-MCP-Server registriert ist:

```
What StackQL server am I connected to?  
Show me the version, mode, and where it pulls providers from.
```

Hier ein Beispiel in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_server_info_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_server_info_web.mp4" type="video/mp4" />
</video>
