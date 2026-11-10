---
title: DENSE_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "DENSE_RANK-Fensterfunktion in StackQL: Rang der aktuellen Zeile in ihrer Partition ohne Lücken, gleiche Werte teilen einen Rang und der nächste folgt direkt."
image: "/img/stackql-featured-image.png"
---
Gibt den Rang der aktuellen Zeile ohne Lücken zurück. Dies ist die Nummer der Peer-Gruppe der aktuellen Zeile innerhalb ihrer Partition. Zeilen mit gleichen Werten in den Sortierspalten erhalten denselben Rang, und der nächste Rangwert wird immer um 1 erhöht (keine Lücken).

Die Zeilen werden beginnend bei 1 in der Reihenfolge nummeriert, die durch die `ORDER BY`-Klausel in der Fensterdefinition festgelegt ist. Gibt es keine `ORDER BY`-Klausel, gelten alle Zeilen als Peers, und diese Funktion gibt immer 1 zurück.

Siehe auch:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Syntax {#syntax}

```sql
SELECT DENSE_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*PARTITION BY column*__
Optional. Teilt die Ergebnismenge in Partitionen auf. Die Funktion `DENSE_RANK` wird auf jede Partition separat angewendet.

__*ORDER BY column*__
Legt die Reihenfolge fest, in der die Ränge vergeben werden. Gibt es keine `ORDER BY`-Klausel, gelten alle Zeilen als Peers und erhalten den Rang 1.

## Rückgabewert(e) {#return-values}

Gibt eine Ganzzahl zurück, die den lückenlosen Rang der aktuellen Zeile innerhalb der Partition darstellt, ohne Lücken bei gleichen Werten.

* * *

## Beispiele {#examples}

### Rang der Mitwirkenden nach Beiträgen ermitteln (ohne Lücken) {#rank-contributors-by-contributions-without-gaps}

```sql
-- Rank contributors without gaps for ties
SELECT
    DENSE_RANK() OVER (ORDER BY contributions DESC) as rank,
    login,
    contributions,
    html_url
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo IN ('stackql', 'stackql-deploy', 'stackql-provider-registry')
ORDER BY contributions DESC;
```

### `DENSE_RANK` mit `RANK` und `ROW_NUMBER` vergleichen {#compare-dense_rank-with-rank-and-row_number}

```sql
-- Compare RANK vs DENSE_RANK vs ROW_NUMBER for contributors
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Rang aggregierter Beiträge über mehrere Repositorys ermitteln {#rank-aggregated-contributions-across-multiple-repositories}

```sql
-- Rank total contributions across multiple repos
WITH all_contributors AS (
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions,
    MAX(html_url) as html_url
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

Weitere Informationen finden Sie unter [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
