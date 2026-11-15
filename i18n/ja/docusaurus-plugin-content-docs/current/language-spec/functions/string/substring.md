---
title: SUBSTRING
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の SUBSTRING 関数（エイリアス SUBSTR）：開始位置と長さを指定して、文字列またはバイナリ値の一部を返します。"
image: "/img/stackql-featured-image.png"
---
文字式またはバイナリ式の一部を返します。`SUBSTR` は `SUBSTRING` のエイリアスです。

関連項目：  
[[` SELECT `]](/language-spec/select) [[ データ型 ]](/language-spec/data-types)

* * * 

:::info

JSON オブジェクトからフィールドを抽出するには、[**JSON_EXTRACT**](/language-spec/functions/json/json_extract) 関数を使用します。

:::

## 構文 {#syntax}

```sql
SELECT SUBSTRING(expression, start[, length]) FROM <multipartIdentifier>;
SELECT SUBSTR(expression, start[, length]) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*expression*__  
ASCII 文字列または BLOB です。

> *expression* が文字列の場合、インデックスは実際の UTF-8 文字を指します。*expression* が BLOB の場合、インデックスはバイトを指します。

__*start*__  
返す部分シーケンスの、入力式における最初の文字またはバイトを表す整数です。*start* が負の場合、部分文字列の最初の文字は左からではなく右から数えて決まります。

> *expression* の左端の文字が 1 番目です。

__*length*__  
省略可能な整数値で、*start* の値から返す部分シーケンスの長さを表します。*length* を省略すると、*expression* で指定した文字列または BLOB の末尾までのすべての文字が返されます。

> *length* が負の場合は、*length* の絶対値が使用されます。

## 戻り値 {#return-values}

*expression* の型に応じて、文字列または BLOB の値を返します。

* * *

## 例 {#examples}

### 列から先頭の *n* 文字を返す {#return-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, 1, 5) as "first five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 列から先頭の *n* 文字を切り捨てる {#truncate-the-first-n-characters-from-a-column}

```sql
SELECT SUBSTR(name, 5) as "from char 5" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```
### 列から末尾の *n* 文字を返す {#return-the-last-n-characters-from-a-column}

```sql
SELECT SUBSTRING(name, -5) as "last five chars" 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

詳細については、[https://www.sqlite.org/lang_corefunc.html#substr](https://www.sqlite.org/lang_corefunc.html#substr) を参照してください。
