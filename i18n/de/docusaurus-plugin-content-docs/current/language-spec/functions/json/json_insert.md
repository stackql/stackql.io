---
title: JSON_INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Neue Werte mit SQL in StackQL in ein JSON-Dokument einfügen, ohne vorhandene Daten zu überschreiben.
image: "/img/stackql-featured-image.png"
---
Fügt neue Werte an den angegebenen Pfaden in ein JSON-Dokument ein, ohne vorhandene Werte zu überschreiben.

Siehe auch:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## Syntax {#syntax}

```sql
SELECT JSON_INSERT(json_column, path, new_value) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*json_column*__  
Die zu ändernde JSON-Spalte oder der zu ändernde Ausdruck.

__*path*__  
Der JSON-Pfadausdruck, an dem der neue Wert eingefügt werden soll. Wenn der Pfad bereits vorhanden ist, wird der ursprüngliche Wert an diesem Pfad nicht ersetzt.

__*new_value*__  
Der Wert, der am angegebenen Pfad in das JSON-Dokument eingefügt werden soll.

## Rückgabewert(e) {#return-values}
Gibt das JSON-Dokument mit dem eingefügten neuen Wert zurück, wenn der angegebene Pfad noch nicht vorhanden ist; andernfalls wird das JSON-Dokument unverändert zurückgegeben.

:::note Verhalten, wenn ein Schlüssel vorhanden ist

| Funktion                                                     | Überschreiben, wenn bereits vorhanden? | Anlegen, wenn nicht vorhanden? |
|--------------------------------------------------------------|----------------------------------------|--------------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | Nein                                   | Ja                             |
| [`json_replace()`](/language-spec/functions/json/json_replace) | Ja                                   | Nein                           |
| [`json_set()`](/language-spec/functions/json/json_set)       | Ja                                     | Ja                             |

:::

* * *

## Beispiele {#examples}

### Ein neues Schlüssel-Wert-Paar in ein JSON-Objekt einfügen {#insert-a-new-key-value-pair-into-a-json-object}

```sql
-- Example: Insert a new 'region' key with the value 'Europe' into a JSON object
SELECT JSON_INSERT(json_data, '$.region', 'Europe') as updated_object;

/* expected output...
|-------------------------------------------------------------|
|                     updated_json                            |
|-------------------------------------------------------------|
| {"name":"ResourceOne", "type":"Storage", "region":"Europe"} |
|-------------------------------------------------------------|
*/
```

Diese Abfrage fügt einen neuen Schlüssel namens 'region' mit dem Wert 'Europe' in das JSON-Dokument ein, wenn der Schlüssel 'region' noch nicht vorhanden ist. Wenn 'region' bereits ein Schlüssel im Dokument ist, wird das ursprüngliche Dokument unverändert zurückgegeben.
