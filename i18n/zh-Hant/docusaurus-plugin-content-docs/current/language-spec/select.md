---
title: SELECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
description: "StackQL 中的 SELECT：以 SQL 查詢雲端與 SaaS 資源，支援對應至 API 參數的 WHERE 篩選條件、聯結、分組、排序與 JSON 欄位存取。"
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

傳回資源的一個或多個執行個體。

另請參閱：
[[ StackQL 資源階層 ]](/getting-started/resource-hierarchy) [[` WITH (CTEs) `]](/language-spec/with) [[` 視窗函式 `]](/language-spec/windowing_functions)

* * * 

## 語法 {#syntax}

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

沒有 `ON` 子句的 `JOIN` 會被視為 `CROSS JOIN`，因此聯結條件可以改在 `WHERE` 子句中指定。`v0.12.732` 之前的 StackQL 發行版本無法規劃沒有條件的 `JOIN`。

* * *

## 範例 {#examples}

### 基本 `SELECT` 陳述式 {#basic-select-statement}
在已驗證的工作階段中執行基本 `SELECT` 陳述式。

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

### 使用 `SELECT` 陳述式傳回指定欄位 {#returning-specified-fields-using-a-select-statement}
執行帶有資料行清單的 `SELECT` 陳述式，從 Compute Engine 執行個體傳回指定欄位。

```sql
-- Selecting specified fields from a resource
SELECT id, name 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### 使用 `ORDER BY` 子句傳回依資料行遞增排序的結果集 {#return-a-result-set-ordered-by-a-column-in-ascending-order-using-the-order-by-clause}
執行 `SELECT` 陳述式，從 Cloud Storage 儲存貯體資源傳回欄位，並將結果依遞增順序排序（最舊的在前）。

```sql
-- Order a list of Cloud Storage buckets by creation time (in ascending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated;
```

### 使用 `ORDER BY` 子句傳回依資料行遞減排序的結果集 {#return-a-result-set-ordered-by-a-column-in-descending-order-using-the-order-by-clause}
執行 `SELECT` 陳述式，從 Cloud Storage 儲存貯體資源傳回欄位，並將結果依遞減順序排序（最新的在前）。

```sql
-- Order a list of Cloud Storage buckets by creation time (in descending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC;
```

### 使用 `ORDER BY` 與 `LIMIT` 子句取得資料行的第一筆結果 {#get-the-top-result-from-a-column-using-the-order-by-and-limit-clauses}
執行 `SELECT` 陳述式，傳回最近建立的 Cloud Storage 儲存貯體。

```sql
-- Find the most recently created bucket
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC LIMIT 1;
```

### 搭配 `GROUP BY` 與 `HAVING` 子句使用 `COUNT` 函式，依分組與篩選條件計算資源數量 {#use-the-count-function-with-the-group-by-and-having-clauses-to-count-resources-based-upon-a-grouping-and-filter}
執行帶有 `COUNT` 函式的 `SELECT` 陳述式，並使用 `GROUP BY` 與 `HAVING` 子句，傳回亞洲以外每個位置的儲存貯體數量。

```sql
-- Return a count of the number of buckets in every location other than Asia
SELECT location, count(*) as num_buckets 
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location
HAVING location != 'ASIA';
```

如需 `COUNT` 函式及 StackQL 支援的其他彙總函式的詳細資訊，請參閱[彙總函式](/language-spec/functions/aggregate/count)。

### 從 Azure VM 擴展集取得隨選 VM 清單及其對應的網路介面 {#get-list-of-on-demand-vms-and-corresponding-network-interface-from-azure-vm-scaleset}
執行使用 [`SPLIT_PART`](/language-spec/functions/string/split_part) 與 [`JSON_EXTRACT`](/language-spec/functions/json/json_extract) 函式的 `SELECT` 陳述式，取得隨選（OD）VM 及其 NIC 的清單。請注意，在沒有限定詞的情況下，`subscription_id` 與 `resource_group_name` 會套用至全部三個資源。

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

### 搭配 `WITH` 使用通用資料表運算式（CTE） {#using-common-table-expressions-ctes-with-with}

CTE 可讓您定義暫時的具名結果集，並在主查詢中參考這些結果集。

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

如需 CTE 的詳細資訊，請參閱 [WITH (CTEs)](/language-spec/with)。

如需視窗函式的詳細資訊，請參閱[視窗函式](/language-spec/windowing_functions)。
