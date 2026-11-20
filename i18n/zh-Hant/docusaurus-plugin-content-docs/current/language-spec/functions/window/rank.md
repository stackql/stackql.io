---
title: RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 RANK 視窗函式：目前資料列在其分割區內留有間隔的排名，並列的資料列排名相同，其後的排名會被跳過。"
image: "/img/stackql-featured-image.png"
---
傳回目前資料列留有間隔的排名。也就是每個群組中第一個對等資料列的 `ROW_NUMBER`。排序資料行值相同的資料列會獲得相同的排名，而下一個排名值會增加並列資料列的數目（因而留下間隔）。

若沒有 `ORDER BY` 子句，所有資料列都視為對等資料列，此函式一律傳回 1。

另請參閱：
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## 語法 {#syntax}

```sql
SELECT RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*PARTITION BY column*__
選用。將結果集分成多個分割區。`RANK` 函式會分別套用至每個分割區。

__*ORDER BY column*__
指定指派排名的順序。若沒有 `ORDER BY` 子句，所有資料列都視為對等資料列，排名皆為 1。

## 傳回值 {#return-values}

傳回整數，代表目前資料列在分割區內的排名，並列的值之後會留有間隔。

* * *

## 範例 {#examples}

### 依貢獻次數為貢獻者排名（留有間隔） {#rank-contributors-by-contributions-with-gaps}

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

### 將 `RANK` 與 `DENSE_RANK` 和 `ROW_NUMBER` 比較 {#compare-rank-with-dense_rank-and-row_number}

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

### 使用 `PARTITION BY` 在每個存放庫內為貢獻者排名 {#rank-contributors-within-each-repository-using-partition-by}

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

如需詳細資訊，請參閱 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
