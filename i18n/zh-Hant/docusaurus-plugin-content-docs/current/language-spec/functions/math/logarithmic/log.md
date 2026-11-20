---
title: LOG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算數字以任意底數為底的對數。
image: "/img/stackql-featured-image.png"
---
計算數字以指定底數為底的對數，相較於以 e 為底的 LN，這是更通用的對數函式形式。

另請參閱：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 語法 {#syntax}

```sql
SELECT LOG(base, numeric_expression) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*base*__  
對數的底數，為不等於 1 的正數值運算式。

__*numeric_expression*__  
要計算對數的正數值運算式。

## 傳回值 {#return-values}
傳回指定數值以指定底數為底的對數。

* * *

## 範例 {#examples}

### 計算 1000 以 10 為底的對數 {#calculate-the-logarithm-of-1000-with-base-10}

```sql
SELECT LOG(10, 1000) AS log_result;
```

此查詢傳回 `3`，因為 10^3 = 1000。

### 計算 16 以 2 為底的對數 {#calculate-the-logarithm-of-16-with-base-2}

```sql
SELECT LOG(2, 16) AS log_result;
```
此查詢計算 16 以 2 為底的對數，結果為 `4`，因為 2^4 = 16。
