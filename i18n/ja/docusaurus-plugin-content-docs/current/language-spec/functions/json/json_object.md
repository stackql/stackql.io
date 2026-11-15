---
title: JSON_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の JSON_OBJECT 関数：キーと値の引数から JSON オブジェクトを構築します（INSERT や UPDATE のリクエストボディの作成など）。"
image: "/img/stackql-featured-image.png"
---
指定した引数から構築したオブジェクトを返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ データ型 ]](/language-spec/data-types)

* * * 

:::tip 

[**DESCRIBE**](/language-spec/describe) 文を使用すると、StackQL の JSON 関数で使用できる `array` または `object` データ型を確認できます。

:::

:::note 

`postgres` SQL バックエンドで stackql を使用している場合は、`json_object` の代わりに `jsonb_build_object` を使用してください。StackQL は `json_object` をそのまま PostgreSQL に渡し、PostgreSQL の同名の関数はキーと値をテキスト配列として受け取ります。

:::

## 構文 {#syntax}

```sql
SELECT JSON_OBJECT(key1, value1, key2, value2, ...) 
FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*key1, key2, ...*__  
JSON オブジェクトのラベル。

__*value1, value2, ...*__  
JSON オブジェクト内のラベルに対応する値。

## 戻り値 {#return-values}
整形式の JSON オブジェクトを表す文字列。

* * *

## 例 {#examples}

### 列から JSON オブジェクトの配列を作成する {#build-an-array-of-json-objects-from-a-column}

```sql
SELECT JSON_GROUP_ARRAY(json_object('instance', selfLink)) as instance_links
FROM google.compute.instances 
WHERE project = 'stackql-k8s-the-hard-way-demo'
AND zone = 'australia-southeast1-a'
AND name like '%-dev-controller-%';
/* returns a data structure required for a targetPool in GCP */
```

詳細については、[https://sqlite.org/json1.html#jobj](https://sqlite.org/json1.html#jobj) を参照してください。
