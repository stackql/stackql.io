---
title: NTH_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Оконная функция NTH_VALUE в StackQL: значение выражения для строки с номером N в рамке окна."
image: "/img/stackql-featured-image.png"
---
Возвращает значение выражения, вычисленного для строки с номером N в рамке окна.

Эта встроенная оконная функция вычисляет рамку окна для каждой строки так же, как агрегатная оконная функция. Она возвращает значение выражения, вычисленного для строки с номером N в рамке окна. Строки в рамке окна нумеруются, начиная с 1, в порядке, заданном предложением `ORDER BY`, если оно есть, или в произвольном порядке в противном случае. Если в рамке окна нет строки с номером N, то возвращается `NULL`.

См. также:
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` LAST_VALUE `]](/language-spec/functions/window/last_value)

* * *

## Синтаксис {#syntax}

```sql
SELECT NTH_VALUE(expr, N) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*expr*__
Выражение, которое вычисляется для строки с номером N в рамке окна.

__*N*__
Положительное целое число, задающее номер строки, которую нужно получить (с отсчетом от 1).

__*PARTITION BY column*__
Необязательный. Разделяет результирующий набор на секции. Функция `NTH_VALUE` применяется в пределах каждой секции отдельно.

__*ORDER BY column*__
Задает порядок, в котором обрабатываются строки.

__*ROWS BETWEEN frame_start AND frame_end*__
Необязательный. Определяет рамку окна.

## Возвращаемое значение {#return-values}

Возвращает значение выражения, вычисленного для строки с номером N в рамке окна, или `NULL`, если строки с номером N не существует.

* * *

## Примеры {#examples}

### Получение трех ведущих участников {#get-the-top-three-contributors}

```sql
-- Get the top 3 contributors by number of contributions
SELECT
    login,
    contributions,
    NTH_VALUE(login, 1) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as top_contributor,
    NTH_VALUE(login, 2) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as second_contributor,
    NTH_VALUE(login, 3) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as third_contributor
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

Подробнее см. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
