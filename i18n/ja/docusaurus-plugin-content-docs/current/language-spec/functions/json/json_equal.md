---
title: JSON_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: SQL を使って 2 つの JSON 文字列が等価かどうかを比較します。
image: "/img/stackql-featured-image.png"
---
2 つの JSON 文字列を比較し、等価であれば true、そうでなければ false を返します。

関連項目：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ データ型 ]](/language-spec/data-types)

* * * 

:::tip 

`json_equal` 関数は、StackQL 独自の拡張関数です。JSON オブジェクトと配列の比較をサポートし、JSON 仕様に従って、キーの順序が異なるオブジェクトを等価として扱います。`json_equal` 関数は StackQL `0.5.707` 以降で利用でき、StackQL のデフォルトである組み込みのインメモリ SQL バックエンドでのみサポートされています。

:::

## 構文 {#syntax}

```sql
SELECT JSON_EQUAL(json1, json2);
```

## 引数 {#arguments}

__*json1*__  
オブジェクトまたは配列を表す JSON 文字列。

__*json2*__  
1 つ目と比較するもう 1 つの JSON 文字列。

> 両方の引数が有効な JSON 文字列である必要があります。

## 戻り値 {#return-values}
整数値：JSON 文字列が等価であれば 1、等価でなければ 0。

* * *

## 例 {#examples}

### JSON オブジェクトを比較する {#compare-json-objects}

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

### JSON 配列を比較する {#compare-json-arrays}

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

詳細については、[json_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/docs/json_equal.md) を参照してください。
