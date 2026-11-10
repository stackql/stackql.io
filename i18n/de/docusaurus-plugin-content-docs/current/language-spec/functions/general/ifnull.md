---
title: IFNULL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "IFNULL-Funktion in StackQL: gibt das erste von genau zwei Argumenten zurück, das nicht NULL ist, eine Form von COALESCE mit zwei Argumenten."
image: "/img/stackql-featured-image.png"
---
Akzeptiert genau zwei Argumente und gibt das erste Nicht-`NULL`-Argument zurück; kann verwendet werden, um einen `NULL`-Wert durch einen Standardwert zu ersetzen.  

> Die Funktion `IFNULL` entspricht der Funktion [`COALESCE`](/language-spec/functions/general/coalesce) mit nur zwei Argumenten; die Funktion [`COALESCE`](/language-spec/functions/general/coalesce) kann mehr als zwei Argumente haben.

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## Syntax {#syntax}

```sql
SELECT IFNULL(expression1, expression2) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*expression*__  
Zwei Ausdrücke eines beliebigen Datentyps, die zu einem `NULL`- oder Nicht-`NULL`-Wert ausgewertet werden.

> Wenn beide *expression*-Argumente `NULL` sind, gibt die Funktion `IFNULL` `NULL` zurück.


## Rückgabewert(e) {#return-values}

Gibt den ersten Nicht-`NULL`-Wert der beiden *expression*-Argumente zurück. 

* * *

## Beispiele {#examples}

### Einen `NULL`-Wert durch einen Standardwert ersetzen {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, ifnull(website, 'Not hosting a website') AS website 
FROM google.storage.buckets where project = 'stackql';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_corefunc.html#ifnull](https://www.sqlite.org/lang_corefunc.html#ifnull)
