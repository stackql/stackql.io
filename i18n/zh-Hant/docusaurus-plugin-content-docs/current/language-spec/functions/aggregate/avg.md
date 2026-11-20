---
title: AVG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 AVG 彙總函式：資料行或分組中非 NULL 值的平均值。"
image: "/img/stackql-featured-image.png"
---
傳回資料行或資料行分組中所有非 `NULL` 值的平均值。  

另請參閱：  
[[` SELECT `]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT AVG(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 引數 {#arguments}

__*columnExpression*__  
資料行或運算式。

> 無法以數值表示的字串和 BLOB 值會解譯為 `0`。  

> *columnExpression* 中必須至少有一個非 `NULL` 的值，否則會傳回 `NULL`。

__*groupByColumn*__  
用來執行摘要或彙總作業的一個或多個資料行。`GROUP BY` 子句會為每個資料行分組傳回一個資料列。

## 傳回值 {#return-values}

傳回表示 *columnExpression* 平均值的浮點數。

* * *

## 範例 {#examples}

### 傳回整個資源中資料行運算式的平均值 {#return-the-average-of-a-column-expression-over-an-entire-resource}

```sql
SELECT avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 傳回依另一個資料行運算式分組的資料行運算式平均值 {#return-the-average-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

### 將 `AVG` 作為視窗函式來計算移動平均 {#use-avg-as-a-window-function-to-calculate-moving-averages}

```sql
-- Calculate moving average of commit activity
WITH weekly_totals AS (
    SELECT
        week,
        SUM(json_each.value) as commits_this_week
    FROM github.repos.stats_commit_activity, JSON_EACH(days)
    WHERE owner = 'stackql'
      AND repo = 'stackql'
    GROUP BY week
)
SELECT
    week,
    commits_this_week,
    ROUND(AVG(commits_this_week) OVER (ORDER BY week ROWS BETWEEN 3 PRECEDING AND CURRENT ROW), 1) as four_week_moving_avg
FROM weekly_totals
ORDER BY week;
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_aggfunc.html#avg](https://www.sqlite.org/lang_aggfunc.html#avg)。
