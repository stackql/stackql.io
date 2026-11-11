---
title: ATAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算一个数的反正切（正切的反函数），返回以弧度表示的角度。
image: "/img/stackql-featured-image.png"
---
计算一个数的反正切（正切的反函数），即正切等于指定数的角度，以弧度表示。

另请参阅：  
[[`SELECT`]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT ATAN(value) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*value*__  
要计算反正切的数值表达式。

## 返回值 {#return-values}
返回指定值的反正切，以弧度表示。

* * *

## 示例 {#examples}

### 计算 1 的反正切 {#calculate-the-arc-tangent-of-1}

```sql
SELECT ATAN(1) AS arc_tangent_value;
```

此查询返回 π/4，约为 `0.785`，因为 1 的反正切为 π/4 弧度。

### 计算 0 的反正切 {#calculate-the-arc-tangent-of-0}

```sql
SELECT ATAN(0) AS arc_tangent_value;
```

此查询计算 0 的反正切，结果为 `0`，因为 0 弧度的正切为 0。
