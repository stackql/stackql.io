---
slug: introducing-materialized-views-with-stackql
title: Introducing Materialized Views with StackQL
hide_table_of_contents: false
authors:  
  - kieranrimmer
image: "/img/blog/stackql-featured-image.png"
keywords: [stackql, analytics]
tags: [stackql, analytics]
---

Materialized Views are now available in StackQL.  Materialized Views can be used to improve performance for dependent or repetetive queries within StackQL provisioning or analytics routines.

## Refresher on Materialized Views 

Unlike standard views that provide a virtual representation of data, a Materialized View physically stores the result set of a query. This implies that the data is pre-computed and stored, which can lead to performance gains as the data doesn't need to be fetched from the underlying resource(s) every time it is queried.

## Benefits of Materialized Views in StackQL

1. **Performance Boost**: With data already stored and readily available, Materialized Views can substantially reduce StackQL query execution time, especially for complex and frequently-run queries.
 
2. **Data Consistency**: Since Materialized Views provide a snapshot of the data at a specific point in time, it ensures consistent data is returned every time it is accessed until it is refreshed.

3. **Flexibility**: You have the flexibility to refresh the Materialized View as needed usign the `REFRESH MATERIALIZED VIEW` lifecycle operation in StackQL. This is particularly useful when working with rapidly changing data.

## Using Materialized Views in StackQL

Here's a step-by-step guide on how you to use this new feature in StackQL:

1. **Create a Materialized View**: 
```sql
CREATE MATERIALIZED VIEW vw_ec2_instance_types AS 
SELECT 
 memoryInfo, 
 hypervisor, 
 autoRecoverySupported, 
 instanceType, 
 SPLIT_PART(processorInfo, '\n', 3) as processorArch, 
 currentGeneration, 
 freeTierEligible, 
 hibernationSupported,
 SPLIT_PART(vCpuInfo, '\n', 2) as vCPUs, 
 bareMetal, 
 burstablePerformanceSupported, 
 dedicatedHostsSupported 
FROM aws.ec2.instance_types 
WHERE region = 'us-east-1';
```

2. **Refresh the Materialized View**:
```sql
REFRESH MATERIALIZED VIEW vw_ec2_instance_types;
```

3. **Use the Materialized View in a StackQL Query**: 
```sql
SELECT 
 i.instanceId, 
 i.instanceType, 
 it.vCPUs, 
 it.memoryInfo 
FROM aws.ec2.instances i 
 INNER JOIN vw_ec2_instance_types it 
 ON i.instanceType = it.instanceType 
WHERE i.region = 'us-east-1';
```

3. **Drop the Materialized View**: 
```sql
DROP MATERIALIZED VIEW vw_ec2_instance_types;
```
More information on Materialized Views in StackQL can be found [here](/docs/language-spec/createview).