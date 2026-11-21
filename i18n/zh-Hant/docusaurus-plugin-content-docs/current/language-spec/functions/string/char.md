---
title: CHAR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 CHAR 函式：一或多個字元碼對應的字元，用於在字串中放入換行字元、引號、定位字元和其他控制字元。"
image: "/img/stackql-featured-image.png"
---

`CHAR()` 函式傳回由一或多個字元碼所對應之字元組成的字串。

另請參閱：  
[[`SELECT`]](/language-spec/select)

* * *

:::note
搭配 PostgreSQL 後端使用 StackQL 時，對等的函式為 `CHR()`，它只接受單一字元碼。
:::

## 語法 {#syntax}

```sql
SELECT CHAR(ascii_code [, ascii_code, ...]) FROM <multipartIdentifier>;
```

> 每個引數會在結果中加入一個字元，因此 `CHAR(72, 105)` 傳回 `Hi`。

## 引數 {#arguments}

**_ascii_code_**  
一或多個整數，每個都是某個字元的字元碼：ASCII 碼，或任何 Unicode 字碼指標。

## 傳回值 {#return-values}

傳回字串，其中每個 *ascii_code* 引數各對應一個字元。

* * *

## 常用 ASCII 碼 {#common-ascii-codes}

| 字元     | 描述           | ASCII 碼 | 用法範例                   |
|---------------|-----------------------|------------|---------------------------------|
| `\n`          | 換行字元               | 10         | `CHAR(10)`                      |
| `'`           | 單引號          | 39         | `CHAR(39)`                      |
| `"`           | 雙引號          | 34         | `CHAR(34)`                      |
| （空格）       | 空格                 | 32         | `CHAR(32)`                      |
| `:`           | 冒號                 | 58         | `CHAR(58)`                      |
| `;`           | 分號             | 59         | `CHAR(59)`                      |
| `\t`          | 水平定位字元        | 9          | `CHAR(9)`                       |
| `\r`          | 歸位字元       | 13         | `CHAR(13)`                      |
| `\b`          | 退格字元             | 8          | `CHAR(8)`                       |
| `\f`          | 換頁字元             | 12         | `CHAR(12)`                      |

* * *

## 範例 {#examples}

### 串連含特殊字元的字串 {#concatenate-strings-with-special-characters}

若要串連字串並在其間加入換行字元：

```sql
SELECT 
'/* ' || displayName || ' */' || CHAR(10) || 'DELETE FROM databricks_account.iam.account_users WHERE account_id = ' || CHAR(39) || account_id || CHAR(39) || ' AND id = ' || CHAR(39) || id || CHAR(39)
FROM
databricks_account.iam.account_users
where account_id = 'abcd1234-1234-5678-90ab-abcdef123456'
AND active = false;
```

**結果**（顯示名稱為 `Jane Doe`、ID 為 `1234567890123456` 的使用者）：

```
/* Jane Doe */
DELETE FROM databricks_account.iam.account_users WHERE account_id = 'abcd1234-1234-5678-90ab-abcdef123456' AND id = '1234567890123456'
```

* * *
