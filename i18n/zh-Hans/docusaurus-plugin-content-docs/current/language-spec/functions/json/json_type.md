---
title: JSON_TYPE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 JSON_TYPE 函数：JSON 值最外层元素或其中某个路径的类型（object、array、text、integer、real、true、false 或 null）。"
image: "/img/stackql-featured-image.png"
---
返回输入的 JSON 对象或数组及路径的最外层元素的数据类型。  

另请参阅：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ 数据类型 ]](/language-spec/data-types)

* * * 

:::tip 

使用 [**DESCRIBE**](/language-spec/describe) 函数查找可与 StackQL JSON 函数配合使用的 `array` 或 `object` 数据类型。

:::

:::note 

搭配 `postgres` SQL 后端使用 stackql 时，`json_type` 函数实现为 `json_typeof` 函数。

:::

## 语法 {#syntax}

```sql
SELECT JSON_TYPE(json_array_or_object, json_path) 
FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*json_array_or_object*__  
JSON 数组或对象。

__*json_path*__  
指向 *json_array_or_object* 中包含的数组或对象的 JSON 路径。请参阅 [jsonpath.com](https://jsonpath.com/)。

> 如果 *json_path* 在 *json_array_or_object* 中不存在，则函数返回 `NULL`。

> 如果任一参数格式不正确，`json_type()` 函数会抛出错误。

## 返回值 {#return-values}
表示解析得到的数据类型的字符串。

> 数据类型包括 `null`、`true`、`false`、`integer`、`real`、`text`、`array` 或 `object`。

* * *

## 示例 {#examples}

### 返回嵌套在 JSON 对象或数组中的字段的数据类型 {#return-the-datatype-of-a-field-nested-within-a-json-object-or-array}

```sql
SELECT json_type(disks, '$[0].licenses[0]') as datatype
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns text */
```

有关更多信息，请参阅 [https://www.sqlite.org/json1.html#jtype](https://www.sqlite.org/json1.html#jtype)
