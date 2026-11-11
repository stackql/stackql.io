---
title: AVG
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 AVG 聚合函数：列或分组中非 NULL 值的平均值。"
image: "/img/stackql-featured-image.png"
---
返回列或列分组中所有非 `NULL` 值的平均值。  

另请参阅：  
[[` SELECT `]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT AVG(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 参数 {#arguments}

__*columnExpression*__  
列或表达式。

> 无法表示为数值的字符串和 BLOB 值会被解释为 `0`。  

> *columnExpression* 中必须至少有一个值不为 `NULL`，否则返回 `NULL`。

__*groupByColumn*__  
用于执行汇总或聚合操作的一列或多列。`GROUP BY` 子句为每个列分组返回一行。

## 返回值 {#return-values}

返回一个浮点值，表示 *columnExpression* 的平均值。

* * *

## 示例 {#examples}

### 返回列表达式在整个资源上的平均值 {#return-the-average-of-a-column-expression-over-an-entire-resource}

```sql
SELECT avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 返回按另一个列表达式分组的列表达式的平均值 {#return-the-average-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, avg(json_array_length(disks)) as avg_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

### 将 `AVG` 用作窗口函数来计算移动平均值 {#use-avg-as-a-window-function-to-calculate-moving-averages}

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

有关更多信息，请参阅 [https://www.sqlite.org/lang_aggfunc.html#avg](https://www.sqlite.org/lang_aggfunc.html#avg)。
