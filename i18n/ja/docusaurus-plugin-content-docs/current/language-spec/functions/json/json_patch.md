---
title: JSON_PATCH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って JSON ドキュメントに JSON MergePatch を適用し、JSON オブジェクトを変更またはマージします。
image: "/img/stackql-featured-image.png"
---
JSON ドキュメントに JSON MergePatch（RFC-7396）を適用し、指定した操作に従ってドキュメントを変更します。この関数は JSON オブジェクト内の要素の追加、変更、削除に使用しますが、JSON 配列は不可分の単位として扱います。

関連項目：  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## 構文 {#syntax}

```sql
SELECT JSON_PATCH(target_json, patch_json) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*target_json*__  
パッチを適用する JSON ドキュメント。

__*patch_json*__  
MergePatch アルゴリズムに従って適用する変更を記述した JSON ドキュメント。

## 戻り値 {#return-values}
パッチを適用した後の、変更された JSON ドキュメントを返します。

* * *

## 例 {#examples}

### シンプルな MergePatch {#simple-mergepatch}

```sql
-- Example: Simple merge of new elements into the target JSON object
SELECT JSON_PATCH('{"a":1,"b":2}', '{"c":3,"d":4}') AS patched_json;
```

出力：`{"a":1,"b":2,"c":3,"d":4}`

### 配列を値で置き換える {#replace-an-array-with-a-value}

```sql
-- Example: Replacing an array with a single value in the JSON object
SELECT JSON_PATCH('{"a":[1,2],"b":2}', '{"a":9}') AS patched_json;
```

出力：`{"a":9,"b":2}`

### null を使用して要素を削除する {#remove-element-using-null}

```sql
-- Example: Removing an element by setting it to null
SELECT JSON_PATCH('{"a":1,"b":2}', '{"a":9,"b":null,"c":8}') AS patched_json;
```

出力：`{"a":9,"c":8}`

### ネストした MergePatch {#nested-mergepatch}

```sql
-- Example: Nested merge patch updating sub-keys within a nested JSON object
SELECT JSON_PATCH('{"a":{"x":1,"y":2},"b":3}', '{"a":{"y":9},"c":8}') AS patched_json;
```

出力：`{"a":{"x":1,"y":9},"b":3,"c":8}`

この機能により、データベースに格納された JSON を柔軟かつ動的に更新できます。データ構造を条件付きで変更する必要があるアプリケーションで特に役立ちます。
