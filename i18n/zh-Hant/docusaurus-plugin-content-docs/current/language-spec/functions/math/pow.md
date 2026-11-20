---
title: POW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算一個數的另一個數次方。
image: "/img/stackql-featured-image.png"
---
計算 *x* 的 *y* 次方。

另請參閱：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 語法 {#syntax}

```sql
SELECT POW(x, y) FROM <multipartIdentifier>;
-- or using the equivalent
SELECT POWER(x, y) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*x*__  
底數，為數值運算式。

__*y*__  
底數 *x* 要乘方的指數，同樣為數值運算式。

## 傳回值 {#return-values}
傳回 *x* 的 *y* 次方結果。

* * *

## 範例 {#examples}

### 計算 x 的 y 次方 {#calculate-x-raised-to-the-power-y}


```sql
SELECT POW(2, 3) AS result;
```
