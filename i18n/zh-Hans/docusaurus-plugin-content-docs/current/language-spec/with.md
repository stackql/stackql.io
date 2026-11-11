---
title: WITH (CTEs)
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 WITH：定义公用表表达式（CTE），即作用范围限于单条 SELECT、INSERT、UPDATE 或 DELETE 语句的临时命名结果集。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

定义公用表表达式（CTE），供随后的 `SELECT`、`INSERT`、`UPDATE` 或 `DELETE` 语句使用。CTE 充当临时的命名结果集，仅在查询执行期间存在。

另请参阅：
[[` SELECT `]](/language-spec/select) [[` INSERT `]](/language-spec/insert) [[` UPDATE `]](/language-spec/update) [[` DELETE `]](/language-spec/delete)

* * *

## 语法 {#syntax}

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

## 参数 {#arguments}

__*RECURSIVE*__
可选关键字，允许 CTE 引用自身，从而支持递归查询，例如遍历层次结构数据。

__*cte_name*__
为 CTE 指定的名称。在主查询中可以像引用表一样引用该名称。

__*column_name*__
可选。CTE 结果集的列名。如果未指定，列名将从 SELECT 语句推导得出。

__*select_statement*__
定义 CTE 结果集的查询。

## 返回值 {#return-values}

CTE 不直接返回值。它们定义可在主查询中引用的临时结果集。

* * *

## 示例 {#examples}

### 用于组织复杂查询的基本 CTE {#basic-cte-for-organizing-complex-queries}

CTE 可以将复杂查询拆分为具名的逻辑组成部分，从而简化查询。

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

### 使用 `UNION ALL` 合并多个来源的数据 {#combining-data-from-multiple-sources-with-union-all}

CTE 适合在执行分析之前合并多个来源的数据。

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

### 将 CTE 与窗口函数结合用于排名 {#using-ctes-with-window-functions-for-ranking}

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

### 在单个查询中使用多个 CTE {#multiple-ctes-in-a-single-query}

可以定义多个 CTE，以逗号分隔。

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

## 递归 CTE {#recursive-ctes}

递归 CTE 允许 CTE 引用自身，从而可以编写遍历层次结构或图状数据结构的查询。

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

递归 CTE 在处理组织结构图、文件系统或依赖树等层次结构数据时非常强大。务必在递归情形中包含终止条件，以防止无限循环。

:::

## 最佳实践 {#best-practices}

1. **使用 CTE 提高可读性**：将复杂查询拆分为具名的逻辑组成部分。
2. **与窗口函数结合使用**：CTE 与窗口函数配合良好，适用于高级分析。
3. **串联多个 CTE**：在后面的 CTE 中引用前面的 CTE，逐步构建复杂逻辑。
4. **考虑性能**：虽然 CTE 能提高可读性，但非常大的 CTE 可能影响性能。请使用实际的数据量进行测试。

有关更多信息，请参阅 [https://sqlite.org/lang_with.html](https://sqlite.org/lang_with.html)。
