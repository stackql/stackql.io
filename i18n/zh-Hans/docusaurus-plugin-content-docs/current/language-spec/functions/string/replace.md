---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 REPLACE 函数：将字符串中某个子字符串的每次出现替换为另一个字符串。"
image: "/img/stackql-featured-image.png"
---
返回将字符串中某一部分的每次出现替换为另一个字符串后形成的字符串。

另请参阅：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 语法 {#syntax}

```sql
SELECT REPLACE(character_expression, pattern, replacement) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*character_expression*__  
要执行替换的字符串字面量或字符串列。

__*pattern*__  
要在原始字符串中查找的子字符串。

> 如果 *pattern* 是空字符串或未在 *character_expression* 中找到，则原样返回 *character_expression*。

__*replacement*__  
用于替换的字符串。

> 如果 *replacement* 最初不是字符串，则在处理之前将其转换为 UTF-8 字符串。

> 比较时使用 `BINARY` 排序规则。

## 返回值 {#return-values}
返回一个字符串。

* * *

## 示例 {#examples}

### 替换列中的字符串模式 {#replace-a-string-pattern-in-a-column}

```sql
SELECT replace(name, 'prd', 'production') AS replacement_string
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_corefunc.html#replace](https://www.sqlite.org/lang_corefunc.html#replace)
