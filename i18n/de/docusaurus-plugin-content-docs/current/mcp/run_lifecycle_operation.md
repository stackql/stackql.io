---
title: run_lifecycle_operation
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_lifecycle_operation
  - model context protocol
description: MCP-Tool, das eine StackQL-EXEC-Lebenszyklusoperation ausführt
image: "/img/stackql-featured-image.png"
---

Führt eine StackQL-`EXEC`-Lebenszyklusoperation aus - die Nicht-CRUD-Aktionen eines Providers (VM starten/stoppen, Schlüssel rotieren, Workflow auslösen usw.).  **Reale Seiteneffekte.**

Unterliegt dem Gating durch den [Modus](/command-line-usage/mcp#server-modes) des Servers:

| Modus | Lebenszyklus-`EXEC` |
|--|--|
|`read_only`|abgelehnt|
|`safe` (Standard)|erfordert Genehmigung (Elicitation)|
|`delete_safe`|erfordert Genehmigung|
|`full_access`|erlaubt|

<br/>

:::info

Eine Payload mit mehreren Anweisungen unterliegt dem Gating ihrer privilegiertesten Anweisung

:::

Wenn eine Genehmigung erforderlich ist und der Client Elicitation nicht angekündigt hat, wird der Aufruf abgelehnt.

Siehe auch:
[[ MCP-Übersicht ]](/command-line-usage/mcp) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `describe_method` ]](/mcp/describe_method) [[ EXEC-Sprachspezifikation ]](/language-spec/exec)

* * *

## Eingaben {#inputs}

| Argument | Erforderlich | Beschreibung |
|--|--|--|
|`sql`|Ja|Eine StackQL-`EXEC`-Anweisung, die auf eine Lebenszyklusmethode abzielt.|
|`source`|Nein|ID des Query-Library-Eintrags, wenn das SQL von [`query_library_get`](/mcp/query_library_get) stammt. Nur zur Attribution; ändert weder Ausführung noch Genehmigungen.|
|`format`|Nein|Textrendering: `markdown` oder `json`. Wenn nicht angegeben, wird die Rendering-Einstellung des Servers verwendet.|

* * *

## Ausgabe {#output}

| Renderer | Struktur |
|--|--|
| KV | `{messages: [...], timestamp}` |

* * *

## Beispiel {#example}

Probieren Sie diesen Prompt mit einem beliebigen Elicitation-fähigen MCP-Client aus, bei dem der StackQL-MCP-Server registriert ist:

```
The Compute instance web-01 in us-central1-a  
(project my-project) is stuck.  Please stop it.
```

Der MCP-Client zeigt eine Genehmigungsaufforderung an, bevor die Operation abgesetzt wird, es sei denn, der Server läuft im Modus `full_access`.

Hier ein Beispiel in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.mp4" type="video/mp4" />
</video>
