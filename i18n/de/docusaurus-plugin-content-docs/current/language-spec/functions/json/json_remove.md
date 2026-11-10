---
title: JSON_REMOVE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: Elemente mit SQL in StackQL durch Angabe von Pfaden aus einem JSON-Dokument entfernen.
image: "/img/stackql-featured-image.png"
---
Entfernt angegebene Elemente mithilfe von Pfadausdrücken aus einem JSON-Dokument. Wenn ein angegebener Pfad im Dokument nicht vorhanden ist, wird er ignoriert. Die sequenzielle Verarbeitung der Pfade kann die Struktur für nachfolgende Entfernungen beeinflussen.

Siehe auch:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Syntax {#syntax}

```sql
SELECT JSON_REMOVE(json_document, path1, path2, ...) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*json_document*__  
Das JSON-Dokument, aus dem Elemente entfernt werden sollen.

__*path1, path2, ...*__  
Ein oder mehrere Pfadausdrücke, die auf die zu entfernenden Elemente im JSON-Dokument verweisen.

## Rückgabewert(e) {#return-values}
Gibt eine Kopie des JSON-Dokuments ohne die angegebenen Elemente zurück. Wenn keine Pfade angegeben werden, wird das Eingabe-JSON neu formatiert und ohne überflüssigen Leerraum zurückgegeben.

* * *

## Beispiele {#examples}

### Ein Element aus einem JSON-Array entfernen {#remove-an-element-from-a-json-array}

```sql
-- Example: Remove the third element from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]') AS modified_json;
```

Ausgabe: `'[0,1,3,4]'`

### Mehrere Elemente aus einem JSON-Array entfernen {#remove-multiple-elements-from-a-json-array}

```sql
-- Example: Remove the first and third elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]', '$[0]') AS modified_json;
```

Ausgabe: `'[1,3,4]'`

### Letztes Element mit negativem Index entfernen {#remove-last-element-using-negative-index}

```sql
-- Example: Remove the last and first elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[#-1]', '$[0]') AS modified_json;
```

Ausgabe: `'[1,2,3]'`

### Nicht vorhandenen Pfad entfernen {#remove-non-existent-path}

```sql
-- Example: Attempt to remove a non-existent key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.z') AS modified_json;
```

Ausgabe: `'{"x":25,"y":42}'`

### Vorhandenen Schlüssel aus einem JSON-Objekt entfernen {#remove-existing-key-from-json-object}

```sql
-- Example: Remove the 'y' key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.y') AS modified_json;
```

Ausgabe: `'{"x":25}'`

### Grenzfall: Entfernen mit dem Root-Pfad {#edge-case-removal-with-root-path}

```sql
-- Example: Attempt to remove using the root path
SELECT JSON_REMOVE('{"x":25,"y":42}', '$') AS modified_json;
```

Ausgabe: `NULL`

Diese Beispiele zeigen, wie `json_remove()` zur dynamischen Bearbeitung von JSON-Daten verwendet werden kann, von einfachen Löschungen bis hin zur Behandlung von Grenzfällen bei Änderungen an Datenstrukturen.
