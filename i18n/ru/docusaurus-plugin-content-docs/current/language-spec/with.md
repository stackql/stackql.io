---
title: WITH (CTE)
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "WITH в StackQL: определение обобщенных табличных выражений (CTE) - временных именованных результирующих наборов, действующих в следующей за ними инструкции SELECT."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Определяет обобщенное табличное выражение (CTE) для использования в следующей за ним инструкции `SELECT`. CTE работают как временные именованные результирующие наборы, которые существуют только во время выполнения запроса.

См. также:
[[` SELECT `]](/language-spec/select)

* * *

## Синтаксис {#syntax}

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

## Аргументы {#arguments}

__*RECURSIVE*__  
Необязательное ключевое слово, которое позволяет CTE ссылаться на само себя, что дает возможность выполнять рекурсивные запросы, например обход иерархических данных.

__*cte_name*__  
Имя, назначенное CTE. На это имя можно ссылаться в основном запросе так, как если бы это была таблица.

__*column_name*__  
Необязательный. Имена столбцов результирующего набора CTE. Если они не указаны, имена столбцов берутся из инструкции SELECT.

__*select_statement*__  
Запрос, определяющий результирующий набор CTE.

## Возвращаемое значение {#return-values}

CTE не возвращают значения напрямую. Они определяют временные результирующие наборы, на которые можно ссылаться в основном запросе.

* * *

## Примеры {#examples}

### Простое CTE для организации сложных запросов {#basic-cte-for-organizing-complex-queries}

CTE может упростить сложные запросы, разбивая их на логические именованные компоненты.

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

### Объединение данных из нескольких источников с помощью `UNION ALL` {#combining-data-from-multiple-sources-with-union-all}

CTE удобны для объединения данных из нескольких источников перед анализом.

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

### Использование CTE с оконными функциями для ранжирования {#using-ctes-with-window-functions-for-ranking}

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

### Несколько CTE в одном запросе {#multiple-ctes-in-a-single-query}

Можно определить несколько CTE через запятую.

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

## Рекурсивные CTE {#recursive-ctes}

Рекурсивные CTE позволяют CTE ссылаться на само себя, что дает возможность выполнять запросы, которые обходят иерархические или графоподобные структуры данных.

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

Рекурсивные CTE - мощное средство для работы с иерархическими данными, такими как организационные структуры, файловые системы или деревья зависимостей. Всегда включайте в рекурсивную часть условие завершения, чтобы избежать бесконечных циклов.

:::

## Рекомендации {#best-practices}

1. **Используйте CTE для удобочитаемости**: разбивайте сложные запросы на логические именованные компоненты.
2. **Сочетайте с оконными функциями**: CTE хорошо работают с оконными функциями при расширенной аналитике.
3. **Выстраивайте цепочки из нескольких CTE**: ссылайтесь на предыдущие CTE в последующих, чтобы поэтапно строить сложную логику.
4. **Учитывайте производительность**: хотя CTE улучшают удобочитаемость, очень большие CTE могут снижать производительность. Проверяйте на ваших реальных объемах данных.

Подробнее см. [https://sqlite.org/lang_with.html](https://sqlite.org/lang_with.html).
