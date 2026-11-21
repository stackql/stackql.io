---
title: LN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算數字的自然對數。
image: "/img/stackql-featured-image.png"
---
計算指定數字的自然對數（以 e 為底的對數）。

另請參閱：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 語法 {#syntax}

```sql
SELECT LN(numeric_expression) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*numeric_expression*__  
要計算自然對數的正數值運算式。

## 傳回值 {#return-values}
傳回指定正數值的自然對數。

* * *

## 範例 {#examples}

### 計算 2.71828（約為 e）的自然對數 {#calculate-the-natural-logarithm-of-271828-approximately-e}

```sql
SELECT LN(2.71828) AS natural_log;
```

此查詢傳回接近 `1` 的值，因為 e（約為 2.71828）的自然對數為 1。

### 計算較大數字的自然對數 {#calculate-the-natural-logarithm-of-a-larger-number}

```sql
SELECT LN(10) AS natural_log;
```

此查詢計算 10 的自然對數，也就是 10 以 e 為底的對數值。
