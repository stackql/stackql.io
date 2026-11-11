---
title: JSON_SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 更新或插入 JSON 文档中的值。
image: "/img/stackql-featured-image.png"
---
在 JSON 文档的指定路径处更新现有值或插入新值。

另请参阅：  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## 语法 {#syntax}

```sql
SELECT JSON_SET(json_column, path, value_to_set) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*json_column*__  
要修改的 JSON 列或表达式。

__*path*__  
指明在何处设置值的 JSON 路径表达式。如果路径存在，则更新该值；如果路径不存在，则添加新的键值对。

__*value_to_set*__  
要在 JSON 文档的指定路径处设置的值。

## 返回值 {#return-values}
返回在指定路径处设置了值的 JSON 文档。如果路径存在，则更新现有值；否则添加新的键值对。

:::note 键已存在时的行为

| 函数                                                         | 已存在时是否覆盖？           | 不存在时是否创建？        |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | 否                           | 是                        |
| [`json_replace()`](/language-spec/functions/json/json_replace) | 是                           | 否                        |
| [`json_set()`](/language-spec/functions/json/json_set)       | 是                           | 是                        |

:::

* * *

## 示例 {#examples}

### 更新 JSON 对象中的现有值 {#update-an-existing-value-in-a-json-object}

```sql
-- Example: Update the 'status' key's value to 'active' in a JSON object
SELECT JSON_SET(json_data, '$.status', 'active') as updated_json
FROM cloud_services
WHERE service_id = 'service123';

-- Expected output:
-- |---------------------------------------------------------|
-- |                    updated_json                         |
-- |---------------------------------------------------------|
-- | {"name":"ServiceOne", "status":"active", "region":"US"} |
-- |---------------------------------------------------------|
```

如果 'status' 键存在，此查询会将 JSON 文档中的 'status' 键更新为 'active'。如果文档中不存在 'status'，则以值 'active' 添加该键。

### 向 JSON 对象添加新的键值对 {#add-a-new-key-value-pair-to-a-json-object}

```sql
-- Example: Add a new key 'lastChecked' with the current date to a JSON object
SELECT JSON_SET(json_data, '$.lastChecked', '2024-04-29') as updated_json
FROM cloud_services
WHERE service_id = 'service123';

/* expected output...
|-----------------------------------------------------------------------|
|                          updated_json                                 |
|-----------------------------------------------------------------------|
| {"name":"ServiceOne", "status":"pending", "lastChecked":"2024-04-29"} |
|-----------------------------------------------------------------------|
*/
```

如果 'lastChecked' 尚不存在，此查询会向 JSON 文档添加一个值为 '2024-04-29' 的新键 'lastChecked'。如果已存在，则将现有值更新为 '2024-04-29'。
