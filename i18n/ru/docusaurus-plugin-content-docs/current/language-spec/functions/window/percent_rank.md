---
title: PERCENT_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Оконная функция PERCENT_RANK в StackQL: относительный ранг текущей строки в ее секции в виде значения от 0.0 до 1.0."
image: "/img/stackql-featured-image.png"
---
Возвращает значение от 0.0 до 1.0, представляющее относительный ранг текущей строки в ее секции.

Несмотря на название, эта функция возвращает значение, равное `(rank - 1) / (partition-rows - 1)`, где `rank` - значение, возвращаемое встроенной оконной функцией `RANK()`, а `partition-rows` - общее количество строк в секции. Если секция содержит только одну строку, функция возвращает 0.0.

См. также:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` CUME_DIST `]](/language-spec/functions/window/cume_dist)

* * *

## Синтаксис {#syntax}

```sql
SELECT PERCENT_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*PARTITION BY column*__
Необязательный. Разделяет результирующий набор на секции. Функция `PERCENT_RANK` применяется к каждой секции отдельно.

__*ORDER BY column*__
Задает порядок, в котором вычисляются процентильные ранги.

## Возвращаемое значение {#return-values}

Возвращает число с плавающей запятой от 0.0 до 1.0, представляющее процентильный ранг.

* * *

## Примеры {#examples}

### Вычисление процентильного ранга участников {#calculate-percentile-ranking-of-contributors}

```sql
-- Calculate percentile ranking of contributors
SELECT
    login,
    contributions,
    PERCENT_RANK() OVER (ORDER BY contributions) as percentile,
    CUME_DIST() OVER (ORDER BY contributions) as cumulative_dist
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY contributions DESC;
```

:::tip

Значение `PERCENT_RANK`, равное 0.0, обозначает строку с самым низким рангом, а 1.0 - строку с самым высоким рангом. Используйте эту функцию, чтобы определить, где находится конкретное значение в распределении.

:::

Подробнее см. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
