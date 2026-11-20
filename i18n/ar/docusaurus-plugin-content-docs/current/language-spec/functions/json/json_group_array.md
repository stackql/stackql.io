---
title: JSON_GROUP_ARRAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
description: تجميع القيم في مصفوفة JSON باستخدام SQL في StackQL.
image: "/img/stackql-featured-image.png"
---
تجمع القيم في صورة مصفوفة JSON. تجمع الدالة `JSON_GROUP_ARRAY()` كل القيم لكل مجموعة في عمود محدد وتدمجها في مصفوفة JSON.

راجع أيضًا:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## بناء الجملة {#syntax}

```sql
SELECT JSON_GROUP_ARRAY(column_name) FROM <multipartIdentifier>
WHERE condition
GROUP BY column_name;
```

## الوسيطات {#arguments}

__*column_name*__  
العمود الذي يتم تجميع قيمه في مصفوفة JSON.

## القيمة المرجعة {#return-values}
تعيد مصفوفة JSON تحتوي على جميع القيم المجمعة من العمود المحدد.

* * *

## أمثلة {#examples}

### تجميع أسماء مجموعات الموارد في مصفوفة JSON {#aggregate-resource-group-names-into-a-json-array}

```sql
-- Collects all resource group names into a JSON array
SELECT JSON_GROUP_ARRAY(name) as res_groups
FROM azure.resource.resource_groups
WHERE subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

يعيد هذا الاستعلام مصفوفة JSON بأسماء مجموعات الموارد، مثل `["stackqlenv1","stackqlenv2","stackqlenv3"]`.

### تجميع مجموعات الموارد حسب الموقع {#aggregate-resource-groups-by-location}

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

ينظم هذا الاستعلام مجموعات الموارد في مصفوفات JSON حسب موقعها، ويعيد عدة مصفوفات، واحدة لكل موقع، تحتوي كل منها على أسماء مجموعات الموارد في ذلك الموقع.
