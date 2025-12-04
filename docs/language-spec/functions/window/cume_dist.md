---
title: CUME_DIST
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
Returns the cumulative distribution of a value within a partition. This is calculated as `row-number / partition-rows`, where `row-number` is the value returned by `ROW_NUMBER()` for the last peer in the group and `partition-rows` is the number of rows in the partition.

See also:
[[` SELECT `]](/docs/language-spec/select) [[` PERCENT_RANK `]](/docs/language-spec/functions/window/percent_rank) [[` RANK `]](/docs/language-spec/functions/window/rank)

* * *

## Syntax

```sql
SELECT CUME_DIST() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Arguments

__*PARTITION BY column*__
Optional. Divides the result set into partitions. The `CUME_DIST` function is applied to each partition separately.

__*ORDER BY column*__
Specifies the order in which the cumulative distribution is calculated.

## Return Value(s)

Returns a floating-point number between 0.0 and 1.0 representing the cumulative distribution value.

* * *

## Examples

### Calculate cumulative distribution of contributors

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

`CUME_DIST` returns the proportion of rows with values less than or equal to the current row's value. A value of 0.75 means 75% of the rows have values less than or equal to the current row.

:::

For more information, see [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
