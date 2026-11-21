---
title: ABS
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の ABS 関数：数値引数の絶対値。"
image: "/img/stackql-featured-image.png"
---
数値引数の絶対値を返します。  

関連項目：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 構文 {#syntax}

```sql
SELECT ABS(numeric_expression) FROM <multipartIdentifier>;
```

> `X` が `NULL` の場合、`abs(X)` は `NULL` を返します。`X` が数値に変換できない文字列または blob の場合、`abs(X)` は `0.0` を返します。`X` が整数 -9223372036854775808 の場合、これに相当する正の 64 ビットの 2 の補数値が存在しないため、`abs(X)` は整数オーバーフローエラーを発生させます。

## 引数 {#arguments}

__*numeric_expression*__  
真数値または概数値のデータ型カテゴリに属する式。

## 戻り値 {#return-values}

負でない数値を返します。

* * *

## 例 {#examples}

### 数値の絶対値を返す {#return-the-absolute-value-of-a-number}

```sql
SELECT name, abs(strftime('%s', creationTimestamp)-strftime('%s','now')) as abs_value
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

詳細については、[https://www.sqlite.org/lang_corefunc.html#abs](https://www.sqlite.org/lang_corefunc.html#abs) を参照してください。
