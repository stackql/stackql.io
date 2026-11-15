---
title: TRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の TRIM 関数：文字列の両端からスペース、または指定した文字の集合に含まれる文字を削除します。"
image: "/img/stackql-featured-image.png"
---
入力文字列の両端から、スペース、または指定した文字の集合に含まれる文字を削除した文字列を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` RTRIM `]](/language-spec/functions/string/rtrim) 

* * * 

## 構文 {#syntax}

```sql
SELECT TRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*character_expression*__  
文字列リテラルまたは文字列型の列です。

__*character*__  
省略可能な文字列です。この文字列に含まれるすべての文字が *character_expression* の先頭と末尾から削除されます。省略した場合はスペースが削除されます（タブや改行は削除されません）。

## 戻り値 {#return-values}
文字列を返します。

* * *

## 例 {#examples}

### 文字列型の列から先頭と末尾のスペースを削除する {#remove-leading-and-trailing-spaces-from-a-string-column}

```sql
SELECT trim(description) AS leading_and_trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

詳細については、[https://www.sqlite.org/lang_corefunc.html#trim](https://www.sqlite.org/lang_corefunc.html#trim) を参照してください。
