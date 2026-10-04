---
slug: anthropic-providers-update-july-2026
title: Anthropic Providers Update - July 2026
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-claude-featured-image.png"
description: Latest release of the StackQL Anthropic providers - anthropic for the Claude API (messages, agents, sessions, skills, files, vaults and more) and anthropic_admin for enterprise organization management, usage and cost reporting, and Claude Code analytics - are available in the StackQL Provider Registry now.
keywords: [stackql, anthropic, claude, provider, admin api, usage reporting, cost reporting, claude code, agents, enterprise]
tags: [stackql, anthropic, claude, provider, admin api, usage reporting, cost reporting, claude code, agents, enterprise]
---

We've released an update to the StackQL providers for the Anthropic platform:

- [__`anthropic`__](https://anthropic-provider.stackql.io) - the Claude API surface: messages, models, batches, files, agents, deployments, environments, sessions, skills, memory stores, user profiles, and vaults (11 services, 26 resources, 103 operations)
- [__`anthropic_admin`__](https://anthropic-admin-provider.stackql.io) __[new]__ - the Admin API surface: organization, users, invites, workspaces, API keys, usage and cost reports, rate limits, and Claude Code analytics (6 services, 11 resources)

Both providers expose a SQL-first surface: authentication is handled automatically, Push down support using the `LIMIT` clause and built in pagination handling.

## Inference as a query

Inference using Claude is accessible via `SELECT` for instance:

```sql
SELECT
id,
model,
stop_reason,
JSON_EXTRACT(content, '$[0].text') AS assistant_message,
JSON_EXTRACT(usage, '$.output_tokens') AS output_tokens
FROM anthropic.messages.messages
WHERE model = 'claude-sonnet-5'
AND max_tokens = 2048
AND messages = '[
  {
    "role": "user",
    "content": "how does stackql work?"
  }
]'
AND system = 'You are a technical assistant. Answer in one short paragraph.'
AND thinking = '{"type": "disabled"}';
```

Token counting works the same way via `anthropic.messages.token_counts` - free of charge.

## Model capabilities view

The provider ships a convenience view that fans the per-model capability matrix out of the `capabilities` JSON column into flat columns:

```sql
SELECT id, display_name, thinking, adaptive, xhigh, max_input_tokens, max_tokens
FROM anthropic.models.vw_model_capabilities;
```

One row per model with boolean flags for batch, citations, code execution, context management, effort tiers (`low` through `xhigh`), image and PDF input, structured outputs, and thinking modes - useful for picking a model programmatically instead of reading release notes.

## The admin provider - your Anthropic org as data

The `anthropic_admin` provider presents the organization management surface as SQL.  

Enumerate workspaces and who's in them:

```sql
SELECT id, name, created_at, data_residency
FROM anthropic_admin.workspaces.workspaces;

SELECT user_id, workspace_id, workspace_role
FROM anthropic_admin.workspaces.members
WHERE workspace_id = '<workspace_id>';
```

Audit users and API keys across the org:

```sql
SELECT id, email, name, role
FROM anthropic_admin.organization.users;

SELECT id, name, status, workspace_id, partial_key_hint
FROM anthropic_admin.api_keys.api_keys;
```

Pull usage reports as time-bucketed rows - `results` is a JSON column you can break down with `JSON_EACH`, and reports can be grouped and filtered by model, workspace, API key, service tier, and more:

```sql
SELECT starting_at, ending_at, results
FROM anthropic_admin.usage.usage_reports
WHERE starting_at = '2026-07-01T00:00:00Z'
AND "group_by[]" = 'model';
```

Cost reporting and Claude Code adoption analytics work the same way:

```sql
SELECT starting_at, ending_at, results
FROM anthropic_admin.cost.cost_reports
WHERE starting_at = '2026-07-01T00:00:00Z';

SELECT starting_at, ending_at, results
FROM anthropic_admin.usage.claude_code_reports
WHERE starting_at = '2026-07-01T00:00:00Z';
```

Because it's all SQL, you can join usage to workspaces, materialize daily cost snapshots into a database, or point a BI tool at StackQL's Postgres wire protocol server and build an org-wide Claude spend dashboard without writing a line of integration code.

## Authentication

The two providers use different key types, which are disjoint by design:

```bash
# anthropic - workspace-scoped Claude API key
export ANTHROPIC_API_KEY=sk-ant-api...

# anthropic_admin - org-scoped Admin API key (created by org admins)
export ANTHROPIC_ADMIN_KEY=sk-ant-admin...
```

Admin keys are available to organization accounts only and can be provisioned by users with the admin role in the [Console](https://console.anthropic.com/settings/admin-keys).

## Get started

Pull the providers from the public registry:

```bash
registry pull anthropic
registry pull anthropic_admin
```

Provider docs are at [anthropic-provider.stackql.io](https://anthropic-provider.stackql.io) and [anthropic-admin-provider.stackql.io](https://anthropic-admin-provider.stackql.io). Let us know what you build. Star us on [__GitHub__](https://github.com/stackql/stackql).
