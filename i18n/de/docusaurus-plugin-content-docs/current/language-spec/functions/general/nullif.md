---
title: NULLIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "NULLIF-Funktion in StackQL: gibt NULL zurück, wenn zwei Argumente gleich sind, andernfalls das erste Argument."
image: "/img/stackql-featured-image.png"
---
Akzeptiert zwei Argumente und gibt einen `NULL`-Wert zurück, wenn sie gleich sind; andernfalls wird das erste Argument zurückgegeben.  

> Die Funktion `NULLIF` ist äquivalent zu `CASE WHEN expression1 = expression2 THEN NULL ELSE expression1 END`

> `NULLIF` ist nützlich in Verbindung mit Aggregatfunktionen wie [`AVG`](/language-spec/functions/aggregate/avg), [`MAX`](/language-spec/functions/aggregate/max), [`MIN`](/language-spec/functions/aggregate/min), [`SUM`](/language-spec/functions/aggregate/sum) und [`COUNT`](/language-spec/functions/aggregate/count), wenn die Ergebnismenge besondere Werte enthält, die nicht `NULL` sind, die Sie für die Aggregation aber wie `NULL` behandeln möchten.

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` IFNULL `]](/language-spec/functions/general/ifnull)

* * * 

## Syntax {#syntax}

```sql
SELECT NULLIF(expression1, expression2) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*expression*__  
Zwei Ausdrücke eines beliebigen Datentyps, die zu einem `NULL`- oder Nicht-`NULL`-Wert ausgewertet werden.

> Die Funktion `NULLIF` durchsucht ihre Argumente von links nach rechts nach einem Argument, das eine Kollationsfunktion definiert, und verwendet diese Kollationsfunktion für alle String-Vergleiche.  Wenn keines der beiden Argumente eine Kollationsfunktion definiert, wird die Kollation `BINARY` verwendet.

## Rückgabewert(e) {#return-values}

Gibt `NULL` zurück, wenn die beiden *expression*-Argumente zum selben Wert ausgewertet werden; andernfalls wird der erste Wert zurückgegeben. 

* * *

## Beispiele {#examples}

### Datensätze zählen, bei denen ein Feld ungleich einem bestimmten Wert ist {#count-records-where-a-field-is-not-equal-to-a-given-value}

```sql
SELECT COUNT(NULLIF(name, 'controller-0'))
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
equivalent to: 
SELECT COUNT(*) FROM google.compute.instances 
WHERE name != 'controller-0';
*/
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_corefunc.html#nullif](https://www.sqlite.org/lang_corefunc.html#nullif)
