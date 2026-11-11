---
title: NTILE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 NTILE 窗口函数：将分区尽可能均匀地划分为 N 组，并用 1 到 N 为每行所在的组编号。"
image: "/img/stackql-featured-image.png"
---
将分区尽可能均匀地划分为 N 组，并按照 `ORDER BY` 子句定义的顺序为每组分配一个介于 1 和 N 之间的整数，否则按任意顺序分配。必要时，较大的组排在前面。

另请参阅：
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## 语法 {#syntax}

```sql
SELECT NTILE(N) OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*N*__
一个正整数，指定将分区划分成的组（桶）的数量。

__*PARTITION BY column*__
可选。将结果集划分为多个分区。`NTILE` 函数分别应用于每个分区。

__*ORDER BY column*__
指定将行分配到桶的顺序。

## 返回值 {#return-values}

返回一个介于 1 和 N 之间的整数，表示当前行所属的桶（组）。

* * *

## 示例 {#examples}

### 按贡献水平将贡献者分为四分位组 {#group-contributors-into-quartiles-by-contribution-level}

```sql
-- Group contributors into quartiles by contribution level
SELECT
    login,
    contributions,
    NTILE(4) OVER (ORDER BY contributions DESC) as contribution_quartile,
    CASE NTILE(4) OVER (ORDER BY contributions DESC)
        WHEN 1 THEN 'Top Contributors'
        WHEN 2 THEN 'Active Contributors'
        WHEN 3 THEN 'Moderate Contributors'
        WHEN 4 THEN 'Occasional Contributors'
    END as contributor_tier
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

使用 `NTILE` 可以在数据中创建分段或层级。常见用例包括将数据划分为四分位（N=4）、十分位（N=10）或百分位（N=100）。

:::

有关更多信息，请参阅 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
