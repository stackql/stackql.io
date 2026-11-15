---
title: JSON_GROUP_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: StackQL で SQL を使ってキーと値のペアを JSON オブジェクトに集約します。
image: "/img/stackql-featured-image.png"
---
キーと値のペアを、指定した列でグループ化して JSON オブジェクトに集約します。`JSON_GROUP_OBJECT()` は、指定した列のすべてのキーと値のペアをグループごとに収集し、グループごとに 1 つの JSON オブジェクトにまとめます。

関連項目：  
[[`SELECT`]](/language-spec/select)

* * * 

## 構文 {#syntax}

```sql
SELECT JSON_GROUP_OBJECT(key_column, value_column) FROM <multipartIdentifier>
WHERE condition
GROUP BY grouping_column;
```

## 引数 {#arguments}

__*key_column*__  
JSON オブジェクトのキーを提供する列。

__*value_column*__  
JSON オブジェクトの値を提供する列。

__*grouping_column*__  
結果をグループ化する基準となる列。

## 戻り値 {#return-values}
指定した列から集約したすべてのキーと値のペアを含む JSON オブジェクトを、`grouping_column` に従ってグループ化して返します。

* * *

## 例 {#examples}

### プログラミング言語ごとにリポジトリサイズを集約する {#aggregate-repository-sizes-by-programming-language}

```sql
-- Example: Group repository names with their corresponding sizes by programming language
SELECT
  language, 
  JSON_GROUP_OBJECT(name, size) AS language_index
FROM
  github.repos.repos
WHERE
  org = 'stackql'
  AND language IS NOT NULL
GROUP BY language;
```

このクエリは、プログラミング言語ごとに JSON オブジェクトを返します。各オブジェクトには、リポジトリ名とそれに対応するサイズがキーと値のペアとして含まれます。想定される出力の例は次のとおりです：

```plaintext
|-------------|-----------------------------------------------------------------------------------|
| language    | language_index                                                                    |
|-------------|-----------------------------------------------------------------------------------|
| C           | {"go-sqlite3":35667,"data-encryption-spike":50746}                                |
|-------------|-----------------------------------------------------------------------------------|
| C++         | {"expt-any-sdk":2399,"isqlang":9}                                                 |
|-------------|-----------------------------------------------------------------------------------|
| Dockerfile  | {"stackql-compliance-checks-devel":8}                                             |
|-------------|-----------------------------------------------------------------------------------|
| Go          | {"stackql-provider-registry":115711, ...}                                         |
|-------------|-----------------------------------------------------------------------------------|
```

この出力は、組織内のリポジトリサイズがプログラミング言語ごとにどう分布しているかを可視化するのに役立ちます。言語別にグループ化された、明確で整理されたデータが得られます。
