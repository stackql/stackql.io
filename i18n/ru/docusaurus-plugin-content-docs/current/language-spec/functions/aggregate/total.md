---
title: TOTAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Агрегатная функция TOTAL в StackQL: сумма отличных от NULL значений в столбце или группировке, всегда возвращаемая как число с плавающей запятой."
image: "/img/stackql-featured-image.png"
---
Возвращает сумму всех отличных от `NULL` значений в столбце или группировке столбцов.  

См. также:  
[[` SELECT `]](/language-spec/select) [[` SUM `]](/language-spec/functions/aggregate/sum)

* * * 

## Синтаксис {#syntax}

```sql
SELECT TOTAL(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Аргументы {#arguments}

__*columnExpression*__  
Столбец или выражение.

> Если все значения *columnExpression* равны `NULL`, `TOTAL` возвращает `0.0`.  

__*groupByColumn*__  
Столбец или столбцы, по которым выполняются итоговые или агрегатные операции.  Предложение `GROUP BY` возвращает одну строку для каждой группировки столбцов.

## Возвращаемое значение {#return-values}

Возвращает число с плавающей запятой, равное сумме *columnExpression*.

* * *

## Примеры {#examples}

### Получение суммы выражения столбца по всему ресурсу {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Получение суммы выражения столбца, сгруппированного по другому выражению столбца {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT status, total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY status;
```

Подробнее см. [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
