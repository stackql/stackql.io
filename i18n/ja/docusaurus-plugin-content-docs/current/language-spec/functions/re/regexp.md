---
title: REGEXP
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使って、正規表現で文字列内のパターンを照合します。
image: "/img/stackql-featured-image.png"
---
ソース文字列が指定した正規表現パターンに一致するかどうかを確認します。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT source_string REGEXP pattern_expression AS match_result FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*source_string*__  
パターンを検索する対象の文字列です。

__*pattern_expression*__  
ソース文字列と照合する正規表現パターンです。

## 戻り値 {#return-values}
ソース文字列が正規表現パターンに一致する場合は `1`、一致しない場合は `0` を返します。

* * *

## 例 {#examples}

### 文字列に数字が含まれるかどうかを確認する {#check-if-a-string-contains-numbers}

この例では、文字列 'the year is 2021' に 1 文字以上の数字が含まれるかどうかを確認します。

```sql
SELECT 'the year is 2021' REGEXP '[0-9]+' AS match_result;
```

文字列には部分文字列 '2021' が含まれ、これは 1 桁以上の数字を表すパターン `[0-9]+` に一致します。そのため、このクエリは `1` を返します。

### メールアドレスの形式を検証する {#validate-email-format}

文字列が有効なメールアドレスの形式かどうかを確認するには、次のようにします。

```sql
SELECT 'user@example.com' REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' AS match_result;
```

このクエリは `1` を返します。これは、'user@example.com' が指定した正規表現パターンに一致し、一般的なメールアドレスの形式であることを示します。
