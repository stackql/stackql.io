---
title: LOWER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 LOWER 函数：将字符串转换为小写。"
image: "/img/stackql-featured-image.png"
---
以小写形式返回输入字符串。  

另请参阅：  
[[` SELECT `]](/language-spec/select) [[` UPPER `]](/language-spec/functions/string/upper) [[ 数据类型 ]](/language-spec/data-types)

* * * 

## 语法 {#syntax}

```sql
SELECT LOWER(character_expression) FROM <multipartIdentifier>;
```
> `lower()` 函数仅适用于 ASCII 字符。

## 参数 {#arguments}

__*character_expression*__  
ASCII 字符串。

## 返回值 {#return-values}
返回一个字符串值。

* * *

## 示例 {#examples}

### 以小写形式返回字符串 {#return-a-string-in-lowercase}

```sql
SELECT name, LOWER(location) FROM google.storage.buckets WHERE project = 'stackql-demo';
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_corefunc.html#lower](https://www.sqlite.org/lang_corefunc.html#lower)
