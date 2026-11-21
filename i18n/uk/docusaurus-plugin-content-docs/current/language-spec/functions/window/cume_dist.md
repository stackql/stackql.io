---
title: CUME_DIST
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Віконна функція CUME_DIST у StackQL: кумулятивний розподіл поточного рядка в його секції як номер рядка, поділений на кількість рядків секції."
image: "/img/stackql-featured-image.png"
---
Повертає кумулятивний розподіл значення в секції. Його обчислюють як `row-number / partition-rows`, де `row-number` - значення, яке `ROW_NUMBER()` повертає для останнього однорангового рядка в групі, а `partition-rows` - кількість рядків у секції.

Див. також:
[[` SELECT `]](/language-spec/select) [[` PERCENT_RANK `]](/language-spec/functions/window/percent_rank) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Синтаксис {#syntax}

```sql
SELECT CUME_DIST() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*PARTITION BY column*__
Необов'язковий. Поділяє набір результатів на секції. Функція `CUME_DIST` застосовується до кожної секції окремо.

__*ORDER BY column*__
Визначає порядок, у якому обчислюється кумулятивний розподіл.

## Значення, що повертається {#return-values}

Повертає число з рухомою комою від 0.0 до 1.0, що є значенням кумулятивного розподілу.

* * *

## Приклади {#examples}

### Обчислення кумулятивного розподілу учасників {#calculate-cumulative-distribution-of-contributors}

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

Функція `CUME_DIST` повертає частку рядків, значення яких менші за значення поточного рядка або дорівнюють йому. Значення 0.75 означає, що 75% рядків мають значення, менші за значення поточного рядка або рівні йому.

:::

Докладніше див. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
