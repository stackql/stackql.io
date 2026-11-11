---
title: MOD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算一个数除以另一个数的余数。
image: "/img/stackql-featured-image.png"
---
计算 *x* 除以 *y* 的余数，返回取模结果。这与使用“%”运算符类似，但兼容非整数参数。

另请参阅：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 语法 {#syntax}

```sql
SELECT MOD(x, y) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*x*__  
被除数，是一个数值表达式。

__*y*__  
除数，同样是一个数值表达式。

## 返回值 {#return-values}
返回 *x* 除以 *y* 的余数。

* * *

## 示例 {#examples}

### 计算 10 除以 3 的取模结果 {#calculate-the-modulus-of-10-divided-by-3}

```sql
SELECT MOD(10, 3) AS remainder;
```
