---
title: JSON_GROUP_ARRAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って JSON オブジェクトを JSON 配列に集約します。
image: "/img/stackql-featured-image.png"
---
値を JSON 配列として集約します。`JSON_GROUP_ARRAY()` は、指定した列のすべての値をグループごとに収集し、JSON 配列にまとめます。

関連項目：  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## 構文 {#syntax}

```sql
SELECT JSON_GROUP_ARRAY(column_name) FROM <multipartIdentifier>
WHERE condition
GROUP BY column_name;
```

## 引数 {#arguments}

__*column_name*__  
値を JSON 配列に集約する元の列。

## 戻り値 {#return-values}
指定した列から集約したすべての値を含む JSON 配列を返します。

* * *

## 例 {#examples}

### リソースグループ名を JSON 配列に集約する {#aggregate-resource-group-names-into-a-json-array}

```sql
-- Collects all resource group names into a JSON array
SELECT JSON_GROUP_ARRAY(name) as res_groups
FROM azure.resource.resource_groups
WHERE subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

このクエリは、`["stackqlenv1","stackqlenv2","stackqlenv3"]` のようなリソースグループ名の JSON 配列を返します。

### リソースグループを場所ごとに集約する {#aggregate-resource-groups-by-location}

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

このクエリは、リソースグループを場所に基づいて JSON 配列にまとめます。場所ごとに 1 つずつ、その場所にあるリソースグループの名前を含む複数の配列を返します。
