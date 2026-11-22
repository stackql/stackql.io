---
title: JSON_GROUP_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 將鍵值對彙總為 JSON 物件。
image: "/img/stackql-featured-image.png"
---
將鍵值對彙總為 JSON 物件，並依指定的資料行分組。`JSON_GROUP_OBJECT()` 會收集指定資料行中每個群組的所有鍵值對，並為每個群組將其合併為單一 JSON 物件。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT JSON_GROUP_OBJECT(key_column, value_column) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## 引數 {#arguments}

__*key_column*__  
提供 JSON 物件之鍵的資料行。

__*value_column*__  
提供 JSON 物件之值的資料行。

__*grouping_column*__  
`GROUP BY` 資料行；它不是 `JSON_GROUP_OBJECT` 的引數，該函式恰好接受兩個引數。每個群組會傳回一個 JSON 物件。

## 傳回值 {#return-values}
傳回包含指定資料行中所有彙總鍵值對的 JSON 物件，並依 `grouping_column` 分組。

* * *

## 範例 {#examples}

### 依程式語言彙總存放庫大小 {#aggregate-repository-sizes-by-programming-language}

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

此查詢會為每種程式語言傳回一個 JSON 物件，每個物件都以鍵值對的形式包含存放庫名稱及其對應的大小。以下是預期輸出的範例：

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

此輸出有助於以視覺化方式呈現組織內不同程式語言的存放庫大小分佈，並提供依語言分組、清楚且有條理的資料。
