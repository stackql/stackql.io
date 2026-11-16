---
title: JSON_REMOVE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使い、パスを指定して JSON ドキュメントから要素を削除します。
image: "/img/stackql-featured-image.png"
---
パス式を使用して、JSON ドキュメントから指定した要素を削除します。指定したパスがドキュメント内に存在しない場合、そのパスは無視されます。パスは順番に処理されるため、前の削除が後続の削除の対象となる構造に影響することがあります。

関連項目：  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## 構文 {#syntax}

```sql
SELECT JSON_REMOVE(json_document, path1, path2, ...) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*json_document*__  
要素を削除する対象の JSON ドキュメント。

__*path1, path2, ...*__  
削除する JSON ドキュメント内の要素を指す 1 つ以上のパス式。

## 戻り値 {#return-values}
指定した要素を削除した JSON ドキュメントのコピーを返します。パスを指定しない場合は、余分な空白文字を取り除いて整形し直した入力 JSON を返します。

* * *

## 例 {#examples}

### JSON 配列から要素を削除する {#remove-an-element-from-a-json-array}

```sql
-- Example: Remove the third element from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]') AS modified_json;
```

出力：`'[0,1,3,4]'`

### JSON 配列から複数の要素を削除する {#remove-multiple-elements-from-a-json-array}

```sql
-- Example: Remove the first and third elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]', '$[0]') AS modified_json;
```

出力：`'[1,3,4]'`

### 負のインデックスを使用して最後と最初の要素を削除する {#remove-last-element-using-negative-index}

```sql
-- Example: Remove the last and first elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[#-1]', '$[0]') AS modified_json;
```

出力：`'[1,2,3]'`

### 存在しないパスを削除する {#remove-non-existent-path}

```sql
-- Example: Attempt to remove a non-existent key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.z') AS modified_json;
```

出力：`'{"x":25,"y":42}'`

### JSON オブジェクトから既存のキーを削除する {#remove-existing-key-from-json-object}

```sql
-- Example: Remove the 'y' key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.y') AS modified_json;
```

出力：`'{"x":25}'`

### エッジケース：ルートパスによる削除 {#edge-case-removal-with-root-path}

```sql
-- Example: Attempt to remove using the root path
SELECT JSON_REMOVE('{"x":25,"y":42}', '$') AS modified_json;
```

出力：`NULL`

これらの例は、`json_remove()` を使用して JSON データを動的に操作する方法を示しています。単純な削除から、データ構造の変更におけるエッジケースの処理まで、さまざまなシナリオに対応します。
