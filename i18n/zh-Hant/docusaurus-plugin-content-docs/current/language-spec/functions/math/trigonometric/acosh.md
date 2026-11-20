---
title: ACOSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: 在 StackQL 中使用 SQL 計算數字的反雙曲餘弦。
image: "/img/stackql-featured-image.png"
---
計算數字的反雙曲餘弦，也就是雙曲餘弦值等於指定數字的非負值。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT ACOSH(value) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*value*__  
要計算反雙曲餘弦的數值運算式，必須大於或等於 1。

## 傳回值 {#return-values}
傳回指定值的反雙曲餘弦。

* * *

## 範例 {#examples}

### 計算 1 的反雙曲餘弦 {#calculate-the-hyperbolic-arc-cosine-of-1}

```sql
SELECT ACOSH(1) AS hyperbolic_arc_cosine_value;
```

此查詢傳回 `0`，因為 1 的反雙曲餘弦為 0。

### 計算 10 的反雙曲餘弦 {#calculate-the-hyperbolic-arc-cosine-of-10}

```sql
SELECT ACOSH(10) AS hyperbolic_arc_cosine_value;
```

此查詢計算 10 的反雙曲餘弦，結果約為 `2.9932`，因為這是雙曲餘弦值等於 10 的值。
