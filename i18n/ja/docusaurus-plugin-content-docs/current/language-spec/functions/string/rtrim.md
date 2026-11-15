---
title: RTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の RTRIM 関数：文字列の右側からスペース、または指定した文字の集合に含まれる文字を削除します。"
image: "/img/stackql-featured-image.png"
---
入力文字列の右側から、スペース、または指定した文字の集合に含まれる文字を削除した文字列を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## 構文 {#syntax}

```sql
SELECT RTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*character_expression*__  
文字列リテラルまたは文字列型の列です。

__*character*__  
省略可能な文字列です。この文字列に含まれるすべての文字が *character_expression* の末尾から削除されます。省略した場合はスペースが削除されます（タブや改行は削除されません）。

## 戻り値 {#return-values}
文字列を返します。

* * *

## 例 {#examples}

### 文字列型の列から末尾のスペースを削除する {#remove-trailing-spaces-from-a-string-column}

```sql
SELECT rtrim(description) AS trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

詳細については、[https://www.sqlite.org/lang_corefunc.html#rtrim](https://www.sqlite.org/lang_corefunc.html#rtrim) を参照してください。
