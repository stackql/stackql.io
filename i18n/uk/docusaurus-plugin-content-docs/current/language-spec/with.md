---
title: WITH (CTE)
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "WITH у StackQL: визначення загальних табличних виразів (CTE) - тимчасових іменованих наборів результатів, що діють у межах наступної за ними інструкції SELECT."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Визначає загальний табличний вираз (CTE) для використання в інструкції `SELECT`, що йде за ним. CTE діють як тимчасові іменовані набори результатів, які існують лише протягом виконання запиту.

Див. також:
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

## Аргументи {#arguments}

__*RECURSIVE*__  
Необов'язкове ключове слово, що дає змогу CTE посилатися на самого себе й виконувати рекурсивні запити, як-от обхід ієрархічних даних.

__*cte_name*__  
Ім'я, присвоєне CTE. На це ім'я можна посилатися в основному запиті так, ніби це таблиця.

__*column_name*__  
Необов'язковий. Імена стовпців для набору результатів CTE. Якщо їх не вказано, імена стовпців визначаються з інструкції SELECT.

__*select_statement*__  
Запит, що визначає набір результатів CTE.

## Значення, що повертається {#return-values}

CTE не повертають значень безпосередньо. Вони визначають тимчасові набори результатів, на які можна посилатися в основному запиті.

* * *

## Приклади {#examples}

### Простий CTE для впорядкування складних запитів {#basic-cte-for-organizing-complex-queries}

CTE може спростити складні запити, розбиваючи їх на логічні іменовані компоненти.

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

### Об'єднання даних із кількох джерел за допомогою `UNION ALL` {#combining-data-from-multiple-sources-with-union-all}

CTE корисні для об'єднання даних із кількох джерел перед виконанням аналізу.

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

### Використання CTE з віконними функціями для ранжування {#using-ctes-with-window-functions-for-ranking}

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

### Кілька CTE в одному запиті {#multiple-ctes-in-a-single-query}

Ви можете визначити кілька CTE, розділених комами.

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

## Рекурсивні CTE {#recursive-ctes}

Рекурсивні CTE дають змогу CTE посилатися на самого себе, що уможливлює запити, які обходять ієрархічні або графоподібні структури даних.

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

Рекурсивні CTE - потужний засіб для ієрархічних даних, як-от організаційні структури, файлові системи чи дерева залежностей. Завжди додавайте умову завершення в рекурсивну частину, щоб запобігти нескінченним циклам.

:::

## Найкращі практики {#best-practices}

1. **Використовуйте CTE для читабельності**: розбивайте складні запити на логічні іменовані компоненти.
2. **Поєднуйте з віконними функціями**: CTE добре працюють із віконними функціями для розширеної аналітики.
3. **Об'єднуйте кілька CTE в ланцюжок**: посилайтеся на попередні CTE в наступних, щоб крок за кроком будувати складну логіку.
4. **Зважайте на продуктивність**: хоча CTE покращують читабельність, дуже великі CTE можуть впливати на продуктивність. Тестуйте на реальних обсягах ваших даних.

Докладніше див. [https://sqlite.org/lang_with.html](https://sqlite.org/lang_with.html).
