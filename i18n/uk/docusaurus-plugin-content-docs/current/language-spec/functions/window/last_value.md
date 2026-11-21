---
title: LAST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Віконна функція LAST_VALUE у StackQL: значення виразу для останнього рядка рамки вікна."
image: "/img/stackql-featured-image.png"
---
Повертає значення виразу, обчисленого для останнього рядка в рамці вікна.

Ця вбудована віконна функція обчислює рамку вікна для кожного рядка так само, як агрегатна віконна функція. Для кожного рядка вона повертає значення виразу, обчисленого для останнього рядка в рамці вікна.

Див. також:
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

## Аргументи {#arguments}

__*expr*__
Вираз, який обчислюється для останнього рядка в рамці вікна.

__*PARTITION BY column*__
Необов'язковий. Поділяє набір результатів на секції. Функція `LAST_VALUE` застосовується в межах кожної секції окремо.

__*ORDER BY column*__
Визначає порядок, у якому обробляються рядки.

__*ROWS BETWEEN frame_start AND frame_end*__
Необов'язковий. Визначає рамку вікна. Поширені варіанти:
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

Якщо речення рамки немає, то рамкою є `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`: вона закінчується на останньому рядку, що має з поточним рядком рівні значення в стовпцях `ORDER BY`, а не на поточному рядку.

## Значення, що повертається {#return-values}

Повертає значення виразу, обчисленого для останнього рядка в рамці вікна.

* * *

## Приклади {#examples}

### Пошук останнього випуску для контексту {#find-the-latest-release-for-context}

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

За замовчуванням рамка вікна простягається від початку секції до останнього рядка, що має з поточним рядком рівні значення в стовпцях `ORDER BY`, тому функція `LAST_VALUE` повертає значення цього останнього рядка з рівними значеннями (самого поточного рядка, якщо його значення `ORDER BY` унікальне), а не останнє значення в секції. Щоб отримати останнє значення в секції, використовуйте `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`.

:::

Докладніше див. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
