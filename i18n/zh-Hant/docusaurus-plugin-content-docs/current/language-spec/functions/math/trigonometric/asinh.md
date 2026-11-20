---
title: ASINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算數字的反雙曲正弦。
image: "/img/stackql-featured-image.png"
---
計算數字的反雙曲正弦，也就是雙曲正弦值等於指定數字的值。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT ASINH(value) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*value*__  
要計算反雙曲正弦的數值運算式。

## 傳回值 {#return-values}
傳回指定值的反雙曲正弦。

* * *

## 範例 {#examples}

### 計算 0.5 的反雙曲正弦 {#calculate-the-hyperbolic-arc-sine-of-05}

```sql
SELECT ASINH(0.5) AS hyperbolic_arc_sine_value;
```

此查詢傳回約 `0.4812`，因為 0.5 的反雙曲正弦約為 0.4812。

### 計算 -1 的反雙曲正弦 {#calculate-the-hyperbolic-arc-sine-of--1}

```sql
SELECT ASINH(-1) AS hyperbolic_arc_sine_value;
```

此查詢計算 -1 的反雙曲正弦，結果約為 `-0.8814`，因為這是雙曲正弦值等於 -1 的值。
