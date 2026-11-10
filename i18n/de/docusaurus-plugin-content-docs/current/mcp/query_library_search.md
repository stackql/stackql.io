---
title: query_library_search
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_search
  - query library
  - model context protocol
description: MCP-Tool, das kuratierte StackQL-Abfragevorlagen nach natürlichsprachlicher Absicht, Provider, Service und Tags durchsucht
image: "/img/stackql-featured-image.png"
---

Durchsucht die [StackQL Query Library](pathname:///docs/query-library/) nach Vorlagen, die zu einer natürlichsprachlichen Absicht passen. Konsultieren Sie es, bevor Sie SQL von Grund auf schreiben, und übergeben Sie dann die `id` eines Treffers an [`query_library_get`](/mcp/query_library_get), um die Vorlage zu inspizieren und ihre Parameter zu validieren.

Das Tool ruft ausschließlich Inhalte der Query Library ab. Es führt kein SQL aus, ruft keine Cloud-APIs auf und benötigt keine Provider-Anmeldedaten.

Siehe auch:
[[ MCP-Übersicht ]](/command-line-usage/mcp) [[ `query_library_get` ]](/mcp/query_library_get) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## Eingaben {#inputs}

| Argument | Erforderlich | Beschreibung |
|--|--|--|
|`intent`|Ja|Natürlichsprachliche Beschreibung der Aufgabe.|
|`provider`|Nein|Filter nach Provider, zum Beispiel `aws`.|
|`service`|Nein|Filter nach Service, zum Beispiel `s3`.|
|`tags`|Nein|Array von Tags. Ein Eintrag muss zu jedem übergebenen Tag passen.|
|`include_mutations`|Nein|Mutationsvorlagen einschließen. Standardmäßig `false`; im Modus `read_only` ignoriert, in dem Mutationseinträge immer ausgeschlossen sind.|
|`limit`|Nein|Maximale Anzahl der Treffer. Standardmäßig `5`, wenn nicht angegeben oder nicht positiv; begrenzt auf `20`.|
|`format`|Nein|Textrendering: `markdown` oder `json`. Wenn nicht angegeben, wird die Rendering-Einstellung des Servers verwendet (standardmäßig `markdown`).|

* * *

## Ausgabe {#output}

Die Standard-Textansicht ist eine nach Rang sortierte Tabelle mit `id`, `title`, `mutation`, `required_params` und `score`, gefolgt von der Quellenangabe und etwaigen Hinweisen bei Fehltreffern. Die strukturierte Payload enthält:

| Feld | Beschreibung |
|--|--|
|`hits`|Nach Rang sortierte Einträge mit `id`, `title`, optionaler `description`, `providers`, `services` und `required_params` sowie `mutation` und `score`. Rufen Sie anschließend `query_library_get` für die vollständigen Parameterdeklarationen auf.|
|`miss`|`true`, wenn es keine Treffer gibt oder der höchste Score unter der Übereinstimmungsschwelle liegt. Nächstliegende Treffer mit niedrigem Score können dennoch zurückgegeben werden.|
|`guidance`|Verweist den Agenten bei einem Fehltreffer auf die Server-Instruktionen, die Discovery-Tools und `validate_select_query`, um eine Abfrage zu schreiben.|
|`source_tier`|Quelle des Inhalts: `primary`, `fallback` oder `snapshot`.|
|`source_url`|URL oder eingebetteter Pfad des verwendeten Katalogs, zur Quellenangabe.|
|`stale`|Bei `true` wurde der Inhalt nach einem fehlgeschlagenen Abruf aus dem Cache oder dem eingebetteten Snapshot bereitgestellt.|

Veraltete Einträge werden ausgeschlossen. Der Server cacht den Katalog und kann von der gehosteten Query Library auf ihre GitHub-Quelle und einen eingebetteten Snapshot zurückfallen.

* * *

## Freigabe {#gating}

In jedem Servermodus erlaubt, vorbehaltlich der konfigurierten Tool-Allowlist. Es sind weder Genehmigung noch Provider-Anmeldedaten erforderlich. Die Suche nach Mutationsvorlagen autorisiert nicht deren Ausführung.

* * *

## Beispiel {#example}

Tool-Argumente:

```json
{
  "intent": "List S3 bucket names and regions",
  "provider": "aws",
  "service": "s3",
  "limit": 5
}
```

Verwenden Sie eine `id` aus `hits`, etwa `aws/s3/buckets-list`, mit [`query_library_get`](/mcp/query_library_get). Wenn `miss` gleich `true` ist, inspizieren Sie die nächstliegenden Vorlagen zur Orientierung oder verwenden Sie [`list_methods`](/mcp/list_methods) und [`describe_method`](/mcp/describe_method), bevor Sie SQL schreiben.
