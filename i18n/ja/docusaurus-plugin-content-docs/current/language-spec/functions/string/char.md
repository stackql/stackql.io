---
title: CHAR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の CHAR 関数：文字列に改行、引用符、タブなどの制御文字を入れるための、1 つ以上の文字コードに対応する文字。"
image: "/img/stackql-featured-image.png"
---

`CHAR()` 関数は、指定した ASCII コードに対応する文字を返します。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * *

:::note
PostgreSQL バックエンドで StackQL を使用する場合、同等の関数は `CHR()` です。
:::

## 構文 {#syntax}

```sql
SELECT CHAR(ascii_code) FROM <multipartIdentifier>;
```

> `CHAR()` 関数は、指定した ASCII コードに関連付けられた文字を返します。

## 引数 {#arguments}

**_ascii_code_**  
目的の文字の ASCII コードを表す整数です。

## 戻り値 {#return-values}

指定した ASCII コードに対応する 1 文字の文字列を返します。

* * *

## よく使われる ASCII コード {#common-ascii-codes}

| 文字          | 説明                  | ASCII コード | 使用例                          |
|---------------|-----------------------|------------|---------------------------------|
| `\n`          | 改行                  | 10         | `CHAR(10)`                      |
| `'`           | 一重引用符            | 39         | `CHAR(39)`                      |
| `"`           | 二重引用符            | 34         | `CHAR(34)`                      |
| （スペース）  | スペース              | 32         | `CHAR(32)`                      |
| `:`           | コロン                | 58         | `CHAR(58)`                      |
| `;`           | セミコロン            | 59         | `CHAR(59)`                      |
| `\t`          | 水平タブ              | 9          | `CHAR(9)`                       |
| `\r`          | キャリッジリターン    | 13         | `CHAR(13)`                      |
| `\b`          | バックスペース        | 8          | `CHAR(8)`                       |
| `\f`          | フォームフィード      | 12         | `CHAR(12)`                      |

* * *

## 例 {#examples}

### 特殊文字を含む文字列を連結する {#concatenate-strings-with-special-characters}

文字列の間に改行文字を挟んで連結するには、次のようにします。

```sql
SELECT 
'/* ' || displayName || ' */' || CHAR(10) || 'DELETE FROM databricks_account.iam.account_users WHERE account_id = ' || CHAR(39) || account_id || CHAR(39) || ' AND id = ' || CHAR(39) || id || CHAR(39)
FROM
databricks_account.iam.account_users
where account_id = 'abcd1234-1234-5678-90ab-abcdef123456'
AND active = false;
```

**結果**（表示名が `Jane Doe`、ID が `1234567890123456` のユーザーの場合）：

```
/* Jane Doe */
DELETE FROM databricks_account.iam.account_users WHERE account_id = 'abcd1234-1234-5678-90ab-abcdef123456' AND id = '1234567890123456'
```

* * *
