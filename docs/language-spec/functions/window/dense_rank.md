---
title: DENSE_RANK
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
Returns the rank of the current row without gaps. This is the number of the current row's peer group within its partition. Rows with equal values for the ordering columns receive the same rank, and the next rank value is always incremented by 1 (no gaps).

Rows are numbered starting from 1 in the order defined by the `ORDER BY` clause in the window definition. If there is no `ORDER BY` clause, then all rows are considered peers and this function always returns 1.

See also:
[[` SELECT `]](/docs/language-spec/select) [[` ROW_NUMBER `]](/docs/language-spec/functions/window/row_number) [[` RANK `]](/docs/language-spec/functions/window/rank)

* * *

## Syntax

```sql
SELECT DENSE_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Arguments

__*PARTITION BY column*__
Optional. Divides the result set into partitions. The `DENSE_RANK` function is applied to each partition separately.

__*ORDER BY column*__
Specifies the order in which ranks are assigned. If there is no `ORDER BY` clause, all rows are considered peers and receive rank 1.

## Return Value(s)

Returns an integer representing the dense rank of the current row within the partition, without gaps for tied values.

* * *

## Examples

### Rank contributors by contributions (without gaps)

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

### Compare `DENSE_RANK` with `RANK` and `ROW_NUMBER`

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

### Rank aggregated contributions across multiple repositories

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

For more information, see [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
