---
title: COALESCE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "COALESCE-Funktion in StackQL: gibt von zwei oder mehr Argumenten das erste zurück, das nicht NULL ist, meist als Standardwert für einen fehlenden Wert."
image: "/img/stackql-featured-image.png"
---
Gibt aus einer Reihe von zwei oder mehr Argumenten das erste Argument zurück, das nicht `NULL` ist; kann verwendet werden, um einen `NULL`-Wert durch einen Standardwert zu ersetzen.  

> Die Funktion `COALESCE` entspricht der Funktion `NVL` in Oracle oder der Funktion `IFNULL` in MySQL.

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[` IFNULL `]](/language-spec/functions/general/ifnull) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## Syntax {#syntax}

```sql
SELECT COALESCE(expression1, expression2 [, ..]) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*expression*__  
Zwei oder mehr Ausdrücke eines beliebigen Datentyps, die zu einem `NULL`- oder Nicht-`NULL`-Wert ausgewertet werden.

> Wenn alle *expression*-Argumente `NULL` sind, gibt die Funktion `COALESCE` `NULL` zurück.

## Rückgabewert(e) {#return-values}

Gibt den ersten Nicht-`NULL`-Wert aus der Reihe der *expression*-Argumente zurück.

* * *

## Beispiele {#examples}

### Einen `NULL`-Wert durch einen Standardwert ersetzen {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, coalesce(website, cors, 'not hosting shared resources') AS website_or_cors_config
FROM google.storage.buckets where project = 'stackql';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_corefunc.html#coalesce](https://www.sqlite.org/lang_corefunc.html#coalesce)
