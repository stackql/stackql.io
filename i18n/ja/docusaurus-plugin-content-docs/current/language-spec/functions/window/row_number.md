---
title: ROW_NUMBER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
description: "StackQL の ROW_NUMBER ウィンドウ関数：ウィンドウの ORDER BY 句で指定された順序で、パーティション内の行に 1 から番号を付けます。"
image: "/img/stackql-featured-image.png"
---
現在のパーティション内での行の番号を返します。行には、ウィンドウ定義の `ORDER BY` 句で定義された順序で（ない場合は任意の順序で）、1 から番号が付けられます。

関連項目：
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## 構文 {#syntax}

```sql
SELECT ROW_NUMBER() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 引数 {#arguments}

__*PARTITION BY column*__
省略可能。結果セットをパーティションに分割します。`ROW_NUMBER` 関数は各パーティションに個別に適用され、パーティションごとに行番号がリセットされます。

__*ORDER BY column*__
行番号を割り当てる順序を指定します。意味のある結果を得るには必須です。

## 戻り値 {#return-values}

パーティション内での連続した行番号を表す、1 から始まる整数を返します。

* * *

## 例 {#examples}

### リポジトリ内でコントリビューターを順位付けする {#rank-contributors-within-a-repository}

```sql
-- Rank contributors within a repository
SELECT
    login,
    contributions,
    ROW_NUMBER() OVER (ORDER BY contributions DESC) as contribution_rank
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

### 時間の経過に沿って Issue の順番を追跡する {#track-issue-sequence-over-time}

```sql
-- Track issue growth over time
SELECT
    number,
    title,
    state,
    created_at,
    ROW_NUMBER() OVER (ORDER BY created_at) as issue_sequence
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

### `ROW_NUMBER` を `RANK` および `DENSE_RANK` と比較する {#compare-row_number-with-rank-and-dense_rank}

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

詳細については、[https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions) を参照してください。
