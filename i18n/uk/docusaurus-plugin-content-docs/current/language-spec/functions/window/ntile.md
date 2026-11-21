---
title: NTILE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Віконна функція NTILE у StackQL: поділ секції на N груп якомога рівномірніше з нумерацією групи кожного рядка від 1 до N."
image: "/img/stackql-featured-image.png"
---
Поділяє секцію на N груп якомога рівномірніше і призначає кожній групі ціле число від 1 до N у порядку, визначеному реченням `ORDER BY`, або в довільному порядку в іншому разі. За потреби більші групи йдуть першими.

Див. також:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## Синтаксис {#syntax}

```sql
SELECT NTILE(N) OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*N*__
Додатне ціле число, що визначає кількість груп (tiles), на які поділяється секція.

__*PARTITION BY column*__
Необов'язковий. Поділяє набір результатів на секції. Функція `NTILE` застосовується до кожної секції окремо.

__*ORDER BY column*__
Визначає порядок, у якому рядки розподіляються між групами.

## Значення, що повертається {#return-values}

Повертає ціле число від 1 до N, яке вказує, до якої групи (tile) належить поточний рядок.

* * *

## Приклади {#examples}

### Групування учасників у квартилі за рівнем внеску {#group-contributors-into-quartiles-by-contribution-level}

```sql
-- Group contributors into quartiles by contribution level
SELECT
    login,
    contributions,
    NTILE(4) OVER (ORDER BY contributions DESC) as contribution_quartile,
    CASE NTILE(4) OVER (ORDER BY contributions DESC)
        WHEN 1 THEN 'Top Contributors'
        WHEN 2 THEN 'Active Contributors'
        WHEN 3 THEN 'Moderate Contributors'
        WHEN 4 THEN 'Occasional Contributors'
    END as contributor_tier
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

Використовуйте функцію `NTILE` для створення сегментів або рівнів у ваших даних. Поширені сценарії використання: поділ даних на квартилі (N=4), децилі (N=10) або процентилі (N=100).

:::

Докладніше див. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
