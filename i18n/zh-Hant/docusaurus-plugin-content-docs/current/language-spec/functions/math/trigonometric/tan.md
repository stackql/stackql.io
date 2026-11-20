---
title: TAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算以弧度表示之角度的正切。
image: "/img/stackql-featured-image.png"
---
計算以弧度指定之角度的正切。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT TAN(radian_value) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*radian_value*__  
數值運算式，以弧度指定要計算正切的角度。

## 傳回值 {#return-values}
傳回指定角度的正切。

* * *

## 範例 {#examples}

### 計算 π/4 弧度的正切 {#calculate-the-tangent-of-π4-radians}

```sql
SELECT TAN(PI() / 4) AS tangent_value;
```

此查詢傳回 `1`，因為 π/4 弧度的正切為 1。

### 計算 π 弧度的正切 {#calculate-the-tangent-of-π-radians}

```sql
SELECT TAN(PI()) AS tangent_value;
```

此查詢計算 π 弧度的正切，結果約為 `0`，因為 π 的正切非常接近 0。
