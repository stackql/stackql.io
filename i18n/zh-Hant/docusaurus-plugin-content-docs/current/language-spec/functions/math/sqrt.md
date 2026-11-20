---
title: SQRT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算數字的平方根。
image: "/img/stackql-featured-image.png"
---
計算非負數值運算式的平方根。

另請參閱：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 語法 {#syntax}

```sql
SELECT SQRT(numeric_expression) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*numeric_expression*__  
要計算平方根的非負數值運算式。

## 傳回值 {#return-values}
傳回指定非負數值的平方根。

* * *

## 範例 {#examples}

### 計算數字的平方根 {#calculate-the-square-root-of-a-number}

```sql
SELECT SQRT(9) AS square_root;
```
