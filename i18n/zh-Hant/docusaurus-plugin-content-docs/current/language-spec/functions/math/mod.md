---
title: MOD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算一個數除以另一個數的餘數。
image: "/img/stackql-featured-image.png"
---
計算 *x* 除以 *y* 的餘數，並傳回模數。這類似於使用 '%' 運算子，但也適用於非整數引數。

另請參閱：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 語法 {#syntax}

```sql
SELECT MOD(x, y) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*x*__  
被除數，為數值運算式。

__*y*__  
除數，同樣為數值運算式。

## 傳回值 {#return-values}
傳回 *x* 除以 *y* 的餘數。

* * *

## 範例 {#examples}

### 計算 10 除以 3 的模數 {#calculate-the-modulus-of-10-divided-by-3}

```sql
SELECT MOD(10, 3) AS remainder;
```
