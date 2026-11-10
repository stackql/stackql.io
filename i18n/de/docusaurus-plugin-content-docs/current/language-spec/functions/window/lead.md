---
title: LEAD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "LEAD-Fensterfunktion in StackQL: einen Ausdruck für eine nachfolgende Zeile der Partition auswerten, mit optionalem Offset und Standardwert."
image: "/img/stackql-featured-image.png"
---
Gibt das Ergebnis der Auswertung eines Ausdrucks für eine nachfolgende Zeile in der Partition zurück.

Die erste Form von `LEAD()` gibt das Ergebnis der Auswertung des Ausdrucks für die nächste Zeile in der Partition zurück. Gibt es keine nächste Zeile (weil die aktuelle Zeile die letzte ist), wird `NULL` zurückgegeben.

Wird das Argument offset angegeben, muss es eine nicht negative Ganzzahl sein. Der zurückgegebene Wert ist das Ergebnis der Auswertung des Ausdrucks für die Zeile, die *offset* Zeilen nach der aktuellen Zeile innerhalb der Partition liegt. Ist offset 0, wird der Ausdruck für die aktuelle Zeile ausgewertet. Gibt es keine Zeile *offset* Zeilen nach der aktuellen Zeile, wird `NULL` zurückgegeben.

Wird zusätzlich ein Standardwert angegeben, wird dieser anstelle von `NULL` zurückgegeben, wenn die durch offset bestimmte Zeile nicht existiert.

Siehe auch:
[[` SELECT `]](/language-spec/select) [[` LAG `]](/language-spec/functions/window/lag)

* * *

## Syntax {#syntax}

```sql
SELECT LEAD(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*expr*__
Der Ausdruck, der für die nachfolgende Zeile ausgewertet wird.

__*offset*__
Optional. Eine nicht negative Ganzzahl, die angibt, um wie viele Zeilen vorausgeblickt wird. Standardwert ist 1.

__*default*__
Optional. Der Wert, der zurückgegeben wird, wenn die durch offset bestimmte Zeile nicht existiert. Standardwert ist `NULL`.

__*PARTITION BY column*__
Optional. Teilt die Ergebnismenge in Partitionen auf. Die Funktion `LEAD` wird innerhalb jeder Partition separat angewendet.

__*ORDER BY column*__
Legt die Reihenfolge fest, in der die Zeilen verarbeitet werden.

## Rückgabewert(e) {#return-values}

Gibt den Wert des Ausdrucks zurück, der für die angegebene nachfolgende Zeile ausgewertet wird, oder `NULL` (bzw. den Standardwert), wenn keine solche Zeile existiert.

* * *

## Beispiele {#examples}

### Auf das nächste Release vorausblicken {#look-ahead-to-the-next-release}

```sql
-- Compare each release to previous and next release dates
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LEAD(tag_name, 1) OVER (ORDER BY published_at) as next_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

Weitere Informationen finden Sie unter [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
