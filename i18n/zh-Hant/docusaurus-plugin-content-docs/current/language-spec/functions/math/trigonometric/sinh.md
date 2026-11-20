---
title: SINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算數字的雙曲正弦並傳回結果。
image: "/img/stackql-featured-image.png"
---
計算指定值的雙曲正弦。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT SINH(value) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*value*__  
要計算雙曲正弦的數值運算式。

## 傳回值 {#return-values}
傳回指定值的雙曲正弦。

* * *

## 範例 {#examples}

### 計算 1 的雙曲正弦 {#calculate-the-hyperbolic-sine-of-1}

```sql
SELECT SINH(1) AS hyperbolic_sine_value;
```

此查詢傳回約 `1.1752`，因為 1 的雙曲正弦約為 1.1752。

### 計算 0 的雙曲正弦 {#calculate-the-hyperbolic-sine-of-0}

```sql
SELECT SINH(0) AS hyperbolic_sine_value;
```

此查詢計算 0 的雙曲正弦，結果為 `0`，因為 0 的雙曲正弦正好是 0。
