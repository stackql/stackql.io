---
title: LOG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算一个数在任意底数下的对数。
image: "/img/stackql-featured-image.png"
---
计算一个数在指定底数下的对数，与以 \(e\) 为底数的 LN 相比，这是对数函数更一般的形式。

另请参阅：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 语法 {#syntax}

```sql
SELECT LOG(base, numeric_expression) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*base*__  
对数的底数，是一个不等于 1 的正数值表达式。

__*numeric_expression*__  
要计算对数的正数值表达式。

## 返回值 {#return-values}
返回指定数值在指定底数下的对数。

* * *

## 示例 {#examples}

### 计算 1000 以 10 为底数的对数 {#calculate-the-logarithm-of-1000-with-base-10}

```sql
SELECT LOG(10, 1000) AS log_result;
```

此查询返回 `3`，因为 \(10^3 = 1000\)。

### 计算 16 以 2 为底数的对数 {#calculate-the-logarithm-of-16-with-base-2}

```sql
SELECT LOG(2, 16) AS log_result;
```
此查询计算 16 以 2 为底数的对数，结果为 `4`，因为 \(2^4 = 16\)。
