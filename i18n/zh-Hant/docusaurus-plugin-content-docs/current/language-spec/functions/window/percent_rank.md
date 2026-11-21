---
title: PERCENT_RANK
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 PERCENT_RANK 視窗函式：目前資料列在其分割區內的相對排名，值介於 0.0 和 1.0 之間。"
image: "/img/stackql-featured-image.png"
---
傳回介於 0.0 和 1.0 之間的值，代表目前資料列在其分割區內的相對排名。

儘管名稱如此，此函式傳回的值等於 `(rank - 1) / (partition-rows - 1)`，其中 `rank` 是內建視窗函式 `RANK()` 傳回的值，`partition-rows` 是分割區中的資料列總數。如果分割區只包含一個資料列，此函式傳回 0.0。

另請參閱：
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank) [[` CUME_DIST `]](/language-spec/functions/window/cume_dist)

* * *

## 語法 {#syntax}

```sql
SELECT PERCENT_RANK() OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*PARTITION BY column*__
選用。將結果集分成多個分割區。`PERCENT_RANK` 函式會分別套用至每個分割區。

__*ORDER BY column*__
指定計算百分位數排名的順序。

## 傳回值 {#return-values}

傳回介於 0.0 和 1.0 之間的浮點數，代表百分位數排名。

* * *

## 範例 {#examples}

### 計算貢獻者的百分位數排名 {#calculate-percentile-ranking-of-contributors}

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

`PERCENT_RANK` 值為 0.0 表示排名最低的資料列，1.0 則表示排名最高的資料列。使用此函式可找出特定值在分佈中的位置。

:::

如需詳細資訊，請參閱 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
