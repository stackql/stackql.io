---
title: 視窗函式
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的視窗函式：搭配 PARTITION BY、ORDER BY 與框架子句，跨相關資料列計算排名、累計總和、位移與分佈。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

視窗函式會針對與目前資料列相關的一組資料列執行計算。彙總函式會為一組資料列傳回單一結果，視窗函式則不同，會為每一資料列傳回一個值，同時考量由相關資料列構成的「視窗」。

另請參閱：
[[` SELECT `]](/language-spec/select) [[` WITH (CTEs) `]](/language-spec/with) [[ 視窗函式參考 ]](/language-spec/functions/window/row_number)

* * *

## 語法 {#syntax}

視窗函式使用 `OVER` 子句定義視窗規格：

```sql
SELECT <windowFunction> OVER ( <windowSpec> ) FROM <multipartIdentifier>;
```

*windowFunctionCall::=*

<RailroadDiagram 
type="windowFunctionCall"
/>

### 視窗函式 {#window-function}

視窗函式是專門的函式，會針對視窗規格所定義的一組資料列執行計算。它們可以是專用的視窗函式（例如 `ROW_NUMBER` 或 `LAG`），也可以是搭配 `OVER` 子句使用的彙總函式（例如 `SUM` 或 `COUNT`）。

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

### 視窗規格 {#window-specification}

視窗規格定義在視窗函式計算時，資料列如何分割、排序與界定框架。`PARTITION BY` 將資料列分成群組，`ORDER BY` 決定每個分割區內的順序，選用的框架子句則指定相對於目前資料列的哪些資料列要納入計算。

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

## 可用的視窗函式 {#available-window-functions}

| 類別 | 函式 |
|----------|-----------|
| **排名** | [__`ROW_NUMBER()`__](/language-spec/functions/window/row_number), [__`RANK()`__](/language-spec/functions/window/rank), [__`DENSE_RANK()`__](/language-spec/functions/window/dense_rank), [__`NTILE()`__](/language-spec/functions/window/ntile) |
| **位移** | [__`LAG()`__](/language-spec/functions/window/lag), [__`LEAD()`__](/language-spec/functions/window/lead), [__`FIRST_VALUE()`__](/language-spec/functions/window/first_value), [__`LAST_VALUE()`__](/language-spec/functions/window/last_value), [__`NTH_VALUE()`__](/language-spec/functions/window/nth_value) |
| **分佈** | [__`PERCENT_RANK()`__](/language-spec/functions/window/percent_rank), [__`CUME_DIST()`__](/language-spec/functions/window/cume_dist) |
| **彙總** | [__`SUM()`__](/language-spec/functions/aggregate/sum), [__`COUNT()`__](/language-spec/functions/aggregate/count), [__`AVG()`__](/language-spec/functions/aggregate/avg), [__`MIN()`__](/language-spec/functions/aggregate/min), [__`MAX()`__](/language-spec/functions/aggregate/max)，搭配 __`OVER`__ 子句使用 |

* * *

## 範例 {#examples}

### 使用 `ROW_NUMBER`、`RANK` 與 `DENSE_RANK` 進行排名 {#ranking-with-row_number-rank-and-dense_rank}

使用排名函式，依據資料行的值為資料列指派排名。

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

### 累計總和與百分比 {#running-totals-and-percentages}

`SUM` 與 `COUNT` 等彙總函式可以當作視窗函式使用，以計算累計總和與百分比。

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

### 使用 `LAG` 與 `LEAD` 比較資料列 {#comparing-rows-with-lag-and-lead}

使用 `LAG` 與 `LEAD` 存取前一筆或後一筆資料列的值。

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

### 使用 `NTILE` 建立群組 {#creating-groups-with-ntile}

使用 `NTILE` 將資料列分成指定數量的群組。

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

### 使用 `PARTITION BY` 進行分組分析 {#using-partition-by-for-grouped-analysis}

使用 `PARTITION BY` 在群組內執行視窗計算。

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

### 使用視窗框架 {#using-window-frames}

指定框架子句，以控制哪些資料列要納入視窗計算。

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

如需各個視窗函式的詳細文件，請參閱[視窗函式參考](/language-spec/functions/window/row_number)。

如需詳細資訊，請參閱 [https://sqlite.org/windowfunctions.html](https://sqlite.org/windowfunctions.html)。
