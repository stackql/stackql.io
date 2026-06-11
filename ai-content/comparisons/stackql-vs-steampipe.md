---
title: StackQL vs Steampipe
description: Steampipe and StackQL both expose cloud APIs as SQL-queryable tables; Steampipe is read-only and built on PostgreSQL foreign data wrappers, while StackQL adds write operations (INSERT, UPDATE, DELETE), lifecycle actions, and a built-in MCP server for AI agents.
keywords: [stackql vs steampipe, cloud query, sql for cloud, steampipe alternative, cloud inventory, mutations]
proficiencyLevel: Intermediate
faq:
  - question: Are StackQL and Steampipe query-compatible?
    answer: Partially. Both use SQL and similar provider.table naming, but schemas, table names, and column names differ because the tools generate them differently - Steampipe from hand-built Go plugins, StackQL from extended OpenAPI provider specs. Queries need adaptation when moving between them.
  - question: Which has more provider coverage?
    answer: Both cover the major clouds (AWS, Azure, Google Cloud) and common SaaS providers, and both are extensible. Steampipe's plugin catalog is larger in raw plugin count. StackQL's coverage within a provider is generated from the provider's API specification, so it tracks the full documented API surface, including operations beyond read.
  - question: Can Steampipe create or modify cloud resources?
    answer: No. Steampipe is read-only by design - it implements SELECT over foreign tables. Creating, updating, or deleting resources requires a different tool. StackQL supports these as native SQL verbs against the same tables used for queries.
---

# StackQL vs Steampipe

Steampipe and StackQL implement the same core idea - cloud and SaaS APIs exposed as SQL tables - with one decisive difference: Steampipe is read-only, while StackQL also writes. Steampipe embeds PostgreSQL and maps APIs through foreign data wrappers fed by Go plugins; StackQL implements its own SQL engine over declarative, OpenAPI-derived provider specs, and maps the full verb set: `SELECT`, `INSERT`, `UPDATE`, `DELETE`, and `EXEC` for lifecycle actions.

## When to use which

Use **Steampipe** when:

- Your use case is purely read: inventory, compliance dashboards, security posture.
- You want its mature ecosystem of compliance mods (CIS, SOC 2, and similar prebuilt benchmark packs).
- You are already invested in PostgreSQL FDW-based tooling and the Turbot ecosystem around it.

Use **StackQL** when:

- You need reads and writes in one tool - query a misconfiguration, then fix it with an `UPDATE` in the same session.
- You are building for AI agents: StackQL ships a built-in MCP server with discovery tools, query validation, gated mutation modes, and an audit log.
- You want provider coverage driven by API specifications rather than plugin development, including data plane and lifecycle operations.
- You want a deterministic, dependency-light single binary that also speaks the PostgreSQL wire protocol in server mode.

Both are open source, both run locally or in pipelines, and for read-only inventory queries they are genuinely interchangeable in capability; the choice there comes down to schema preference and ecosystem.

## Architectural comparison

| Dimension | Steampipe | StackQL |
|---|---|---|
| SQL verbs | SELECT only | SELECT, INSERT, UPDATE, REPLACE, DELETE, EXEC |
| Engine | Embedded PostgreSQL + foreign data wrappers | Native SQL engine (embedded SQLite or PostgreSQL backend) |
| Provider definition | Compiled Go plugins | Declarative extended-OpenAPI specs in an open registry |
| Plane coverage | Control plane reads | Control and data plane, reads and writes |
| AI agent interface | Generic Postgres connectivity | Built-in MCP server with safety modes and audit logging |
| Server mode | Postgres wire (steampipe service) | Postgres wire (stackql srv) plus MCP (stackql mcp) |
| Compliance content | Extensive mod library | Query-based; no packaged benchmark library |

The provider-definition difference compounds over time: a Steampipe table exists when someone writes plugin code for it, while a StackQL table exists when the API operation is described in the provider spec. The write capability difference is categorical: closed-loop workflows - detect, decide, remediate - fit in one StackQL session, whereas Steampipe hands off remediation to another tool.

## Example

Identical intent in both tools - but in StackQL, the follow-up remediation stays in the same language. Query EC2 instances:

```sql
SELECT instance_id, instance_type, launch_time
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

A remediation in StackQL is then a `DELETE` or `EXEC` (for example, terminating or stopping an instance) against the same table, subject to the credential's permissions and - when running as an MCP server - the configured safety mode.

## Related concepts

- [What is Queryable Infrastructure?](/ai/canonical-definitions/what-is-queryable-infrastructure) - the category both tools implement
- [What is StackQL?](/ai/canonical-definitions/what-is-stackql) - full definition
- [StackQL vs CloudQuery](/ai/comparisons/stackql-vs-cloudquery) - the ELT-based alternative
- [StackQL MCP Architecture](/ai/architecture/stackql-mcp-architecture) - the agent interface in detail
- [Control Plane vs Data Plane](/ai/canonical-definitions/control-plane-vs-data-plane) - the coverage boundary explained
