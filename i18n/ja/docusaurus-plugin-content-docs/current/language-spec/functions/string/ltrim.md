---
title: LTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の LTRIM 関数：文字列の左側から空白文字、または指定した文字の集合を削除します。"
image: "/img/stackql-featured-image.png"
---
入力文字列の左側から、空白文字または指定した文字を削除した文字列を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[` RTRIM `]](/language-spec/functions/string/rtrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## 構文 {#syntax}

```sql
SELECT LTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*character_expression*__  
文字列リテラルまたは文字列型の列です。

__*character*__  
省略可能な引数で、*character_expression* の先頭にある場合に削除する文字です。

## 戻り値 {#return-values}
文字列を返します。

* * *

## 例 {#examples}

### 文字列型の列から先頭の空白文字を削除する {#remove-leading-whitespace-from-a-string-column}

```sql
SELECT ltrim(description) AS leading_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

詳細については、[https://www.sqlite.org/lang_corefunc.html#ltrim](https://www.sqlite.org/lang_corefunc.html#ltrim) を参照してください。
