---
title: SELECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
description: "StackQL의 SELECT: API 매개 변수에 매핑되는 WHERE 필터, 조인, 그룹화, 정렬, JSON 필드 액세스를 사용하여 SQL로 클라우드 및 SaaS 리소스를 쿼리합니다."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

리소스의 인스턴스를 하나 이상 반환합니다.  

참고 항목:
[[ StackQL 리소스 계층 구조 ]](/getting-started/resource-hierarchy) [[` WITH (CTEs) `]](/language-spec/with) [[` 윈도우 함수 `]](/language-spec/windowing_functions)

* * * 

## 구문 {#syntax}

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

`ON` 절이 없는 `JOIN` 절은 `CROSS JOIN`으로 처리되므로, 조인 조건을 대신 `WHERE` 절에 지정할 수 있습니다.  `v0.12.732` 이전의 StackQL 릴리스에서는 조건이 없는 `JOIN` 절의 실행 계획을 수립하지 못합니다.

* * *

## 예제 {#examples}

### 기본 `SELECT` 문 {#basic-select-statement}
인증된 세션에서 기본 `SELECT` 문을 실행합니다.

```sql
-- Selecting all instances in a zone using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

### `SELECT` 문으로 지정한 필드 반환 {#returning-specified-fields-using-a-select-statement}
열 목록을 포함한 `SELECT` 문을 실행하여 Compute Engine 인스턴스에서 지정한 필드를 반환합니다.

```sql
-- Selecting specified fields from a resource
SELECT id, name 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### `ORDER BY` 절로 열 기준 오름차순 정렬된 결과 집합 반환 {#return-a-result-set-ordered-by-a-column-in-ascending-order-using-the-order-by-clause}
`SELECT` 문을 실행하여 Cloud Storage 버킷 리소스에서 필드를 반환하고, 결과를 오름차순(오래된 항목부터)으로 정렬합니다. 

```sql
-- Order a list of Cloud Storage buckets by creation time (in ascending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated;
```

### `ORDER BY` 절로 열 기준 내림차순 정렬된 결과 집합 반환 {#return-a-result-set-ordered-by-a-column-in-descending-order-using-the-order-by-clause}
`SELECT` 문을 실행하여 Cloud Storage 버킷 리소스에서 필드를 반환하고, 결과를 내림차순(최신 항목부터)으로 정렬합니다.

```sql
-- Order a list of Cloud Storage buckets by creation time (in descending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC;
```

### `ORDER BY` 절과 `LIMIT` 절로 열의 최상위 결과 조회 {#get-the-top-result-from-a-column-using-the-order-by-and-limit-clauses}
`SELECT` 문을 실행하여 가장 최근에 생성된 Cloud Storage 버킷을 반환합니다.

```sql
-- Find the most recently created bucket
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC LIMIT 1;
```

### `COUNT` 함수와 `GROUP BY` 및 `HAVING` 절을 사용하여 그룹화 및 필터 기준으로 리소스 개수 계산 {#use-the-count-function-with-the-group-by-and-having-clauses-to-count-resources-based-upon-a-grouping-and-filter}
`COUNT` 함수를 포함한 `SELECT` 문을 실행하고, `GROUP BY` 절과 `HAVING` 절을 사용하여 아시아를 제외한 모든 위치의 버킷 수를 반환합니다.

```sql
-- Return a count of the number of buckets in every location other than Asia
SELECT location, count(*) as num_buckets 
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location
HAVING location != 'ASIA';
```

`COUNT` 함수 및 StackQL에서 지원하는 기타 집계 함수에 대한 자세한 내용은 [집계 함수](/language-spec/functions/aggregate/count)를 참조하세요.

### Azure VM 확장 집합에서 온디맨드 VM 및 해당 네트워크 인터페이스 목록 조회 {#get-list-of-on-demand-vms-and-corresponding-network-interface-from-azure-vm-scaleset}
[`SPLIT_PART`](/language-spec/functions/string/split_part) 함수와 [`JSON_EXTRACT`](/language-spec/functions/json/json_extract) 함수를 사용하는 `SELECT` 문을 실행하여 온디맨드(OD) VM과 해당 NIC 목록을 조회합니다. 한정자가 없으면 `subscription_id` 및 `resource_group_name` 매개 변수가 세 리소스 모두에 적용됩니다.

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

### `WITH` 절로 공통 테이블 식(CTE) 사용 {#using-common-table-expressions-ctes-with-with}

CTE를 사용하면 주 쿼리에서 참조할 수 있는, 이름이 지정된 임시 결과 집합을 정의할 수 있습니다.

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

CTE에 대한 자세한 내용은 [WITH (CTEs)](/language-spec/with)를 참조하세요.

윈도우 함수에 대한 자세한 내용은 [윈도우 함수](/language-spec/windowing_functions)를 참조하세요.
