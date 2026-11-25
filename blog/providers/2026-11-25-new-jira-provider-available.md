---
slug: new-jira-provider-available
title: New Jira Provider Available
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-jira-provider-featured-image.png"
description: A new StackQL provider for Jira Cloud - search issues with JQL, report on projects, permission schemes, sprints and the audit log, and create and change issues, versions, components, filters, boards and sprints using SQL - available in the StackQL Provider Registry now.
keywords: [stackql, jira, jira cloud, atlassian, jql, jira software, agile, sprints, issues, provider, devops, sql]
tags: [stackql, jira, atlassian, provider, devops]
---

We've released a new StackQL provider for Jira Cloud:

- [__`jira`__](https://jira-provider.stackql.io) - the Jira Cloud platform REST API v3 and the Jira Software REST API: __`issues`__, __`projects`__, __`users`__, __`permissions`__, __`workflows`__, __`fields`__, __`issue_types`__, __`screens`__, __`dashboards`__, __`filters`__, __`issue_attributes`__, __`notifications`__, __`jql`__, __`plans`__, __`system`__, __`agile`__ (boards, sprints and epics) and __`atlassian_tenant`__ (17 services, 227 resources, 611 methods)

<!-- truncate -->

The provider is generated from the two OpenAPI documents Atlassian publishes for Jira Cloud. It covers issues and everything attached to them (comments, worklogs, links, watchers, votes, properties and attachment metadata), the JQL issue search, projects with their components, versions and roles, users and groups, workflows and statuses, fields and custom field contexts, issue types, screens, the permission, notification and issue security schemes, filters, dashboards, plans, site configuration and the audit log, and the Jira Software boards, sprints and epics. Not mapped: operations that only Connect, Forge or OAuth 2.0 (3LO) apps can call, attachment and avatar uploads and downloads, and deprecated operations whose replacement is mapped. Jira Service Management has its own API and is not part of this provider.

## Connect

Authentication is an Atlassian service account with an OAuth 2.0 credential, using the client credentials grant: `ATLASSIAN_CLIENT_ID` and `ATLASSIAN_CLIENT_SECRET`. Requests go through the Atlassian API gateway, which addresses a Jira site by its cloud ID, read from `ATLASSIAN_CLOUD_ID`. The site serves its cloud ID publicly, so it can be looked up before anything else is set:

```bash
export ATLASSIAN_CLIENT_ID=...
export ATLASSIAN_CLIENT_SECRET=...
export ATLASSIAN_CLOUD_ID=$(curl -s https://acme.atlassian.net/_edge/tenant_info | jq -r .cloudId)
stackql shell
```

The credential's scopes are chosen when it is created: the classic Jira scopes (`read:jira-work`, `write:jira-work`, `read:jira-user`, `manage:jira-project`, `manage:jira-configuration`) for the platform API, plus the granular `*:jira-software` scopes for boards and sprints. The service account needs Jira product access and the project roles the operations need, as any user does.

The same lookup is the `atlassian_tenant` service, which takes the site's subdomain and needs the client ID and secret but not the cloud ID. Joined to the resource being read, it supplies the cloud ID without the environment variable:

```sql
SELECT cloud_id FROM jira.atlassian_tenant.info WHERE site = 'acme';

SELECT p.key, p.name
FROM jira.atlassian_tenant.info t
INNER JOIN jira.projects.projects p ON p.cloud_id = t.cloud_id
WHERE t.site = 'acme';
```

`SET "$.params_by_provider.jira" = '{"cloud_id": "<cloud id>"}'` sets it as the default for the session instead. Columns, parameters and body attributes are snake_case; the provider maps them to the API's camelCase names on the wire.

## Searching issues with JQL

Issues are searched with JQL, passed as the `jql` parameter in the `WHERE` clause. Each row carries the issue `id` and `key` and its fields as one JSON column, read with `json_extract`. Unresolved issues in a project, by assignee:

```sql
SELECT json_extract(fields, '$.assignee.displayName') AS assignee,
       COUNT(*) AS open_issues
FROM jira.issues.issues
WHERE jql = 'project = ENG AND resolution = Unresolved'
GROUP BY assignee
ORDER BY open_issues DESC;
```

Unresolved issues not updated in 30 days, oldest first:

```sql
SELECT key,
       json_extract(fields, '$.summary') AS summary,
       json_extract(fields, '$.status.name') AS status,
       json_extract(fields, '$.updated') AS updated
FROM jira.issues.issues
WHERE jql = 'project = ENG AND resolution = Unresolved AND updated <= -30d ORDER BY updated ASC';
```

Rich text (an issue description, a comment body) is Atlassian Document Format JSON: read it with `json_extract` and write it as a JSON document. Users are identified by `account_id`.

Lists are read to the last page, whichever of Jira's three paging styles they use (`startAt`, a page token, or `offset` on the audit log). `LIMIT` is sent to Jira as the page size, and `OFFSET` and `ORDER BY` are sent where the list supports them.

## Projects, permissions and the audit log

Every project the credential can browse, with its type and lead:

```sql
SELECT key, name, project_type_key, style,
       json_extract(lead, '$.displayName') AS lead
FROM jira.projects.projects
WHERE expand = 'description,lead'
ORDER BY key;
```

The grants of every permission scheme, one row per scheme and permission, and whether the credential holds a set of permissions in a project:

```sql
SELECT ps.name AS scheme,
       json_extract(g.value, '$.permission') AS permission,
       json_extract(g.value, '$.holder.type') AS holder_type,
       json_extract(g.value, '$.holder.parameter') AS holder
FROM jira.permissions.permission_schemes ps,
     json_each(ps.permissions) g
WHERE ps.expand = 'permissions'
ORDER BY scheme, permission;

SELECT key, have_permission
FROM jira.permissions.my_permissions
WHERE permissions = 'BROWSE_PROJECTS,CREATE_ISSUES,TRANSITION_ISSUES,DELETE_ISSUES'
AND project_key = 'ENG';
```

Audit log records by category (the audit log needs the Administer Jira global permission):

```sql
SELECT category, COUNT(*) AS records
FROM jira.system.audit_records
GROUP BY category
ORDER BY records DESC;
```

## Boards and sprints

The scrum boards of a project, and the closed sprints of a board with their dates and goals:

```sql
SELECT id, name, type,
       json_extract(location, '$.projectKey') AS project
FROM jira.agile.boards
WHERE type = 'scrum' AND project_key_or_id = 'ENG';

SELECT id, name, state, start_date, complete_date, goal
FROM jira.agile.board_sprints
WHERE board_id = '12' AND state = 'closed'
ORDER BY complete_date DESC;
```

## Provisioning

Mutations use the same SQL grammar - `INSERT` creates, `UPDATE` changes, `DELETE` removes, and actions such as a workflow transition are `EXEC` methods on the resource they act on. Jira's updates change only the fields supplied. An issue created, changed, moved through its workflow, commented on and removed:

```sql
INSERT INTO jira.issues.issues (fields)
SELECT '{"project": {"key": "ENG"}, "summary": "Rotate the build agent keys", "issuetype": {"name": "Task"}, "labels": ["ops"]}'
RETURNING id, key;

UPDATE jira.issues.issues
SET fields = '{"summary": "Rotate the build agent keys before Friday"}'
WHERE issue_id_or_key = 'ENG-123';

SELECT id, name FROM jira.issues.transitions WHERE issue_id_or_key = 'ENG-123';

EXEC jira.issues.issues.transition
  @issue_id_or_key = 'ENG-123',
  @transition = '{"id": "31"}';

INSERT INTO jira.issues.comments (issue_id_or_key, body)
SELECT 'ENG-123', '{"type": "doc", "version": 1, "content": [{"type": "paragraph", "content": [{"type": "text", "text": "Done by the rotation job."}]}]}';

DELETE FROM jira.issues.issues
WHERE issue_id_or_key = 'ENG-123';
```

A release version created and released, and a sprint created on a board with issues moved into it:

```sql
INSERT INTO jira.projects.versions (name, project_id, description)
SELECT '2026.12', 10001, 'December release'
RETURNING id, name;

UPDATE jira.projects.versions
SET released = true, release_date = '2026-12-15'
WHERE id = '10042';

INSERT INTO jira.agile.sprints (name, origin_board_id, goal)
SELECT 'Sprint 42', 12, 'Rotate the build agent keys'
RETURNING id, state;

EXEC jira.agile.sprints.move_issues
  @sprint_id = '57',
  @issues = '["ENG-123", "ENG-124"]';
```

## Several sites, other providers

A `WHERE cloud_id = '...'` value takes precedence over `ATLASSIAN_CLOUD_ID`, so one session can read every site the credential can reach:

```sql
SELECT 'acme' AS site, key, name
FROM jira.projects.projects
WHERE cloud_id = '<acme cloud id>'
UNION ALL
SELECT 'acme-labs' AS site, key, name
FROM jira.projects.projects
WHERE cloud_id = '<acme-labs cloud id>';
```

Open work in Jira alongside open issues in a GitHub repository, using the `github` provider in the same session:

```sql
SELECT 'jira' AS tracker, key AS ref,
       json_extract(fields, '$.summary') AS title
FROM jira.issues.issues
WHERE jql = 'project = ENG AND resolution = Unresolved'
UNION ALL
SELECT 'github' AS tracker, CAST(number AS TEXT) AS ref, title
FROM github.issues.issues
WHERE owner = 'acme' AND repo = 'platform' AND state = 'open';
```

## Get started

Pull the provider and start querying:

```sql
REGISTRY PULL jira;
```

The full reference is at [jira-provider.stackql.io](https://jira-provider.stackql.io), and the source is at [stackql-registry/stackql-provider-jira](https://github.com/stackql-registry/stackql-provider-jira). Questions and requests are welcome in the [StackQL Discussions](https://github.com/orgs/stackql/discussions).
