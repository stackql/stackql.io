---
title: How to authenticate StackQL to GitHub
description: StackQL authenticates to GitHub using basic authentication with a personal access token, supplied via the STACKQL_GITHUB_USERNAME and STACKQL_GITHUB_PASSWORD environment variables; public data can be queried without authentication at lower rate limits.
keywords: [stackql, github authentication, personal access token, github api, environment variables]
proficiencyLevel: Beginner
faq:
  - question: Can I query GitHub with StackQL without authenticating?
    answer: Yes, for public data. Unauthenticated requests can read public repositories, organizations, and users, subject to GitHub's low unauthenticated rate limit. Authenticated requests get substantially higher limits and access to private resources the token can see.
  - question: What token scopes are needed?
    answer: For public-data queries, a token with no scopes raises your rate limit. Reading private repositories requires the repo scope; organization data may require read:org. Mutations (INSERT, UPDATE, DELETE) require scopes matching the operation, such as repo or delete_repo.
  - question: Does StackQL support GitHub Enterprise?
    answer: The GitHub provider targets the github.com REST API surface as described in the StackQL provider registry. For GitHub Enterprise Cloud, which is served at github.com endpoints, organization and repository queries work with an appropriately scoped token.
---

# How to authenticate StackQL to GitHub

StackQL authenticates to GitHub with basic authentication: your GitHub username and a personal access token (PAT), supplied as `STACKQL_GITHUB_USERNAME` and `STACKQL_GITHUB_PASSWORD`. Public data is queryable without any credentials at GitHub's unauthenticated rate limits.

## Steps

1. Create a personal access token in GitHub (Settings -> Developer settings). Grant only the scopes your queries need - none for public data at higher rate limits, `repo` for private repositories.

2. Export the credentials:

```bash
export STACKQL_GITHUB_USERNAME=yourghuser
export STACKQL_GITHUB_PASSWORD=ghp_yourgithubpersonalaccesstoken
```

3. Pull the GitHub provider (first use only):

```sql
REGISTRY PULL github;
```

4. Verify with a query:

```sql
SELECT name, description, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

The `org` predicate selects the `list_for_org` access method; `WHERE username = '...'` selects `list_for_user` instead. `SHOW METHODS IN github.repos.repos` lists every method and its required parameters, including the mutation methods (`create_in_org`, `update`, `delete`).

## Unauthenticated access

The query above works without credentials because the `stackql` organization is public - useful for evaluation and for agents exploring the query model before credentials are provisioned. GitHub's unauthenticated rate limit is low (60 requests per hour per IP), so any sustained use should authenticate.

## Related concepts

- [How to query GitHub repositories with StackQL](/ai/how-tos/query-github-repositories-with-stackql) - the full query patterns
- [How to authenticate StackQL to AWS](/ai/how-tos/authenticate-stackql-to-aws) - the AWS equivalent
- [What is SQL for APIs?](/ai/canonical-definitions/what-is-sql-for-apis) - how REST operations map to SQL verbs
- [Getting started with StackQL](/ai/tutorials/getting-started-with-stackql) - GitHub is the fastest zero-credential start
- [Common StackQL errors](/ai/troubleshooting/common-stackql-errors) - including rate-limit symptoms
