---
slug: stackql-in-databricks-web-terminal
title: Run StackQL Queries from the Databricks Web Terminal
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-databricks-provider-featured-image.png"
description: Run StackQL SQL queries against your Databricks workspace directly from the Databricks web terminal.
keywords: [stackql, databricks, sql, databricks workspace]
tags: [stackql, databricks, sql, databricks workspace]
---

If you have access to a Databricks workspace, you can run StackQL queries directly from the Databricks Web Terminal using your Databricks identity.

## How It Works

Download the latest release of `stackql`, then run the convenience script included (similar scripts are included for other cloud provider terminals - e.g. AWS Cloud Shell).

```bash
curl -L https://bit.ly/stackql-zip -O && unzip stackql-zip
sh stackql-databricks-shell.sh
```

<video controls width="100%" preload="metadata">
  <source src="https://pub-690c90d9ca2e4ad09b9455b620bd429f.r2.dev/databricks-stackql-web-terminal-demo.mp4" type="video/mp4" />
</video>

## Example Queries

Here are the sample queries run in the video, just change the `deployment_name` for your workspace.

**User entitlements**

```sql
SELECT
  deployment_name,
  id,
  userName,
  displayName,
  entitlement
FROM databricks_workspace.iam.vw_user_entitlements
WHERE deployment_name = 'dbc-74aa95f7-8c7e';
```

**All workspace settings**

```sql
SELECT * FROM
  databricks_workspace.settings.vw_all_settings
WHERE deployment_name = 'dbc-74aa95f7-8c7e';
```

**Tag policies filtered by key prefix**

```sql
SELECT
  tag_key as key,
  description
FROM databricks_workspace.tags.tag_policies
WHERE deployment_name = 'dbc-74aa95f7-8c7e'
AND key LIKE 'class%';
```

**Catalog count by type**

```sql
SELECT
  catalog_type,
  COUNT(*) as num_catalogs
FROM databricks_workspace.catalog.catalogs
WHERE deployment_name = 'dbc-74aa95f7-8c7e'
GROUP BY catalog_type;
```

## Provider Coverage

The `databricks_workspace` provider covers workspace related services, the `databricks_account` provider covers account-level operations including provisioning, billing, and account IAM.  

The web terminal flow covers workspace-scoped queries using the token of the logged-in user. For account-level queries (provisioning, billing, account IAM), you need a Databricks service principal with account admin rights and OAuth2 credentials:

```bash
export DATABRICKS_ACCOUNT_ID="your-account-id"
export DATABRICKS_CLIENT_ID="your-client-id"
export DATABRICKS_CLIENT_SECRET="your-client-secret"
```

These are the same variables used by the Databricks CLI and Terraform provider, so if you already have those configured the auth story is identical.

## Get Started

Full provider documentation:

- [__Databricks Workspace Provider for StackQL__](https://databricks-workspace-provider.stackql.io/)
- [__Databricks Account Provider for StackQL__](https://databricks-account-provider.stackql.io/)

Visti [__StackQL on GitHub__](https://github.com/stackql/stackql).
