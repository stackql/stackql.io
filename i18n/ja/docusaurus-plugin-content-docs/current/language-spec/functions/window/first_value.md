---
title: FIRST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の FIRST_VALUE ウィンドウ関数：ウィンドウフレームの最初の行に対する式の値。"
image: "/img/stackql-featured-image.png"
---
ウィンドウフレームの最初の行に対して評価した式の値を返します。

この組み込みウィンドウ関数は、集約ウィンドウ関数と同じ方法で各行のウィンドウフレームを計算します。各行について、ウィンドウフレームの最初の行に対して評価した式の値を返します。

関連項目：
[[` SELECT `]](/language-spec/select) [[` LAST_VALUE `]](/language-spec/functions/window/last_value) [[` NTH_VALUE `]](/language-spec/functions/window/nth_value)

* * *

## 構文 {#syntax}

```sql
SELECT FIRST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*expr*__
ウィンドウフレームの最初の行に対して評価する式。

__*PARTITION BY column*__
省略可能。結果セットをパーティションに分割します。`FIRST_VALUE` 関数は各パーティション内で個別に適用されます。

__*ORDER BY column*__
行を処理する順序を指定します。

__*ROWS BETWEEN frame_start AND frame_end*__
省略可能。ウィンドウフレームを定義します。主なオプションは次のとおりです。
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

フレーム句を省略した場合、フレームは `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW` になります。このフレームの終端は現在の行ではなく、`ORDER BY` の列で現在の行と同順位になる最後の行です。

## 戻り値 {#return-values}

ウィンドウフレームの最初の行に対して評価した式の値を返します。

* * *

## 例 {#examples}

### 参考として最初のリリースを取得する {#find-the-first-release-for-context}

```sql
-- Find first and latest release for context
SELECT
    tag_name,
    name,
    published_at,
    FIRST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as first_release,
    LAST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as latest_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

デフォルトでは、ウィンドウフレームはパーティションの先頭から、`ORDER BY` 列で現在の行と同順位になる最後の行までです。`FIRST_VALUE` はフレームの最初の行を読み取るため、デフォルトのフレームでも `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING` でも同じ値を返します。明示的なフレームが意味を持つのは、フレームのさらに先まで読み取る `LAST_VALUE` と `NTH_VALUE` です。

:::

詳細については、[https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) を参照してください。
