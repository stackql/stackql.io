---
title: LTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 LTRIM 函数：从字符串左侧移除空白字符或指定的一组字符。"
image: "/img/stackql-featured-image.png"
---
返回移除输入字符串左侧出现的空白字符或任意字符后形成的字符串。  

另请参阅：  
[[` SELECT `]](/language-spec/select) [[` RTRIM `]](/language-spec/functions/string/rtrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## 语法 {#syntax}

```sql
SELECT LTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*character_expression*__  
字符串字面量或字符串列。

__*character*__  
可选字符，如果出现在 *character_expression* 的开头，则将其移除。

## 返回值 {#return-values}
返回一个字符串。

* * *

## 示例 {#examples}

### 移除字符串列开头的空白字符 {#remove-leading-whitespace-from-a-string-column}

```sql
SELECT ltrim(description) AS leading_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_corefunc.html#ltrim](https://www.sqlite.org/lang_corefunc.html#ltrim)
