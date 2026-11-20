---
title: MAX
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 MAX 彙總函式：資料行或資料行分組中的最大值。"
image: "/img/stackql-featured-image.png"
---
根據資料行輸入或資料行分組傳回最大值。  

另請參閱：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 語法 {#syntax}

```sql
/* aggregate function */
SELECT MAX(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MAX(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## 引數 {#arguments}

### 彙總函式 {#aggregate-function}

__*columnExpression*__  
資料行或運算式，可包含運算子（`+`、`-` 等）。

> `MAX` 函式會根據資料行的資料型別傳回該資料行的最大值，例如對整數資料行執行 `MAX` 作業，會傳回該資料行或資料行分組中最大的整數；而對字串資料行或資料行分組執行 `MAX` 作業，則會傳回排序在最後的值，字串以 `BINARY` 定序函式進行比較（逐位元組比較，因此大寫字母排在小寫字母之前）。

__*groupByColumn*__  
用來執行摘要或彙總作業的一個或多個資料行。`GROUP BY` 子句會為每個資料行分組傳回一個資料列。

> `MAX` 函式會忽略 `NULL` 值。

> 若分組中的所有值都是 `NULL`，`MAX` 函式會傳回 `NULL`。

### 純量函式 {#scalar-function}

__*scalarExpression*__  
由資料行或運算式（2 個以上）組成的清單，會從中判定並傳回最大值。 

> 若任何引數為 `NULL`，純量 `MAX` 函式會傳回 `NULL`。 

> 純量或多引數 `MAX` 函式會由左至右搜尋其引數，找出定義了定序函式的引數，並將該定序函式用於所有字串比較。 

> 若純量 `MAX` 函式的引數都沒有定義定序函式，則會使用 `BINARY` 定序函式。 

> 若只提供一個引數，則會叫用 `MAX` 彙總函式。

## 傳回值 {#return-values}

根據輸入的資料型別（或分組）傳回最大值。

* * *

## 範例 {#examples}

### 傳回資料行的最大值 {#return-the-maximum-value-for-a-column}

```sql
SELECT name, location, max(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### 為資料行中的每個值傳回最大值 {#return-the-maximum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
max(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### 使用純量 `max` 函式從值清單中傳回最大值 {#return-the-maximum-value-from-a-list-of-values-using-the-scalar-max-function}

```sql
SELECT max(CAST(json_extract(disks, '$[0].diskSizeGb') AS INTEGER), CAST(json_extract(disks, '$[1].diskSizeGb') AS INTEGER)) as largest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 將 `MAX` 作為視窗函式，在分割區內找出最大值 {#use-max-as-a-window-function-to-find-maximum-values-within-partitions}

```sql
-- Find the most recent issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MAX(created_at) OVER (PARTITION BY state) as latest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at DESC;
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_aggfunc.html#max_agg](https://www.sqlite.org/lang_aggfunc.html#max_agg) 或 [https://www.sqlite.org/lang_corefunc.html#max_scalar](https://www.sqlite.org/lang_corefunc.html#max_scalar)。
