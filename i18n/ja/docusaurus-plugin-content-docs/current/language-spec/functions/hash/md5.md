---
title: MD5
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - md5
  - ハッシュ
  - ダイジェスト
description: "StackQL の MD5 関数：セキュリティ用途ではなく、フィンガープリントや変更検出のために文字列の MD5 ダイジェストを小文字の 16 進数で返します。"
image: "/img/stackql-featured-image.png"
---
文字列の MD5 ダイジェストを 32 文字の小文字の 16 進数として返します。StackQL `v1.0` 以降で利用できます。

関連項目：  
[[` SELECT `]](/language-spec/select) [[ 連結 ]](/language-spec/functions/string/concatenation)

* * * 

## 構文 {#syntax}

```sql
SELECT MD5(expression) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*expression*__  
文字列、またはテキストに型変換される値。引数が `NULL` の場合は `NULL` を返します。

## 戻り値 {#return-values}

32 文字の小文字の 16 進数文字列を返します。

> `MD5` はフィンガープリントと変更検出のために提供されています。パスワード用途やセキュリティプリミティブではありません。PostgreSQL バックエンドでは、データベース自身の `md5` 関数が使用されます。

* * *

## 例 {#examples}

### 派生値のフィンガープリントを取得する {#fingerprint-a-derived-value}

変数は型付きの値として置換され、整数はテキストに変換されてサフィックスと連結され、小文字に変換した結果に対してダイジェストが計算されます。同じ入力からは常に同じ 32 文字の 16 進数文字列が生成されるため、この値は安定したキーとして使用できます。

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest;
```

### 構成の変更を検出する {#detect-configuration-changes}

シリアル化された JSON 列をハッシュ化すると、保存して実行間で比較できる短い値が得られます。ダイジェストが前回の実行と異なるバケットは IAM 構成が変更されており、ドキュメント全体を保存したり差分を取ったりする必要はありません。

```sql
SELECT name, md5(json_extract(iamConfiguration, '$')) AS iam_digest
FROM google.storage.buckets
WHERE project = 'stackql-demo';
```
