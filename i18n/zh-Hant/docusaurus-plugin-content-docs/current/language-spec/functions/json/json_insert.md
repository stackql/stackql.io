---
title: JSON_INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 將新值插入 JSON 文件，不覆寫現有資料。
image: "/img/stackql-featured-image.png"
---
在 JSON 文件的指定路徑插入新值，不覆寫現有的值。

另請參閱：  
[[`SELECT`]](/language-spec/select) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_SET `]](/language-spec/functions/json/json_set) [[` JSON_REPLACE `]](/language-spec/functions/json/json_replace)

* * * 

## 語法 {#syntax}

```sql
SELECT JSON_INSERT(json_column, path, new_value) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*json_column*__  
要修改的 JSON 資料行或運算式。

__*path*__  
要插入新值之位置的 JSON 路徑運算式。如果路徑已存在，該路徑上的原始值不會被取代。

__*new_value*__  
要插入 JSON 文件中指定路徑的值。

## 傳回值 {#return-values}
如果指定的路徑尚不存在，傳回已插入新值的 JSON 文件；否則傳回未變更的 JSON 文件。

:::note 鍵已存在時的行為

| 函式                                                         | 已存在時是否覆寫？           | 不存在時是否建立？        |
|--------------------------------------------------------------|------------------------------|---------------------------|
| [`json_insert()`](/language-spec/functions/json/json_insert) | 否                           | 是                        |
| [`json_replace()`](/language-spec/functions/json/json_replace) | 是                           | 否                        |
| [`json_set()`](/language-spec/functions/json/json_set)       | 是                           | 是                        |

:::

* * *

## 範例 {#examples}

### 將新的鍵值對插入 JSON 物件 {#insert-a-new-key-value-pair-into-a-json-object}

```sql
-- Example: Insert a new 'region' key with the value 'Europe' into a JSON object
SELECT JSON_INSERT('{"name":"ResourceOne","type":"Storage"}', '$.region', 'Europe') as updated_object;

/* expected output...
|-----------------------------------------------------------|
|                      updated_object                       |
|-----------------------------------------------------------|
| {"name":"ResourceOne","type":"Storage","region":"Europe"} |
|-----------------------------------------------------------|
*/
```

如果 'region' 鍵尚不存在，此查詢會將名為 'region'、值為 'Europe' 的新鍵插入 JSON 文件。如果 'region' 已是文件中的鍵，則傳回未變更的原始文件。
