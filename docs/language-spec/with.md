---
title: WITH (CTEs)
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Defines a Common Table Expression (CTE) for use in a subsequent `SELECT`, `INSERT`, `UPDATE`, or `DELETE` statement. CTEs act as temporary named result sets that exist only for the duration of the query.

See also:
[[` SELECT `]](/docs/language-spec/select) [[` INSERT `]](/docs/language-spec/insert) [[` UPDATE `]](/docs/language-spec/update) [[` DELETE `]](/docs/language-spec/delete)

* * *

## Syntax

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

## Arguments

__*RECURSIVE*__
Optional keyword that enables the CTE to reference itself, allowing for recursive queries such as traversing hierarchical data.

__*cte_name*__
The name assigned to the CTE. This name can be referenced in the main query as if it were a table.

__*column_name*__
Optional. Column names for the CTE result set. If not specified, column names are derived from the SELECT statement.

__*select_statement*__
The query that defines the CTE's result set.

## Return Value(s)

CTEs do not return values directly. They define temporary result sets that can be referenced in the main query.

* * *

## Examples

### Basic CTE for organizing complex queries

A CTE can simplify complex queries by breaking them into logical, named components.

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

### Combining data from multiple sources with `UNION ALL`

CTEs are useful for combining data from multiple sources before performing analysis.

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

### Using CTEs with window functions for ranking

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

### Multiple CTEs in a single query

You can define multiple CTEs separated by commas.

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

## Recursive CTEs

Recursive CTEs allow a CTE to reference itself, enabling queries that traverse hierarchical or graph-like data structures.

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

Recursive CTEs are powerful for hierarchical data like organizational charts, file systems, or dependency trees. Always include a termination condition in the recursive case to prevent infinite loops.

:::

## Best Practices

1. **Use CTEs for readability**: Break complex queries into logical, named components.
2. **Combine with window functions**: CTEs work well with window functions for advanced analytics.
3. **Chain multiple CTEs**: Reference earlier CTEs in later ones to build up complex logic step by step.
4. **Consider performance**: While CTEs improve readability, very large CTEs may impact performance. Test with your actual data volumes.

For more information, see [https://sqlite.org/lang_with.html](https://sqlite.org/lang_with.html).
