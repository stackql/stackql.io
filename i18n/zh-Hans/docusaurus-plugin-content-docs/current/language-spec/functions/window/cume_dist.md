---
title: CUME_DIST
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 CUME_DIST 窗口函数：当前行在其分区内的累积分布，即行号除以分区行数。"
image: "/img/stackql-featured-image.png"
---
返回某个值在分区内的累积分布。计算方式为 `row-number / partition-rows`，其中 `row-number` 是 `ROW_NUMBER()` 为组中最后一个同级行返回的值，`partition-rows` 是分区中的行数。

另请参阅：
[[` SELECT `]](/language-spec/select) [[` PERCENT_RANK `]](/language-spec/functions/window/percent_rank) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## 语法 {#syntax}

```sql
SELECT CUME_DIST() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*PARTITION BY column*__
可选。将结果集划分为多个分区。`CUME_DIST` 函数分别应用于每个分区。

__*ORDER BY column*__
指定计算累积分布的顺序。

## 返回值 {#return-values}

返回一个介于 0.0 和 1.0 之间的浮点数，表示累积分布值。

* * *

## 示例 {#examples}

### 计算贡献者的累积分布 {#calculate-cumulative-distribution-of-contributors}

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

`CUME_DIST` 返回其值小于或等于当前行的值的行所占的比例。值为 0.75 表示 75% 的行的值小于或等于当前行的值。

:::

有关更多信息，请参阅 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
