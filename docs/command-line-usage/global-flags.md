---
title: Global Flags
hide_title: false
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: "Global flags for the stackql binary: authentication, output format, registry location, logging and SQL backend settings, available on every command."
image: "/img/stackql-featured-image.png"
---

Global flags specify runtime program behavior for the StackQL application, these flags are set at the command line when the StackQL application is launched (in either the interactive/shell mode or non-interactive exec mode) or are sourced from the StackQL initialization ([`.stackqlrc`](/getting-started/using-stackql#the-stackqlrc-initialization-file)) file.  Flags include:  

| Flag | Data Type | Description | Default |
| -- | -- | -- | -- |
| <span class="nowrap">`--access.config`</span> | string | Caller access configuration, a JSON envelope `{"path": "/path/to/access.json"}` (a bare file path is also accepted).  Enables [access management](/command-line-usage/access-management): callers are authenticated by JWT, static bearer token, mutual TLS or local OS identity and statements are authorised against policy rules.  Local token credentials are read from `STACKQL_CALLER_TOKEN`.  Available in StackQL releases from `v1.0`. | |
| <span class="nowrap">`--acid`</span> | string | JSON / YAML string representing ACID config | `{}` |
| <span class="nowrap">`--allow-persistent-secrets`</span> | flag | Allow a session to select a persistent OS secret store (macOS Keychain, Linux Secret Service or Windows Credential Manager) with `SET "$.secret_store"`.  Flat-file stores are refused even with the flag.  See [Provider Sessions and Secrets](/getting-started/provider-sessions#persistent-os-secret-stores).  Available in StackQL releases from `v1.0`. | `false` |
| <span class="nowrap">`--allow-secret-dereference`</span> | flag | Allow secret and credential values to be dereferenced in SQL expressions (`"$secrets.name"`, `"$auth.provider.field"`), with the consequence that they can enter results, logs and persisted query artefacts.  Not needed for normal provider authentication.  See [Secret values in SQL](/getting-started/provider-sessions#secret-values-in-sql).  Available in StackQL releases from `v1.0`. | `false` |
| <span class="nowrap">`--apirequesttimeout`</span> | integer | API request timeout in seconds, 0 for no timeout | `45` |
| <span class="nowrap">`--approot`</span> | string | Application config and cache root path | `{cwd}/.stackql` |
| <span class="nowrap">`--approotfilemode`</span> | uint32 | Application config and cache file mode | `493` |
| <span class="nowrap">`--auth`</span> | string | `auth` context keyvals in json form |  |
| <span class="nowrap">`--cachekeycount`</span> | integer | Cache initial key count | `100` |
| <span class="nowrap">`--configfile`</span> | string | Config file full path | `{cwd}/.stackqlrc` |
| <span class="nowrap">`--cpuprofile`</span> | string | `cpuprofile` file, none if empty | `null` |
| <span class="nowrap">`--dataflow.dependency.max`</span> | int | Maximum dataflow dependency depth for a given query | `50` |
| <span class="nowrap">`--dataflow.components.max`</span> | int | Maximum dataflow weakly connected components for a given query | `50` |
| <span class="nowrap">`--dbInternal`</span> | string | JSON / YAML string to configure DBMS housekeeping query handling | `{}` |
| <span class="nowrap">`-d`, `--delimiter`</span> | string | Delimiter for csv output. Single char only.  Ignored for all non-csv output | `,` |
| <span class="nowrap">`--dryrun`</span> | flag | `dryrun` flag; run preprocessor only, templated output will be returned | `false` |
| <span class="nowrap">`--env.file`</span> | string | Optional dotenv-style file sourced into the process environment at startup (all commands); the MCP [`reload_credentials`](/mcp/reload_credentials) tool re-sources it on demand. A missing file is tolerated; keys with empty values are skipped; existing variables are overwritten. Not sourced from `.stackqlrc`. Available in StackQL releases after `v0.10.542`. | `null` |
| <span class="nowrap">`--execution.concurrency.limit`</span> | int | Concurrency limit for query execution | `1` |
| <span class="nowrap">`--export.alias`</span> | string | Export alias prefix (namespace or schema) | |
| <span class="nowrap">`--gc`</span> | string | JSON / YAML string representing GC config | `{}` |
| <span class="nowrap">`-H`, `--hideheaders`</span> | flag | Disables column headers, valid only with `--output csv` or `--output text` | `false` |
| <span class="nowrap">`--http.log.enabled`</span> | flag | Display http request info in terminal | `false` |
| <span class="nowrap">`--http.proxy.host`</span> | string | Proxy host (leave blank for no proxy) | `null` |
| <span class="nowrap">`--http.proxy.password`</span> | string | Proxy password | `null` |
| <span class="nowrap">`--http.proxy.port`</span> | integer | Proxy port, any number less than or equal to 0 will result in the default port for a given scheme (e.g. `http -> 80`) | `-1` |
| <span class="nowrap">`--http.proxy.scheme`</span> | string | Proxy scheme, `http` or `https` | `http` |
| <span class="nowrap">`--http.proxy.user`</span> | string | Proxy user | `null` |
| <span class="nowrap">`--http.response.maxResults`</span> | integer | Maximum results per http request, any number less than or equal to 0 results in no limitation | `-1` |
| <span class="nowrap">`--http.response.pageLimit`</span> | integer | Maximum pages of results that will be returned per resource, any number less than or equal to results in no limitation | `20` |
| <span class="nowrap">`--indirect.depth.max`</span> | integer | Maximum depth for indirect queries: views and subqueries | `5` |
| <span class="nowrap">`-i`, `--infile`</span> | string | Input file (IQL file) from which queries are read | `{stdin}` |
| <span class="nowrap">`-q`, `--iqldata`</span> | string | Context (data) file for templating (`json` or `jsonnet` file)| |
| <span class="nowrap">`--loglevel`</span> | string | Log level, must be one of {`info`, `warn`, `debug`, `fatal`} | `fatal` |
| <span class="nowrap">`--mcp.config`</span> | string | JSON / YAML configuration object for the [MCP server](/command-line-usage/mcp#mcp-configuration-object) (`stackql mcp` and `stackql srv`) | `{}` |
| <span class="nowrap">`--mcp.log.format`</span> | string | MCP audit log encoding, `jsonl` or `otel` (OTLP/JSON log records); overrides `server.audit.format` in `--mcp.config`.  See [Log format](/command-line-usage/mcp#log-format).  Available in StackQL releases from `v0.11.660`. | `jsonl` |
| <span class="nowrap">`--mcp.protocol.version`</span> | string | Newest MCP protocol revision advertised, `auto`, `2026-07-28` (sessionless only) or an older revision such as `2025-11-25`; overrides `server.protocol_version` in `--mcp.config`.  See [Pinning the revision](/command-line-usage/mcp#pinning-the-revision).  Available in StackQL releases from `v0.12.732`. | `auto` |
| <span class="nowrap">`--mcp.server.type`</span> | string | MCP server type, one of {`http`, `stdio`, `reverse_proxy`}.  See [MCP server type](/command-line-usage/mcp#mcp-server-type). | `http` |
| <span class="nowrap">`--metadatattl`</span> | integer | TTL for cached metadata documents, in seconds | `3600` |
| <span class="nowrap">`--namespaces`</span> | string | JSON / YAML string representing namespaces for caching, views etc | `{}` |
| <span class="nowrap">`--offline`</span> | flag | Work offline, using cached data | `false` |
| <span class="nowrap">`-f`, `--outfile`</span> | string | Output file into which results are written | `{stdout}` |
| <span class="nowrap">`-o`, `--output`</span> | string | Output format, must be one of {`json`, `jsonl`, `table`, `csv`, `text`, `otel`} (`ndjson` is accepted as an alias for `jsonl`).  `otel` emits OpenTelemetry log records, see [Output Modes](/getting-started/output-modes#otel-output); available in StackQL releases from `v0.11.660`.  With an exporter configured in `--otel.config`, `otel` records are also pushed to that endpoint | `table` |
| <span class="nowrap">`--otel.config`</span> | string | JSON / YAML string configuring the `otel` output format; `exporter.endpoint`, `exporter.headers`, `exporter.timeout_ms` and `exporter.batch_size` push the records to an OTLP/HTTP logs endpoint, see [Pushing to an OTLP endpoint](/getting-started/output-modes#pushing-to-an-otlp-endpoint).  Available in StackQL releases from `v0.12.732` | `{}` |
| <span class="nowrap">`--preview`</span> | string | JSON string enabling preview functionality.  `{"unstable": true}` exposes the document-driven `stackql_unstable_<provider>.<service>.<resource>` relations, which are resolved and run by `omnisdk` straight from the provider documents with rows streamed as they are produced; the remaining keys (`batchSize`, `flushInterval`, `endpoint`, `insecureSkipTLSVerify`) tune that backend.  Preview functionality is not stable: behaviour, syntax and output may change or break between releases without notice, so do not rely on it in production.  From `v1.0` the same opt-in exposes the `stackql_preview.iac.run` and `stackql_preview.iac.destroy` relations, which apply and tear down a declared resource set through `omnisdk` with state kept under the application root.  See [Preview functionality](https://github.com/stackql/stackql/blob/main/docs/preview.md) | `{}` |
| <span class="nowrap">`--querycachesize`</span> | integer | Size in number of entries of LRU cache for query plans | `10000` |
| <span class="nowrap">`--registry`</span> | string | JSON / YAML string configuring the provider registry location and provider document signature verification | see [`--registry`](#--registry) |
| <span class="nowrap">`--session`</span> | string | JSON / YAML string representing session config | `{}` |
| <span class="nowrap">`--sqlBackend`</span> | string | JSON / YAML string representing SQL Backend System Config | see [`--sqlBackend`](#--sqlbackend) |
| <span class="nowrap">`--store.txn`</span> | string | JSON / YAML string representing Txn store config | `{}` |
| <span class="nowrap">`--tls.CABundle`</span> | string | Path to CA bundle, if not specified then system defaults used |  |
| <span class="nowrap">`--tls.allowInsecure`</span> | flag | Allow trust of insecure certificates (not recommended) |  |
| <span class="nowrap">`--var`</span> | string | External variables provided as key/value pairs for sourcing into StackQL queries through `jsonnet` data files |  |
| <span class="nowrap">`-v`, `--verbose`</span> | flag | Verbose output | `false` |
| <span class="nowrap">`-h`, `--help`</span> | | Context specific help for stackql | |
| <span class="nowrap">`--version`</span> | | Displays the version of the StackQL program | |

## `--registry` {#--registry}

The `--registry` flag configures where provider documents are read from and whether their signatures are verified.  The value is a JSON string with the following keys:

| Key | Description | Default |
| -- | -- | -- |
| `url` | Registry URL.  The public StackQL provider registry is used when omitted. | public registry |
| `localDocRoot` | A local directory holding provider documents, read in place of downloading from the registry.  Used when [developing a provider](/developers/developing-stackql-providers#testing-a-provider-locally). | |
| `srcPrefix` | Path prefix under the registry root where provider source documents live. | `src` |
| `verifyConfig.nopVerify` | `true` skips signature verification.  Set to `false` to require an embedded ML-DSA-65 (FIPS 204) signature on every provider and service document read, including the document-backed preview relations. | `true` |
| `verifyConfig.publicKeys` | A JSON object mapping key IDs to hex-encoded ML-DSA-65 public keys, for private registries.  It replaces the embedded production keys; an explicit empty object is an error. | embedded keys |

### Example: Require signed provider documents {#example-require-signed-provider-documents}

Turns verification on for every provider and service document read from the public registry, checked against the embedded production keys:

```bash
stackql --registry '{"verifyConfig": {"nopVerify": false}}' shell
```

### Example: Read unsigned documents from a local directory {#example-read-unsigned-documents-from-a-local-directory}

Points the registry at a directory of provider documents under development.  Those documents are unsigned, so verification stays off:

```bash
stackql --registry '{"localDocRoot": "/path/to/provider-dev/openapi", "verifyConfig": {"nopVerify": true}}' shell
```

:::note

Signature verification is available in StackQL releases from `v1.0` and is off by default.  Signatures cover the exact YAML bytes of a document, so any edit, including a formatting or newline change, invalidates them; re-sign with the registry's `pqsig sign-doc` tool.  Unsigned local development documents must keep `nopVerify: true`.  The former certificate options and `.sig` sidecar files are no longer used, and archives published with sidecars only must be republished before verified use.  Verification authenticates documents when they are read; archive download and extraction are not covered.

:::

## `--sqlBackend` {#--sqlbackend}

The `--sqlBackend` flag configures an external SQL backend for StackQL, allowing you to use an external database engine (such as PostgreSQL) instead of the default embedded SQL engine. This is useful for leveraging an external database for query processing, persistent storage, or integration with existing database infrastructure.

The value is a JSON string with the following keys:

| Key | Description | Example Values |
| -- | -- | -- |
| `dbEngine` | The database engine type | `postgres_tcp` |
| `sqlDialect` | The SQL dialect to use | `postgres` |
| `dsn` | The data source name (connection string) for the database | See examples below |

### Example: Using a PostgreSQL Backend {#example-using-a-postgresql-backend}

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user:password@hostname:port/dbname?sslmode=require"}' \
  shell
```

### Example: Using Databricks PostgreSQL Interface {#example-using-databricks-postgresql-interface}

Databricks exposes a PostgreSQL-compatible interface that can be used as a StackQL SQL backend. The DSN uses the standard PostgreSQL connection string format with your Databricks credentials:

```bash
./stackql \
  --sqlBackend='{"dbEngine": "postgres_tcp", "sqlDialect": "postgres", "dsn": "postgres://user%40domain.com:your-access-token@your-databricks-host.cloud.databricks.com/databricks_postgres?sslmode=require"}' \
  shell
```

<br/>

:::note
Special characters in the username or password (such as `@`) must be URL-encoded in the DSN (e.g., `@` becomes `%40`).
:::
