---
title: ROW_NUMBER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
description: "ROW_NUMBER-Fensterfunktion in StackQL: die Zeilen einer Partition ab 1 in der Reihenfolge der ORDER BY-Klausel des Fensters nummerieren."
image: "/img/stackql-featured-image.png"
---
Gibt die Nummer der Zeile innerhalb der aktuellen Partition zurück. Die Zeilen werden beginnend bei 1 in der Reihenfolge nummeriert, die durch die `ORDER BY`-Klausel in der Fensterdefinition festgelegt ist, andernfalls in beliebiger Reihenfolge.

Siehe auch:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## Syntax {#syntax}

```sql
SELECT ROW_NUMBER() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Argumente {#arguments}

__*PARTITION BY column*__
Optional. Teilt die Ergebnismenge in Partitionen auf. Die Funktion `ROW_NUMBER` wird auf jede Partition separat angewendet und setzt die Zeilennummer für jede Partition zurück.

__*ORDER BY column*__
Legt die Reihenfolge fest, in der die Zeilennummern vergeben werden. Für aussagekräftige Ergebnisse erforderlich.

## Rückgabewert(e) {#return-values}

Gibt eine Ganzzahl zurück, die die fortlaufende Zeilennummer innerhalb der Partition darstellt, beginnend bei 1.

* * *

## Beispiele {#examples}

### Rang der Mitwirkenden innerhalb eines Repositorys ermitteln {#rank-contributors-within-a-repository}

```sql
-- Rank contributors within a repository
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as contribution_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Reihenfolge der Issues im Zeitverlauf verfolgen {#track-issue-sequence-over-time}

```sql
-- Track issue growth over time
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### `ROW_NUMBER` mit `RANK` und `DENSE_RANK` vergleichen {#compare-row_number-with-rank-and-dense_rank}

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

Weitere Informationen finden Sie unter [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
