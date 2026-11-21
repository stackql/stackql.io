---
title: COUNT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Агрегатна функція COUNT у StackQL: кількість рядків у ресурсі або відмінних від NULL значень стовпця в межах групування."
image: "/img/stackql-featured-image.png"
---
Повертає загальну кількість рядків у ресурсі або кількість відмінних від `NULL` значень стовпця в групуванні.  

Див. також:  
[[` SELECT `]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT COUNT( * | columnExpression ) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Аргументи {#arguments}

__*columnExpression*__  або __*__  
Стовпець або вираз.  Якщо вказано __*__, підраховуються всі рядки ресурсу або групування.  

> Значення `NULL` у *columnExpression* не враховуються.  Якщо всі значення дорівнюють `NULL`, `COUNT` повертає `0`.

__*groupByColumn*__  
Стовпець або стовпці, за якими виконуються підсумкові чи агрегатні операції.  Речення `GROUP BY` повертає один рядок для кожного групування стовпців.

## Значення, що повертається {#return-values}

Повертає ціле число, що відповідає кількості.

* * *

## Приклади {#examples}

### Отримання кількості рядків у всьому ресурсі {#return-the-number-of-rows-in-an-entire-resource}

```sql
SELECT count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Отримання кількості рядків, згрупованих за виразом стовпця {#return-the-number-of-rows-grouped-by-a-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

### Використання `COUNT` як віконної функції для обчислення накопиченої кількості {#use-count-as-a-window-function-to-calculate-cumulative-counts}

```sql
-- Track cumulative issue counts over time
SELECT
    number,
    title,
    state,
    created_at,
    COUNT(*) OVER (ORDER BY created_at) as cumulative_issues,
    COUNT(*) OVER (PARTITION BY state ORDER BY created_at) as cumulative_by_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

Докладніше див. [https://www.sqlite.org/lang_aggfunc.html#count](https://www.sqlite.org/lang_aggfunc.html#count).
