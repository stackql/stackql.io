---
title: ウィンドウ関数
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL のウィンドウ関数：PARTITION BY、ORDER BY、フレーム句を使用し、関連する行にわたって順位、累計、オフセット、分布を計算します。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

ウィンドウ関数は、現在の行に関連する行の集合にわたって計算を行います。行のグループに対して単一の結果を返す集約関数とは異なり、ウィンドウ関数は関連する行の「ウィンドウ」を考慮しながら、行ごとに値を返します。

関連項目：
[[` SELECT `]](/language-spec/select) [[` WITH (CTEs) `]](/language-spec/with) [[ ウィンドウ関数リファレンス ]](/language-spec/functions/window/row_number)

* * *

## 構文 {#syntax}

ウィンドウ関数は、`OVER` 句を使用してウィンドウ指定を定義します。

```sql
SELECT <windowFunction> OVER ( <windowSpec> ) FROM <multipartIdentifier>;
```

*windowFunctionCall::=*

<RailroadDiagram 
type="windowFunctionCall"
/>

### ウィンドウ関数 {#window-function}

ウィンドウ関数は、ウィンドウ指定で定義された行の集合にわたって計算を行う特殊な関数です。専用のウィンドウ関数（`ROW_NUMBER` や `LAG` など）と、`OVER` 句と組み合わせて使用する集約関数（`SUM` や `COUNT` など）のどちらも使用できます。

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

### ウィンドウ指定 {#window-specification}

ウィンドウ指定は、ウィンドウ関数の計算のために行をどのようにパーティション分割し、並べ替え、フレーム化するかを定義します。`PARTITION BY` は行をグループに分割し、`ORDER BY` は各パーティション内の順序を決定します。省略可能なフレーム句は、現在の行を基準として、どの行を計算に含めるかを指定します。

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

## 利用可能なウィンドウ関数 {#available-window-functions}

| カテゴリ | 関数 |
|----------|-----------|
| **順位付け** | [__`ROW_NUMBER()`__](/language-spec/functions/window/row_number), [__`RANK()`__](/language-spec/functions/window/rank), [__`DENSE_RANK()`__](/language-spec/functions/window/dense_rank), [__`NTILE()`__](/language-spec/functions/window/ntile) |
| **オフセット** | [__`LAG()`__](/language-spec/functions/window/lag), [__`LEAD()`__](/language-spec/functions/window/lead), [__`FIRST_VALUE()`__](/language-spec/functions/window/first_value), [__`LAST_VALUE()`__](/language-spec/functions/window/last_value), [__`NTH_VALUE()`__](/language-spec/functions/window/nth_value) |
| **分布** | [__`PERCENT_RANK()`__](/language-spec/functions/window/percent_rank), [__`CUME_DIST()`__](/language-spec/functions/window/cume_dist) |
| **集約** | [__`SUM()`__](/language-spec/functions/aggregate/sum), [__`COUNT()`__](/language-spec/functions/aggregate/count), [__`AVG()`__](/language-spec/functions/aggregate/avg), [__`MIN()`__](/language-spec/functions/aggregate/min), [__`MAX()`__](/language-spec/functions/aggregate/max)（__`OVER`__ 句と組み合わせて使用） |

* * *

## 例 {#examples}

### `ROW_NUMBER`、`RANK`、`DENSE_RANK` による順位付け {#ranking-with-row_number-rank-and-dense_rank}

順位付け関数を使用して、列の値に基づいて行に順位を割り当てます。

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

### 累計と割合 {#running-totals-and-percentages}

`SUM` や `COUNT` などの集約関数をウィンドウ関数として使用し、累計や割合を計算できます。

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

### `LAG` と `LEAD` による行の比較 {#comparing-rows-with-lag-and-lead}

`LAG` と `LEAD` を使用して、前の行または後続の行の値にアクセスします。

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

### `NTILE` によるグループの作成 {#creating-groups-with-ntile}

`NTILE` を使用して、行を指定した数のグループに分割します。

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

### `PARTITION BY` を使用したグループ別の分析 {#using-partition-by-for-grouped-analysis}

`PARTITION BY` を使用して、グループ内でウィンドウ計算を行います。

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

### ウィンドウフレームの使用 {#using-window-frames}

フレーム句を指定して、ウィンドウ計算に含める行を制御します。

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

各ウィンドウ関数の詳しいドキュメントについては、[ウィンドウ関数リファレンス](/language-spec/functions/window/row_number)を参照してください。

詳細については、[https://sqlite.org/windowfunctions.html](https://sqlite.org/windowfunctions.html) を参照してください。
