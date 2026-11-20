---
title: LOG10
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算數字以 10 為底的對數。
image: "/img/stackql-featured-image.png"
---
計算指定數字以 10 為底的對數，也就是求出 10 必須取幾次方才能得到該值。

另請參閱：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 語法 {#syntax}

```sql
SELECT LOG10(numeric_expression) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*numeric_expression*__  
要計算以 10 為底之對數的正數值運算式。

## 傳回值 {#return-values}
傳回指定數值以 10 為底的對數。

* * *

## 範例 {#examples}

### 計算 100 以 10 為底的對數 {#calculate-the-base-10-logarithm-of-100}

```sql
SELECT LOG10(100) AS log_result;
```

此查詢傳回 `2`，因為 10^2 = 100。

### 計算 1000 以 10 為底的對數 {#calculate-the-base-10-logarithm-of-1000}

```sql
SELECT LOG10(1000) AS log_result;
```

此查詢計算 1000 以 10 為底的對數，結果為 `3`，因為 10^3 = 1000。
