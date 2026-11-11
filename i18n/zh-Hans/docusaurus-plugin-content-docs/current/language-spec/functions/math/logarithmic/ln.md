---
title: LN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算一个数的自然对数。
image: "/img/stackql-featured-image.png"
---
计算给定数的自然对数（以 \(e\) 为底数的对数）。

另请参阅：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 语法 {#syntax}

```sql
SELECT LN(numeric_expression) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*numeric_expression*__  
要计算自然对数的正数值表达式。

## 返回值 {#return-values}
返回指定正数值的自然对数。

* * *

## 示例 {#examples}

### 计算 2.71828（约等于 \(e\)）的自然对数 {#calculate-the-natural-logarithm-of-271828-approximately-e}

```sql
SELECT LN(2.71828) AS natural_log;
```

此查询返回一个接近 `1` 的值，因为 \(e\)（约等于 2.71828）的自然对数为 1。

### 计算一个较大数的自然对数 {#calculate-the-natural-logarithm-of-a-larger-number}

```sql
SELECT LN(10) AS natural_log;
```

此查询计算 10 的自然对数，给出 10 以 \(e\) 为底数的对数值。
