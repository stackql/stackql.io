---
title: ROW_NUMBER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Оконная функция ROW_NUMBER в StackQL: нумерация строк в секции начиная с 1 в порядке, заданном предложением ORDER BY окна."
image: "/img/stackql-featured-image.png"
---
Возвращает номер строки в текущей секции. Строки нумеруются начиная с 1 в порядке, заданном предложением `ORDER BY` в определении окна, а в противном случае - в произвольном порядке.

См. также:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## Синтаксис {#syntax}

```sql
SELECT ROW_NUMBER() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*PARTITION BY column*__
Необязательный. Разделяет результирующий набор на секции. Функция `ROW_NUMBER` применяется к каждой секции отдельно и сбрасывает номер строки для каждой секции.

__*ORDER BY column*__
Задает порядок, в котором назначаются номера строк. Обязателен для получения осмысленных результатов.

## Возвращаемое значение {#return-values}

Возвращает целое число, представляющее порядковый номер строки в секции, начиная с 1.

* * *

## Примеры {#examples}

### Ранжирование участников в репозитории {#rank-contributors-within-a-repository}

```sql
-- Rank contributors within a repository
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as contribution_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Отслеживание последовательности issue во времени {#track-issue-sequence-over-time}

```sql
-- Track issue growth over time
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### Сравнение функций `ROW_NUMBER`, `RANK` и `DENSE_RANK` {#compare-row_number-with-rank-and-dense_rank}

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

Подробнее см. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
