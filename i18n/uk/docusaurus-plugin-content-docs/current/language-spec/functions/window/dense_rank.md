---
title: DENSE_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Віконна функція DENSE_RANK у StackQL: ранг поточного рядка в секції без пропусків; рядки з рівними значеннями мають спільний ранг, а наступний ранг іде одразу за ним."
image: "/img/stackql-featured-image.png"
---
Повертає ранг поточного рядка без пропусків. Це номер групи однорангових рядків, до якої належить поточний рядок, у його секції. Рядки з рівними значеннями в стовпцях упорядкування отримують однаковий ранг, а наступне значення рангу завжди збільшується на 1 (без пропусків).

Рядки нумеруються, починаючи з 1, у порядку, визначеному реченням `ORDER BY` у визначенні вікна. Якщо речення `ORDER BY` немає, то всі рядки вважаються одноранговими, і ця функція завжди повертає 1.

Див. також:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Синтаксис {#syntax}

```sql
SELECT DENSE_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*PARTITION BY column*__
Необов'язковий. Поділяє набір результатів на секції. Функція `DENSE_RANK` застосовується до кожної секції окремо.

__*ORDER BY column*__
Визначає порядок, у якому призначаються ранги. Якщо речення `ORDER BY` немає, то всі рядки вважаються одноранговими й отримують ранг 1.

## Значення, що повертається {#return-values}

Повертає ціле число, що є щільним рангом поточного рядка в секції, без пропусків для рівних значень.

* * *

## Приклади {#examples}

### Ранжування учасників за кількістю внесків (без пропусків) {#rank-contributors-by-contributions-without-gaps}

```sql
-- Rank contributors without gaps for ties
SELECT
    DENSE_RANK() OVER (ORDER BY contributions DESC) as rank,
    login,
    contributions,
    html_url
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo IN ('stackql', 'stackql-deploy', 'stackql-provider-registry')
ORDER BY contributions DESC;
```

### Порівняння функції `DENSE_RANK` з функціями `RANK` і `ROW_NUMBER` {#compare-dense_rank-with-rank-and-row_number}

```sql
-- Compare RANK vs DENSE_RANK vs ROW_NUMBER for contributors
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Ранжування сумарних внесків у кількох репозиторіях {#rank-aggregated-contributions-across-multiple-repositories}

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

Докладніше див. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
