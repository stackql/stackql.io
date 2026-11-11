---
title: ASINH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算一个数的反双曲正弦（双曲正弦的反函数），返回以弧度表示的角度。
image: "/img/stackql-featured-image.png"
---
计算一个数的反双曲正弦（双曲正弦的反函数），即双曲正弦等于指定数的弧度值 \( \theta \)。

另请参阅：  
[[`SELECT`]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT ASINH(value) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*value*__  
要计算反双曲正弦的数值表达式。

## 返回值 {#return-values}
返回指定值的反双曲正弦，以弧度表示。

* * *

## 示例 {#examples}

### 计算 0.5 的反双曲正弦 {#calculate-the-hyperbolic-arc-sine-of-05}

```sql
SELECT ASINH(0.5) AS hyperbolic_arc_sine_value;
```

此查询返回约 `0.4812`，因为 0.5 的反双曲正弦约为 0.4812 弧度。

### 计算 -1 的反双曲正弦 {#calculate-the-hyperbolic-arc-sine-of--1}

```sql
SELECT ASINH(-1) AS hyperbolic_arc_sine_value;
```

此查询计算 -1 的反双曲正弦，结果约为 `-0.8814`，因为这是双曲正弦等于 -1 的弧度值。
