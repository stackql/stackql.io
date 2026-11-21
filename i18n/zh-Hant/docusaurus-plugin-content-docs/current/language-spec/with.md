---
title: WITH (CTEs)
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 WITH：定義通用資料表運算式（CTE），即範圍限於其後 SELECT 陳述式的暫時具名結果集。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

定義通用資料表運算式（CTE），供其後的 `SELECT` 陳述式使用。CTE 的作用如同暫時的具名結果集，只在查詢執行期間存在。

另請參閱：
[[` SELECT `]](/language-spec/select)

* * *

## 語法 {#syntax}

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

## 引數 {#arguments}

__*RECURSIVE*__  
選用的關鍵字，可讓 CTE 參照自身，以進行遞迴查詢，例如走訪階層式資料。

__*cte_name*__  
指派給 CTE 的名稱。在主查詢中可以像參照資料表一樣參照此名稱。

__*column_name*__  
選用。CTE 結果集的資料行名稱。若未指定，資料行名稱會從 SELECT 陳述式衍生。

__*select_statement*__  
定義 CTE 結果集的查詢。

## 傳回值 {#return-values}

CTE 不會直接傳回值。CTE 定義的是可在主查詢中參照的暫時結果集。

* * *

## 範例 {#examples}

### 使用基本 CTE 組織複雜查詢 {#basic-cte-for-organizing-complex-queries}

CTE 可以將複雜查詢拆分為合乎邏輯的具名元件，藉此簡化查詢。

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

### 使用 `UNION ALL` 合併多個來源的資料 {#combining-data-from-multiple-sources-with-union-all}

CTE 適合在執行分析之前合併多個來源的資料。

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

### 搭配視窗函式使用 CTE 進行排名 {#using-ctes-with-window-functions-for-ranking}

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

### 在單一查詢中使用多個 CTE {#multiple-ctes-in-a-single-query}

您可以定義多個 CTE，並以逗號分隔。

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

## 遞迴 CTE {#recursive-ctes}

遞迴 CTE 允許 CTE 參照自身，因此能執行走訪階層式或圖形結構資料的查詢。

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

遞迴 CTE 在處理組織圖、檔案系統或相依性樹狀結構等階層式資料時非常強大。請務必在遞迴部分加入終止條件，以避免無限迴圈。

:::

## 最佳做法 {#best-practices}

1. **使用 CTE 提升可讀性**：將複雜查詢拆分為合乎邏輯的具名元件。
2. **與視窗函式結合**：CTE 與視窗函式搭配良好，適合進行進階分析。
3. **串接多個 CTE**：在後面的 CTE 中參照前面的 CTE，逐步建構複雜的邏輯。
4. **考量效能**：雖然 CTE 能提升可讀性，但非常大的 CTE 可能影響效能。請以您實際的資料量進行測試。

如需詳細資訊，請參閱 [https://sqlite.org/lang_with.html](https://sqlite.org/lang_with.html)。
