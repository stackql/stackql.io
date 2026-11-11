---
title: LAG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 LAG 窗口函数：针对分区中前面的某一行对表达式求值，可指定偏移量和默认值。"
image: "/img/stackql-featured-image.png"
---
返回针对分区中前面的某一行对表达式求值的结果。

`LAG()` 的第一种形式返回针对分区中上一行对表达式求值的结果。如果没有上一行（因为当前行是第一行），则返回 `NULL`。

如果提供了偏移量参数，它必须是非负整数。返回值是针对分区内当前行之前第 *offset* 行对表达式求值的结果。如果偏移量为 0，则针对当前行对表达式求值。如果当前行之前不存在第 *offset* 行，则返回 `NULL`。

如果还提供了默认值，则当偏移量所指的行不存在时，返回该默认值而不是 `NULL`。

另请参阅：
[[` SELECT `]](/language-spec/select) [[` LEAD `]](/language-spec/functions/window/lead)

* * *

## 语法 {#syntax}

```sql
SELECT LAG(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*expr*__
针对前面的行求值的表达式。

__*offset*__
可选。一个非负整数，指定向前回溯的行数。默认为 1。

__*default*__
可选。偏移量所指的行不存在时返回的值。默认为 `NULL`。

__*PARTITION BY column*__
可选。将结果集划分为多个分区。`LAG` 函数分别在每个分区内应用。

__*ORDER BY column*__
指定处理行的顺序。

## 返回值 {#return-values}

返回表达式针对指定的前面一行求值所得的值；如果不存在这样的行，则返回 `NULL`（或默认值）。

* * *

## 示例 {#examples}

### 将每个发布版本与上一个发布版本进行比较 {#compare-each-release-to-the-previous-release}

```sql
-- Compare each release to previous and next release dates
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LAG(published_at, 1) OVER (ORDER BY published_at) as previous_release_date,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### 分析 issue 创建之间的间隔 {#analyze-gaps-between-issue-creation}

```sql
-- Analyze gaps between issue creation
SELECT
    number,
    title,
    created_at,
    LAG(created_at, 1) OVER (ORDER BY created_at) as prev_issue_date,
    julianday(created_at) - julianday(LAG(created_at, 1) OVER (ORDER BY created_at)) as days_since_prev_issue
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

有关更多信息，请参阅 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
