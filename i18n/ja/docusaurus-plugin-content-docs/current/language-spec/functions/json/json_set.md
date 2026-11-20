---
title: JSON_SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って JSON ドキュメントの値を更新または挿入します。
image: "/img/stackql-featured-image.png"
---
JSON ドキュメントの指定したパスにある既存の値を更新するか、新しい値を挿入します。

関連項目：  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_INSERT `]](/language-spec/functions/json/json_insert) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## 構文 {#syntax}

```sql
SELECT JSON_SET(json_column, path, value_to_set) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*json_column*__  
変更する JSON 列または式。

__*path*__  
値を設定する場所を示す JSON パス式。パスが存在する場合は値が更新され、パスが存在しない場合は新しいキーと値のペアが追加されます。

__*value_to_set*__  
JSON ドキュメントの指定したパスに設定する値。

## 戻り値 {#return-values}
指定したパスに値を設定した JSON ドキュメントを返します。パスが存在する場合は既存の値が更新され、存在しない場合は新しいキーと値のペアが追加されます。

:::note キーが存在する場合の動作

| 関数                                                         | すでに存在する場合に上書きするか？ | 存在しない場合に作成するか？ |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | いいえ                       | はい                      |
| [`json_replace()`](/language-spec/functions/json/json_replace) | はい                         | いいえ                    |
| [`json_set()`](/language-spec/functions/json/json_set)       | はい                         | はい                      |

:::

* * *

## 例 {#examples}

### JSON オブジェクトの既存の値を更新する {#update-an-existing-value-in-a-json-object}

```sql
-- Example: Update the 'status' key's value to 'active' in a JSON object
SELECT JSON_SET('{"name":"ServiceOne","status":"pending","region":"US"}', '$.status', 'active') as updated_json;

-- Expected output:
-- |---------------------------------------------------------|
-- |                    updated_json                         |
-- |---------------------------------------------------------|
-- | {"name":"ServiceOne","status":"active","region":"US"}   |
-- |---------------------------------------------------------|
```

このクエリは、'status' キーが存在する場合、JSON ドキュメントの 'status' キーを 'active' に更新します。ドキュメントに 'status' が存在しない場合は、値 'active' で追加されます。

### JSON オブジェクトに新しいキーと値のペアを追加する {#add-a-new-key-value-pair-to-a-json-object}

```sql
-- Example: Add a new key 'lastChecked' with a date value to a JSON object
SELECT JSON_SET('{"name":"ServiceOne","status":"pending"}', '$.lastChecked', '2024-04-29') as updated_json;

/* expected output...
|-----------------------------------------------------------------------|
|                          updated_json                                 |
|-----------------------------------------------------------------------|
| {"name":"ServiceOne","status":"pending","lastChecked":"2024-04-29"}   |
|-----------------------------------------------------------------------|
*/
```

このクエリは、'lastChecked' がまだ存在しない場合、値が '2024-04-29' の新しいキー 'lastChecked' を JSON ドキュメントに追加します。すでに存在する場合は、既存の値が '2024-04-29' に更新されます。
