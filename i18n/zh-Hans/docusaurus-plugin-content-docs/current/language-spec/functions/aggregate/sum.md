---
title: SUM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 SUM 聚合函数：列或分组中非 NULL 值的总和，所有输入均为整数时返回整数。"
image: "/img/stackql-featured-image.png"
---
返回列或列分组中所有非 `NULL` 值的总和。  

另请参阅：  
[[` SELECT `]](/language-spec/select) [[` TOTAL `]](/language-spec/functions/aggregate/total)

* * * 

:::tip

使用 [**TOTAL**](/language-spec/functions/aggregate/total) 函数对浮点数或长整型值求和。

:::

## 语法 {#syntax}

```sql
SELECT SUM(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 参数 {#arguments}

__*columnExpression*__  
列或表达式。

> 如果 *columnExpression* 中的所有值均为 `NULL`，则 `SUM` 返回 `NULL`。  

> 如果计算过程中的任何时刻发生整数溢出，`SUM` 会抛出“integer overflow”异常。[`TOTAL`]](/language-spec/functions/aggregate/total) 函数永远不会抛出整数溢出。  

__*groupByColumn*__  
用于执行汇总或聚合操作的一列或多列。`GROUP BY` 子句为每个列分组返回一行。

## 返回值 {#return-values}

如果所有非 `NULL` 输入均为整数，则返回一个整数，表示 *columnExpression* 的总和。如果任一输入不是整数或为 `NULL`，则返回一个浮点值，该值可能是真实总和的近似值。

* * *

## 示例 {#examples}

### 返回列表达式在整个资源上的总和 {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 返回按另一个列表达式分组的列表达式的总和 {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, sum(json_array_length(disks)) as sum_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

### 将 `SUM` 用作窗口函数来计算累计总和 {#use-sum-as-a-window-function-to-calculate-running-totals}

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

有关更多信息，请参阅 [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc)。
