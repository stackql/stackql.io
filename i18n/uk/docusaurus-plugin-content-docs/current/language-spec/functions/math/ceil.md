---
title: CEIL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Функція CEIL у StackQL: округлення числового аргументу вгору до найближчого цілого числа."
image: "/img/stackql-featured-image.png"
---
Округлює числовий аргумент угору до найближчого цілого числа.  

Див. також:  
[[`SELECT`]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
SELECT CEIL(numeric_expression) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

__*numeric_expression*__  
Вираз, тип даних якого належить до категорії точних або наближених числових типів.

## Значення, що повертається {#return-values}
Повертає найменше ціле число, більше за вказаний числовий вираз або рівне йому.

* * *

## Приклади {#examples}

### Округлення числа з рухомою комою вгору до найближчого (більшого) цілого числа {#round-up-a-floating-point-number-to-the-closest-larger-integer}

```sql
SELECT name, timeCreated,
ceil(julianday('now')-julianday(timeCreated)) as days_since_ceiling
FROM google.storage.buckets WHERE project = 'stackql';
```
