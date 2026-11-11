---
title: JSON_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 使用 SQL 比较两个 JSON 字符串是否等价。
image: "/img/stackql-featured-image.png"
---
比较两个 JSON 字符串，如果二者等价则返回 true，否则返回 false。

另请参阅：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ 数据类型 ]](/language-spec/data-types)

* * * 

:::tip 

`json_equal` 函数是自定义的 StackQL 扩展函数。它支持比较 JSON 对象和数组，并按照 JSON 规范将键顺序不同的对象视为等价。`json_equal` 函数在 StackQL `0.5.707` 及以上版本中可用，并且仅支持 StackQL 默认的嵌入式内存 SQL 后端。

:::

## 语法 {#syntax}

```sql
SELECT JSON_EQUAL(json1, json2);
```

## 参数 {#arguments}

__*json1*__  
表示对象或数组的 JSON 字符串。

__*json2*__  
另一个 JSON 字符串，用于与第一个进行比较。

> 两个参数都必须是有效的 JSON 字符串。

## 返回值 {#return-values}
一个整数值：如果 JSON 字符串等价则为 1，否则为 0。

* * *

## 示例 {#examples}

### 比较 JSON 对象 {#compare-json-objects}

```sql
SELECT 
public_access_block_configuration as current_state,
'{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}' as desired_state,
JSON_EQUAL(public_access_block_configuration, '{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}') as comp
FROM awscc.s3.buckets 
WHERE region = 'ap-southeast-2' 
AND Identifier = 'my-bucket';
/* returns...
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
|                                             current_state                                              |         desired_state          | comp |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
| {"RestrictPublicBuckets":true,"BlockPublicPolicy":true,"BlockPublicAcls":true,"IgnorePublicAcls":true} | {"BlockPublicAcls": true,      |    1 |
|                                                                                                        | "BlockPublicPolicy": true,     |      |
|                                                                                                        | "IgnorePublicAcls": true,      |      |
|                                                                                                        | "RestrictPublicBuckets": true} |      |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
*/
```

### 比较 JSON 数组 {#compare-json-arrays}

```sql
SELECT 
  json_equal('[1, 2, 3]', '[1, 2, 3]') as array_comp_1,
  json_equal('[1, 2, 3]', '[3, 2, 1]') as array_comp_2
; 
/* returns...
|--------------|--------------|
| array_comp_1 | array_comp_2 |
|--------------|--------------|
|            1 |            0 |
|--------------|--------------|
*/
```

有关更多信息，请参阅 [json_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/docs/json_equal.md)。
