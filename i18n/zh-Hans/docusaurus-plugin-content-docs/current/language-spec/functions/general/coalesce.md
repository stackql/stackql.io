---
title: COALESCE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 COALESCE 函数：返回两个或更多参数中第一个非 NULL 的参数，通常用于以默认值替代缺失值。"
image: "/img/stackql-featured-image.png"
---
从一系列两个或更多参数中返回第一个不为 `NULL` 的参数，可用于以默认值替代 `NULL` 值。  

> `COALESCE` 函数等价于 Oracle 中的 `NVL` 函数或 MySQL 中的 `IFNULL` 函数。

另请参阅：  
[[` SELECT `]](/language-spec/select) [[` IFNULL `]](/language-spec/functions/general/ifnull) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## 语法 {#syntax}

```sql
SELECT COALESCE(expression1, expression2 [, ..]) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*expression*__  
两个或更多任意数据类型的表达式，求值结果为 `NULL` 或非 `NULL` 值。

> 如果所有 *expression* 参数均为 `NULL`，则 `COALESCE` 函数返回 `NULL`。

## 返回值 {#return-values}

返回一系列 *expression* 参数中第一个非 `NULL` 的值。

* * *

## 示例 {#examples}

### 用默认值替换 `NULL` 值 {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, coalesce(website, cors, 'not hosting shared resources') AS website_or_cors_config
FROM google.storage.buckets where project = 'stackql';
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_corefunc.html#coalesce](https://www.sqlite.org/lang_corefunc.html#coalesce)
