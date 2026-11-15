---
title: LOWER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の LOWER 関数：文字列を小文字に変換します。"
image: "/img/stackql-featured-image.png"
---
入力文字列を小文字で返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) [[` UPPER `]](/language-spec/functions/string/upper) [[ データ型 ]](/language-spec/data-types)

* * * 

## 構文 {#syntax}

```sql
SELECT LOWER(character_expression) FROM <multipartIdentifier>;
```
> `lower()` 関数は ASCII 文字に対してのみ機能します。

## 引数 {#arguments}

__*character_expression*__  
ASCII 文字列です。

## 戻り値 {#return-values}
文字列値を返します。

* * *

## 例 {#examples}

### 文字列を小文字で返す {#return-a-string-in-lowercase}

```sql
SELECT name, LOWER(location) FROM google.storage.buckets WHERE project = 'stackql-demo';
```

詳細については、[https://www.sqlite.org/lang_corefunc.html#lower](https://www.sqlite.org/lang_corefunc.html#lower) を参照してください。
