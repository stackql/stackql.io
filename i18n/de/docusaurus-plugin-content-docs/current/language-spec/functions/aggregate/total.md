---
title: TOTAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Aggregatfunktion TOTAL in StackQL: die Summe der Nicht-NULL-Werte in einer Spalte oder Gruppierung, immer als Gleitkommazahl zurückgegeben."
image: "/img/stackql-featured-image.png"
---
Gibt die Summe aller Nicht-`NULL`-Werte in einer Spalte oder Spaltengruppierung zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[` SUM `]](/language-spec/functions/aggregate/sum)

* * * 

## Syntax {#syntax}

```sql
SELECT TOTAL(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumente {#arguments}

__*columnExpression*__  
Eine Spalte oder ein Ausdruck.

> Wenn alle Werte in der *columnExpression* `NULL` sind, gibt `TOTAL` `0.0` zurück.  

__*groupByColumn*__  
Eine oder mehrere Spalten, über die Zusammenfassungs- oder Aggregatoperationen ausgeführt werden.  Die `GROUP BY`-Klausel gibt für jede Spaltengruppierung eine Zeile zurück.

## Rückgabewert(e) {#return-values}

Gibt einen Gleitkommawert zurück, der die Summe der *columnExpression* darstellt.

* * *

## Beispiele {#examples}

### Die Summe eines Spaltenausdrucks über eine gesamte Ressource zurückgeben {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Die Summe eines Spaltenausdrucks gruppiert nach einem anderen Spaltenausdruck zurückgeben {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
