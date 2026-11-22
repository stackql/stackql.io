---
title: RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Віконна функція RANK у StackQL: ранг поточного рядка в його секції з пропусками, тож рядки з рівними значеннями мають спільний ранг, а наступні ранги пропускаються."
image: "/img/stackql-featured-image.png"
---
Повертає ранг поточного рядка з пропусками. Це значення `ROW_NUMBER` першого однорангового рядка в кожній групі. Рядки з рівними значеннями в стовпцях упорядкування отримують однаковий ранг, а наступне значення рангу збільшується на кількість рядків із рівними значеннями (тому виникають пропуски).

Якщо речення `ORDER BY` немає, усі рядки вважаються одноранговими, і ця функція завжди повертає 1.

Див. також:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## Синтаксис {#syntax}

```sql
SELECT RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*PARTITION BY column*__
Необов'язковий. Розбиває набір результатів на секції. Функція `RANK` застосовується до кожної секції окремо.

__*ORDER BY column*__
Визначає порядок, у якому призначаються ранги. Якщо речення `ORDER BY` немає, усі рядки вважаються одноранговими й отримують ранг 1.

## Значення, що повертається {#return-values}

Повертає ціле число, що виражає ранг поточного рядка в секції, з пропусками для рівних значень.

* * *

## Приклади {#examples}

### Ранжування учасників за кількістю внесків (з пропусками) {#rank-contributors-by-contributions-with-gaps}

```sql
-- Rank contributors with gaps for ties
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Порівняння `RANK` з `DENSE_RANK` і `ROW_NUMBER` {#compare-rank-with-dense_rank-and-row_number}

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

### Ранжування учасників у кожному репозиторії за допомогою `PARTITION BY` {#rank-contributors-within-each-repository-using-partition-by}

```sql
-- Rank contributors within each repo
WITH repo_contributors AS (
    SELECT
        repo,
        login,
        contributions,
        RANK() OVER (PARTITION BY repo ORDER BY contributions DESC) as rank_in_repo
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

Докладніше див. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
