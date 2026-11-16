---
title: WITH (CTEs)
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 WITH: 단일 SELECT, INSERT, UPDATE 또는 DELETE 문으로 범위가 한정된 이름 있는 임시 결과 집합인 공통 테이블 식(CTE)을 정의합니다."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

뒤이은 `SELECT`, `INSERT`, `UPDATE` 또는 `DELETE` 문에서 사용할 공통 테이블 식(CTE)을 정의합니다. CTE는 쿼리가 실행되는 동안에만 존재하는, 이름이 지정된 임시 결과 집합 역할을 합니다.

참고 항목:
[[` SELECT `]](/language-spec/select) [[` INSERT `]](/language-spec/insert) [[` UPDATE `]](/language-spec/update) [[` DELETE `]](/language-spec/delete)

* * *

## 구문 {#syntax}

*withClause::=*

<RailroadDiagram
type="withClause"
/>

*cteDefinition::=*

<RailroadDiagram
type="cteDefinition"
/>

&nbsp;
&nbsp;

```sql
WITH [RECURSIVE] cte_name [(column_name, ...)] AS (
    select_statement
)
[, cte_name [(column_name, ...)] AS (
    select_statement
)]
SELECT ... FROM cte_name ...;
```

## 인수 {#arguments}

__*RECURSIVE*__  
CTE가 자기 자신을 참조할 수 있게 하는 선택적 키워드입니다. 이를 통해 계층적 데이터 순회와 같은 재귀 쿼리를 작성할 수 있습니다.

__*cte_name*__  
CTE에 할당되는 이름입니다. 이 이름은 주 쿼리에서 테이블처럼 참조할 수 있습니다.

__*column_name*__  
선택 사항입니다. CTE 결과 집합의 열 이름입니다. 지정하지 않으면 열 이름은 SELECT 문에서 파생됩니다.

__*select_statement*__  
CTE의 결과 집합을 정의하는 쿼리입니다.

## 반환 값 {#return-values}

CTE는 값을 직접 반환하지 않습니다. 주 쿼리에서 참조할 수 있는 임시 결과 집합을 정의합니다.

* * *

## 예제 {#examples}

### 복잡한 쿼리 구성을 위한 기본 CTE {#basic-cte-for-organizing-complex-queries}

CTE를 사용하면 복잡한 쿼리를 이름이 지정된 논리적 구성 요소로 나누어 단순화할 수 있습니다.

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

### `UNION ALL` 연산자를 사용한 여러 소스의 데이터 결합 {#combining-data-from-multiple-sources-with-union-all}

CTE는 분석을 수행하기 전에 여러 소스의 데이터를 결합하는 데 유용합니다.

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

### 순위 지정을 위한 CTE와 윈도우 함수 사용 {#using-ctes-with-window-functions-for-ranking}

```sql
-- Rank contributors within each repo using CTE and window functions
WITH repo_contributors AS (
    SELECT
        repo,
        login,
        contributions,
        RANK() OVER (PARTITION BY repo ORDER BY contributions DESC) as rank_in_repo,
        SUM(contributions) OVER (PARTITION BY repo) as repo_total_contributions
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

### 단일 쿼리의 여러 CTE {#multiple-ctes-in-a-single-query}

쉼표로 구분하여 여러 CTE를 정의할 수 있습니다.

```sql
-- Analyze issues and releases together
WITH open_issues AS (
    SELECT number, title, created_at
    FROM github.issues.issues
    WHERE owner = 'stackql'
      AND repo = 'stackql'
      AND state = 'open'
),
recent_releases AS (
    SELECT tag_name, published_at
    FROM github.repos.releases
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    ORDER BY published_at DESC
    LIMIT 5
)
SELECT
    'Open Issues' as category,
    COUNT(*) as count
FROM open_issues
UNION ALL
SELECT
    'Recent Releases' as category,
    COUNT(*) as count
FROM recent_releases;
```

## 재귀 CTE {#recursive-ctes}

재귀 CTE를 사용하면 CTE가 자기 자신을 참조할 수 있으므로, 계층적 데이터 구조나 그래프 형태의 데이터 구조를 순회하는 쿼리를 작성할 수 있습니다.

```sql
WITH RECURSIVE cte_name (columns) AS (
    -- Base case (anchor member)
    SELECT ...
    UNION ALL
    -- Recursive case (references cte_name)
    SELECT ... FROM cte_name WHERE ...
)
SELECT * FROM cte_name;
```

:::tip

재귀 CTE는 조직도, 파일 시스템, 종속성 트리와 같은 계층적 데이터에 효과적입니다. 무한 루프를 방지하려면 재귀 부분에 항상 종료 조건을 포함하세요.

:::

## 모범 사례 {#best-practices}

1. **가독성을 위한 CTE 사용**: 복잡한 쿼리를 이름이 지정된 논리적 구성 요소로 나눕니다.
2. **윈도우 함수와 결합**: CTE는 윈도우 함수와 함께 사용하면 고급 분석에 효과적입니다.
3. **여러 CTE 연결**: 뒤의 CTE에서 앞의 CTE를 참조하여 복잡한 로직을 단계별로 구성합니다.
4. **성능 고려**: CTE는 가독성을 높이지만 매우 큰 CTE는 성능에 영향을 줄 수 있습니다. 실제 데이터 양으로 테스트하세요.

자세한 내용은 [https://sqlite.org/lang_with.html](https://sqlite.org/lang_with.html) 문서를 참조하세요.
