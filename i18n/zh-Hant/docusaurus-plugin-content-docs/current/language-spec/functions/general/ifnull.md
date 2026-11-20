---
title: IFNULL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 IFNULL 函式：從恰好兩個引數中傳回第一個非 NULL 的引數，是 COALESCE 的雙引數形式。"
image: "/img/stackql-featured-image.png"
---
接受恰好兩個引數並傳回第一個非 `NULL` 的引數，可用來以預設值取代 `NULL` 值。  

> `IFNULL` 函式相當於只有兩個引數的 [`COALESCE`](/language-spec/functions/general/coalesce) 函式，[`COALESCE`](/language-spec/functions/general/coalesce) 函式可以有超過兩個引數。

另請參閱：  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## 語法 {#syntax}

```sql
SELECT IFNULL(expression1, expression2) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*expression*__  
兩個任何資料型別的運算式，其求值結果為 `NULL` 或非 `NULL` 值。

> 如果兩個 *expression* 引數都是 `NULL`，`IFNULL` 函式會傳回 `NULL`。


## 傳回值 {#return-values}

傳回兩個 *expression* 引數中第一個非 `NULL` 的值。 

* * *

## 範例 {#examples}

### 以預設值取代 `NULL` 值 {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, ifnull(website, 'Not hosting a website') AS website 
FROM google.storage.buckets where project = 'stackql';
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_corefunc.html#ifnull](https://www.sqlite.org/lang_corefunc.html#ifnull)
