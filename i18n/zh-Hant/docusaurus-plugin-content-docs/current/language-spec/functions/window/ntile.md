---
title: NTILE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 NTILE 視窗函式：將分割區盡可能平均地分成 N 個群組，並為每個資料列所屬的群組編號 1 到 N。"
image: "/img/stackql-featured-image.png"
---
將分割區盡可能平均地分成 N 個群組，並依照 `ORDER BY` 子句所定義的順序（否則依任意順序）為每個群組指派介於 1 和 N 之間的整數。必要時，較大的群組會排在前面。

另請參閱：
[[` SELECT `]](/language-spec/select) [[` RANK `]](/language-spec/functions/window/rank)

* * *

## 語法 {#syntax}

```sql
SELECT NTILE(N) OVER ([PARTITION BY column] ORDER BY column [ASC|DESC])
FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*N*__
正整數，指定要將分割區分成多少個群組（tile）。

__*PARTITION BY column*__
選用。將結果集分成多個分割區。`NTILE` 函式會分別套用至每個分割區。

__*ORDER BY column*__
指定將資料列指派到各群組的順序。

## 傳回值 {#return-values}

傳回介於 1 和 N 之間的整數，表示目前資料列所屬的群組。

* * *

## 範例 {#examples}

### 依貢獻程度將貢獻者分成四分位數群組 {#group-contributors-into-quartiles-by-contribution-level}

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

使用 `NTILE` 可在您的資料中建立區段或層級。常見的使用案例包括將資料分成四分位數（N=4）、十分位數（N=10）或百分位數（N=100）。

:::

如需詳細資訊，請參閱 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
