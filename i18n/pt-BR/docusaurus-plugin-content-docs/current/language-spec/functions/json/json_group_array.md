---
title: JSON_GROUP_ARRAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
description: Agregar valores em um array JSON com SQL no StackQL.
image: "/img/stackql-featured-image.png"
---
Agrega valores como um array JSON. `JSON_GROUP_ARRAY()` coleta todos os valores de cada grupo em uma coluna especificada e os combina em um array JSON.

Veja também:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Sintaxe {#syntax}

```sql
SELECT JSON_GROUP_ARRAY(column_name) FROM <multipartIdentifier>
WHERE condition
GROUP BY column_name;
```

## Argumentos {#arguments}

__*column_name*__  
A coluna cujos valores são agregados em um array JSON.

## Valor(es) retornado(s) {#return-values}
Retorna um array JSON que contém todos os valores agregados da coluna especificada.

* * *

## Exemplos {#examples}

### Agregar nomes de grupos de recursos em um array JSON {#aggregate-resource-group-names-into-a-json-array}

```sql
-- Collects all resource group names into a JSON array
SELECT JSON_GROUP_ARRAY(name) as res_groups
FROM azure.resource.resource_groups
WHERE subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

Esta consulta retorna um array JSON de nomes de grupos de recursos, como `["stackqlenv1","stackqlenv2","stackqlenv3"]`.

### Agregar grupos de recursos por local {#aggregate-resource-groups-by-location}

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

Esta consulta organiza os grupos de recursos em arrays JSON com base no local, retornando vários arrays, um para cada local, com os nomes dos grupos de recursos daquele local.
