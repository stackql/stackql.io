---
title: JSON_GROUP_ARRAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
description: Агрегування значень у масив JSON за допомогою SQL у StackQL.
image: "/img/stackql-featured-image.png"
---
Агрегує значення у вигляді масиву JSON. Функція `JSON_GROUP_ARRAY()` збирає всі значення кожної групи у вказаному стовпці й об'єднує їх у масив JSON.

Див. також:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Синтаксис {#syntax}

```sql
SELECT JSON_GROUP_ARRAY(column_name) FROM <multipartIdentifier>
WHERE condition
GROUP BY column_name;
```

## Аргументи {#arguments}

__*column_name*__  
Стовпець, значення якого агрегуються в масив JSON.

## Значення, що повертається {#return-values}
Повертає масив JSON з усіма агрегованими значеннями вказаного стовпця.

* * *

## Приклади {#examples}

### Агрегування імен груп ресурсів у масив JSON {#aggregate-resource-group-names-into-a-json-array}

```sql
-- Collects all resource group names into a JSON array
SELECT JSON_GROUP_ARRAY(name) as res_groups
FROM azure.resource.resource_groups
WHERE subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

Цей запит повертає масив JSON з іменами груп ресурсів, наприклад `["stackqlenv1","stackqlenv2","stackqlenv3"]`.

### Агрегування груп ресурсів за розташуванням {#aggregate-resource-groups-by-location}

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

Цей запит розподіляє групи ресурсів між масивами JSON відповідно до їхнього розташування й повертає кілька масивів, один для кожного розташування, з іменами груп ресурсів у цьому розташуванні.
