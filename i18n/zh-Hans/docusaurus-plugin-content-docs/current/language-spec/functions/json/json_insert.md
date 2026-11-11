---
title: JSON_INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 向 JSON 文档插入新值，且不覆盖现有数据。
image: "/img/stackql-featured-image.png"
---
在 JSON 文档的指定路径处插入新值，不覆盖现有值。

另请参阅：  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## 语法 {#syntax}

```sql
SELECT JSON_INSERT(json_column, path, new_value) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*json_column*__  
要修改的 JSON 列或表达式。

__*path*__  
要插入新值的 JSON 路径表达式。如果该路径已存在，则不会替换该路径处的原始值。

__*new_value*__  
要在 JSON 文档的指定路径处插入的值。

## 返回值 {#return-values}
如果指定路径尚不存在，则返回插入了新值的 JSON 文档；否则原样返回 JSON 文档。

:::note 键已存在时的行为

| 函数                                                         | 已存在时是否覆盖？           | 不存在时是否创建？        |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | 否                           | 是                        |
| [`json_replace()`](/language-spec/functions/json/json_replace) | 是                           | 否                        |
| [`json_set()`](/language-spec/functions/json/json_set)       | 是                           | 是                        |

:::

* * *

## 示例 {#examples}

### 向 JSON 对象插入新的键值对 {#insert-a-new-key-value-pair-into-a-json-object}

```sql
-- Example: Insert a new 'region' key with the value 'Europe' into a JSON object
SELECT JSON_INSERT(json_data, '$.region', 'Europe') as updated_object;

/* expected output...
|-------------------------------------------------------------|
|                     updated_json                            |
|-------------------------------------------------------------|
| {"name":"ResourceOne", "type":"Storage", "region":"Europe"} |
|-------------------------------------------------------------|
*/
```

如果 'region' 键尚不存在，此查询会向 JSON 文档插入一个名为 'region'、值为 'Europe' 的新键。如果 'region' 已是文档中的键，则原样返回原始文档。
