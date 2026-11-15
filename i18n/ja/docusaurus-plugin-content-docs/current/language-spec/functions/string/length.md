---
title: LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の LENGTH 関数：文字列の文字数、または BLOB のバイト数。"
image: "/img/stackql-featured-image.png"
---
文字列の入力式に含まれる最初の `NULL` 文字より前の文字数、または BLOB 式のバイト数を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) 

* * * 

:::info

JSON 配列の長さを取得するには、[**JSON_ARRAY_LENGTH**](/language-spec/functions/json/json_array_length) 関数を使用します。

:::

## 構文 {#syntax}

```sql
SELECT LENGTH(expression) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*expression*__  
ASCII 文字列または BLOB です。

> *expression* が `NULL` の場合は `NULL` を返します。

> *expression* が数値の場合は、*expression* の文字列表現の長さを返します。

## 戻り値 {#return-values}
整数を返します。

* * *

## 例 {#examples}

### 文字列型の列の長さを返す {#return-the-length-of-a-string-column}

```sql
SELECT name, length(name) AS len
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

詳細については、[https://www.sqlite.org/lang_corefunc.html#length](https://www.sqlite.org/lang_corefunc.html#length) を参照してください。
