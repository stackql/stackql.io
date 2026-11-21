---
title: JSON_SET
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 更新或插入 JSON 文件中的值。
image: "/img/stackql-featured-image.png"
---
在 JSON 文件的指定路徑更新現有值或插入新值。

另請參閱：  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_INSERT `]](/language-spec/functions/json/json_insert) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## 語法 {#syntax}

```sql
SELECT JSON_SET(json_column, path, value_to_set) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*json_column*__  
要修改的 JSON 資料行或運算式。

__*path*__  
指出要在何處設定值的 JSON 路徑運算式。如果路徑存在，則更新該值；如果路徑不存在，則新增一個鍵值對。

__*value_to_set*__  
要在 JSON 文件中指定路徑設定的值。

## 傳回值 {#return-values}
傳回已在指定路徑設定值的 JSON 文件。如果路徑存在，則更新現有值；否則新增一個鍵值對。

:::note 鍵已存在時的行為

| 函式                                                         | 已存在時是否覆寫？           | 不存在時是否建立？        |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | 否                           | 是                        |
| [`json_replace()`](/language-spec/functions/json/json_replace) | 是                           | 否                        |
| [`json_set()`](/language-spec/functions/json/json_set)       | 是                           | 是                        |

:::

* * *

## 範例 {#examples}

### 更新 JSON 物件中的現有值 {#update-an-existing-value-in-a-json-object}

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

如果 'status' 鍵存在，此查詢會將 JSON 文件中的 'status' 鍵更新為 'active'。如果文件中不存在 'status'，則會以值 'active' 新增該鍵。

### 將新的鍵值對新增至 JSON 物件 {#add-a-new-key-value-pair-to-a-json-object}

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

如果 'lastChecked' 尚不存在，此查詢會將值為 '2024-04-29' 的新鍵 'lastChecked' 新增至 JSON 文件。如果已存在，則將現有值更新為 '2024-04-29'。
