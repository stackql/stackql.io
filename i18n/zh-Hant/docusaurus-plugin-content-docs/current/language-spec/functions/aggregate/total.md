---
title: TOTAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 TOTAL 彙總函式：資料行或分組中非 NULL 值的總和，一律以浮點數傳回。"
image: "/img/stackql-featured-image.png"
---
傳回資料行或資料行分組中所有非 `NULL` 值的總和。  

另請參閱：  
[[` SELECT `]](/language-spec/select) [[` SUM `]](/language-spec/functions/aggregate/sum)

* * * 

## 語法 {#syntax}

```sql
SELECT TOTAL(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 引數 {#arguments}

__*columnExpression*__  
資料行或運算式。

> 若 *columnExpression* 中的所有值都是 `NULL`，`TOTAL` 會傳回 `0.0`。  

__*groupByColumn*__  
用來執行摘要或彙總作業的一個或多個資料行。`GROUP BY` 子句會為每個資料行分組傳回一個資料列。

## 傳回值 {#return-values}

傳回表示 *columnExpression* 總和的浮點數值。

* * *

## 範例 {#examples}

### 傳回整個資源中資料行運算式的總和 {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 傳回依另一個資料行運算式分組的資料行運算式總和 {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT status, total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY status;
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc)。
