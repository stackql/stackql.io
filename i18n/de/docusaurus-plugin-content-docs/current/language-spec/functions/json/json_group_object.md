---
title: JSON_GROUP_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Schlüssel-Wert-Paare mit SQL in StackQL zu einem JSON-Objekt aggregieren.
image: "/img/stackql-featured-image.png"
---
Aggregiert Schlüssel-Wert-Paare zu einem JSON-Objekt, gruppiert nach einer angegebenen Spalte. `JSON_GROUP_OBJECT()` sammelt alle Schlüssel-Wert-Paare jeder Gruppe in den angegebenen Spalten und fasst sie für jede Gruppe zu einem einzigen JSON-Objekt zusammen.

Siehe auch:  
[[`SELECT`]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT JSON_GROUP_OBJECT(key_column, value_column) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## Argumente {#arguments}

__*key_column*__  
Die Spalte, die die Schlüssel für die JSON-Objekte liefert.

__*value_column*__  
Die Spalte, die die Werte für die JSON-Objekte liefert.

__*grouping_column*__  
Die Spalte, nach der die Ergebnisse gruppiert werden.

## Rückgabewert(e) {#return-values}
Gibt ein JSON-Objekt zurück, das alle aggregierten Schlüssel-Wert-Paare aus den angegebenen Spalten enthält, gruppiert nach der `grouping_column`.

* * *

## Beispiele {#examples}

### Repository-Größen nach Programmiersprache aggregieren {#aggregate-repository-sizes-by-programming-language}

```sql
-- Example: Group repository names with their corresponding sizes by programming language
SELECT
  language, 
  JSON_GROUP_OBJECT(name, size) AS language_index
FROM
  github.repos.repos
WHERE
  org = 'stackql'
  AND language IS NOT NULL
GROUP BY language;
```

Diese Abfrage gibt für jede Programmiersprache ein JSON-Objekt zurück, wobei jedes Objekt Repository-Namen und ihre jeweiligen Größen als Schlüssel-Wert-Paare enthält. Hier ist ein Auszug der erwarteten Ausgabe:

```plaintext
|-------------|-----------------------------------------------------------------------------------|
| language    | language_index                                                                    |
|-------------|-----------------------------------------------------------------------------------|
| C           | {"go-sqlite3":35667,"data-encryption-spike":50746}                                |
|-------------|-----------------------------------------------------------------------------------|
| C++         | {"expt-any-sdk":2399,"isqlang":9}                                                 |
|-------------|-----------------------------------------------------------------------------------|
| Dockerfile  | {"stackql-compliance-checks-devel":8}                                             |
|-------------|-----------------------------------------------------------------------------------|
| Go          | {"stackql-provider-registry":115711, ...}                                         |
|-------------|-----------------------------------------------------------------------------------|
```

Diese Ausgabe ist nützlich, um die Verteilung der Repository-Größen über verschiedene Programmiersprachen innerhalb einer Organisation zu visualisieren, und liefert übersichtliche und geordnete, nach Sprache gruppierte Daten.
