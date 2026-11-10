---
title: SUM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Aggregatfunktion SUM in StackQL: die Summe der Nicht-NULL-Werte in einer Spalte oder Gruppierung, als Ganzzahl, wenn alle Eingaben Ganzzahlen sind."
image: "/img/stackql-featured-image.png"
---
Gibt die Summe aller Nicht-`NULL`-Werte in einer Spalte oder Spaltengruppierung zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) [[` TOTAL `]](/language-spec/functions/aggregate/total)

* * * 

:::tip

Verwenden Sie die Funktion [**TOTAL**](/language-spec/functions/aggregate/total), um Gleitkommazahlen oder Long-Werte zu summieren.

:::

## Syntax {#syntax}

```sql
SELECT SUM(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumente {#arguments}

__*columnExpression*__  
Eine Spalte oder ein Ausdruck.

> Wenn alle Werte in der *columnExpression* `NULL` sind, gibt `SUM` `NULL` zurück.  

> `SUM` löst eine "integer overflow"-Ausnahme aus, wenn zu irgendeinem Zeitpunkt der Berechnung ein Ganzzahlüberlauf auftritt.  Die Funktion [`TOTAL`]](/language-spec/functions/aggregate/total) löst nie einen Ganzzahlüberlauf aus.  

__*groupByColumn*__  
Eine oder mehrere Spalten, über die Zusammenfassungs- oder Aggregatoperationen ausgeführt werden.  Die `GROUP BY`-Klausel gibt für jede Spaltengruppierung eine Zeile zurück.

## Rückgabewert(e) {#return-values}

Gibt eine Ganzzahl zurück, die die Summe der *columnExpression* darstellt, wenn alle Nicht-`NULL`-Eingaben Ganzzahlen sind. Wenn Eingaben keine Ganzzahlen oder `NULL` sind, wird ein Gleitkommawert zurückgegeben, der eine Näherung der tatsächlichen Summe sein kann.

* * *

## Beispiele {#examples}

### Die Summe eines Spaltenausdrucks über eine gesamte Ressource zurückgeben {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Die Summe eines Spaltenausdrucks gruppiert nach einem anderen Spaltenausdruck zurückgeben {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

### `SUM` als Fensterfunktion zur Berechnung laufender Summen verwenden {#use-sum-as-a-window-function-to-calculate-running-totals}

```sql
-- Calculate running total and percentage of contributions
SELECT
    login,
    contributions,
    SUM(contributions) OVER (ORDER BY contributions DESC) as running_total,
    SUM(contributions) OVER () as total_contributions,
    ROUND(100.0 * contributions / SUM(contributions) OVER (), 2) as pct_of_total
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
