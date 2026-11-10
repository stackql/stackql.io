---
title: validate_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - validate_select_query
  - model context protocol
description: MCP-Tool, das ein StackQL-SELECT parst und plant, ohne es auszuführen
image: "/img/stackql-featured-image.png"
---

Parst und plant eine einzelne `SELECT`-Anweisung, ohne sie auszuführen.  Gibt `{valid, errors}` zurück.  Nützlich, wenn ein Agent prüfen möchte, ob seine Abfrage kompiliert und an die ermittelte Methode gebunden wird, bevor er sich auf einen echten API-Aufruf festlegt.

:::info

Nur `SELECT` und genau eine Anweisung; eine Payload mit mehr als einer Anweisung wird mit `validate_select_query accepts exactly one statement` abgelehnt.

:::

Siehe auch:
[[ MCP-Übersicht ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## Eingaben {#inputs}

| Argument | Erforderlich | Beschreibung |
|--|--|--|
|`sql`|Ja|Eine einzelne `SELECT`-Anweisung.|

* * *

## Ausgabe {#output}

| Renderer | Struktur |
|--|--|
| KV | `valid` (bool) und `errors` (Array von Strings, leer wenn gültig) |

* * *

## Freigabe {#gating}

In jedem Servermodus erlaubt.  Es werden keine API-Aufrufe durchgeführt.

* * *

## Beispiel {#example}

Probieren Sie diesen Prompt mit einem beliebigen Elicitation-fähigen MCP-Client aus, bei dem der StackQL-MCP-Server registriert ist:

```
Can you validate this query before I run it:

select name, network  
from google.compute.subnetworks  
where project = 'my-project' and region = 'us-central1';
```

Hier ein Beispiel in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.mp4" type="video/mp4" />
</video>
