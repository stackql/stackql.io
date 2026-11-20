---
title: JSON_GROUP_ARRAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Tổng hợp các giá trị thành một mảng JSON bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Tổng hợp các giá trị thành một mảng JSON. Hàm `JSON_GROUP_ARRAY()` thu thập mọi giá trị của từng nhóm trong một cột được chỉ định và kết hợp chúng thành một mảng JSON.

Xem thêm:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Cú pháp {#syntax}

```sql
SELECT JSON_GROUP_ARRAY(column_name) FROM <multipartIdentifier>
WHERE condition
GROUP BY column_name;
```

## Đối số {#arguments}

__*column_name*__  
Cột chứa các giá trị được tổng hợp thành mảng JSON.

## Giá trị trả về {#return-values}
Trả về một mảng JSON chứa tất cả các giá trị đã tổng hợp từ cột được chỉ định.

* * *

## Ví dụ {#examples}

### Tổng hợp tên nhóm tài nguyên thành một mảng JSON {#aggregate-resource-group-names-into-a-json-array}

```sql
-- Collects all resource group names into a JSON array
SELECT JSON_GROUP_ARRAY(name) as res_groups
FROM azure.resource.resource_groups
WHERE subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

Truy vấn này trả về một mảng JSON gồm các tên nhóm tài nguyên, chẳng hạn `["stackqlenv1","stackqlenv2","stackqlenv3"]`.

### Tổng hợp nhóm tài nguyên theo vị trí {#aggregate-resource-groups-by-location}

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

Truy vấn này sắp xếp các nhóm tài nguyên thành các mảng JSON dựa trên vị trí của chúng và trả về nhiều mảng, mỗi mảng cho một vị trí, chứa tên các nhóm tài nguyên ở vị trí đó.
