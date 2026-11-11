---
title: JSON_REMOVE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 通过指定路径从 JSON 文档中移除元素。
image: "/img/stackql-featured-image.png"
---
使用路径表达式从 JSON 文档中移除指定的元素。如果指定的路径在文档中不存在，则忽略该路径。路径按顺序依次处理，可能会影响后续移除所针对的结构。

另请参阅：  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## 语法 {#syntax}

```sql
SELECT JSON_REMOVE(json_document, path1, path2, ...) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*json_document*__  
要从中移除元素的 JSON 文档。

__*path1, path2, ...*__  
一个或多个路径表达式，指向 JSON 文档中要移除的元素。

## 返回值 {#return-values}
返回移除了指定元素的 JSON 文档副本。如果未提供路径，则返回重新格式化并去除了多余空白字符的输入 JSON。

* * *

## 示例 {#examples}

### 从 JSON 数组中移除一个元素 {#remove-an-element-from-a-json-array}

```sql
-- Example: Remove the third element from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]') AS modified_json;
```

输出：`'[0,1,3,4]'`

### 从 JSON 数组中移除多个元素 {#remove-multiple-elements-from-a-json-array}

```sql
-- Example: Remove the first and third elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[2]', '$[0]') AS modified_json;
```

输出：`'[1,3,4]'`

### 使用负索引移除最后一个元素 {#remove-last-element-using-negative-index}

```sql
-- Example: Remove the last and first elements from a JSON array
SELECT JSON_REMOVE('[0,1,2,3,4]', '$[#-1]', '$[0]') AS modified_json;
```

输出：`'[1,2,3]'`

### 移除不存在的路径 {#remove-non-existent-path}

```sql
-- Example: Attempt to remove a non-existent key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.z') AS modified_json;
```

输出：`'{"x":25,"y":42}'`

### 从 JSON 对象中移除现有键 {#remove-existing-key-from-json-object}

```sql
-- Example: Remove the 'y' key from a JSON object
SELECT JSON_REMOVE('{"x":25,"y":42}', '$.y') AS modified_json;
```

输出：`'{"x":25}'`

### 边界情况：使用根路径移除 {#edge-case-removal-with-root-path}

```sql
-- Example: Attempt to remove using the root path
SELECT JSON_REMOVE('{"x":25,"y":42}', '$') AS modified_json;
```

输出：`NULL`

这些示例说明了如何使用 `json_remove()` 动态处理 JSON 数据，涵盖从简单删除到处理数据结构修改中的边界情况等多种场景。
