---
title: ROUND
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の ROUND 関数：数値引数を指定した小数点以下の桁数に丸めます（デフォルトは 0）。"
image: "/img/stackql-featured-image.png"
---
数値引数を丸めます。  

関連項目：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 構文 {#syntax}

```sql
SELECT ROUND(numeric_expression [, precision]) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*numeric_expression*__  
真数値または概数値のデータ型カテゴリに属する式。

__*precision*__  
*numeric_expression* を丸める小数点以下の桁数。

> *precision* 引数を省略した場合は 0 とみなされます。

## 戻り値 {#return-values}
数値を返します。

* * *

## 例 {#examples}

### 浮動小数点数を小数点以下 2 桁に丸める {#round-a-floating-point-number-to-two-decimal-places}

```sql
SELECT name, timeCreated,
round(julianday('now')-julianday(timeCreated), 2) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
詳細については、[https://www.sqlite.org/lang_corefunc.html#round](https://www.sqlite.org/lang_corefunc.html#round) を参照してください。
