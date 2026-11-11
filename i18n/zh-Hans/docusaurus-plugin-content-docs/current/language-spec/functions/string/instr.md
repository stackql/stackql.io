---
title: INSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 INSTR 函数：返回一个字符串在另一个字符串中首次出现的位置（从 1 开始），未找到时返回 0。"
image: "/img/stackql-featured-image.png"
---
返回一个字符串在另一个字符串中首次出现的字符位置；如果未找到要搜索的字符串，则返回 0。

另请参阅：  
[[` SELECT `]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT INSTR(string, searchstring) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*string*__  
被搜索的源字符串。

__*searchstring*__  
要搜索的子字符串。

> 如果 *string* 和 *searchstring* 都是 BLOB，则 `instr(<string>, <searchstring>)` 返回 *searchstring* 首次出现位置之前的字节数加 1；如果 *searchstring* 未在 *string* 中的任何位置出现，则返回 0。如果两个参数都不为 `NULL` 且都不是 BLOB，则两者都被解释为字符串。

## 返回值 {#return-values}
返回一个整数。

> 如果 *string* 或 *searchstring* 的值为 `NULL`，则结果为 `NULL`。

* * *

## 示例 {#examples}

### 在字段中搜索字符串 {#search-a-field-for-a-string}

```sql
SELECT INSTR(name, 'worker') as charpos
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
returns 0 if the term 'worker' is not present in the 'name' column, otherwise returns the character position (starting from 1) of the first occurence of the term 'worker' in the 'name' column
*/
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_corefunc.html#instr](https://www.sqlite.org/lang_corefunc.html#instr)
