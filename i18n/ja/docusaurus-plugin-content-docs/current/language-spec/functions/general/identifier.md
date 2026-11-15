---
title: IDENTIFIER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - セッション変数
  - identifier
description: "StackQL の IDENTIFIER 関数：列名を保持するセッション変数を列参照に解決し、射影や ORDER BY で変数により列を指定できるようにします。"
image: "/img/stackql-featured-image.png"
---
文字列値（通常はセッション変数）を列名に解決します。そのままの `$variable` ではスカラー値として読み取られてしまう箇所で使用します。StackQL `v1.0` 以降で利用できます。

関連項目：  
[[` SET `]](/language-spec/set) [[ セッション変数 ]](/getting-started/session-variables)

* * * 

## 構文 {#syntax}

```sql
SELECT IDENTIFIER($variable) FROM <multipartIdentifier> ORDER BY IDENTIFIER($variable);
```

## 引数 {#arguments}

__*variable*__  
列名を保持するセッション変数または文字列リテラル。`t.name` のような修飾名も受け付けます。受け付ける引数は 1 つだけで、単一の文字列値である必要があります。`'t.' || $column_name` のような連結、2 つ目の引数、数値は `IDENTIFIER requires one string value` で拒否されます。空の値や文を含む値は `invalid identifier value` で拒否され、未定義の変数は未定義としてエラーになります。これらのチェックはすべて、プロバイダーへのリクエストが行われる前に実行されます。

## 戻り値 {#return-values}

解析済みの文の中で、この関数は指定された列への参照に置き換えられるため、その列の値を返します。名前は識別子に変換され、SQL テキストに直接挿入されることはありません。結果の列参照は、選択リスト、`WHERE`、`GROUP BY`、`ORDER BY` の各句、関数の引数、CTE やサブクエリの内部など、列を使用できる場所であればどこでも使用できます。

* * *

## 例 {#examples}

### 変数で指定した列を射影して並べ替える {#project-and-order-by-a-column-named-in-a-variable}

返す列と並べ替えに使う列はどちらも `column_name` から取得され、出力列の名前は `output_name` に従って変更されます。選択リストと `ORDER BY` で `IDENTIFIER` が必要なのは、これらの位置にあるそのままの `$column_name` は列ではなく文字列 `'name'` になるためです。エイリアスには代わりに二重引用符で囲む形式を使用します。変数が解決されると、この文は `SELECT name AS bucket FROM aws.s3.buckets WHERE region = 'ap-southeast-2' ORDER BY name` と同じになります。

```sql
SET column_name = 'name';
SET output_name = 'bucket';

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM aws.s3.buckets
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name);
```

### 変数で指定した列でフィルタリングする {#filter-on-a-column-named-in-a-variable}

解決された参照は、射影だけでなく述語でも機能します。この例では、フィルタリングに使うフィールドと照合する値の両方を変数から取得します。`IDENTIFIER($filter_column)` は列 `status` になり、`$filter_value` は文字列リテラル `'RUNNING'` として渡されます。前者は列を指定し、後者は値を渡すため、2 つの形式は意図的に異なります。

```sql
SET filter_column = 'status';
SET filter_value = 'RUNNING';

SELECT name, zone
FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a'
AND IDENTIFIER($filter_column) = $filter_value;
```

### テーブルエイリアスで列を修飾する {#qualify-the-column-with-a-table-alias}

ドットを含む値は、修飾された列参照に解決されます。テーブルには `w` というエイリアスが付けられ、そのエイリアス自体も変数から取得されます。`IDENTIFIER($column_name)` は `w.name` になります。結合の両側に同じ列名が存在し、そのままでは参照があいまいになる場合は、この形式を使用します。

```sql
SET table_alias = 'w';
SET column_name = 'w.name';

SELECT IDENTIFIER($column_name) AS bucket
FROM aws.s3.buckets AS "$table_alias"
WHERE w.region = 'ap-southeast-2';
```

### 拒否される値 {#rejected-values}

値はクエリテキストではなく名前になるため、名前として使用できないものは文の計画が作成される前に拒否され、プロバイダーにリクエストは送信されません。次の最初の 2 つの文は `invalid identifier value` で失敗し、3 つ目は `IDENTIFIER requires one string value` で失敗します。

```sql
/*
SET column_name = '';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 'name; DROP TABLE imaginary';
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with invalid identifier value

SET column_name = 17;
SELECT IDENTIFIER($column_name) FROM aws.s3.buckets;
-- FAILS with IDENTIFIER requires one string value
*/
```

> `FROM` 句の `$variable` は `IDENTIFIER` なしでリレーションに解決されます。また、二重引用符で囲んだ `"$variable"` は、エイリアス、DDL の列名、`INSERT` の列リスト、`UPDATE` の対象に使用します。
