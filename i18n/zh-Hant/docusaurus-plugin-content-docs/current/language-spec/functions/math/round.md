---
title: ROUND
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 ROUND 函式：將數值引數四捨五入至指定的小數位數，預設為零。"
image: "/img/stackql-featured-image.png"
---
將數值引數四捨五入。  

另請參閱：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 語法 {#syntax}

```sql
SELECT ROUND(numeric_expression [, precision]) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*numeric_expression*__  
屬於精確數值或近似數值資料型別類別的運算式。

__*precision*__  
將 *numeric_expression* 四捨五入後保留的小數點右側位數。

> 如果省略 *precision* 引數，則視為 0。

## 傳回值 {#return-values}
傳回數值。

* * *

## 範例 {#examples}

### 將浮點數四捨五入至小數點後兩位 {#round-a-floating-point-number-to-two-decimal-places}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated), 2) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
如需詳細資訊，請參閱 [https://www.sqlite.org/lang_corefunc.html#round](https://www.sqlite.org/lang_corefunc.html#round)。
