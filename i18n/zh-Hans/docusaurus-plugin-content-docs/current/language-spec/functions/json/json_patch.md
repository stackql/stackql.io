---
title: JSON_PATCH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 对 JSON 文档应用 JSON MergePatch，以修改或合并 JSON 对象。
image: "/img/stackql-featured-image.png"
---
对 JSON 文档应用 JSON MergePatch（RFC-7396），按指定的操作对其进行修改。此函数用于添加、修改或删除 JSON 对象中的元素，但将 JSON 数组视为原子单元。

另请参阅：  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract)

* * * 

## 语法 {#syntax}

```sql
SELECT JSON_PATCH(target_json, patch_json) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*target_json*__  
要应用补丁的 JSON 文档。

__*patch_json*__  
一个 JSON 文档，描述要按照 MergePatch 算法应用的更改。

## 返回值 {#return-values}
返回应用补丁后经过修改的 JSON 文档。

* * *

## 示例 {#examples}

### 简单的 MergePatch {#simple-mergepatch}

```sql
-- Example: Simple merge of new elements into the target JSON object
SELECT JSON_PATCH('{"a":1,"b":2}', '{"c":3,"d":4}') AS patched_json;
```

输出：`{"a":1,"b":2,"c":3,"d":4}`

### 替换数组中的元素 {#replace-element-in-array}

```sql
-- Example: Replacing an array with a single value in the JSON object
SELECT JSON_PATCH('{"a":[1,2],"b":2}', '{"a":9}') AS patched_json;
```

输出：`{"a":9,"b":2}`

### 使用 null 移除元素 {#remove-element-using-null}

```sql
-- Example: Removing an element by setting it to null
SELECT JSON_PATCH('{"a":1,"b":2}', '{"a":9,"b":null,"c":8}') AS patched_json;
```

输出：`{"a":9,"c":8}`

### 嵌套的 MergePatch {#nested-mergepatch}

```sql
-- Example: Nested merge patch updating sub-keys within a nested JSON object
SELECT JSON_PATCH('{"a":{"x":1,"y":2},"b":3}', '{"a":{"y":9},"c":8}') AS patched_json;
```

输出：`{"a":{"x":1,"y":9},"b":3,"c":8}`

此功能可以对存储在数据库中的 JSON 进行灵活的动态更新，对于需要按条件修改数据结构的应用尤其有用。
