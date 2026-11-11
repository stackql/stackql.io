---
title: COS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算以弧度给出的角度的余弦。
image: "/img/stackql-featured-image.png"
---
计算以弧度给出的指定角度的余弦。

另请参阅：  
[[`SELECT`]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT COS(radian_value) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*radian_value*__  
以弧度指定要计算余弦的角度的数值表达式。

## 返回值 {#return-values}
返回指定角度的余弦。

* * *

## 示例 {#examples}

### 计算 π/3 弧度的余弦 {#calculate-the-cosine-of-π3-radians}

```sql
SELECT COS(PI() / 3) AS cosine_value;
```

此查询返回 `0.5`，因为 π/3 弧度的余弦为 0.5。

### 计算 π 弧度的余弦 {#calculate-the-cosine-of-π-radians}

```sql
SELECT COS(PI()) AS cosine_value;
```

此查询计算 π 弧度的余弦，结果为 `-1`，因为 π 的余弦为 -1。
