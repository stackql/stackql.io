---
title: SUBSTRING
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 SUBSTRING 函数（别名 SUBSTR）：按起始位置和长度返回字符串或二进制值的一部分。"
image: "/img/stackql-featured-image.png"
---
返回字符表达式或二进制表达式的一部分。`SUBSTR` 是 `SUBSTRING` 的别名。

另请参阅：  
[[` SELECT `]](/language-spec/select) [[ 数据类型 ]](/language-spec/data-types)

* * * 

:::info

要从 JSON 对象中提取字段，请使用 [**JSON_EXTRACT**](/language-spec/functions/json/json_extract) 函数。

:::

## 语法 {#syntax}

```sql
SELECT SUBSTRING(expression, start[, length]) FROM <multipartIdentifier>;
SELECT SUBSTR(expression, start[, length]) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*expression*__  
ASCII 字符串或 BLOB。

> 如果 *expression* 是字符串，则字符索引指的是实际的 UTF-8 字符。如果 *expression* 是 BLOB，则索引指的是字节。

__*start*__  
一个整数，表示要返回的输入表达式子序列的第一个字符或字节。如果 *start* 为负数，则从右侧而不是左侧开始计数来确定子字符串的第一个字符。

> *expression* 最左侧的字符编号为 1。

__*length*__  
可选的整数值，表示从 *start* 值之后要返回的子序列的长度。如果省略 *length*，则返回直到 *expression* 指定的字符串或 BLOB 末尾的所有字符。

> 如果 *length* 为负数，则使用 *length* 的绝对值。

## 返回值 {#return-values}

根据 *expression* 的类型返回字符串或 BLOB 值。

* * *

## 示例 {#examples}

### 返回列的前 *n* 个字符 {#return-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, 1, 5) as "first five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 截去列的前 *n* 个字符 {#truncate-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTR(name, 5) as "from char 5" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```
### 返回列的最后 *n* 个字符 {#return-the-last-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, -5) as "last five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_corefunc.html#substr](https://www.sqlite.org/lang_corefunc.html#substr)
