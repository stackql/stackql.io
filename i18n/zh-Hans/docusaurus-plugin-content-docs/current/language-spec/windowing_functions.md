---
title: 窗口函数
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的窗口函数：对相关行计算排名、累计总和、偏移和分布，支持 PARTITION BY、ORDER BY 和帧子句。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

窗口函数对与当前行相关的一组行执行计算。与为一组行返回单个结果的聚合函数不同，窗口函数为每一行返回一个值，同时考虑由相关行组成的“窗口”。

另请参阅：
[[` SELECT `]](/language-spec/select) [[` WITH (CTEs) `]](/language-spec/with) [[ 窗口函数参考 ]](/language-spec/functions/window/row_number)

* * *

## 语法 {#syntax}

窗口函数使用 `OVER` 子句定义窗口规范：

```sql
SELECT <windowFunction> OVER ( <windowSpec> ) FROM <multipartIdentifier>;
```

*windowFunctionCall::=*

<RailroadDiagram 
type="windowFunctionCall"
/>

### 窗口函数 {#window-function}

窗口函数是一类专用函数，对窗口规范定义的一组行执行计算。它们既可以是专用的窗口函数（如 `ROW_NUMBER` 或 `LAG`），也可以是与 `OVER` 子句一起使用的聚合函数（如 `SUM` 或 `COUNT`）。

```sql
{ <aggregateFunction> | ROW_NUMBER() | RANK() | DENSE_RANK() | PERCENT_RANK() 
  | CUME_DIST() | NTILE(<int>) | LAG(<expr> [,<offset> [,<default>]]) 
  | LEAD(<expr> [,<offset> [,<default>]]) | FIRST_VALUE(<expr>) 
  | LAST_VALUE(<expr>) | NTH_VALUE(<expr>, <int>) }
```  

*windowFunction::=*

<RailroadDiagram 
type="windowFunction"
/>

### 窗口规范 {#window-specification}

窗口规范定义在窗口函数计算中如何对行进行分区、排序和划定帧。`PARTITION BY` 将行划分为多个组，`ORDER BY` 确定每个分区内的顺序，可选的帧子句指定相对于当前行的哪些行参与计算。

```sql
[ <windowName> ]
[ PARTITION BY <fieldList> ]
[ ORDER BY <fieldList> [ ASC | DESC ] ]
[ { ROWS | RANGE | GROUPS } 
    { UNBOUNDED PRECEDING | <int> PRECEDING | CURRENT ROW }
  | { ROWS | RANGE | GROUPS } BETWEEN 
    { UNBOUNDED PRECEDING | <int> PRECEDING | CURRENT ROW | <int> FOLLOWING }
    AND
    { <int> PRECEDING | CURRENT ROW | <int> FOLLOWING | UNBOUNDED FOLLOWING } ]
```

*windowSpec::=*

<RailroadDiagram 
type="windowSpec"
/>

## 可用的窗口函数 {#available-window-functions}

| 类别 | 函数 |
|----------|-----------|
| **排名** | [__`ROW_NUMBER()`__](/language-spec/functions/window/row_number)、[__`RANK()`__](/language-spec/functions/window/rank)、[__`DENSE_RANK()`__](/language-spec/functions/window/dense_rank)、[__`NTILE()`__](/language-spec/functions/window/ntile) |
| **偏移** | [__`LAG()`__](/language-spec/functions/window/lag)、[__`LEAD()`__](/language-spec/functions/window/lead)、[__`FIRST_VALUE()`__](/language-spec/functions/window/first_value)、[__`LAST_VALUE()`__](/language-spec/functions/window/last_value)、[__`NTH_VALUE()`__](/language-spec/functions/window/nth_value) |
| **分布** | [__`PERCENT_RANK()`__](/language-spec/functions/window/percent_rank)、[__`CUME_DIST()`__](/language-spec/functions/window/cume_dist) |
| **聚合** | [__`SUM()`__](/language-spec/functions/aggregate/sum)、[__`COUNT()`__](/language-spec/functions/aggregate/count)、[__`AVG()`__](/language-spec/functions/aggregate/avg)、[__`MIN()`__](/language-spec/functions/aggregate/min)、[__`MAX()`__](/language-spec/functions/aggregate/max)，配合 __`OVER`__ 子句使用 |

* * *

## 示例 {#examples}

### 使用 `ROW_NUMBER`、`RANK` 和 `DENSE_RANK` 进行排名 {#ranking-with-row_number-rank-and-dense_rank}

使用排名函数根据列值为行分配排名。

```sql
-- Rank contributors by contribution count
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### 累计总和与百分比 {#running-totals-and-percentages}

`SUM` 和 `COUNT` 等聚合函数可以用作窗口函数，以计算累计总和与百分比。

```sql
-- Running total and percentage of contributions
SELECT
    login,
    contributions,
    SUM(contributions) OVER (ORDER BY contributions DESC) as running_total,
    SUM(contributions) OVER () as total_contributions,
    ROUND(100.0 * contributions / SUM(contributions) OVER (), 2) as pct_of_total
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### 使用 `LAG` 和 `LEAD` 比较行 {#comparing-rows-with-lag-and-lead}

使用 `LAG` 和 `LEAD` 访问前面或后面的行中的值。

```sql
-- Compare each release to previous release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LEAD(tag_name, 1) OVER (ORDER BY published_at) as next_release,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### 使用 `NTILE` 创建分组 {#creating-groups-with-ntile}

使用 `NTILE` 将行划分为指定数量的组。

```sql
-- Group contributors into quartiles
SELECT
    login,
    contributions,
    NTILE(4) OVER (ORDER BY contributions DESC) as quartile,
    CASE NTILE(4) OVER (ORDER BY contributions DESC)
        WHEN 1 THEN 'Top Contributors'
        WHEN 2 THEN 'Active Contributors'
        WHEN 3 THEN 'Moderate Contributors'
        WHEN 4 THEN 'Occasional Contributors'
    END as tier
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### 使用 `PARTITION BY` 进行分组分析 {#using-partition-by-for-grouped-analysis}

使用 `PARTITION BY` 在组内执行窗口计算。

```sql
-- Track cumulative issues by state
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence,
    COUNT(*) OVER (ORDER BY created_at) as cumulative_issues,
    COUNT(*) OVER (PARTITION BY state ORDER BY created_at) as cumulative_by_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### 使用窗口帧 {#using-window-frames}

指定帧子句以控制哪些行参与窗口计算。

```sql
-- Calculate moving average of commit activity
WITH weekly_totals AS (
    SELECT
        week,
        SUM(json_each.value) as commits_this_week
    FROM github.repos.stats_commit_activity, JSON_EACH(days)
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    GROUP BY week
)
SELECT
    week,
    commits_this_week,
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg,
    SUM(commits_this_week) OVER (ORDER BY week) as cumulative_commits
FROM weekly_totals
ORDER BY week;
```

有关每个窗口函数的详细文档，请参阅[窗口函数参考](/language-spec/functions/window/row_number)。

有关更多信息，请参阅 [https://sqlite.org/windowfunctions.html](https://sqlite.org/windowfunctions.html)。
