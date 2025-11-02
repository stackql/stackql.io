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

* * *

## What is MCP?

The [Model Context Protocol (MCP)](https://modelcontextprotocol.io/) is an open protocol that standardizes how AI applications interact with external data sources and tools. By running StackQL as an MCP server, you can enable AI agents (like Claude, ChatGPT, and others) to query and manage cloud infrastructure across multiple providers using natural language.

* * *

### Syntax

`stackql mcp [flags]`

* * *

### Deployment Modes

StackQL's MCP server can be deployed in three different configurations to suit various architectural requirements:

#### 1. Standalone MCP Server

Run StackQL as a dedicated MCP server on a specified port.

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912"}}'
```

**Use case:** When you only need MCP protocol access and don't require PostgreSQL wire protocol compatibility.

#### 2. MCP + PostgreSQL Server (In-Memory)

Run both MCP and PostgreSQL servers simultaneously with in-memory communication between them.

```bash
stackql srv \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912"}}' \
  --pgsrv.port 5665
```

**Use case:** When you need both MCP protocol access for AI agents and PostgreSQL wire protocol for traditional database clients, with maximum performance through in-memory communication.

#### 3. MCP + PostgreSQL Server (Reverse Proxy)

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
  --mcp.config '{"server": {"tls_cert_file": "/path/to/mtls/credentials/pg_server_cert.pem", "tls_key_file": "/path/to/mtls/credentials/pg_server_key.pem", "transport": "http", "address": "127.0.0.1:9004"}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446
```

**Use case:** When you need to separate MCP and PostgreSQL workloads across different processes or hosts, or when you require TLS encryption for the MCP endpoint.

* * *

### Configuration Options

#### MCP Server Type

| Type | Description |
|--|--|
|`http`|Direct HTTP server mode - MCP requests are handled directly by StackQL|
|`reverse_proxy`|Reverse proxy mode - MCP requests are forwarded to a PostgreSQL backend via DSN|

#### MCP Configuration Object

The `--mcp.config` flag accepts a JSON object with the following structure:

##### Server Configuration

| Field | Description | Required |
|--|--|--|
|`server.transport`|Transport protocol (currently only `http` is supported)|Yes|
|`server.address`|Address and port to bind the MCP server (e.g., `127.0.0.1:9912`)|Yes|
|`server.tls_cert_file`|Path to TLS certificate file for HTTPS|No|
|`server.tls_key_file`|Path to TLS private key file for HTTPS|No|

##### Backend Configuration (Reverse Proxy Mode Only)

| Field | Description | Required |
|--|--|--|
|`backend.dsn`|PostgreSQL connection string for the backend StackQL server|Yes (for reverse_proxy)|

:::info

The backend DSN should include the `default_query_exec_mode=simple_protocol` parameter for optimal compatibility.

:::

* * *

### Available MCP Tools

When running as an MCP server, StackQL exposes the following tools that AI agents can invoke:

| Tool | Description | Parameters |
|--|--|--|
|`greet`|Simple greeting tool for testing connectivity|`name` (string)|
|`list_providers`|List all available StackQL providers|None|
|`list_services`|List services available in a provider|`provider` (string)|
|`list_resources`|List resources in a provider service|`provider` (string), `service` (string)|
|`list_methods`|List methods available for a resource|`provider` (string), `service` (string), `resource` (string)|
|`query_v2`|Execute a StackQL query|`sql` (string)|

* * *

### Flags

| Flag | Description |
|--|--|
|`--mcp.server.type`|MCP server type: `http` or `reverse_proxy`|
|`--mcp.config`|JSON configuration object for the MCP server|
|`-H,--help`|Print help information|
|`-v,--verbose`|Run in verbose mode with additional output|

&nbsp;
&nbsp;
> see [Global Flags](/docs/command-line-usage/global-flags) for additional options

:::info

You need to set environment variables required for provider authentication before starting the MCP server, see [Using a Provider](/docs/getting-started/using-a-provider) for more information.

:::

* * *

### Examples

#### Basic Standalone MCP Server

Launch a standalone MCP server with provider authentication:

```bash
export GOOGLE_CREDENTIALS=$(cat /path/to/google-credentials.json)

stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912"}}' \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

#### MCP Server with PostgreSQL Server (In-Memory)

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

#### Secure MCP Server with TLS

Launch an MCP server with TLS encryption in reverse proxy mode:

First, generate TLS certificates:

```bash
openssl req -x509 -newkey rsa:4096 -keyout server_key.pem -out server_cert.pem -days 365 -nodes
```

Then start the server:

```bash
stackql srv \
  --mcp.server.type=reverse_proxy \
  --mcp.config '{"server": {"tls_cert_file": "server_cert.pem", "tls_key_file": "server_key.pem", "transport": "http", "address": "127.0.0.1:9004"}, "backend": {"dsn": "postgres://stackql:stackql@127.0.0.1:5446?default_query_exec_mode=simple_protocol"}}' \
  --pgsrv.port 5446 \
  --registry='{"url": "https://registry.stackql.io/providers"}' \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/google-credentials.json"}}'
```

* * *

### Testing Your MCP Server

You can test your MCP server using an MCP client. Here's an example using a command-line MCP client:

```bash
# List all available tools
mcp-client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912

# Test the greeting tool
mcp-client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action greet \
  --exec.args '{"name": "World"}'

# List providers
mcp-client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action list_providers

# Execute a query
mcp-client exec \
  --client-type=http \
  --url=http://127.0.0.1:9912 \
  --exec.action query_v2 \
  --exec.args '{"sql": "SHOW SERVICES IN google"}'
```

* * *

### Integration with AI Assistants

To integrate StackQL's MCP server with AI assistants like Claude Desktop, add the following to your MCP configuration file:

**For Claude Desktop** (`~/Library/Application Support/Claude/claude_desktop_config.json` on macOS):

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=http",
        "--mcp.config",
        "{\"server\": {\"transport\": \"http\", \"address\": \"127.0.0.1:9912\"}}"
      ]
    }
  }
}
```

:::note

Configuration file locations vary by operating system and AI assistant. Consult your AI assistant's documentation for the correct configuration file path and format.

:::

* * *

### Architecture Considerations

When choosing a deployment mode, consider:

1. **Standalone (`mcp` command)**: Simplest setup, ideal for development and testing
2. **In-Memory (`srv` with `http` type)**: Best performance, single process, suitable for most production use cases
3. **Reverse Proxy (`srv` with `reverse_proxy` type)**:
   - Enables workload separation across processes or hosts
   - Supports TLS encryption for secure MCP endpoints
   - Allows independent scaling of MCP and PostgreSQL interfaces
   - Provides flexibility for enterprise CA integration (note: enterprise CA support is experimental)

:::caution

When using TLS with enterprise Certificate Authorities (CAs), additional configuration may be required. This functionality is experimental and may require adjustments based on your specific CA implementation.

:::
