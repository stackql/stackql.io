---
title: JSON_EACH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 JSON_EACH 資料表值函式：將 JSON 陣列或物件展開為每個元素或成員一個資料列，以聯結並篩選巢狀 API 回應。"
image: "/img/stackql-featured-image.png"
---
資料表值函式，傳回由每個陣列元素或物件成員各一個資料列所組成的資料表。

另請參閱：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ 資料型別 ]](/language-spec/data-types)

* * * 

:::tip 

使用 [**DESCRIBE**](/language-spec/describe) 陳述式找出可搭配 `json_each` 函式使用的 `array` 資料型別。

:::

:::note 

如果您搭配 `postgres` SQL 後端使用 stackql，`json_each` 函式會實作為 `json_array_elements_text` 函式

:::

## 語法 {#syntax}

```sql
SELECT <alias>.<projectedField>, ... , json_each.value  
FROM <multipartIdentifier> <alias>, JSON_EACH(json_array_or_object, json_path);
```
## 引數 {#arguments}

__*json_array_or_object*__  
JSON 陣列或物件。

__*json_path*__  
指向 *json_array_or_object* 內所含之陣列或物件的 JSON 路徑。請參閱 [jsonpath.com](https://jsonpath.com/)。

> 如果 *json_array_or_object* 中不存在 *json_path*，函式不會傳回任何資料列。

> 如果 *json_array_or_object* 不是格式正確的 JSON，`json_each()` 函式會擲回錯誤。

## 傳回值 {#return-values}
由每個陣列元素或物件成員各一個資料列所組成的資料表。

* * *

## 範例 {#examples}

### 將字串陣列展開為與相關欄位聯結的資料表 {#unnest-an-array-of-strings-into-a-table-joined-with-associated-fields}

供參考：  

```sql
describe google.cloudresourcemanager.projects_iam_policies;
```

傳回：

```
|-----------|--------|                                                                                                                                                                                                     
|   name    |  type  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| condition | object |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| members   | array  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| role      | string |                                                                                                                                                                                                     
|-----------|--------|  
```

下列 `stackql` 查詢使用 `json_each` 函式，將 `members` 陣列展開為與 `role` 及選擇性的 `condition` 欄位聯結的資料表。  

```sql
select 
iam.role, 
SPLIT_PART(json_each.value, ':', 2) as member 
from google.cloudresourcemanager.projects_iam_policies iam, json_each(members) 
where projectsId = 'stackql';
```

會傳回：

```
|--------------------------------------|-------------------------------------------------------|
|                 role                 |                        member                         |
|--------------------------------------|-------------------------------------------------------|
| roles/cloudbuild.builds.builder      | 1234567890@cloudbuild.gserviceaccount.com             |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890-compute@developer.gserviceaccount.com      |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890@cloudservices.gserviceaccount.com          |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | stackql@appspot.gserviceaccount.com                   |
|--------------------------------------|-------------------------------------------------------|
| ...                                  | ...                                                   |
|--------------------------------------|-------------------------------------------------------|
```

如需詳細資訊，請參閱 [https://www.sqlite.org/json1.html#jeach](https://www.sqlite.org/json1.html#jeach)
