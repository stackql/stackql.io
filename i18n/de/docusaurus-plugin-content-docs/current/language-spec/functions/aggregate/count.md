---
title: COUNT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "Aggregatfunktion COUNT in StackQL: die Anzahl der Zeilen in einer Ressource oder der Nicht-NULL-Werte einer Spalte innerhalb einer Gruppierung."
image: "/img/stackql-featured-image.png"
---
Gibt die Gesamtzahl der Datensätze in einer Ressource oder die Anzahl der Nicht-`NULL`-Vorkommen einer Spalte in einer Gruppierung zurück.  

Siehe auch:  
[[` SELECT `]](/language-spec/select)

* * * 

## Syntax {#syntax}

```sql
SELECT COUNT( * | columnExpression ) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Argumente {#arguments}

__*columnExpression*__  oder __*__  
Eine Spalte oder ein Ausdruck.  Wenn __*__ angegeben ist, werden alle Datensätze in der Ressource oder Gruppierung gezählt.  

> Mindestens ein Wert der *columnExpression* darf nicht `NULL` sein, andernfalls wird `NULL` zurückgegeben.

__*groupByColumn*__  
Eine oder mehrere Spalten, über die Zusammenfassungs- oder Aggregatoperationen ausgeführt werden.  Die `GROUP BY`-Klausel gibt für jede Spaltengruppierung eine Zeile zurück.

## Rückgabewert(e) {#return-values}

Gibt eine Ganzzahl zurück, die die Anzahl darstellt.

* * *

## Beispiele {#examples}

### Den Durchschnitt eines Spaltenausdrucks über eine gesamte Ressource zurückgeben {#return-the-average-of-a-column-expression-over-an-entire-resource}

```sql
SELECT count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Den Durchschnitt eines Spaltenausdrucks gruppiert nach einem anderen Spaltenausdruck zurückgeben {#return-the-average-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

### `COUNT` als Fensterfunktion zur Berechnung kumulierter Anzahlen verwenden {#use-count-as-a-window-function-to-calculate-cumulative-counts}

```sql
-- Track cumulative issue counts over time
SELECT
    number,
    title,
    state,
    created_at,
    COUNT(*) OVER (ORDER BY created_at) as cumulative_issues,
    COUNT(*) OVER (PARTITION BY state ORDER BY created_at) as cumulative_by_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

Weitere Informationen finden Sie unter [https://www.sqlite.org/lang_aggfunc.html#count](https://www.sqlite.org/lang_aggfunc.html#count).
