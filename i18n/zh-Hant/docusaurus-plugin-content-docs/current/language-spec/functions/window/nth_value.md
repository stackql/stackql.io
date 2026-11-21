---
title: NTH_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 NTH_VALUE 視窗函式：運算式在視窗框架第 N 個資料列上的值。"
image: "/img/stackql-featured-image.png"
---
傳回運算式針對視窗框架中第 N 個資料列求值的結果。

此內建視窗函式以與彙總視窗函式相同的方式，為每個資料列計算視窗框架。它會傳回運算式針對視窗框架第 N 個資料列求值的結果。視窗框架內的資料列從 1 開始編號，若有 `ORDER BY` 子句則依其定義的順序，否則依任意順序。如果視窗框架中沒有第 N 個資料列，則傳回 `NULL`。

另請參閱：
[[` SELECT `]](/language-spec/select) [[` FIRST_VALUE `]](/language-spec/functions/window/first_value) [[` LAST_VALUE `]](/language-spec/functions/window/last_value)

* * *

## 語法 {#syntax}

```sql
SELECT NTH_VALUE(expr, N) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*expr*__
要針對視窗框架中第 N 個資料列求值的運算式。

__*N*__
正整數，指定要擷取第幾個資料列（從 1 開始編號）。

__*PARTITION BY column*__
選用。將結果集分成多個分割區。`NTH_VALUE` 函式會在每個分割區內分別套用。

__*ORDER BY column*__
指定處理資料列的順序。

__*ROWS BETWEEN frame_start AND frame_end*__
選用。定義視窗框架。

## 傳回值 {#return-values}

傳回運算式針對視窗框架中第 N 個資料列求值的結果；若第 N 個資料列不存在，則傳回 `NULL`。

* * *

## 範例 {#examples}

### 取得前三名貢獻者 {#get-the-top-three-contributors}

```sql
-- Get the top 3 contributors by number of contributions
SELECT
    login,
    contributions,
    NTH_VALUE(login, 1) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as top_contributor,
    NTH_VALUE(login, 2) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as second_contributor,
    NTH_VALUE(login, 3) OVER (ORDER BY contributions DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as third_contributor
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

如需詳細資訊，請參閱 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
