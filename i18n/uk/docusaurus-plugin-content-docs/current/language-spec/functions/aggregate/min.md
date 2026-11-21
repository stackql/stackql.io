---
title: MIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Агрегатна функція MIN у StackQL: найменше значення в стовпці або групуванні стовпців."
image: "/img/stackql-featured-image.png"
---
Повертає мінімальне значення на основі вхідного стовпця або групування стовпців.  

Див. також:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
/* aggregate function */
SELECT MIN(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MIN(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

### Агрегатна функція {#aggregate-function}

__*columnExpression*__  
Стовпець або вираз, зокрема з операторами (`+`, `-`, ...).

> Функція `MIN` повертає найменше значення для заданого стовпця залежно від типу даних стовпця: наприклад, операція `MIN` над стовпцем цілих чисел повертає найменше ціле число для цього стовпця або групування стовпців, тоді як операція `MIN` над рядковим стовпцем або групуванням стовпців повертає значення, яке під час сортування стоїть першим, порівнюючи рядкові значення за допомогою функції зіставлення `BINARY` (побайтово, тому літери верхнього регістру сортуються перед літерами нижнього регістру).

__*groupByColumn*__  
Стовпець або стовпці, за якими виконуються підсумкові чи агрегатні операції.  Речення `GROUP BY` повертає один рядок для кожного групування стовпців.

> Функція `MIN` ігнорує значення `NULL`.

> Функція `MIN` повертає `NULL`, якщо всі значення в групі дорівнюють `NULL`.

### Скалярна функція {#scalar-function}

__*scalarExpression*__  
Список стовпців або виразів (2 або більше), з яких визначається й повертається найменше значення. 

> Скалярна функція `MIN` повертає `NULL`, якщо будь-який аргумент дорівнює `NULL`. 

> Скалярна, або багатоаргументна, функція `MIN` шукає серед своїх аргументів зліва направо аргумент, що визначає функцію зіставлення, і використовує цю функцію зіставлення для всіх порівнянь рядкових значень. 

> Якщо жоден з аргументів скалярної функції `MIN` не визначає функцію зіставлення, використовується функція зіставлення `BINARY`. 

> Якщо передано лише один аргумент, викликається агрегатна функція `MIN`.

## Значення, що повертається {#return-values}

Повертає мінімальне значення залежно від типу вхідних даних (або групування).

* * *

## Приклади {#examples}

### Отримання мінімального значення стовпця {#return-the-minimum-value-for-a-column}

```sql
SELECT name, location, min(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### Отримання мінімального значення для кожного значення в стовпці {#return-the-minimum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
min(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### Отримання мінімального значення зі списку значень за допомогою скалярної функції `min` {#return-the-minimum-value-from-a-list-of-values-using-the-scalar-min-function}

```sql
SELECT min(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER), CAST(json_extract(disks, '$[1].diskSizeGb') AS INTEGER)) as smallest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Використання `MIN` як віконної функції для пошуку мінімальних значень у секціях {#use-min-as-a-window-function-to-find-minimum-values-within-partitions}

```sql
-- Find the earliest issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MIN(created_at) OVER (PARTITION BY state) as earliest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at;
```

Докладніше див. [https://www.sqlite.org/lang_aggfunc.html#min_agg](https://www.sqlite.org/lang_aggfunc.html#min_agg) або [https://www.sqlite.org/lang_corefunc.html#min_scalar](https://www.sqlite.org/lang_corefunc.html#min_scalar)
