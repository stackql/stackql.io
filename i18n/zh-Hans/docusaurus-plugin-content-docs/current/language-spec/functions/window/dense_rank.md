---
title: DENSE_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 DENSE_RANK 窗口函数：当前行在其分区内不留间隔的排名，并列的行排名相同，下一个排名紧随其后。"
image: "/img/stackql-featured-image.png"
---
返回当前行的排名，排名不留间隔。该值是当前行所在的同级组在其分区内的编号。排序列的值相等的行获得相同的排名，下一个排名值总是加 1（不留间隔）。

行按照窗口定义中 `ORDER BY` 子句定义的顺序从 1 开始编号。如果没有 `ORDER BY` 子句，则所有行都被视为同级行，该函数始终返回 1。

另请参阅：
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## 语法 {#syntax}

```sql
SELECT DENSE_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*PARTITION BY column*__
可选。将结果集划分为多个分区。`DENSE_RANK` 函数分别应用于每个分区。

__*ORDER BY column*__
指定分配排名的顺序。如果没有 `ORDER BY` 子句，则所有行都被视为同级行，排名均为 1。

## 返回值 {#return-values}

返回一个整数，表示当前行在分区内的密集排名，并列的值不会产生间隔。

* * *

## 示例 {#examples}

### 按贡献数对贡献者排名（不留间隔） {#rank-contributors-by-contributions-without-gaps}

```sql
-- Rank contributors without gaps for ties
SELECT
    DENSE_RANK() OVER (ORDER BY contributions DESC) as rank,
    login,
    contributions,
    html_url
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo IN ('stackql', 'stackql-deploy', 'stackql-provider-registry')
ORDER BY contributions DESC;
```

### 比较 `DENSE_RANK` 与 `RANK` 和 `ROW_NUMBER` {#compare-dense_rank-with-rank-and-row_number}

```sql
-- Compare RANK vs DENSE_RANK vs ROW_NUMBER for contributors
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### 对跨多个仓库聚合的贡献数排名 {#rank-aggregated-contributions-across-multiple-repositories}

```sql
-- Rank total contributions across multiple repos
WITH all_contributors AS (
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions,
    MAX(html_url) as html_url
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

有关更多信息，请参阅 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
