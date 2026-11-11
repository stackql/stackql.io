---
title: COSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算一个数的双曲余弦并返回结果。
image: "/img/stackql-featured-image.png"
---
计算指定值的双曲余弦。

另请参阅：  
[[`SELECT`]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT COSH(value) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*value*__  
要计算双曲余弦的数值表达式。

## 返回值 {#return-values}
返回指定值的双曲余弦。

* * *

## 示例 {#examples}

### 计算 1 的双曲余弦 {#calculate-the-hyperbolic-cosine-of-1}

```sql
SELECT COSH(1) AS hyperbolic_cosine_value;
```

此查询返回约 `1.5431`，因为 1 的双曲余弦约为 1.5431。

### 计算 0 的双曲余弦 {#calculate-the-hyperbolic-cosine-of-0}

```sql
SELECT COSH(0) AS hyperbolic_cosine_value;
```

此查询计算 0 的双曲余弦，结果为 `1`，因为 0 的双曲余弦恰好为 1。
