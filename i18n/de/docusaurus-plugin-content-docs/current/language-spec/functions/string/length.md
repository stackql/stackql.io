---
title: LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "LENGTH-Funktion in StackQL: die Anzahl der Zeichen in einem String oder die Anzahl der Bytes in einem BLOB."
image: "/img/stackql-featured-image.png"
---
Gibt die Anzahl der Zeichen eines String-Eingabeausdrucks vor dem ersten `NULL`-Zeichen oder die Anzahl der Bytes eines BLOB-Ausdrucks zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) 

* * * 

:::info

Um die Länge eines JSON-Arrays zu ermitteln, verwenden Sie die Funktion [**JSON_ARRAY_LENGTH**](/language-spec/functions/json/json_array_length).

:::

## Syntax {#syntax}

```sql
SELECT LENGTH(expression) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*expression*__  
Ein ASCII-String oder ein BLOB.

> Gibt `NULL` zurück, wenn *expression* `NULL` ist.

> Wenn *expression* numerisch ist, wird die Länge einer String-Darstellung von *expression* zurückgegeben.

## Rückgabewert(e) {#return-values}
Gibt eine Ganzzahl zurück.

* * *

## Beispiele {#examples}

### Die Länge einer String-Spalte zurückgeben {#return-the-length-of-a-string-column}

```sql
SELECT name, length(name) AS len
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_corefunc.html#length](https://www.sqlite.org/lang_corefunc.html#length)
