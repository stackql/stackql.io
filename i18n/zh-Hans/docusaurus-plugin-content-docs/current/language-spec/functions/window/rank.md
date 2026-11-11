---
title: RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 RANK 窗口函数：当前行在其分区内留有间隔的排名，并列的行排名相同，其后的排名被跳过。"
image: "/img/stackql-featured-image.png"
---
返回当前行的排名，排名留有间隔。该值是每组中第一个同级行的 `ROW_NUMBER`。排序列的值相等的行获得相同的排名，下一个排名值按并列行的数量递增（留有间隔）。

如果没有 `ORDER BY` 子句，则所有行都被视为同级行，该函数始终返回 1。

另请参阅：
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## 语法 {#syntax}

```sql
SELECT RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*PARTITION BY column*__
可选。将结果集划分为多个分区。`RANK` 函数分别应用于每个分区。

__*ORDER BY column*__
指定分配排名的顺序。如果没有 `ORDER BY` 子句，则所有行都被视为同级行，排名均为 1。

## 返回值 {#return-values}

返回一个整数，表示当前行在分区内的排名，并列的值会产生间隔。

* * *

## 示例 {#examples}

### 按贡献数对贡献者排名（留有间隔） {#rank-contributors-by-contributions-with-gaps}

```sql
-- Rank contributors with gaps for ties
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### 比较 `RANK` 与 `DENSE_RANK` 和 `ROW_NUMBER` {#compare-rank-with-dense_rank-and-row_number}

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

### 使用 `PARTITION BY` 在每个仓库内对贡献者排名 {#rank-contributors-within-each-repository-using-partition-by}

```sql
-- Rank contributors within each repo
WITH repo_contributors AS (
    SELECT
        repo,
        login,
        contributions,
        RANK() OVER (PARTITION BY repo ORDER BY contributions DESC) as rank_in_repo
    FROM (
        SELECT 'stackql' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql'
        UNION ALL
        SELECT 'stackql-provider-registry' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
    ) t
)
SELECT
    repo,
    login,
    contributions,
    rank_in_repo
FROM repo_contributors
ORDER BY repo, rank_in_repo;
```

有关更多信息，请参阅 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
