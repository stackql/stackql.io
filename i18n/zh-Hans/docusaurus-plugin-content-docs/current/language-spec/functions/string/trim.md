---
title: TRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 TRIM 函数：从字符串两端移除空格或指定字符集中的任意字符。"
image: "/img/stackql-featured-image.png"
---
返回从输入字符串两端移除空格或指定字符集中的任意字符后形成的字符串。  

另请参阅：  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` RTRIM `]](/language-spec/functions/string/rtrim) 

* * * 

## 语法 {#syntax}

```sql
SELECT TRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*character_expression*__  
字符串字面量或字符串列。

__*character*__  
可选的字符串：其中的每个字符都会从 *character_expression* 的开头和末尾移除。如果省略，则移除空格（不包括制表符和换行符）。

## 返回值 {#return-values}
返回一个字符串。

* * *

## 示例 {#examples}

### 移除字符串列开头和末尾的空格 {#remove-leading-and-trailing-spaces-from-a-string-column}

```sql
SELECT trim(description) AS leading_and_trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_corefunc.html#trim](https://www.sqlite.org/lang_corefunc.html#trim)
