---
title: JSON_GROUP_ARRAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 將值彙總為 JSON 陣列。
image: "/img/stackql-featured-image.png"
---
將值彙總為 JSON 陣列。`JSON_GROUP_ARRAY()` 會收集指定資料行中每個群組的所有值，並將其合併為 JSON 陣列。

另請參閱：  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## 語法 {#syntax}

```sql
SELECT grouping_column, JSON_GROUP_ARRAY(column_name) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## 引數 {#arguments}

__*column_name*__  
要將其值彙總為 JSON 陣列的資料行。

## 傳回值 {#return-values}
傳回包含指定資料行中所有彙總值的 JSON 陣列。

* * *

## 範例 {#examples}

### 將資源群組名稱彙總為 JSON 陣列 {#aggregate-resource-group-names-into-a-json-array}

```sql
-- Collects all resource group names into a JSON array
SELECT JSON_GROUP_ARRAY(name) as res_groups
FROM azure.resource.resource_groups
WHERE subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

此查詢會傳回資源群組名稱的 JSON 陣列，例如 `["stackqlenv1","stackqlenv2","stackqlenv3"]`。

### 依位置彙總資源群組 {#aggregate-resource-groups-by-location}

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

此查詢會依位置將資源群組整理為 JSON 陣列，並傳回多個陣列，每個位置一個，其中包含該位置的資源群組名稱。
