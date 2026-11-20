---
title: SIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算以弧度表示之角度的正弦。
image: "/img/stackql-featured-image.png"
---
計算以弧度指定之角度的正弦。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT SIN(radian_value) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*radian_value*__  
數值運算式，以弧度指定要計算正弦的角度。

## 傳回值 {#return-values}
傳回指定角度的正弦。

* * *

## 範例 {#examples}

### 計算 π/2 弧度的正弦 {#calculate-the-sine-of-π2-radians}

```sql
SELECT SIN(PI() / 2) AS sine_value;
```

此查詢傳回 `1`，因為 π/2 弧度的正弦為 1。

### 計算 π 弧度的正弦 {#calculate-the-sine-of-π-radians}

```sql
SELECT SIN(PI()) AS sine_value;
```

此查詢計算 π 弧度的正弦，結果約為 `0`，因為 π 的正弦為 0。
