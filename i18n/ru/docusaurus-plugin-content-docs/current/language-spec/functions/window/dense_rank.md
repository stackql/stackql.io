---
title: DENSE_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Оконная функция DENSE_RANK в StackQL: ранг текущей строки в секции без пропусков, так что строки с равными значениями получают общий ранг, а следующий идет сразу за ним."
image: "/img/stackql-featured-image.png"
---
Возвращает ранг текущей строки без пропусков. Это номер группы родственных строк, в которую входит текущая строка, в пределах ее секции. Строки с равными значениями в столбцах упорядочения получают одинаковый ранг, а следующее значение ранга всегда увеличивается на 1 (без пропусков).

Строки нумеруются, начиная с 1, в порядке, заданном предложением `ORDER BY` в определении окна. Если предложения `ORDER BY` нет, то все строки считаются родственными и эта функция всегда возвращает 1.

См. также:
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Синтаксис {#syntax}

```sql
SELECT DENSE_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*PARTITION BY column*__
Необязательный. Разделяет результирующий набор на секции. Функция `DENSE_RANK` применяется к каждой секции отдельно.

__*ORDER BY column*__
Задает порядок, в котором присваиваются ранги. Если предложения `ORDER BY` нет, то все строки считаются родственными и получают ранг 1.

## Возвращаемое значение {#return-values}

Возвращает целое число, представляющее плотный ранг текущей строки в секции, без пропусков для равных значений.

* * *

## Примеры {#examples}

### Ранжирование участников по числу вкладов (без пропусков) {#rank-contributors-by-contributions-without-gaps}

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

### Сравнение функции `DENSE_RANK` с функциями `RANK` и `ROW_NUMBER` {#compare-dense_rank-with-rank-and-row_number}

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

### Ранжирование суммарных вкладов в нескольких репозиториях {#rank-aggregated-contributions-across-multiple-repositories}

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

Подробнее см. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
