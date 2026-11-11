---
title: ABS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 ABS 函数：数值参数的绝对值。"
image: "/img/stackql-featured-image.png"
---
返回数值参数的绝对值。  

另请参阅：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 语法 {#syntax}

```sql
SELECT ABS(numeric_expression) FROM <multipartIdentifier>;
```

> 如果 `X` 为 `NULL`，`abs(X)` 返回 `NULL`。如果 `X` 是无法转换为数值的字符串或 blob，`abs(X)` 返回 `0.0`。如果 `X` 是整数 -9223372036854775808，则 `abs(X)` 会抛出整数溢出错误，因为不存在与之对应的 64 位二进制补码正值。

## 参数 {#arguments}

__*numeric_expression*__  
属于精确数值或近似数值数据类型类别的表达式。

## 返回值 {#return-values}

返回一个正数值。

* * *

## 示例 {#examples}

### 返回一个数的绝对值 {#return-the-absolute-value-of-a-number}

```sql
SELECT name, abs(strftime('%s', creationTimestamp)-strftime('%s','now')) as abs_value
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_mathfunc.html](https://www.sqlite.org/lang_mathfunc.html)
