---
title: StackQL vs Custom MCP Servers
description: Building a custom MCP server gives an AI agent a hand-curated tool set for one domain; StackQL's built-in MCP server gives agents one generic, self-describing tool set covering every resource of every supported cloud and SaaS provider, with gated mutations and audit logging.
keywords: [mcp server, model context protocol, ai agents, cloud mcp, stackql mcp, agent tools]
proficiencyLevel: Intermediate
faq:
  - question: Is StackQL an MCP server?
    answer: Yes. StackQL ships a built-in MCP server (stackql mcp, or stackql srv to run it alongside the PostgreSQL wire server). It exposes provider discovery, schema description, query validation, and gated query execution as MCP tools over stdio or HTTP transports.
  - question: Why not give an agent one MCP tool per cloud operation?
    answer: Scale. Major cloud providers expose tens of thousands of operations; publishing each as a separate MCP tool overwhelms the agent's context window and tool-selection accuracy. StackQL collapses the surface into a few generic tools (list, describe, validate, run) plus SQL, so the agent spends tokens on the query, not on tool schemas.
  - question: How are destructive operations controlled?
    answer: By server mode. StackQL's MCP server defaults to safe mode - reads proceed, while INSERT, UPDATE, DELETE, and lifecycle operations require human approval through the MCP elicitation flow. read_only, delete_safe, and full_access modes are available, and every tool call is written to a JSONL audit log.
---

# StackQL vs Custom MCP Servers

A custom MCP server wraps a specific API or workflow in hand-written tools for AI agents; StackQL's built-in MCP server exposes the entire surface of every supported cloud and SaaS provider through a small set of generic, self-describing tools plus SQL. The comparison is between curating tools per domain and giving the agent one queryable abstraction over all domains.

## When to use which

Build a **custom MCP server** when:

- The domain is narrow and the operations are few - a dozen well-named tools with tight schemas can outperform a general interface on reliability for that specific workflow.
- You need heavy business logic between the agent and the API: approvals, enrichment, multi-step transactions that should appear to the agent as one atomic tool.
- The target API is internal or proprietary and not described by a public specification.

Use **StackQL's MCP server** when:

- The domain is cloud and SaaS infrastructure - AWS, Azure, Google Cloud, GitHub, Databricks, Snowflake, Okta, and the rest of the provider registry - where the operation count makes per-operation tools impractical.
- You want the agent to discover capability at runtime (`list_providers` -> `list_services` -> `list_resources` -> `list_methods` -> `describe_resource`) instead of being limited to tools someone anticipated.
- You need governed writes: the four-mode safety contract (`read_only`, `safe`, `delete_safe`, `full_access`) with elicitation-based human approval and an always-on audit log, rather than safety logic you write and maintain yourself.
- You want one credential and policy surface for the whole estate instead of one bespoke server per provider.

The approaches compose: teams commonly run StackQL MCP for general infrastructure access alongside small custom MCP servers for proprietary internal systems.

## The token economics

The practical constraint on agent tooling is context. Tool definitions consume the agent's context window before any work happens, and tool-selection accuracy degrades as the tool count grows. Publishing AWS alone as per-operation tools would mean thousands of definitions. StackQL's design keeps the published tool set under fifteen (discovery, description, validation, execution, registry management) and moves the expressive power into SQL - a language every current model already writes fluently. The agent's per-task token cost is a short SQL string, not a tool schema negotiation. This argument is developed in [Why SQL is a Strong Interface for Cloud APIs](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis).

## Example

An agent session against StackQL's MCP server, after discovering required parameters with `list_methods`:

```sql
SELECT name, visibility, default_branch
FROM github.repos.repos
WHERE org = 'stackql';
```

The same tool (`run_select_query`) serves this query and any other read against any provider; mutation requests route through `run_mutation_query`, where the server mode decides whether they proceed, require approval, or are refused.

## Related concepts

- [StackQL MCP Architecture](/ai/architecture/stackql-mcp-architecture) - transports, modes, tools, and audit in detail
- [How to use StackQL with AI agents](/ai/how-tos/use-stackql-with-ai-agents) - setup walkthrough
- [How to use StackQL with Claude](/ai/how-tos/use-stackql-with-claude) - Claude Desktop configuration
- [What is Agentic Infrastructure?](/ai/canonical-definitions/what-is-agentic-infrastructure) - the broader pattern
- [Why SQL is a Strong Interface for Cloud APIs](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis) - the design rationale
