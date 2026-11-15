---
title: TRUNC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の TRUNC 関数：0 方向に丸めて小数部分を切り捨てた、数値の整数部分。"
image: "/img/stackql-featured-image.png"
---

0 方向に丸めて小数部分を取り除き、数値の整数部分を返します。

関連項目：  
[[`SELECT`]](/language-spec/select) 

* * * 

## 構文 {#syntax}

```sql
SELECT TRUNC(numeric_expression) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*numeric_expression*__  
真数値または概数値のデータ型カテゴリに属する式です。

## 戻り値 {#return-values}
整数部分に切り捨てた数値を返します。

* * *

## 例 {#examples}

### Google Cloud Storage バケットの作成からの経過日数を小数点以下 2 桁に切り捨てる {#truncate-the-number-of-days-since-creation-of-google-cloud-storage-buckets-to-two-decimal-places}

```sql
SELECT name, timeCreated,
TRUNC(julianday('now') - julianday(timeCreated)) as days_since
FROM google.storage.buckets WHERE project = 'stackql';
```
