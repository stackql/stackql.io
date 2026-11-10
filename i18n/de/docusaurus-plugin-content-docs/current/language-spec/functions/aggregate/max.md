---
title: MAX
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Aggregatfunktion MAX in StackQL: der größte Wert in einer Spalte oder Spaltengruppierung."
image: "/img/stackql-featured-image.png"
---
Gibt den Maximalwert auf Basis einer Eingabespalte oder Spaltengruppierung zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Syntax {#syntax}

```sql
/* aggregate function */
SELECT MAX(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MAX(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

### Aggregatfunktion {#aggregate-function}

__*columnExpression*__  
Eine Spalte oder ein Ausdruck, einschließlich Operanden (`+`, `-`, ...).

> Die Funktion `MAX` gibt den größten Wert für die angegebene Spalte auf Basis des Datentyps der Spalte zurück. Eine `MAX`-Operation auf einer Ganzzahlspalte gibt zum Beispiel die größte Ganzzahl für diese Spalte oder Spaltengruppierung zurück, während eine `MAX`-Operation auf einer Zeichenfolgenspalte oder -spaltengruppierung den größten ASCII-Wert zurückgibt (in der Regel der erste Wert bei lexikografischer Sortierung, mit einigen Abweichungen).

__*groupByColumn*__  
Eine oder mehrere Spalten, über die Zusammenfassungs- oder Aggregatoperationen ausgeführt werden.  Die `GROUP BY`-Klausel gibt für jede Spaltengruppierung eine Zeile zurück.

> Die Funktion `MAX` ignoriert `NULL`-Werte.

> Die Funktion `MAX` gibt `NULL` zurück, wenn alle Werte in der Gruppe `NULL` sind.

### Skalarfunktion {#scalar-function}

__*scalarExpression*__  
Eine Liste von Spalten oder Ausdrücken (2 oder mehr), aus denen der größte Wert ermittelt und zurückgegeben wird. 

> Die skalare Funktion `MAX` gibt `NULL` zurück, wenn eines der Argumente `NULL` ist. 

> Die skalare `MAX`-Funktion (mit mehreren Argumenten) durchsucht ihre Argumente von links nach rechts nach einem Argument, das eine Kollationsfunktion definiert, und verwendet diese Kollationsfunktion für alle Zeichenfolgenvergleiche. 

> Wenn keines der Argumente der skalaren `MAX`-Funktion eine Kollationsfunktion definiert, wird die Kollationsfunktion `BINARY` verwendet. 

> Wird nur ein Argument angegeben, wird die Aggregatfunktion `MAX` aufgerufen.

## Rückgabewert(e) {#return-values}

Gibt den Maximalwert auf Basis des Eingabedatentyps (oder der Gruppierung) zurück.

* * *

## Beispiele {#examples}

### Den Maximalwert einer Spalte zurückgeben {#return-the-maximum-value-for-a-column}

```sql
SELECT name, location, max(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### Den Maximalwert für jeden Wert in einer Spalte zurückgeben {#return-the-maximum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
max(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### Den Maximalwert aus einer Liste von Werten mit der skalaren Funktion `max` zurückgeben {#return-the-maximum-value-from-a-list-of-values-using-the-scalar-max-function}

```sql
SELECT max(json_extract(disks, '$[0].diskSizeGb'),json_extract(disks, '$[1].diskSizeGb')) as largest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### `MAX` als Fensterfunktion verwenden, um Maximalwerte innerhalb von Partitionen zu ermitteln {#use-max-as-a-window-function-to-find-maximum-values-within-partitions}

```sql
-- Find the most recent issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MAX(created_at) OVER (PARTITION BY state) as latest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at DESC;
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_aggfunc.html#max_agg](https://www.sqlite.org/lang_aggfunc.html#max_agg) oder [https://www.sqlite.org/lang_corefunc.html#max_scalar](https://www.sqlite.org/lang_corefunc.html#max_scalar)
