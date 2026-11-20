---
title: JSON_TYPE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 JSON_TYPE 函式：JSON 值最外層元素或其中某個路徑的型別（object、array、text、integer、real、true、false 或 null）。"
image: "/img/stackql-featured-image.png"
---
傳回 JSON 值最外層元素的資料型別，或選用路徑上元素的資料型別。  

另請參閱：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ 資料型別 ]](/language-spec/data-types)

* * * 

:::tip 

使用 [**DESCRIBE**](/language-spec/describe) 陳述式找出可搭配 StackQL JSON 函式使用的 `array` 或 `object` 資料型別。

:::

:::note 

如果您搭配 `postgres` SQL 後端使用 StackQL，請改用 `json_typeof` 函式。

:::

## 語法 {#syntax}

```sql
SELECT JSON_TYPE(json_array_or_object, json_path) 
FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*json_array_or_object*__  
JSON 陣列或物件。

__*json_path*__  
指向 *json_array_or_object* 中所含陣列或物件的 JSON 路徑。請參閱 [jsonpath.com](https://jsonpath.com/)。

> 如果 *json_path* 不存在於 *json_array_or_object* 中，函式會傳回 `NULL`。

> 如果任何引數的格式不正確，`json_type()` 函式會擲回錯誤。

## 傳回值 {#return-values}
表示所判定資料型別的字串。

> 資料型別包括 `null`、`true`、`false`、`integer`、`real`、`text`、`array` 或 `object`。

* * *

## 範例 {#examples}

### 傳回 JSON 物件或陣列中巢狀欄位的資料型別 {#return-the-datatype-of-a-field-nested-within-a-json-object-or-array}

```sql
SELECT json_type(disks, '$[0].licenses[0]') as datatype
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns text */
```

如需詳細資訊，請參閱 [https://www.sqlite.org/json1.html#jtype](https://www.sqlite.org/json1.html#jtype)。
