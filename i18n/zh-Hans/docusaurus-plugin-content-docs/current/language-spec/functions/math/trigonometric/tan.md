---
title: TAN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算以弧度给出的角度的正切。
image: "/img/stackql-featured-image.png"
---
计算以弧度给出的指定角度的正切。

另请参阅：  
[[`SELECT`]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT TAN(radian_value) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*radian_value*__  
以弧度指定要计算正切的角度的数值表达式。

## 返回值 {#return-values}
返回指定角度的正切。

* * *

## 示例 {#examples}

### 计算 π/4 弧度的正切 {#calculate-the-tangent-of-π4-radians}

```sql
SELECT TAN(PI() / 4) AS tangent_value;
```

此查询返回 `1`，因为 π/4 弧度的正切为 1。

### 计算 π 弧度的正切 {#calculate-the-tangent-of-π-radians}

```sql
SELECT TAN(PI()) AS tangent_value;
```

此查询计算 π 弧度的正切，结果约为 `0`，因为 π 的正切非常接近 0。
