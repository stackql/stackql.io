---
title: JSON_TYPE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の JSON_TYPE 関数：JSON 値の最も外側の要素、またはその中のパスの型（object、array、text、integer、real、true、false または null）。"
image: "/img/stackql-featured-image.png"
---
入力された JSON オブジェクトまたは配列とパスについて、最も外側の要素のデータ型を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ データ型 ]](/language-spec/data-types)

* * * 

:::tip 

[**DESCRIBE**](/language-spec/describe) 関数を使用すると、StackQL の JSON 関数で使用できる `array` または `object` データ型を見つけられます。

:::

:::note 

`postgres` SQL バックエンドで stackql を使用している場合、`json_type` 関数は `json_typeof` 関数として実装されています。

:::

## 構文 {#syntax}

```sql
SELECT JSON_TYPE(json_array_or_object, json_path) 
FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*json_array_or_object*__  
JSON 配列またはオブジェクト。

__*json_path*__  
*json_array_or_object* に含まれる配列またはオブジェクトへの JSON パス。[jsonpath.com](https://jsonpath.com/) を参照してください。

> *json_path* が *json_array_or_object* に存在しない場合、関数は `NULL` を返します。

> いずれかの引数が整形式でない場合、`json_type()` 関数はエラーを発生させます。

## 戻り値 {#return-values}
解決されたデータ型を表す文字列。

> データ型には `null`、`true`、`false`、`integer`、`real`、`text`、`array`、`object` があります。

* * *

## 例 {#examples}

### JSON オブジェクトまたは配列内にネストされたフィールドのデータ型を返す {#return-the-datatype-of-a-field-nested-within-a-json-object-or-array}

```sql
SELECT json_type(disks, '$[0].licenses[0]') as datatype
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns text */
```

詳細については、[https://www.sqlite.org/json1.html#jtype](https://www.sqlite.org/json1.html#jtype) を参照してください。
