---
slug: anthropic-providers-update-september-2026
title: Anthropic Providers Update - September 2026
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-claude-featured-image.png"
description: Update to the StackQL anthropic provider regenerated from the current Claude API spec - a new dreams service for memory consolidation jobs, files and skills on their GA endpoints, and workspace-scoped queries through the anthropic-workspace-id parameter. The anthropic_admin provider is unchanged.
keywords: [stackql, anthropic, claude, provider, dreams, memory stores, files, skills, workspaces, agents, sql]
tags: [stackql, anthropic, claude, provider, agents]
---

We've released an update to the [__StackQL `anthropic` provider__](https://anthropic-provider.stackql.io), regenerated from the current Claude API specification. The provider now covers 12 services, 27 resources and 108 operations (up from 11, 26 and 103 in the [July release](/blog/anthropic-providers-update-july-2026)). Changes in this release:

- A new `dreams` service
- Files and skills moved to their generally available endpoints
- Workspace-scoped queries on most operations through the `anthropic-workspace-id` parameter

The [__`anthropic_admin`__](https://anthropic-admin-provider.stackql.io) provider (6 services, 11 resources, 27 operations) is unchanged in this release.

## Dreams

Dreams are asynchronous memory-consolidation jobs: a dream reads a memory store and a set of session transcripts and writes consolidated memories into an output store. The service is a research preview on the Anthropic side, so the endpoints return a 404 for keys that are not enrolled. The `dreams` resource maps the surface as follows:

| Method | SQL verb | Operation |
|--------|----------|-----------|
| `list`, `get` | `SELECT` | list dreams (cursor-paginated, walked automatically), get a dream |
| `create` | `INSERT` | start a dream over one or more inputs |
| `cancel`, `archive` | `EXEC` | lifecycle operations |

Dreams and their status as rows:

```sql
SELECT id, status, JSON_ARRAY_LENGTH(inputs) AS input_count, created_at, ended_at
FROM anthropic.dreams.dreams
ORDER BY created_at DESC;
```

Starting one is an `INSERT`. `inputs` and `model` are JSON values that the provider passes through as structured request fields:

```sql
INSERT INTO anthropic.dreams.dreams (inputs, model, instructions)
SELECT '[{"type": "memory_store", "memory_store_id": "memstore_01..."}]',
       '{"id": "claude-opus-5"}',
       'Consolidate project decisions and open questions.'
RETURNING id, status;
```

## Files and skills on GA endpoints

The Files API and the Skills API are generally available. The `files` and `skills` services now call the GA endpoints (`/v1/files`, `/v1/skills`) instead of the beta ones, and no longer send an `anthropic-beta` header. Resources, methods and SQL verbs are unchanged, so existing queries keep working. One behavioural change: the files list is now cursor-paginated and the provider walks the pages automatically.

```sql
SELECT id, filename, mime_type, size_bytes, created_at
FROM anthropic.files.files
ORDER BY created_at DESC;

SELECT id, display_name, JSON_EXTRACT(source, '$.type') AS source, latest_version_id, updated_at
FROM anthropic.skills.skills
ORDER BY updated_at DESC;
```

Skill versions are a separate resource keyed by the skill:

```sql
SELECT id, skill_id, name, description, created_at
FROM anthropic.skills.versions
WHERE skill_id = 'xlsx';
```

Uploading a file or creating a skill version is a multipart request, which SQL cannot express; those two methods remain documented as `EXEC` operations, and the rest of each resource (list, get, delete) is plain SQL.

## Workspace-scoped queries

The Claude API added an optional `anthropic-workspace-id` header to most operations, for credentials that can act on more than one workspace. The provider exposes it as an optional parameter on around 110 operations. It is a hyphenated name, so it is double-quoted in SQL:

```sql
SELECT id, display_name, created_at
FROM anthropic.models.models
WHERE "anthropic-workspace-id" = 'wrkspc_01CZkZaBF1tNoB5wlCeusgy';
```

The workspace ids are the ones the `anthropic_admin` provider lists:

```sql
SELECT id, name, archived_at
FROM anthropic_admin.workspaces.workspaces;
```

A key that belongs to a single workspace can omit the parameter. The API validates the value: a malformed id is rejected with a 400.

## Under the hood

The provider is generated from the OpenAPI specification that Anthropic now bundles with its SDKs. That specification grew from 126 to 244 operations since July, and the SDKs' configured endpoint count from 116 to 201. Beyond the changes above, the growth is the beta twins of the GA files and skills endpoints and the Admin API, which the specification now models but which needs an org-scoped admin key and belongs to the `anthropic_admin` provider. Every operation in the specification is either mapped or listed in a documented exclusion list, and the build fails when the two do not add up.

Every documented example query runs in the provider's test suite, and each generated docs page now shows the date it was last regenerated.

## Authentication

Unchanged. The two providers use different key types, which are disjoint by design:

```bash
# anthropic - workspace-scoped Claude API key
export ANTHROPIC_API_KEY=sk-ant-api...

# anthropic_admin - org-scoped Admin API key (created by org admins)
export ANTHROPIC_ADMIN_KEY=sk-ant-admin...
```

## Get started

Pull the latest provider from the public registry:

```bash
registry pull anthropic;
```

Provider docs, including required parameters and example queries for every resource, are at [anthropic-provider.stackql.io](https://anthropic-provider.stackql.io) and [anthropic-admin-provider.stackql.io](https://anthropic-admin-provider.stackql.io). Visit us on [__GitHub__](https://github.com/stackql/stackql) and let us know how you're using it.
