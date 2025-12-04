---
slug: window-functions-and-ctes-now-available
title: Window Functions and CTEs Now Available in StackQL
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-blog-post-featured-image.png"
description: Window functions and Common Table Expressions (CTEs) are now available in StackQL for advanced analytics across cloud and SaaS resources.
keywords: [stackql, analytics, window functions, cte, sql]
tags: [stackql, analytics, window functions, cte, sql]
---

Window functions and Common Table Expressions (CTEs) are now generally available in StackQL. These features work with both the embedded SQLite backend and PostgreSQL backend.

## Window Functions

Window functions allow you to perform calculations across sets of rows related to the current row. Supported functions include:

- **Ranking**: `ROW_NUMBER()`, `RANK()`, `DENSE_RANK()`, `NTILE()`
- **Offset**: `LAG()`, `LEAD()`, `FIRST_VALUE()`, `LAST_VALUE()`, `NTH_VALUE()`
- **Distribution**: `PERCENT_RANK()`, `CUME_DIST()`
- **Aggregates as window functions**: `SUM()`, `COUNT()`, `AVG()`, etc. with `OVER` clause

### Example: Ranking Contributors

```sql
SELECT
    login,
    contributions,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as rank
FROM github.repos.contributors
WHERE owner = 'stackql' AND repo = 'stackql';
```

### Example: Running Totals

```sql
SELECT
    login,
    contributions,
    SUM(contributions) OVER (ORDER BY contributions DESC) as running_total,
    ROUND(100.0 * contributions / SUM(contributions) OVER (), 2) as pct_of_total
FROM github.repos.contributors
WHERE owner = 'stackql' AND repo = 'stackql';
```

## Common Table Expressions (CTEs)

CTEs let you define temporary named result sets using the `WITH` clause. This simplifies complex queries by breaking them into logical components.

### Example: Aggregating Across Multiple Resources

```sql
WITH all_contributors AS (
    SELECT login, contributions
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

## Documentation

Full documentation is available:
- [Window Functions](/docs/language-spec/functions/window/row_number)
- [WITH (CTEs)](/docs/language-spec/with)
- [SELECT](/docs/language-spec/select)

Let us know your thoughts! Visit us and give us a star on [__GitHub__](https://github.com/stackql/stackql).
