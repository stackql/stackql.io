---
title: COALESCE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の COALESCE 関数：2 つ以上の引数から NULL でない最初の引数を返し、主に欠損値をデフォルト値で置き換えるために使います。"
image: "/img/stackql-featured-image.png"
---
2 つ以上の一連の引数から、`NULL` でない最初の引数を返します。`NULL` 値をデフォルト値で置き換えるために使用できます。  

> `COALESCE` 関数は、Oracle の `NVL` 関数や MySQL の `IFNULL` 関数と同等です。

関連項目：  
[[` SELECT `]](/language-spec/select) [[` IFNULL `]](/language-spec/functions/general/ifnull) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## 構文 {#syntax}

```sql
SELECT COALESCE(expression1, expression2 [, ..]) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*expression*__  
`NULL` 値または `NULL` 以外の値に評価される、任意のデータ型の 2 つ以上の式。

> *expression* 引数がすべて `NULL` の場合、`COALESCE` 関数は `NULL` を返します。

## 戻り値 {#return-values}

一連の *expression* 引数から、`NULL` でない最初の値を返します。

* * *

## 例 {#examples}

### `NULL` 値をデフォルト値で置き換える {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, coalesce(website, cors, 'not hosting shared resources') AS website_or_cors_config
FROM google.storage.buckets where project = 'stackql';
```

詳細については、[https://www.sqlite.org/lang_corefunc.html#coalesce](https://www.sqlite.org/lang_corefunc.html#coalesce) を参照してください。
