---
title: MAX
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: "Агрегатна функція MAX у StackQL: найбільше значення в стовпці або групуванні стовпців."
image: "/img/stackql-featured-image.png"
---
Повертає максимальне значення на основі вхідного стовпця або групування стовпців.  

Див. також:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Синтаксис {#syntax}

```sql
/* aggregate function */
SELECT MAX(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MAX(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## Аргументи {#arguments}

### Агрегатна функція {#aggregate-function}

__*columnExpression*__  
Стовпець або вираз, зокрема з операторами (`+`, `-`, ...).

> Функція `MAX` повертає найбільше значення для заданого стовпця залежно від типу даних стовпця: наприклад, операція `MAX` над стовпцем цілих чисел повертає найбільше ціле число для цього стовпця або групування стовпців, тоді як операція `MAX` над рядковим стовпцем або групуванням стовпців повертає значення, яке під час сортування стоїть останнім, порівнюючи рядкові значення за допомогою функції зіставлення `BINARY` (побайтово, тому літери верхнього регістру сортуються перед літерами нижнього регістру).

__*groupByColumn*__  
Стовпець або стовпці, за якими виконуються підсумкові чи агрегатні операції.  Речення `GROUP BY` повертає один рядок для кожного групування стовпців.

> Функція `MAX` ігнорує значення `NULL`.

> Функція `MAX` повертає `NULL`, якщо всі значення в групі дорівнюють `NULL`.

### Скалярна функція {#scalar-function}

__*scalarExpression*__  
Список стовпців або виразів (2 або більше), з яких визначається й повертається найбільше значення. 

> Скалярна функція `MAX` повертає `NULL`, якщо будь-який аргумент дорівнює `NULL`. 

> Скалярна, або багатоаргументна, функція `MAX` шукає серед своїх аргументів зліва направо аргумент, що визначає функцію зіставлення, і використовує цю функцію зіставлення для всіх порівнянь рядкових значень. 

> Якщо жоден з аргументів скалярної функції `MAX` не визначає функцію зіставлення, використовується функція зіставлення `BINARY`. 

> Якщо передано лише один аргумент, викликається агрегатна функція `MAX`.

## Значення, що повертається {#return-values}

Повертає максимальне значення залежно від типу вхідних даних (або групування).

* * *

## Приклади {#examples}

### Отримання максимального значення стовпця {#return-the-maximum-value-for-a-column}

```sql
SELECT name, location, max(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### Отримання максимального значення для кожного значення в стовпці {#return-the-maximum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
max(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### Отримання максимального значення зі списку значень за допомогою скалярної функції `max` {#return-the-maximum-value-from-a-list-of-values-using-the-scalar-max-function}

```sql
SELECT max(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER), CAST(json_extract(disks, '$[1].diskSizeGb') AS INTEGER)) as largest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Використання `MAX` як віконної функції для пошуку максимальних значень у секціях {#use-max-as-a-window-function-to-find-maximum-values-within-partitions}

```sql
-- Find the most recent issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MAX(created_at) OVER (PARTITION BY state) as latest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at DESC;
```

Докладніше див. [https://www.sqlite.org/lang_aggfunc.html#max_agg](https://www.sqlite.org/lang_aggfunc.html#max_agg) або [https://www.sqlite.org/lang_corefunc.html#max_scalar](https://www.sqlite.org/lang_corefunc.html#max_scalar)
