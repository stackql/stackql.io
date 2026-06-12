---
title: mcp
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL via MCP
image: "/img/stackql-featured-image.png"
---

Command used to launch StackQL as a Model Context Protocol (MCP) server, enabling AI agents and assistants to interact with cloud infrastructure using StackQL's query capabilities.

:::info[What is MCP?]

The [Model Context Protocol (MCP)](https://modelcontextprotocol.io/) is an open protocol that standardizes how AI applications interact with external data sources and tools.  By running StackQL as an MCP server, you can enable AI agents (Claude, Cursor, Continue, custom agents, etc) to query and manage cloud infrastructure across multiple providers using natural language.

:::

* * *

### Syntax

`stackql mcp [flags]` for a standalone MCP server.

`stackql srv [flags]` to run the MCP server alongside the PostgreSQL wire-protocol server in the same process.

* * *

### Deployment modes

StackQL's MCP server can be deployed in three different configurations to suit various architectural requirements.

#### 1. Standalone MCP server

Run StackQL as a dedicated MCP server on a specified port.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912"}}'
```

**Use case:** when you only need MCP protocol access and don't require PostgreSQL wire protocol compatibility.

#### 2. MCP + PostgreSQL server (in-memory)

Run both MCP and PostgreSQL servers simultaneously with in-memory communication between them.

```bash
stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912"}}' \
  --pgsrv.port 5665
```

**Use case:** when you need both MCP protocol access for AI agents and PostgreSQL wire protocol for traditional database clients, with maximum performance through in-memory communication.

#### 3. MCP + PostgreSQL server (reverse proxy)

Run both servers with TCP-based communication, supporting distributed deployments and TLS encryption.

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9004"}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

**With TLS encryption:**

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"tls_cert_file": "/path/to/server_cert.pem", "tls_key_file": "/path/to/server_key.pem", "transport": "http", "address": "127.0.0.1:9004"}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

**Use case:** when you need to separate MCP and PostgreSQL workloads across different processes or hosts, or when you require TLS encryption for the MCP endpoint.

* * *

### Configuration options

#### MCP server type

| Type | Description |
|--|--|
|`http`|Direct HTTP server mode -- MCP requests are handled in-process by the StackQL engine.|
|`reverse_proxy`|Reverse-proxy mode -- MCP requests are dispatched to a backing PostgreSQL server via the configured DSN.  Used with `stackql srv`.|
|`stdio`|Standard input/output transport -- the server reads requests from `stdin` and writes responses to `stdout`.  Used by editor-embedded MCP clients (Claude Desktop, Cursor, etc).|

#### MCP configuration object

The `--mcp.config` flag accepts a JSON object with the following structure.

##### Server configuration

| Field | Description | Required |
|--|--|--|
|`server.transport`|Transport protocol -- `http` or `stdio`.|Yes|
|`server.address`|Address and port to bind the MCP server (e.g., `127.0.0.1:9912`).  HTTP transport only.|For `http`|
|`server.tls_cert_file`|Path to TLS certificate file for HTTPS.|No|
|`server.tls_key_file`|Path to TLS private key file for HTTPS.|No|
|`server.mode`|Safety contract that gates mutation and lifecycle operations.  One of `read_only`, `safe` (default), `delete_safe`, `full_access`.  See [Server modes](#server-modes).|No|
|`server.read_only`|**Legacy** boolean alias for `mode: read_only`.  When both fields are set, `mode` wins.|No|
|`server.audit`|Audit subsystem configuration.  See [Audit log](#audit-log).|No|

##### Backend configuration (reverse-proxy mode only)

| Field | Description | Required |
|--|--|--|
|`backend.dsn`|PostgreSQL connection string for the backend StackQL server.|Yes (for `reverse_proxy`)|

<br />

:::info

The backend DSN should include the `default_query_exec_mode=simple_protocol` parameter for optimal compatibility.

:::

##### Allowlists

| Field | Description |
|--|--|
|`enabled_tools`|Array of tool names that the server is permitted to publish.  When omitted or empty, every built-in tool is registered.  Used to expose a narrow subset (e.g., a read-only inventory server).|
|`enabled_prompts`|Array of prompt names.  Same semantics as `enabled_tools` but for the prompt surface.|

<br/>

Example -- a server that publishes only `server_info` and `list_providers`:

```json
{
  "server": {"transport": "http", "address": "127.0.0.1:9912"},
  "enabled_tools": ["server_info", "list_providers"]
}
```

* * *

### Server modes

`server.mode` chooses one of four safety contracts.  All four allow `SELECT` and metadata reads; they differ in how they handle mutations and lifecycle operations.

| Mode | `SELECT` / metadata | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` | `EXEC` (lifecycle) |
|--|--|--|--|--|
|`read_only`|allow|refuse|refuse|refuse|
|`safe` (default)|allow|needs approval|needs approval|needs approval|
|`delete_safe`|allow|allow|needs approval|needs approval|
|`full_access`|allow|allow|allow|allow|

<br />

**Refuse** returns an error immediately to the client.

**Needs approval** uses the MCP elicitation flow:

- If the client advertised the elicitation capability at initialise, the server sends an `elicitation/create` request with a short message describing the action (tool name, query class, SQL).  The user accepts, declines, or cancels.
- If the client did **not** advertise elicitation, the tool is refused with a message that explains the gap and points the operator at `full_access` mode.

The mode is global per server.  There is no per-tool override.

#### Default-mode behaviour change

Prior releases enforced safety only through a single `read_only: true/false` flag whose default was "no enforcement".  The current default (`mode: safe`) makes mutations require user approval out of the box.  Operators running an elicitation-capable client (Claude Desktop, Cursor, Continue, etc) will see one approval prompt per mutation.  Operators running automation, or the bundled `stackql_mcp_client` (which does not advertise elicitation), must explicitly opt into `mode: full_access` to run mutations and lifecycle operations.

The legacy `read_only: true` JSON / YAML key is still accepted for back-compat and is treated as equivalent to `mode: read_only`.

#### Examples

```bash
# Read-only: SELECTs proceed; mutations and lifecycle refused immediately.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "mode": "read_only"}}'

# Delete-safe: INSERT/UPDATE proceed; DELETE and EXEC need approval.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "mode": "delete_safe"}}'

# Full access: everything proceeds without prompting.  Use only with trusted
# clients and operators.
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "mode": "full_access"}}'
```

* * *

### Audit log

Every tool call writes one JSONL record to the configured audit sink.  Audit is **on by default**.  The audit answers "what did the agent do," not "what did the agent see" -- result rows from `SELECT` statements are intentionally not recorded.

#### What gets recorded

| Field | Description |
|--|--|
|`timestamp`|Start-of-call wall clock (RFC3339).|
|`tool`|Tool name (e.g., `run_select_query`).|
|`mode`|Server mode in effect at call time.|
|`decision`|`allow` / `refuse_immediate` / `needs_approval_accepted` / `needs_approval_declined` / `needs_approval_cancelled` / `needs_approval_unavailable`.|
|`query_class`|`select` / `mutation_create` / `mutation_delete` / `lifecycle` / `unknown`.|
|`sql`|SQL string for query tools (`run_select_query`, `run_mutation_query`, `run_lifecycle_operation`, `validate_select_query`).|
|`args`|Hierarchy fields for metadata tools (`list_*`, `describe_*`); SQL + `row_limit` for query tools.|
|`duration_ms`|Wall-clock duration of the gate + handler.|
|`error`|Error message if the tool errored or was refused.|

<br />

Result rows from `SELECT` statements are deliberately excluded -- they can be very large and may carry sensitive data.

#### File sink

The only sink shipped today is `file`.  One JSON object per line, fsynced after each record, lumberjack-style rotation by size / age / backup count.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "audit": {
        "file": {
          "path": "/var/log/stackql-mcp.log",
          "max_size_mb": 100,
          "max_backups": 5,
          "max_age_days": 30
        }
      }
    }
  }'
```

Two ways to specify the location:

- `audit.file.path` -- a complete file path (absolute, or relative to cwd).
- `audit.file.dir` -- a directory; the sink picks the basename `stackql_mcp_server_<RFC3339-utc-second>.log` inside it.

When neither is set in `mcp.config`, the MCP server defaults `dir` to cwd so existing operators see the same behaviour as before.  The underlying generic sink package itself refuses to silently pick a directory -- the "where do logs land" decision is always made by the caller.

The resolved absolute path is logged to stderr at startup as `sink file: /path/to/file.log`.

#### Failure modes

When the sink returns an error, the response behaviour depends on `audit.failure_mode`.

| failure_mode | Effect |
|--|--|
|`strict` (default)|The tool call returns the audit error to the client even if the underlying tool succeeded.  Intentional: better an ambiguous client response than an undetected `DELETE`.|
|`strict_mutations`|`SELECT` / metadata reads proceed with a stderr note; mutations and lifecycle ops surface the audit error.|
|`best_effort`|Always log to stderr and proceed.|

#### Sequencing

The audit write happens **after** the tool executes (or is gated out) but **before** the response returns to the client.  In strict mode, an audit-write failure on a successful DELETE means the row is gone but the client receives an error -- by design, so no mutation slips through unaudited.

#### Disabling audit

To turn audit off entirely (the pre-PR2 behaviour):

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "audit": {"disabled": true}}}'
```

* * *

### Available MCP tools

When running as an MCP server, StackQL exposes the following tools.  Each returns both a rendered text view (for the LLM) and a typed structured payload (for programmatic clients).  Rendering is fixed per tool: a markdown table for uniform multi-row results, a markdown KV block for sparse / single-record results.

Click any tool name for a full reference page, including inputs, gating behaviour, and an example prompt.

| Tool | Renderer | Description | Inputs |
|--|--|--|--|
|[`server_info`](/docs/mcp/server_info)|KV|Server identity and runtime: stackql version, backing SQL engine, provider registry location, mode, read-only flag.  Call once at session start.|none|
|[`list_providers`](/docs/mcp/list_providers)|Table|Providers already pulled into the local cache -- top of the hierarchy.|none|
|[`list_services`](/docs/mcp/list_services)|Table|Services under a provider.|`provider`|
|[`list_resources`](/docs/mcp/list_resources)|Table|Resources under a `provider`.`service`.|`provider`, `service`|
|[`list_methods`](/docs/mcp/list_methods)|Table|Access methods (HTTP operations) for a resource.  Call before writing any query -- this is where required `WHERE` parameters are inferred.|`provider`, `service`, `resource`|
|[`describe_resource`](/docs/mcp/describe_resource)|KV|Output fields for a resource's primary read method.|`provider`, `service`, `resource`|
|[`describe_method`](/docs/mcp/describe_method)|KV|Full I/O contract for one method (always EXTENDED).|`provider`, `service`, `resource`, `method`|
|[`validate_select_query`](/docs/mcp/validate_select_query)|KV|Parse and plan a `SELECT` without executing.  Returns `{valid, errors}`.  `SELECT` only.|`sql`|
|[`run_select_query`](/docs/mcp/run_select_query)|Table|Execute a `SELECT`.  Returns `{rows}`.  Reads only.|`sql`, `row_limit?`|
|[`run_mutation_query`](/docs/mcp/run_mutation_query)|KV|Execute `INSERT`/`UPDATE`/`REPLACE`/`DELETE` against the provider.  **Real side effects.** Returns `{messages, timestamp}`.  Gated by the server [mode](#server-modes).|`sql`|
|[`run_lifecycle_operation`](/docs/mcp/run_lifecycle_operation)|KV|Execute a stackql `EXEC` lifecycle operation.  Returns `{messages, timestamp}`.  Gated by the server [mode](#server-modes).|`sql`|
|[`list_registry`](/docs/mcp/list_registry)|Table|Providers (and their versions) available in the configured registry.  Distinct from `list_providers`, which lists only providers already pulled.|`provider?`|
|[`pull_provider`](/docs/mcp/pull_provider)|KV|Install a single provider from the registry into the local cache.  Local cache state only -- no cloud control or data plane effect.|`provider`, `version?`|

### Available MCP prompts

One static prompt is published.

| Prompt | Description |
|--|--|
|`write_safe_select`|Guidance for writing safe `SELECT` queries against stackql resources.  Explains how to use `SHOW METHODS IN <provider>.<service>.<resource>` to discover the right read method and the required `WHERE` parameters.|

* * *

### Flags

| Flag | Description |
|--|--|
|`--mcp.server.type`|MCP server type: `http`, `stdio`, or `reverse_proxy` (the latter is used with `stackql srv`).|
|`--mcp.config`|JSON configuration object for the MCP server.  YAML is also accepted.|
|`--pgsrv.port`|TCP port for the PostgreSQL wire-protocol server (used with `stackql srv`).|
|`-H`, `--help`|Print help information.|
|`-v`, `--verbose`|Run in verbose mode with additional output.|

&nbsp;
&nbsp;
> see [Global Flags](/docs/command-line-usage/global-flags) for additional options

:::info

You need to set environment variables required for provider authentication before starting the MCP server.  See [Using a Provider](/docs/getting-started/using-a-provider) for more information.

:::

* * *

### Examples

#### Basic standalone MCP server

Launch a standalone MCP server with provider authentication:

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912"}}' \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### MCP server with PostgreSQL server (in-memory)

Run both MCP and PostgreSQL servers for dual-protocol access:

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)

stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912"}}' \
  --pgsrv.port 5665 \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Read-only inventory server with audit to a known path

Useful for an inventory-scanning agent that should never mutate:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "mode": "read_only",
      "audit": {"file": {"path": "/var/log/stackql-mcp-audit.log"}}
    }
  }' \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Full-access automation server

For trusted automation pipelines that need mutations and can't respond to elicitation prompts:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{
    "server": {
      "transport": "http",
      "address": "127.0.0.1:9912",
      "mode": "full_access"
    }
  }' \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### Secure MCP server with TLS

Launch an MCP server with TLS encryption in reverse-proxy mode:

First, generate TLS certificates (self-signed for development):

```bash
openssl req -x509 -newkey rsa:4096 -keyout server_key.pem -out server_cert.pem -days 365 -nodes
```

Then start the server:

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{
    "server": {
      "tls_cert_file": "server_cert.pem",
      "tls_key_file": "server_key.pem",
      "transport": "http",
      "address": "127.0.0.1:9004"
    },
    "backend": {
      "dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"
    }
  }' \
  --pgsrv.port 5446 \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

* * *

### Testing your MCP server

The repository ships a development MCP client (`stackql_mcp_client`) for scripting and regression tests.  Build it with:

```bash
python cicd/python/build.py --build-mcp-client
```

This produces `./build/stackql_mcp_client`.

Note that `stackql_mcp_client` does **not** advertise the MCP elicitation capability -- it's for non-interactive scripting.  Against a `safe` or `delete_safe` server it will receive the "client does not support elicitation" refusal on mutation and lifecycle calls.  For interactive testing with elicitation prompts, use an editor-embedded MCP client (Claude Desktop, Cursor, Continue).

```bash
# List all available tools
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912

# Server identity, mode, audit / registry context
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action server_info

# List providers already pulled into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_providers

# List providers (and versions) available in the registry
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_registry

# Install a provider from the registry into the local cache
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action pull_provider \
  --exec.args '{"provider": "kafka"}'

# List services for a provider
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_services \
  --exec.args '{"provider": "google"}'

# List access methods for a resource (this is how an agent finds required
# WHERE parameters before writing a query)
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_methods \
  --exec.args '{"provider": "google", "service": "compute", "resource": "networks"}'

# Validate a SELECT without executing
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action validate_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Execute a SELECT
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_select_query \
  --exec.args '{"sql": "select name from google.compute.networks where project = '"'"'my-project'"'"';"}'

# Mutation - succeeds only against a full_access server, or against
# delete_safe (for INSERT/UPDATE) or safe with an elicitation-capable client.
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action run_mutation_query \
  --exec.args '{"sql": "delete from google.compute.firewalls where project = '"'"'my-project'"'"' and firewall = '"'"'old-rule'"'"';"}'
```

#### Example responses

`server_info` (against a default-mode server):

```json
{
  "version": "0.10.444",
  "commit": "abc1234",
  "build_date": "2026-05-16T10:47:33Z",
  "platform": "linux/amd64",
  "transport": "http",
  "sql_backend": "sqlite3",
  "provider_registry": "https://registry.stackql.io/providers",
  "mode": "safe",
  "is_read_only": false
}
```

`run_select_query`:

```json
{
  "rows": [
    {"name": "default"},
    {"name": "pathfinders-test-01"},
    {"name": "returning-test-03"}
  ]
}
```

`run_mutation_query` (against a `full_access` server):

```json
{
  "messages": ["The operation was despatched successfully"],
  "timestamp": "2026-05-16T10:47:33+10:00 AEST"
}
```

Sample audit log line for the same mutation:

```json
{"timestamp":"2026-05-16T00:47:33Z","tool":"run_mutation_query","mode":"full_access","decision":"allow","query_class":"mutation_delete","sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","args":{"sql":"delete from google.compute.firewalls where project = 'my-project' and firewall = 'old-rule';","row_limit":0},"duration_ms":42}
```

* * *

### Integration with AI assistants

To integrate StackQL's MCP server with an AI assistant, register `stackql` as an MCP server in the assistant's configuration.  Most editor-embedded MCP clients run the server over `stdio`; for those, use `--mcp.server.type=stdio` and the assistant launches the process directly.  Standalone agents that speak HTTP can connect to a long-running `stackql mcp --mcp.server.type=http` process.

For Claude Desktop - including the prebuilt one-click MCP Bundle (`.mcpb`) and manual configuration - see [Using StackQL with Claude Desktop](/docs/getting-started/claude-desktop).

:::note

Configuration file locations vary by operating system and AI assistant.  Consult your AI assistant's documentation for the correct path and format.

:::

* * *

### Architecture considerations

When choosing a deployment mode, consider:

1. **Standalone (`stackql mcp`)**: simplest setup, ideal for development and for editor-embedded clients over `stdio`.
2. **In-memory dual-server (`stackql srv` with `mcp.server.type=http`)**: single process serves both MCP and PostgreSQL wire protocols.  Best performance, suitable for most production deployments.
3. **Reverse proxy (`stackql srv` with `mcp.server.type=reverse_proxy`)**:
   - enables workload separation across processes or hosts
   - supports TLS encryption for secure MCP endpoints
   - allows independent scaling of MCP and PostgreSQL interfaces
   - provides flexibility for enterprise CA integration (note: enterprise CA support is experimental)

And separately, on safety:

- Start with the default `mode: safe`.  Elicitation-capable clients (Claude Desktop, Cursor, Continue) will prompt the user before any mutation.
- Pin `mode: read_only` for inventory or analytics agents that should never write.
- Pin `mode: delete_safe` when you want create/update freedom but want a human in the loop for destructive operations.
- Reserve `mode: full_access` for trusted automation pipelines, and only in conjunction with an audit log that someone reviews.

:::caution

When using TLS with enterprise Certificate Authorities (CAs), additional configuration may be required.  This functionality is experimental and may require adjustments based on your specific CA implementation.

:::

* * *

### Further reading

- The package-developer reference is the [`pkg/mcp_server` README](https://github.com/stackql/stackql/blob/main/pkg/mcp_server/README.md) in the stackql repo.
- The contributor-facing technical doc lives at [`docs/mcp.md`](https://github.com/stackql/stackql/blob/main/docs/mcp.md) in the stackql repo and includes worked example responses for every tool.
