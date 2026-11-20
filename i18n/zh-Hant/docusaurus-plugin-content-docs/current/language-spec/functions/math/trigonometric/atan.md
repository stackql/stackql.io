---
title: ATAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算數字的反正切，並以弧度傳回角度。
image: "/img/stackql-featured-image.png"
---
計算數字的反正切，也就是正切值等於指定數字的角度（以弧度表示）。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT ATAN(value) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*value*__  
要計算反正切的數值運算式。

## 傳回值 {#return-values}
以弧度傳回指定值的反正切。

* * *

## 範例 {#examples}

### 計算 1 的反正切 {#calculate-the-arc-tangent-of-1}

```sql
SELECT ATAN(1) AS arc_tangent_value;
```

此查詢傳回 π/4，約為 `0.785`，因為 1 的反正切為 π/4 弧度。

### 計算 0 的反正切 {#calculate-the-arc-tangent-of-0}

```sql
SELECT ATAN(0) AS arc_tangent_value;
```

此查詢計算 0 的反正切，結果為 `0`，因為 0 弧度的正切為 0。
