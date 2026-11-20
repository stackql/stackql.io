---
title: TANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算數字的雙曲正切並傳回結果。
image: "/img/stackql-featured-image.png"
---
計算指定值的雙曲正切。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT TANH(value) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*value*__  
要計算雙曲正切的數值運算式。

## 傳回值 {#return-values}
傳回指定值的雙曲正切。

* * *

## 範例 {#examples}

### 計算 1 的雙曲正切 {#calculate-the-hyperbolic-tangent-of-1}

```sql
SELECT TANH(1) AS hyperbolic_tangent_value;
```

此查詢傳回約 `0.7616`，因為 1 的雙曲正切約為 0.7616。

### 計算 0 的雙曲正切 {#calculate-the-hyperbolic-tangent-of-0}

```sql
SELECT TANH(0) AS hyperbolic_tangent_value;
```

此查詢計算 0 的雙曲正切，結果為 `0`，因為 0 的雙曲正切正好是 0。
