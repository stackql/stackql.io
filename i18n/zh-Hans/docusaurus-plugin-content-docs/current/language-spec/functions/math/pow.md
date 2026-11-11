---
title: POW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算以一个数为底数、另一个数为指数的幂。
image: "/img/stackql-featured-image.png"
---
计算 *x* 的 *y* 次幂。

另请参阅：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 语法 {#syntax}

```sql
SELECT POW(x, y) FROM <multipartIdentifier>;
-- or using the equivalent
SELECT POWER(x, y) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*x*__  
底数，是一个数值表达式。

__*y*__  
底数 *x* 的指数，同样是一个数值表达式。

## 返回值 {#return-values}
返回 *x* 的 *y* 次幂的结果。

* * *

## 示例 {#examples}

### 计算 x 的 y 次幂 {#calculate-x-raised-to-the-power-y}


```sql
SELECT POW(2, 3) AS result;
```
