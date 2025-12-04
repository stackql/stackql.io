---
title: FIRST_VALUE
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
Returns the value of the expression evaluated against the first row in the window frame.

This built-in window function calculates the window frame for each row in the same way as an aggregate window function. It returns the value of the expression evaluated against the first row in the window frame for each row.

See also:
[[` SELECT `]](/docs/language-spec/select) [[` LAST_VALUE `]](/docs/language-spec/functions/window/last_value) [[` NTH_VALUE `]](/docs/language-spec/functions/window/nth_value)

* * *

## Syntax

```sql
SELECT FIRST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Arguments

__*expr*__
The expression to evaluate against the first row in the window frame.

__*PARTITION BY column*__
Optional. Divides the result set into partitions. The `FIRST_VALUE` function is applied within each partition separately.

__*ORDER BY column*__
Specifies the order in which rows are processed.

__*ROWS BETWEEN frame_start AND frame_end*__
Optional. Defines the window frame. Common options include:
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW` (default)
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

## Return Value(s)

Returns the value of the expression evaluated against the first row in the window frame.

* * *

## Examples

### Find the first release for context

```sql
-- Find first and latest release for context
SELECT
    tag_name,
    name,
    published_at,
    FIRST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as first_release,
    LAST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as latest_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

By default, the window frame extends from the start of the partition to the current row. To include all rows in the partition, use `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`.

:::

For more information, see [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
