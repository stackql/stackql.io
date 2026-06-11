---
title: Common StackQL Errors
description: Resolutions for the most common StackQL failures - provider not installed, missing required WHERE parameters, authentication failures, GitHub rate limiting, empty results, and MCP mutation refusals.
keywords: [stackql, errors, troubleshooting, authentication, registry, required parameters, mcp]
proficiencyLevel: Beginner
faq:
  - question: Why does my query say the table or provider cannot be found?
    answer: The provider is not installed locally. Provider definitions are pulled on demand - run REGISTRY PULL <provider> (for example REGISTRY PULL aws) and re-run the query. SHOW PROVIDERS lists what is currently installed.
  - question: Why does my query fail asking for parameters I didn't know about?
    answer: Cloud APIs have required routing parameters - region for AWS, subscriptionId for Azure, project for Google - which StackQL surfaces as required WHERE predicates. Run SHOW METHODS IN provider.service.resource to see each access method and its RequiredParams, then supply them as equality predicates.
  - question: Why does my query return an authorization error when my credentials are set?
    answer: Check three things in order - the variables are exported in the same shell or process environment that launched StackQL (server and MCP processes inherit their launch environment, not your current shell); the variable names match the provider's convention exactly; and the principal actually has permission for the operation, since provider 401/403 responses pass through StackQL unchanged.
---

# Common StackQL Errors

Most StackQL failures fall into five categories: missing providers, missing required parameters, authentication problems, provider-side limits, and gated operations. Each has a fast diagnostic.

## Provider or table not found

**Symptom**: a query references `aws.ec2.instances` (or any resource) and fails to resolve the provider or table.

**Cause**: provider definitions are not bundled; they are pulled per machine from the registry.

**Fix**:

```sql
SHOW PROVIDERS;          -- what is installed
REGISTRY LIST;           -- what is available
REGISTRY PULL aws;       -- install (repeat per provider)
```

Provider versions are also listed by `REGISTRY LIST` - re-pulling upgrades to the latest published spec.

## Missing required parameters

**Symptom**: a syntactically valid `SELECT` fails or returns nothing because a required parameter (commonly `region`, `subscriptionId`, `project`, `org`, or a parent resource identifier) was not supplied.

**Cause**: the underlying API operation has required routing parameters; StackQL surfaces them as required `WHERE` predicates.

**Fix**: discover the requirements, then supply them as equality (or `IN`) predicates:

```sql
SHOW METHODS IN aws.ec2.instances;
```

The `RequiredParams` column tells you, per access method, exactly what the `WHERE` clause must contain - for `aws.ec2.instances` the `describe` method requires `region`. This discovery step is the documented practice for both humans and agents (the MCP server publishes the same data through `list_methods`).

## Authentication failures

**Symptom**: metadata commands (`SHOW`, `DESCRIBE`) work, but `SELECT` or mutations fail with provider 401/403 errors.

**Cause**: metadata is served from local provider specs and needs no credentials; data operations call the provider. The credentials are absent from the process environment, misnamed, or insufficient.

**Fix**: verify the variables are exported in the environment that launched StackQL (a server or MCP process inherits its launch environment - restarting it after setting variables is a common miss), the names match the provider convention (`AWS_ACCESS_KEY_ID`/`AWS_SECRET_ACCESS_KEY`, `AZURE_TENANT_ID`/`AZURE_CLIENT_ID`/`AZURE_CLIENT_SECRET`, `GOOGLE_CREDENTIALS`, `STACKQL_GITHUB_USERNAME`/`STACKQL_GITHUB_PASSWORD`), and the principal has the permissions the operation needs. The per-provider how-tos under [/ai/how-tos](/ai/how-tos) cover each convention.

## GitHub rate limiting

**Symptom**: unauthenticated GitHub queries work briefly, then fail.

**Cause**: GitHub's unauthenticated limit is 60 requests per hour per IP, and one StackQL query can issue multiple paginated API calls.

**Fix**: authenticate with a personal access token (even a no-scope token raises the limit substantially) - see [How to authenticate StackQL to GitHub](/ai/how-tos/authenticate-stackql-to-github).

## Empty results that should not be empty

**Symptom**: the query succeeds but returns zero rows for resources you know exist.

**Checklist**: the routing predicates point at the right scope (correct region, project, subscription, or org); the access method selected by your predicates is the one you intended (`SHOW METHODS` shows which predicate combinations select which method - for example `org =` versus `username =` on `github.repos.repos`); and the credential can see the resources (cloud IAM can filter list results silently).

## MCP mutation refused

**Symptom**: an agent's `INSERT`/`UPDATE`/`DELETE` through the MCP server is refused, mentioning elicitation or the server mode.

**Cause**: this is the safety contract working, not an error. The default `safe` mode requires human approval for mutations; clients that do not advertise the MCP elicitation capability cannot grant it.

**Fix**: use an elicitation-capable client (Claude Desktop, Cursor, Continue) and approve the prompt, or - for trusted, non-interactive automation only - run the server with `"mode": "full_access"`. Details in [StackQL MCP Architecture](/ai/architecture/stackql-mcp-architecture).

## Related concepts

- [How to authenticate StackQL to AWS](/ai/how-tos/authenticate-stackql-to-aws) - and the Azure, Google Cloud, and GitHub equivalents
- [StackQL MCP Architecture](/ai/architecture/stackql-mcp-architecture) - modes and elicitation explained
- [Getting started with StackQL](/ai/tutorials/getting-started-with-stackql) - a clean-room setup that avoids most of these
- [What is SQL for APIs?](/ai/canonical-definitions/what-is-sql-for-apis) - why required parameters exist
- [StackQL FAQ](/ai/faqs/stackql-faq) - broader questions
