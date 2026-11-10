---
title: list_registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_registry
  - model context protocol
description: MCP-Tool, das die in der StackQL-Provider-Registry verfügbaren Provider und Versionen auflistet
image: "/img/stackql-featured-image.png"
---

Gibt die in der konfigurierten StackQL-Provider-Registry verfügbaren Provider (und ihre Versionen) zurück.  Nicht zu verwechseln mit [`list_providers`](/mcp/list_providers), das nur die bereits in den lokalen Cache geladenen Provider meldet.

Übergeben Sie das optionale Argument `provider`, um die veröffentlichten Versionen eines einzelnen Providers aufzulisten.  Kombinieren Sie es mit [`pull_provider`](/mcp/pull_provider), wenn der Agent etwas installieren muss, das noch nicht lokal vorhanden ist.

Siehe auch:
[[ MCP-Übersicht ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `pull_provider` ]](/mcp/pull_provider) [[ Registry-Befehl ]](/command-line-usage/registry)

* * *

## Eingaben {#inputs}

| Argument | Erforderlich | Beschreibung |
|--|--|--|
|`provider`|Nein|Name des Providers.  Wenn angegeben, wird das Ergebnis auf die veröffentlichten Versionen dieses Providers eingegrenzt; wenn nicht angegeben, wird jeder Registry-Eintrag zurückgegeben.|

* * *

## Ausgabe {#output}

| Renderer | Struktur |
|--|--|
| Tabelle | Eine Zeile pro Registry-Eintrag (Provider plus Versionsmetadaten) |

* * *

## Freigabe {#gating}

In jedem Servermodus erlaubt.  Keine Auswirkung auf die Control Plane oder Data Plane der Cloud - nur die Registry wird gelesen.

* * *

## Beispiel {#example}

Probieren Sie diesen Prompt mit einem beliebigen Elicitation-fähigen MCP-Client aus, bei dem der StackQL-MCP-Server registriert ist:

```
What providers does the StackQL registry offer,  
and which versions of the aws provider are published?
```

Hier ein Beispiel in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.mp4" type="video/mp4" />
</video>
