---
slug: new-clickhouse-provider-available
title: New ClickHouse Cloud Provider Available
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-clickhouse-provider-featured-image.png"
description: A new StackQL provider for the ClickHouse Cloud API - query and provision organizations, services, API keys, members and roles, backups, ClickPipes, ClickStack dashboards and alerts, UDFs and Managed Postgres, and report usage cost by day and entity, using SQL - available in the StackQL Provider Registry now.
keywords: [stackql, clickhouse, clickhouse cloud, provider, finops, clickstack, clickpipes, observability, infrastructure as code]
tags: [stackql, clickhouse, clickhouse-cloud, provider, finops, clickstack, observability]
---

We've released a new StackQL provider for ClickHouse Cloud:

- [__`clickhouse`__](https://clickhouse-provider.stackql.io) - the ClickHouse Cloud control plane (`api.clickhouse.cloud`): organizations, services (lifecycle, scaling, settings, passwords, private endpoints), API keys, members and invitations, organization roles, backups and backup configuration, usage cost and quotas, activities, ClickPipes, the ClickStack surface (dashboards, alerts, sources, webhooks, saved searches), user-defined functions and Managed Postgres (10 services, 44 resources, 139 operations)

The provider covers the Cloud management API only. The ClickHouse server HTTP interface - SQL against a service endpoint and the `system.*` tables - is a separate surface with separate authentication and is reserved as a future sibling provider, `clickhouse_server`. What takes two Terraform providers today (the Cloud infrastructure provider and the DBops provider) will be two namespaces in one StackQL session.

## Organization scope from the environment

Every resource except `organizations` is scoped to an organization. The organization ID is a server variable that StackQL resolves from `CLICKHOUSE_ORG_ID` when it is set, so queries carry no organization clause:

```sql
SELECT name, state, provider, region
FROM clickhouse.services.services;
```

A `WHERE organization_id = '...'` still takes precedence when you need to address another organization in the same session. Columns and `WHERE`/`INSERT` keys are snake_case (`created_at`, `ip_access_list`, `service_id`); the provider maps them to the API's camelCase on the wire.

## Estate inventory

State, footprint and scaling configuration for every service in one result set:

```sql
SELECT name, state, provider, region,
       num_replicas,
       min_replica_memory_gb, max_replica_memory_gb,
       json_extract(current_scaling, '$.effectiveAutoscalingMode') AS scaling_mode,
       idle_scaling, idle_timeout_minutes, clickhouse_version
FROM clickhouse.services.services
ORDER BY provider, region, name;
```

Organization quotas report usage against limits, which is the quickest answer to "how much room is left":

```sql
SELECT quota_code, name, value AS quota_limit, usage
FROM clickhouse.organizations.quotas;
```

## Usage cost by day and entity

The usage cost endpoint returns one row per entity per day in ClickHouse Credits, with the compute, storage, backup and data-transfer components in a `metrics` object:

```sql
SELECT date, entity_type, entity_name, total_chc,
       json_extract(metrics, '$.computeCHC') AS compute_chc,
       json_extract(metrics, '$.storageCHC') AS storage_chc,
       json_extract(metrics, '$.backupCHC')  AS backup_chc
FROM clickhouse.organizations.usage_costs
WHERE from_date = '2026-08-01' AND to_date = '2026-08-31'
ORDER BY date, entity_name;
```

Joined to the services list, the same data answers which stopped or idle services still carried cost in the window:

```sql
SELECT s.name, s.state, SUM(c.total_chc) AS chc_in_window
FROM clickhouse.services.services s
JOIN clickhouse.organizations.usage_costs c
  ON c.service_id = s.id
WHERE c.from_date = '2026-08-01' AND c.to_date = '2026-08-31'
  AND s.state IN ('stopped', 'idle')
GROUP BY s.name, s.state
ORDER BY chc_in_window DESC;
```

## Scaffolding

Provisioning is the usual SQL verbs. A service is an `INSERT`; the state command (`start`, `stop`, `awake`) is an `EXEC`; access-list changes are an `UPDATE` whose `PATCH` body takes `add`/`remove` arrays, passed through as written:

```sql
INSERT INTO clickhouse.services.services
  (name, provider, region, min_replica_memory_gb, max_replica_memory_gb, num_replicas, idle_scaling, idle_timeout_minutes)
SELECT 'analytics-dev', 'aws', 'us-east-1', 8, 8, 1, true, 15;

UPDATE clickhouse.services.services
SET ip_access_list = '{"add": [{"source": "203.0.113.0/24", "description": "office"}],
                       "remove": [{"source": "0.0.0.0/0", "description": "Anywhere"}]}'
WHERE service_id = '<service-uuid>';

EXEC clickhouse.services.services.update_state
  @serviceId = '<service-uuid>',
  @command = 'stop';
```

Organizations that use Custom Roles assign API key roles by ID, so the role lookup and the key creation are one statement, and the generated secret comes back with `RETURNING`:

```sql
INSERT INTO clickhouse.keys.keys (name, assigned_role_ids, state)
SELECT 'finops-reader', '["' || id || '"]', 'enabled'
FROM clickhouse.roles.roles
WHERE name = 'Organization API Reader'
RETURNING json_extract(result, '$.key.id')    AS id,
          json_extract(result, '$.keyId')     AS key_id,
          json_extract(result, '$.keySecret') AS key_secret;
```

Backup configuration is queryable across the estate, and settable per service:

```sql
SELECT s.name, b.backup_period_in_hours, b.backup_retention_period_in_hours, b.backup_start_time
FROM clickhouse.services.services s
JOIN clickhouse.backups.backup_configurations b
  ON b.service_id = s.id;
```

## Observability as code

ClickStack dashboards, alerts, sources, webhooks and saved searches are resources with full CRUD, so a dashboard definition lives in version control and is applied with an `INSERT`:

```sql
INSERT INTO clickhouse.clickstack.dashboards (service_id, name, tiles, tags)
SELECT '<service-uuid>', 'Service Overview',
       '[{"name": "Error rate", "x": 0, "y": 0, "w": 6, "h": 3,
          "config": {"displayType": "line",
                     "select": [{"aggFn": "count", "where": "SeverityText = ''ERROR''"}]}}]',
       '["production"]';
```

and audited with a `SELECT`:

```sql
SELECT d.name, json_array_length(d.tiles) AS tiles, d.tags
FROM clickhouse.clickstack.dashboards d
WHERE d.service_id = '<service-uuid>';
```

Alerts and webhooks follow the same pattern (`clickhouse.clickstack.alerts`, `clickhouse.clickstack.webhooks`), and the organization activity log is a table for audit questions:

```sql
SELECT created_at, type, actor_type, actor_details
FROM clickhouse.organizations.activities
ORDER BY created_at DESC;
```

## The data platform estate in one query

Cross-provider joins are ordinary SQL, so ClickHouse Cloud services sit alongside Snowflake warehouses and Databricks clusters in one inventory:

```sql
SELECT 'clickhouse' AS platform, name, state, region
FROM clickhouse.services.services
UNION ALL
SELECT 'snowflake', name, state, NULL
FROM snowflake.warehouses.warehouses
UNION ALL
SELECT 'databricks', cluster_name, state, NULL
FROM databricks_workspace.compute.clusters
WHERE deployment_name = '<workspace>';
```

## Authentication

Create an API key pair in the ClickHouse Cloud console (Settings -> API Keys) with the role the queries need - `Organization API Reader` / `Service API Reader` for inventory and cost queries, `Service API Admin` for provisioning - and export three variables:

```bash
export CLICKHOUSE_CLOUD_API_KEY=...      # Key ID
export CLICKHOUSE_CLOUD_API_SECRET=...   # Key Secret
export CLICKHOUSE_ORG_ID=...             # organization ID
```

The API allows a fixed window of requests per key (documented as 10 per 10 seconds); a `429` is the signal to pace wide scans.

## Get started

Pull the provider from the public registry:

```bash
registry pull clickhouse
```

Provider docs are at [clickhouse-provider.stackql.io](https://clickhouse-provider.stackql.io). Let us know what you build. Star us on [__GitHub__](https://github.com/stackql/stackql).
