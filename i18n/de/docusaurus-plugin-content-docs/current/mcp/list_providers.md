---
title: list_providers
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_providers
  - model context protocol
description: MCP-Tool, das die dem StackQL-MCP-Server verfügbaren Cloud- und SaaS-Provider auflistet
image: "/img/stackql-featured-image.png"
---

Gibt die bereits in den lokalen StackQL-Cache geladenen Provider zurück - die oberste Ebene der [Ressourcenhierarchie](/getting-started/resource-hierarchy) von StackQL.  Verwenden Sie dieses Tool als Einstiegspunkt, wenn ein Agent herausfinden muss, was er abfragen kann.

Für die in der *Registry* verfügbaren Provider (einschließlich noch nicht geladener) siehe [`list_registry`](/mcp/list_registry).

Siehe auch:
[[ MCP-Übersicht ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_registry` ]](/mcp/list_registry) [[ `pull_provider` ]](/mcp/pull_provider)

* * *

## Eingaben {#inputs}

Keine.

* * *

## Ausgabe {#output}

| Renderer | Struktur |
|--|--|
| Tabelle (Markdown-Tabelle für das LLM) | Eine Zeile pro Provider |

<br />

Jede Zeile enthält den Namen des Providers und die Versionsmetadaten, wie sie von der Registry veröffentlicht werden.

* * *

## Freigabe {#gating}

In jedem Servermodus erlaubt (`read_only`, `safe`, `delete_safe`, `full_access`).  Nur lesend.

* * *

## Beispiel {#example}

Probieren Sie diesen Prompt mit einem beliebigen Elicitation-fähigen MCP-Client aus, bei dem der StackQL-MCP-Server registriert ist:

```
Which cloud providers are available for you to query right now?
```

Hier ein Beispiel in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.mp4" type="video/mp4" />
</video>
