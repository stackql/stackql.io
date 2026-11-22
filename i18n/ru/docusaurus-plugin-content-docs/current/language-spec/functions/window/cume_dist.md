---
title: CUME_DIST
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Оконная функция CUME_DIST в StackQL: интегральное распределение текущей строки в ее секции как номер строки, деленный на число строк в секции."
image: "/img/stackql-featured-image.png"
---
Возвращает интегральное распределение значения в секции. Оно вычисляется как `row-number / partition-rows`, где `row-number` - значение, возвращаемое функцией `ROW_NUMBER()` для последней родственной строки в группе, а `partition-rows` - число строк в секции.

См. также:
[[` SELECT `]](/language-spec/select) [[` PERCENT_RANK `]](/language-spec/functions/window/percent_rank) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Синтаксис {#syntax}

```sql
SELECT CUME_DIST() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*PARTITION BY column*__
Необязательный. Разделяет результирующий набор на секции. Функция `CUME_DIST` применяется к каждой секции отдельно.

__*ORDER BY column*__
Задает порядок, в котором вычисляется интегральное распределение.

## Возвращаемое значение {#return-values}

Возвращает число с плавающей запятой от 0.0 до 1.0, представляющее значение интегрального распределения.

* * *

## Примеры {#examples}

### Вычисление интегрального распределения участников {#calculate-cumulative-distribution-of-contributors}

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

Функция `CUME_DIST` возвращает долю строк, значения которых меньше значения текущей строки или равны ему. Значение 0.75 означает, что у 75% строк значения меньше значения текущей строки или равны ему.

:::

Подробнее см. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
