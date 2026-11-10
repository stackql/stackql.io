---
title: run_mutation_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_mutation_query
  - model context protocol
description: MCP-Tool, das INSERT, UPDATE, REPLACE oder DELETE über StackQL gegen einen Provider ausführt
image: "/img/stackql-featured-image.png"
---

Führt `INSERT`, `UPDATE`, `REPLACE` oder `DELETE` gegen den Provider aus.  **Reale Seiteneffekte** - ein erfolgreicher Aufruf ändert den Cloud-Zustand.

Unterliegt dem Gating durch den [Modus](/command-line-usage/mcp#server-modes) des Servers:

| Modus | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` |
|--|--|--|
|`read_only`|abgelehnt|abgelehnt|
|`safe` (Standard)|erfordert Genehmigung (Elicitation)|erfordert Genehmigung|
|`delete_safe`|erlaubt|erfordert Genehmigung|
|`full_access`|erlaubt|erlaubt|

<br/>

:::info

Eine Payload mit mehreren Anweisungen unterliegt dem Gating ihrer privilegiertesten Anweisung

:::

Wenn eine Mutation eine Genehmigung erfordert und der Client die MCP-Elicitation-Fähigkeit nicht angekündigt hat, wird der Aufruf abgelehnt.

Siehe auch:
[[ MCP-Übersicht ]](/command-line-usage/mcp) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation) [[ `describe_method` ]](/mcp/describe_method) [[ INSERT-Sprachspezifikation ]](/language-spec/insert) [[ DELETE-Sprachspezifikation ]](/language-spec/delete)

* * *

## Eingaben {#inputs}

| Argument | Erforderlich | Beschreibung |
|--|--|--|
|`sql`|Ja|Eine `INSERT`-, `UPDATE`-, `REPLACE`- oder `DELETE`-Anweisung.|
|`source`|Nein|ID des Query-Library-Eintrags, wenn das SQL von [`query_library_get`](/mcp/query_library_get) stammt. Nur zur Attribution; ändert weder Ausführung noch Genehmigungen.|
|`format`|Nein|Textrendering: `markdown` oder `json`. Wenn nicht angegeben, wird die Rendering-Einstellung des Servers verwendet.|

* * *

## Ausgabe {#output}

| Renderer | Struktur |
|--|--|
| KV | `{messages: [...], timestamp}` - Bestätigung des Absendens, nicht der Antworttext der API |

* * *

## Beispiel {#example}

Probieren Sie diesen Prompt mit einem beliebigen Elicitation-fähigen MCP-Client aus, bei dem der StackQL-MCP-Server registriert ist:

```
The firewall rule allow-test-old in project my-project  
is no longer needed.  Please remove it.
```

Der MCP-Client zeigt eine Genehmigungsaufforderung an, bevor das Löschen abgesetzt wird, es sei denn, der Server läuft im Modus `full_access`.

Der folgende Clip zeigt den Elicitation-Ablauf, wenn der StackQL-MCP-Server im Standardmodus `safe` läuft - der Client wird aufgefordert, das `DELETE` zu genehmigen, bevor eine cloudseitige Änderung abgesetzt wird.  Sie können dies gegen Ihren eigenen StackQL-MCP-Server mit dem offiziellen [MCP Inspector](https://github.com/modelcontextprotocol/inspector) über `npx @modelcontextprotocol/inspector` nachvollziehen:


<video autoPlay loop muted playsInline style={{width: 'calc(100% - 4rem)', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.webm" type="video/webm" />
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.mp4" type="video/mp4" />
</video>
