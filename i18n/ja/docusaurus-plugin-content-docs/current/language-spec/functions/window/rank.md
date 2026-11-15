---
title: RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の RANK ウィンドウ関数：パーティション内での現在の行の欠番のある順位で、同順位の行は同じ順位を共有し、後続の順位はその分飛ばされます。"
image: "/img/stackql-featured-image.png"
---
現在の行の順位を欠番ありで返します。これは、各グループの最初のピアの `ROW_NUMBER` です。並べ替え列の値が等しい行には同じ順位が割り当てられ、次の順位の値は同順位の行の数だけ増加します（欠番が生じます）。

`ORDER BY` 句がない場合、すべての行がピアと見なされ、この関数は常に 1 を返します。

関連項目：
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## 構文 {#syntax}

```sql
SELECT RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*PARTITION BY column*__
省略可能。結果セットをパーティションに分割します。`RANK` 関数は各パーティションに個別に適用されます。

__*ORDER BY column*__
順位を割り当てる順序を指定します。`ORDER BY` 句がない場合、すべての行がピアと見なされ、順位 1 が割り当てられます。

## 戻り値 {#return-values}

パーティション内での現在の行の順位を表す整数を返します。同値の行がある場合は欠番が生じます。

* * *

## 例 {#examples}

### コントリビューション数でコントリビューターを順位付けする（欠番あり） {#rank-contributors-by-contributions-with-gaps}

```sql
-- Rank contributors with gaps for ties
SELECT
    login,
    contributions,
    RANK() OVER (ORDER BY contributions DESC) as rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### `RANK` を `DENSE_RANK` および `ROW_NUMBER` と比較する {#compare-rank-with-dense_rank-and-row_number}

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

### `PARTITION BY` を使用して各リポジトリ内でコントリビューターを順位付けする {#rank-contributors-within-each-repository-using-partition-by}

```sql
-- Rank contributors within each repo
WITH repo_contributors AS (
    SELECT
        repo,
        login,
        contributions,
        RANK() OVER (PARTITION BY repo ORDER BY contributions DESC) as rank_in_repo
    FROM (
        SELECT 'stackql' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql'
        UNION ALL
        SELECT 'stackql-provider-registry' as repo, login, contributions
        FROM github.repos.contributors
        WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
    ) t
)
SELECT
    repo,
    login,
    contributions,
    rank_in_repo
FROM repo_contributors
ORDER BY repo, rank_in_repo;
```

詳細については、[https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) を参照してください。
