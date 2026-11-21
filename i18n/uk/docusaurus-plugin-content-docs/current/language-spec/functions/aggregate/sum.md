---
title: SUM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Агрегатна функція SUM у StackQL: сума відмінних від NULL значень стовпця або групування, яка є цілим числом, якщо всі вхідні значення цілі."
image: "/img/stackql-featured-image.png"
---
Повертає суму всіх відмінних від `NULL` значень у стовпці або групуванні стовпців.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[` TOTAL `]](/language-spec/functions/aggregate/total)

* * * 

:::tip

Щоб підсумувати числа з рухомою комою або довгі значення, використовуйте функцію [**TOTAL**](/language-spec/functions/aggregate/total).

:::

## Синтаксис {#syntax}

```sql
SELECT SUM(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Аргументи {#arguments}

__*columnExpression*__  
Стовпець або вираз.

> Якщо всі значення *columnExpression* дорівнюють `NULL`, `SUM` повертає `NULL`.  

> `SUM` видає виняток "integer overflow", якщо на будь-якому етапі обчислення стається переповнення цілого числа.  Функція [`TOTAL`](/language-spec/functions/aggregate/total) ніколи не видає помилку переповнення цілого числа.  

__*groupByColumn*__  
Стовпець або стовпці, за якими виконуються підсумкові чи агрегатні операції.  Речення `GROUP BY` повертає один рядок для кожного групування стовпців.

## Значення, що повертається {#return-values}

Повертає ціле число, що є сумою *columnExpression*, якщо всі відмінні від `NULL` вхідні значення є цілими числами. Якщо будь-яке відмінне від `NULL` вхідне значення не є цілим числом, повертається число з рухомою комою, яке може бути наближенням до справжньої суми.

* * *

## Приклади {#examples}

### Отримання суми виразу стовпця для всього ресурсу {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Отримання суми виразу стовпця, згрупованого за іншим виразом стовпця {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT status, sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY status;
```

### Використання `SUM` як віконної функції для обчислення наростальних підсумків {#use-sum-as-a-window-function-to-calculate-running-totals}

```sql
-- Calculate running total and percentage of contributions
SELECT
    login,
    contributions,
    SUM(contributions) OVER (ORDER BY contributions DESC) as running_total,
    SUM(contributions) OVER () as total_contributions,
    ROUND(100.0 * contributions / SUM(contributions) OVER (), 2) as pct_of_total
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

Докладніше див. [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
