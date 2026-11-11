---
title: LAST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 LAST_VALUE 窗口函数：表达式针对窗口帧最后一行的值。"
image: "/img/stackql-featured-image.png"
---
返回表达式针对窗口帧中最后一行求值所得的值。

此内置窗口函数以与聚合窗口函数相同的方式为每一行计算窗口帧。它为每一行返回表达式针对窗口帧中最后一行求值所得的值。

另请参阅：
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` NTH_VALUE `]](/language-spec/functions/window/nth_value)

* * *

## 语法 {#syntax}

```sql
SELECT LAST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*expr*__
针对窗口帧中最后一行求值的表达式。

__*PARTITION BY column*__
可选。将结果集划分为多个分区。`LAST_VALUE` 函数分别在每个分区内应用。

__*ORDER BY column*__
指定处理行的顺序。

__*ROWS BETWEEN frame_start AND frame_end*__
可选。定义窗口帧。常用选项包括：
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`（默认）
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

## 返回值 {#return-values}

返回表达式针对窗口帧中最后一行求值所得的值。

* * *

## 示例 {#examples}

### 查找最新发布版本作为参照 {#find-the-latest-release-for-context}

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

默认情况下，窗口帧从分区开头延伸到当前行，这意味着 `LAST_VALUE` 会返回当前行的值。要获取分区中真正的最后一个值，请使用 `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`。

:::

有关更多信息，请参阅 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
