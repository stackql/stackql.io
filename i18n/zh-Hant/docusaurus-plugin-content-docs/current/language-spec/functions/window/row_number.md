---
title: ROW_NUMBER
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 ROW_NUMBER 視窗函式：依視窗 ORDER BY 子句指定的順序，從 1 開始為分割區內的資料列編號。"
image: "/img/stackql-featured-image.png"
---
傳回資料列在目前分割區內的編號。資料列從 1 開始編號，依照視窗定義中 `ORDER BY` 子句所定義的順序，否則依任意順序。

另請參閱：
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` DENSE_RANK `]](/language-spec/functions/window/dense_rank)

* * *

## 語法 {#syntax}

```sql
SELECT ROW_NUMBER() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*PARTITION BY column*__
選用。將結果集分成多個分割區。`ROW_NUMBER` 函式會分別套用至每個分割區，並在每個分割區重設資料列編號。

__*ORDER BY column*__
指定指派資料列編號的順序。必須指定才能得到有意義的結果。

## 傳回值 {#return-values}

傳回整數，代表分割區內從 1 開始的連續資料列編號。

* * *

## 範例 {#examples}

### 在存放庫內為貢獻者排名 {#rank-contributors-within-a-repository}

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

### 追蹤 issue 隨時間的順序 {#track-issue-sequence-over-time}

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

### 將 `ROW_NUMBER` 與 `RANK` 和 `DENSE_RANK` 比較 {#compare-row_number-with-rank-and-dense_rank}

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

如需詳細資訊，請參閱 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
