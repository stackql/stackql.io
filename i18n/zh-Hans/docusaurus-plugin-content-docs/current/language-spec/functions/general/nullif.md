---
title: NULLIF
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 NULLIF 函数：两个参数相等时返回 NULL，否则返回第一个参数。"
image: "/img/stackql-featured-image.png"
---
接受两个参数，如果二者相等则返回 `NULL` 值，否则返回第一个参数。  

> `NULLIF` 函数等价于 `CASE WHEN expression1 = expression2 THEN NULL ELSE expression1 END`

> 当结果集包含并非 `NULL` 但你希望在聚合时视为 `NULL` 的特殊值时，`NULLIF` 适合与 [`AVG`](/language-spec/functions/aggregate/avg)、[`MAX`](/language-spec/functions/aggregate/max)、[`MIN`](/language-spec/functions/aggregate/min)、[`SUM`](/language-spec/functions/aggregate/sum) 和 [`COUNT`](/language-spec/functions/aggregate/count) 等聚合函数配合使用。

另请参阅：  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` IFNULL `]](/language-spec/functions/general/ifnull)

* * * 

## 语法 {#syntax}

```sql
SELECT NULLIF(expression1, expression2) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*expression*__  
两个任意数据类型的表达式，求值结果为 `NULL` 或非 `NULL` 值。

> `NULLIF` 函数从左到右在其参数中查找定义了排序规则函数的参数，并将该排序规则函数用于所有字符串比较。如果两个参数都未定义排序规则函数，则使用 `BINARY` 排序规则。

## 返回值 {#return-values}

如果两个 *expression* 参数的求值结果相同，则返回 `NULL`，否则返回第一个值。 

* * *

## 示例 {#examples}

### 统计某字段不等于给定值的记录数 {#count-records-where-a-field-is-not-equal-to-a-given-value}

```sql
SELECT COUNT(NULLIF(name, 'controller-0'))
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
equivalent to: 
SELECT COUNT(*) FROM google.compute.instances 
WHERE name != 'controller-0';
*/
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_corefunc.html#nullif](https://www.sqlite.org/lang_corefunc.html#nullif)
