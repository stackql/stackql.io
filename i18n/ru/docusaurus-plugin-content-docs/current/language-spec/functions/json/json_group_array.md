---
title: JSON_GROUP_ARRAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
description: Агрегирование значений в JSON-массив с помощью SQL в StackQL.
image: "/img/stackql-featured-image.png"
---
Агрегирует значения в JSON-массив. Функция `JSON_GROUP_ARRAY()` собирает все значения указанного столбца для каждой группы и объединяет их в JSON-массив.

См. также:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Синтаксис {#syntax}

```sql
SELECT grouping_column, JSON_GROUP_ARRAY(column_name) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## Аргументы {#arguments}

__*column_name*__  
Столбец, значения которого агрегируются в JSON-массив.

## Возвращаемое значение {#return-values}
Возвращает JSON-массив, содержащий все агрегированные значения из указанного столбца.

* * *

## Примеры {#examples}

### Агрегирование имен групп ресурсов в JSON-массив {#aggregate-resource-group-names-into-a-json-array}

```sql
-- Collects all resource group names into a JSON array
SELECT JSON_GROUP_ARRAY(name) as res_groups
FROM azure.resource.resource_groups
WHERE subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

Этот запрос возвращает JSON-массив имен групп ресурсов, например `["stackqlenv1","stackqlenv2","stackqlenv3"]`.

### Агрегирование групп ресурсов по расположению {#aggregate-resource-groups-by-location}

```sql
-- Collects resource group names into a JSON array grouped by location
SELECT
  location,
  JSON_GROUP_ARRAY(name) AS resource_groups
FROM
  azure.resource.resource_groups
WHERE
  subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468'
GROUP BY
  location;
```

Этот запрос распределяет группы ресурсов по JSON-массивам в зависимости от их расположения и возвращает несколько массивов, по одному на каждое расположение, с именами групп ресурсов в этом расположении.
