---
title: ROW_NUMBER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Віконна функція ROW_NUMBER у StackQL: нумерація рядків у секції від 1 у порядку, визначеному реченням ORDER BY вікна."
image: "/img/stackql-featured-image.png"
---
Повертає номер рядка в поточній секції. Рядки нумеруються, починаючи з 1, у порядку, визначеному реченням `ORDER BY` у визначенні вікна, а інакше - у довільному порядку.

Див. також:
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## Синтаксис {#syntax}

```sql
SELECT ROW_NUMBER() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*PARTITION BY column*__
Необов'язковий. Розбиває набір результатів на секції. Функція `ROW_NUMBER` застосовується до кожної секції окремо й скидає номер рядка для кожної секції.

__*ORDER BY column*__
Визначає порядок, у якому призначаються номери рядків. Обов'язковий для змістовних результатів.

## Значення, що повертається {#return-values}

Повертає ціле число, що виражає порядковий номер рядка в секції, починаючи з 1.

* * *

## Приклади {#examples}

### Ранжування учасників у репозиторії {#rank-contributors-within-a-repository}

```sql
-- Rank contributors within a repository
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as contribution_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### Відстеження послідовності issue в часі {#track-issue-sequence-over-time}

```sql
-- Track issue growth over time
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### Порівняння `ROW_NUMBER` з `RANK` і `DENSE_RANK` {#compare-row_number-with-rank-and-dense_rank}

```sql
-- Compare RANK vs DENSE_RANK vs ROW_NUMBER for contributors
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

Докладніше див. [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions).
