---
title: COS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算以弧度表示之角度的餘弦。
image: "/img/stackql-featured-image.png"
---
計算以弧度指定之角度的餘弦。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT COS(radian_value) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*radian_value*__  
數值運算式，以弧度指定要計算餘弦的角度。

## 傳回值 {#return-values}
傳回指定角度的餘弦。

* * *

## 範例 {#examples}

### 計算 π/3 弧度的餘弦 {#calculate-the-cosine-of-π3-radians}

```sql
SELECT COS(PI() / 3) AS cosine_value;
```

此查詢傳回 `0.5000000000000001`，即 π/3 弧度餘弦值的浮點數近似值，該餘弦值為 0.5。

### 計算 π 弧度的餘弦 {#calculate-the-cosine-of-π-radians}

```sql
SELECT COS(PI()) AS cosine_value;
```

此查詢計算 π 弧度的餘弦，結果為 `-1`，因為 π 的餘弦為 -1。
