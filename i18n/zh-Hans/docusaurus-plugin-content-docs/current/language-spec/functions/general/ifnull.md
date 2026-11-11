---
title: IFNULL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 IFNULL 函数：返回恰好两个参数中第一个非 NULL 的参数，是 COALESCE 的双参数形式。"
image: "/img/stackql-featured-image.png"
---
恰好接受两个参数，并返回第一个非 `NULL` 的参数，可用于以默认值替代 `NULL` 值。  

> `IFNULL` 函数等价于只有两个参数的 [`COALESCE`](/language-spec/functions/general/coalesce) 函数，[`COALESCE`](/language-spec/functions/general/coalesce) 函数可以有两个以上的参数。

另请参阅：  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## 语法 {#syntax}

```sql
SELECT IFNULL(expression1, expression2) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*expression*__  
两个任意数据类型的表达式，求值结果为 `NULL` 或非 `NULL` 值。

> 如果两个 *expression* 参数均为 `NULL`，则 `IFNULL` 函数返回 `NULL`。


## 返回值 {#return-values}

返回两个 *expression* 参数中第一个非 `NULL` 的值。 

* * *

## 示例 {#examples}

### 用默认值替换 `NULL` 值 {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, ifnull(website, 'Not hosting a website') AS website 
FROM google.storage.buckets where project = 'stackql';
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_corefunc.html#ifnull](https://www.sqlite.org/lang_corefunc.html#ifnull)
