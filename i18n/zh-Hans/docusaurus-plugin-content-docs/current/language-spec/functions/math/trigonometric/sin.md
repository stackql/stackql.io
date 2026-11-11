---
title: SIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算以弧度给出的角度的正弦。
image: "/img/stackql-featured-image.png"
---
计算以弧度给出的指定角度的正弦。

另请参阅：  
[[`SELECT`]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT SIN(radian_value) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*radian_value*__  
以弧度指定要计算正弦的角度的数值表达式。

## 返回值 {#return-values}
返回指定角度的正弦。

* * *

## 示例 {#examples}

### 计算 π/2 弧度的正弦 {#calculate-the-sine-of-π2-radians}

```sql
SELECT SIN(PI() / 2) AS sine_value;
```

此查询返回 `1`，因为 π/2 弧度的正弦为 1。

### 计算 π 弧度的正弦 {#calculate-the-sine-of-π-radians}

```sql
SELECT SIN(PI()) AS sine_value;
```

此查询计算 π 弧度的正弦，结果约为 `0`，因为 π 的正弦为 0。
