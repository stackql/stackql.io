---
title: ASIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算數字的反正弦，並以弧度傳回角度。
image: "/img/stackql-featured-image.png"
---
計算數字的反正弦，也就是正弦值等於指定數字的角度（以弧度表示）。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT ASIN(value) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*value*__  
要計算反正弦的數值運算式，範圍為 -1 到 1（含）。

## 傳回值 {#return-values}
以弧度傳回指定值的反正弦。

* * *

## 範例 {#examples}

### 計算 0.5 的反正弦 {#calculate-the-arc-sine-of-05}

```sql
SELECT ASIN(0.5) AS arc_sine_value;
```

此查詢傳回 π/6，約為 `0.5236`，因為 0.5 的反正弦對應 π/6 弧度的角度。

### 計算 -1 的反正弦 {#calculate-the-arc-sine-of--1}

```sql
SELECT ASIN(-1) AS arc_sine_value;
```

此查詢計算 -1 的反正弦，結果為 -π/2，約為 `-1.5708`，因為 -π/2 的正弦為 -1。
