---
slug: new-supabase-provider-available
title: New Supabase Provider Available
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-supabase-provider-featured-image.png"
description: A new StackQL provider for Supabase - query and manage organizations, projects, branches, edge functions, secrets, auth and Postgres configuration, network restrictions and the project database itself using SQL - available in the StackQL Provider Registry now.
keywords: [stackql, supabase, provider, postgres, edge functions, management api, security posture, infrastructure as code, cloud inventory, sql]
tags: [stackql, supabase, provider, postgres, security]
---

We've released a new StackQL provider for Supabase:

- [__`supabase`__](https://supabase-provider.stackql.io) - the Supabase Management API: __`organizations`__, __`projects`__, __`branches`__, __`functions`__, __`secrets`__, __`config`__, __`database`__, __`domains`__, __`network`__, __`storage`__, __`billing`__, __`advisors`__, __`analytics`__ and __`profile`__ (14 services, 65 resources, 158 operations)

<!-- truncate -->

The provider covers the control plane at `api.supabase.com`: the project estate, preview branches, edge functions, secrets and API keys, project configuration (auth, Postgres, pooler, API, storage, realtime, SSL enforcement), custom domains, network restrictions and bans, backups and restore points, add-ons and the vendor's security and performance advisors. It also surfaces the project SQL query endpoint, so the control plane and the project database are queryable in one session. The surface is generated from the vendor's published OpenAPI document; Supabase's own Terraform provider is labelled Public Alpha and covers seven resources.

## Connect

Authentication is a personal access token (dashboard -> Account -> Access Tokens), read from `SUPABASE_ACCESS_TOKEN` - the same variable the Supabase CLI and the Terraform provider use. Most resources are scoped to a project by its reference (`ref`); setting `SUPABASE_PROJECT_ID` resolves it for the session, and a `WHERE ref = '...'` value takes precedence when given:

```bash
export SUPABASE_ACCESS_TOKEN=sbp_...
export SUPABASE_PROJECT_ID=abcdefghijklmnopqrst   # optional
stackql shell
```

The Management API allows a fixed number of requests per minute per user (documented as 120), so queries that fan out across many projects should be sequenced rather than run in parallel.

## The project estate

Every project the token can see, with region, status and organization:

```sql
SELECT id, name, region, status, organization_slug, created_at
FROM supabase.projects.projects
ORDER BY organization_slug, name;
```

The members of an organization and whether they have MFA enabled:

```sql
SELECT user_name, role_name, mfa_enabled
FROM supabase.organizations.members
WHERE slug = '<organization-slug>';
```

## Security posture

With `SUPABASE_PROJECT_ID` set to the project under review. Signups, MFA enrolment and the password policy:

```sql
SELECT disable_signup, external_anonymous_users_enabled,
       mfa_totp_enroll_enabled, mfa_phone_enroll_enabled,
       password_min_length, password_hibp_enabled
FROM supabase.config.auth_configs;
```

SSL enforcement on the database, and the network restrictions in force - `0.0.0.0/0` means any address may reach it:

```sql
SELECT applied_successfully,
       json_extract(current_config, '$.database') AS ssl_enforced
FROM supabase.config.ssl_enforcement_configs;

SELECT entitlement, status,
       json_extract(config, '$.dbAllowedCidrs') AS allowed_v4,
       json_extract(config, '$.dbAllowedCidrsV6') AS allowed_v6
FROM supabase.network.network_restrictions;
```

The vendor's own security lints for the project, at error level:

```sql
SELECT name, level, title, json_extract(metadata, '$.name') AS object
FROM supabase.advisors.security_lints
WHERE level = 'ERROR';
```

To review several projects in one statement, address each by `ref`:

```sql
SELECT 'abcdefghijklmnopqrst' AS ref, disable_signup, mfa_totp_enroll_enabled
FROM supabase.config.auth_configs WHERE ref = 'abcdefghijklmnopqrst'
UNION ALL
SELECT 'tsrqponmlkjihgfedcba', disable_signup, mfa_totp_enroll_enabled
FROM supabase.config.auth_configs WHERE ref = 'tsrqponmlkjihgfedcba';
```

## From the control plane to Postgres rows

The query endpoint runs SQL against the project database as the service role. The result arrives as one row whose `rows` column carries the result set, so listing the projects and then querying one of them is two statements in the same session:

```sql
SELECT id, name FROM supabase.projects.projects;

INSERT INTO supabase.database.queries (ref, query)
SELECT 'abcdefghijklmnopqrst',
       'select schemaname, relname, n_live_tup from pg_stat_user_tables order by n_live_tup desc limit 10'
RETURNING rows;
```

Values in the result are addressed with `json_extract(rows, '$[0].relname')`. The statement is executed as written, so use the `read_only` flag (`INSERT ... (ref, query, read_only) SELECT ..., true`) for inspection queries.

## Secrets, functions and branches

```sql
SELECT name, updated_at FROM supabase.secrets.secrets;

SELECT slug, name, status, verify_jwt, version
FROM supabase.functions.edge_functions;

SELECT name, git_branch, status, persistent, updated_at
FROM supabase.branches.branches
WHERE persistent = false
ORDER BY updated_at;
```

## Provisioning and day-two operations

Secrets are created and removed one per statement:

```sql
INSERT INTO supabase.secrets.secrets (name, value)
SELECT 'STRIPE_WEBHOOK_SECRET', 'whsec_...';

DELETE FROM supabase.secrets.secrets WHERE name = 'STRIPE_WEBHOOK_SECRET';
```

Configuration is updated in place; `UPDATE` sends only the columns you set, and values are written as quoted strings:

```sql
UPDATE supabase.config.auth_configs
SET disable_signup = 'true', password_min_length = '12';
```

Network restrictions and the project lifecycle are `EXEC` methods:

```sql
EXEC supabase.network.network_restrictions.apply
  @db_allowed_cidrs = '["203.0.113.0/24"]',
  @db_allowed_cidrs_v6 = '[]';

EXEC supabase.projects.projects.pause @ref = 'abcdefghijklmnopqrst';
EXEC supabase.projects.projects.restore @ref = 'abcdefghijklmnopqrst';
```

## Get started

Pull the provider and start querying:

```sql
REGISTRY PULL supabase;
```

The full reference is at [supabase-provider.stackql.io](https://supabase-provider.stackql.io), and the source is at [stackql-registry/stackql-provider-supabase](https://github.com/stackql-registry/stackql-provider-supabase). Questions and requests are welcome in the [StackQL Discussions](https://github.com/orgs/stackql/discussions).
