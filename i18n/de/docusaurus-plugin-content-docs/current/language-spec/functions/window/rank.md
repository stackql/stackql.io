---
title: RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "RANK-Fensterfunktion in StackQL: Rang der aktuellen Zeile in ihrer Partition mit Lücken, gleiche Werte teilen einen Rang und die folgenden Ränge entfallen."
image: "/img/stackql-featured-image.png"
---
Gibt den Rang der aktuellen Zeile mit Lücken zurück. Dies ist die `ROW_NUMBER` des ersten Peers in jeder Gruppe. Zeilen mit gleichen Werten in den Sortierspalten erhalten denselben Rang, und der nächste Rangwert wird um die Anzahl der gleichrangigen Zeilen erhöht (es entstehen Lücken).

Gibt es keine `ORDER BY`-Klausel, gelten alle Zeilen als Peers, und diese Funktion gibt immer 1 zurück.

Siehe auch:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## Syntax {#syntax}

```sql
SELECT RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*PARTITION BY column*__
Optional. Teilt die Ergebnismenge in Partitionen auf. Die Funktion `RANK` wird auf jede Partition separat angewendet.

__*ORDER BY column*__
Legt die Reihenfolge fest, in der die Ränge vergeben werden. Gibt es keine `ORDER BY`-Klausel, gelten alle Zeilen als Peers und erhalten den Rang 1.

## Rückgabewert(e) {#return-values}

Gibt eine Ganzzahl zurück, die den Rang der aktuellen Zeile innerhalb der Partition darstellt, mit Lücken bei gleichen Werten.

* * *

## Beispiele {#examples}

### Rang der Mitwirkenden nach Beiträgen ermitteln (mit Lücken) {#rank-contributors-by-contributions-with-gaps}

```sql
-- Rank contributors with gaps for ties
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### `RANK` mit `DENSE_RANK` und `ROW_NUMBER` vergleichen {#compare-rank-with-dense_rank-and-row_number}

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

### Rang der Mitwirkenden innerhalb jedes Repositorys mit `PARTITION BY` ermitteln {#rank-contributors-within-each-repository-using-partition-by}

```sql
-- Rank contributors within each repo
WITH repo_contributors AS (
    SELECT
        repo,
        login,
        contributions,
        RANK() OVER (PARTITION BY repo ORDER BY contributions DESC) as rank_in_repo
    FROM (
        SELECT 'stackql' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql'
        UNION ALL
        SELECT 'stackql-provider-registry' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
    ) t
)
SELECT
    repo,
    login,
    contributions,
    rank_in_repo
FROM repo_contributors
ORDER BY repo, rank_in_repo;
```

Weitere Informationen finden Sie unter [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
