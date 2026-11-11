---
title: NTH_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 NTH_VALUE 窗口函数：表达式针对窗口帧第 N 行的值。"
image: "/img/stackql-featured-image.png"
---
返回表达式针对窗口帧中第 N 行求值所得的值。

此内置窗口函数以与聚合窗口函数相同的方式为每一行计算窗口帧。它返回表达式针对窗口帧的第 N 行求值所得的值。如果存在 `ORDER BY` 子句，行在窗口帧内按照该子句定义的顺序从 1 开始编号，否则按任意顺序编号。如果分区中不存在第 N 行，则返回 `NULL`。

另请参阅：
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` LAST_VALUE `]](/language-spec/functions/window/last_value)

* * *

## 语法 {#syntax}

```sql
SELECT NTH_VALUE(expr, N) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*expr*__
针对窗口帧中第 N 行求值的表达式。

__*N*__
一个正整数，指定要获取的行号（从 1 开始计数）。

__*PARTITION BY column*__
可选。将结果集划分为多个分区。`NTH_VALUE` 函数分别在每个分区内应用。

__*ORDER BY column*__
指定处理行的顺序。

__*ROWS BETWEEN frame_start AND frame_end*__
可选。定义窗口帧。

## 返回值 {#return-values}

返回表达式针对窗口帧中第 N 行求值所得的值；如果第 N 行不存在，则返回 `NULL`。

* * *

## 示例 {#examples}

### 获取贡献数第二高的贡献者 {#get-the-second-highest-contributor}

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

有关更多信息，请参阅 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
