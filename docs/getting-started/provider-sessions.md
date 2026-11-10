---
title: Provider Sessions and Secrets
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - provider sessions
  - multi-account
  - credentials
  - secrets
  - keychain
description: "Provider sessions in StackQL: bind provider aliases with their own credentials, tenancy and parameter defaults, override credential fields per session, and hold secrets in session memory or an OS secret store."
image: "/img/stackql-featured-image.png"
---

See also:  
[[` SET `]](/language-spec/set) [[` UNSET `]](/language-spec/unset) [[ Session Variables ]](/getting-started/session-variables) [[ Global Flags ]](/command-line-usage/global-flags)

A session can bind a provider under an alias with its own credentials, tenancy label, parameter defaults and request concurrency limits, so several accounts or tenants of the same provider can be queried in one session, and in one statement.  Credential fields can also be overridden for the session, and secrets can be held in session memory or, with an operator opt-in, in the operating system's secret store.  All of this is configured with [`SET`](/language-spec/set) and [`UNSET`](/language-spec/unset) and is available in StackQL `v1.0` and later.

Everything described here is scoped to the current session.  Independent connections to a `stackql srv` server do not share bindings or secrets, and each MCP request is its own session, so configure and query in one statement batch when using the MCP server.

## Provider aliases {#provider-aliases}

`SET "$.providers.<alias>"` binds an alias to a provider.  The value is a JSON document held in a string:

| Key | Required | Description |
|--|--|--|
| `provider` | Yes | The canonical provider the alias resolves to, for example `aws`. |
| `tenancy` | Yes | A grouping label such as an account or tenant name.  StackQL does not verify it against the provider. |
| `auth` | One of `auth` or `auth_secret` | An authentication document with the same shape as the provider's entry in the [`--auth`](/command-line-usage/global-flags) object.  The alias does not borrow the canonical provider's credentials. |
| `auth_secret` | One of `auth` or `auth_secret` | The name of a [session secret](#session-secrets) holding the authentication document. |
| `params` | No | Default values for declared provider parameters, including declared server parameters.  A predicate in the query overrides a default. |
| `tuning` | No | `max_concurrent` and `max_per_host` limits for the alias's requests.  Aliases that share a provider and tenancy share these controls and must declare identical tuning. |
<br/>
The following binds two AWS accounts under separate aliases, each reading its keys from differently named environment variables, and queries both in one statement:

```sql
SET "$.providers.production" = '{
  "provider": "aws",
  "tenancy": "production-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "PRODUCTION_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "PRODUCTION_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 4, "max_per_host": 4}
}';

SET "$.providers.development" = '{
  "provider": "aws",
  "tenancy": "development-account",
  "auth": {
    "type": "aws_signing_v4",
    "access_key_id_env_var": "DEVELOPMENT_AWS_ACCESS_KEY_ID",
    "secret_access_key_env_var": "DEVELOPMENT_AWS_SECRET_ACCESS_KEY"
  },
  "params": {"region": "ap-southeast-2"},
  "tuning": {"max_concurrent": 2}
}';

SELECT name, 'production' AS account FROM production.s3.buckets
UNION ALL
SELECT name, 'development' AS account FROM development.s3.buckets;
```

Environment variable references are resolved from the StackQL process environment; there is no session-local environment.  Temporary AWS credentials also need the session token field or environment variable reference.

An alias behaves like its provider for metadata and data statements: `SHOW`, `DESCRIBE`, `SELECT`, `INSERT`, `UPDATE`, `DELETE` and `EXEC` all accept the alias in place of the provider name.  Aliases do not extend to native built-in relations or to the preview IaC relations.  `UNION ALL` branches that use different aliases run sequentially.

Each query takes a detached snapshot of the alias's credentials.  Changing a binding affects later queries, not one that is already running.  `UNSET "$.providers.<alias>"` removes the binding.

## Overriding credential fields {#overriding-credential-fields}

`SET "$.auth.<provider>.<field>"` changes one field of a provider's authentication context for the session, where the field names are the keys of the provider's `--auth` entry:

```sql
SET "$.auth.github.credentialsenvvar" = 'GITHUB_TOKEN_READONLY';
SELECT name FROM github.repos.repos WHERE org = 'stackql';
UNSET "$.auth.github.credentialsenvvar";
```

As with aliases, the change applies to queries started after the `SET`.  Queries against a provider whose authentication has been unset, or whose alias has no usable `auth`, fail before any request is sent.

## Session secrets {#session-secrets}

By default every session has a memory-only secret store, with no configuration needed.  `SET "$.secrets.<name>"` stores a literal string, typically a JSON authentication document, and a provider alias references it with `auth_secret`:

```sql
SET "$.secrets.production" = '{"type":"aws_signing_v4","access_key_id_env_var":"PRODUCTION_AWS_ACCESS_KEY_ID","secret_access_key_env_var":"PRODUCTION_AWS_SECRET_ACCESS_KEY"}';
SET "$.providers.production" = '{"provider":"aws","tenancy":"production-account","auth_secret":"production","params":{"region":"ap-southeast-2"}}';

SELECT name FROM production.s3.buckets;

UNSET "$.secrets.production";
```

Rotating a secret affects later queries; deleting it makes later uses fail.  A new session receives a fresh, empty store, and ending the session releases it.  `UNSET "$.secret_store"` discards the current store binding and starts again with empty session memory.

Normal credential use, and `SET` statements that store variables or configuration, do not write secrets into the SQL backend.  Putting sensitive data into an ordinary variable or table is the caller's responsibility.

## Persistent OS secret stores {#persistent-os-secret-stores}

Persistent storage is disabled by default and can only be enabled by the operator starting StackQL with `--allow-persistent-secrets`; SQL cannot grant it.  With the flag set, a session selects an OS store with:

```sql
SET "$.secret_store" = '{"backend": "os", "namespace": "stackql/provider-auth"}';
```

The store is the macOS Keychain, the Linux Secret Service (`secret-tool` with a working session service) or the Windows Credential Manager.  Only the `os` backend is accepted: flat-file stores, file paths and any implicit file fallback are rejected even with the flag set, and an unavailable OS facility fails rather than falling back to a file.

Values written to an OS store outlive the SQL session and are deleted explicitly with `UNSET "$.secrets.<name>"`.  Resetting the store binding does not delete values already persisted.  Access to the store is governed by the operating system identity of the StackQL process; a namespace is not a per-user access control, so do not treat this opt-in as a multi-user vault.

## Secret values in SQL {#secret-values-in-sql}

Secret values are never dereferenced into SQL unless the operator starts StackQL with `--allow-secret-dereference`.  With that flag, references such as `"$secrets.production"`, `"$secrets.production.credentials"`, `"$auth.github.password"` and `"$providers.production.auth.credentials"` resolve in expressions:

```sql
SELECT upper("$secrets.production.credentials") AS credential;

/*
-- the same statement in a session started without --allow-secret-dereference
SELECT upper("$secrets.production.credentials") AS credential;
-- FAILS with secret variable dereferencing is disabled
*/
```

Started without the flag, the same statement is refused before anything is evaluated, as the commented-out copy shows.  The flag is an acknowledgement that explicitly dereferenced values can enter SQL staging, results, logs and persisted query artefacts.  Normal provider authentication does not require it.

## Permissions {#permissions}

With [access management](/command-line-usage/access-management) enabled, reading variables and secrets requires `session/variables:read`, and `SET` or `UNSET` requires `session/config:write`.  A resolved alias is authorised as its canonical provider before execution.  Per-variable and per-secret ownership and grants are not implemented.
