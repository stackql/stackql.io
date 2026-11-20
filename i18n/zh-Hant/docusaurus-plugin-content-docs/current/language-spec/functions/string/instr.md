---
title: INSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 INSTR 函式：某字串在另一字串中第一次出現的位置（從 1 起算），找不到時為 0。"
image: "/img/stackql-featured-image.png"
---
傳回某個字串在另一個字串中第一次出現的字元位置；如果找不到要搜尋的字串，則傳回 0。

另請參閱：  
[[` SELECT `]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT INSTR(string, searchstring) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*string*__  
要在其中搜尋的來源字串。

__*searchstring*__  
要搜尋的子字串。

> 如果 *string* 和 *searchstring* 都是 BLOB，則 `instr(<string>, <searchstring>)` 會傳回 *searchstring* 第一次出現之前的位元組數加一；如果 *searchstring* 未出現在 *string* 中的任何位置，則傳回 0。如果兩個引數都不是 `NULL` 也都不是 BLOB，則兩者都會解譯為字串。 

## 傳回值 {#return-values}
傳回整數。

> 如果 *string* 或 *searchstring* 的值為 `NULL`，則結果為 `NULL`。

* * *

## 範例 {#examples}

### 在欄位中搜尋字串 {#search-a-field-for-a-string}

```sql
SELECT INSTR(name, 'worker') as charpos
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
returns 0 if the term 'worker' is not present in the 'name' column, otherwise returns the character position (starting from 1) of the first occurrence of the term 'worker' in the 'name' column
*/
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_corefunc.html#instr](https://www.sqlite.org/lang_corefunc.html#instr)。
