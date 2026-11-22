---
title: COUNT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Агрегатная функция COUNT в StackQL: количество строк в ресурсе или отличных от NULL значений столбца в группировке."
image: "/img/stackql-featured-image.png"
---
Возвращает общее количество строк в ресурсе или количество отличных от `NULL` значений столбца в группировке.  

См. также:  
[[` SELECT `]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT COUNT( * | columnExpression ) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Аргументы {#arguments}

__*columnExpression*__  или __*__  
Столбец или выражение.  Если указано __*__, подсчитываются все строки в ресурсе или группировке.  

> Значения `NULL` в *columnExpression* не учитываются.  Если все значения равны `NULL`, `COUNT` возвращает `0`.

__*groupByColumn*__  
Столбец или столбцы, по которым выполняются итоговые или агрегатные операции.  Предложение `GROUP BY` возвращает одну строку для каждой группировки столбцов.

## Возвращаемое значение {#return-values}

Возвращает целое число, равное результату подсчета.

* * *

## Примеры {#examples}

### Получение количества строк во всем ресурсе {#return-the-number-of-rows-in-an-entire-resource}

```sql
SELECT count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Получение количества строк, сгруппированных по выражению столбца {#return-the-number-of-rows-grouped-by-a-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

### Использование `COUNT` как оконной функции для подсчета нарастающим итогом {#use-count-as-a-window-function-to-calculate-cumulative-counts}

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

Подробнее см. [https://www.sqlite.org/lang_aggfunc.html#count](https://www.sqlite.org/lang_aggfunc.html#count).
