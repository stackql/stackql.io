---
title: RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Оконная функция RANK в StackQL: ранг текущей строки в ее секции с пропусками, где строки с равными значениями получают один ранг, а следующие ранги пропускаются."
image: "/img/stackql-featured-image.png"
---
Возвращает ранг текущей строки с пропусками. Это значение `ROW_NUMBER` первой родственной строки в каждой группе. Строки с равными значениями в столбцах упорядочения получают одинаковый ранг, а следующее значение ранга увеличивается на количество строк с равными значениями (что оставляет пропуски).

Если предложение `ORDER BY` отсутствует, все строки считаются родственными и функция всегда возвращает 1.

См. также:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## Синтаксис {#syntax}

```sql
SELECT RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*PARTITION BY column*__
Необязательный. Разделяет результирующий набор на секции. Функция `RANK` применяется к каждой секции отдельно.

__*ORDER BY column*__
Задает порядок, в котором назначаются ранги. Если предложение `ORDER BY` отсутствует, все строки считаются родственными и получают ранг 1.

## Возвращаемое значение {#return-values}

Возвращает целое число, представляющее ранг текущей строки в секции, с пропусками для равных значений.

* * *

## Примеры {#examples}

### Ранжирование участников по количеству вкладов (с пропусками) {#rank-contributors-by-contributions-with-gaps}

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

### Сравнение функций `RANK`, `DENSE_RANK` и `ROW_NUMBER` {#compare-rank-with-dense_rank-and-row_number}

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

### Ранжирование участников в каждом репозитории с помощью предложения `PARTITION BY` {#rank-contributors-within-each-repository-using-partition-by}

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

Подробнее см. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
