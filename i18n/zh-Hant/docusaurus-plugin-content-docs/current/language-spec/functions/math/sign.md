---
title: SIGN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 SIGN 函式：依數值引數為負數、零或正數，傳回 -1、0 或 1。"
image: "/img/stackql-featured-image.png"
---
如果輸入值為負數，傳回 -1；如果輸入值為 0，傳回 0；如果輸入值為正數，傳回 1。  

另請參閱：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 語法 {#syntax}

```sql
SELECT SIGN(numeric_expression) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*numeric_expression*__  
屬於精確數值或近似數值資料型別類別的運算式。

> 如果 *numeric_expression* 為 `NULL`，或是無法無損轉換為數字的 `string` 或 BLOB，函式會傳回 `NULL`。

## 傳回值 {#return-values}

傳回 `-1`、`0` 或 `1`。

* * *

## 範例 {#examples}

### 判斷數字的正負號 {#determine-the-sign-of-a-number}

```sql
SELECT name, sign(strftime('%s', creationTimestamp)-strftime('%s','now')) as sign
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_corefunc.html#sign](https://www.sqlite.org/lang_corefunc.html#sign)。
