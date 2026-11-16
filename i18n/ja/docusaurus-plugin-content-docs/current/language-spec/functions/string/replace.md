---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の REPLACE 関数：文字列内に出現するすべての部分文字列を別の文字列に置き換えます。"
image: "/img/stackql-featured-image.png"
---
文字列内で部分文字列が出現するすべての箇所を別の文字列に置き換えた、文字列のコピーを返します。

関連項目：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 構文 {#syntax}

```sql
SELECT REPLACE(character_expression, pattern, replacement) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*character_expression*__  
置換を実行する文字列リテラルまたは文字列型の列です。

__*pattern*__  
元の文字列内で検索する部分文字列です。

> *pattern* が空文字列の場合、または *character_expression* 内に見つからない場合、*character_expression* は変更されずに返されます。 

__*replacement*__  
置換文字列です。

> *replacement* が元々文字列でない場合は、処理の前に UTF-8 文字列にキャストされます。

> 比較には `BINARY` 照合順序が使用されます。

## 戻り値 {#return-values}
文字列を返します。

* * *

## 例 {#examples}

### 列内の文字列パターンを置換する {#replace-a-string-pattern-in-a-column}

```sql
SELECT replace(name, 'prd', 'production') AS replacement_string
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

詳細については、[https://www.sqlite.org/lang_corefunc.html#replace](https://www.sqlite.org/lang_corefunc.html#replace) を参照してください。
