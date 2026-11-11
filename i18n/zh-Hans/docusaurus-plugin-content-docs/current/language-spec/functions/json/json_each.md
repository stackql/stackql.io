---
title: JSON_EACH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 JSON_EACH 表值函数：将 JSON 数组或对象展开为每个元素或成员一行，用于连接和过滤嵌套的 API 响应。"
image: "/img/stackql-featured-image.png"
---
表值函数，返回一个表，其中每个数组元素或对象成员对应一行。

另请参阅：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ 数据类型 ]](/language-spec/data-types)

* * * 

:::tip 

使用 [**DESCRIBE**](/language-spec/describe) 函数查找可用于 `json_each` 函数的 `array` 数据类型。

:::

:::note 

如果你将 stackql 与 `postgres` SQL 后端一起使用，`json_each` 函数将实现为 `json_array_elements_text` 函数

:::

## 语法 {#syntax}

```sql
SELECT <alias>.<projectedField>, ... , json_each.value  
FROM <multipartIdentifier> <alias>, JSON_EACH(json_array_or_object, json_path);
```
## 参数 {#arguments}

__*json_array_or_object*__  
JSON 数组或对象。

__*json_path*__  
指向 *json_array_or_object* 内所含数组或对象的 JSON 路径。请参阅 [jsonpath.com](https://jsonpath.com/)。

> 如果 *json_path* 在 *json_array_or_object* 中不存在，则函数返回 `NULL`。

> 如果任一参数格式不正确，`json_type()` 函数会抛出错误。

## 返回值 {#return-values}
一个表，其中每个数组元素或对象成员对应一行。

* * *

## 示例 {#examples}

### 将字符串数组展开为与相关字段连接的表 {#unnest-an-array-of-strings-into-a-table-joined-with-associated-fields}

背景信息：  

```sql
describe google.cloudresourcemanager.projects_iam_policies;
```

返回...

```
|-----------|--------|                                                                                                                                                                                                     
|   name    |  type  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| condition | object |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| members   | array  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| role      | string |                                                                                                                                                                                                     
|-----------|--------|  
```

以下 `stackql` 查询使用 `json_each` 函数将 `members` 数组展开为表，并与 `role` 字段以及可选的 `condition` 字段连接。  

```sql
select 
iam.role, 
SPLIT_PART(json_each.value, ':', 2) as member 
from google.cloudresourcemanager.projects_iam_policies iam, json_each(members) 
where projectsId = 'stackql';
```

将返回...

```
|--------------------------------------|-------------------------------------------------------|                                                                                                     
|                 role                 |                     member                            |                                                                                                     
|--------------------------------------|-------------------------------------------------------|                                                                                                     
| roles/cloudbuild.builds.builder      | 1234567890@cloudbuild.gserviceaccount.com           |                                                                                                     
|--------------------------------------|---------------------------------------------- --------|                                                                                                     
| roles/editor                         | 1234567890-compute@developer.gserviceaccount.com    |                                                                                                     
|--------------------------------------|-------------------------------------------------------|                                                                                                     
| roles/editor                         | 1234567890@cloudservices.gserviceaccount.com        |                                                                                                     
|--------------------------------------|-------------------------------------------------------|                                                                                                     
| roles/editor                         | stackql@appspot.gserviceaccount.com                   |                                                                                                     
|--------------------------------------|-------------------------------------------------------|                                                                                                     
| ...                                  | ...                                                   |                                                                                                     
|--------------------------------------|-------------------------------------------------------|    
```

有关更多信息，请参阅 [https://www.sqlite.org/json1.html#jeach](https://www.sqlite.org/json1.html#jeach)
