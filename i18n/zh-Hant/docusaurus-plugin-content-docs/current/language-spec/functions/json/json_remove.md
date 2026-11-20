---
title: JSON_REMOVE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 指定路徑，從 JSON 文件中移除元素。
image: "/img/stackql-featured-image.png"
---
使用路徑運算式從 JSON 文件中移除指定的元素。如果指定的路徑不存在於文件中，則會略過該路徑。路徑依序處理，因此前面的移除可能影響後續移除時的結構。

另請參閱：  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## 語法 {#syntax}

```sql
SELECT JSON_REMOVE(json_document, path1, path2, ...) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*json_document*__  
要從中移除元素的 JSON 文件。

__*path1, path2, ...*__  
一或多個路徑運算式，指向 JSON 文件中要移除的元素。

## 傳回值 {#return-values}
傳回已移除指定元素的 JSON 文件複本。如果未提供任何路徑，則傳回移除多餘空白字元並重新格式化的輸入 JSON。

* * *

## 範例 {#examples}

### 從 JSON 陣列中移除元素 {#remove-an-element-from-a-json-array}

```sql
-- Example: Remove the third element from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]') AS modified_json;
```

輸出：`'[0,1,3,4]'`

### 從 JSON 陣列中移除多個元素 {#remove-multiple-elements-from-a-json-array}

```sql
-- Example: Remove the first and third elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]', '$[0]') AS modified_json;
```

輸出：`'[1,3,4]'`

### 使用負數索引移除最後一個和第一個元素 {#remove-last-element-using-negative-index}

```sql
-- Example: Remove the last and first elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[#-1]', '$[0]') AS modified_json;
```

輸出：`'[1,2,3]'`

### 移除不存在的路徑 {#remove-non-existent-path}

```sql
-- Example: Attempt to remove a non-existent key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.z') AS modified_json;
```

輸出：`'{"x":25,"y":42}'`

### 從 JSON 物件中移除現有的鍵 {#remove-existing-key-from-json-object}

```sql
-- Example: Remove the 'y' key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.y') AS modified_json;
```

輸出：`'{"x":25}'`

### 邊界情況：使用根路徑移除 {#edge-case-removal-with-root-path}

```sql
-- Example: Attempt to remove using the root path
SELECT JSON_REMOVE('{"x":25,"y":42}', '$') AS modified_json;
```

輸出：`NULL`

這些範例說明如何使用 `json_remove()` 動態調整 JSON 資料，涵蓋從簡單刪除到處理資料結構修改中邊界情況的各種情境。
