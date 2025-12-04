---
title: NTILE
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
Divides the partition into N groups as evenly as possible and assigns an integer between 1 and N to each group, in the order defined by the `ORDER BY` clause, or in arbitrary order otherwise. If necessary, larger groups occur first.

See also:
[[` SELECT `]](/docs/language-spec/select) [[` RANK `]](/docs/language-spec/functions/window/rank)

* * *

## Syntax

```sql
SELECT NTILE(N) OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Arguments

__*N*__
A positive integer specifying the number of groups (tiles) to divide the partition into.

__*PARTITION BY column*__
Optional. Divides the result set into partitions. The `NTILE` function is applied to each partition separately.

__*ORDER BY column*__
Specifies the order in which rows are assigned to tiles.

## Return Value(s)

Returns an integer between 1 and N indicating which tile (group) the current row belongs to.

* * *

## Examples

### Group contributors into quartiles by contribution level

```sql
-- Group contributors into quartiles by contribution level
SELECT
    login,
    contributions,
    NTILE(4) OVER (ORDER BY contributions DESC) as contribution_quartile,
    CASE NTILE(4) OVER (ORDER BY contributions DESC)
        WHEN 1 THEN 'Top Contributors'
        WHEN 2 THEN 'Active Contributors'
        WHEN 3 THEN 'Moderate Contributors'
        WHEN 4 THEN 'Occasional Contributors'
    END as contributor_tier
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

Use `NTILE` to create segments or tiers in your data. Common use cases include dividing data into quartiles (N=4), deciles (N=10), or percentiles (N=100).

:::

For more information, see [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
