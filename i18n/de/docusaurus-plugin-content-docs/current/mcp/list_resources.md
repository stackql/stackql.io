---
title: list_resources
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_resources
  - model context protocol
description: MCP-Tool, das die Ressourcen unter einem provider.service in StackQL auflistet
image: "/img/stackql-featured-image.png"
---

Gibt die Ressourcen unter einem `provider.service` zurück - die dritte Ebene der [Ressourcenhierarchie](/getting-started/resource-hierarchy) von StackQL.  Ressourcen sind die Ziele, die in der `FROM`-Klausel einer StackQL-Abfrage erscheinen.

Siehe auch:
[[ MCP-Übersicht ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_methods` ]](/mcp/list_methods) [[ `describe_method` ]](/mcp/describe_method)

* * *

## Eingaben {#inputs}

| Argument | Erforderlich | Beschreibung |
|--|--|--|
|`provider`|Ja|Name des Providers.|
|`service`|Ja|Service unter diesem Provider.|

<br/>

:::info

Bezeichner dürfen keine Whitespace-Zeichen, Semikolons oder Backslashes enthalten; solche Eingaben werden abgewiesen, bevor eine Anweisung erzeugt wird.

:::

* * *

## Ausgabe {#output}

| Renderer | Struktur |
|--|--|
| Tabelle | Eine Zeile pro Ressource unter `provider.service` |

* * *

## Freigabe {#gating}

In jedem Servermodus erlaubt.  Nur lesend.

* * *

## Beispiel {#example}

Probieren Sie diesen Prompt mit einem beliebigen Elicitation-fähigen MCP-Client aus, bei dem der StackQL-MCP-Server registriert ist:

```
List the resources in the google.storage service
```

Hier ein Beispiel in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.mp4" type="video/mp4" />
</video>
