---
title: PERCENT_RANK
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
Returns a value between 0.0 and 1.0 representing the relative rank of the current row within its partition.

Despite the name, this function returns a value equal to `(rank - 1) / (partition-rows - 1)`, where `rank` is the value returned by the built-in window function `RANK()` and `partition-rows` is the total number of rows in the partition. If the partition contains only one row, this function returns 0.0.

See also:
[[` SELECT `]](/docs/language-spec/select) [[` RANK `]](/docs/language-spec/functions/window/rank) [[` CUME_DIST `]](/docs/language-spec/functions/window/cume_dist)

* * *

## Syntax

```sql
SELECT PERCENT_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Arguments

__*PARTITION BY column*__
Optional. Divides the result set into partitions. The `PERCENT_RANK` function is applied to each partition separately.

__*ORDER BY column*__
Specifies the order in which the percentile ranks are calculated.

## Return Value(s)

Returns a floating-point number between 0.0 and 1.0 representing the percentile rank.

* * *

## Examples

### Calculate percentile ranking of contributors

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

The `PERCENT_RANK` value of 0.0 indicates the lowest-ranked row, while 1.0 indicates the highest-ranked row. Use this function to identify where a particular value falls within a distribution.

:::

For more information, see [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
