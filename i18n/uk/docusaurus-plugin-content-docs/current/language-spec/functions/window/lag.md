---
title: LAG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Віконна функція LAG у StackQL: обчислення виразу для попереднього рядка в секції з необов'язковими зсувом і значенням за замовчуванням."
image: "/img/stackql-featured-image.png"
---
Повертає результат обчислення виразу для попереднього рядка в секції.

Перша форма `LAG()` повертає результат обчислення виразу для попереднього рядка в секції. Якщо ж попереднього рядка немає (бо поточний рядок є першим), то повертається `NULL`.

Якщо вказано аргумент зсуву, він має бути невід'ємним цілим числом. Повернуте значення є результатом обчислення виразу для рядка, розташованого за *offset* рядків перед поточним рядком у секції. Якщо зсув дорівнює 0, то вираз обчислюється для поточного рядка. Якщо рядка за *offset* рядків перед поточним рядком немає, то повертається `NULL`.

Якщо також вказано значення за замовчуванням, воно повертається замість `NULL` у разі, коли рядка, визначеного зсувом, не існує.

Див. також:
[[` SELECT `]](/language-spec/select) [[` LEAD `]](/language-spec/functions/window/lead)

* * *

## Синтаксис {#syntax}

```sql
SELECT LAG(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*expr*__
Вираз, який обчислюється для попереднього рядка.

__*offset*__
Необов'язковий. Невід'ємне ціле число, що визначає, на скільки рядків назад потрібно перейти. За замовчуванням: 1.

__*default*__
Необов'язковий. Значення, яке повертається, якщо рядка зі зсувом не існує. За замовчуванням: `NULL`.

__*PARTITION BY column*__
Необов'язковий. Поділяє набір результатів на секції. Функція `LAG` застосовується в межах кожної секції окремо.

__*ORDER BY column*__
Визначає порядок, у якому обробляються рядки.

## Значення, що повертається {#return-values}

Повертає значення виразу, обчисленого для вказаного попереднього рядка, або `NULL` (чи значення за замовчуванням), якщо такого рядка не існує.

* * *

## Приклади {#examples}

### Порівняння кожного випуску з попереднім випуском {#compare-each-release-to-the-previous-release}

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

### Аналіз проміжків між створенням issue {#analyze-gaps-between-issue-creation}

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

Докладніше див. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
