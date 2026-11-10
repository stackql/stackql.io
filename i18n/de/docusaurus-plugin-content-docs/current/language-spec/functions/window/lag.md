---
title: LAG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "LAG-Fensterfunktion in StackQL: einen Ausdruck für eine vorherige Zeile der Partition auswerten, mit optionalem Offset und Standardwert."
image: "/img/stackql-featured-image.png"
---
Gibt das Ergebnis der Auswertung eines Ausdrucks für eine vorherige Zeile in der Partition zurück.

Die erste Form von `LAG()` gibt das Ergebnis der Auswertung des Ausdrucks für die vorherige Zeile in der Partition zurück. Gibt es keine vorherige Zeile (weil die aktuelle Zeile die erste ist), wird `NULL` zurückgegeben.

Wird das Argument offset angegeben, muss es eine nicht negative Ganzzahl sein. Der zurückgegebene Wert ist das Ergebnis der Auswertung des Ausdrucks für die Zeile, die *offset* Zeilen vor der aktuellen Zeile innerhalb der Partition liegt. Ist offset 0, wird der Ausdruck für die aktuelle Zeile ausgewertet. Gibt es keine Zeile *offset* Zeilen vor der aktuellen Zeile, wird `NULL` zurückgegeben.

Wird zusätzlich ein Standardwert angegeben, wird dieser anstelle von `NULL` zurückgegeben, wenn die durch offset bestimmte Zeile nicht existiert.

Siehe auch:
[[` SELECT `]](/language-spec/select) [[` LEAD `]](/language-spec/functions/window/lead)

* * *

## Syntax {#syntax}

```sql
SELECT LAG(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*expr*__
Der Ausdruck, der für die vorherige Zeile ausgewertet wird.

__*offset*__
Optional. Eine nicht negative Ganzzahl, die angibt, um wie viele Zeilen zurückgeblickt wird. Standardwert ist 1.

__*default*__
Optional. Der Wert, der zurückgegeben wird, wenn die durch offset bestimmte Zeile nicht existiert. Standardwert ist `NULL`.

__*PARTITION BY column*__
Optional. Teilt die Ergebnismenge in Partitionen auf. Die Funktion `LAG` wird innerhalb jeder Partition separat angewendet.

__*ORDER BY column*__
Legt die Reihenfolge fest, in der die Zeilen verarbeitet werden.

## Rückgabewert(e) {#return-values}

Gibt den Wert des Ausdrucks zurück, der für die angegebene vorherige Zeile ausgewertet wird, oder `NULL` (bzw. den Standardwert), wenn keine solche Zeile existiert.

* * *

## Beispiele {#examples}

### Jedes Release mit dem vorherigen Release vergleichen {#compare-each-release-to-the-previous-release}

```sql
-- Compare each release to previous and next release dates
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LAG(published_at, 1) OVER (ORDER BY published_at) as previous_release_date,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### Abstände zwischen der Erstellung von Issues analysieren {#analyze-gaps-between-issue-creation}

```sql
-- Analyze gaps between issue creation
SELECT
    number,
    title,
    created_at,
    LAG(created_at, 1) OVER (ORDER BY created_at) as prev_issue_date,
    julianday(created_at) - julianday(LAG(created_at, 1) OVER (ORDER BY created_at)) as days_since_prev_issue
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

Weitere Informationen finden Sie unter [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
