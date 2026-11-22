---
title: LAST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Оконная функция LAST_VALUE в StackQL: значение выражения для последней строки рамки окна."
image: "/img/stackql-featured-image.png"
---
Возвращает значение выражения, вычисленного для последней строки в рамке окна.

Эта встроенная оконная функция вычисляет рамку окна для каждой строки так же, как агрегатная оконная функция. Для каждой строки она возвращает значение выражения, вычисленного для последней строки в рамке окна.

См. также:
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` NTH_VALUE `]](/language-spec/functions/window/nth_value)

* * *

## Синтаксис {#syntax}

```sql
SELECT LAST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*expr*__
Выражение, которое вычисляется для последней строки в рамке окна.

__*PARTITION BY column*__
Необязательный. Разделяет результирующий набор на секции. Функция `LAST_VALUE` применяется в пределах каждой секции отдельно.

__*ORDER BY column*__
Задает порядок, в котором обрабатываются строки.

__*ROWS BETWEEN frame_start AND frame_end*__
Необязательный. Определяет рамку окна. Распространенные варианты:
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

Если предложение рамки не указано, используется рамка `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`: она заканчивается на последней строке, значения которой в столбцах `ORDER BY` равны значениям текущей строки, а не на текущей строке.

## Возвращаемое значение {#return-values}

Возвращает значение выражения, вычисленного для последней строки в рамке окна.

* * *

## Примеры {#examples}

### Поиск последнего выпуска для контекста {#find-the-latest-release-for-context}

```sql
-- Find first and latest release for context
SELECT
    tag_name,
    name,
    published_at,
    FIRST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as first_release,
    LAST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as latest_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

По умолчанию рамка окна простирается от начала секции до последней строки, значения которой в столбцах `ORDER BY` равны значениям текущей строки, поэтому функция `LAST_VALUE` возвращает значение этой последней строки с равными значениями (самой текущей строки, если ее значение `ORDER BY` уникально), а не последнее значение в секции. Чтобы получить последнее значение в секции, используйте `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`.

:::

Подробнее см. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
