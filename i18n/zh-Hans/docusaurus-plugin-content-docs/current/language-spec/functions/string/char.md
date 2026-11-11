---
title: CHAR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 CHAR 函数：返回一个或多个字符代码对应的字符，用于在字符串中插入换行符、引号、制表符和其他控制字符。"
image: "/img/stackql-featured-image.png"
---

`CHAR()` 函数返回与指定 ASCII 码对应的字符。

另请参阅：  
[[`SELECT`]](/language-spec/select)

* * *

:::note
将 StackQL 与 PostgreSQL 后端搭配使用时，等效函数为 `CHR()`。
:::

## 语法 {#syntax}

```sql
SELECT CHAR(ascii_code) FROM <multipartIdentifier>;
```

> `CHAR()` 函数返回与给定 ASCII 码关联的字符。

## 参数 {#arguments}

**_ascii_code_**  
一个整数，表示所需字符的 ASCII 码。

## 返回值 {#return-values}

返回与指定 ASCII 码对应的单字符字符串。

* * *

## 常用 ASCII 码 {#common-ascii-codes}

| 字符          | 说明                  | ASCII 码   | 用法示例                        |
|---------------|-----------------------|------------|---------------------------------|
| `\n`          | 换行符                | 10         | `CHAR(10)`                      |
| `'`           | 单引号                | 39         | `CHAR(39)`                      |
| `"`           | 双引号                | 34         | `CHAR(34)`                      |
| （空格）      | 空格                  | 32         | `CHAR(32)`                      |
| `:`           | 冒号                  | 58         | `CHAR(58)`                      |
| `;`           | 分号                  | 59         | `CHAR(59)`                      |
| `\t`          | 水平制表符            | 9          | `CHAR(9)`                       |
| `\r`          | 回车符                | 13         | `CHAR(13)`                      |
| `\b`          | 退格符                | 8          | `CHAR(8)`                       |
| `\f`          | 换页符                | 12         | `CHAR(12)`                      |

* * *

## 示例 {#examples}

### 将字符串与特殊字符拼接 {#concatenate-strings-with-special-characters}

拼接字符串并在其间插入换行符：

```sql
SELECT 
'/* ' || displayName || ' */' || CHAR(10) || 'DELETE FROM databricks_account.iam.account_users WHERE account_id = ' || CHAR(39) || account_id || CHAR(39) || ' AND id = ' || CHAR(39) || id || CHAR(39)
FROM
databricks_account.iam.account_users
where account_id = 'abcd1234-1234-5678-90ab-abcdef123456'
AND active = false;
```

**结果：**

```
He said "Hello" and left.
```

* * *
