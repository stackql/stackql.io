---
title: IFNULL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の IFNULL 関数：COALESCE の 2 引数形式で、ちょうど 2 つの引数のうち NULL でない最初の引数を返します。"
image: "/img/stackql-featured-image.png"
---
ちょうど 2 つの引数を受け取り、`NULL` でない最初の引数を返します。`NULL` 値をデフォルト値で置き換えるために使用できます。  

> `IFNULL` 関数は、引数が 2 つだけの [`COALESCE`](/language-spec/functions/general/coalesce) 関数と同等です。[`COALESCE`](/language-spec/functions/general/coalesce) 関数は 3 つ以上の引数を取ることができます。

関連項目：  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## 構文 {#syntax}

```sql
SELECT IFNULL(expression1, expression2) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*expression*__  
`NULL` 値または `NULL` 以外の値に評価される、任意のデータ型の 2 つの式。

> 両方の *expression* 引数が `NULL` の場合、`IFNULL` 関数は `NULL` を返します。


## 戻り値 {#return-values}

2 つの *expression* 引数のうち、`NULL` でない最初の値を返します。

* * *

## 例 {#examples}

### `NULL` 値をデフォルト値で置き換える {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, ifnull(website, 'Not hosting a website') AS website 
FROM google.storage.buckets where project = 'stackql';
```

詳細については、[https://www.sqlite.org/lang_corefunc.html#ifnull](https://www.sqlite.org/lang_corefunc.html#ifnull) を参照してください。
