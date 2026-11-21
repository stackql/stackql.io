---
title: ACOS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算數字的反餘弦，並以弧度傳回角度。
image: "/img/stackql-featured-image.png"
---
計算數字的反餘弦，也就是餘弦值等於指定數字的角度（以弧度表示）。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT ACOS(value) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*value*__  
要計算反餘弦的數值運算式，範圍為 -1 到 1（含）。

## 傳回值 {#return-values}
以弧度傳回指定值的反餘弦。

* * *

## 範例 {#examples}

### 計算 0.5 的反餘弦 {#calculate-the-arc-cosine-of-05}

```sql
SELECT ACOS(0.5) AS arc_cosine_value;
```

此查詢傳回約 `1.047`，因為 0.5 的反餘弦為 π/3 弧度（60 度），約為 1.047 弧度。

### 計算 1 的反餘弦 {#calculate-the-arc-cosine-of-1}

```sql
SELECT ACOS(1) AS arc_cosine_value;
```

此查詢計算 1 的反餘弦，結果為 `0`，因為 0 弧度的餘弦為 1。
