---
title: JSON_EACH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の JSON_EACH テーブル値関数：JSON 配列またはオブジェクトを要素またはメンバーごとに 1 行に展開し、ネストした API レスポンスの結合とフィルタリングに使用します。"
image: "/img/stackql-featured-image.png"
---
配列要素またはオブジェクトメンバーごとに 1 行を含むテーブルを返すテーブル値関数です。

関連項目：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ データ型 ]](/language-spec/data-types)

* * * 

:::tip 

[**DESCRIBE**](/language-spec/describe) 文を使用すると、`json_each` 関数で使用できる `array` データ型を確認できます。

:::

:::note 

`postgres` SQL バックエンドで stackql を使用している場合、`json_each` 関数は `json_array_elements_text` 関数として実装されます。

:::

## 構文 {#syntax}

```sql
SELECT <alias>.<projectedField>, ... , json_each.value  
FROM <multipartIdentifier> <alias>, JSON_EACH(json_array_or_object, json_path);
```
## 引数 {#arguments}

__*json_array_or_object*__  
JSON 配列またはオブジェクト。

__*json_path*__  
*json_array_or_object* に含まれる配列またはオブジェクトへの JSON パス。[jsonpath.com](https://jsonpath.com/) を参照してください。

> *json_path* が *json_array_or_object* に存在しない場合、関数は行を返しません。

> *json_array_or_object* が整形式の JSON でない場合、`json_each()` 関数はエラーを発生させます。

## 戻り値 {#return-values}
配列要素またはオブジェクトメンバーごとに 1 行を含むテーブル。

* * *

## 例 {#examples}

### 文字列の配列を関連フィールドと結合したテーブルに展開する {#unnest-an-array-of-strings-into-a-table-joined-with-associated-fields}

前提として：  

```sql
describe google.cloudresourcemanager.projects_iam_policies;
```

このクエリは次の結果を返します：

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

次の `stackql` クエリは、`json_each` 関数を使用して `members` 配列をテーブルに展開し、`role` フィールドと、必要に応じて `condition` フィールドと結合します。  

```sql
select 
iam.role, 
SPLIT_PART(json_each.value, ':', 2) as member 
from google.cloudresourcemanager.projects_iam_policies iam, json_each(members) 
where projectsId = 'stackql';
```

次のような結果が返されます：

```
|--------------------------------------|-------------------------------------------------------|
|                 role                 |                        member                         |
|--------------------------------------|-------------------------------------------------------|
| roles/cloudbuild.builds.builder      | 1234567890@cloudbuild.gserviceaccount.com             |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890-compute@developer.gserviceaccount.com      |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890@cloudservices.gserviceaccount.com          |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | stackql@appspot.gserviceaccount.com                   |
|--------------------------------------|-------------------------------------------------------|
| ...                                  | ...                                                   |
|--------------------------------------|-------------------------------------------------------|
```

詳細については、[https://www.sqlite.org/json1.html#jeach](https://www.sqlite.org/json1.html#jeach) を参照してください。
