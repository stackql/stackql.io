---
title: Віконні функції
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Віконні функції в StackQL: ранги, наростальні підсумки, зсуви й розподіли за пов'язаними рядками, з PARTITION BY, ORDER BY і реченнями рамки."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Віконні функції виконують обчислення над набором рядків, пов'язаних із поточним рядком. На відміну від агрегатних функцій, які повертають один результат для групи рядків, віконні функції повертають значення для кожного рядка, враховуючи "вікно" пов'язаних рядків.

Див. також:
[[` SELECT `]](/language-spec/select) [[` WITH (CTE) `]](/language-spec/with) [[ Довідник із віконних функцій ]](/language-spec/functions/window/row_number)

* * *

## Синтаксис {#syntax}

Віконні функції використовують речення `OVER` для визначення специфікації вікна:

```sql
SELECT <windowFunction> OVER ( <windowSpec> ) FROM <multipartIdentifier>;
```

*windowFunctionCall::=*

<RailroadDiagram 
type="windowFunctionCall"
/>

### Віконна функція {#window-function}

Віконні функції - це спеціалізовані функції, що виконують обчислення над набором рядків, визначеним специфікацією вікна. Це можуть бути як власне віконні функції (наприклад, `ROW_NUMBER` або `LAG`), так і агрегатні функції (наприклад, `SUM` або `COUNT`), використані з реченням `OVER`.

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

### Специфікація вікна {#window-specification}

Специфікація вікна визначає, як рядки поділяються на секції, упорядковуються й обмежуються рамкою для обчислення віконної функції. `PARTITION BY` поділяє рядки на групи, `ORDER BY` визначає послідовність у кожній секції, а необов'язкове речення рамки вказує, які рядки відносно поточного рядка включаються в обчислення.

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

## Доступні віконні функції {#available-window-functions}

| Категорія | Функції |
|----------|-----------|
| **Ранжування** | [__`ROW_NUMBER()`__](/language-spec/functions/window/row_number), [__`RANK()`__](/language-spec/functions/window/rank), [__`DENSE_RANK()`__](/language-spec/functions/window/dense_rank), [__`NTILE()`__](/language-spec/functions/window/ntile) |
| **Зсув** | [__`LAG()`__](/language-spec/functions/window/lag), [__`LEAD()`__](/language-spec/functions/window/lead), [__`FIRST_VALUE()`__](/language-spec/functions/window/first_value), [__`LAST_VALUE()`__](/language-spec/functions/window/last_value), [__`NTH_VALUE()`__](/language-spec/functions/window/nth_value) |
| **Розподіл** | [__`PERCENT_RANK()`__](/language-spec/functions/window/percent_rank), [__`CUME_DIST()`__](/language-spec/functions/window/cume_dist) |
| **Агрегатні** | [__`SUM()`__](/language-spec/functions/aggregate/sum), [__`COUNT()`__](/language-spec/functions/aggregate/count), [__`AVG()`__](/language-spec/functions/aggregate/avg), [__`MIN()`__](/language-spec/functions/aggregate/min), [__`MAX()`__](/language-spec/functions/aggregate/max) з реченням __`OVER`__ |

* * *

## Приклади {#examples}

### Ранжування за допомогою `ROW_NUMBER`, `RANK` і `DENSE_RANK` {#ranking-with-row_number-rank-and-dense_rank}

Використовуйте функції ранжування, щоб призначати рядкам ранги на основі значень стовпців.

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

### Наростальні підсумки та відсотки {#running-totals-and-percentages}

Агрегатні функції, як-от `SUM` і `COUNT`, можна використовувати як віконні функції для обчислення наростальних підсумків і відсотків.

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

### Порівняння рядків за допомогою `LAG` і `LEAD` {#comparing-rows-with-lag-and-lead}

Використовуйте функції `LAG` і `LEAD`, щоб отримувати значення з попередніх або наступних рядків.

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

### Створення груп за допомогою `NTILE` {#creating-groups-with-ntile}

Використовуйте функцію `NTILE`, щоб поділити рядки на вказану кількість груп.

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

### Використання `PARTITION BY` для аналізу за групами {#using-partition-by-for-grouped-analysis}

Використовуйте речення `PARTITION BY`, щоб виконувати віконні обчислення в межах груп.

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

### Використання рамок вікна {#using-window-frames}

Вкажіть речення рамки, щоб керувати тим, які рядки включаються у віконне обчислення.

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

Докладну документацію з кожної віконної функції див. на сторінці [Довідник із віконних функцій](/language-spec/functions/window/row_number).

Докладніше див. [https://sqlite.org/windowfunctions.html](https://sqlite.org/windowfunctions.html).
