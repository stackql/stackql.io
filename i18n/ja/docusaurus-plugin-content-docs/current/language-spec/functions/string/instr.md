---
title: INSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の INSTR 関数：ある文字列内で別の文字列が最初に出現する位置（1 から数える）、見つからない場合は 0。"
image: "/img/stackql-featured-image.png"
---
別の文字列内で文字列が最初に出現する文字位置を返します。検索する文字列が見つからない場合は 0 を返します。

関連項目：  
[[` SELECT `]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT INSTR(string, searchstring) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*string*__  
検索の対象となるソース文字列です。

__*searchstring*__  
検索する部分文字列です。

> *string* と *searchstring* がどちらも BLOB の場合、`instr(<string>, <searchstring>)` は *searchstring* が最初に出現するまでのバイト数に 1 を加えた値を返し、*searchstring* が *string* 内のどこにも出現しない場合は 0 を返します。両方の引数が `NULL` でも BLOB でもない場合、両方とも文字列として解釈されます。 

## 戻り値 {#return-values}
整数を返します。

> *string* または *searchstring* のいずれかの値が `NULL` の場合、結果は `NULL` です。

* * *

## 例 {#examples}

### フィールド内で文字列を検索する {#search-a-field-for-a-string}

```sql
SELECT INSTR(name, 'worker') as charpos
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/*
returns 0 if the term 'worker' is not present in the 'name' column, otherwise returns the character position (starting from 1) of the first occurrence of the term 'worker' in the 'name' column
*/
```

詳細については、[https://www.sqlite.org/lang_corefunc.html#instr](https://www.sqlite.org/lang_corefunc.html#instr) を参照してください。
