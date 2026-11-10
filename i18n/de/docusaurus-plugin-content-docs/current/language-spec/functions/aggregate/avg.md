---
title: AVG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Aggregatfunktion AVG in StackQL: der Durchschnitt der Nicht-NULL-Werte in einer Spalte oder Gruppierung."
image: "/img/stackql-featured-image.png"
---
Gibt den Durchschnittswert aller Nicht-`NULL`-Werte innerhalb einer Spalte oder Spaltengruppierung zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT AVG(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumente {#arguments}

__*columnExpression*__  
Eine Spalte oder ein Ausdruck.

> Zeichenfolgen- und BLOB-Werte, die nicht als numerische Werte dargestellt werden können, werden als `0` interpretiert.  

> Mindestens ein Wert der *columnExpression* darf nicht `NULL` sein, andernfalls wird `NULL` zurückgegeben.

__*groupByColumn*__  
Eine oder mehrere Spalten, über die Zusammenfassungs- oder Aggregatoperationen ausgeführt werden.  Die `GROUP BY`-Klausel gibt für jede Spaltengruppierung eine Zeile zurück.

## Rückgabewert(e) {#return-values}

Gibt einen Gleitkommawert zurück, der den Durchschnitt der *columnExpression* darstellt.

* * *

## Beispiele {#examples}

### Den Durchschnitt eines Spaltenausdrucks über eine gesamte Ressource zurückgeben {#return-the-average-of-a-column-expression-over-an-entire-resource}

```sql
SELECT avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Den Durchschnitt eines Spaltenausdrucks gruppiert nach einem anderen Spaltenausdruck zurückgeben {#return-the-average-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

### `AVG` als Fensterfunktion zur Berechnung gleitender Durchschnitte verwenden {#use-avg-as-a-window-function-to-calculate-moving-averages}

```sql
-- Calculate moving average of commit activity
WITH weekly_totals AS (
    SELECT
        week,
        SUM(json_each.value) as commits_this_week
    FROM github.repos.stats_commit_activity, JSON_EACH(days)
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    GROUP BY week
)
SELECT
    week,
    commits_this_week,
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg
FROM weekly_totals
ORDER BY week;
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_aggfunc.html#avg](https://www.sqlite.org/lang_aggfunc.html#avg).
