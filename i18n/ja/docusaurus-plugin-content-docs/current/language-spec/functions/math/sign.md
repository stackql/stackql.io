---
title: SIGN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の SIGN 関数：数値引数が負、ゼロ、正のいずれであるかに応じて -1、0、1 を返します。"
image: "/img/stackql-featured-image.png"
---
入力値が負の場合は -1、入力値が 0 の場合は 0、入力値が正の場合は 1 を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 構文 {#syntax}

```sql
SELECT SIGN(numeric_expression) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*numeric_expression*__  
真数値または概数値のデータ型カテゴリに属する式。

> *numeric_expression* が `NULL` の場合、または数値に損失なく変換できない `string` か BLOB の場合、関数は `NULL` を返します。

## 戻り値 {#return-values}

`-1`、`0`、`1` のいずれかを返します。

* * *

## 例 {#examples}

### 数値の符号を判定する {#determine-the-sign-of-a-number}

```sql
SELECT name, sign(strftime('%s', creationTimestamp)-strftime('%s','now')) as sign
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

詳細については、[https://www.sqlite.org/lang_corefunc.html#sign](https://www.sqlite.org/lang_corefunc.html#sign) を参照してください。
