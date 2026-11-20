---
title: LOG2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算數字以 2 為底的對數。
image: "/img/stackql-featured-image.png"
---
計算指定數字以 2 為底的對數，傳回 2 必須取幾次方才能得到該值。

另請參閱：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 語法 {#syntax}

```sql
SELECT LOG2(numeric_expression) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*numeric_expression*__  
要計算以 2 為底之對數的正數值運算式。

## 傳回值 {#return-values}
傳回指定數值以 2 為底的對數。

* * *

## 範例 {#examples}

### 計算 16 以 2 為底的對數 {#calculate-the-base-2-logarithm-of-16}

```sql
SELECT LOG2(16) AS log_result;
```

此查詢傳回 `4`，因為 2^4 = 16。

### 計算 32 以 2 為底的對數 {#calculate-the-base-2-logarithm-of-32}

```sql
SELECT LOG2(32) AS log_result;
```

此查詢計算 32 以 2 為底的對數，結果為 `5`，因為 2^5 = 32。
