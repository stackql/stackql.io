---
title: MAX
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Агрегатная функция MAX в StackQL: наибольшее значение в столбце или группировке столбцов."
image: "/img/stackql-featured-image.png"
---
Возвращает максимальное значение на основе входного столбца или группировки столбцов.  

См. также:  
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

## Аргументы {#arguments}

### Агрегатная функция {#aggregate-function}

__*columnExpression*__  
Столбец или выражение, в том числе с операторами (`+`, `-`, ...).

> Функция `MAX` возвращает наибольшее значение заданного столбца с учетом типа данных столбца: например, операция `MAX` над целочисленным столбцом возвращает наибольшее целое число этого столбца или группировки столбцов, а операция `MAX` над строковым столбцом или группировкой столбцов возвращает значение, которое при сортировке оказывается последним; строки при этом сравниваются с помощью функции сравнения `BINARY` (побайтно, поэтому буквы верхнего регистра идут перед буквами нижнего регистра).

__*groupByColumn*__  
Столбец или столбцы, по которым выполняются итоговые или агрегатные операции.  Предложение `GROUP BY` возвращает одну строку для каждой группировки столбцов.

> Функция `MAX` игнорирует значения `NULL`.

> Функция `MAX` возвращает `NULL`, если все значения в группе равны `NULL`.

### Скалярная функция {#scalar-function}

__*scalarExpression*__  
Список столбцов или выражений (2 или более), среди которых определяется и возвращается наибольшее значение. 

> Скалярная функция `MAX` возвращает `NULL`, если хотя бы один аргумент равен `NULL`. 

> Скалярная функция `MAX` (с несколькими аргументами) просматривает аргументы слева направо в поиске аргумента, который определяет функцию сравнения, и использует эту функцию сравнения для всех сравнений строк. 

> Если ни один из аргументов скалярной функции `MAX` не определяет функцию сравнения, используется функция сравнения `BINARY`. 

> Если передан только один аргумент, вызывается агрегатная функция `MAX`.

## Возвращаемое значение {#return-values}

Возвращает максимальное значение с учетом типа входных данных (или группировки).

* * *

## Примеры {#examples}

### Получение максимального значения столбца {#return-the-maximum-value-for-a-column}

```sql
SELECT name, location, max(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### Получение максимального значения для каждого значения в столбце {#return-the-maximum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
max(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### Получение максимального значения из списка значений с помощью скалярной функции `max` {#return-the-maximum-value-from-a-list-of-values-using-the-scalar-max-function}

```sql
SELECT max(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER), CAST(json_extract(disks, '$[1].diskSizeGb') AS INTEGER)) as largest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Использование `MAX` как оконной функции для поиска максимальных значений в секциях {#use-max-as-a-window-function-to-find-maximum-values-within-partitions}

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

Подробнее см. [https://www.sqlite.org/lang_aggfunc.html#max_agg](https://www.sqlite.org/lang_aggfunc.html#max_agg) или [https://www.sqlite.org/lang_corefunc.html#max_scalar](https://www.sqlite.org/lang_corefunc.html#max_scalar)
