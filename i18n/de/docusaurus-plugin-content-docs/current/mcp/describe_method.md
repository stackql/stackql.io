---
title: describe_method
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - describe_method
  - model context protocol
description: MCP-Tool, das den vollständigen I/O-Vertrag einer StackQL-Zugriffsmethode zurückgibt
image: "/img/stackql-featured-image.png"
---

Gibt den vollständigen I/O-Vertrag einer einzelnen Zugriffsmethode zurück - immer EXTENDED, also mit Eingaben, Request-Body-Schema und vollständiger Ausgabestruktur.  Hier erhält ein Agent die Spaltennamen: Jede Zeile trägt einen `param_type` von `input_required` (ein verpflichtendes `WHERE`-Prädikat mit exakter Übereinstimmung), `input_optional` oder `output` (ein Feld, auf das ein `SELECT` oder `RETURNING` verweisen kann).  Eine Ressource hat keine einzelne Feldliste, weil jede Methode ihre eigene Struktur zurückgibt; rufen Sie dieses Tool daher für die aus [`list_methods`](/mcp/list_methods) gewählte Methode auf, bevor Sie die erste Abfrage dagegen ausführen.

Siehe auch:
[[ MCP-Übersicht ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## Eingaben {#inputs}

| Argument | Erforderlich | Beschreibung |
|--|--|--|
|`provider`|Ja|Name des Providers.|
|`service`|Ja|Service unter diesem Provider.|
|`resource`|Ja|Ressource unter `provider.service`.|
|`method`|Ja|Name der Methode, wie von [`list_methods`](/mcp/list_methods) zurückgegeben.|

<br/>

:::info

Bezeichner dürfen keine Whitespace-Zeichen, Semikolons oder Backslashes enthalten; solche Eingaben werden abgewiesen, bevor eine Anweisung erzeugt wird.

:::

* * *

## Ausgabe {#output}

| Renderer | Struktur |
|--|--|
| KV | Eine Zeile pro Eingabe- und Ausgabefeld: Name, Typ, `param_type` (`input_required`, `input_optional` oder `output`), Struktur und Beschreibung |

* * *

## Freigabe {#gating}

In jedem Servermodus erlaubt.  Nur lesend.

* * *

## Beispiel {#example}

Probieren Sie diesen Prompt mit einem beliebigen Elicitation-fähigen MCP-Client aus, bei dem der StackQL-MCP-Server registriert ist:

```
Show me everything I need to know to call the insert method  
on Google Compute networks -- required parameters,  
optional parameters, and the request body shape.
```

Hier ein Beispiel in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.mp4" type="video/mp4" />
</video>
