---
title: LEAD
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の LEAD ウィンドウ関数：パーティション内の後続の行に対して式を評価します（オフセットとデフォルト値は省略可能）。"
image: "/img/stackql-featured-image.png"
---
パーティション内の後続の行に対して式を評価した結果を返します。

`LEAD()` の 1 つ目の形式は、パーティション内の直後の行に対して式を評価した結果を返します。直後の行がない場合（現在の行が最後の行である場合）は、`NULL` を返します。

オフセット引数を指定する場合は、非負の整数である必要があります。返される値は、パーティション内で現在の行より *offset* 行後の行に対して式を評価した結果です。オフセットが 0 の場合、式は現在の行に対して評価されます。現在の行より *offset* 行後の行がない場合は、`NULL` を返します。

デフォルト値も指定した場合、オフセットで特定される行が存在しないときは、`NULL` の代わりにその値を返します。

関連項目：
[[` SELECT `]](/language-spec/select) [[` LAG `]](/language-spec/functions/window/lag)

* * *

## 構文 {#syntax}

```sql
SELECT LEAD(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LEAD(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*expr*__
後続の行に対して評価する式。

__*offset*__
省略可能。何行先を参照するかを指定する非負の整数。デフォルトは 1 です。

__*default*__
省略可能。オフセットの行が存在しない場合に返す値。デフォルトは `NULL` です。

__*PARTITION BY column*__
省略可能。結果セットをパーティションに分割します。`LEAD` 関数は各パーティション内で個別に適用されます。

__*ORDER BY column*__
行を処理する順序を指定します。

## 戻り値 {#return-values}

指定した後続の行に対して評価した式の値を返します。そのような行が存在しない場合は `NULL`（またはデフォルト値）を返します。

* * *

## 例 {#examples}

### 次のリリースを先読みする {#look-ahead-to-the-next-release}

```sql
-- Compare each release to previous and next release dates
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LEAD(tag_name, 1) OVER (ORDER BY published_at) as next_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

詳細については、[https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) を参照してください。
