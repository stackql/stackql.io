---
title: SUM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Агрегатная функция SUM в StackQL: сумма отличных от NULL значений в столбце или группировке; если все входные значения целые, возвращается целое число."
image: "/img/stackql-featured-image.png"
---
Возвращает сумму всех отличных от `NULL` значений в столбце или группировке столбцов.  

См. также:  
[[` SELECT `]](/language-spec/select) [[` TOTAL `]](/language-spec/functions/aggregate/total)

* * * 

:::tip

Для суммирования чисел с плавающей запятой или больших целых чисел используйте функцию [**TOTAL**](/language-spec/functions/aggregate/total).

:::

## Синтаксис {#syntax}

```sql
SELECT SUM(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Аргументы {#arguments}

__*columnExpression*__  
Столбец или выражение.

> Если все значения *columnExpression* равны `NULL`, `SUM` возвращает `NULL`.  

> `SUM` выдает исключение "integer overflow", если на каком-либо этапе вычисления происходит целочисленное переполнение.  Функция [`TOTAL`](/language-spec/functions/aggregate/total) никогда не выдает ошибку целочисленного переполнения.  

__*groupByColumn*__  
Столбец или столбцы, по которым выполняются итоговые или агрегатные операции.  Предложение `GROUP BY` возвращает одну строку для каждой группировки столбцов.

## Возвращаемое значение {#return-values}

Возвращает целое число, равное сумме *columnExpression*, если все отличные от `NULL` входные значения - целые числа. Если хотя бы одно отличное от `NULL` входное значение не является целым числом, возвращается число с плавающей запятой, которое может быть приближением к истинной сумме.

* * *

## Примеры {#examples}

### Получение суммы выражения столбца по всему ресурсу {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Получение суммы выражения столбца, сгруппированного по другому выражению столбца {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT status, sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY status;
```

### Использование `SUM` как оконной функции для вычисления нарастающих итогов {#use-sum-as-a-window-function-to-calculate-running-totals}

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

Подробнее см. [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
