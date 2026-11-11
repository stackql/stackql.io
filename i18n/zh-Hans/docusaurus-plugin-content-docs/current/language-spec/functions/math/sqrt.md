---
title: SQRT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中使用 SQL 计算一个数的平方根。
image: "/img/stackql-featured-image.png"
---
计算非负数值表达式的平方根。

另请参阅：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 语法 {#syntax}

```sql
SELECT SQRT(numeric_expression) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*numeric_expression*__  
要计算平方根的非负数值表达式。

## 返回值 {#return-values}
返回指定非负数值的平方根。

* * *

## 示例 {#examples}

### 计算一个数的平方根 {#calculate-the-square-root-of-a-number}

```sql
SELECT SQRT(9) AS square_root;
```
