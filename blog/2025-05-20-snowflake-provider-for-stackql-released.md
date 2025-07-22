---
slug: snowflake-provider-for-stackql-released
title: Snowflake Provider for StackQL Released
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-snowflake-provider-featured-image.png"
description: Query and interact with Snowflake resources using SQL.
keywords: [stackql, snowflake, iac, analytics]
tags: [stackql, snowflake, iac, analytics]
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

We're excited to announce the release of the Snowflake provider for StackQL! This new provider enables you to query and interact with your Snowflake resources using familiar SQL syntax, bridging the gap between data analytics and infrastructure management.

The Snowflake provider for StackQL gives you the ability to:

- Query Snowflake metadata and statistics using SQL
- Monitor warehouse, database, and query performance
- Analyze resource usage and optimize costs
- Integrate Snowflake management with your existing cloud infrastructure
- Build cross-provider workflows and automation

Full documentation for the Snowflake provider is available at [https://registry.stackql.io/snowflake](https://registry.stackql.io/snowflake).

## Getting Started

Getting started is as easy as...

```sql
REGISTRY PULL snowflake;
```

## Example Queries

Let's explore some powerful examples of what you can do with the Snowflake provider for StackQL.

### Analyzing Warehouses

<Tabs
  defaultValue="query"
  values={[
    { label: 'Query', value: 'query', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="query">

```sql
SELECT 
  size, 
  count(*) as num_warehouses
FROM 
  snowflake.warehouse.warehouses
WHERE 
  endpoint = 'OKXVNMC-VH34026'
GROUP BY 
  size;
```

</TabItem>
<TabItem value="results">

```bash
|---------|----------------|
|  size   | num_warehouses |
|---------|----------------|
| X-Small |             12 |
|---------|----------------|
| Small   |              8 |
|---------|----------------|
| Medium  |              5 |
|---------|----------------|
| Large   |              3 |
|---------|----------------|
| X-Large |              1 |
|---------|----------------|
```

</TabItem>
</Tabs>

Other fields for the `warehouses` resource include : `name`, `warehouse_type`, `state`, `scaling_policy`, `auto_suspend`, `auto_resume`, `resource_monitor`, `enable_query_acceleration`, `query_acceleration_max_scale_factor`, `max_concurrency_level`, `owner`, `warehouse_credit_limit`, `target_statement_size` and more.

### Table Analysis

<Tabs
  defaultValue="query"
  values={[
    { label: 'Query', value: 'query', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="query">

```sql
SELECT 
  name, 
  bytes, 
  data_retention_time_in_days, 
  table_type 
FROM 
  snowflake.table.tables 
WHERE 
  database_name = 'SNOWFLAKE_SAMPLE_DATA' 
  AND schema_name = 'TPCH_SF10' 
  AND endpoint = 'OKXVNMC-VH34026' 
ORDER BY 
  bytes DESC;
```

</TabItem>
<TabItem value="results">

```bash
|----------|------------|-----------------------------|------------|
|   name   |   bytes    | data_retention_time_in_days | table_type |
|----------|------------|-----------------------------|------------|
| LINEITEM | 1717059584 |                           1 | NORMAL     |
|----------|------------|-----------------------------|------------|
| ORDERS   |  446439424 |                           1 | NORMAL     |
|----------|------------|-----------------------------|------------|
| PARTSUPP |  373145600 |                           1 | NORMAL     |
|----------|------------|-----------------------------|------------|
| CUSTOMER |  108249088 |                           1 | NORMAL     |
|----------|------------|-----------------------------|------------|
| PART     |   52776960 |                           1 | NORMAL     |
|----------|------------|-----------------------------|------------|
| SUPPLIER |    6791168 |                           1 | NORMAL     |
|----------|------------|-----------------------------|------------|
| NATION   |       4096 |                           1 | NORMAL     |
|----------|------------|-----------------------------|------------|
| REGION   |       4096 |                           1 | NORMAL     |
|----------|------------|-----------------------------|------------|
```

</TabItem>
</Tabs>

## Other Services and Resources

Other notable resources which can be provisioned, managed or queried using the `snowflake` provider for `stackql` include:  

`alerts`, `api_integrations`, `catalog_integrations`, `compute_pools`, `databases`, `database_roles`, `dynamic_tables`, `event_tables`, `external_volumes`, `functions`, `grants`, `iceberg_tables`, `image_repositories`, `network_policies`, `notebooks`,  `notification_integrations`, `pipes`, `procedures`, `roles`, `schemas`, `stages`, `streams`, `tasks`, `users`, `user_defined_functions`, `views`, and more!

## Use Cases for the Snowflake Provider

The Snowflake provider for StackQL opens up numerous possibilities:

1. **Infrastructure as Code**: Manage your Snowflake resources alongside other cloud providers in a unified IaC approach, see [__`stackql-deploy`__](https://stackql-deploy.io/).

2. **Cost Optimization**: Identify unused resources, inefficient warehouses, and opportunities for cost savings.

3. **Security and Compliance**: Audit account roles, permissions, and access patterns to ensure compliance with security policies.

4. **Performance Monitoring**: Track query performance, warehouse utilization, and identify optimization opportunities.

5. **Cross-Provider Orchestration**: Build workflows that span Snowflake and other cloud providers, enabling sophisticated data and infrastructure pipelines.

6. **Automated Reporting**: Create automated reports on Snowflake usage, performance, and costs.

⭐ us on [__GitHub__](https://github.com/stackql/stackql) and join our community!