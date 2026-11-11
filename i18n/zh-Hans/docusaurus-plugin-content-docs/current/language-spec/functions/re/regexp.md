---
title: REGEXP
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: 在 StackQL 中通过 SQL 使用正则表达式匹配字符串中的模式。
image: "/img/stackql-featured-image.png"
---
检查源字符串是否与提供的正则表达式模式匹配。

另请参阅：  
[[`SELECT`]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT source_string REGEXP pattern_expression AS match_result FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*source_string*__  
要在其中搜索模式的字符串。

__*pattern_expression*__  
用于与源字符串进行匹配的正则表达式模式。

## 返回值 {#return-values}
返回布尔值：如果源字符串与正则表达式模式匹配，则为 `TRUE`，否则为 `FALSE`。

* * *

## 示例 {#examples}

### 检查字符串是否包含数字 {#check-if-a-string-contains-numbers}

此示例演示如何检查字符串 'the year is 2021' 是否包含一个或多个数字字符：

```sql
SELECT 'the year is 2021' REGEXP '[0-9]+' AS match_result;
```

此查询将返回 `TRUE`，因为该字符串包含子字符串 '2021'，它与表示一个或多个数字的模式 `[0-9]+` 匹配。

### 验证电子邮件格式 {#validate-email-format}

检查字符串是否为有效的电子邮件格式：

```sql
SELECT 'user@example.com' REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' AS match_result;
```

此查询返回 `TRUE`，表明 'user@example.com' 的格式符合典型的电子邮件地址，与指定的正则表达式模式匹配。
