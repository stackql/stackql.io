---
title: JSON_PATCH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 將 JSON MergePatch 套用至 JSON 文件，以修改或合併 JSON 物件。
image: "/img/stackql-featured-image.png"
---
將 JSON MergePatch（RFC-7396）套用至 JSON 文件，依指定的作業修改該文件。此函式用於新增、修改或刪除 JSON 物件中的元素，但會將 JSON 陣列視為不可分割的單位。

另請參閱：  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## 語法 {#syntax}

```sql
SELECT JSON_PATCH(target_json, patch_json) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*target_json*__  
要修補的 JSON 文件。

__*patch_json*__  
描述要依 MergePatch 演算法套用之變更的 JSON 文件。

## 傳回值 {#return-values}
傳回套用修補後的 JSON 文件。

* * *

## 範例 {#examples}

### 簡單的 MergePatch {#simple-mergepatch}

```sql
-- Example: Simple merge of new elements into the target JSON object
SELECT JSON_PATCH('{"a":1,"b":2}', '{"c":3,"d":4}') AS patched_json;
```

輸出：`{"a":1,"b":2,"c":3,"d":4}`

### 以值取代陣列 {#replace-an-array-with-a-value}

```sql
-- Example: Replacing an array with a single value in the JSON object
SELECT JSON_PATCH('{"a":[1,2],"b":2}', '{"a":9}') AS patched_json;
```

輸出：`{"a":9,"b":2}`

### 使用 null 移除元素 {#remove-element-using-null}

```sql
-- Example: Removing an element by setting it to null
SELECT JSON_PATCH('{"a":1,"b":2}', '{"a":9,"b":null,"c":8}') AS patched_json;
```

輸出：`{"a":9,"c":8}`

### 巢狀 MergePatch {#nested-mergepatch}

```sql
-- Example: Nested merge patch updating sub-keys within a nested JSON object
SELECT JSON_PATCH('{"a":{"x":1,"y":2},"b":3}', '{"a":{"y":9},"c":8}') AS patched_json;
```

輸出：`{"a":{"x":1,"y":9},"b":3,"c":8}`

此功能可彈性且動態地更新儲存在資料庫中的 JSON，特別適用於需要有條件地修改資料結構的應用程式。
