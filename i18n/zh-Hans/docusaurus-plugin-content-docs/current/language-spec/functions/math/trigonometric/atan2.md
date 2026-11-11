---
title: ATAN2
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算两个数（y/x）的反正切，返回以弧度表示的角度。
image: "/img/stackql-featured-image.png"
---
计算两个数的反正切，具体而言是正切等于两个指定数之比（y 除以 x）的角度 \( \theta \)，以弧度表示，并正确处理输入的符号以确定正确的象限。

另请参阅：  
[[`SELECT`]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT ATAN2(y, x) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*y*__  
点的 y 坐标（分子）。

__*x*__  
点的 x 坐标（分母）。

## 返回值 {#return-values}
返回 y/x 的反正切，以弧度表示，并考虑角度所在的正确象限。

* * *

## 示例 {#examples}

### 计算点 (1,1) 的反正切 {#calculate-the-arc-tangent-of-the-point-11}

```sql
SELECT ATAN2(1, 1) AS arc_tangent_value;
```

此查询返回 π/4，约为 `0.785`，因为点 (1,1) 的反正切对应第一象限中 π/4 弧度的角度。

### 计算点 (10, -10) 的反正切 {#calculate-the-arc-tangent-of-the-point-10--10}

```sql
SELECT ATAN2(10, -10) AS arc_tangent_value;
```

此查询计算点 (10, -10) 的反正切，结果为 3π/4，约为 `2.356`，因为该角度在第二象限中的正切为 -1。
