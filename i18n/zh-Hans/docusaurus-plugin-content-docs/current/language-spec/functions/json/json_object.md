---
title: JSON_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 JSON_OBJECT 函数：根据键和值参数构建 JSON 对象，例如用于构造 INSERT 和 UPDATE 的请求体。"
image: "/img/stackql-featured-image.png"
---
返回由所提供的参数构造的对象。  

另请参阅：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ 数据类型 ]](/language-spec/data-types)

* * * 

:::tip 

使用 [**DESCRIBE**](/language-spec/describe) 函数查找可与 StackQL JSON 函数配合使用的 `array` 或 `object` 数据类型。

:::

:::note 

搭配 `postgres` SQL 后端使用 stackql 时，`json_object` 函数实现为 `jsonb_build_object`。

:::

## 语法 {#syntax}

```sql
SELECT JSON_OBJECT(key1, value1, key2, value2, ...) 
FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*key1, key2, ...*__  
JSON 对象的标签。

__*value1, value2, ...*__  
JSON 对象中与标签对应的值。

## 返回值 {#return-values}
表示格式正确的 JSON 对象的字符串。

* * *

## 示例 {#examples}

### 返回嵌套在 JSON 对象或数组中的字段的数据类型 {#return-the-datatype-of-a-field-nested-within-a-json-object-or-array}

```sql
SELECT JSON_GROUP_ARRAY(json_object('instance', selfLink)) as instance_links
FROM google.compute.instances 
WHERE project = 'stackql-k8s-the-hard-way-demo'
AND zone = 'australia-southeast1-a'
AND name like '%-dev-controller-%';
/* returns a data structure required for a targetPool in GCP */
```

有关更多信息，请参阅 [https://sqlite.org/json1.html#jobj](https://sqlite.org/json1.html#jobj)
