---
title: Оконные функции
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Оконные функции в StackQL: ранги, нарастающие итоги, смещения и распределения по связанным строкам с PARTITION BY, ORDER BY и предложениями рамки."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Оконные функции выполняют вычисления по набору строк, связанных с текущей строкой. В отличие от агрегатных функций, которые возвращают один результат для группы строк, оконные функции возвращают значение для каждой строки, учитывая "окно" связанных строк.

См. также:
[[` SELECT `]](/language-spec/select) [[` WITH (CTE) `]](/language-spec/with) [[ Справочник по оконным функциям ]](/language-spec/functions/window/row_number)

* * *

## Синтаксис {#syntax}

Оконные функции используют предложение `OVER`, чтобы определить спецификацию окна:

```sql
SELECT <windowFunction> OVER ( <windowSpec> ) FROM <multipartIdentifier>;
```

*windowFunctionCall::=*

<RailroadDiagram 
type="windowFunctionCall"
/>

### Оконная функция {#window-function}

Оконные функции - это специализированные функции, которые выполняют вычисления по набору строк, определенному спецификацией окна. Это могут быть как собственно оконные функции (например, `ROW_NUMBER` или `LAG`), так и агрегатные функции (например, `SUM` или `COUNT`), используемые с предложением `OVER`.

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

### Спецификация окна {#window-specification}

Спецификация окна определяет, как строки разбиваются на секции, упорядочиваются и ограничиваются рамкой для вычисления оконной функции. `PARTITION BY` разделяет строки на группы, `ORDER BY` задает порядок внутри каждой секции, а необязательное предложение рамки указывает, какие строки относительно текущей строки включаются в вычисление.

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

## Доступные оконные функции {#available-window-functions}

| Категория | Функции |
|----------|-----------|
| **Ранжирование** | [__`ROW_NUMBER()`__](/language-spec/functions/window/row_number), [__`RANK()`__](/language-spec/functions/window/rank), [__`DENSE_RANK()`__](/language-spec/functions/window/dense_rank), [__`NTILE()`__](/language-spec/functions/window/ntile) |
| **Смещение** | [__`LAG()`__](/language-spec/functions/window/lag), [__`LEAD()`__](/language-spec/functions/window/lead), [__`FIRST_VALUE()`__](/language-spec/functions/window/first_value), [__`LAST_VALUE()`__](/language-spec/functions/window/last_value), [__`NTH_VALUE()`__](/language-spec/functions/window/nth_value) |
| **Распределение** | [__`PERCENT_RANK()`__](/language-spec/functions/window/percent_rank), [__`CUME_DIST()`__](/language-spec/functions/window/cume_dist) |
| **Агрегатные** | [__`SUM()`__](/language-spec/functions/aggregate/sum), [__`COUNT()`__](/language-spec/functions/aggregate/count), [__`AVG()`__](/language-spec/functions/aggregate/avg), [__`MIN()`__](/language-spec/functions/aggregate/min), [__`MAX()`__](/language-spec/functions/aggregate/max) с предложением __`OVER`__ |

* * *

## Примеры {#examples}

### Ранжирование с помощью `ROW_NUMBER`, `RANK` и `DENSE_RANK` {#ranking-with-row_number-rank-and-dense_rank}

Используйте функции ранжирования, чтобы назначать строкам ранги на основе значений столбцов.

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

### Нарастающие итоги и проценты {#running-totals-and-percentages}

Агрегатные функции, такие как `SUM` и `COUNT`, можно использовать как оконные функции для вычисления нарастающих итогов и процентов.

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

### Сравнение строк с помощью `LAG` и `LEAD` {#comparing-rows-with-lag-and-lead}

Используйте `LAG` и `LEAD`, чтобы получать значения из предыдущих или последующих строк.

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

### Создание групп с помощью `NTILE` {#creating-groups-with-ntile}

Используйте `NTILE`, чтобы разделить строки на заданное число групп.

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

### Использование `PARTITION BY` для анализа по группам {#using-partition-by-for-grouped-analysis}

Используйте `PARTITION BY`, чтобы выполнять оконные вычисления внутри групп.

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

### Использование рамок окна {#using-window-frames}

Укажите предложение рамки, чтобы управлять тем, какие строки включаются в оконное вычисление.

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

Подробную документацию по каждой оконной функции см. на странице [Справочник по оконным функциям](/language-spec/functions/window/row_number).

Подробнее см. [https://sqlite.org/windowfunctions.html](https://sqlite.org/windowfunctions.html).
