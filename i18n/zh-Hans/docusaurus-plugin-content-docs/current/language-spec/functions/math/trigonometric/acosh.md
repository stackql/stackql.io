---
title: ACOSH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算一个数的反双曲余弦（双曲余弦的反函数），返回以弧度表示的角度。
image: "/img/stackql-featured-image.png"
---
计算一个数的反双曲余弦（双曲余弦的反函数），即双曲余弦等于指定数的非负弧度值 \( \theta \)。

另请参阅：  
[[`SELECT`]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT ACOSH(value) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*value*__  
要计算反双曲余弦的数值表达式，其值大于或等于 1。

## 返回值 {#return-values}
返回指定值的反双曲余弦，以弧度表示。

* * *

## 示例 {#examples}

### 计算 1 的反双曲余弦 {#calculate-the-hyperbolic-arc-cosine-of-1}

```sql
SELECT ACOSH(1) AS hyperbolic_arc_cosine_value;
```

此查询返回 `0`，因为 1 的反双曲余弦为 0 弧度。

### 计算 10 的反双曲余弦 {#calculate-the-hyperbolic-arc-cosine-of-10}

```sql
SELECT ACOSH(10) AS hyperbolic_arc_cosine_value;
```

此查询计算 10 的反双曲余弦，结果约为 `2.9932`，因为这是双曲余弦等于 10 的弧度值。
