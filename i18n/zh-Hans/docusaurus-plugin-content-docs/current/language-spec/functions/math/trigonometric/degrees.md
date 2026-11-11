---
title: DEGREES
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 将角度从弧度转换为度。
image: "/img/stackql-featured-image.png"
---
将指定角度从弧度转换为度。

另请参阅：  
[[`SELECT`]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT DEGREES(angle_in_radians) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*angle_in_radians*__  
以弧度表示角度的数值表达式。

## 返回值 {#return-values}
返回该角度对应的度数。

* * *

## 示例 {#examples}

### 将 π 弧度转换为度 {#convert-π-radians-to-degrees}

```sql
SELECT DEGREES(3.14159) AS degrees_value;
```

此查询返回约 `180`，因为将 π 弧度（约 `3.14159`）转换后得到约 180 度。

### 将 π/2 弧度转换为度 {#convert-π2-radians-to-degrees}

```sql
SELECT DEGREES(1.5708) AS degrees_value;
```

此查询计算 π/2 弧度（约 `1.5708`）到度的转换，结果约为 `90`，因为 π/2 弧度相当于 90 度的角度。
