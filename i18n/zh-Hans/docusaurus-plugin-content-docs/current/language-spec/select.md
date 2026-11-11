---
title: SELECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: "StackQL 中的 SELECT：使用 SQL 查询云和 SaaS 资源，支持映射到 API 参数的 WHERE 过滤条件、连接、分组、排序和 JSON 字段访问。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

返回资源的一个或多个实例。  

另请参阅：
[[ StackQL 资源层次结构 ]](/getting-started/resource-hierarchy) [[` WITH (CTEs) `]](/language-spec/with) [[` 窗口函数 `]](/language-spec/windowing_functions)

* * * 

## 语法 {#syntax}

*selectStatement::=*

<RailroadDiagram 
type="select"
/>

&nbsp;  
&nbsp;

```sql
[ WITH [ RECURSIVE ] <cteName> [ ( <columnList> ) ] AS ( <selectStatement> ) [, ...] ]
SELECT { * | <fieldList> | <windowFunctionCall> [ AS <alias> ] }
FROM { <multipartIdentifier> | <joinStatement(s)> }
[ WHERE <expression> ]
[ GROUP BY <fieldList> ]
[ HAVING <expression> ]
[ WINDOW <windowName> AS ( <windowSpec> ) [, ...] ]
[ ORDER BY <fieldList> [ ASC | DESC ] ]
[ LIMIT <integer> ]
[ UNION <selectStatement> ];
```

不带 `ON` 子句的 `JOIN` 被视为 `CROSS JOIN`，因此连接条件可以改在 `WHERE` 子句中给出。`v0.12.732` 之前的 StackQL 发布版本无法规划不带条件的 `JOIN`。

* * *

## 示例 {#examples}

### 基本 `SELECT` 语句 {#basic-select-statement}
在已通过身份验证的会话中运行基本 `SELECT` 语句。

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

### 使用 `SELECT` 语句返回指定字段 {#returning-specified-fields-using-a-select-statement}
运行带列列表的 `SELECT` 语句，返回 Compute Engine 实例的指定字段。

```sql
-- Selecting specified fields from a resource
SELECT id, name 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 使用 `ORDER BY` 子句返回按某列升序排序的结果集 {#return-a-result-set-ordered-by-a-column-in-ascending-order-using-the-order-by-clause}
运行 `SELECT` 语句，返回 Cloud Storage buckets 资源的字段，并按升序（最早的在前）对结果排序。 

```sql
-- Order a list of Cloud Storage buckets by creation time (in ascending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated;
```

### 使用 `ORDER BY` 子句返回按某列降序排序的结果集 {#return-a-result-set-ordered-by-a-column-in-descending-order-using-the-order-by-clause}
运行 `SELECT` 语句，返回 Cloud Storage buckets 资源的字段，并按降序（最新的在前）对结果排序。

```sql
-- Order a list of Cloud Storage buckets by creation time (in descending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC;
```

### 使用 `ORDER BY` 和 `LIMIT` 子句获取某列的首个结果 {#get-the-top-result-from-a-column-using-the-order-by-and-limit-clauses}
运行 `SELECT` 语句，返回最近创建的 Cloud Storage 存储桶。

```sql
-- Find the most recently created bucket
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC LIMIT 1;
```

### 将 `COUNT` 函数与 `GROUP BY` 和 `HAVING` 子句配合使用，按分组和过滤条件统计资源数量 {#use-the-count-function-with-the-group-by-and-having-clauses-to-count-resources-based-upon-a-grouping-and-filter}
运行带 `COUNT` 函数的 `SELECT` 语句，使用 `GROUP BY` 和 `HAVING` 子句返回除亚洲之外每个位置的存储桶数量。

```sql
-- Return a count of the number of buckets in every location other than Asia
SELECT name, location, count(*) as num_buckets 
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location
HAVING location != 'ASIA';
```

有关 `COUNT` 函数以及 StackQL 支持的其他聚合函数的更多信息，请参阅[聚合函数](/language-spec/functions/aggregate/count)。

### 从 Azure VM Scaleset 获取按需 VM 及其对应网络接口的列表 {#get-list-of-on-demand-vms-and-corresponding-network-interface-from-azure-vm-scaleset}
运行 `SELECT` 语句，使用 [`SPLIT_PART`](/language-spec/functions/string/split_part) 和 [`JSON_EXTRACT`](/language-spec/functions/json/json_extract) 函数获取 OD VM 及其 NIC 的列表。注意，在不带限定符的情况下，subscription_id 和 resource_group_name 会应用于全部三个资源。

```sql
SELECT  c.name AS vm_name,
        SPLIT_PART(JSON_EXTRACT(c.properties,'$.networkProfile.networkInterfaces[0].id'), '/', -1) AS nic_name
FROM
  azure.compute.virtual_machine_scale_sets a
  INNER JOIN  azure.compute.virtual_machine_scale_set_vms b
    ON a.name = b.virtualMachineScaleSetName
  INNER JOIN  azure.compute.virtual_machines c
    ON b.name = c.name
WHERE subscription_id = '0123456789'
AND resource_group_name = 'vmss-flex'
AND JSON_EXTRACT(c.properties,'$.priority') is null;
```

有关窗口函数的更多信息，请参阅[窗口函数](/language-spec/windowing_functions)。

### 通过 `WITH` 使用公用表表达式（CTE） {#using-common-table-expressions-ctes-with-with}

CTE 可用于定义临时的命名结果集，这些结果集可在主查询中引用。

```sql
-- Combine and rank contributors across multiple repositories
WITH all_contributors AS (
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions,
    MAX(html_url) as html_url
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

有关 CTE 的更多信息，请参阅 [WITH (CTEs)](/language-spec/with)。
