---
title: How to use StackQL with AI agents
description: Run StackQL as a Model Context Protocol (MCP) server so AI agents can discover cloud provider schemas, validate queries, and execute governed reads and mutations against live infrastructure.
keywords: [stackql, ai agents, mcp, model context protocol, agentic infrastructure, automation]
proficiencyLevel: Intermediate
faq:
  - question: What MCP tools does the StackQL server expose?
    answer: Discovery tools (list_providers, list_services, list_resources, list_methods, describe_resource, describe_method), registry tools (list_registry, pull_provider), validation (validate_select_query), execution (run_select_query, run_mutation_query, run_lifecycle_operation), server_info, and reload_credentials (re-sources the --env.file dotenv file and reports per-provider credential status). The enabled_tools allowlist can narrow this set, for example to publish a read-only inventory server.
  - question: How are agent mutations kept safe?
    answer: By the server mode. The default safe mode allows SELECT and metadata calls but requires human approval (via the MCP elicitation flow) for INSERT, UPDATE, DELETE, and EXEC. read_only refuses mutations outright; delete_safe allows create/update but gates deletes; full_access allows everything. Every tool call is written to a JSONL audit log, on by default.
  - question: Which transports are supported?
    answer: stdio for editor-embedded clients (Claude Desktop, Cursor, Continue), HTTP for standalone agents, and a reverse-proxy mode that dispatches MCP requests to a backing StackQL PostgreSQL server, with optional TLS.
---

# How to use StackQL with AI agents

StackQL ships a built-in Model Context Protocol (MCP) server. Any MCP-capable agent - Claude, Cursor, Continue, or a custom agent framework - connects to it and gains a self-describing, governed SQL interface to every cloud and SaaS provider in the StackQL registry.

## Steps

1. **Set provider credentials** as environment variables before starting the server (the server inherits them):

```bash
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
```

Alternatively (in StackQL releases after v0.10.542), nominate a dotenv-style credentials file with `--env.file`; the `reload_credentials` MCP tool re-sources it mid-session so rotated credentials reach a running server without a restart.

2. **Start the MCP server.** For a standalone HTTP server:

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912"}}'
```

For editor-embedded clients, use `--mcp.server.type=stdio` and let the client launch the process (see [How to use StackQL with Claude](/ai/how-tos/use-stackql-with-claude)). To serve MCP and the PostgreSQL wire protocol from one process, use `stackql srv` with the same MCP flags plus `--pgsrv.port`.

3. **Choose a safety mode.** The mode is set in the server config and gates what agents can do:

| Mode | SELECT / metadata | INSERT / UPDATE | DELETE | EXEC |
|---|---|---|---|---|
| `read_only` | allow | refuse | refuse | refuse |
| `safe` (default) | allow | needs approval | needs approval | needs approval |
| `delete_safe` | allow | allow | needs approval | needs approval |
| `full_access` | allow | allow | allow | allow |

"Needs approval" uses the MCP elicitation flow: the client shows the user the pending SQL and the user accepts or declines. Pin `read_only` for inventory agents; reserve `full_access` for trusted pipelines with a reviewed audit log.

4. **Let the agent discover, then query.** A typical agent session: `server_info` once, then `list_providers` -> `list_methods` (which reveals required `WHERE` parameters) -> `validate_select_query` -> `run_select_query`:

```sql
SELECT name, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

The agent never loads an SDK; the schema is discovered at runtime and the query is plain SQL.

## Auditing

Every tool call writes one JSONL record - timestamp, tool, mode, decision, query class, SQL, duration - to a file sink with rotation. Audit is on by default and records what the agent did (not result rows). In the default `strict` failure mode, a failed audit write surfaces as an error rather than letting an unaudited mutation slip through.

## Related concepts

- [How to use StackQL with Claude](/ai/how-tos/use-stackql-with-claude) - Claude Desktop configuration
- [StackQL MCP Architecture](/ai/architecture/stackql-mcp-architecture) - transports, modes, and audit internals
- [StackQL vs Custom MCP Servers](/ai/comparisons/stackql-vs-custom-mcp-servers) - when to build instead
- [What is Agentic Infrastructure?](/ai/canonical-definitions/what-is-agentic-infrastructure) - the pattern this enables
- [Why SQL is a Strong Interface for Cloud APIs](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis) - why agents and SQL fit
