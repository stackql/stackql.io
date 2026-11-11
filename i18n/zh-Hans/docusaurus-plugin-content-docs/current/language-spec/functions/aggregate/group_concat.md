---
title: GROUP_CONCAT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 GROUP_CONCAT 聚合函数：将列或分组中的非 NULL 值拼接为一个字符串，可指定分隔符。"
image: "/img/stackql-featured-image.png"
---
返回一个字符串，由输入列或分组中所有非 `NULL` 值拼接而成。  

> 拼接元素的顺序是任意的。

另请参阅：  
[[` SELECT `]](/language-spec/select)

* * * 

## 语法 {#syntax}

```sql
SELECT GROUP_CONCAT(columnExpression [, separator]) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## 参数 {#arguments}

__*columnExpression*__  
列或表达式。如果指定 __*__，则统计资源或分组中的所有记录。  

> *columnExpression* 中必须至少有一个值不为 `NULL`，否则返回 `NULL`。

__*separator*__  
拼接结果使用的分隔符，如果未指定 *separator*，则使用默认值 `,`。

__*groupByColumn*__  
用于执行汇总或聚合操作的一列或多列。`GROUP BY` 子句为每个列分组返回一行。

## 返回值 {#return-values}

返回一个字符串，其内容为以 *separator* 或 `,` 分隔的值列表。

* * *

## 示例 {#examples}

### 返回列在整个资源上的值列表 {#return-a-list-of-values-for-a-column-over-an-entire-resource}

```sql
SELECT group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 返回按另一个列表达式分组的列表达式的值列表 {#return-a-list-of-values-for-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT tags, group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY json_extract(tags, '$.instanceType');
```

有关更多信息，请参阅 [https://www.sqlite.org/lang_aggfunc.html#group_concat](https://www.sqlite.org/lang_aggfunc.html#group_concat)。
