---
title: JSON_GROUP_ARRAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Aggregate JSON objects into a JSON array using SQL in StackQL.
image: "/img/stackql-featured-image.png"
---
Aggregates values as a JSON array. `JSON_GROUP_ARRAY()` collects all values for each group in a specified column and combines them into a JSON array.

See also:  
[[`SELECT`]](/docs/language-spec/select) [[` JSON_EXTRACT `]](/docs/language-spec/functions/json/json_extract)

* * * 

## Syntax

```sql
SELECT JSON_GROUP_ARRAY(column_name) FROM <multipartIdentifier>
WHERE condition
GROUP BY column_name;
```

## Arguments

__*column_name*__  
The column from which values are aggregated into a JSON array.

## Return Value(s)
Returns a JSON array containing all the aggregated values from the specified column.

* * *

## Examples

### Aggregate Resource Group Names into a JSON Array

```sql
-- Collects all resource group names into a JSON array
SELECT JSON_GROUP_ARRAY(name) as res_groups
FROM azure.resources.resource_groups
WHERE subscriptionId = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

This query returns a JSON array of resource group names such as `["stackqlenv1","stackqlenv2","stackqlenv3"]`.

### Aggregate Resource Groups by Location

```sql
-- Collects resource group names into a JSON array grouped by location
SELECT
  location,
  JSON_GROUP_ARRAY(name) AS resource_groups
FROM
  azure.resources.resource_groups
WHERE
  subscriptionId = '631d1c6d-2a65-43e7-93c2-688bfe4e1468'
GROUP BY
  location;
```

This query organizes resource groups into JSON arrays based on their location, returning multiple arrays, one for each location, containing the names of resource groups in that location.
