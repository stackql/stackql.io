---
title: GROUP_CONCAT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: "Агрегатная функция GROUP_CONCAT в StackQL: конкатенация отличных от NULL значений столбца или группировки в одну строку с необязательным разделителем."
image: "/img/stackql-featured-image.png"
---
Возвращает строку, которая представляет собой конкатенацию всех отличных от `NULL` значений входного столбца или группировки.  

> Порядок конкатенируемых элементов произвольный.

См. также:  
[[` SELECT `]](/language-spec/select)

* * * 

## Синтаксис {#syntax}

```sql
SELECT GROUP_CONCAT(columnExpression [, separator]) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Аргументы {#arguments}

__*columnExpression*__  
Столбец или выражение.  

> Хотя бы одно значение *columnExpression* должно быть отличным от `NULL`, иначе возвращается `NULL`.

__*separator*__  
Разделитель для результата конкатенации; если *separator* не указан, используется значение по умолчанию `,`.

__*groupByColumn*__  
Столбец или столбцы, по которым выполняются итоговые или агрегатные операции.  Предложение `GROUP BY` возвращает одну строку для каждой группировки столбцов.

## Возвращаемое значение {#return-values}

Возвращает строку в виде списка значений, разделенных значением *separator* или `,`.

* * *

## Примеры {#examples}

### Получение списка значений столбца по всему ресурсу {#return-a-list-of-values-for-a-column-over-an-entire-resource}

```sql
SELECT group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Получение списка значений выражения столбца, сгруппированных по другому выражению столбца {#return-a-list-of-values-for-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

Подробнее см. [https://www.sqlite.org/lang_aggfunc.html#group_concat](https://www.sqlite.org/lang_aggfunc.html#group_concat).
