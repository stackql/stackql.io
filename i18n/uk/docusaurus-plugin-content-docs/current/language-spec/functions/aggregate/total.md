---
title: TOTAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Агрегатна функція TOTAL у StackQL: сума відмінних від NULL значень стовпця або групування, яку завжди повертають як число з рухомою комою."
image: "/img/stackql-featured-image.png"
---
Повертає суму всіх відмінних від `NULL` значень у стовпці або групуванні стовпців.  

Див. також:  
[[` SELECT `]](/language-spec/select) [[` SUM `]](/language-spec/functions/aggregate/sum)

* * * 

## Синтаксис {#syntax}

```sql
SELECT TOTAL(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Аргументи {#arguments}

__*columnExpression*__  
Стовпець або вираз.

> Якщо всі значення *columnExpression* дорівнюють `NULL`, `TOTAL` повертає `0.0`.  

__*groupByColumn*__  
Стовпець або стовпці, за якими виконуються підсумкові чи агрегатні операції.  Речення `GROUP BY` повертає один рядок для кожного групування стовпців.

## Значення, що повертається {#return-values}

Повертає число з рухомою комою, що відповідає сумі *columnExpression*.

* * *

## Приклади {#examples}

### Отримання суми виразу стовпця для всього ресурсу {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Отримання суми виразу стовпця, згрупованого за іншим виразом стовпця {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT status, total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY status;
```

Докладніше див. [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
