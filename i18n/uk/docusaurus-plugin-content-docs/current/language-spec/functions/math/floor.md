---
title: FLOOR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція FLOOR у StackQL: округлення числового аргументу вниз до найближчого цілого числа."
image: "/img/stackql-featured-image.png"
---
Округлює числовий аргумент униз до найближчого цілого числа.  

Див. також:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT FLOOR(numeric_expression) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*numeric_expression*__  
Вираз, тип даних якого належить до категорії точних або наближених числових типів.

## Значення, що повертається {#return-values}
Повертає найбільше ціле число, менше за вказаний числовий вираз або рівне йому.

* * *

## Приклади {#examples}

### Округлення числа з рухомою комою вниз до найближчого (меншого) цілого числа {#round-down-a-floating-point-number-to-the-closest-smaller-integer}

```sql
SELECT name, timeCreated,
floor(julianday('now')-julianday(timeCreated)) as days_since_floor
FROM google.storage.buckets WHERE project = 'stackql';
```
