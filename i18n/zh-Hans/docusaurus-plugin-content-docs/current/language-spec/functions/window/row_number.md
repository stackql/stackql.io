---
title: ROW_NUMBER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 ROW_NUMBER 窗口函数：按窗口的 ORDER BY 子句给出的顺序，从 1 开始为分区内的行编号。"
image: "/img/stackql-featured-image.png"
---
返回行在当前分区内的编号。行按照窗口定义中 `ORDER BY` 子句定义的顺序从 1 开始编号，否则按任意顺序编号。

另请参阅：
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## 语法 {#syntax}

```sql
SELECT ROW_NUMBER() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*PARTITION BY column*__
可选。将结果集划分为多个分区。`ROW_NUMBER` 函数分别应用于每个分区，并为每个分区重置行号。

__*ORDER BY column*__
指定分配行号的顺序。要获得有意义的结果，必须指定。

## 返回值 {#return-values}

返回一个整数，表示分区内的顺序行号，从 1 开始。

* * *

## 示例 {#examples}

### 在仓库内对贡献者排名 {#rank-contributors-within-a-repository}

```sql
-- Rank contributors within a repository
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as contribution_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### 跟踪 issue 随时间的顺序 {#track-issue-sequence-over-time}

```sql
-- Track issue growth over time
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### 比较 `ROW_NUMBER` 与 `RANK` 和 `DENSE_RANK` {#compare-row_number-with-rank-and-dense_rank}

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

有关更多信息，请参阅 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
