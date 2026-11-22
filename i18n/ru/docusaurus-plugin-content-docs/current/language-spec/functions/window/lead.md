---
title: LEAD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Оконная функция LEAD в StackQL: вычисление выражения для последующей строки в секции с необязательными смещением и значением по умолчанию."
image: "/img/stackql-featured-image.png"
---
Возвращает результат вычисления выражения для последующей строки в секции.

Первая форма функции `LEAD()` возвращает результат вычисления выражения для следующей строки в секции. Если же следующей строки нет (так как текущая строка последняя), то возвращается `NULL`.

Если указан аргумент смещения, он должен быть неотрицательным целым числом. Возвращаемое значение - результат вычисления выражения для строки, которая находится на *offset* строк позже текущей строки в секции. Если смещение равно 0, то выражение вычисляется для текущей строки. Если строки, которая находится на *offset* строк позже текущей, нет, то возвращается `NULL`.

Если указано также значение по умолчанию, то оно возвращается вместо `NULL`, когда строки, определяемой смещением, не существует.

См. также:
[[` SELECT `]](/language-spec/select) [[` LAG `]](/language-spec/functions/window/lag)

* * *

## Синтаксис {#syntax}

```sql
SELECT LEAD(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*expr*__
Выражение, которое вычисляется для последующей строки.

__*offset*__
Необязательный. Неотрицательное целое число, указывающее, на сколько строк вперед нужно сместиться. По умолчанию: 1.

__*default*__
Необязательный. Значение, которое возвращается, если строки по указанному смещению не существует. По умолчанию: `NULL`.

__*PARTITION BY column*__
Необязательный. Разделяет результирующий набор на секции. Функция `LEAD` применяется в пределах каждой секции отдельно.

__*ORDER BY column*__
Задает порядок, в котором обрабатываются строки.

## Возвращаемое значение {#return-values}

Возвращает значение выражения, вычисленного для указанной последующей строки, или `NULL` (либо значение по умолчанию), если такой строки нет.

* * *

## Примеры {#examples}

### Просмотр следующего выпуска {#look-ahead-to-the-next-release}

```sql
-- Compare each release to the previous and next release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LEAD(tag_name, 1) OVER (ORDER BY published_at) as next_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

Подробнее см. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
