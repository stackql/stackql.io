---
title: ATANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算數字的雙曲反正切（反雙曲正切）。
image: "/img/stackql-featured-image.png"
---
計算數字的雙曲反正切（反雙曲正切），也就是雙曲正切等於指定數字的值。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT ATANH(value) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*value*__  
要計算雙曲反正切的數值運算式，範圍介於 -1 到 1 之間（不含兩端）。

## 傳回值 {#return-values}
傳回指定值的雙曲反正切。

* * *

## 範例 {#examples}

### 計算 0.5 的雙曲反正切 {#calculate-the-hyperbolic-arc-tangent-of-05}

```sql
SELECT ATANH(0.5) AS hyperbolic_arc_tangent_value;
```

此查詢傳回約 `0.5493`，因為 0.5 的雙曲反正切約為 0.5493。

### 計算 -0.5 的雙曲反正切 {#calculate-the-hyperbolic-arc-tangent-of--05}

```sql
SELECT ATANH(-0.5) AS hyperbolic_arc_tangent_value;
```

此查詢計算 -0.5 的雙曲反正切，結果約為 `-0.5493`，因為這是雙曲正切為 -0.5 的值。
