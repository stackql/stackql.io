---
title: NTH_VALUE
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
Returns the value of the expression evaluated against the Nth row in the window frame.

This built-in window function calculates the window frame for each row in the same way as an aggregate window function. It returns the value of the expression evaluated against row N of the window frame. Rows are numbered within the window frame starting from 1 in the order defined by the `ORDER BY` clause if present, or in arbitrary order otherwise. If there is no Nth row in the partition, then `NULL` is returned.

See also:
[[` SELECT `]](/docs/language-spec/select) [[` FIRST_VALUE `]](/docs/language-spec/functions/window/first_value) [[` LAST_VALUE `]](/docs/language-spec/functions/window/last_value)

* * *

## Syntax

```sql
SELECT NTH_VALUE(expr, N) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Arguments

__*expr*__
The expression to evaluate against the Nth row in the window frame.

__*N*__
A positive integer specifying which row number to retrieve (1-indexed).

__*PARTITION BY column*__
Optional. Divides the result set into partitions. The `NTH_VALUE` function is applied within each partition separately.

__*ORDER BY column*__
Specifies the order in which rows are processed.

__*ROWS BETWEEN frame_start AND frame_end*__
Optional. Defines the window frame.

## Return Value(s)

Returns the value of the expression evaluated against the Nth row in the window frame, or `NULL` if the Nth row does not exist.

* * *

## Examples

### Get the second-highest contributor

```sql
-- Get the top 3 contributors by name for comparison
SELECT
    login,
    contributions,
    NTH_VALUE(login, 1) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as top_contributor,
    NTH_VALUE(login, 2) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as second_contributor,
    NTH_VALUE(login, 3) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as third_contributor
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

For more information, see [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
