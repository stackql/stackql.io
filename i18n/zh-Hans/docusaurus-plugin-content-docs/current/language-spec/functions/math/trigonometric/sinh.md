---
title: SINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算一个数的双曲正弦并返回结果。
image: "/img/stackql-featured-image.png"
---
计算指定值的双曲正弦。

另请参阅：  
[[`SELECT`]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT SINH(value) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*value*__  
要计算双曲正弦的数值表达式。

## 返回值 {#return-values}
返回指定值的双曲正弦。

* * *

## 示例 {#examples}

### 计算 1 的双曲正弦 {#calculate-the-hyperbolic-sine-of-1}

```sql
SELECT SINH(1) AS hyperbolic_sine_value;
```

此查询返回约 `1.1752`，因为 1 的双曲正弦约为 1.1752。

### 计算 0 的双曲正弦 {#calculate-the-hyperbolic-sine-of-0}

```sql
SELECT SINH(0) AS hyperbolic_sine_value;
```

此查询计算 0 的双曲正弦，结果为 `0`，因为 0 的双曲正弦恰好为 0。
