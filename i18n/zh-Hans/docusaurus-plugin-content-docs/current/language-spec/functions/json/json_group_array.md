---
title: JSON_GROUP_ARRAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 将 JSON 对象聚合为 JSON 数组。
image: "/img/stackql-featured-image.png"
---
将值聚合为 JSON 数组。`JSON_GROUP_ARRAY()` 收集指定列中每个分组的所有值，并将它们合并为一个 JSON 数组。

另请参阅：  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## 语法 {#syntax}

```sql
SELECT JSON_GROUP_ARRAY(column_name) FROM <multipartIdentifier>
WHERE condition
GROUP BY column_name;
```

## 参数 {#arguments}

__*column_name*__  
要将其值聚合为 JSON 数组的列。

## 返回值 {#return-values}
返回一个 JSON 数组，包含指定列中所有聚合的值。

* * *

## 示例 {#examples}

### 将资源组名称聚合为 JSON 数组 {#aggregate-resource-group-names-into-a-json-array}

```sql
-- Collects all resource group names into a JSON array
SELECT JSON_GROUP_ARRAY(name) as res_groups
FROM azure.resource.resource_groups
WHERE subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

此查询返回由资源组名称组成的 JSON 数组，例如 `["stackqlenv1","stackqlenv2","stackqlenv3"]`。

### 按位置聚合资源组 {#aggregate-resource-groups-by-location}

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

此查询按位置将资源组组织为 JSON 数组，返回多个数组，每个位置一个，其中包含该位置的资源组名称。
