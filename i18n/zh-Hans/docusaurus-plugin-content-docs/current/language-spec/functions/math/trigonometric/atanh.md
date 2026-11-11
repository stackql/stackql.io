---
title: ATANH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算一个数的反双曲正切（双曲正切的反函数），返回以弧度表示的角度。
image: "/img/stackql-featured-image.png"
---
计算一个数的反双曲正切（双曲正切的反函数），即双曲正切等于指定数的弧度值 \( \theta \)。

另请参阅：  
[[`SELECT`]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT ATANH(value) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*value*__  
要计算反双曲正切的数值表达式，其值在 -1 到 1 的范围内（不含两端）。

## 返回值 {#return-values}
返回指定值的反双曲正切，以弧度表示。

* * *

## 示例 {#examples}

### 计算 0.5 的反双曲正切 {#calculate-the-hyperbolic-arc-tangent-of-05}

```sql
SELECT ATANH(0.5) AS hyperbolic_arc_tangent_value;
```

此查询返回约 `0.5493`，因为 0.5 的反双曲正切约为 0.5493 弧度。

### 计算 -0.5 的反双曲正切 {#calculate-the-hyperbolic-arc-tangent-of--05}

```sql
SELECT ATANH(-0.5) AS hyperbolic_arc_tangent_value;
```

此查询计算 -0.5 的反双曲正切，结果约为 `-0.5493`，因为这是双曲正切等于 -0.5 的弧度值。
