---
title: LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 LENGTH 函数：返回字符串中的字符数，或 BLOB 中的字节数。"
image: "/img/stackql-featured-image.png"
---
返回字符串输入表达式中第一个 `NULL` 字符之前的字符数，或 BLOB 表达式中的字节数。  

另请参阅：  
[[` SELECT `]](/language-spec/select) 

* * * 

:::info

要获取 JSON 数组的长度，请使用 [**JSON_ARRAY_LENGTH**](/language-spec/functions/json/json_array_length) 函数。

:::

## 语法 {#syntax}

```sql
SELECT LENGTH(expression) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*expression*__  
ASCII 字符串或 BLOB。

> 如果 *expression* 为 `NULL`，则返回 `NULL`。

> 如果 *expression* 是数值，则返回 *expression* 的字符串表示形式的长度。

## 返回值 {#return-values}
返回一个整数。

* * *

## 示例 {#examples}

### 返回字符串列的长度 {#return-the-length-of-a-string-column}

```sql
SELECT name, length(name) AS len
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_corefunc.html#length](https://www.sqlite.org/lang_corefunc.html#length)
