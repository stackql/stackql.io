---
title: JSON_GROUP_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 将键值对聚合为 JSON 对象。
image: "/img/stackql-featured-image.png"
---
将键值对聚合为 JSON 对象，并按指定列分组。`JSON_GROUP_OBJECT()` 收集指定列中每个分组的所有键值对，并将它们合并为每个分组对应的单个 JSON 对象。

另请参阅：  
[[`SELECT`]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT JSON_GROUP_OBJECT(key_column, value_column) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## 参数 {#arguments}

__*key_column*__  
为 JSON 对象提供键的列。

__*value_column*__  
为 JSON 对象提供值的列。

__*grouping_column*__  
对结果进行分组所依据的列。

## 返回值 {#return-values}
返回一个 JSON 对象，包含指定列中所有聚合的键值对，并按 `grouping_column` 分组。

* * *

## 示例 {#examples}

### 按编程语言聚合仓库大小 {#aggregate-repository-sizes-by-programming-language}

```sql
-- Example: Group repository names with their corresponding sizes by programming language
SELECT
  language, 
  JSON_GROUP_OBJECT(name, size) AS language_index
FROM
  github.repos.repos
WHERE
  org = 'stackql'
  AND language IS NOT NULL
GROUP BY language;
```

此查询为每种编程语言返回一个 JSON 对象，其中每个对象以键值对的形式包含仓库名称及其对应的大小。以下是预期输出的示例：

```plaintext
|-------------|-----------------------------------------------------------------------------------|
| language    | language_index                                                                    |
|-------------|-----------------------------------------------------------------------------------|
| C           | {"go-sqlite3":35667,"data-encryption-spike":50746}                                |
|-------------|-----------------------------------------------------------------------------------|
| C++         | {"expt-any-sdk":2399,"isqlang":9}                                                 |
|-------------|-----------------------------------------------------------------------------------|
| Dockerfile  | {"stackql-compliance-checks-devel":8}                                             |
|-------------|-----------------------------------------------------------------------------------|
| Go          | {"stackql-provider-registry":115711, ...}                                         |
|-------------|-----------------------------------------------------------------------------------|
```

此输出可用于直观展示组织内不同编程语言的仓库大小分布，提供按语言分组的清晰、有条理的数据。
