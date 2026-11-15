---
title: LAG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の LAG ウィンドウ関数：パーティション内の前の行に対して式を評価します（オフセットとデフォルト値は省略可能）。"
image: "/img/stackql-featured-image.png"
---
パーティション内の前の行に対して式を評価した結果を返します。

`LAG()` の 1 つ目の形式は、パーティション内の直前の行に対して式を評価した結果を返します。直前の行がない場合（現在の行が最初の行である場合）は、`NULL` を返します。

オフセット引数を指定する場合は、非負の整数である必要があります。返される値は、パーティション内で現在の行より *offset* 行前の行に対して式を評価した結果です。オフセットが 0 の場合、式は現在の行に対して評価されます。現在の行より *offset* 行前の行がない場合は、`NULL` を返します。

デフォルト値も指定した場合、オフセットで特定される行が存在しないときは、`NULL` の代わりにその値を返します。

関連項目：
[[` SELECT `]](/language-spec/select) [[` LEAD `]](/language-spec/functions/window/lead)

* * *

## 構文 {#syntax}

```sql
SELECT LAG(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*expr*__
前の行に対して評価する式。

__*offset*__
省略可能。何行前を参照するかを指定する非負の整数。デフォルトは 1 です。

__*default*__
省略可能。オフセットの行が存在しない場合に返す値。デフォルトは `NULL` です。

__*PARTITION BY column*__
省略可能。結果セットをパーティションに分割します。`LAG` 関数は各パーティション内で個別に適用されます。

__*ORDER BY column*__
行を処理する順序を指定します。

## 戻り値 {#return-values}

指定した前の行に対して評価した式の値を返します。そのような行が存在しない場合は `NULL`（またはデフォルト値）を返します。

* * *

## 例 {#examples}

### 各リリースを前のリリースと比較する {#compare-each-release-to-the-previous-release}

```sql
-- Compare each release to previous and next release dates
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LAG(published_at, 1) OVER (ORDER BY published_at) as previous_release_date,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### Issue の作成間隔を分析する {#analyze-gaps-between-issue-creation}

```sql
-- Analyze gaps between issue creation
SELECT
    number,
    title,
    created_at,
    LAG(created_at, 1) OVER (ORDER BY created_at) as prev_issue_date,
    julianday(created_at) - julianday(LAG(created_at, 1) OVER (ORDER BY created_at)) as days_since_prev_issue
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

詳細については、[https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) を参照してください。
