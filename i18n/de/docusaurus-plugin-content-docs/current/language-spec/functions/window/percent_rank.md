---
title: PERCENT_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "PERCENT_RANK-Fensterfunktion in StackQL: der relative Rang der aktuellen Zeile innerhalb ihrer Partition als Wert zwischen 0.0 und 1.0."
image: "/img/stackql-featured-image.png"
---
Gibt einen Wert zwischen 0.0 und 1.0 zurück, der den relativen Rang der aktuellen Zeile innerhalb ihrer Partition darstellt.

Trotz ihres Namens gibt diese Funktion einen Wert gleich `(rank - 1) / (partition-rows - 1)` zurück, wobei `rank` der von der eingebauten Fensterfunktion `RANK()` zurückgegebene Wert und `partition-rows` die Gesamtzahl der Zeilen in der Partition ist. Enthält die Partition nur eine Zeile, gibt diese Funktion 0.0 zurück.

Siehe auch:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` CUME_DIST `]](/language-spec/functions/window/cume_dist)

* * *

## Syntax {#syntax}

```sql
SELECT PERCENT_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*PARTITION BY column*__
Optional. Teilt die Ergebnismenge in Partitionen auf. Die Funktion `PERCENT_RANK` wird auf jede Partition separat angewendet.

__*ORDER BY column*__
Legt die Reihenfolge fest, in der die Perzentilränge berechnet werden.

## Rückgabewert(e) {#return-values}

Gibt eine Gleitkommazahl zwischen 0.0 und 1.0 zurück, die den Perzentilrang darstellt.

* * *

## Beispiele {#examples}

### Perzentilrang der Mitwirkenden berechnen {#calculate-percentile-ranking-of-contributors}

```sql
-- Calculate percentile ranking of contributors
SELECT
    login,
    contributions,
    PERCENT_RANK() OVER (ORDER BY contributions) as percentile,
    CUME_DIST() OVER (ORDER BY contributions) as cumulative_dist
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY contributions DESC;
```

:::tip

Der `PERCENT_RANK`-Wert 0.0 kennzeichnet die Zeile mit dem niedrigsten Rang, 1.0 die Zeile mit dem höchsten Rang. Verwenden Sie diese Funktion, um festzustellen, wo ein bestimmter Wert innerhalb einer Verteilung liegt.

:::

Weitere Informationen finden Sie unter [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
