---
title: SIGN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Die Funktion SIGN in StackQL: -1, 0 oder 1 zurückgeben, je nachdem, ob ein numerisches Argument negativ, null oder positiv ist."
image: "/img/stackql-featured-image.png"
---
Gibt -1 zurück, wenn der Eingabewert negativ ist, 0, wenn der Eingabewert 0 ist, und 1, wenn der Eingabewert positiv ist.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT SIGN(numeric_expression) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*numeric_expression*__  
Ein Ausdruck aus der Kategorie der exakten oder näherungsweisen numerischen Datentypen.

> Ist *numeric_expression* `NULL` oder ein `string` oder BLOB, der sich nicht verlustfrei in eine Zahl umwandeln lässt, gibt die Funktion `NULL` zurück.

## Rückgabewert(e) {#return-values}

Gibt `-1`, `0` oder `1` zurück.

* * *

## Beispiele {#examples}

### Das Vorzeichen einer Zahl bestimmen {#determine-the-sign-of-a-number}

```sql
SELECT name, sign(strftime('%s', creationTimestamp)-strftime('%s','now')) as sign
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_corefunc.html#sign](https://www.sqlite.org/lang_corefunc.html#sign)
