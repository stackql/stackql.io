---
title: CUME_DIST
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 CUME_DIST 視窗函式：目前資料列在其分割區內的累積分佈，即資料列編號除以分割區資料列數。"
image: "/img/stackql-featured-image.png"
---
傳回值在分割區內的累積分佈。計算方式為 `row-number / partition-rows`，其中 `row-number` 是 `ROW_NUMBER()` 針對群組中最後一個對等資料列傳回的值，`partition-rows` 是分割區中的資料列數。

另請參閱：
[[` SELECT `]](/language-spec/select) [[` PERCENT_RANK `]](/language-spec/functions/window/percent_rank) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## 語法 {#syntax}

```sql
SELECT CUME_DIST() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*PARTITION BY column*__
選用。將結果集分成多個分割區。`CUME_DIST` 函式會分別套用至每個分割區。

__*ORDER BY column*__
指定計算累積分佈的順序。

## 傳回值 {#return-values}

傳回介於 0.0 和 1.0 之間的浮點數，代表累積分佈值。

* * *

## 範例 {#examples}

### 計算貢獻者的累積分佈 {#calculate-cumulative-distribution-of-contributors}

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

`CUME_DIST` 傳回值小於或等於目前資料列之值的資料列所佔的比例。值為 0.75 表示有 75% 的資料列，其值小於或等於目前資料列的值。

:::

如需詳細資訊，請參閱 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
