---
title: pull_provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - pull_provider
  - model context protocol
description: MCP-Tool, das einen StackQL-Provider aus der Registry in den lokalen Cache installiert
image: "/img/stackql-featured-image.png"
---

Installiert einen einzelnen Provider aus der StackQL-Registry in den lokalen approot-Cache.  Wenn `version` nicht angegeben ist, wird die neueste veröffentlichte Version geladen.

Schreibt nur den lokalen Cache-Zustand - es gibt keine Auswirkung auf die Control Plane oder Data Plane der Cloud, daher ist dieses Tool in jedem Servermodus erlaubt (einschließlich `read_only`).  Verwenden Sie [`list_registry`](/mcp/list_registry), um vor dem Laden den Namen des Providers und die verfügbaren Versionen zu ermitteln.

Siehe auch:
[[ MCP-Übersicht ]](/command-line-usage/mcp) [[ `list_registry` ]](/mcp/list_registry) [[ `list_providers` ]](/mcp/list_providers) [[ Registry-Befehl ]](/command-line-usage/registry)

* * *

## Eingaben {#inputs}

| Argument | Erforderlich | Beschreibung |
|--|--|--|
|`provider`|Ja|Name des Providers, wie in der Registry veröffentlicht (z. B. `aws`, `google`, `cloudflare`).|
|`version`|Nein|Zu ladende veröffentlichte Version.  Wenn nicht angegeben, wird die neueste veröffentlichte Version installiert.|

* * *

## Ausgabe {#output}

| Renderer | Struktur |
|--|--|
| KV | Bestätigung, dass der Provider (und die Version) in den lokalen Cache installiert wurde |

* * *

## Freigabe {#gating}

In jedem Servermodus erlaubt.  Keine cloudseitigen Auswirkungen; nur der lokale Cache-Zustand ändert sich.

* * *

## Beispiel {#example}

Probieren Sie diesen Prompt mit einem beliebigen Elicitation-fähigen MCP-Client aus, bei dem der StackQL-MCP-Server registriert ist:

```
I want to start querying Google resources with StackQL.  
Can you install the Google provider for me?
```

Hier ein Beispiel in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.mp4" type="video/mp4" />
</video>
