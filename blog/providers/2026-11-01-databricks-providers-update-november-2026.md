---
slug: databricks-providers-update-november-2026
title: Databricks Providers Update - November 2026
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-databricks-provider-featured-image.png"
description: Update to the StackQL Databricks providers adding eight new services including Agent Kit memory and session stores, Sandboxes, AI Functions, Data Domains, Knowledge Assistants, workspace base environments, data classification and account private networking, full lifecycle coverage for Lakebase (Postgres) resources, new budget spend, identity and feature engineering resources, and support for Azure Databricks and Databricks on Google Cloud.
keywords: [stackql, databricks, provider, agent kit, sandbox, ai functions, lakebase, knowledge assistants, unity catalog, private networking, data domains, azure databricks, google cloud]
tags: [stackql, databricks, provider, lakebase, agent-bricks, unity-catalog]
---

We've released an update to the StackQL Databricks providers, regenerated from the latest Databricks platform APIs (SDK v0.158.0):

- [__`databricks_account`__](https://databricks-account-provider.stackql.io/) - account-level operations: __10 services, 55 resources__
- [__`databricks_workspace`__](https://databricks-workspace-provider.stackql.io/) - workspace-level operations: __36 services, 315 resources__

<!-- truncate -->

Includes __370 resources and over 1,300 operations__ across both providers, with eight new services and more than 50 new resources, and 49 built-in views. Both providers now work against Databricks on AWS, Azure and Google Cloud; cloud selection and credentials are covered in the [addendum](#addendum-azure-databricks-and-databricks-on-google-cloud) at the end of this post.

## New Services

| Provider | Service | Description |
|----------|---------|-------------|
| `databricks_workspace` | `agentkit` | Agent Kit managed memory stores and session stores: memories, sessions, session items, forks, memory extraction, and store permissions |
| `databricks_workspace` | `sandbox` | Sandboxes: create, start, stop and update isolated environments, run commands synchronously, and manage files and directories inside them |
| `databricks_workspace` | `aifunctions` | The AI Functions API: `ai_classify`, `ai_decide`, `ai_extract` and `ai_parse_document` as `EXEC` methods |
| `databricks_workspace` | `domains` | Data domains in Unity Catalog, with owners, hierarchy and draft state |
| `databricks_workspace` | `knowledgeassistants` | Agent Bricks Knowledge Assistants, their knowledge sources (with sync), examples and permissions |
| `databricks_workspace` | `environments` | Workspace base environments for serverless compute (previously under `compute.default_base_environments`), including the default environment and refresh |
| `databricks_workspace` | `dataclassification` | Data classification configuration per Unity Catalog catalog |
| `databricks_account` | `networking` | Account private networking: endpoints and private network gateways on network connectivity configurations |

## Expanded Coverage in Existing Services

- `postgres` (Lakebase) - every Lakebase resource now has the full `get`, `update` and `delete` lifecycle alongside `create` and `list` (branches, endpoints, roles, databases, catalogs, CDF configs, data APIs, snapshots), plus snapshot schedules, `undelete` for branches and `reveal_password` for roles. Earlier releases could only create and list several of these resources because the API addresses them by resource name on a shared path; the generator now maps each one to its own path
- `catalog` - AI Gateway skills (create, list, get, update, delete and finalize) and per-user MCP service credentials
- `iamv2` (account and workspace) - identity visibility filters, workspace assignments, and external group and service principal lookups
- `ml` - experiment issues, feature materialization sets, feature engineering operations, backfill and purge
- `billing` (account) - budget spend statuses
- `disasterrecovery` (account) - get and delete for failover groups
- `supervisoragents` - get, update and delete for supervisor agents, tools and examples
- `vectorsearch` - start and stop lifecycle operations on vector search endpoints
- `dashboards` - cancel a running AI/BI Genie response
- `bundledeployments` - get and update for deployments, get for operations and deployed resources

## Request Body Columns

Operations whose request body is a single object in the Databricks API (for example creating a network connectivity configuration, a private endpoint rule, a sandbox or a domain) now expose that object's fields directly as `INSERT` and `UPDATE` columns, which is what the API accepts:

~~~sql
INSERT INTO databricks_account.settings.network_connectivity (account_id, name, region)
SELECT '<account_id>', 'lakeside-prod-ncc', 'australiaeast';
~~~

Earlier releases modelled these bodies as one JSON column named after the object (`network_connectivity_config`, `private_endpoint_rule`), a shape the API rejected. Statements written that way need the object's fields instead.

## Removed Upstream

The following operations were removed from the Databricks platform APIs and no longer appear in the providers: account `billing.usage_policy`, account `iamv2` access identity rules and attribute control entries, `compute.default_base_environments` (now `environments.workspace_base_environments`), `database` instance failover and autoscaling upgrade, the published dashboard embed endpoint, pipeline restore, vector search index auto-evaluation and reranker fine-tuning, Lakebase tables, forward ETL and replication group previews, and AI Gateway agent services.

## Sandboxes

Create a sandbox, run a command in it and read the output, then stop it - the compute lifecycle as `INSERT`, `EXEC` and `SELECT`:

~~~sql
/* create */
INSERT INTO databricks_workspace.sandbox.sandboxes (
  sandbox_id,
  display_name,
  deployment_name
)
SELECT
  'etl-scratch',
  'etl-scratch',
  '<deployment_name>';

/* list */
SELECT name, display_name, status
FROM databricks_workspace.sandbox.sandboxes
WHERE deployment_name = '<deployment_name>';

/* run a command synchronously */
EXEC databricks_workspace.sandbox.sandbox_commands.execute
  @sandbox_id = '<sandbox_id>',
  @deployment_name = '<deployment_name>'
  @@json = '{"cmd": "ls -la /workspace"}';

/* stop and delete */
EXEC databricks_workspace.sandbox.sandboxes.stop
  @sandbox_id = '<sandbox_id>', @deployment_name = '<deployment_name>';

DELETE FROM databricks_workspace.sandbox.sandboxes
WHERE sandbox_id = '<sandbox_id>'
AND deployment_name = '<deployment_name>';
~~~

## Agent Kit Memory and Sessions

Managed memory stores and session stores for agents, with their contents as queryable rows:

~~~sql
SELECT name, display_name, storage_backend, owner_user_id
FROM databricks_workspace.agentkit.memory_stores
WHERE deployment_name = '<deployment_name>';

SELECT name, display_name, create_time
FROM databricks_workspace.agentkit.session_stores
WHERE deployment_name = '<deployment_name>';

SELECT *
FROM databricks_workspace.agentkit.sessions
WHERE session_store_id = '<session_store_id>'
AND deployment_name = '<deployment_name>';
~~~

## AI Functions

The AI Functions API is exposed as `EXEC` methods, so a classification or document parse is one statement:

~~~sql
EXEC databricks_workspace.aifunctions.ai_functions.ai_classify
  @deployment_name = '<deployment_name>'
  @@json = '{"content": "The invoice is 30 days overdue", "labels": ["billing", "support", "sales"]}';
~~~

## Data Domains and Knowledge Assistants

Unity Catalog data domains with their owners and hierarchy, and Agent Bricks Knowledge Assistants with their knowledge sources:

~~~sql
SELECT domain_id, name, parent_domain_id, business_owner_ids
FROM databricks_workspace.domains.domains
WHERE deployment_name = '<deployment_name>';

SELECT id, display_name, state, endpoint_name
FROM databricks_workspace.knowledgeassistants.knowledge_assistants
WHERE deployment_name = '<deployment_name>';

SELECT id, display_name, source_type, state
FROM databricks_workspace.knowledgeassistants.knowledge_sources
WHERE knowledge_assistant_id = '<knowledge_assistant_id>'
AND deployment_name = '<deployment_name>';
~~~

## Lakebase Snapshots

Snapshots and snapshot schedules for a Lakebase branch, now with the complete lifecycle:

~~~sql
SELECT snapshot_id, name, status
FROM databricks_workspace.postgres.postgres_snapshots
WHERE project_id = '<project_id>'
AND deployment_name = '<deployment_name>';

SELECT name, schedule
FROM databricks_workspace.postgres.postgres_snapshot_schedules
WHERE project_id = '<project_id>'
AND branch_id = '<branch_id>'
AND deployment_name = '<deployment_name>';
~~~

## Account Private Networking

Private endpoints and private network gateways at the account level:

~~~sql
SELECT endpoint_id, display_name, region, state, use_case
FROM databricks_account.networking.endpoints
WHERE account_id = '<account_id>';

SELECT display_name, traffic_mode, bandwidth_tier_gigabits_per_second, state
FROM databricks_account.networking.private_network_gateways
WHERE account_id = '<account_id>'
AND ncc_id = '<network_connectivity_config_id>';
~~~

## Budget Spend

Where each budget stands against its alerts:

~~~sql
SELECT name, alert_spend_statuses
FROM databricks_account.billing.budget_spend_statuses
WHERE account_id = '<account_id>';
~~~

## Get Started

Pull the latest providers from the public registry:

~~~bash
stackql registry pull databricks_account
stackql registry pull databricks_workspace
~~~

Authenticate with the same service principal environment variables used by Terraform and the Databricks CLI (`DATABRICKS_ACCOUNT_ID`, `DATABRICKS_CLIENT_ID`, `DATABRICKS_CLIENT_SECRET`), add `DATABRICKS_CLOUD=azure` or `DATABRICKS_CLOUD=gcp` for an account on those clouds (stackql v1.0.888 or later), then explore:

~~~sql
SELECT workspace_id, workspace_name, workspace_status, deployment_name
FROM databricks_account.provisioning.workspaces
WHERE account_id = '<account_id>';
~~~

Provider docs are at [databricks-account-provider.stackql.io](https://databricks-account-provider.stackql.io/) and [databricks-workspace-provider.stackql.io](https://databricks-workspace-provider.stackql.io/). Visit us on [__GitHub__](https://github.com/stackql/stackql) and let us know how you're using it.

## Addendum: Azure Databricks and Databricks on Google Cloud

Both providers now work against Databricks accounts and workspaces on AWS, Azure and Google Cloud, with the same statements and the same three credential variables. Cloud selection needs stackql v1.0.888 or later.

### Selecting the cloud

One variable picks the cloud; the OAuth token endpoint, the account API host and the workspace host domain follow it:

- `DATABRICKS_CLOUD` - `aws` (default), `azure` or `gcp`
- `DATABRICKS_ACCOUNT_HOST` - optional, a bare account console host (no scheme) for accounts that are not on the standard hosts; overrides `DATABRICKS_CLOUD`
- `DATABRICKS_DEPLOYMENT_NAME` - optional, defaults the `deployment_name` predicate for `databricks_workspace` queries

| Cloud | `DATABRICKS_CLOUD` | Account console and token host | Workspace host | `deployment_name` |
|-------|--------------------|--------------------------------|----------------|-------------------|
| AWS | `aws` | `accounts.cloud.databricks.com` | `<deployment_name>.cloud.databricks.com` | `dbc-abcd0123-a1bc` |
| Azure | `azure` | `accounts.azuredatabricks.net` | `<deployment_name>.azuredatabricks.net` | `adb-1234567890123456.7` |
| GCP | `gcp` | `accounts.gcp.databricks.com` | `<deployment_name>.gcp.databricks.com` | `1234567890123456.7` |

`deployment_name` is the host prefix of the workspace URL on every cloud. With nothing set, behaviour is unchanged from earlier releases: AWS hosts and the AWS token endpoint. One cloud per process: the token endpoint follows the environment rather than a SQL predicate, so use a separate `stackql` process or shell for each cloud.

### Credentials

The credential on every cloud is a service principal with a Databricks OAuth secret, created in the account console (__User management > Service principals > Add service principal__, then __Generate secret__) and given the __Account admin__ role for account-level operations. Cloud identity tokens (AWS IAM, Microsoft Entra ID, Google service accounts) are not used.

- __Azure__: sign in to [accounts.azuredatabricks.net](https://accounts.azuredatabricks.net) with the Entra ID identity that administers the Databricks account. A Databricks-managed service principal, or an Entra ID application registered as a service principal, both work when the OAuth secret is generated in the Databricks account console.
- __GCP__: [accounts.gcp.databricks.com](https://accounts.gcp.databricks.com). The Google service account that Databricks itself uses is not the principal.
- __Workspace access__: the service principal must be a member of each workspace it queries (an admin for admin-only resources such as workspace configuration). From SQL, with the account provider:

~~~sql
REPLACE databricks_account.iam.workspace_assignment
SET permissions = '["USER"]'
WHERE account_id = '<account_id>'
AND workspace_id = '<workspace_id>'
AND principal_id = '<service_principal_id>';
~~~

### Azure Databricks

~~~bash
export DATABRICKS_CLOUD=azure
export DATABRICKS_ACCOUNT_ID=...
export DATABRICKS_CLIENT_ID=...
export DATABRICKS_CLIENT_SECRET=...
stackql shell
~~~

~~~sql
/* workspaces in the account, with their Azure subscription and resource group */
SELECT
  workspace_id,
  workspace_name,
  workspace_status,
  location,
  deployment_name,
  azure_workspace_info
FROM databricks_account.provisioning.workspaces
WHERE account_id = '<account_id>';

/* SQL warehouses in a workspace */
SELECT id, name, state, cluster_size, auto_stop_mins
FROM databricks_workspace.sql.warehouses
WHERE deployment_name = 'adb-1234567890123456.7';
~~~

### Databricks on Google Cloud

~~~powershell
$env:DATABRICKS_CLOUD = "gcp"
$env:DATABRICKS_ACCOUNT_ID = "..."
$env:DATABRICKS_CLIENT_ID = "..."
$env:DATABRICKS_CLIENT_SECRET = "..."
stackql.exe shell
~~~

~~~sql
/* workspaces in the account, with their Google project */
SELECT
  workspace_id,
  workspace_name,
  workspace_status,
  location,
  deployment_name,
  cloud_resource_container
FROM databricks_account.provisioning.workspaces
WHERE account_id = '<account_id>';

/* catalogs in a workspace */
SELECT full_name, catalog_type, owner
FROM databricks_workspace.catalog.catalogs
WHERE deployment_name = '1234567890123456.7';
~~~

### What differs by cloud

- `provisioning.workspaces` reports the region in `aws_region` on AWS and in `location` on Azure and GCP; Azure rows carry `azure_workspace_info`, GCP rows `cloud_resource_container` and `network_id`.
- The infrastructure objects under account `provisioning` (`credentials`, `storage`, `networks`, `vpc_endpoints`, `private_access`, `encryption_keys`) and the views over them are the AWS and GCP account APIs. Azure Databricks workspaces are created through Azure Resource Manager, so none of these exist on an Azure account and Databricks answers those calls with an HTML error page rather than an empty list; on GCP, `credentials` answers 503.
- `billing.billable_usage` (the usage CSV download) returns data for AWS and GCP accounts; an Azure Databricks account returns no rows because consumption is billed through the Azure subscription. Budgets, budget policies and budget spend statuses work everywhere.
- Workspace APIs are the same on all three clouds. Resources tied to one cloud's primitives (AWS instance profiles, for example) exist only there, and where a workspace's cloud or region does not offer a feature Databricks answers with an error rather than an empty list; the provider surfaces that error as is.

The per-cloud setup is also documented on the provider pages: [databricks-account-provider.stackql.io](https://databricks-account-provider.stackql.io/) and [databricks-workspace-provider.stackql.io](https://databricks-workspace-provider.stackql.io/).
