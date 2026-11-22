---
title: PERCENT_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Віконна функція PERCENT_RANK у StackQL: відносний ранг поточного рядка в його секції як значення від 0.0 до 1.0."
image: "/img/stackql-featured-image.png"
---
Повертає значення від 0.0 до 1.0, що виражає відносний ранг поточного рядка в його секції.

Попри назву, ця функція повертає значення, що дорівнює `(rank - 1) / (partition-rows - 1)`, де `rank` - це значення, яке повертає вбудована віконна функція `RANK()`, а `partition-rows` - загальна кількість рядків у секції. Якщо секція містить лише один рядок, ця функція повертає 0.0.

Див. також:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` CUME_DIST `]](/language-spec/functions/window/cume_dist)

* * *

## Синтаксис {#syntax}

```sql
SELECT PERCENT_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*PARTITION BY column*__
Необов'язковий. Розбиває набір результатів на секції. Функція `PERCENT_RANK` застосовується до кожної секції окремо.

__*ORDER BY column*__
Визначає порядок, у якому обчислюються процентильні ранги.

## Значення, що повертається {#return-values}

Повертає число з рухомою комою від 0.0 до 1.0, що виражає процентильний ранг.

* * *

## Приклади {#examples}

### Обчислення процентильних рангів учасників {#calculate-percentile-ranking-of-contributors}

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

Значення 0.0 функції `PERCENT_RANK` позначає рядок із найнижчим рангом, а 1.0 - рядок із найвищим рангом. Використовуйте цю функцію, щоб визначити, де в розподілі розташоване певне значення.

:::

Докладніше див. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
