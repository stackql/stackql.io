---
title: AVG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Агрегатная функция AVG в StackQL: среднее арифметическое отличных от NULL значений в столбце или группировке."
image: "/img/stackql-featured-image.png"
---
Возвращает среднее арифметическое всех отличных от `NULL` значений в столбце или группировке столбцов.  

См. также:  
[[` SELECT `]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT AVG(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Аргументы {#arguments}

__*columnExpression*__  
Столбец или выражение.

> Строковые значения и значения BLOB, которые нельзя представить как числовые значения, интерпретируются как `0`.  

> Хотя бы одно значение *columnExpression* должно быть отличным от `NULL`, иначе возвращается `NULL`.

__*groupByColumn*__  
Столбец или столбцы, по которым выполняются итоговые или агрегатные операции.  Предложение `GROUP BY` возвращает одну строку для каждой группировки столбцов.

## Возвращаемое значение {#return-values}

Возвращает число с плавающей запятой, равное среднему значению *columnExpression*.

* * *

## Примеры {#examples}

### Получение среднего значения выражения столбца по всему ресурсу {#return-the-average-of-a-column-expression-over-an-entire-resource}

```sql
SELECT avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Получение среднего значения выражения столбца, сгруппированного по другому выражению столбца {#return-the-average-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

### Использование `AVG` как оконной функции для вычисления скользящих средних {#use-avg-as-a-window-function-to-calculate-moving-averages}

```sql
-- Calculate moving average of commit activity
WITH weekly_totals AS (
    SELECT
        week,
        SUM(json_each.value) as commits_this_week
    FROM github.repos.stats_commit_activity, JSON_EACH(days)
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    GROUP BY week
)
SELECT
    week,
    commits_this_week,
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg
FROM weekly_totals
ORDER BY week;
```

Подробнее см. [https://www.sqlite.org/lang_aggfunc.html#avg](https://www.sqlite.org/lang_aggfunc.html#avg).
