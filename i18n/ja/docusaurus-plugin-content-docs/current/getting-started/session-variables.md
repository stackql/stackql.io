---
title: セッション変数
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - セッション変数
  - set
  - unset
  - identifier
description: "StackQL のセッション変数：SET で型付きのスカラー値を設定し、述語や式で $name により参照し、IDENTIFIER でリレーション名や列名として使用します。"
image: "/img/stackql-featured-image.png"
---

関連項目：  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ プロバイダーセッション ]](/getting-started/provider-sessions) [[ 変数の使用 ]](/getting-started/variables)

セッション変数は、現在のセッションが保持するスカラー値で、後続の文から参照されます。StackQL `v1.0` 以降で利用でき、対話型シェル、`exec` スクリプト、PostgreSQL ワイヤプロトコル、MCP サーバーで動作します。

セッション変数は、[変数の使用](/getting-started/variables)で説明するテンプレート変数とは異なります。テンプレート変数は `--iqldata` と `--var` を通じて `stackql exec` に渡され、スクリプトのテキストが解析される前にそのテキストにレンダリングされます。セッション変数は解析済みの文の内部で解決され、型を保持し、文と文の間で変更できます。

## 変数の設定と削除 {#setting-and-removing-variables}

[`SET`](/language-spec/set) は、文字列、数値、ブール値、または `null` を割り当てます。[`UNSET`](/language-spec/unset) は変数を削除します。`SET name = DEFAULT` も同じ意味です。明示的な `null` は格納された値であり、リセットではありません。

```sql
SET region = 'ap-southeast-2';
SET minimum_count = 3;
SET enabled = true;
SET optional_value = null;
UNSET region;
```

配列とオブジェクトはスカラー値ではありません。JSON ドキュメントを保持するには、文字列として格納します。

```sql
SET "providers.saved" = '{"provider":"aws","tenancy":"production"}';
```

## 変数の参照 {#dereferencing-a-variable}

式を置ける任意の位置で、引用符なしの `$name` を使用します。文字列は SQL リテラルとしてエスケープされ、数値、ブール値、`null` は型を保持します。単一引用符で囲んだ `'$name'` はリテラルテキストの `$name` であり、変数の参照ではありません。存在しない変数への参照は、プロバイダーへのリクエストが行われる前に失敗します。

解決された値は、`CAST`、`CONVERT`、`||` による連結、関数呼び出しなど、通常の SQL 式で使用できます。

```sql
SET some_id = 42;
SET suffix = 'East';

SELECT CAST($some_id AS TEXT) AS id_text,
       CONVERT($some_id, TEXT) || '-' || lower($suffix) AS label,
       md5(lower(CONVERT($some_id, TEXT) || '-' || $suffix)) AS digest,
       '$some_id' AS literal_text;
```

割り当てで指定できるのは、スカラーリテラルまたは直接参照のみです。変換やその他の式は、`SET` 文ではなく、変数を使用する箇所で適用してください。

## 述語内の変数 {#variables-in-predicates}

`WHERE` 句の変数は文の計画が作成される前に解決されるため、リテラルとまったく同じようにプロバイダー API にルーティングされます。そのため、必須パラメーターを変数から指定できます。

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';

SELECT name, status
FROM google.compute.instances
WHERE project = $project AND zone = $zone;
```

型付きの値は、その型で比較されます。`SET enabled = false` を設定した状態の `WHERE active = $enabled` は、ブール値でフィルタリングします。プロバイダーの列が文字列で変数が数値の場合は、`WHERE id = CAST($some_id AS TEXT)` の形式を使用します。

## オブジェクト名としての変数 {#variables-as-object-names}

変数は、値だけでなくリレーションや列の名前も指定できます。名前は解析済みの文の中で識別子に変換され、SQL テキストに差し込まれることはありません。そのため、文を含む値、空の値、文字列でない値は、プロバイダーへのアクセスの前に拒否されます。

| 位置 | 形式 |
|--|--|
| `FROM` 句 | `FROM $relation_name`、またはコンポーネントごとに `FROM $provider.$service.$resource` |
| 式の中の列 | `IDENTIFIER($column_name)`（[`IDENTIFIER`](/language-spec/functions/general/identifier) を参照） |
| 列またはテーブルのエイリアス | `AS "$alias_name"` |
| `ORDER BY` | `ORDER BY IDENTIFIER($column_name)` |
| `LIMIT` | `LIMIT $row_count` |
| DDL の列名、`INSERT` の列リスト、`UPDATE` の対象 | 二重引用符で囲んだ `"$name"` 形式 |
<br/>

```sql
SET relation_name = 'aws.s3.buckets';
SET column_name = 'name';
SET output_name = 'bucket';
SET row_count = 10;

SELECT IDENTIFIER($column_name) AS "$output_name"
FROM $relation_name
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name)
LIMIT $row_count;
```

結合、CTE、サブクエリも、同じ方法でリレーション変数を解決します。ドット区切りのリレーション値は、最大 4 つのコンポーネントで構成されます。変数は任意の SQL の断片ではありません。キーワード、カンマ区切りの列リスト、`WHERE` 句全体は置換できません。

## スコープと分離 {#scope-and-isolation}

変数は、それを設定したセッションに属します。`stackql srv` サーバーへの別々の接続は、それぞれ別の変数を持ちます。別々の `exec` 呼び出しも同様です。MCP リクエストはそれぞれが独立したセッションであるため、`SET` とそれを使用するクエリは同じ文のバッチで送信する必要があります。新しいセッションは、変数がない状態で開始します。

スコープはセッションのみです。`SET SESSION` と `UNSET SESSION` も受け付けられ、`SET` と `UNSET` とまったく同じ意味を持ちます。このキーワードは、セッション設定とグローバル設定が異なるデータベースと同じように読めるスクリプトのために用意されています。セッション間で共有される状態はないため、`GLOBAL` は拒否されます。

プリペアド文は、準備時ではなく実行時に変数を解決します。また、バインドされたセッション値を含む計画は、実行をまたいで再利用されません。

[アクセス管理](/command-line-usage/access-management)を有効にしている場合、変数の読み取りには `session/variables:read` が、変数の設定またはリセットには `session/config:write` が必要です。変数をオブジェクト名に解決しても、そのオブジェクトに対する権限は付与されません。解決されたリレーションは、通常どおり実行前に認可されます。
