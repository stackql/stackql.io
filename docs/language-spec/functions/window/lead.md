---
title: LEAD
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
Returns the result of evaluating an expression against a subsequent row in the partition.

The first form of `LEAD()` returns the result of evaluating the expression against the next row in the partition. Or, if there is no next row (because the current row is the last), `NULL` is returned.

If the offset argument is provided, it must be a non-negative integer. The value returned is the result of evaluating the expression against the row *offset* rows after the current row within the partition. If offset is 0, the expression is evaluated against the current row. If there is no row *offset* rows after the current row, `NULL` is returned.

If a default value is also provided, it is returned instead of `NULL` if the row identified by offset does not exist.

See also:
[[` SELECT `]](/docs/language-spec/select) [[` LAG `]](/docs/language-spec/functions/window/lag)

* * *

## Syntax

```sql
SELECT LEAD(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## Arguments

__*expr*__
The expression to evaluate against the subsequent row.

__*offset*__
Optional. A non-negative integer specifying how many rows forward to look. Defaults to 1.

__*default*__
Optional. The value to return if the offset row does not exist. Defaults to `NULL`.

__*PARTITION BY column*__
Optional. Divides the result set into partitions. The `LEAD` function is applied within each partition separately.

__*ORDER BY column*__
Specifies the order in which rows are processed.

## Return Value(s)

Returns the value of the expression evaluated against the specified subsequent row, or `NULL` (or the default value) if no such row exists.

* * *

## Examples

### Look ahead to the next release

```sql
-- Compare each release to previous and next release dates
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LEAD(tag_name, 1) OVER (ORDER BY published_at) as next_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

For more information, see [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
