---
title: JSON_INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って、既存のデータを上書きせずに JSON ドキュメントに新しい値を挿入します。
image: "/img/stackql-featured-image.png"
---
既存の値を上書きせずに、JSON ドキュメントの指定したパスに新しい値を挿入します。

関連項目：  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## 構文 {#syntax}

```sql
SELECT JSON_INSERT(json_column, path, new_value) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*json_column*__  
変更する JSON 列または式。

__*path*__  
新しい値を挿入する位置を示す JSON パス式。パスがすでに存在する場合、そのパスにある元の値は置き換えられません。

__*new_value*__  
JSON ドキュメントの指定したパスに挿入する値。

## 戻り値 {#return-values}
指定したパスがまだ存在しない場合は、新しい値を挿入した JSON ドキュメントを返します。存在する場合は、JSON ドキュメントを変更せずに返します。

:::note キーが存在する場合の動作

| 関数                                                         | すでに存在する場合に上書き | 存在しない場合に作成 |
|--------------------------------------------------------------|----------------------------|----------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | いいえ                     | はい                 |
| [`json_replace()`](/language-spec/functions/json/json_replace) | はい                       | いいえ               |
| [`json_set()`](/language-spec/functions/json/json_set)       | はい                       | はい                 |

:::

* * *

## 例 {#examples}

### JSON オブジェクトに新しいキーと値のペアを挿入する {#insert-a-new-key-value-pair-into-a-json-object}

```sql
-- Example: Insert a new 'region' key with the value 'Europe' into a JSON object
SELECT JSON_INSERT('{"name":"ResourceOne","type":"Storage"}', '$.region', 'Europe') as updated_object;

/* expected output...
|-----------------------------------------------------------|
|                      updated_object                       |
|-----------------------------------------------------------|
| {"name":"ResourceOne","type":"Storage","region":"Europe"} |
|-----------------------------------------------------------|
*/
```

このクエリは、'region' キーがまだ存在しない場合に、値が 'Europe' の 'region' という新しいキーを JSON ドキュメントに挿入します。'region' がすでにドキュメント内のキーである場合は、元のドキュメントが変更されずに返されます。
