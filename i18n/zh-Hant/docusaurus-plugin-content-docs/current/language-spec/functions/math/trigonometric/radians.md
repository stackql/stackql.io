---
title: RADIANS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 將角度從度數轉換為弧度。
image: "/img/stackql-featured-image.png"
---
將指定的角度從度數轉換為弧度。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT RADIANS(angle_in_degrees) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*angle_in_degrees*__  
以度數表示角度的數值運算式。

## 傳回值 {#return-values}
傳回該角度對應的弧度。

* * *

## 範例 {#examples}

### 將 180 度轉換為弧度 {#convert-180-degrees-to-radians}

```sql
SELECT RADIANS(180) AS radians_value;
```

此查詢會傳回 π（約 `3.14159`），因為將 180 度轉換為弧度的結果是 π 弧度。

### 將 90 度轉換為弧度 {#convert-90-degrees-to-radians}

```sql
SELECT RADIANS(90) AS radians_value;
```

此查詢將 90 度轉換為弧度，結果為 π/2，即約 `1.5708`，因為 90 度是 180 度的一半。
