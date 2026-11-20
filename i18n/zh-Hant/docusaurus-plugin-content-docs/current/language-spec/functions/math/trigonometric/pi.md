---
title: PI
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 取得數學常數 π（pi）。
image: "/img/stackql-featured-image.png"
---
傳回數學常數 π（pi），即圓周長與直徑的比值。

另請參閱：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 語法 {#syntax}

```sql
SELECT PI() AS pi_value;
```

## 引數 {#arguments}

此函式不需要任何引數。

## 傳回值 {#return-values}
傳回常數 π，約等於 3.14159。

* * *

## 範例 {#examples}

### 取得 π 的值 {#retrieve-the-value-of-π}

```sql
SELECT PI() AS pi_value;
```

此查詢會輸出 π 的值，約為 `3.14159`，可用於各種涉及圓和其他幾何函式的數學計算。
