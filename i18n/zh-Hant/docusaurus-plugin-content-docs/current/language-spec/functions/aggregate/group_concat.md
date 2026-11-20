---
title: GROUP_CONCAT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 GROUP_CONCAT 彙總函式：以選用的分隔符號，將資料行或分組的非 NULL 值串連成一個字串。"
image: "/img/stackql-featured-image.png"
---
傳回一個字串，由輸入資料行或分組中所有非 `NULL` 值串連而成。  

> 串連元素的順序是任意的。

另請參閱：  
[[` SELECT `]](/language-spec/select)

* * * 

## 語法 {#syntax}

```sql
SELECT GROUP_CONCAT(columnExpression [, separator]) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 引數 {#arguments}

__*columnExpression*__  
資料行或運算式。  

> *columnExpression* 中必須至少有一個非 `NULL` 的值，否則會傳回 `NULL`。

__*separator*__  
用於串連結果的分隔符號；若未指定 *separator*，則使用預設值 `,`。

__*groupByColumn*__  
用來執行摘要或彙總作業的一個或多個資料行。`GROUP BY` 子句會為每個資料行分組傳回一個資料列。

## 傳回值 {#return-values}

傳回以 *separator* 或 `,` 分隔的值清單字串。

* * *

## 範例 {#examples}

### 傳回整個資源中某資料行的值清單 {#return-a-list-of-values-for-a-column-over-an-entire-resource}

```sql
SELECT group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 傳回依另一個資料行運算式分組的資料行運算式值清單 {#return-a-list-of-values-for-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

如需詳細資訊，請參閱 [https://www.sqlite.org/lang_aggfunc.html#group_concat](https://www.sqlite.org/lang_aggfunc.html#group_concat)。
