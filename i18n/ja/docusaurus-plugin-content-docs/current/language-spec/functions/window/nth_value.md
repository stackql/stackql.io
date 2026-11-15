---
title: NTH_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の NTH_VALUE ウィンドウ関数：ウィンドウフレームの N 番目の行に対する式の値。"
image: "/img/stackql-featured-image.png"
---
ウィンドウフレームの N 番目の行に対して評価した式の値を返します。

この組み込みウィンドウ関数は、集約ウィンドウ関数と同じ方法で各行のウィンドウフレームを計算します。ウィンドウフレームの N 行目に対して評価した式の値を返します。ウィンドウフレーム内の行には、`ORDER BY` 句がある場合はその句で定義された順序で、ない場合は任意の順序で、1 から番号が付けられます。パーティションに N 番目の行がない場合は、`NULL` を返します。

関連項目：
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` LAST_VALUE `]](/language-spec/functions/window/last_value)

* * *

## 構文 {#syntax}

```sql
SELECT NTH_VALUE(expr, N) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*expr*__
ウィンドウフレームの N 番目の行に対して評価する式。

__*N*__
取得する行番号を指定する正の整数（1 始まり）。

__*PARTITION BY column*__
省略可能。結果セットをパーティションに分割します。`NTH_VALUE` 関数は各パーティション内で個別に適用されます。

__*ORDER BY column*__
行を処理する順序を指定します。

__*ROWS BETWEEN frame_start AND frame_end*__
省略可能。ウィンドウフレームを定義します。

## 戻り値 {#return-values}

ウィンドウフレームの N 番目の行に対して評価した式の値を返します。N 番目の行が存在しない場合は `NULL` を返します。

* * *

## 例 {#examples}

### コントリビューション数が 2 番目に多いコントリビューターを取得する {#get-the-second-highest-contributor}

```sql
-- Get the top 3 contributors by name for comparison
SELECT
    login,
    contributions,
    NTH_VALUE(login, 1) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as top_contributor,
    NTH_VALUE(login, 2) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as second_contributor,
    NTH_VALUE(login, 3) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as third_contributor
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

詳細については、[https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) を参照してください。
