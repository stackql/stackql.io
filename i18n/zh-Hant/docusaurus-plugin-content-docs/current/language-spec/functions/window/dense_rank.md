---
title: DENSE_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 DENSE_RANK 視窗函式：目前資料列在其分割區內不留間隔的排名，並列的資料列排名相同，下一個排名緊接在後。"
image: "/img/stackql-featured-image.png"
---
傳回目前資料列不留間隔的排名。也就是目前資料列的對等群組在其分割區內的編號。排序資料行值相同的資料列會獲得相同的排名，而下一個排名值一律只加 1（不留間隔）。

資料列依照視窗定義中 `ORDER BY` 子句所定義的順序從 1 開始編號。若沒有 `ORDER BY` 子句，所有資料列都視為對等資料列，此函式一律傳回 1。

另請參閱：
[[` SELECT `]](/language-spec/select) [[` ROW_NUMBER `]](/language-spec/functions/window/row_number) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## 語法 {#syntax}

```sql
SELECT DENSE_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*PARTITION BY column*__
選用。將結果集分成多個分割區。`DENSE_RANK` 函式會分別套用至每個分割區。

__*ORDER BY column*__
指定指派排名的順序。若沒有 `ORDER BY` 子句，所有資料列都視為對等資料列，排名皆為 1。

## 傳回值 {#return-values}

傳回整數，代表目前資料列在分割區內的密集排名，並列的值之後不留間隔。

* * *

## 範例 {#examples}

### 依貢獻次數為貢獻者排名（不留間隔） {#rank-contributors-by-contributions-without-gaps}

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

### 將 `DENSE_RANK` 與 `RANK` 和 `ROW_NUMBER` 比較 {#compare-dense_rank-with-rank-and-row_number}

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

### 為多個存放庫的彙總貢獻次數排名 {#rank-aggregated-contributions-across-multiple-repositories}

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

如需詳細資訊，請參閱 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
