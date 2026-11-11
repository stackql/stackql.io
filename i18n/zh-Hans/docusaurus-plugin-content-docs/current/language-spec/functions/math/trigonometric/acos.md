---
title: ACOS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算一个数的反余弦（余弦的反函数），返回以弧度表示的角度。
image: "/img/stackql-featured-image.png"
---
计算一个数的反余弦（余弦的反函数），即余弦等于指定数的角度，以弧度表示。

另请参阅：  
[[`SELECT`]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT ACOS(value) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*value*__  
要计算反余弦的数值表达式，其值在 -1 到 1 的范围内，含两端。

## 返回值 {#return-values}
返回指定值的反余弦，以弧度表示。

* * *

## 示例 {#examples}

### 计算 0.5 的反余弦 {#calculate-the-arc-cosine-of-05}

```sql
SELECT ACOS(0.5) AS arc_cosine_value;
```

此查询返回约 `1.047`，因为 0.5 弧度的反余弦为 π/3，约为 1.047 弧度。

### 计算 1 的反余弦 {#calculate-the-arc-cosine-of-1}

```sql
SELECT ACOS(1) AS arc_cosine_value;
```

此查询计算 1 的反余弦，结果为 `0`，因为 0 弧度的余弦为 1。
