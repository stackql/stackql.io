---
slug: new-databricks-providers-available
title: New Databricks Providers for StackQL Released
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-databricks-provider-featured-image.png"
description: Updated providers for Databricks released covering account and workspace operations, with over 30 services, 300 resources, and nearly 1,000 operations queryable using SQL.
keywords: [stackql, databricks, provider, infrastructure-as-code, iac, sql, databricks account, databricks workspace]
tags: [stackql, databricks, provider, infrastructure-as-code]
---

Updated StackQL providers for Databricks are now available: [__`databricks_account`__](https://databricks-account-provider.stackql.io/) and [__`databricks_workspace`__](https://databricks-workspace-provider.stackql.io/), giving you SQL access to the full Databricks control plane across account-level and workspace-level operations.


## Provider Structure

The following updated providers are available:

| Provider | Scope | Services |
|----------|-------|----------|
| `databricks_account` | Account | 8 |
| `databricks_workspace` | Workspace | 26 |

## Coverage

There are over **30 services**, **300+ resources**, and **983 operations** spanning IAM, compute, catalog, billing, jobs, ML, serving, sharing, vector search, and more.

## Example Queries

### List workspaces in an account

```sql
SELECT
  workspace_id,
  workspace_name,
  workspace_status,
  aws_region,
  compute_mode,
  deployment_name,
  datetime(creation_time/1000, 'unixepoch') as creation_date_time
FROM databricks_account.provisioning.workspaces
WHERE account_id = 'ebfcc5a9-9d49-4c93-b651-b3ee6cf1c9ce';
```

### Query account users and roles

```sql
SELECT
  id as user_id,
  displayName as display_name,
  userName as user_name,
  active,
  IIF(JSON_EXTRACT(roles,'$[0].value') = 'account_admin', 'true', 'false') as is_account_admin
FROM databricks_account.iam.account_users
WHERE account_id = 'ebfcc5a9-9d49-4c93-b651-b3ee6cf1c9ce';
```

### List catalogs in a workspace

```sql
SELECT
  full_name,
  catalog_type,
  comment,
  datetime(created_at/1000, 'unixepoch') as created_at,
  created_by,
  datetime(updated_at/1000, 'unixepoch') as updated_at,
  updated_by,
  enable_predictive_optimization
FROM databricks_workspace.catalog.catalogs
WHERE deployment_name = 'dbc-36ff48e3-4a69';
```

### Download billable usage to CSV

This one is worth calling out. You can pull billable usage data for a given period and write it straight to a CSV file:

```bash
./stackql exec \
  -o text \
  --hideheaders \
  -f billable_usage.csv \
  "SELECT contents
  FROM databricks_account.billing.billable_usage
  WHERE start_month = '2025-12'
  AND end_month = '2026-01'
  AND account_id = 'your-account-id'"
```

## Authentication

Both providers authenticate using OAuth2 with a Databricks service principal. Set the following environment variables:

```bash
export DATABRICKS_ACCOUNT_ID="your-account-id"
export DATABRICKS_CLIENT_ID="your-client-id"
export DATABRICKS_CLIENT_SECRET="your-client-secret"
```

These are the same variables used by Terraform, the Databricks SDKs, and the Databricks CLI.

## Get Started

Pull the providers:

```sql
registry pull databricks_account;
registry pull databricks_workspace;
```

Start querying via the `shell` or `exec`:

```sql
SELECT * FROM databricks_account.iam.account_groups WHERE account_id = 'your-account-id';
```

Full documentation is available at [databricks-account-provider.stackql.io](https://databricks-account-provider.stackql.io/) and [databricks-workspace-provider.stackql.io](https://databricks-workspace-provider.stackql.io/). Let us know what you think on [__GitHub__](https://github.com/stackql/stackql).
