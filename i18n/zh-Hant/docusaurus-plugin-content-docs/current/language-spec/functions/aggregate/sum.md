---
title: SUM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 SUM 彙總函式：資料行或分組中非 NULL 值的總和，所有輸入皆為整數時傳回整數。"
image: "/img/stackql-featured-image.png"
---
傳回資料行或資料行分組中所有非 `NULL` 值的總和。  

另請參閱：  
[[` SELECT `]](/language-spec/select) [[` TOTAL `]](/language-spec/functions/aggregate/total)

* * * 

:::tip

使用 [**TOTAL**](/language-spec/functions/aggregate/total) 函式來加總浮點數或長整數值。

:::

## 語法 {#syntax}

```sql
SELECT SUM(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 引數 {#arguments}

__*columnExpression*__  
資料行或運算式。

> 若 *columnExpression* 中的所有值都是 `NULL`，`SUM` 會傳回 `NULL`。  

> 若計算過程中的任何時間點發生整數溢位，`SUM` 會擲回「integer overflow」例外狀況。[`TOTAL`](/language-spec/functions/aggregate/total) 函式永遠不會擲回整數溢位。  

__*groupByColumn*__  
用來執行摘要或彙總作業的一個或多個資料行。`GROUP BY` 子句會為每個資料行分組傳回一個資料列。

## 傳回值 {#return-values}

若所有非 `NULL` 輸入都是整數，則傳回表示 *columnExpression* 總和的整數。若有任何輸入不是整數或為 `NULL`，則傳回浮點數值，該值可能是真實總和的近似值。

* * *

## 範例 {#examples}

### 傳回整個資源中資料行運算式的總和 {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 傳回依另一個資料行運算式分組的資料行運算式總和 {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT status, sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY status;
```

### 將 `SUM` 作為視窗函式來計算累計總和 {#use-sum-as-a-window-function-to-calculate-running-totals}

```sql
-- Calculate running total and percentage of contributions
SELECT
    login,
    contributions,
    SUM(contributions) OVER (ORDER BY contributions DESC) as running_total,
    SUM(contributions) OVER () as total_contributions,
    ROUND(100.0 * contributions / SUM(contributions) OVER (), 2) as pct_of_total
FROM github.repos.contributors
WHERE owner = 'stackql'
  AND repo = 'stackql';
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc)。
