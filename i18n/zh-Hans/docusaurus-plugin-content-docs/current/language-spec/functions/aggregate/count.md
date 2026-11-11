---
title: COUNT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 COUNT 聚合函数：资源中的行数，或分组内某列非 NULL 值的数量。"
image: "/img/stackql-featured-image.png"
---
返回资源中的记录总数，或分组中某列非 `NULL` 实例的计数。  

另请参阅：  
[[` SELECT `]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT COUNT( * | columnExpression ) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 参数 {#arguments}

__*columnExpression*__ 或 __*__  
列或表达式。如果指定 __*__，则统计资源或分组中的所有记录。  

> *columnExpression* 中必须至少有一个值不为 `NULL`，否则返回 `NULL`。

__*groupByColumn*__  
用于执行汇总或聚合操作的一列或多列。`GROUP BY` 子句为每个列分组返回一行。

## 返回值 {#return-values}

返回一个整数，表示计数。

* * *

## 示例 {#examples}

### 返回列表达式在整个资源上的平均值 {#return-the-average-of-a-column-expression-over-an-entire-resource}

```sql
SELECT count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 返回按另一个列表达式分组的列表达式的平均值 {#return-the-average-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, count(*) as num_instances
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

### 将 `COUNT` 用作窗口函数来计算累计计数 {#use-count-as-a-window-function-to-calculate-cumulative-counts}

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

有关更多信息，请参阅 [https://www.sqlite.org/lang_aggfunc.html#count](https://www.sqlite.org/lang_aggfunc.html#count)。
