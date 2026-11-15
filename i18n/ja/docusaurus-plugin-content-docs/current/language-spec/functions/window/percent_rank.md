---
title: PERCENT_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の PERCENT_RANK ウィンドウ関数：パーティション内での現在の行の相対順位を表す 0.0 から 1.0 までの値。"
image: "/img/stackql-featured-image.png"
---
パーティション内での現在の行の相対順位を表す 0.0 から 1.0 までの値を返します。

名前とは異なり、この関数は `(rank - 1) / (partition-rows - 1)` に等しい値を返します。ここで、`rank` は組み込みウィンドウ関数 `RANK()` が返す値、`partition-rows` はパーティション内の行の総数です。パーティションに行が 1 つしかない場合、この関数は 0.0 を返します。

関連項目：
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` CUME_DIST `]](/language-spec/functions/window/cume_dist)

* * *

## 構文 {#syntax}

```sql
SELECT PERCENT_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*PARTITION BY column*__
省略可能。結果セットをパーティションに分割します。`PERCENT_RANK` 関数は各パーティションに個別に適用されます。

__*ORDER BY column*__
パーセンタイル順位を計算する順序を指定します。

## 戻り値 {#return-values}

パーセンタイル順位を表す 0.0 から 1.0 までの浮動小数点数を返します。

* * *

## 例 {#examples}

### コントリビューターのパーセンタイル順位を計算する {#calculate-percentile-ranking-of-contributors}

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

`PERCENT_RANK` の値 0.0 は順位が最も低い行を示し、1.0 は順位が最も高い行を示します。この関数を使用すると、特定の値が分布のどこに位置するかを特定できます。

:::

詳細については、[https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) を参照してください。
