---
title: JSON_GROUP_ARRAY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
description: Mengagregasi nilai menjadi array JSON menggunakan SQL di StackQL.
image: "/img/stackql-featured-image.png"
---
Mengagregasi nilai sebagai array JSON. Fungsi `JSON_GROUP_ARRAY()` mengumpulkan semua nilai untuk setiap grup pada kolom tertentu dan menyatukannya menjadi array JSON.

Lihat juga:  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## Sintaksis {#syntax}

```sql
SELECT grouping_column, JSON_GROUP_ARRAY(column_name) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## Argumen {#arguments}

__*column_name*__  
Kolom yang nilainya diagregasi menjadi array JSON.

## Nilai yang dikembalikan {#return-values}
Mengembalikan array JSON yang berisi semua nilai teragregasi dari kolom yang ditentukan.

* * *

## Contoh {#examples}

### Mengagregasi nama grup sumber daya menjadi array JSON {#aggregate-resource-group-names-into-a-json-array}

```sql
-- Collects all resource group names into a JSON array
SELECT JSON_GROUP_ARRAY(name) as res_groups
FROM azure.resource.resource_groups
WHERE subscription_id = '631d1c6d-2a65-43e7-93c2-688bfe4e1468';
```

Kueri ini mengembalikan array JSON berisi nama grup sumber daya, misalnya `["stackqlenv1","stackqlenv2","stackqlenv3"]`.

### Mengagregasi grup sumber daya berdasarkan lokasi {#aggregate-resource-groups-by-location}

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

Kueri ini menyusun grup sumber daya ke dalam array JSON berdasarkan lokasinya dan mengembalikan beberapa array, satu untuk setiap lokasi, yang berisi nama grup sumber daya di lokasi tersebut.
