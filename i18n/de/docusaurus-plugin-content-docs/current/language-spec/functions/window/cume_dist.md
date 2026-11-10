---
title: CUME_DIST
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "CUME_DIST-Fensterfunktion in StackQL: kumulative Verteilung der aktuellen Zeile in ihrer Partition als Zeilennummer geteilt durch die Zeilenzahl der Partition."
image: "/img/stackql-featured-image.png"
---
Gibt die kumulative Verteilung eines Werts innerhalb einer Partition zurück. Sie wird als `row-number / partition-rows` berechnet, wobei `row-number` der von `ROW_NUMBER()` für den letzten Peer der Gruppe zurückgegebene Wert und `partition-rows` die Anzahl der Zeilen in der Partition ist.

Siehe auch:
[[` SELECT `]](/language-spec/select) [[` PERCENT_RANK `]](/language-spec/functions/window/percent_rank) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Syntax {#syntax}

```sql
SELECT CUME_DIST() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*PARTITION BY column*__
Optional. Teilt die Ergebnismenge in Partitionen auf. Die Funktion `CUME_DIST` wird auf jede Partition separat angewendet.

__*ORDER BY column*__
Legt die Reihenfolge fest, in der die kumulative Verteilung berechnet wird.

## Rückgabewert(e) {#return-values}

Gibt eine Gleitkommazahl zwischen 0.0 und 1.0 zurück, die den Wert der kumulativen Verteilung darstellt.

* * *

## Beispiele {#examples}

### Kumulative Verteilung der Mitwirkenden berechnen {#calculate-cumulative-distribution-of-contributors}

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

`CUME_DIST` gibt den Anteil der Zeilen zurück, deren Werte kleiner oder gleich dem Wert der aktuellen Zeile sind. Ein Wert von 0.75 bedeutet, dass 75% der Zeilen Werte haben, die kleiner oder gleich dem Wert der aktuellen Zeile sind.

:::

Weitere Informationen finden Sie unter [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
