---
title: DENSE_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の DENSE_RANK ウィンドウ関数：パーティション内での現在の行の欠番のない順位で、同順位の行は同じ順位を共有し、次の順位は間を空けずに続きます。"
image: "/img/stackql-featured-image.png"
---
現在の行の順位を欠番なしで返します。これは、パーティション内での現在の行のピアグループの番号です。並べ替え列の値が等しい行には同じ順位が割り当てられ、次の順位の値は常に 1 ずつ増加します（欠番なし）。

行には、ウィンドウ定義の `ORDER BY` 句で定義された順序で 1 から番号が付けられます。`ORDER BY` 句がない場合、すべての行がピアと見なされ、この関数は常に 1 を返します。

関連項目：
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## 構文 {#syntax}

```sql
SELECT DENSE_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*PARTITION BY column*__
省略可能。結果セットをパーティションに分割します。`DENSE_RANK` 関数は各パーティションに個別に適用されます。

__*ORDER BY column*__
順位を割り当てる順序を指定します。`ORDER BY` 句がない場合、すべての行がピアと見なされ、順位 1 が割り当てられます。

## 戻り値 {#return-values}

パーティション内での現在の行の密な順位を表す整数を返します。同値の行があっても欠番は生じません。

* * *

## 例 {#examples}

### コントリビューション数でコントリビューターを順位付けする（欠番なし） {#rank-contributors-by-contributions-without-gaps}

```sql
-- Rank contributors without gaps for ties
SELECT
    DENSE_RANK() OVER (ORDER BY contributions DESC) as rank,
    login,
    contributions,
    html_url
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo IN ('stackql', 'stackql-deploy', 'stackql-provider-registry')
ORDER BY contributions DESC;
```

### `DENSE_RANK` を `RANK` および `ROW_NUMBER` と比較する {#compare-dense_rank-with-rank-and-row_number}

```sql
-- Compare RANK vs DENSE_RANK vs ROW_NUMBER for contributors
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank,
    DENSE_RANK() OVER (ORDER BY contributions DESC) as dense_rank,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as row_num
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### 複数のリポジトリにわたって合算したコントリビューション数を順位付けする {#rank-aggregated-contributions-across-multiple-repositories}

```sql
-- Rank total contributions across multiple repos
WITH all_contributors AS (
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions,
    MAX(html_url) as html_url
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

詳細については、[https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) を参照してください。
