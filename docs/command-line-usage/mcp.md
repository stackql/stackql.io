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

#### MCP log format

The `--mcp.log.format` flag selects the audit log encoding: `jsonl` (default) or `otel` (OpenTelemetry OTLP/JSON log records).  It overrides `server.audit.format` in the configuration object.  See [Log format](#log-format).

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
|`server.stateless`|Serve Streamable HTTP without sessions (no `Mcp-Session-Id`), which is how protocol revision `2026-07-28` is served over HTTP.  Default `false`.  Ignored for `stdio`.  See [Protocol revision support](#protocol-revision-support).|No|
|`server.audit`|Audit subsystem configuration, including `audit.format` (`jsonl` or `otel`).  See [Audit log](#audit-log).|No|

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

### Protocol revision support

Protocol revision `2026-07-28`, `server.stateless` and the `otel` audit log format are available in StackQL releases from `v0.11.660`.

The server speaks every revision of the Model Context Protocol supported by the [Go MCP SDK](https://github.com/modelcontextprotocol/go-sdk) and negotiates per client, so a fleet of mixed clients works against one server.

| Revision | Lifecycle | Approval prompt (`safe` / `delete_safe`) |
|--|--|--|
|`2026-07-28` (advertised)|No handshake: `server/discover`, then every request carries `io.modelcontextprotocol/protocolVersion` and `io.modelcontextprotocol/clientCapabilities` in `_meta` (plus the `Mcp-Protocol-Version`, `Mcp-Method` and `Mcp-Name` headers on HTTP).|`input_required` result, retried by the client with `inputResponses` (SEP-2322).|
|`2025-11-25`, `2025-06-18`|`initialize` / `notifications/initialized` handshake.|Server-initiated `elicitation/create` request.|
|`2025-03-26`, `2024-11-05`|As above.|As above.|

<br />

**stdio** serves every revision on one process: a `2026-07-28` client's first request is served without a handshake and an older client's `initialize` still works.  Nothing needs configuring for Claude Desktop, the npm / PyPI launchers or the Docker image.

**Streamable HTTP** defaults to the stateful, session-per-client model (`Mcp-Session-Id`), which the SDK serves for revisions up to `2025-11-25`.  A `2026-07-28` client learns that from `server/discover` and negotiates down, so existing HTTP integrations keep their sessions and their approval prompts unchanged.  Set `server.stateless` to serve `2026-07-28` natively over HTTP:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "stateless": true}}'
```

A sessionless server issues no `Mcp-Session-Id`, keeps `tools/list`, `prompts/list` and `resources/list` connection-invariant, and runs the approval round trip through `input_required`.  It still accepts an older client's `initialize` and serves reads to it, but it cannot retain the elicitation capability that client declared at initialise (each request gets an ephemeral session), so older clients cannot approve gated writes on a sessionless server.  Choose `stateless` for current-revision hosts and the default for a fleet that still includes older clients.

The server holds no cross-call state: mode, audit and provider credentials are process-level configuration, so nothing needs to move behind explicit handles.

* * *

### Credential (re)sourcing (`--env.file` + `reload_credentials`)

:::note

`--env.file` and the `reload_credentials` tool are available in StackQL releases after `v0.10.542`.

:::

A process environment block is fixed at spawn.  This creates two problems for MCP servers:

- An MCP `stdio` server spawned by a desktop client (Claude Desktop, Cursor, etc) without the credential environment variables can never see them - the client passes its own environment to the subprocess.
- Rotated short-lived credentials never reach a long-running server session.

The `--env.file` flag bridges both gaps by nominating a dotenv-style file that acts as a mutable credential store:

```bash
stackql mcp --mcp.server.type=stdio --env.file /path/to/stackql-credentials.env
```

#### Sourcing semantics

The file is sourced into the process environment at startup for every entrypoint (`exec`, `shell`, `srv`, `registry`, `mcp`):

- When the flag is empty or unset, nothing happens - credentials resolve lazily from the process environment at request time, exactly the historical behaviour.
- A missing file at the given path is a tolerated no-op (the file may be created later).
- A malformed or unreadable file is fatal at startup.
- Only keys with non-empty values are set; existing process environment variables **are** overwritten (the file is the source of truth); nothing is ever unset.
- The flag is command line only - it is not sourced from `.stackqlrc`.

The file format is one `KEY=VALUE` per line.  `#` comments, blank lines, an optional `export ` prefix, optional single or double quotes around the value, and CRLF line endings are all tolerated:

```bash
# stackql-credentials.env
export OKTA_SECRET_KEY="00abc...xyz"
STACKQL_GITHUB_USERNAME=myuser
STACKQL_GITHUB_PASSWORD='ghp_mytoken'
```

#### `reload_credentials`

The [`reload_credentials`](/docs/mcp/reload_credentials) tool re-sources the `--env.file` file into the process environment mid-session, then reports per-provider credential resolution status.  Secret values are never returned, logged, or audited - names and statuses only.  Without `--env.file` configured the tool degrades to a pure status probe.

When a query fails on credential resolution, the MCP error carries a hint directing the agent to call `reload_credentials` and retry, so agentic clients self-heal: write the credential to the file, ask the agent to reload, and the failed query succeeds on retry - no server restart required.

`reload_credentials` is not supported in `reverse_proxy` backend mode - queries execute in the remote stackql server process, so the tool returns an error directing the user to reload on the backing server.

#### Windows and Claude Desktop

`setx` writes the registry, not running processes, and Claude Desktop passes its own (stale) environment to MCP subprocesses - so credential changes normally require a full Claude Desktop restart.  With `--env.file` the credentials file can be created or rotated at any time and `reload_credentials` picks it up without any restart.  See [Using StackQL with Claude Desktop](/docs/getting-started/claude-desktop) for a worked configuration.

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

**Needs approval** uses the MCP elicitation flow, shaped by the negotiated [protocol revision](#protocol-revision-support):

- If the client advertised the elicitation capability (at initialise, or in the per-request `_meta` client capabilities on `2026-07-28`), the server asks the user to approve the action with a short message (tool name, query class, SQL).  On `2026-07-28` the prompt is returned as an `input_required` result carrying an `elicitation/create` input request under the id `stackql_approval`, and the client retries the call with the answer in `inputResponses`; on earlier revisions the server sends the `elicitation/create` request itself mid-call.  The user accepts, declines, or cancels.
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

Every tool call writes one record to the configured audit sink, as JSONL by default or as OpenTelemetry log records (see [Log format](#log-format)).  Audit is **on by default**.  The audit answers "what did the agent do," not "what did the agent see" -- result rows from `SELECT` statements are intentionally not recorded, in either format.

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

#### Log format

The audit stream has two encodings, written to the same file sink:

```bash
stackql mcp --mcp.server.type=stdio --mcp.log.format=jsonl   # default, unchanged
stackql mcp --mcp.server.type=stdio --mcp.log.format=otel    # OTLP/JSON log records
```

The flag overrides `server.audit.format` in `mcp.config` (`"audit": {"format": "otel"}`).  `jsonl` is byte-compatible with the records described above.  `otel` writes one OTLP/JSON `LogsData` object per line, the shape the OpenTelemetry Collector's `otlp_json_file` receiver ingests as-is (verified against otelcol-contrib 0.160.0), so the stream reaches any OTLP pipeline with no transform processor:

```yaml
receivers:
  otlp_json_file:
    include: [/var/log/stackql-mcp.log]
exporters:
  debug: {}
service:
  pipelines:
    logs:
      receivers: [otlp_json_file]
      exporters: [debug]
```

Each tool call produces one log record; a call that went through the approval gate produces a second record for the elicitation decision.  Records of one call share `gen_ai.tool.call.id` and a trace id.  A W3C `traceparent` supplied by the caller in `params._meta` is honoured; otherwise one trace id is generated per MCP session so an agent session's records correlate.

The emitted attribute set is a versioned interface: the instrumentation scope is `github.com/stackql/stackql/pkg/mcp_server/audit` at version `1.0.0`, and `schemaUrl` pins the stable conventions to `https://opentelemetry.io/schemas/1.44.0`.  The `gen_ai.*` and `mcp.*` attributes follow the [GenAI](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/gen-ai-spans.md#execute-tool-span) and [MCP](https://github.com/open-telemetry/semantic-conventions-genai/blob/main/docs/gen-ai/mcp.md) semantic conventions, which are Development status; the scope version is bumped when the set changes.

| Attribute | Record | Value |
|--|--|--|
|`service.name`, `service.version`|resource|`stackql`, the StackQL version|
|`gen_ai.operation.name`|tool call|`execute_tool`|
|`mcp.method.name`|both|`tools/call` on the tool call record, `elicitation/create` on the decision record|
|`gen_ai.tool.name`|both|Tool name|
|`gen_ai.tool.call.id`|both|Generated per call, shared by both records|
|`mcp.protocol.version`|both|Negotiated revision|
|`mcp.session.id`|both|Session id (absent for sessionless HTTP requests)|
|`stackql.mode`|both|Server mode|
|`stackql.decision`|both|The `decision` value from the table above|
|`stackql.query`|tool call|Verbatim SQL, for query tools|
|`stackql.query_class`|tool call|The `query_class` value|
|`stackql.provider`|tool call|Provider, for metadata tools|
|`stackql.query.source`|tool call|Query library entry id, when supplied|
|`stackql.duration_ms`|tool call|Wall-clock duration|
|`stackql.rows_returned`|tool call|Row count, for row-returning tools|
|`error.type`, `error.message`|tool call|On failure: the refusing decision, or `tool_error` past the gate|

<br />

Body is `execute_tool <tool>` / `elicitation <tool>`; severity is `INFO`, or `ERROR` when the call failed or was refused.  The redaction contract is identical in both formats: result values are never serialised, only the statement that produced them.

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
|[`reload_credentials`](/docs/mcp/reload_credentials)|Table|Re-source credentials from the `--env.file` dotenv file into the process environment and report per-provider resolution status (`ok`, `unresolved`, `not_checked`).  Never returns secret values.  Allowed in every mode.|`provider?`|

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
|`--env.file`|Dotenv-style credentials file sourced at startup and re-sourced on demand by the `reload_credentials` tool.  See [Credential (re)sourcing](#credential-resourcing---envfile--reload_credentials).|
|`--pgsrv.port`|TCP port for the PostgreSQL wire-protocol server (used with `stackql srv`).|
|`-H`, `--help`|Print help information.|
|`-v`, `--verbose`|Run in verbose mode with additional output.|

&nbsp;
&nbsp;
> see [Global Flags](/docs/command-line-usage/global-flags) for additional options

:::info

You need to set environment variables required for provider authentication before starting the MCP server, or nominate a dotenv-style credentials file with [`--env.file`](#credential-resourcing---envfile--reload_credentials).  See [Using a Provider](/docs/getting-started/using-a-provider) for more information.

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

# Re-source the --env.file credentials file and report per-provider
# credential status - a good first diagnostic when queries fail on auth
./build/stackql_mcp_client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action reload_credentials

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

For Claude Desktop - including the Anthropic Connector Directory listing (the recommended installation method), the downloadable MCP Bundle (`.mcpb`), and manual configuration - see [Using StackQL with Claude Desktop](/docs/getting-started/claude-desktop).

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
