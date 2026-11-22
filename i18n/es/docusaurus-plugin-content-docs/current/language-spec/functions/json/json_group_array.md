---
title: JSON_GROUP_ARRAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
description: Agregar valores en un array JSON con SQL en StackQL.
image: "/img/stackql-featured-image.png"
---
Agrega valores en forma de array JSON. `JSON_GROUP_ARRAY()` recopila todos los valores de cada grupo de una columna especificada y los combina en un array JSON.

Consulte también:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Sintaxis {#syntax}

```sql
SELECT grouping_column, JSON_GROUP_ARRAY(column_name) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## Argumentos {#arguments}

__*column_name*__  
La columna cuyos valores se agregan en un array JSON.

## Valor(es) devuelto(s) {#return-values}
Devuelve un array JSON que contiene todos los valores agregados de la columna especificada.

* * *

## Ejemplos {#examples}

### Agregar los nombres de grupos de recursos en un array JSON {#aggregate-resource-group-names-into-a-json-array}

```sql
-- Collects all resource group names into a JSON array
SELECT JSON_GROUP_ARRAY(name) as res_groups
FROM azure.resource.resource_groups
WHERE subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

Esta consulta devuelve un array JSON con los nombres de los grupos de recursos, como `["stackqlenv1","stackqlenv2","stackqlenv3"]`.

### Agregar grupos de recursos por ubicación {#aggregate-resource-groups-by-location}

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

Esta consulta organiza los grupos de recursos en arrays JSON según su ubicación y devuelve varios arrays, uno por cada ubicación, con los nombres de los grupos de recursos de esa ubicación.
