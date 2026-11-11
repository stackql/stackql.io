---
title: RADIANS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 将角度从度转换为弧度。
image: "/img/stackql-featured-image.png"
---
将指定角度从度转换为弧度。

另请参阅：  
[[`SELECT`]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT RADIANS(angle_in_degrees) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*angle_in_degrees*__  
以度表示角度的数值表达式。

## 返回值 {#return-values}
返回该角度对应的弧度值。

* * *

## 示例 {#examples}

### 将 180 度转换为弧度 {#convert-180-degrees-to-radians}

```sql
SELECT RADIANS(180) AS radians_value;
```

此查询返回 π（约 `3.14159`），因为将 180 度转换为弧度得到 π 弧度。

### 将 90 度转换为弧度 {#convert-90-degrees-to-radians}

```sql
SELECT RADIANS(90) AS radians_value;
```

此查询计算 90 度到弧度的转换，结果为 π/2，约为 `1.5708`，因为 90 度是 180 度的一半。
