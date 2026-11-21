---
title: DEGREES
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 將角度從弧度轉換為度數。
image: "/img/stackql-featured-image.png"
---
將指定的角度從弧度轉換為度數。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT DEGREES(angle_in_radians) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*angle_in_radians*__  
以弧度表示角度的數值運算式。

## 傳回值 {#return-values}
傳回該角度對應的度數。

* * *

## 範例 {#examples}

### 將 π 弧度轉換為度數 {#convert-π-radians-to-degrees}

```sql
SELECT DEGREES(3.14159) AS degrees_value;
```

此查詢會傳回約 `180`，因為將 π 弧度（約 `3.14159`）轉換後約為 180 度。

### 將 π/2 弧度轉換為度數 {#convert-π2-radians-to-degrees}

```sql
SELECT DEGREES(1.5708) AS degrees_value;
```

此查詢將 π/2 弧度（約 `1.5708`）轉換為度數，結果約為 `90`，因為 π/2 弧度相當於 90 度角。
