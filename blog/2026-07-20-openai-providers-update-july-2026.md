---
slug: openai-providers-update-july-2026
title: OpenAI Providers Update - July 2026
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-provider-for-openai-released.png"
description: Latest release of the StackQL OpenAI providers - openai for the platform API (models, files, fine-tuning, batches, vector stores, evals and more) and openai_admin for organization management, usage and cost reporting, projects, RBAC and audit logs - are available in the StackQL Provider Registry now.
keywords: [stackql, openai, provider, admin api, usage reporting, cost reporting, finops, projects, audit logs, enterprise]
tags: [stackql, openai, provider, admin api, usage reporting, cost reporting, finops, projects, audit logs, enterprise]
---

We've released an update to the StackQL providers for the OpenAI platform:

- [__`openai`__](https://openai-provider.stackql.io) - the platform API surface available to standard API keys: models, files, fine-tuning, batches, vector stores, assistants, evals, conversations, uploads, containers, and skills (11 services, 26 resources, 97 operations)
- [__`openai_admin`__](https://openai-admin-provider.stackql.io) __[new]__ - the organization and administration API surface: usage and cost reporting, projects, organization users and invites, groups and roles, admin API keys, audit logs, and certificates (10 services, 29 resources, 81 operations)

Both providers expose a SQL-first surface: authentication is handled automatically, push down support using the `LIMIT` clause and built in pagination handling.

The `openai` provider is a ground-up rebuild of the previous provider, generated from the vendor's published OpenAPI specification. Some resources have been renamed and the organization/admin surface has moved to `openai_admin` - the previous provider version remains available in the registry for pinning, and the full disposition is documented at [openai-provider.stackql.io](https://openai-provider.stackql.io).

## Async jobs as SQL

Fine-tuning jobs, batches, vector store file batches and uploads follow the same pattern: `INSERT` creates the job, `SELECT` polls it, `EXEC` cancels it. For example:

```sql
SELECT id, status, model, fine_tuned_model, trained_tokens
FROM openai.fine_tuning.jobs;

SELECT id, status, endpoint, request_counts
FROM openai.batches.batches
LIMIT 10;
```

Vector stores are a full CRUD surface with file membership:

```sql
SELECT id, name, status, usage_bytes, file_counts
FROM openai.vector_stores.vector_stores;
```

Inference endpoints (chat/completions, responses, embeddings, images, audio) are deliberately out of scope - the provider covers the control plane; use the vendor SDKs for invocation.

## The admin provider - your OpenAI org as data

The `openai_admin` provider presents the organization management surface as SQL.

Usage and cost are bucketed time series: one row per time bucket, with the per-group breakdown in the `results` JSON column, fanned out with `JSON_EACH`. Token usage by project and model over a 30-day window:

```sql
SELECT
  json_extract(r.value, '$.project_id')    AS project_id,
  json_extract(r.value, '$.model')         AS model,
  strftime('%Y-%m-%d', u.start_time, 'unixepoch') AS usage_date,
  json_extract(r.value, '$.input_tokens')  AS input_tokens,
  json_extract(r.value, '$.output_tokens') AS output_tokens
FROM openai_admin.usage.completions u, json_each(u.results) r
WHERE u.start_time = 1781481600
  AND u.bucket_width = '1d'
  AND u.limit = 31
  AND u.group_by = 'project_id'
ORDER BY usage_date, project_id;
```

Daily spend in USD by project:

```sql
SELECT
  strftime('%Y-%m-%d', c.start_time, 'unixepoch') AS cost_date,
  json_extract(r.value, '$.project_id')   AS project_id,
  json_extract(r.value, '$.amount.value') AS amount_usd
FROM openai_admin.costs.costs c, json_each(c.results) r
WHERE c.start_time = 1781481600
  AND c.limit = 180
  AND c.group_by = 'project_id'
ORDER BY cost_date, amount_usd DESC;
```

Usage is broken out per capability - completions, embeddings, moderations, images, audio, vector stores and code interpreter sessions - and can be grouped by `project_id`, `api_key_id` or `model`.

## Governance and auditing

Projects and their child resources (users, service accounts, API keys, rate limits) are queryable and writable:

```sql
SELECT p.name AS project, p.status, sa.name AS service_account, sa.role
FROM openai_admin.projects.projects p
JOIN openai_admin.projects.service_accounts sa ON sa.project_id = p.id
WHERE p.status = 'active';
```

Admin key hygiene and audit logs work the same way:

```sql
SELECT name, created_at, last_used_at, owner
FROM openai_admin.admin_api_keys.admin_api_keys
ORDER BY created_at;

SELECT id, type, effective_at, actor, project
FROM openai_admin.audit_logs.audit_logs
WHERE "effective_at[gt]" = 1750000000
  AND "event_types[]" = 'project.created';
```

Because it's all SQL, you can join usage to projects, materialize daily cost snapshots into a database, or point a BI tool at StackQL's Postgres wire protocol server and build an org-wide OpenAI spend dashboard without writing a line of integration code.

## Authentication

The two providers use different key types, which are disjoint by design:

```bash
# openai - standard API key
export OPENAI_API_KEY=sk-...

# openai_admin - org-scoped admin key (created by organization owners)
export OPENAI_ADMIN_KEY=sk-admin-...
```

Admin keys are available to organization accounts only and can be provisioned by organization owners in the [platform console](https://platform.openai.com/settings/organization/admin-keys). A standard key cannot call the admin endpoints and vice versa.

## Get started

Pull the providers from the public registry:

```bash
registry pull openai
registry pull openai_admin
```

Provider docs are at [openai-provider.stackql.io](https://openai-provider.stackql.io) and [openai-admin-provider.stackql.io](https://openai-admin-provider.stackql.io). Let us know what you build. Star us on [__GitHub__](https://github.com/stackql/stackql).
