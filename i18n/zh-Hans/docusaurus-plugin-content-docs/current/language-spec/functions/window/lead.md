---
title: LEAD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 LEAD 窗口函数：针对分区中后面的某一行对表达式求值，可指定偏移量和默认值。"
image: "/img/stackql-featured-image.png"
---
返回针对分区中后面的某一行对表达式求值的结果。

`LEAD()` 的第一种形式返回针对分区中下一行对表达式求值的结果。如果没有下一行（因为当前行是最后一行），则返回 `NULL`。

如果提供了偏移量参数，它必须是非负整数。返回值是针对分区内当前行之后第 *offset* 行对表达式求值的结果。如果偏移量为 0，则针对当前行对表达式求值。如果当前行之后不存在第 *offset* 行，则返回 `NULL`。

如果还提供了默认值，则当偏移量所指的行不存在时，返回该默认值而不是 `NULL`。

另请参阅：
[[` SELECT `]](/language-spec/select) [[` LAG `]](/language-spec/functions/window/lag)

* * *

## 语法 {#syntax}

```sql
SELECT LEAD(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*expr*__
针对后面的行求值的表达式。

__*offset*__
可选。一个非负整数，指定向后查看的行数。默认为 1。

__*default*__
可选。偏移量所指的行不存在时返回的值。默认为 `NULL`。

__*PARTITION BY column*__
可选。将结果集划分为多个分区。`LEAD` 函数分别在每个分区内应用。

__*ORDER BY column*__
指定处理行的顺序。

## 返回值 {#return-values}

返回表达式针对指定的后面一行求值所得的值；如果不存在这样的行，则返回 `NULL`（或默认值）。

* * *

## 示例 {#examples}

### 向后查看下一个发布版本 {#look-ahead-to-the-next-release}

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

有关更多信息，请参阅 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
