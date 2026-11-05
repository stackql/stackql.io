---
title: Access Management
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - access management
  - authentication
  - authorization
  - jwt
  - mtls
  - bearer token
description: "Opt-in caller access management for StackQL: authenticate callers with JWT, bearer tokens, mutual TLS or the local OS identity, map them to principals and groups in a directory, and authorise statements with policy rules."
image: "/img/stackql-featured-image.png"
---

Caller access management authenticates the caller of a StackQL session and authorises each statement against a policy.  It is independent of provider credentials, which continue to be supplied through `--auth`, environment variables or [provider sessions](/getting-started/provider-sessions).  It is disabled by default, and deployments that do not enable it behave as before.  Available in StackQL `v1.0` and later.

See also:  
[[ Global Flags ]](/command-line-usage/global-flags) [[` mcp `]](/command-line-usage/mcp) [[` whoami `]](/language-spec/functions/session/whoami) [[` groups `]](/language-spec/functions/session/groups)

* * * 

## Enabling access management

Access management is switched on by starting StackQL with the `--access.config` global flag pointing at a JSON configuration file.  The example starts an interactive shell in protected mode; `exec` and `mcp` are enabled the same way.

```bash
stackql --access.config '{"path": "/etc/stackql/access.json"}' shell
```

`--access.config` takes a JSON envelope with a single `path` key; a bare file path is also accepted.  The configuration is loaded once at startup, so rotate keys or policy through a controlled restart.

While access management is enabled only `exec`, `shell`, `mcp` and `version` run; other commands, including `registry`, refuse rather than bypass the protected path.  `srv` and the MCP `reverse_proxy` server type also refuse, because the PostgreSQL wire dependency does not yet expose per-connection authentication evidence.  The SQL `AUTH` and `AUTH REVOKE` statements refuse in protected mode; provision provider credentials through host configuration instead.  A protected `exec` that fails returns exit status `1` after presenting the error.

## Authentication modes

The configuration file selects one `type`: `jwt` (the default when omitted), `bearer`, `mtls` or `local_os`.  Configuration is strict JSON: unknown fields, and fields belonging to another mode, are rejected.  All modes share the same `rules` and the same [principal directory](#principal-directory).

### JWT

The caller presents a signed token, which is verified offline against the keys pinned in the configuration.  A minimal configuration names the issuer and audience the token must carry, lists one public key, and grants the `readers` group read access:

```json
{
  "type": "jwt",
  "issuer": "https://identity.example",
  "audience": "stackql",
  "keys": [
    {"kid": "primary", "algorithm": "EdDSA", "public_key_pem": "-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----\n"}
  ],
  "rules": [
    {"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}
  ]
}
```

The issuer, audience and public keys are pinned in the configuration; there is no OIDC discovery.  Accepted algorithms are `EdDSA`, `RS256`, `RS384`, `RS512`, `PS256`, `PS384`, `PS512`, `ES256`, `ES384` and `ES512`, and RSA keys must be at least 2048 bits.  Each key is bound to its configured algorithm.  Tokens with an unknown key ID, an invalid signature, a missing or past expiry, a future `nbf` or `iat`, or a wrong issuer or audience are rejected.  Group claims and URLs carried in the token are not used for trust or directory membership.

### Static bearer token

The caller presents an opaque token, and the configuration lists the SHA-256 digest of each accepted token with the subject it identifies.  This suits a small, fixed set of service identities where running a token issuer is not warranted:

```json
{
  "type": "bearer",
  "issuer": "local-tokens",
  "tokens": [{"subject": "alice", "sha256": "<64 hexadecimal characters: the SHA-256 of the token>"}],
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

Only the SHA-256 digest of each token is configured, and digests must be unique.  Changing the configured digests requires a restart.

### Mutual TLS

The caller is identified by the client certificate it presents on the HTTPS MCP transport.  The configuration carries the CA that must have issued the certificate and says which subject alternative name holds the identity:

```json
{
  "type": "mtls",
  "issuer": "client-certificates",
  "client_ca_pem": "-----BEGIN CERTIFICATE-----\n...\n-----END CERTIFICATE-----\n",
  "certificate_identity": "uri_san",
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

The client certificate chain is verified against `client_ca_pem` and the identity subject is taken from exactly one SAN of the configured kind, `uri_san` or `dns_san`.  There is no Common Name fallback.  Mutual TLS is available over the HTTPS MCP transport only.

### Local OS identity

No credential is exchanged: the subject is the operating system identity that the StackQL process runs as.  The configuration needs only an `issuer` label and the rules:

```json
{
  "type": "local_os",
  "issuer": "this-host",
  "rules": [{"subject": "group:readers", "resource": "host/sql/read", "action": "execute"}]
}
```

The identity subject is the effective Unix UID or Windows SID of the StackQL process, including numeric container UIDs with no passwd entry.  No credential is presented and `STACKQL_CALLER_TOKEN` is ignored.  This mode is local only: it is refused for HTTP callers.  Operating system group membership does not grant access; only directory membership does.

### Session deadline

Queries have no authentication-derived deadline by default.  Credentials are verified and directory membership is refreshed for each new statement, so an expired credential cannot start new work, but expiry does not interrupt a statement that is already authorised.  Set `"enforce_session_deadline": true` to cancel at `max_session_seconds` (default `300`, maximum `86400`), shortened by JWT or certificate expiry.  This does not renew tokens.

## Principal directory

Identities are mapped to principals, and principals to groups, in four tables in the configured SQLite or PostgreSQL backend:

| Table | Contents |
|--|--|
| `stackql_access_principals` | Principal ID and a JSON object of string attributes.  A `tenant` attribute namespaces preview IaC state. |
| `stackql_access_identities` | Verified `(issuer, subject)` pair to principal ID. |
| `stackql_access_groups` | Group IDs. |
| `stackql_access_memberships` | Principal to group bindings. |

Provision the tables, identities and memberships through a trusted database administration connection before serving protected queries:

```sql
INSERT INTO stackql_access_principals (id, attributes) VALUES ('alice', '{}');
INSERT INTO stackql_access_groups (id) VALUES ('readers');
INSERT INTO stackql_access_identities (issuer, subject, principal_id)
VALUES ('https://identity.example', 'alice', 'alice');
INSERT INTO stackql_access_memberships (principal_id, group_id) VALUES ('alice', 'readers');
```

Use the mode's actual issuer and subject: for local OS identity this might be `('this-host', '1000')`, and for mutual TLS `('client-certificates', 'spiffe://example/alice')`.  Select the backend explicitly:

```bash
stackql --access.config '{"path":"/etc/stackql/access.json"}' \
  --sqlBackend '{"dbEngine":"postgres_tcp","dsn":"postgres://runtime@localhost/stackql?sslmode=require","sqlDialect":"postgres"}' \
  exec 'SELECT whoami(), groups();'
```

The directory produces an immutable snapshot for each statement.  Revocation is a `DELETE FROM stackql_access_memberships` on the administration connection; the next statement sees the new snapshot.  Unknown identities, an absent schema, malformed data and directory outages all deny access.  Ordinary protected SQL cannot address the directory tables.  Keep membership writes on the administration connection, protect the database file, and use separate administrative and runtime database privileges where the backend supports them.

## Policy rules

Each rule grants one `action` on one `resource` to a `subject` of the form `group:<group id>`.  Use the compiled method and operation identifiers from trusted provider documents; an operation wildcard grants mutation methods as well as reads.

The `readers` group implicitly holds the host's safe-read SQL capability.  Reads by principals outside that group require `host/sql/read`, and other statements require `execute` on `host/sql/<statement kind>`.  Provider operations and credential use always require explicit rules, and a collection grant does not grant its underlying methods.  Additional host checks:

| Resource | Action | Scope |
|--|--|--|
| `host/sql/read` | `execute` | Read statements outside the implicit reader group |
| `host/sql/exec` | `execute` | Native `EXEC`, including nested `EXEC` |
| `host/iac/run`, `host/iac/destroy` | `execute` | The preview IaC relations, despite their `SELECT` syntax |
| `host/function/<name>` | `execute` | Functions outside the small allowlist of known-pure functions |
| `host/backend` | `read` | Opaque stored and external backend relations |
| `host/credentials` | `reload` | The MCP `reload_credentials` tool |
| `session/variables` | `read` | Dereferencing [session variables](/getting-started/session-variables) and secrets |
| `session/config` | `write` | `SET` and `UNSET` |

Stored query expansion is checked before execution, and plan and row cache reuse is disabled on protected paths.

## Entry points

| Authentication | `exec`, `shell`, MCP stdio | HTTPS MCP |
|--|--|--|
| JWT | `STACKQL_CALLER_TOKEN` environment variable | `Authorization: Bearer` request header |
| Static bearer | `STACKQL_CALLER_TOKEN` environment variable | `Authorization: Bearer` request header |
| Local OS | Effective process UID or SID; no credential | Refused |
| Mutual TLS | Refused: no verified TLS connection | Verified client chain and one configured SAN |

Supply local token credentials through the process environment, never as a SQL literal or a command-line argument.  Over HTTPS the caller verifier replaces the shared `auth_token_env_var` bearer gate described in [HTTP client authentication](/command-line-usage/mcp#http-client-authentication); server TLS certificate and key configuration is mandatory, and a request without credentials never falls back to the server's environment token.

## Inspecting the caller

In an authenticated session, `whoami()` returns the principal ID and `groups()` returns the directory groups as a JSON array:

```sql
SELECT whoami() AS principal, groups() AS memberships;
```

```
|-----------|-------------|
| principal | memberships |
|-----------|-------------|
| alice     | ["readers"] |
|-----------|-------------|
```

Both functions require a session started with `--access.config`.

## Walkthroughs

The StackQL repository ships [four executable walkthroughs](https://github.com/stackql/stackql/blob/main/docs/examples/access-management/README.md), one per authentication mode, driven by a Python script that provisions a demonstration directory and verifies a query.  They need no cloud account and are run by the project's test suite.
