---
title: list_services
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_services
  - model context protocol
description: MCP-Tool, das die Services unter einem StackQL-Provider auflistet
image: "/img/stackql-featured-image.png"
---

Gibt die von einem einzelnen Provider bereitgestellten Services zurück - die zweite Ebene der [Ressourcenhierarchie](/getting-started/resource-hierarchy) von StackQL.  Rufen Sie dieses Tool nach [`list_providers`](/mcp/list_providers) auf, um den Umfang einzugrenzen, bevor Sie zu den einzelnen Ressourcen übergehen.

Siehe auch:
[[ MCP-Übersicht ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_resources` ]](/mcp/list_resources)

* * *

## Eingaben {#inputs}

| Argument | Erforderlich | Beschreibung |
|--|--|--|
|`provider`|Ja|Name des Providers (z. B. `google`, `aws`, `azure`, `github`).|

<br/>

:::info

Bezeichner dürfen keine Whitespace-Zeichen, Semikolons oder Backslashes enthalten; solche Eingaben werden abgewiesen, bevor eine Anweisung erzeugt wird.

:::

* * *

## Ausgabe {#output}

| Renderer | Struktur |
|--|--|
| Tabelle | Eine Zeile pro Service unter dem angegebenen Provider |

* * *

## Freigabe {#gating}

In jedem Servermodus erlaubt.  Nur lesend.

* * *

## Beispiel {#example}

Probieren Sie diesen Prompt mit einem beliebigen Elicitation-fähigen MCP-Client aus, bei dem der StackQL-MCP-Server registriert ist:

```
List the services available in Google Cloud
```

Hier ein Beispiel in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_services_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_services_web.mp4" type="video/mp4" />
</video>
