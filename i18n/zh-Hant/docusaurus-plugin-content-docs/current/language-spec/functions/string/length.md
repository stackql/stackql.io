---
title: LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 LENGTH 函式：字串中的字元數，或 BLOB 中的位元組數。"
image: "/img/stackql-featured-image.png"
---
傳回字串輸入運算式中第一個 NUL 字元（`char(0)`）之前的字元數，或 BLOB 運算式中的位元組數。  

另請參閱：  
[[` SELECT `]](/language-spec/select) 

* * * 

:::info

若要取得 JSON 陣列的長度，請使用 [**JSON_ARRAY_LENGTH**](/language-spec/functions/json/json_array_length) 函式。

:::

## 語法 {#syntax}

```sql
SELECT LENGTH(expression) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*expression*__  
字串或 BLOB。

> 如果 *expression* 為 `NULL`，則傳回 `NULL`。

> 如果 *expression* 是數值，則傳回 *expression* 字串表示法的長度。

## 傳回值 {#return-values}
傳回整數。

* * *

## 範例 {#examples}

### 傳回字串資料行的長度 {#return-the-length-of-a-string-column}

```sql
SELECT name, length(name) AS len
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_corefunc.html#length](https://www.sqlite.org/lang_corefunc.html#length)。
