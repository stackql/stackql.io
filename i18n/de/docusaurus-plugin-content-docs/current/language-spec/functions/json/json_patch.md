---
title: JSON_PATCH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Mit SQL in StackQL einen JSON MergePatch auf ein JSON-Dokument anwenden, um JSON-Objekte zu ändern oder zusammenzuführen.
image: "/img/stackql-featured-image.png"
---
Wendet einen JSON MergePatch (RFC-7396) auf ein JSON-Dokument an und ändert es gemäß den angegebenen Operationen. Diese Funktion dient zum Hinzufügen, Ändern oder Löschen von Elementen in JSON-Objekten, behandelt JSON-Arrays jedoch als atomare Einheiten.

Siehe auch:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Syntax {#syntax}

```sql
SELECT JSON_PATCH(target_json, patch_json) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*target_json*__  
Das JSON-Dokument, auf das der Patch angewendet werden soll.

__*patch_json*__  
Ein JSON-Dokument, das die gemäß dem MergePatch-Algorithmus anzuwendenden Änderungen beschreibt.

## Rückgabewert(e) {#return-values}
Gibt das geänderte JSON-Dokument zurück, nachdem der Patch angewendet wurde.

* * *

## Beispiele {#examples}

### Einfacher MergePatch {#simple-mergepatch}

```sql
-- Example: Simple merge of new elements into the target JSON object
SELECT JSON_PATCH('{"a":1,"b":2}', '{"c":3,"d":4}') AS patched_json;
```

Ausgabe: `{"a":1,"b":2,"c":3,"d":4}`

### Element in einem Array ersetzen {#replace-element-in-array}

```sql
-- Example: Replacing an array with a single value in the JSON object
SELECT JSON_PATCH('{"a":[1,2],"b":2}', '{"a":9}') AS patched_json;
```

Ausgabe: `{"a":9,"b":2}`

### Element mit null entfernen {#remove-element-using-null}

```sql
-- Example: Removing an element by setting it to null
SELECT JSON_PATCH('{"a":1,"b":2}', '{"a":9,"b":null,"c":8}') AS patched_json;
```

Ausgabe: `{"a":9,"c":8}`

### Verschachtelter MergePatch {#nested-mergepatch}

```sql
-- Example: Nested merge patch updating sub-keys within a nested JSON object
SELECT JSON_PATCH('{"a":{"x":1,"y":2},"b":3}', '{"a":{"y":9},"c":8}') AS patched_json;
```

Ausgabe: `{"a":{"x":1,"y":9},"b":3,"c":8}`

Diese Funktionalität ermöglicht flexible und dynamische Aktualisierungen von in einer Datenbank gespeichertem JSON und ist besonders nützlich für Anwendungen, die bedingte Änderungen von Datenstrukturen erfordern.
