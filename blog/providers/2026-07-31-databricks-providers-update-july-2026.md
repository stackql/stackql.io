---
slug: databricks-providers-update-july-2026
title: Databricks Providers Update - July 2026
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-databricks-provider-featured-image.png"
description: Update to the StackQL Databricks providers adding four new services including disaster recovery, AI Search, bundle deployments and supervisor agents, a major expansion of Lakebase (Postgres) and identity coverage, and improved SQL statement execution semantics including DELETE to cancel running statements.
keywords: [stackql, databricks, provider, lakebase, ai search, agent bricks, asset bundles, disaster recovery, unity catalog, sql statement execution]
tags: [stackql, databricks, provider, lakebase, unity-catalog, agent-bricks]
---

We've released an update to the StackQL Databricks providers, regenerated from the latest Databricks platform APIs (SDK v0.123.0):

- [__`databricks_account`__](https://databricks-account-provider.stackql.io/) - account-level operations: __9 services, 79 resources__
- [__`databricks_workspace`__](https://databricks-workspace-provider.stackql.io/) - workspace-level operations: __29 services, 313 resources__

Includes __392 resources and over 1,200 operations__ across both providers, including four new services and more than 70 new or restructured resources.

## New Services

| Provider | Service | Description |
|----------|---------|-------------|
| `databricks_account` | `disasterrecovery` | Manage disaster recovery failover groups and stable URLs for workspace failover across regions |
| `databricks_workspace` | `aisearch` | Databricks AI Search endpoints and indexes, including data plane operations to query, scan, sync and upsert index data |
| `databricks_workspace` | `bundledeployments` | Databricks Asset Bundle deployments - deployments, versions, operations and deployed resources |
| `databricks_workspace` | `supervisoragents` | Agent Bricks supervisor agents - agents, tools, examples and permissions |

## Expanded Coverage in Existing Services
- `postgres` (Lakebase) - the largest expansion in this release, now 24 resources covering projects, branches, databases, roles, endpoints, tables, synced tables, catalogs, snapshots, compute instances, change data feed configs and statuses, forward ETL, replication group previews and recovery branch previews
- `iamv2` (account and workspace) - the restructured identity APIs: full lifecycle for `users`, `groups`, `service_principals`, `direct_group_members`, `workspace_access_details`, `workspace_assignment_details`, `external_users` and `transitive_parent_groups`, plus account access identity rules and attribute control entries
- `catalog` - Unity Catalog AI Gateway services (`ai_gateway_model_services`, `ai_gateway_mcp_services`, `ai_gateway_agent_services`, `ai_gateway_model_provider_services`), privilege assignments (direct and effective), UC secrets and temporary volume credentials
- `dashboards` - AI/BI Genie evaluation runs and results
- `apps` - app spaces, app space operations and app thumbnails
- `compute` - default base environments for serverless environments and library management
- `vectorsearch` - vector search index management and endpoint permissions
- `ml` - feature engineering streams
- `billing` (account) - usage policies

## SQL Statement Execution Improvements

StackQL lets you traverse the Databricks control plane and data plane in the same query surface. This release cleans up the verb mappings for `databricks_workspace.sql.statement_execution`, so the full statement lifecycle maps naturally to SQL:

~~~sql
/* submit a statement to a SQL warehouse */
INSERT INTO databricks_workspace.sql.statement_execution (
  statement,
  warehouse_id,
  wait_timeout,
  deployment_name
)
SELECT
  'SELECT * FROM samples.nyctaxi.trips LIMIT 100',
  '<warehouse_id>',
  '0s',
  '<deployment_name>';

/* poll for status and results */
SELECT status, manifest, result
FROM databricks_workspace.sql.statement_execution
WHERE statement_id = '<statement_id>'
AND deployment_name = '<deployment_name>';

/* cancel a running statement */
DELETE FROM databricks_workspace.sql.statement_execution
WHERE statement_id = '<statement_id>'
AND deployment_name = '<deployment_name>';
~~~

The `cancel` operation is now mapped to the `DELETE` verb (previously `INSERT`), and `sql.query_history` now projects one row per query with server-side pagination wired in, so large result sets append across pages automatically.

## Built-in Views

Both providers ship with curated views that flatten common multi-step queries into a single `SELECT`, and this release adds views for Lakebase alongside the existing IAM, billing, provisioning, networking and settings views - 49 views in total. For example:

~~~sql
SELECT *
FROM databricks_account.iam.vw_account_user_roles
WHERE account_id = '<account_id>';

SELECT key, value
FROM databricks_workspace.settings.vw_all_settings
WHERE deployment_name = '<deployment_name>';
~~~

## Get Started

Pull the latest providers from the public registry:

~~~bash
stackql registry pull databricks_account
stackql registry pull databricks_workspace
~~~

Authenticate with the same service principal environment variables used by Terraform and the Databricks CLI (`DATABRICKS_ACCOUNT_ID`, `DATABRICKS_CLIENT_ID`, `DATABRICKS_CLIENT_SECRET`), then explore:

~~~sql
SELECT workspace_id, workspace_name, workspace_status, deployment_name
FROM databricks_account.provisioning.workspaces
WHERE account_id = '<account_id>';
~~~

Provider docs are at [databricks-account-provider.stackql.io](https://databricks-account-provider.stackql.io/) and [databricks-workspace-provider.stackql.io](https://databricks-workspace-provider.stackql.io/). Visit us on [__GitHub__](https://github.com/stackql/stackql) and let us know how you're using it.
