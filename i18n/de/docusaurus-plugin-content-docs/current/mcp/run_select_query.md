---
title: run_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_select_query
  - model context protocol
description: MCP-Tool, das ein StackQL-SELECT gegen einen Provider ausführt
image: "/img/stackql-featured-image.png"
---

Führt eine `SELECT`-Anweisung gegen die konfigurierten Provider aus und gibt die Ergebniszeilen zurück.  Nur lesend - Mutationen und Lebenszyklusoperationen werden nicht akzeptiert; verwenden Sie dafür [`run_mutation_query`](/mcp/run_mutation_query) oder [`run_lifecycle_operation`](/mcp/run_lifecycle_operation).

Ergebniszeilen werden an den Client zurückgegeben, aber bewusst aus dem Audit-Log ausgeschlossen.

Siehe auch:
[[ MCP-Übersicht ]](/command-line-usage/mcp) [[ `validate_select_query` ]](/mcp/validate_select_query) [[ `list_methods` ]](/mcp/list_methods) [[ SELECT-Sprachspezifikation ]](/language-spec/select)

* * *

## Eingaben {#inputs}

| Argument | Erforderlich | Beschreibung |
|--|--|--|
|`sql`|Ja|Eine `SELECT`-Anweisung.  Metadaten-Anweisungen (`SHOW`, `DESCRIBE`, `DESC`, `EXPLAIN`) werden vom Policy-Gate als nur lesend eingestuft und ebenfalls akzeptiert; `DESC` wird in StackQL-Releases ab `v0.12.732` erkannt.|
|`row_limit`|Nein|Obergrenze für die Anzahl der an den Client zurückgegebenen Zeilen.  `0` (Standard) bedeutet keine clientseitige Obergrenze.|
|`source`|Nein|ID des Query-Library-Eintrags, wenn das SQL von [`query_library_get`](/mcp/query_library_get) stammt. Nur zur Attribution; ändert weder Ausführung noch Genehmigungen.|
|`format`|Nein|Textrendering: `markdown` oder `json`. Wenn nicht angegeben, wird die Rendering-Einstellung des Servers verwendet.|

* * *

## Ausgabe {#output}

| Renderer | Struktur |
|--|--|
| Tabelle | `{rows: [...]}` - ein Objekt pro Zeile, mit Spaltennamen als Schlüssel |

* * *

## Freigabe {#gating}

In jedem Servermodus erlaubt (`read_only`, `safe`, `delete_safe`, `full_access`), wenn jede Anweisung in der Payload nur lesend ist.  Ab `v1.0` werden `INSERT`, `UPDATE`, `REPLACE`, `DELETE` und `EXEC` von diesem Tool in jedem Modus abgelehnt, auch in `full_access`, mit einem Fehler, der [`run_mutation_query`](/mcp/run_mutation_query) oder [`run_lifecycle_operation`](/mcp/run_lifecycle_operation) nennt.  Eine Payload mit mehreren Anweisungen wird nach ihrer privilegiertesten Anweisung eingestuft, sodass eine Mutation hinter einem vorangestellten `SELECT` auf dieselbe Weise abgelehnt wird.  Das Tool deklariert `destructiveHint: false` und `openWorldHint: true`.

* * *

## Beispiel {#example}

Probieren Sie diesen Prompt mit einem beliebigen Elicitation-fähigen MCP-Client aus, bei dem der StackQL-MCP-Server registriert ist:

```
List the VPC networks in my Google Cloud project my-project  
and tell me which ones use auto subnet creation.
```

Hier ein Beispiel in Claude Desktop:

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.mp4" type="video/mp4" />
</video>
