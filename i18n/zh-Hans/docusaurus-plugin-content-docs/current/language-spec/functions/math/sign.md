---
title: SIGN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 SIGN 函数：根据数值参数为负数、零或正数返回 -1、0 或 1。"
image: "/img/stackql-featured-image.png"
---
如果输入值为负数，返回 -1；如果输入值为 0，返回 0；如果输入值为正数，返回 1。  

另请参阅：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 语法 {#syntax}

```sql
SELECT SIGN(numeric_expression) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*numeric_expression*__  
属于精确数值或近似数值数据类型类别的表达式。

> 如果 *numeric_expression* 为 `NULL`，或者是无法无损转换为数字的 `string` 或 BLOB，则函数返回 `NULL`。

## 返回值 {#return-values}

返回 `-1`、`0` 或 `1`。

* * *

## 示例 {#examples}

### 判断一个数的符号 {#determine-the-sign-of-a-number}

```sql
SELECT name, sign(strftime('%s', creationTimestamp)-strftime('%s','now')) as sign
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_corefunc.html#sign](https://www.sqlite.org/lang_corefunc.html#sign)
