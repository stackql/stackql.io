---
title: How to query GitHub repositories with StackQL
description: Query GitHub repositories as the SQL table github.repos.repos - list repositories by organization or user, filter on language, visibility, and activity, and update repository settings with SQL.
keywords: [stackql, github, repositories, sql, github api, devops, governance]
proficiencyLevel: Beginner
faq:
  - question: Do I need credentials to query public GitHub repositories?
    answer: No. Public organizations and repositories are queryable unauthenticated, subject to GitHub's unauthenticated rate limit of 60 requests per hour. Set STACKQL_GITHUB_USERNAME and STACKQL_GITHUB_PASSWORD (a personal access token) for higher limits and private data.
  - question: What other GitHub resources can StackQL query?
    answer: The github provider covers the documented REST surface - among others github.repos exposes branches, commits, releases, collaborators, webhooks, deployments, environments, tags, teams, and traffic statistics. SHOW RESOURCES IN github.repos lists them all.
  - question: Can StackQL change repository settings?
    answer: Yes. github.repos.repos exposes UPDATE (requiring owner and repo), INSERT (create_in_org), and DELETE methods. Request-body fields are set through data__ prefixed columns, and the required parameters for each method are listed by SHOW METHODS IN github.repos.repos.
---

# How to query GitHub repositories with StackQL

GitHub repositories are exposed as the table `github.repos.repos`. Listing by organization or user maps to the corresponding GitHub REST operations, selected by the `WHERE` clause: `org = '...'` uses the organization listing, `username = '...'` the user listing.

## Prerequisites

- The GitHub provider installed: `REGISTRY PULL github;`
- Optional credentials for private data and higher rate limits - see [How to authenticate StackQL to GitHub](/ai/how-tos/authenticate-stackql-to-github)

## List an organization's repositories

```sql
SELECT name, description, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

Run against the public `stackql` organization, this returns rows such as the `stackql` engine repository (Go, 861 stars at the time of writing) and `stackql-provider-registry` - live API data, no snapshot involved.

## Filter on repository attributes

Standard SQL predicates apply to any column:

```sql
SELECT name, visibility, default_branch, archived
FROM github.repos.repos
WHERE org = 'my-org'
AND archived = false
AND visibility = 'public';
```

Useful governance columns include `private`, `visibility`, `archived`, `fork`, `default_branch`, `delete_branch_on_merge`, `has_wiki`, `topics`, `created_at`, `pushed_at`, and `security_and_analysis`. `DESCRIBE github.repos.repos` lists all of them.

## Beyond the repository list

The `github.repos` service exposes the rest of the repository surface as sibling tables - `github.repos.branches`, `github.repos.commits`, `github.repos.releases`, `github.repos.collaborators`, `github.repos.webhooks`, `github.repos.deployments`, and more. Discover them with:

```sql
SHOW RESOURCES IN github.repos;
```

## Updating repositories

Repository settings changes are `UPDATE` statements; the method requires `owner` and `repo`, and body fields use the `data__` prefix:

```sql
UPDATE github.repos.repos
SET data__delete_branch_on_merge = true
WHERE owner = 'my-org' AND repo = 'my-repo';
```

Applied across a result set from the listing query, this is fleet-wide repository governance in two statements - a pattern that pairs naturally with cross-provider queries joining GitHub data to cloud resources.

## Related concepts

- [How to authenticate StackQL to GitHub](/ai/how-tos/authenticate-stackql-to-github) - tokens and rate limits
- [What is SQL for APIs?](/ai/canonical-definitions/what-is-sql-for-apis) - the REST-to-SQL mapping used here
- [Getting started with StackQL](/ai/tutorials/getting-started-with-stackql) - GitHub as the zero-credential first provider
- [How to use StackQL with AI agents](/ai/how-tos/use-stackql-with-ai-agents) - agents running these queries via MCP
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe) - read-only alternatives compared
