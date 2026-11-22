---
title: MIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Агрегатная функция MIN в StackQL: наименьшее значение в столбце или группировке столбцов."
image: "/img/stackql-featured-image.png"
---
Возвращает минимальное значение на основе входного столбца или группировки столбцов.  

См. также:  
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

## Аргументы {#arguments}

### Агрегатная функция {#aggregate-function}

__*columnExpression*__  
Столбец или выражение, в том числе с операторами (`+`, `-`, ...).

> Функция `MIN` возвращает наименьшее значение заданного столбца с учетом типа данных столбца: например, операция `MIN` над целочисленным столбцом возвращает наименьшее целое число этого столбца или группировки столбцов, а операция `MIN` над строковым столбцом или группировкой столбцов возвращает значение, которое при сортировке оказывается первым; строки при этом сравниваются с помощью функции сравнения `BINARY` (побайтно, поэтому буквы верхнего регистра идут перед буквами нижнего регистра).

__*groupByColumn*__  
Столбец или столбцы, по которым выполняются итоговые или агрегатные операции.  Предложение `GROUP BY` возвращает одну строку для каждой группировки столбцов.

> Функция `MIN` игнорирует значения `NULL`.

> Функция `MIN` возвращает `NULL`, если все значения в группе равны `NULL`.

### Скалярная функция {#scalar-function}

__*scalarExpression*__  
Список столбцов или выражений (2 или более), среди которых определяется и возвращается наименьшее значение. 

> Скалярная функция `MIN` возвращает `NULL`, если хотя бы один аргумент равен `NULL`. 

> Скалярная функция `MIN` (с несколькими аргументами) просматривает аргументы слева направо в поиске аргумента, который определяет функцию сравнения, и использует эту функцию сравнения для всех сравнений строк. 

> Если ни один из аргументов скалярной функции `MIN` не определяет функцию сравнения, используется функция сравнения `BINARY`. 

> Если передан только один аргумент, вызывается агрегатная функция `MIN`.

## Возвращаемое значение {#return-values}

Возвращает минимальное значение с учетом типа входных данных (или группировки).

* * *

## Примеры {#examples}

### Получение минимального значения столбца {#return-the-minimum-value-for-a-column}

```sql
SELECT name, location, min(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### Получение минимального значения для каждого значения в столбце {#return-the-minimum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
min(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### Получение минимального значения из списка значений с помощью скалярной функции `min` {#return-the-minimum-value-from-a-list-of-values-using-the-scalar-min-function}

```sql
SELECT min(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER), CAST(json_extract(disks, '$[1].diskSizeGb') AS INTEGER)) as smallest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Использование `MIN` как оконной функции для поиска минимальных значений в секциях {#use-min-as-a-window-function-to-find-minimum-values-within-partitions}

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

Подробнее см. [https://www.sqlite.org/lang_aggfunc.html#min_agg](https://www.sqlite.org/lang_aggfunc.html#min_agg) или [https://www.sqlite.org/lang_corefunc.html#min_scalar](https://www.sqlite.org/lang_corefunc.html#min_scalar)
