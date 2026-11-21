---
title: JSON_GROUP_ARRAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: StackQL에서 SQL로 값을 JSON 배열로 집계합니다.
image: "/img/stackql-featured-image.png"
---
값을 JSON 배열로 집계합니다. `JSON_GROUP_ARRAY()` 함수는 지정한 열에서 각 그룹의 모든 값을 수집하여 하나의 JSON 배열로 결합합니다.

참고 항목:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## 구문 {#syntax}

```sql
SELECT grouping_column, JSON_GROUP_ARRAY(column_name) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## 인수 {#arguments}

__*column_name*__  
값을 JSON 배열로 집계할 열입니다.

## 반환 값 {#return-values}
지정한 열에서 집계된 모든 값을 포함하는 JSON 배열을 반환합니다.

* * *

## 예제 {#examples}

### 리소스 그룹 이름을 JSON 배열로 집계 {#aggregate-resource-group-names-into-a-json-array}

```sql
-- Collects all resource group names into a JSON array
SELECT JSON_GROUP_ARRAY(name) as res_groups
FROM azure.resource.resource_groups
WHERE subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

이 쿼리는 `["stackqlenv1","stackqlenv2","stackqlenv3"]`와 같은 리소스 그룹 이름의 JSON 배열을 반환합니다.

### 위치별 리소스 그룹 집계 {#aggregate-resource-groups-by-location}

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

이 쿼리는 위치에 따라 리소스 그룹을 JSON 배열로 구성합니다. 위치마다 하나씩 여러 배열을 반환하며, 각 배열에는 해당 위치에 있는 리소스 그룹의 이름이 포함됩니다.
