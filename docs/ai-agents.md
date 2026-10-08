---
slug: /ai-agents
title: AI Agents
hide_title: false
keywords:
  - stackql
  - ai agents
  - mcp
  - model context protocol
  - agentic
description: Connect AI agents to cloud and SaaS services with StackQL through MCP servers, tools, embedded integrations and SQL queries.
image: "/img/stackql-featured-image.png"
---

StackQL gives AI agents the same SQL interface people use to query, provision and operate cloud and SaaS services. An agent discovers what a provider exposes, validates a query before it runs, reads live state with `SELECT` and changes it with `INSERT`, `UPDATE` and `DELETE`, with no state file and no per-cloud SDK.

## How agents connect

- **MCP over stdio** for editor and desktop clients such as Claude Desktop, Cursor and Continue. The client launches `stackql mcp --mcp.server.type=stdio` itself. See [MCP Server](/command-line-usage/mcp).
- **MCP over HTTP** for standalone agents and automation that connect to a long-running `stackql mcp` or `stackql srv` process, with bearer-token authentication and optional TLS.
- **Embedded MCP** for compiled applications: the server runs in-process in Go, Rust, Swift, Kotlin, .NET and Gleam. See [Embedded MCP](/mcp/embedded).
- **PostgreSQL wire protocol** via [`stackql srv`](/command-line-usage/srv) for agent frameworks, notebooks and BI tools that already speak Postgres.

## Quick start with Claude Desktop

Install from the Anthropic Connector Directory (recommended) or the downloadable MCP Bundle, or register the server by hand in `claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": ["mcp", "--mcp.server.type=stdio", "--tls.allowInsecure"],
      "env": {
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key"
      }
    }
  }
}
```

Include only the credentials for the providers you plan to use. Full instructions, including the bundle downloads and using a credentials file, are in [Using StackQL with Claude Desktop](/getting-started/claude-desktop).

## What an agent can do

The MCP server exposes a small, typed tool set. Each tool returns a rendered view for the model and a structured payload for programmatic clients.

- **Discover**: `list_providers`, `list_services`, `list_resources`, `list_methods` and `describe_method` walk the provider hierarchy and return the inputs and output fields of each operation, so required `WHERE` parameters are known before a query is written.
- **Validate and read**: `validate_select_query` parses and plans a `SELECT` without running it; `run_select_query` executes it.
- **Change**: `run_mutation_query` runs `INSERT`, `UPDATE`, `REPLACE` and `DELETE`; `run_lifecycle_operation` runs `EXEC` lifecycle operations. Both are gated by the server mode below.
- **Housekeeping**: `server_info`, `list_registry`, `pull_provider` and `reload_credentials`.

The full reference, with inputs, gating behaviour and an example prompt per tool, is at [MCP Tools](/mcp).

## Guardrails

The server mode sets the safety contract for the whole session:

| Mode | `SELECT` / metadata | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` / `EXEC` |
|--|--|--|--|
| `read_only` | allow | refuse | refuse |
| `safe` (default) | allow | needs approval | needs approval |
| `delete_safe` | allow | allow | needs approval |
| `full_access` | allow | allow | allow |

"Needs approval" uses MCP elicitation: clients that support it (Claude Desktop, Cursor, Continue) show the user the tool name, query class and SQL and wait for a decision before anything runs; clients that don't advertise it are refused. Every tool call is written to an audit log, HTTP listeners require a bearer token, and credentials are sourced from the environment or a dotenv file without secret values ever being returned to the client. Details are in [Server modes](/command-line-usage/mcp#server-modes), [HTTP client authentication](/command-line-usage/mcp#http-client-authentication) and [Audit log](/command-line-usage/mcp#audit-log).

## Next steps

- [MCP Server](/command-line-usage/mcp) - deployment modes, configuration, server modes, audit log and flags.
- [MCP Tools](/mcp) - the full tool reference with inputs, gating and example prompts.
- [Embedded MCP](/mcp/embedded) - run the server inside your own Go, Rust, Swift, Kotlin, .NET or Gleam application.
- [Query Library](pathname:///docs/query-library/) - ready-to-run SQL for common cloud operations questions, by provider.
- [AI Reference](/ai) - concepts, how-tos, comparisons and troubleshooting written for agents and the people who build them.
