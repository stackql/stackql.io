---
title: LAST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: "LAST_VALUE window function in StackQL: the value of an expression for the last row of the window frame."
image: "/img/stackql-featured-image.png"
---
Returns the value of the expression evaluated against the last row in the window frame.

This built-in window function calculates the window frame for each row in the same way as an aggregate window function. It returns the value of the expression evaluated against the last row in the window frame for each row.

See also:
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` NTH_VALUE `]](/language-spec/functions/window/nth_value)

* * *

## Syntax {#syntax}

```sql
SELECT LAST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Arguments {#arguments}

__*expr*__
The expression to evaluate against the last row in the window frame.

__*PARTITION BY column*__
Optional. Divides the result set into partitions. The `LAST_VALUE` function is applied within each partition separately.

__*ORDER BY column*__
Specifies the order in which rows are processed.

__*ROWS BETWEEN frame_start AND frame_end*__
Optional. Defines the window frame. Common options include:
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

Without a frame clause, the frame is `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`: it ends at the last row that ties with the current row on the `ORDER BY` columns, not at the current row.

## Return Value(s) {#return-values}

Returns the value of the expression evaluated against the last row in the window frame.

* * *

## Examples {#examples}

### Find the latest release for context {#find-the-latest-release-for-context}

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

By default, the window frame extends from the start of the partition to the last row that ties with the current row on the `ORDER BY` columns, so `LAST_VALUE` returns the value of that last tied row (the current row itself when its `ORDER BY` value is unique), not the last value in the partition. To get the last value in the partition, use `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`.

:::

For more information, see [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
