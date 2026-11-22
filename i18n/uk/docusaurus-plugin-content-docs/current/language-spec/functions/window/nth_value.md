---
title: NTH_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Віконна функція NTH_VALUE у StackQL: значення виразу для рядка з номером N у рамці вікна."
image: "/img/stackql-featured-image.png"
---
Повертає значення виразу, обчисленого для рядка з номером N у рамці вікна.

Ця вбудована віконна функція обчислює рамку вікна для кожного рядка так само, як агрегатна віконна функція. Вона повертає значення виразу, обчисленого для рядка N рамки вікна. Рядки в рамці вікна нумеруються, починаючи з 1, у порядку, визначеному реченням `ORDER BY`, якщо воно є, або в довільному порядку в іншому разі. Якщо рядка з номером N у рамці вікна немає, то повертається `NULL`.

Див. також:
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

## Аргументи {#arguments}

__*expr*__
Вираз, який обчислюється для рядка з номером N у рамці вікна.

__*N*__
Додатне ціле число, що визначає номер рядка, який потрібно отримати (нумерація з 1).

__*PARTITION BY column*__
Необов'язковий. Поділяє набір результатів на секції. Функція `NTH_VALUE` застосовується в межах кожної секції окремо.

__*ORDER BY column*__
Визначає порядок, у якому обробляються рядки.

__*ROWS BETWEEN frame_start AND frame_end*__
Необов'язковий. Визначає рамку вікна.

## Значення, що повертається {#return-values}

Повертає значення виразу, обчисленого для рядка з номером N у рамці вікна, або `NULL`, якщо рядка з номером N не існує.

* * *

## Приклади {#examples}

### Отримання трьох найактивніших учасників {#get-the-top-three-contributors}

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

Докладніше див. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
