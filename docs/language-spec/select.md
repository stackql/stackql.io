---
title: SELECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Returns an instance or instances of a resource.  

See also:
[[ StackQL Resource Hierarchy ]](/docs/getting-started/resource-hierarchy) [[` WITH (CTEs) `]](/docs/language-spec/with) [[` Window Functions `]](/docs/language-spec/windowing_functions)

* * * 

## Syntax

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

* * *

## Examples

### Basic `SELECT` Statement
Run a basic `SELECT` statement in an authenticated session.

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

### Returning specified fields using a `SELECT` Statement
Run a `SELECT` statement with a column list to return specified fields from a Compute Engine instance.

```sql
-- Selecting specified fields from a resource
SELECT id, name 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Return a result set ordered by a column in ascending order using the `ORDER BY` clause
Run a `SELECT` statement to return fields from the Cloud Storage buckets resource, ordering the results in ascending order (oldest first). 

```sql
-- Order a list of Cloud Storage buckets by creation time (in ascending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated;
```

### Return a result set ordered by a column in descending order using the `ORDER BY` clause
Run a `SELECT` statement to return fields from the Cloud Storage buckets resource, ordering the results in descending order (newest first).

```sql
-- Order a list of Cloud Storage buckets by creation time (in descending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC;
```

### Get the top result from a column using the `ORDER BY` and `LIMIT` clauses
Run a `SELECT` statement to return the most recently created Cloud Storage bucket.

```sql
-- Find the most recently created bucket
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC LIMIT 1;
```

### Use the `COUNT` function with the `GROUP BY` and `HAVING` clauses to count resources based upon a grouping and filter
Run a `SELECT` statement with a `COUNT` function, using the `GROUP BY` and `HAVING` clauses to return the number of buckets in every location other than Asia.

```sql
-- Return a count of the number of buckets in every location other than Asia
SELECT name, location, count(*) as num_buckets 
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location
HAVING location != 'ASIA';
```

For more information on the `COUNT` function and other aggregate functions supported by StackQL see [Aggregate Functions](/docs/language-spec/functions/aggregate/count).

### Get list of On Demand VMs and corresponding network interface from Azure VM Scaleset
Run a `SELECT` statement using the [`SPLIT_PART`](/docs/language-spec/functions/string/split_part) and [`JSON_EXTRACT`](/docs/language-spec/functions/json/json_extract) functions to get list of OD VMs and their NICs. Note that without a qualifier the subscriptionId and resourceGroupName is applied to all the three Resources.

```sql
SELECT  c.name AS vm_name,
        SPLIT_PART(JSON_EXTRACT(c.properties,'$.networkProfile.networkInterfaces[0].id'), '/', -1) AS nic_name
FROM
  azure.compute.virtual_machine_scale_sets a
  INNER JOIN  azure.compute.virtual_machine_scale_set_vms b
    ON a.name = b.virtualMachineScaleSetName
  INNER JOIN  azure.compute.virtual_machines c
    ON b.name = c.name
WHERE subscriptionId = '0123456789'
AND resourceGroupName = 'vmss-flex'
AND JSON_EXTRACT(c.properties,'$.priority') is null;
```

For more information on window functions, see [Window Functions](/docs/language-spec/windowing_functions).

### Using Common Table Expressions (CTEs) with `WITH`

CTEs allow you to define temporary named result sets that can be referenced in the main query.

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

For more information on CTEs, see [WITH (CTEs)](/docs/language-spec/with).
