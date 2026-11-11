---
title: MIN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 MIN 聚合函数：列或列分组中的最小值。"
image: "/img/stackql-featured-image.png"
---
根据输入列或列分组返回最小值。  

另请参阅：  
[[` SELECT `]](/language-spec/select) 

* * * 

## 语法 {#syntax}

```sql
/* aggregate function */
SELECT MIN(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```
```sql
/* scalar (multi-argument) function */
SELECT MIN(scalarExpression1, scalarExpressionN, ...) FROM <multipartIdentifier>;
```

## 参数 {#arguments}

### 聚合函数 {#aggregate-function}

__*columnExpression*__  
列或表达式，可包含操作数（`+`、`-` 等）。

> `MIN` 函数根据列的数据类型返回给定列的最小值，例如，对整数列执行 `MIN` 操作会返回该列或列分组中的最小整数，而对字符串列或列分组执行 `MIN` 操作会返回最小的 ASCII 值（通常是按逆字典序排序时的第一个值，但存在一些差异）。

__*groupByColumn*__  
用于执行汇总或聚合操作的一列或多列。`GROUP BY` 子句为每个列分组返回一行。

> `MIN` 函数忽略 `NULL` 值。

> 如果分组中的所有值均为 `NULL`，则 `MIN` 函数返回 `NULL`。

### 标量函数 {#scalar-function}

__*scalarExpression*__  
由列或表达式组成的列表（2 个或更多），从中确定并返回最小值。

> 如果任一参数为 `NULL`，则标量 `MIN` 函数返回 `NULL`。

> 标量或多参数 `MIN` 函数从左到右搜索其参数，查找定义了排序规则函数的参数，并将该排序规则函数用于所有字符串比较。

> 如果标量 `MIN` 函数的参数均未定义排序规则函数，则使用 `BINARY` 排序规则函数。

> 如果只提供一个参数，则调用 `MIN` 聚合函数。

## 返回值 {#return-values}

根据输入数据类型（或分组）返回最小值。

* * *

## 示例 {#examples}

### 返回列的最小值 {#return-the-minimum-value-for-a-column}

```sql
SELECT name, location, min(timeCreated) 
FROM google.storage.buckets WHERE project = 'stackql';
```

### 返回列中每个值的最小值 {#return-the-minimum-value-for-each-value-in-a-column}

```sql
SELECT name, location,
min(round(julianday('now')-julianday(timeCreated))) as age
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location;
```

### 使用标量 `min` 函数从值列表中返回最小值 {#return-the-minimum-value-from-a-list-of-values-using-the-scalar-min-function}

```sql
SELECT min(json_extract(disks, '$[0].diskSizeGb'),json_extract(disks, '$[1].diskSizeGb')) as smallest_disk
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 将 `MIN` 用作窗口函数来查找分区内的最小值 {#use-min-as-a-window-function-to-find-minimum-values-within-partitions}

```sql
-- Find the earliest issue in each state
SELECT
    number,
    title,
    state,
    created_at,
    MIN(created_at) OVER (PARTITION BY state) as earliest_in_state
FROM github.issues.issues
WHERE owner = 'stackql'
  AND repo = 'stackql'
ORDER BY state, created_at;
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_aggfunc.html#min_agg](https://www.sqlite.org/lang_aggfunc.html#min_agg) 或 [https://www.sqlite.org/lang_corefunc.html#min_scalar](https://www.sqlite.org/lang_corefunc.html#min_scalar)。
