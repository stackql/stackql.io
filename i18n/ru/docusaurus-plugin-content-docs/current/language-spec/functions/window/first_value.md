---
title: FIRST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Оконная функция FIRST_VALUE в StackQL: значение выражения для первой строки рамки окна."
image: "/img/stackql-featured-image.png"
---
Возвращает значение выражения, вычисленного для первой строки в рамке окна.

Эта встроенная оконная функция вычисляет рамку окна для каждой строки так же, как агрегатная оконная функция. Для каждой строки она возвращает значение выражения, вычисленного для первой строки в рамке окна.

См. также:
[[` SELECT `]](/language-spec/select) [[` LAST_VALUE `]](/language-spec/functions/window/last_value) [[` NTH_VALUE `]](/language-spec/functions/window/nth_value)

* * *

## Синтаксис {#syntax}

```sql
SELECT FIRST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*expr*__
Выражение, которое вычисляется для первой строки в рамке окна.

__*PARTITION BY column*__
Необязательный. Разделяет результирующий набор на секции. Функция `FIRST_VALUE` применяется в пределах каждой секции отдельно.

__*ORDER BY column*__
Задает порядок, в котором обрабатываются строки.

__*ROWS BETWEEN frame_start AND frame_end*__
Необязательный. Определяет рамку окна. Распространенные варианты:
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

Если предложение рамки не указано, используется рамка `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`: она заканчивается на последней строке, значения которой в столбцах `ORDER BY` равны значениям текущей строки, а не на текущей строке.

## Возвращаемое значение {#return-values}

Возвращает значение выражения, вычисленного для первой строки в рамке окна.

* * *

## Примеры {#examples}

### Поиск первого выпуска для контекста {#find-the-first-release-for-context}

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

По умолчанию рамка окна простирается от начала секции до последней строки, значения которой в столбцах `ORDER BY` равны значениям текущей строки. Функция `FIRST_VALUE` читает первую строку рамки, поэтому возвращает одно и то же значение как с рамкой по умолчанию, так и с рамкой `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`; явная рамка важна для функций `LAST_VALUE` и `NTH_VALUE`, которые читают строки дальше в рамке.

:::

Подробнее см. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
