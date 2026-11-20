---
title: JSON_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 使用 SQL 比較兩個 JSON 字串是否相等。
image: "/img/stackql-featured-image.png"
---
比較兩個 JSON 字串，若兩者相等則傳回 1，否則傳回 0。

另請參閱：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ 資料型別 ]](/language-spec/data-types)

* * * 

:::tip 

`json_equal` 函式是 StackQL 的自訂擴充函式。它支援比較 JSON 物件與陣列，並依據 JSON 規格，將鍵順序不同的物件視為相等。`json_equal` 函式適用於 StackQL `0.5.707` 及以上版本，且僅支援 StackQL 預設的內嵌式記憶體內 SQL 後端。

:::

## 語法 {#syntax}

```sql
SELECT JSON_EQUAL(json1, json2);
```

## 引數 {#arguments}

__*json1*__  
表示物件或陣列的 JSON 字串。

__*json2*__  
另一個用來與第一個引數比較的 JSON 字串。

> 兩個引數都必須是有效的 JSON 字串。

## 傳回值 {#return-values}
整數值：JSON 字串相等時為 1，不相等時為 0。

* * *

## 範例 {#examples}

### 比較 JSON 物件 {#compare-json-objects}

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

### 比較 JSON 陣列 {#compare-json-arrays}

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

如需詳細資訊，請參閱 [json_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/docs/json_equal.md)。
