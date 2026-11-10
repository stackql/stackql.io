---
title: JSON_SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Werte in einem JSON-Dokument mit SQL in StackQL aktualisieren oder einfügen.
image: "/img/stackql-featured-image.png"
---
Aktualisiert vorhandene Werte oder fügt neue Werte an den angegebenen Pfaden in ein JSON-Dokument ein.

Siehe auch:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## Syntax {#syntax}

```sql
SELECT JSON_SET(json_column, path, value_to_set) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*json_column*__  
Die zu ändernde JSON-Spalte oder der zu ändernde Ausdruck.

__*path*__  
Der JSON-Pfadausdruck, der angibt, wo der Wert gesetzt werden soll. Wenn der Pfad vorhanden ist, wird der Wert aktualisiert; wenn der Pfad nicht vorhanden ist, wird ein neues Schlüssel-Wert-Paar hinzugefügt.

__*value_to_set*__  
Der Wert, der am angegebenen Pfad im JSON-Dokument gesetzt werden soll.

## Rückgabewert(e) {#return-values}
Gibt ein JSON-Dokument zurück, in dem der Wert am angegebenen Pfad gesetzt ist. Wenn der Pfad vorhanden ist, wird der vorhandene Wert aktualisiert; andernfalls wird ein neues Schlüssel-Wert-Paar hinzugefügt.

:::note Verhalten, wenn ein Schlüssel vorhanden ist

| Funktion                                                     | Überschreiben, wenn bereits vorhanden? | Anlegen, wenn nicht vorhanden? |
|--------------------------------------------------------------|----------------------------------------|--------------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | Nein                                   | Ja                             |
| [`json_replace()`](/language-spec/functions/json/json_replace) | Ja                                   | Nein                           |
| [`json_set()`](/language-spec/functions/json/json_set)       | Ja                                     | Ja                             |

:::

* * *

## Beispiele {#examples}

### Einen vorhandenen Wert in einem JSON-Objekt aktualisieren {#update-an-existing-value-in-a-json-object}

```sql
-- Example: Update the 'status' key's value to 'active' in a JSON object
SELECT JSON_SET(json_data, '$.status', 'active') as updated_json
FROM cloud_services
WHERE service_id = 'service123';

-- Expected output:
-- |---------------------------------------------------------|
-- |                    updated_json                         |
-- |---------------------------------------------------------|
-- | {"name":"ServiceOne", "status":"active", "region":"US"} |
-- |---------------------------------------------------------|
```

Diese Abfrage aktualisiert den Schlüssel 'status' im JSON-Dokument auf 'active', wenn der Schlüssel 'status' vorhanden ist. Wenn 'status' im Dokument nicht vorhanden ist, wird er mit dem Wert 'active' hinzugefügt.

### Ein neues Schlüssel-Wert-Paar zu einem JSON-Objekt hinzufügen {#add-a-new-key-value-pair-to-a-json-object}

```sql
-- Example: Add a new key 'lastChecked' with the current date to a JSON object
SELECT JSON_SET(json_data, '$.lastChecked', '2024-04-29') as updated_json
FROM cloud_services
WHERE service_id = 'service123';

/* expected output...
|-----------------------------------------------------------------------|
|                          updated_json                                 |
|-----------------------------------------------------------------------|
| {"name":"ServiceOne", "status":"pending", "lastChecked":"2024-04-29"} |
|-----------------------------------------------------------------------|
*/
```

Diese Abfrage fügt dem JSON-Dokument einen neuen Schlüssel 'lastChecked' mit dem Wert '2024-04-29' hinzu, wenn 'lastChecked' noch nicht vorhanden ist. Ist er bereits vorhanden, wird der vorhandene Wert auf '2024-04-29' aktualisiert.
