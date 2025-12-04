---
title: ROW_NUMBER
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
Returns the number of the row within the current partition. Rows are numbered starting from 1 in the order defined by the `ORDER BY` clause in the window definition, or in arbitrary order otherwise.

See also:
[[` SELECT `]](/docs/language-spec/select) [[` RANK `]](/docs/language-spec/functions/window/rank) [[` DENSE_RANK `]](/docs/language-spec/functions/window/dense_rank)

* * *

## Syntax

```sql
SELECT ROW_NUMBER() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Arguments

__*PARTITION BY column*__
Optional. Divides the result set into partitions. The `ROW_NUMBER` function is applied to each partition separately and resets the row number for each partition.

__*ORDER BY column*__
Specifies the order in which the row numbers are assigned. Required for meaningful results.

## Return Value(s)

Returns an integer representing the sequential row number within the partition, starting at 1.

* * *

## Examples

### Rank contributors within a repository

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

### Track issue sequence over time

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

### Compare ROW_NUMBER with RANK and DENSE_RANK

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

For more information, see [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
