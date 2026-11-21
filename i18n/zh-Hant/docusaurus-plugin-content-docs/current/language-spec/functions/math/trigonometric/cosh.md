---
title: COSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算數字的雙曲餘弦並傳回結果。
image: "/img/stackql-featured-image.png"
---
計算指定值的雙曲餘弦。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT COSH(value) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*value*__  
要計算雙曲餘弦的數值運算式。

## 傳回值 {#return-values}
傳回指定值的雙曲餘弦。

* * *

## 範例 {#examples}

### 計算 1 的雙曲餘弦 {#calculate-the-hyperbolic-cosine-of-1}

```sql
SELECT COSH(1) AS hyperbolic_cosine_value;
```

此查詢傳回約 `1.5431`，因為 1 的雙曲餘弦約為 1.5431。

### 計算 0 的雙曲餘弦 {#calculate-the-hyperbolic-cosine-of-0}

```sql
SELECT COSH(0) AS hyperbolic_cosine_value;
```

此查詢計算 0 的雙曲餘弦，結果為 `1`，因為 0 的雙曲餘弦正好是 1。
