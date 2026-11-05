---
slug: stackql-1-0-released
title: "StackQL 1.0 released"
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-blog-post-featured-image.png"
description: "StackQL v1.0 replaces the any-sdk provider backend with omnisdk and adds session variables, provider sessions, secrets and opt-in caller access management."
keywords: [stackql, release, v1.0, omnisdk, session variables, access management, mcp]
tags: [stackql, release, omnisdk, mcp]
---

StackQL v1.0 is available from the [releases page](https://github.com/stackql/stackql/releases).  It is the first major version of StackQL and the first release built on [omnisdk](https://github.com/stackql-labs/omnisdk), the replacement for the `any-sdk` provider backend.  This post lists what changed for users since v0.12.824; the details are in the linked documentation.

<!-- truncate -->

## New provider backend

Every provider request is now planned and executed by omnisdk, and `any-sdk` is no longer a dependency.  Provider documents, the registry and the SQL you write are unchanged.  What changed is how a statement becomes requests and rows:

- **Graph planning.** A statement is planned as a graph of provider exchanges joined by data and control edges.  Joins, subqueries, CTEs, views and materialized views run through the same planner, and requests that fan out from a plan run concurrently within per-provider and per-host limits.
- **Pushdown.** Conditions that map to request parameters, and `LIMIT` where no ordering is required, are pushed into the requests rather than applied after every page has been fetched, so fewer requests are made and less data is staged.
- **Streaming.** Rows flow from the backend as responses arrive rather than after the last page lands, which lowers peak memory use on large result sets.

Result sets are the same.  The v1.0 test suite requires every mocked and live scenario to return the same rows through omnisdk as through the previous backend, and that check is mandatory in CI.  Logging and debug output are not identical and will not be made so.  One visible correction: boolean fields served by XML providers such as AWS are now reported as `true` and `false` rather than `0` and `1`.

## Session variables

`SET` and `UNSET` manage scalar variables for the current session, dereferenced with `$name` in any expression.  Values keep their type and are resolved before the request is planned, so a variable in a `WHERE` clause is pushed to the provider like a literal.

```sql
SET project = 'stackql-demo';
SET zone = 'australia-southeast1-a';
SET row_count = 10;

SELECT name, status FROM google.compute.instances
WHERE project = $project AND zone = $zone
LIMIT $row_count;
```

Variables can also name objects.  A `$variable` in a `FROM` clause resolves to a relation, and `IDENTIFIER($variable)` resolves to a column name in expression positions.  Both are converted to identifiers in the parsed statement and never spliced into SQL text.

```sql
SET relation_name = 'aws.s3.buckets';
SET column_name = 'name';

SELECT IDENTIFIER($column_name) AS bucket
FROM $relation_name
WHERE region = 'ap-southeast-2'
ORDER BY IDENTIFIER($column_name);
```

See [Session Variables](/getting-started/session-variables), [`SET`](/language-spec/set) and [`UNSET`](/language-spec/unset).  Session variables are separate from the Jsonnet templating variables supplied with `--iqldata`, which are unchanged.

## Provider sessions and secrets

A session can bind a provider under an alias with its own credentials, a tenancy label, parameter defaults and request concurrency limits.  Two AWS accounts can be queried in one statement:

```sql
SET "$.providers.production" = '{"provider":"aws","tenancy":"production-account",
  "auth":{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"},
  "params":{"region":"ap-southeast-2"}}';
SET "$.providers.development" = '{"provider":"aws","tenancy":"development-account",
  "auth":{"type":"aws_signing_v4","access_key_id_env_var":"DEVELOPMENT_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"DEVELOPMENT_AWS_SECRET_ACCESS_KEY"},
  "params":{"region":"ap-southeast-2"}}';

SELECT name, 'production' AS account FROM production.s3.buckets
UNION ALL
SELECT name, 'development' AS account FROM development.s3.buckets;
```

Individual credential fields can be changed for the session with `SET "$.auth.<provider>.<field>"`, and secrets can be held in session memory with `SET "$.secrets.<name>"` and referenced from an alias.  Persistent storage in the macOS Keychain, Linux Secret Service or Windows Credential Manager is available behind the operator flag `--allow-persistent-secrets`; flat-file stores are refused.  Secret values never enter SQL results unless the operator starts StackQL with `--allow-secret-dereference`.

See [Provider Sessions and Secrets](/getting-started/provider-sessions).

## Caller access management

`--access.config` enables authentication and authorisation of the caller, independent of provider credentials.  Four modes are supported: JWT with a pinned issuer, audience and public keys; static bearer tokens stored as SHA-256 digests; mutual TLS with a client CA and a URI or DNS SAN identity; and the local OS identity of the process.  Principals, identities, groups and memberships live in four tables in the configured SQLite or PostgreSQL backend, and policy rules grant actions on resources such as `host/sql/read` and the operations and credentials of a provider.

```sql
SELECT whoami() AS principal, groups() AS memberships;
```

Access management applies to `exec`, `shell` and the MCP server over stdio and HTTPS.  It is disabled by default and unprotected deployments behave as before.  See [Access Management](/command-line-usage/access-management).

## Grammar and functions

- `UNSET` is a new statement, and `SET` accepts an explicit `SESSION` scope.
- `||` is parsed as string concatenation with the standard precedence.  Earlier releases tokenised `||` as boolean `OR`, which gave the wrong result when it appeared inside a comparison.
- `CAST(expr AS TEXT | INTEGER | INT | BIGINT | BOOLEAN | BOOL)` and the matching `CONVERT(expr, type)` forms are accepted alongside the existing conversion types.
- `IDENTIFIER(value)` resolves a string to a column name.
- `md5(text)` returns the lowercase hexadecimal MD5 digest of its argument.
- `whoami()` and `groups()` report the authenticated caller when access management is enabled.

## MCP server

- Every tool carries a `title` and the four MCP behavioural hints as explicit booleans: `readOnlyHint`, `destructiveHint`, `idempotentHint` and `openWorldHint`.  `openWorldHint` is `true` for the tools that reach provider or registry endpoints and `false` for tools that read local state only.
- `run_select_query` refuses `INSERT`, `UPDATE`, `REPLACE`, `DELETE` and `EXEC` in every server mode, including `full_access`, and names `run_mutation_query` or `run_lifecycle_operation` in the error.  Previously those statements were gated by the server mode like any other payload.
- With access management enabled, the HTTPS transport authenticates each request with the configured JWT, bearer or client certificate scheme in place of the shared `auth_token_env_var` token.

## Provider documents and the registry

- Signature verification of provider documents remains off by default.  Setting `"verifyConfig": {"nopVerify": false}` in `--registry` requires an embedded ML-DSA-65 (FIPS 204) signature on every provider and service document read.  Private registries supply their own keys through `verifyConfig.publicKeys`.  The former certificate options and `.sig` sidecar files are no longer used.  See [`--registry`](/command-line-usage/global-flags#--registry).
- Provider documents can declare endpoint selectors on server variables, resolved in a fixed order: an explicit predicate, a provider alias parameter default, the variable's environment override, the selector's environment choice, then the selector's default.  Unknown selector values are errors rather than fallbacks, and `SHOW METHODS` reports the resolved bindings.
- Providers whose documents declare an OAuth token endpoint authenticate without an `--auth` entry; an explicit `token_url` in `--auth` or in a provider alias still wins.
- A registry that answers with an HTTP error now reports that status instead of a parse error from the error page.

## Packaging

- Release tags are `v1.0.<build>`.  Builds that are not from a release branch report a `-rc`, `-alpha` or `-beta` suffix in `stackql --version`, and their Docker tags carry the same suffix.
- The `stackql/stackql` Docker image is based on Ubuntu 26.04 LTS, and a variant with Python 3.13 preinstalled is tagged `-python3.13` (alias `-python3`).
- The ChatGPT, Codex and Claude Code plugins for the MCP server are built from one vendored launcher that needs only Node.js built-ins.  See [Installing StackQL](/installing-stackql#using-with-mcp-clients).

## Preview

Behind `--preview '{"unstable": true}'`, the `stackql_preview.iac.run` and `stackql_preview.iac.destroy` relations apply and tear down a declared set of resources through the converging run in omnisdk: the collection is locked for the run, intent is journalled before each effect, and a failed run is undone.  State is kept under the application root.  This surface is a preview; its syntax and output can change between releases.

## Upgrading

No changes are required to existing queries, provider documents or configuration files.  Review the following if your scripts depend on them:

- Boolean columns from XML providers return `true` and `false` instead of `0` and `1`.
- Expressions that relied on `||` being parsed as `OR` must use `OR`.
- MCP clients that sent writes through `run_select_query` must use `run_mutation_query` or `run_lifecycle_operation`.
- `stackql exec` exits with status `1` when a `SET` or `UNSET` configuration statement fails, when access management is enabled and a statement is refused, or when a preview IaC run reports a failed resource.

Binaries, installers, MCP bundles and container images are listed on the [installation page](/installing-stackql).
