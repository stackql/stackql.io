---
title: LEAD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Віконна функція LEAD у StackQL: обчислення виразу для подальшого рядка в секції з необов'язковими зсувом і значенням за замовчуванням."
image: "/img/stackql-featured-image.png"
---
Повертає результат обчислення виразу для подальшого рядка в секції.

Перша форма `LEAD()` повертає результат обчислення виразу для наступного рядка в секції. Якщо ж наступного рядка немає (бо поточний рядок є останнім), то повертається `NULL`.

Якщо вказано аргумент зсуву, він має бути невід'ємним цілим числом. Повернуте значення є результатом обчислення виразу для рядка, розташованого за *offset* рядків після поточного рядка в секції. Якщо зсув дорівнює 0, то вираз обчислюється для поточного рядка. Якщо рядка за *offset* рядків після поточного рядка немає, то повертається `NULL`.

Якщо також вказано значення за замовчуванням, воно повертається замість `NULL` у разі, коли рядка, визначеного зсувом, не існує.

Див. також:
[[` SELECT `]](/language-spec/select) [[` LAG `]](/language-spec/functions/window/lag)

* * *

## Синтаксис {#syntax}

```sql
SELECT LEAD(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*expr*__
Вираз, який обчислюється для подальшого рядка.

__*offset*__
Необов'язковий. Невід'ємне ціле число, що визначає, на скільки рядків уперед потрібно перейти. За замовчуванням: 1.

__*default*__
Необов'язковий. Значення, яке повертається, якщо рядка зі зсувом не існує. За замовчуванням: `NULL`.

__*PARTITION BY column*__
Необов'язковий. Поділяє набір результатів на секції. Функція `LEAD` застосовується в межах кожної секції окремо.

__*ORDER BY column*__
Визначає порядок, у якому обробляються рядки.

## Значення, що повертається {#return-values}

Повертає значення виразу, обчисленого для вказаного подальшого рядка, або `NULL` (чи значення за замовчуванням), якщо такого рядка не існує.

* * *

## Приклади {#examples}

### Перегляд наступного випуску {#look-ahead-to-the-next-release}

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

Докладніше див. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
