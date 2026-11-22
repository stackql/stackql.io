---
title: LAG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Оконная функция LAG в StackQL: вычисление выражения для предыдущей строки в секции с необязательными смещением и значением по умолчанию."
image: "/img/stackql-featured-image.png"
---
Возвращает результат вычисления выражения для предыдущей строки в секции.

Первая форма функции `LAG()` возвращает результат вычисления выражения для предыдущей строки в секции. Если же предыдущей строки нет (так как текущая строка первая), то возвращается `NULL`.

Если указан аргумент смещения, он должен быть неотрицательным целым числом. Возвращаемое значение - результат вычисления выражения для строки, которая находится на *offset* строк раньше текущей строки в секции. Если смещение равно 0, то выражение вычисляется для текущей строки. Если строки, которая находится на *offset* строк раньше текущей, нет, то возвращается `NULL`.

Если указано также значение по умолчанию, то оно возвращается вместо `NULL`, когда строки, определяемой смещением, не существует.

См. также:
[[` SELECT `]](/language-spec/select) [[` LEAD `]](/language-spec/functions/window/lead)

* * *

## Синтаксис {#syntax}

```sql
SELECT LAG(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## Аргументы {#arguments}

__*expr*__
Выражение, которое вычисляется для предыдущей строки.

__*offset*__
Необязательный. Неотрицательное целое число, указывающее, на сколько строк назад нужно сместиться. По умолчанию: 1.

__*default*__
Необязательный. Значение, которое возвращается, если строки по указанному смещению не существует. По умолчанию: `NULL`.

__*PARTITION BY column*__
Необязательный. Разделяет результирующий набор на секции. Функция `LAG` применяется в пределах каждой секции отдельно.

__*ORDER BY column*__
Задает порядок, в котором обрабатываются строки.

## Возвращаемое значение {#return-values}

Возвращает значение выражения, вычисленного для указанной предыдущей строки, или `NULL` (либо значение по умолчанию), если такой строки нет.

* * *

## Примеры {#examples}

### Сравнение каждого выпуска с предыдущим выпуском {#compare-each-release-to-the-previous-release}

```sql
-- Compare each release to the previous release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LAG(published_at, 1) OVER (ORDER BY published_at) as previous_release_date,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### Анализ промежутков между созданием issue {#analyze-gaps-between-issue-creation}

```sql
-- Analyze gaps between issue creation
SELECT
    number,
    title,
    created_at,
    LAG(created_at, 1) OVER (ORDER BY created_at) as prev_issue_date,
    julianday(created_at) - julianday(LAG(created_at, 1) OVER (ORDER BY created_at)) as days_since_prev_issue
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

Подробнее см. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
