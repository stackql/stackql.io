---
slug: snowflake-provider-update-august-2026
title: Snowflake Provider Update - August 2026
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-snowflake-provider-featured-image.png"
description: Ground-up rebuild of the StackQL Snowflake provider from the vendor-published REST API specs - consolidated services, grants as data, CREATE OR ALTER as REPLACE, LIMIT pushdown, new Cortex AI endpoints including OpenAI and Anthropic compatible interfaces, and full data plane statement submission.
keywords: [stackql, snowflake, provider, cortex, grants, warehouses, sql api, data plane, ai]
tags: [stackql, snowflake, provider, cortex, sql-api]
---

We've released a ground-up rebuild of the [__StackQL Snowflake provider__](https://snowflake-provider.stackql.io/), generated from the vendor-published [snowflake-rest-api-specs](https://github.com/snowflakedb/snowflake-rest-api-specs) and kept current via upstream sync. The provider now covers __13 consolidated services, 75 resources and 303 operations__ - restructured from the previous release's 36 single-resource services, 63 resources and 260 operations - spanning the full control plane, the SQL API data plane, and the Cortex AI surface.

This is a breaking release: resource names are plural snake_case, services are consolidated by domain (for example `databases`, `tables`, `warehouses`, `roles`, `grants`, `security`, `pipelines`, `apps`, `cortex`), and lifecycle operations are uniformly named. The previous provider version remains available in the registry for pinning; the [Breaking Changes](https://github.com/stackql-registry/stackql-provider-snowflake#breaking-changes) section in the repository covers the migration in detail.

## Grants as Data

Snowflake access control is where SQL-based management shines against state-file tooling: a grant is a row, not a resource to reconcile. Granting is an `INSERT`, revoking is a `DELETE`, and auditing is a `SELECT` - there is no state to drift:

```sql
-- audit every privilege granted to a role
SELECT securable_type, securable_name, privileges, granted_by
FROM snowflake.grants.grants
WHERE grantee_type = 'role' AND grantee_name = 'ANALYST_ROLE'
AND endpoint = 'myorg-myaccount';

-- grant is an INSERT
INSERT INTO snowflake.grants.grants(grantee_type, grantee_name, securable_type, securable_name, privileges, endpoint)
SELECT 'role', 'ANALYST_ROLE', 'DATABASE', 'ANALYTICS', '["USAGE"]', 'myorg-myaccount';

-- revoke is a DELETE
DELETE FROM snowflake.grants.grants
WHERE grantee_type = 'role' AND grantee_name = 'ANALYST_ROLE'
AND securable_type = 'DATABASE' AND securable_name = 'ANALYTICS'
AND privilege = 'USAGE' AND endpoint = 'myorg-myaccount';
```

The role, database role and user grant subresources (`role_grants`, `role_grants_of`, `role_grants_on`, `role_future_grants`, `database_role_grants`, `user_grants` and friends) are also fully mapped in the `roles` service.

## CREATE OR ALTER as REPLACE

Snowflake's create-or-alter PUT operations map to the StackQL `REPLACE` verb - declarative, idempotent resource definition with no state file. Eleven resources support it, including databases, schemas, tables, tasks, warehouses, compute pools, users and services:

```sql
REPLACE snowflake.warehouses.warehouses
SET name = 'ANALYST_WH', warehouse_size = 'XSMALL',
    auto_suspend = 60, auto_resume = 'true'
WHERE warehouse_name = 'ANALYST_WH' AND endpoint = 'myorg-myaccount';
```

## New Cortex AI Surface

The Cortex service picks up the latest vendor endpoints, several of which are new since the previous release:

| Resource | Description |
|----------|-------------|
| `messages` | Anthropic-compatible messages endpoint (`/api/v2/cortex/v1/messages`) - inference via `SELECT` |
| `chat_completions` | OpenAI-compatible chat completions endpoint (`/api/v2/cortex/v1/chat/completions`) - inference via `SELECT` |
| `analyst_messages` | Cortex Analyst conversational analytics over semantic models |
| `analyst_agentic_optimizations` | New agentic optimization listing and retrieval |
| `analyst_pre_selection` | New semantic model pre-selection endpoint |
| `analyst_filters_and_metrics_suggestions` | New filter and metric suggestion generation |
| `cortex_search_services` | Cortex Search - full lifecycle plus `query` and `suggest` actions |
| `models` | Available Cortex model inventory |

Inference maps to `SELECT`, consistent with the `anthropic`, `openai` and `gemini` providers - `WHERE` members feed the request body and the completion projects as columns, so a completion composes with everything else SQL can do:

```sql
SELECT model, choices, usage
FROM snowflake.cortex.chat_completions
WHERE model = 'llama3.1-8b'
AND messages = '[{"role": "user", "content": "Summarize warehouse spend drivers"}]'
AND endpoint = 'myorg-myaccount';
```

Server-sent-event-only operations (streaming completions and fast generation) are excluded, consistent with previous provider builds.

## Data Plane: SQL API Statement Submission

The `sqlapi` service exposes statement submission alongside the control plane, so you can inventory infrastructure and query the data inside it in one session:

```sql
-- control plane: find a warehouse
SELECT name, size, state FROM snowflake.warehouses.warehouses
WHERE endpoint = 'myorg-myaccount';

-- data plane: run a query on it
INSERT INTO snowflake.sqlapi.statements(statement, warehouse, endpoint)
SELECT 'SELECT COUNT(*) FROM ANALYTICS.RAW.EVENTS', 'ANALYST_WH', 'myorg-myaccount'
RETURNING statement_handle, data;
```

Result retrieval is now a first-class `SELECT` - `snowflake.sqlapi.results` is bound to the ResultSet schema with partition retrieval for large result sets (it was an unmapped operation in the previous release).

## Everything Else

- __New control plane coverage__ - resources added upstream since the last release are now queryable: Streamlit apps (`streamlits`), artifact repositories, sequences, network rules, password policies, secrets, tags, and compute pool instance families.
- __LIMIT pushdown__ - `LIMIT n` is pushed down to the wire as the `showLimit` parameter on the 31 list endpoints that accept it, and WHERE predicates naming declared parameters (`like`, `fromName`) push into the request automatically.
- __Native request bodies__ - `INSERT`/`REPLACE` columns are the native wire property names (`name`, `kind`, `warehouse_size`), via the naive request body translator used across recent provider builds.
- __snake_case throughout__ - camelCase wire fields present as snake_case columns (`statementHandle` -> `statement_handle`) and camelCase path parameters are renamed (`granteeType` -> `grantee_type`), so the SQL surface is consistently snake_case.
- __Lifecycle actions__ - 100 resource actions (resume/suspend, execute, refresh, clone, undrop, swap, rename and more) are uniformly mapped to `EXEC` methods.
- __Deprecated endpoint cleanup__ - vendor-deprecated twin endpoints carried by the old provider (`*_deprecated` methods) are dropped.

A [stackql-deploy example stack](https://github.com/stackql-registry/stackql-provider-snowflake/tree/stackql-provider/examples/stackql-deploy/snowflake-analytics) ships in the repository - a declarative analytics stack (database, schema, table, warehouse, role and grants) with no state file.

Full documentation is available at [snowflake-provider.stackql.io](https://snowflake-provider.stackql.io/); raise issues or requests in the [provider repository](https://github.com/stackql-registry/stackql-provider-snowflake).
