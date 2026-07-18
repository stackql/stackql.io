---
title: StackQL MCP Architecture
description: StackQL's built-in MCP server exposes provider discovery, schema description, query validation, and gated query execution as Model Context Protocol tools, over stdio, HTTP, or reverse-proxy transports, governed by four safety modes and an always-on JSONL audit log.
keywords: [stackql, mcp architecture, model context protocol, ai agents, audit log, elicitation, safety modes]
proficiencyLevel: Expert
faq:
  - question: What is the difference between stackql mcp and stackql srv?
    answer: stackql mcp runs a standalone MCP server. stackql srv runs the PostgreSQL wire protocol server and can host the MCP server alongside it in the same process - either with in-memory communication (mcp.server.type=http) or as a reverse proxy dispatching MCP requests to the Postgres backend over TCP, which supports distributed deployment and TLS.
  - question: What happens when an agent attempts a mutation in safe mode?
    answer: The server sends an MCP elicitation/create request to the client describing the action - tool name, query class, and the SQL itself. The user accepts, declines, or cancels. If the client does not advertise the elicitation capability, the call is refused with a message explaining the gap; non-interactive automation must explicitly opt into full_access.
  - question: What does the audit log record?
    answer: One JSONL record per tool call - timestamp, tool name, server mode, gating decision, query class, the SQL string, arguments, duration, and any error. Result rows are deliberately excluded. Audit is on by default with file-based rotation, and in the default strict failure mode an audit-write failure surfaces as an error so no mutation completes unaudited.
---

# StackQL MCP Architecture

StackQL's MCP server is built into the engine binary and publishes the StackQL capability surface - discovery, description, validation, execution - as Model Context Protocol tools. Its design centers on three things: a small generic tool set instead of per-operation tools, a graduated safety contract for mutations, and an audit trail that records everything an agent does.

## Transports and deployment modes

| Mode | Command | Use |
|---|---|---|
| stdio | `stackql mcp --mcp.server.type=stdio` | Editor-embedded clients (Claude Desktop, Cursor, Continue) that launch the process directly |
| HTTP (standalone) | `stackql mcp --mcp.server.type=http` | Long-running server for standalone agents |
| Dual-server, in-memory | `stackql srv --mcp.server.type=http --pgsrv.port <port>` | MCP and PostgreSQL wire protocol from one process |
| Reverse proxy | `stackql srv --mcp.server.type=reverse_proxy` | MCP requests dispatched to a backing StackQL Postgres server via DSN; supports workload separation and TLS |

Configuration is a JSON (or YAML) object passed via `--mcp.config`, covering transport, address, TLS certificate paths, mode, audit settings, and optional `enabled_tools` / `enabled_prompts` allowlists for publishing a narrowed surface (for example, a read-only inventory server exposing only `server_info` and `list_providers`).

## The tool surface

Fourteen tools, each returning a rendered text view for the LLM plus a typed structured payload:

- **Discovery**: `list_providers`, `list_services`, `list_resources`, `list_methods`, `describe_resource`, `describe_method` - the hierarchy walk that reveals required `WHERE` parameters before any query is written.
- **Registry**: `list_registry` (available providers and versions), `pull_provider` (install into the local cache).
- **Validation**: `validate_select_query` - parse and plan a `SELECT` without executing.
- **Execution**: `run_select_query` (reads), `run_mutation_query` (`INSERT`/`UPDATE`/`REPLACE`/`DELETE` - real side effects), `run_lifecycle_operation` (`EXEC`).
- **Identity**: `server_info` - version, backend, registry, mode; called once at session start.
- **Credentials**: `reload_credentials` - re-sources the `--env.file` dotenv file into the process environment mid-session and reports per-provider credential resolution status; names and statuses only, never secret values (available in releases after v0.10.542).

One static prompt, `write_safe_select`, teaches the method-discovery workflow.

## The safety contract

`server.mode` selects one of four contracts; all allow `SELECT` and metadata, differing on writes:

| Mode | INSERT / UPDATE / REPLACE | DELETE | EXEC |
|---|---|---|---|
| `read_only` | refuse | refuse | refuse |
| `safe` (default) | needs approval | needs approval | needs approval |
| `delete_safe` | allow | needs approval | needs approval |
| `full_access` | allow | allow | allow |

"Needs approval" is implemented with MCP elicitation: the server sends the pending action - tool, query class, SQL - to the client, and a human accepts or declines. Clients that do not advertise elicitation get a refusal pointing the operator at `full_access`, which makes unattended mutation an explicit opt-in rather than a default. The mode is global per server; there are no per-tool overrides.

## The audit subsystem

Every tool call writes one JSONL record - timestamp, tool, mode, decision (`allow`, `refuse_immediate`, `needs_approval_accepted`, and so on), query class, SQL, arguments, duration, error - to a file sink with size/age/backup rotation. The log answers "what did the agent do", deliberately not "what did the agent see": `SELECT` result rows are excluded as potentially large and sensitive. The write happens after execution but before the response returns; under the default `strict` failure mode, an audit failure on a successful mutation returns an error to the client - an intentional choice that no `DELETE` completes unaudited.

## Related concepts

- [How to use StackQL with AI agents](/ai/how-tos/use-stackql-with-ai-agents) - operational setup
- [How to use StackQL with Claude](/ai/how-tos/use-stackql-with-claude) - Claude Desktop specifics
- [StackQL vs Custom MCP Servers](/ai/comparisons/stackql-vs-custom-mcp-servers) - the design trade-off
- [StackQL Architecture Overview](/ai/architecture/stackql-architecture-overview) - where the MCP server sits in the engine
- [What is Agentic Infrastructure?](/ai/canonical-definitions/what-is-agentic-infrastructure) - the pattern this implements
