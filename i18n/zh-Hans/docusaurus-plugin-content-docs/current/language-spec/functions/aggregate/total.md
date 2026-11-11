---
title: TOTAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 TOTAL 聚合函数：列或分组中非 NULL 值的总和，始终以浮点数返回。"
image: "/img/stackql-featured-image.png"
---
返回列或列分组中所有非 `NULL` 值的总和。  

另请参阅：  
[[` SELECT `]](/language-spec/select) [[` SUM `]](/language-spec/functions/aggregate/sum)

* * * 

## 语法 {#syntax}

```sql
SELECT TOTAL(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 参数 {#arguments}

__*columnExpression*__  
列或表达式。

> 如果 *columnExpression* 中的所有值均为 `NULL`，则 `TOTAL` 返回 `0.0`。  

__*groupByColumn*__  
用于执行汇总或聚合操作的一列或多列。`GROUP BY` 子句为每个列分组返回一行。

## 返回值 {#return-values}

返回一个浮点值，表示 *columnExpression* 的总和。

* * *

## 示例 {#examples}

### 返回列表达式在整个资源上的总和 {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 返回按另一个列表达式分组的列表达式的总和 {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc)。
