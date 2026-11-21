---
title: ATAN2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算兩個數字（y/x）的反正切，並以弧度傳回角度。
image: "/img/stackql-featured-image.png"
---
計算兩個數字的反正切，具體而言是正切值等於兩個指定數字之比（y 除以 x）的角度（以弧度表示），並正確處理輸入值的正負號以判定正確的象限。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT ATAN2(y, x) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*y*__  
點的 y 座標（分子）。

__*x*__  
點的 x 座標（分母）。

## 傳回值 {#return-values}
以弧度傳回 y/x 的反正切，並考量角度所在的正確象限。

* * *

## 範例 {#examples}

### 計算點 (1,1) 的反正切 {#calculate-the-arc-tangent-of-the-point-11}

```sql
SELECT ATAN2(1, 1) AS arc_tangent_value;
```

此查詢傳回 π/4，約為 `0.785`，因為點 (1,1) 的反正切對應第一象限中 π/4 弧度的角度。

### 計算點 (-10, 10) 的反正切 {#calculate-the-arc-tangent-of-the-point--10-10}

```sql
SELECT ATAN2(10, -10) AS arc_tangent_value;
```

此查詢計算點 (-10, 10) 的角度，其中 y = 10、x = -10，結果為 3π/4，約為 `2.356`：此角度的正切為 -1，且該點位於第二象限。
