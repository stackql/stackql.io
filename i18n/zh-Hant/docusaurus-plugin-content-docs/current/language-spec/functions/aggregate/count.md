---
title: COUNT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 COUNT 彙總函式：資源中的資料列數，或分組內某資料行的非 NULL 值個數。"
image: "/img/stackql-featured-image.png"
---
傳回資源中的記錄總數，或分組中某資料行非 `NULL` 值的個數。  

另請參閱：  
[[` SELECT `]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT COUNT( * | columnExpression ) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 引數 {#arguments}

__*columnExpression*__  或 __*__  
資料行或運算式。指定 __*__ 時，會計算資源或分組中的所有記錄。  

> *columnExpression* 的 `NULL` 值不會計入。若所有值都是 `NULL`，`COUNT` 會傳回 `0`。

__*groupByColumn*__  
用來執行摘要或彙總作業的一個或多個資料行。`GROUP BY` 子句會為每個資料行分組傳回一個資料列。

## 傳回值 {#return-values}

傳回表示計數的整數。

* * *

## 範例 {#examples}

### 傳回整個資源中的資料列數 {#return-the-number-of-rows-in-an-entire-resource}

```sql
SELECT count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 傳回依資料行運算式分組的資料列數 {#return-the-number-of-rows-grouped-by-a-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

### 將 `COUNT` 作為視窗函式來計算累計計數 {#use-count-as-a-window-function-to-calculate-cumulative-counts}

```sql
-- Track cumulative issue counts over time
SELECT
    number,
    title,
    state,
    created_at,
    COUNT(*) OVER (ORDER BY created_at) as cumulative_issues,
    COUNT(*) OVER (PARTITION BY state ORDER BY created_at) as cumulative_by_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY created_at;
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_aggfunc.html#count](https://www.sqlite.org/lang_aggfunc.html#count)。
