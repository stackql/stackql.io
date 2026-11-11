---
title: PERCENT_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 PERCENT_RANK 窗口函数：当前行在其分区内的相对排名，取值介于 0.0 和 1.0 之间。"
image: "/img/stackql-featured-image.png"
---
返回一个介于 0.0 和 1.0 之间的值，表示当前行在其分区内的相对排名。

尽管名称如此，该函数返回的值等于 `(rank - 1) / (partition-rows - 1)`，其中 `rank` 是内置窗口函数 `RANK()` 返回的值，`partition-rows` 是分区中的总行数。如果分区只包含一行，该函数返回 0.0。

另请参阅：
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` CUME_DIST `]](/language-spec/functions/window/cume_dist)

* * *

## 语法 {#syntax}

```sql
SELECT PERCENT_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*PARTITION BY column*__
可选。将结果集划分为多个分区。`PERCENT_RANK` 函数分别应用于每个分区。

__*ORDER BY column*__
指定计算百分位排名的顺序。

## 返回值 {#return-values}

返回一个介于 0.0 和 1.0 之间的浮点数，表示百分位排名。

* * *

## 示例 {#examples}

### 计算贡献者的百分位排名 {#calculate-percentile-ranking-of-contributors}

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

`PERCENT_RANK` 值为 0.0 表示排名最低的行，1.0 表示排名最高的行。使用此函数可以确定某个值在分布中所处的位置。

:::

有关更多信息，请参阅 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
