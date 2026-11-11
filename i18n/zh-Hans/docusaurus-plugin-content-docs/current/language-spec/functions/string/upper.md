---
title: UPPER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 UPPER 函数：将字符串转换为大写。"
image: "/img/stackql-featured-image.png"
---
以大写形式返回输入字符串。  

另请参阅：  
[[` SELECT `]](/language-spec/select) [[` LOWER `]](/language-spec/functions/string/lower) [[ 数据类型 ]](/language-spec/data-types)

* * * 

## 语法 {#syntax}

```sql
SELECT UPPER(character_expression) FROM <multipartIdentifier>;
```
> `upper()` 函数仅适用于 ASCII 字符。

## 参数 {#arguments}

__*character_expression*__  
ASCII 字符串。

## 返回值 {#return-values}
返回一个字符串值。

* * *

## 示例 {#examples}

### 以大写形式返回字符串 {#return-a-string-in-uppercase}

```sql
SELECT UPPER(name), location FROM google.storage.buckets WHERE project = 'stackql-demo';
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_corefunc.html#upper](https://www.sqlite.org/lang_corefunc.html#upper)
