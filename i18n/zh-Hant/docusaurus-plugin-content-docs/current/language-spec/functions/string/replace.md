---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 REPLACE 函式：將字串中每個出現的子字串取代為另一個字串。"
image: "/img/stackql-featured-image.png"
---
傳回字串的副本，其中每個出現的子字串都會取代為另一個字串。

另請參閱：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 語法 {#syntax}

```sql
SELECT REPLACE(character_expression, pattern, replacement) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*character_expression*__  
您要執行取代的字串常值或字串資料行。

__*pattern*__  
要在原始字串中尋找的子字串。

> 如果 *pattern* 是空字串，或在 *character_expression* 中找不到，則會原封不動地傳回 *character_expression*。 

__*replacement*__  
取代字串。

> 如果 *replacement* 原本不是字串，則會在處理前轉換為 UTF-8 字串。

> 比較時使用 `BINARY` 定序。

## 傳回值 {#return-values}
傳回字串。

* * *

## 範例 {#examples}

### 取代資料行中的字串模式 {#replace-a-string-pattern-in-a-column}

```sql
SELECT replace(name, 'prd', 'production') AS replacement_string
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_corefunc.html#replace](https://www.sqlite.org/lang_corefunc.html#replace)。
