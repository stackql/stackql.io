---
title: FIRST_VALUE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 FIRST_VALUE 視窗函式：運算式在視窗框架第一個資料列上的值。"
image: "/img/stackql-featured-image.png"
---
傳回運算式針對視窗框架中第一個資料列求值的結果。

此內建視窗函式以與彙總視窗函式相同的方式，為每個資料列計算視窗框架。對於每個資料列，它會傳回運算式針對視窗框架中第一個資料列求值的結果。

另請參閱：
[[` SELECT `]](/language-spec/select) [[` LAST_VALUE `]](/language-spec/functions/window/last_value) [[` NTH_VALUE `]](/language-spec/functions/window/nth_value)

* * *

## 語法 {#syntax}

```sql
SELECT FIRST_VALUE(expr) OVER (
    [PARTITION BY column]
    ORDER BY column
    [ROWS BETWEEN frame_start AND frame_end]
) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*expr*__
要針對視窗框架中第一個資料列求值的運算式。

__*PARTITION BY column*__
選用。將結果集分成多個分割區。`FIRST_VALUE` 函式會在每個分割區內分別套用。

__*ORDER BY column*__
指定處理資料列的順序。

__*ROWS BETWEEN frame_start AND frame_end*__
選用。定義視窗框架。常見選項包括：
- `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`
- `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`

沒有框架子句時，框架為 `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`：框架結束於在 `ORDER BY` 資料行上與目前資料列同值的最後一個資料列，而不是目前資料列。

## 傳回值 {#return-values}

傳回運算式針對視窗框架中第一個資料列求值的結果。

* * *

## 範例 {#examples}

### 找出第一個發行版本以供參照 {#find-the-first-release-for-context}

```sql
-- Find first and latest release for context
SELECT
    tag_name,
    name,
    published_at,
    FIRST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as first_release,
    LAST_VALUE(tag_name) OVER (
        ORDER BY published_at
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) as latest_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

:::tip

根據預設，視窗框架從分割區的開頭延伸到在 `ORDER BY` 資料行上與目前資料列同值的最後一個資料列。`FIRST_VALUE` 讀取的是框架的第一個資料列，因此使用預設框架與使用 `ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING` 時傳回的值相同；明確指定框架對 `LAST_VALUE` 和 `NTH_VALUE` 才有影響，因為它們會讀取框架中更後面的資料列。

:::

如需詳細資訊，請參閱 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
