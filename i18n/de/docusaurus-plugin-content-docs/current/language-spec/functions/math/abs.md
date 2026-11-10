---
title: ABS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Die Funktion ABS in StackQL: der Absolutwert eines numerischen Arguments."
image: "/img/stackql-featured-image.png"
---
Gibt den Absolutwert des numerischen Arguments zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
SELECT ABS(numeric_expression) FROM <multipartIdentifier>;
```

> `abs(X)` gibt `NULL` zurück, wenn `X` gleich `NULL` ist. `abs(X)` gibt `0.0` zurück, wenn `X` eine Zeichenkette oder ein BLOB ist, die sich nicht in einen numerischen Wert umwandeln lassen. Ist `X` die Ganzzahl -9223372036854775808, löst `abs(X)` einen Integer-Überlauffehler aus, da es keinen entsprechenden positiven 64-Bit-Zweierkomplementwert gibt.

## Argumente {#arguments}

__*numeric_expression*__  
Ein Ausdruck aus der Kategorie der exakten oder näherungsweisen numerischen Datentypen.

## Rückgabewert(e) {#return-values}

Gibt einen positiven numerischen Wert zurück.

* * *

## Beispiele {#examples}

### Den Absolutwert einer Zahl zurückgeben {#return-the-absolute-value-of-a-number}

```sql
SELECT name, abs(strftime('%s', creationTimestamp)-strftime('%s','now')) as abs_value
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_mathfunc.html](https://www.sqlite.org/lang_mathfunc.html)
