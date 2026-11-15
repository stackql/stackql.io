---
title: CUME_DIST
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の CUME_DIST ウィンドウ関数：パーティション内での現在の行の累積分布（行番号をパーティションの行数で割った値）。"
image: "/img/stackql-featured-image.png"
---
パーティション内での値の累積分布を返します。この値は `row-number / partition-rows` として計算されます。ここで、`row-number` はグループ内の最後のピアに対して `ROW_NUMBER()` が返す値、`partition-rows` はパーティション内の行数です。

関連項目：
[[` SELECT `]](/language-spec/select) [[` PERCENT_RANK `]](/language-spec/functions/window/percent_rank) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## 構文 {#syntax}

```sql
SELECT CUME_DIST() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*PARTITION BY column*__
省略可能。結果セットをパーティションに分割します。`CUME_DIST` 関数は各パーティションに個別に適用されます。

__*ORDER BY column*__
累積分布を計算する順序を指定します。

## 戻り値 {#return-values}

累積分布の値を表す 0.0 から 1.0 までの浮動小数点数を返します。

* * *

## 例 {#examples}

### コントリビューターの累積分布を計算する {#calculate-cumulative-distribution-of-contributors}

```sql
-- Calculate percentile ranking of contributors
SELECT
    login,
    contributions,
    PERCENT_RANK() OVER (ORDER BY contributions) as percentile,
    CUME_DIST() OVER (ORDER BY contributions) as cumulative_dist
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY contributions DESC;
```

:::tip

`CUME_DIST` は、現在の行の値以下の値を持つ行の割合を返します。値が 0.75 であれば、75% の行が現在の行以下の値を持つことを意味します。

:::

詳細については、[https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) を参照してください。
