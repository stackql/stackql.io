---
title: 윈도우 함수
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 윈도우 함수: PARTITION BY, ORDER BY, 프레임 절을 사용하여 관련 행에 걸친 순위, 누계, 오프셋, 분포를 계산합니다."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

윈도우 함수는 현재 행과 관련된 행 집합에 걸쳐 계산을 수행합니다. 행 그룹에 대해 단일 결과를 반환하는 집계 함수와 달리, 윈도우 함수는 관련 행의 "윈도우"를 고려하면서 각 행에 대해 값을 반환합니다.

참고 항목:
[[` SELECT `]](/language-spec/select) [[` WITH (CTEs) `]](/language-spec/with) [[ 윈도우 함수 참조 ]](/language-spec/functions/window/row_number)

* * *

## 구문 {#syntax}

윈도우 함수는 `OVER` 절을 사용하여 윈도우 사양을 정의합니다.

```sql
SELECT <windowFunction> OVER ( <windowSpec> ) FROM <multipartIdentifier>;
```

*windowFunctionCall::=*

<RailroadDiagram 
type="windowFunctionCall"
/>

### 윈도우 함수 {#window-function}

윈도우 함수는 윈도우 사양으로 정의된 행 집합에 걸쳐 계산을 수행하는 특수 함수입니다. 전용 윈도우 함수(`ROW_NUMBER`, `LAG` 등)일 수도 있고, `OVER` 절과 함께 사용하는 집계 함수(`SUM`, `COUNT` 등)일 수도 있습니다.

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

### 윈도우 사양 {#window-specification}

윈도우 사양은 윈도우 함수 계산을 위해 행을 분할하고, 정렬하고, 프레임을 지정하는 방법을 정의합니다. `PARTITION BY` 절은 행을 그룹으로 나누고, `ORDER BY` 절은 각 파티션 내의 순서를 결정하며, 선택적 프레임 절은 현재 행을 기준으로 어떤 행을 계산에 포함할지 지정합니다.

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

## 사용 가능한 윈도우 함수 {#available-window-functions}

| 범주 | 함수 |
|----------|-----------|
| **순위** | [__`ROW_NUMBER()`__](/language-spec/functions/window/row_number), [__`RANK()`__](/language-spec/functions/window/rank), [__`DENSE_RANK()`__](/language-spec/functions/window/dense_rank), [__`NTILE()`__](/language-spec/functions/window/ntile) |
| **오프셋** | [__`LAG()`__](/language-spec/functions/window/lag), [__`LEAD()`__](/language-spec/functions/window/lead), [__`FIRST_VALUE()`__](/language-spec/functions/window/first_value), [__`LAST_VALUE()`__](/language-spec/functions/window/last_value), [__`NTH_VALUE()`__](/language-spec/functions/window/nth_value) |
| **분포** | [__`PERCENT_RANK()`__](/language-spec/functions/window/percent_rank), [__`CUME_DIST()`__](/language-spec/functions/window/cume_dist) |
| **집계** | [__`SUM()`__](/language-spec/functions/aggregate/sum), [__`COUNT()`__](/language-spec/functions/aggregate/count), [__`AVG()`__](/language-spec/functions/aggregate/avg), [__`MIN()`__](/language-spec/functions/aggregate/min), [__`MAX()`__](/language-spec/functions/aggregate/max) (__`OVER`__ 절과 함께 사용) |

* * *

## 예제 {#examples}

### `ROW_NUMBER`, `RANK`, `DENSE_RANK` 함수를 사용한 순위 지정 {#ranking-with-row_number-rank-and-dense_rank}

순위 함수를 사용하여 열 값을 기준으로 행에 순위를 지정합니다.

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

### 누계 및 백분율 {#running-totals-and-percentages}

`SUM`, `COUNT` 같은 집계 함수를 윈도우 함수로 사용하여 누계와 백분율을 계산할 수 있습니다.

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

### `LAG` 및 `LEAD` 함수를 사용한 행 비교 {#comparing-rows-with-lag-and-lead}

`LAG` 함수와 `LEAD` 함수를 사용하여 이전 행이나 다음 행의 값에 액세스합니다.

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

### `NTILE` 함수를 사용한 그룹 생성 {#creating-groups-with-ntile}

`NTILE` 함수를 사용하여 행을 지정된 수의 그룹으로 나눕니다.

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

### `PARTITION BY` 절을 사용한 그룹별 분석 {#using-partition-by-for-grouped-analysis}

`PARTITION BY` 절을 사용하여 그룹 내에서 윈도우 계산을 수행합니다.

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

### 윈도우 프레임 사용 {#using-window-frames}

프레임 절을 지정하여 윈도우 계산에 포함할 행을 제어합니다.

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

각 윈도우 함수에 대한 자세한 문서는 [윈도우 함수 참조](/language-spec/functions/window/row_number)를 참조하세요.

자세한 내용은 [https://sqlite.org/windowfunctions.html](https://sqlite.org/windowfunctions.html) 문서를 참조하세요.
