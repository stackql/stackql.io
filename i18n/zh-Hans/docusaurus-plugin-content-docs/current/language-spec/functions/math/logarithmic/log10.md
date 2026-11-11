---
title: LOG10
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算一个数以 10 为底数的对数。
image: "/img/stackql-featured-image.png"
---
计算给定数以 10 为底数的对数，确定数字 10 必须取多少次幂才能得到该值。

另请参阅：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 语法 {#syntax}

```sql
SELECT LOG10(numeric_expression) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*numeric_expression*__  
要计算以 10 为底数的对数的正数值表达式。

## 返回值 {#return-values}
返回指定数值以 10 为底数的对数。

* * *

## 示例 {#examples}

### 计算 100 以 10 为底数的对数 {#calculate-the-base-10-logarithm-of-100}

```sql
SELECT LOG10(100) AS log_result;
```

此查询返回 `2`，因为 \(10^2 = 100\)。

### 计算 1000 以 10 为底数的对数 {#calculate-the-base-10-logarithm-of-1000}

```sql
SELECT LOG10(1000) AS log_result;
```

此查询计算 1000 以 10 为底数的对数，结果为 `3`，因为 \(10^3 = 1000\)。
