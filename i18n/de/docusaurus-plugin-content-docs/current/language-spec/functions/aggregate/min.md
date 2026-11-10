---
title: MIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Aggregatfunktion MIN in StackQL: der kleinste Wert in einer Spalte oder Spaltengruppierung."
image: "/img/stackql-featured-image.png"
---
Gibt den Minimalwert auf Basis einer Eingabespalte oder Spaltengruppierung zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
/* aggregate function */
SELECT MIN(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MIN(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

### Aggregatfunktion {#aggregate-function}

__*columnExpression*__  
Eine Spalte oder ein Ausdruck, einschließlich Operanden (`+`, `-`, ...).

> Die Funktion `MIN` gibt den kleinsten Wert für die angegebene Spalte auf Basis des Datentyps der Spalte zurück. Eine `MIN`-Operation auf einer Ganzzahlspalte gibt zum Beispiel die kleinste Ganzzahl für diese Spalte oder Spaltengruppierung zurück, während eine `MIN`-Operation auf einer Zeichenfolgenspalte oder -spaltengruppierung den kleinsten ASCII-Wert zurückgibt (in der Regel der erste Wert bei umgekehrter lexikografischer Sortierung, mit einigen Abweichungen).

__*groupByColumn*__  
Eine oder mehrere Spalten, über die Zusammenfassungs- oder Aggregatoperationen ausgeführt werden.  Die `GROUP BY`-Klausel gibt für jede Spaltengruppierung eine Zeile zurück.

> Die Funktion `MIN` ignoriert `NULL`-Werte.

> Die Funktion `MIN` gibt `NULL` zurück, wenn alle Werte in der Gruppe `NULL` sind.

### Skalarfunktion {#scalar-function}

__*scalarExpression*__  
Eine Liste von Spalten oder Ausdrücken (2 oder mehr), aus denen der kleinste Wert ermittelt und zurückgegeben wird. 

> Die skalare Funktion `MIN` gibt `NULL` zurück, wenn eines der Argumente `NULL` ist. 

> Die skalare `MIN`-Funktion (mit mehreren Argumenten) durchsucht ihre Argumente von links nach rechts nach einem Argument, das eine Kollationsfunktion definiert, und verwendet diese Kollationsfunktion für alle Zeichenfolgenvergleiche. 

> Wenn keines der Argumente der skalaren `MIN`-Funktion eine Kollationsfunktion definiert, wird die Kollationsfunktion `BINARY` verwendet. 

> Wird nur ein Argument angegeben, wird die Aggregatfunktion `MIN` aufgerufen.

## Rückgabewert(e) {#return-values}

Gibt den Minimalwert auf Basis des Eingabedatentyps (oder der Gruppierung) zurück.

* * *

## Beispiele {#examples}

### Den Minimalwert einer Spalte zurückgeben {#return-the-minimum-value-for-a-column}

```sql
SELECT name, location, min(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### Den Minimalwert für jeden Wert in einer Spalte zurückgeben {#return-the-minimum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
min(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### Den Minimalwert aus einer Liste von Werten mit der skalaren Funktion `min` zurückgeben {#return-the-minimum-value-from-a-list-of-values-using-the-scalar-min-function}

```sql
SELECT min(json_extract(disks, '$[0].diskSizeGb'),json_extract(disks, '$[1].diskSizeGb')) as smallest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### `MIN` als Fensterfunktion verwenden, um Minimalwerte innerhalb von Partitionen zu ermitteln {#use-min-as-a-window-function-to-find-minimum-values-within-partitions}

```sql
-- Find the earliest issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MIN(created_at) OVER (PARTITION BY state) as earliest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at;
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_aggfunc.html#min_agg](https://www.sqlite.org/lang_aggfunc.html#min_agg) oder [https://www.sqlite.org/lang_corefunc.html#min_scalar](https://www.sqlite.org/lang_corefunc.html#min_scalar)
