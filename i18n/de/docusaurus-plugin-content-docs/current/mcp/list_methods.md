---
title: list_methods
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_methods
  - model context protocol
description: MCP-Tool, das die Zugriffsmethoden einer StackQL-Ressource auflistet
image: "/img/stackql-featured-image.png"
---

Gibt die an eine Ressource gebundenen Zugriffsmethoden (HTTP-Operationen) zurück - die verfügbaren Lesemethoden, Mutationen und Lebenszyklusoperationen.  Dieses Tool sollte ein Agent **vor dem Schreiben jeder Abfrage** aufrufen, weil die erforderlichen `WHERE`-Parameter aus der gewählten Lesemethode abgeleitet werden.  Wählen Sie die Methode, deren SQL-Verb der Absicht entspricht und deren erforderliche Parameter sich bereitstellen lassen, und rufen Sie dann [`describe_method`](/mcp/describe_method) dafür auf, um den vollständigen Vertrag einschließlich der Ausgabefelder zu erhalten.

Siehe auch:
[[ MCP-Übersicht ]](/command-line-usage/mcp) [[ `list_resources` ]](/mcp/list_resources) [[ `describe_method` ]](/mcp/describe_method) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## Eingaben {#inputs}

| Argument | Erforderlich | Beschreibung |
|--|--|--|
|`provider`|Ja|Name des Providers.|
|`service`|Ja|Service unter diesem Provider.|
|`resource`|Ja|Ressource unter `provider.service`.|

<br/>

:::info

Bezeichner dürfen keine Whitespace-Zeichen, Semikolons oder Backslashes enthalten; solche Eingaben werden abgewiesen, bevor eine Anweisung erzeugt wird.

:::

* * *

## Ausgabe {#output}

| Renderer | Struktur |
|--|--|
| Tabelle | Eine Zeile pro Zugriffsmethode, einschließlich des zugeordneten SQL-Verbs und der erforderlichen Parameter |

* * *

## Freigabe {#gating}

In jedem Servermodus erlaubt.  Nur lesend.

* * *

## Beispiel {#example}

Probieren Sie diesen Prompt mit einem beliebigen Elicitation-fähigen MCP-Client aus, bei dem der StackQL-MCP-Server registriert ist:

```
For Google Compute instances, what methods are available  
and what parameters are required?
```

Hier ein Beispiel in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.mp4" type="video/mp4" />
</video>
