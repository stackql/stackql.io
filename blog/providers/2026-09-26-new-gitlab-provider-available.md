---
slug: new-gitlab-provider-available
title: New GitLab Provider Available
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-gitlab-provider-featured-image.png"
description: A new read-only StackQL provider for GitLab, generated from the GitLab GraphQL schema - projects, groups, members, issues, merge requests, pipelines, jobs, runners, releases, environments, packages, vulnerabilities and more as SQL, with transparent Relay pagination and pushed-down filters - available in the StackQL Provider Registry now.
keywords: [stackql, gitlab, provider, graphql, devops, ci/cd, merge requests, pipelines, runners, vulnerabilities, cloud inventory, sql]
tags: [stackql, gitlab, provider, graphql, devops, ci-cd, security]
---

We've released a new StackQL provider for GitLab:

- [__`gitlab`__](https://gitlab-provider.stackql.io) - the GitLab GraphQL API as a read-only SQL surface: __`projects`__, __`groups`__, __`users`__, __`issues`__, __`merge_requests`__, __`ci`__, __`work_items`__, __`security`__, __`packages`__, __`snippets`__, __`boards`__, __`analytics`__, __`audit`__, __`metadata`__, __`admin`__, __`workspaces`__, __`ml`__ and __`duo`__ (18 services, 244 resources, every one of them `SELECT`)

The provider is generated from the introspection schema gitlab.com publishes, pinned by content hash and refreshed as a reviewed diff. It works against gitlab.com out of the box and routes to a self-managed instance from an environment variable. It is read-only by architecture: StackQL's GraphQL path is a query path, so there are no `INSERT`, `UPDATE`, `DELETE` or `EXEC` methods. What it is for is inventory, reporting and cross-provider joins over the GitLab control plane - the questions that otherwise need a script and three API clients.

## Connect

Authentication is a personal access token with the `read_api` scope, read from `GITLAB_TOKEN` - the same variable the Terraform GitLab provider uses:

```bash
export GITLAB_TOKEN=glpat-...
stackql shell
```

Public projects and groups on gitlab.com are readable without a token (`--auth='{"gitlab": {"type": "null_auth"}}'`). For a self-managed instance, set `GITLAB_HOST=gitlab.example.com` and every query routes there; an explicit `WHERE host = ...` still wins for addressing another instance in the same session.

## Three scopes, one shape

Resources follow the GraphQL schema. Instance-scoped resources take optional filters only (`projects`, `users`, `runners`); project-scoped resources are prefixed `project_` and take the project path; group-scoped resources are prefixed `group_` and take the group path. Columns are snake_case, and the nested identity objects GitLab attaches everywhere (`author`, `namespace`, `milestone`, `user`) are JSON columns one `json_extract` away.

Every project in a group tree, with activity and visibility signals:

```sql
SELECT full_path, visibility, archived,
       star_count, forks_count,
       open_issues_count, open_merge_requests_count,
       last_activity_at
FROM gitlab.groups.group_projects
WHERE full_path = 'gitlab-org' AND include_subgroups = true
ORDER BY last_activity_at DESC;
```

GitLab connections are Relay-paginated with a hard page size of 100. StackQL walks the `pageInfo` cursor chain transparently, so the query above returns the whole tree, not the first page.

## Filters are pushed down

Every scalar or enum argument a GitLab field accepts is a `WHERE` parameter rendered into the GraphQL query itself, so the API does the filtering. Open merge requests with their approval state and age:

```sql
SELECT iid, title,
       json_extract(author, '$.username') AS author,
       draft, approved, approvals_left, detailed_merge_status,
       ROUND(julianday('now') - julianday(created_at)) AS age_days
FROM gitlab.merge_requests.project_merge_requests
WHERE full_path = 'gitlab-org/gitlab-runner' AND state = 'opened'
ORDER BY age_days DESC;
```

Cycle time over a window (`merged_after` is a filter, `merged_at` a column):

```sql
SELECT iid, title,
       ROUND((julianday(merged_at) - julianday(created_at)) * 24, 1) AS hours_to_merge
FROM gitlab.merge_requests.project_merge_requests
WHERE full_path = 'gitlab-org/gitlab-runner'
  AND state = 'merged'
  AND merged_after = '2026-09-01T00:00:00Z'
ORDER BY merged_at DESC;
```

## Pipelines and runners

Pipeline outcomes for a project, and the same across every project in a group by joining the inventory to each project's pipelines (the engine issues one pipelines request per project):

```sql
SELECT status, count(*) AS pipelines, ROUND(AVG(duration) / 60.0, 1) AS avg_minutes
FROM gitlab.ci.project_pipelines
WHERE full_path = 'gitlab-org/gitlab-runner'
GROUP BY status;

SELECT p.full_path,
       count(*) AS pipelines,
       SUM(c.status = 'FAILED') AS failed,
       ROUND(100.0 * SUM(c.status = 'FAILED') / count(*), 1) AS failure_pct
FROM gitlab.groups.group_projects p
JOIN gitlab.ci.project_pipelines c ON c.full_path = p.full_path
WHERE p.full_path = 'my-group'
GROUP BY p.full_path
ORDER BY failure_pct DESC;
```

Runner fleet status for a group, with contact recency (the instance-wide runner listing is administrator-only on gitlab.com; the group and project listings are what a token can read):

```sql
SELECT id, description, runner_type, status, paused, contacted_at, upgrade_status
FROM gitlab.ci.group_runners
WHERE full_path = 'my-group'
ORDER BY contacted_at DESC;
```

## Vulnerability reporting

Findings across a group by severity and state, then the unresolved criticals with their owning project:

```sql
SELECT severity, state, count(*) AS findings
FROM gitlab.security.group_vulnerabilities
WHERE full_path = 'my-group'
GROUP BY severity, state;

SELECT json_extract(project, '$.full_path') AS project,
       title, report_type, detected_at, web_url
FROM gitlab.security.group_vulnerabilities
WHERE full_path = 'my-group' AND severity = 'CRITICAL' AND state = 'DETECTED'
ORDER BY detected_at;
```

## Membership, and a join across providers

Group membership with access level is a table:

```sql
SELECT json_extract(user, '$.username') AS username,
       json_extract(user, '$.name') AS name,
       json_extract(access_level, '$.string_value') AS access_level,
       expires_at
FROM gitlab.groups.group_group_members
WHERE full_path = 'my-group';
```

which makes the offboarding question a `LEFT JOIN` against the identity provider - GitLab members with no active Okta user, computed locally by the SQL engine after `registry pull okta`:

```sql
SELECT json_extract(m.user, '$.username') AS gitlab_username,
       json_extract(m.user, '$.name') AS gitlab_name,
       o.status AS okta_status
FROM gitlab.groups.group_group_members m
LEFT JOIN okta.user.users o
  ON lower(json_extract(o.profile, '$.login')) = lower(json_extract(m.user, '$.username') || '@example.com')
 AND o.subdomain = 'my-okta-org'
WHERE m.full_path = 'my-group'
  AND (o.status IS NULL OR o.status != 'ACTIVE');
```

## How it is built

- The selection set for every resource is generated by one policy from the schema: all scalar and enum fields of the node type plus a fixed allowlist of nested identity objects. The query text and the response schema come from the same field list, so `DESCRIBE` always matches what the wire returns.
- GitLab enforces a query complexity limit (200 anonymous, 250 authenticated on gitlab.com). Every generated query is scored against the live limit as a build gate; the one node type that exceeded it (merge requests, at 245) was trimmed by measured per-field cost, protecting the approval and merge-state columns, to 190.
- A handful of resolvers time out on large result sets or answer anonymous callers with a server error. Those fields are excluded by a recorded policy entry rather than left to fail whole pages.
- `all_services.csv` in the repository records which schema field backs every resource and method, and regeneration fails when a mapping moves, so resource names stay stable between provider versions.

Premium-tier fields (issue weight, health status, epics) read back as null on the free tier, and the schema is gitlab.com's, so an older self-managed instance may reject fields it does not serve.

## Get started

Pull the provider from the public registry:

```bash
registry pull gitlab;
```

Provider docs are at [gitlab-provider.stackql.io](https://gitlab-provider.stackql.io). Let us know what you build. Star us on [__GitHub__](https://github.com/stackql/stackql).
