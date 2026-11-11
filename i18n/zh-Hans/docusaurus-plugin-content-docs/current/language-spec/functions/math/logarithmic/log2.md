---
title: LOG2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算一个数以 2 为底数的对数。
image: "/img/stackql-featured-image.png"
---
计算给定数以 2 为底数的对数，返回数字 2 得到该值所需的幂次。

另请参阅：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 语法 {#syntax}

```sql
SELECT LOG2(numeric_expression) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*numeric_expression*__  
要计算以 2 为底数的对数的正数值表达式。

## 返回值 {#return-values}
返回指定数值以 2 为底数的对数。

* * *

## 示例 {#examples}

### 计算 16 以 2 为底数的对数 {#calculate-the-base-2-logarithm-of-16}

```sql
SELECT LOG2(16) AS log_result;
```

此查询返回 `4`，因为 \(2^4 = 16\)。

### 计算 32 以 2 为底数的对数 {#calculate-the-base-2-logarithm-of-32}

```sql
SELECT LOG2(32) AS log_result;
```

此查询计算 32 以 2 为底数的对数，结果为 `5`，因为 \(2^5 = 32\)。
