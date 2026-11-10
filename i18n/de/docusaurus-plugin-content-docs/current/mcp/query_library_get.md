---
title: query_library_get
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_get
  - query library
  - model context protocol
description: MCP-Tool, das eine StackQL-Abfragevorlage abruft, übergebene Parameter validiert und gerendertes SQL ohne Ausführung zurückgibt
image: "/img/stackql-featured-image.png"
---

Ruft einen Eintrag der [Query Library](pathname:///docs/query-library/) anhand einer von [`query_library_search`](/mcp/query_library_search) zurückgegebenen `id` ab.

Wenn `params` fehlt oder leer ist, gibt es die Rohvorlage, die Parameterdeklarationen und die Anmerkungen zurück. Bei einem nicht leeren `params`-Objekt validiert der Server die Werte und setzt sie in die Vorlage ein. SQL wird nur zurückgegeben, wenn das Rendering erfolgreich ist. Das Modell muss die Werte nicht selbst einsetzen.

Dieses Tool führt kein SQL aus und prüft keine Cloud-Anmeldedaten. Seine Parametervalidierung unterscheidet sich von [`validate_select_query`](/mcp/validate_select_query), das ein `SELECT` gegen den installierten Provider parst und plant.

Siehe auch:
[[ MCP-Übersicht ]](/command-line-usage/mcp) [[ `query_library_search` ]](/mcp/query_library_search) [[ `run_select_query` ]](/mcp/run_select_query) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation)

* * *

## Eingaben {#inputs}

| Argument | Erforderlich | Beschreibung |
|--|--|--|
|`id`|Ja|Abfrage-ID aus den Suchergebnissen, zum Beispiel `aws/s3/buckets-list`.|
|`params`|Nein|Objekt, das deklarierte Parameternamen auf Werte abbildet. Lassen Sie es weg oder übergeben Sie `{}`, um die Vorlage ohne Rendering zu inspizieren.|
|`format`|Nein|Textrendering: `markdown` oder `json`. Wenn nicht angegeben, wird die Rendering-Einstellung des Servers verwendet (standardmäßig `markdown`).|

Unbekannte Parameter werden abgelehnt. Erforderliche Parameter ohne übergebene Werte oder Standardwerte werden in `missing_params` gemeldet. Übergebene Werte werden gegen die deklarierten Typen und Einschränkungen geprüft; nicht aufgelöste Platzhalter sind Fehler.

* * *

## Ausgabe {#output}

Die Standard-Textansicht ist ein KV-Block mit Identität, Mutationsstatus, dem nächsten Tool, Gültigkeit, Quell-Links sowie etwaiger Vorlage, gerendertem SQL oder Fehlern. Verwenden Sie die strukturierte Payload oder `format: "json"` für die vollständigen Metadaten:

| Feld | Beschreibung |
|--|--|
|`id`, `title`, `description`|Identität und Zusammenfassung des Eintrags.|
|`mutation`, `verb`|Ob der Eintrag Ressourcen ändert und, sofern deklariert, sein Operationstyp (`select`, `mutation` oder `lifecycle`).|
|`next_tool`|Ausführungstool für den Eintrag: `run_select_query`, `run_mutation_query` oder `run_lifecycle_operation`. Der Abruf ruft es nicht auf.|
|`params`|Parameterdeklarationen mit Namen, Typen, Pflichtkennzeichen sowie etwaigen Standardwerten, Beschreibungen, Beispielen, Enums oder Mustern.|
|`template`, `notes`|Rohvorlage und Anmerkungen des Autors, zurückgegeben, wenn `params` fehlt oder leer ist.|
|`rendered`, `valid`|Ob das Rendering versucht wurde und erfolgreich war. Beide sind `false`, wenn die Rohvorlage inspiziert wird.|
|`sql`|Gerendertes SQL, nur nach erfolgreichem Rendering vorhanden.|
|`missing_params`, `unknown_params`, `errors`|Validierungsdetails, wenn das Rendering fehlschlägt. Führen Sie kein SQL aus, bis diese behoben sind.|
|`auth`, `permissions`|Sofern deklariert, die für die Ausführung benötigten Umgebungsvariablen für Anmeldedaten und providereigenen Berechtigungen. Der Abruf selbst benötigt keines von beiden.|
|`cost`, `outputs`, `related`|Sofern deklariert, Hinweise zu Fan-out und Kosten, Beschreibungen der Ergebnisspalten und IDs verwandter Einträge.|
|`doc_url`|Für Menschen lesbare Seite der Query Library.|
|`source_tier`, `source_url`, `stale`|Herkunft des Inhalts: `primary`, `fallback` oder `snapshot`, dessen URL oder eingebetteter Pfad sowie ob der Inhalt nach einem fehlgeschlagenen Abruf aus dem Cache oder Snapshot bereitgestellt wurde.|

Eine unbekannte oder fehlerhafte ID liefert einen Fehler. Verwenden Sie `query_library_search`, um gültige IDs zu finden.

* * *

## Freigabe {#gating}

In jedem Servermodus erlaubt, vorbehaltlich der konfigurierten Tool-Allowlist. Das Abrufen oder Rendern einer Mutations- oder Lebenszyklusvorlage hat keine cloudseitige Wirkung und benötigt keine Genehmigung. Das Ausführen des zurückgegebenen SQL ist ein separater Aufruf, der dem [Modus](/command-line-usage/mcp#server-modes) des Servers unterliegt.

* * *

## Beispiel {#example}

Inspizieren Sie zunächst einen Eintrag:

```json
{
  "id": "aws/s3/buckets-list"
}
```

Übergeben Sie dann die erforderliche Routing-Region:

```json
{
  "id": "aws/s3/buckets-list",
  "params": {
    "region": "us-east-1"
  }
}
```

Nachdem Sie `rendered` und `valid` geprüft haben, validieren Sie das zurückgegebene `SELECT` mit [`validate_select_query`](/mcp/validate_select_query). Führen Sie es mit dem in `next_tool` genannten Tool aus, übergeben Sie dabei das zurückgegebene `sql` und setzen Sie `source` zur Attribution auf die `id` des Query-Library-Eintrags. Zur Ausführungszeit sind Provider-Anmeldedaten und -Berechtigungen erforderlich.
