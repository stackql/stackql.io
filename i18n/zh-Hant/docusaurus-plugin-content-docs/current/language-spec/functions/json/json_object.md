---
title: JSON_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 JSON_OBJECT 函式：以鍵和值引數建立 JSON 物件，例如用來建構 INSERT 和 UPDATE 的要求本文。"
image: "/img/stackql-featured-image.png"
---
傳回以所提供引數建構的物件。  

另請參閱：  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ 資料型別 ]](/language-spec/data-types)

* * * 

:::tip 

使用 [**DESCRIBE**](/language-spec/describe) 陳述式找出可搭配 StackQL JSON 函式使用的 `array` 或 `object` 資料型別。

:::

:::note 

如果您搭配 `postgres` SQL 後端使用 stackql，請以 `jsonb_build_object` 取代 `json_object`：StackQL 會將 `json_object` 原封不動傳給 PostgreSQL，而 PostgreSQL 的同名函式是以文字陣列接收鍵和值。

:::

## 語法 {#syntax}

```sql
SELECT JSON_OBJECT(key1, value1, key2, value2, ...) 
FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*key1, key2, ...*__  
JSON 物件的標籤。

__*value1, value2, ...*__  
JSON 物件中與各標籤對應的值。

## 傳回值 {#return-values}
表示格式正確之 JSON 物件的字串。

* * *

## 範例 {#examples}

### 從資料行建立 JSON 物件陣列 {#build-an-array-of-json-objects-from-a-column}

```sql
SELECT JSON_GROUP_ARRAY(json_object('instance', selfLink)) as instance_links
FROM google.compute.instances 
WHERE project = 'stackql-k8s-the-hard-way-demo'
AND zone = 'australia-southeast1-a'
AND name like '%-dev-controller-%';
/* returns a data structure required for a targetPool in GCP */
```

如需詳細資訊，請參閱 [https://sqlite.org/json1.html#jobj](https://sqlite.org/json1.html#jobj)。
