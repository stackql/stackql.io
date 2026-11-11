---
title: 拼接运算符
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 || 拼接运算符：将两个或多个字符串值连接成一个字符串。"
image: "/img/stackql-featured-image.png"
---
返回两个或多个字符串值拼接（即连接）后得到的字符串。  

另请参阅：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 语法 {#syntax}

```sql
SELECT character_expression1 || character_expression2 FROM <multipartIdentifier>;
```

## 参数 {#arguments}

__*character_expression*__  
字符串字面量或字符串列。

## 返回值 {#return-values}

返回一个字符串。

:::note

从 StackQL `v1.0` 起，解析器将 `||` 视为拼接运算符，其结合优先级高于比较运算符，因此 `WHERE id = prefix || suffix` 会将 `id` 与连接后的字符串进行比较。早期发布版本将 `||` 作为布尔 `OR` 进行词法分析。布尔析取请使用 `OR`。[会话变量](/getting-started/session-variables)也可以拼接：`$prefix || '-' || $suffix`。

:::

* * *

## 示例 {#examples}

### 将两个字段拼接在一起 {#concatenate-two-fields-together}

```sql
SELECT name || ' (' || location || ')' as bucket_location
FROM google.storage.buckets 
WHERE project = 'stackql-demo';
```

有关更多信息，请参阅 [https://sqlite.org/lang_expr.html](https://sqlite.org/lang_expr.html)
