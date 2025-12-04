---
title: RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: "/img/stackql-featured-image.png"
---
Returns the rank of the current row with gaps. This is the `ROW_NUMBER` of the first peer in each group. Rows with equal values for the ordering columns receive the same rank, and the next rank value is incremented by the number of tied rows (leaving gaps).

If there is no `ORDER BY` clause, then all rows are considered peers and this function always returns 1.

See also:
[[` SELECT `]](/docs/language-spec/select) [[` ROW_NUMBER `]](/docs/language-spec/functions/window/row_number) [[` DENSE_RANK `]](/docs/language-spec/functions/window/dense_rank)

* * *

## Syntax

```sql
SELECT RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Arguments

__*PARTITION BY column*__
Optional. Divides the result set into partitions. The `RANK` function is applied to each partition separately.

__*ORDER BY column*__
Specifies the order in which ranks are assigned. If there is no `ORDER BY` clause, all rows are considered peers and receive rank 1.

## Return Value(s)

Returns an integer representing the rank of the current row within the partition, with gaps for tied values.

* * *

## Examples

### Rank contributors by contributions (with gaps)

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

### Compare `RANK` with `DENSE_RANK` and `ROW_NUMBER`

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

### Rank contributors within each repository using `PARTITION BY`

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

For more information, see [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
