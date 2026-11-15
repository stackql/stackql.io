---
title: NTILE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の NTILE ウィンドウ関数：パーティションをできるだけ均等に N 個のグループに分割し、各行のグループに 1 から N までの番号を付けます。"
image: "/img/stackql-featured-image.png"
---
パーティションをできるだけ均等に N 個のグループに分割し、`ORDER BY` 句で定義された順序で（ない場合は任意の順序で）、各グループに 1 から N までの整数を割り当てます。必要な場合は、大きいグループが先になります。

関連項目：
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## 構文 {#syntax}

```sql
SELECT NTILE(N) OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*N*__
パーティションを分割するグループ（タイル）の数を指定する正の整数。

__*PARTITION BY column*__
省略可能。結果セットをパーティションに分割します。`NTILE` 関数は各パーティションに個別に適用されます。

__*ORDER BY column*__
行をタイルに割り当てる順序を指定します。

## 戻り値 {#return-values}

現在の行が属するタイル（グループ）を示す 1 から N までの整数を返します。

* * *

## 例 {#examples}

### コントリビューション数でコントリビューターを四分位に分ける {#group-contributors-into-quartiles-by-contribution-level}

```sql
-- Group contributors into quartiles by contribution level
SELECT
    login,
    contributions,
    NTILE(4) OVER (ORDER BY contributions DESC) as contribution_quartile,
    CASE NTILE(4) OVER (ORDER BY contributions DESC)
        WHEN 1 THEN 'Top Contributors'
        WHEN 2 THEN 'Active Contributors'
        WHEN 3 THEN 'Moderate Contributors'
        WHEN 4 THEN 'Occasional Contributors'
    END as contributor_tier
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

`NTILE` を使用すると、データにセグメントや階層を作成できます。主な用途として、データを四分位（N=4）、十分位（N=10）、または百分位（N=100）に分割することが挙げられます。

:::

詳細については、[https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) を参照してください。
