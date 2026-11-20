---
title: LAG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 LAG 視窗函式：針對分割區中前面的資料列對運算式求值，可選擇指定位移和預設值。"
image: "/img/stackql-featured-image.png"
---
傳回針對分割區中前面的資料列對運算式求值的結果。

第一種形式的 `LAG()` 會傳回針對分割區中前一個資料列對運算式求值的結果。如果沒有前一個資料列（因為目前資料列是第一個），則傳回 `NULL`。

如果提供 offset 引數，它必須是非負整數。傳回值是針對分割區內目前資料列之前第 *offset* 個資料列對運算式求值的結果。如果 offset 為 0，則針對目前資料列對運算式求值。如果目前資料列之前沒有第 *offset* 個資料列，則傳回 `NULL`。

如果同時提供預設值，當 offset 所指的資料列不存在時，會傳回該預設值而非 `NULL`。

另請參閱：
[[` SELECT `]](/language-spec/select) [[` LEAD `]](/language-spec/functions/window/lead)

* * *

## 語法 {#syntax}

```sql
SELECT LAG(expr) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;

SELECT LAG(expr, offset, default) OVER ([PARTITION BY column] ORDER BY column) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

__*expr*__
要針對前一個資料列求值的運算式。

__*offset*__
選用。非負整數，指定要往回查看多少個資料列。預設為 1。

__*default*__
選用。位移所指的資料列不存在時要傳回的值。預設為 `NULL`。

__*PARTITION BY column*__
選用。將結果集分成多個分割區。`LAG` 函式會在每個分割區內分別套用。

__*ORDER BY column*__
指定處理資料列的順序。

## 傳回值 {#return-values}

傳回運算式針對指定的前面資料列求值的結果；若該資料列不存在，則傳回 `NULL`（或預設值）。

* * *

## 範例 {#examples}

### 將每個發行版本與前一個發行版本比較 {#compare-each-release-to-the-previous-release}

```sql
-- Compare each release to the previous release
SELECT
    tag_name,
    name,
    published_at,
    LAG(tag_name, 1) OVER (ORDER BY published_at) as previous_release,
    LAG(published_at, 1) OVER (ORDER BY published_at) as previous_release_date,
    julianday(published_at) - julianday(LAG(published_at, 1) OVER (ORDER BY published_at)) as days_since_last_release
FROM github.repos.releases
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY published_at;
```

### 分析各 issue 建立時間之間的間隔 {#analyze-gaps-between-issue-creation}

```sql
-- Analyze gaps between issue creation
SELECT
    number,
    title,
    created_at,
    LAG(created_at, 1) OVER (ORDER BY created_at) as prev_issue_date,
    julianday(created_at) - julianday(LAG(created_at, 1) OVER (ORDER BY created_at)) as days_since_prev_issue
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

如需詳細資訊，請參閱 [https://sqlite.org/windowfunctions.html#built-in_window_functions](https://sqlite.org/windowfunctions.html#built-in_window_functions)。
