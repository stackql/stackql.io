---
title: COALESCE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 COALESCE 函式：從兩個或更多引數中傳回第一個非 NULL 的引數，通常用來以預設值取代缺少的值。"
image: "/img/stackql-featured-image.png"
---
從一系列兩個或更多引數中傳回第一個不是 `NULL` 的引數，可用來以預設值取代 `NULL` 值。  

> `COALESCE` 函式相當於 Oracle 的 `NVL` 函式或 MySQL 的 `IFNULL` 函式。

另請參閱：  
[[` SELECT `]](/language-spec/select) [[` IFNULL `]](/language-spec/functions/general/ifnull) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## 語法 {#syntax}

```sql
SELECT COALESCE(expression1, expression2 [, ..]) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*expression*__  
兩個或更多任何資料型別的運算式，其求值結果為 `NULL` 或非 `NULL` 值。

> 如果所有 *expression* 引數都是 `NULL`，`COALESCE` 函式會傳回 `NULL`。

## 傳回值 {#return-values}

傳回這一系列 *expression* 引數中第一個非 `NULL` 的值。

* * *

## 範例 {#examples}

### 以預設值取代 `NULL` 值 {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, coalesce(website, cors, 'not hosting shared resources') AS website_or_cors_config
FROM google.storage.buckets where project = 'stackql';
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_corefunc.html#coalesce](https://www.sqlite.org/lang_corefunc.html#coalesce)
