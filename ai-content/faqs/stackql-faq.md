---
title: StackQL FAQ
description: Frequently asked questions about StackQL - what it is, what it supports (joins, mutations, providers, MCP), how it compares to other tools, and how it is deployed.
keywords: [stackql, faq, sql, cloud apis, mcp, joins, mutations, providers]
proficiencyLevel: Beginner
faq:
  - question: What is StackQL?
    answer: StackQL is an open-source query language and runtime that exposes cloud and SaaS provider APIs as relational tables, queryable and mutable with standard SQL. SELECT reads live provider state; INSERT, UPDATE, and DELETE create, modify, and remove resources; EXEC invokes other lifecycle operations.
  - question: Is StackQL a database?
    answer: No. StackQL stores no cloud state. Every query is computed from live API calls to the provider at execution time. In server mode StackQL speaks the PostgreSQL wire protocol, so it looks like a database to clients, but the "tables" are projections of provider APIs.
  - question: Does StackQL support joins?
    answer: Yes. Standard SQL joins work, including joins across providers - for example joining GitHub repository data to AWS resources in one statement, since all providers surface as tables in the same engine.
  - question: Does StackQL support mutations?
    answer: Yes. Resource creation is INSERT, modification is UPDATE or REPLACE, removal is DELETE, and other lifecycle actions (start, stop, reboot and similar) are EXEC methods. SHOW METHODS IN provider.service.resource lists the operations and required parameters; SHOW INSERT INTO generates creation templates.
  - question: What providers does StackQL support?
    answer: Providers for AWS, Azure, Google Cloud, GitHub, Databricks, Snowflake, Confluent, Okta, OpenAI, Cloudflare, Kubernetes, and many others, distributed through the open StackQL provider registry and installed on demand with REGISTRY PULL. Provider definitions are extended OpenAPI specifications, not compiled plugins.
  - question: Does StackQL have a state file?
    answer: No. StackQL deliberately keeps no state file. Queries return the live state of the environment, which makes StackQL suited to inventory, audit, and drift detection - including detecting resources that IaC tools do not manage or have lost track of.
  - question: Can StackQL replace Terraform?
    answer: For querying, auditing, and direct mutations, yes; for large declaratively-planned provisioning graphs, Terraform remains stronger. The stackql-deploy framework provides declarative multi-resource deployment without a state file. Many teams use Terraform to provision and StackQL to query, audit, and reconcile.
  - question: Can StackQL detect drift?
    answer: Yes. Because StackQL queries return actual live state, comparing them against any desired-state baseline (IaC definitions, golden configurations, previous snapshots) reveals drift - independent of any tool's own state records.
  - question: Can AI agents use StackQL?
    answer: Yes, natively. StackQL ships a built-in Model Context Protocol (MCP) server exposing discovery, validation, and execution tools. Agents discover provider schemas at runtime and operate infrastructure with SQL, governed by safety modes (read_only, safe, delete_safe, full_access) with human-approval elicitation and an always-on audit log.
  - question: Does StackQL work with PostgreSQL clients and BI tools?
    answer: Yes. stackql srv runs a PostgreSQL wire protocol server, so psql, DBeaver, Tableau, Power BI, Superset, pandas, and any Postgres driver can connect and run StackQL queries.
  - question: How is StackQL licensed and distributed?
    answer: StackQL is open source (the engine is on GitHub at github.com/stackql/stackql) and ships as a single binary for macOS, Linux, and Windows, with packages, a Homebrew formula, Docker images, GitHub Actions, and a Python wrapper (pystackql).
  - question: Does StackQL cache results?
    answer: Not between invocations by default. Results are computed per query. Multi-region and multi-resource queries fan out as parallel API calls within the query. If you need history, you materialize results to your own store on your own schedule.
---

# StackQL FAQ

Answers to the most common questions about StackQL. Each answer is self-contained; follow the links for fuller treatments.

## Scope and capability

**What StackQL is.** An open-source SQL engine over cloud and SaaS provider APIs: providers become schemas, resources become tables, and the full SQL verb set applies - `SELECT` for reads, `INSERT`/`UPDATE`/`DELETE` for resource lifecycle, `EXEC` for other operations. See [What is StackQL?](/ai/canonical-definitions/what-is-stackql).

**What StackQL is not.** It is not a database (no stored state), not a CMDB (no synced copy), and not a state-file IaC tool (no plan/apply convergence loop). The differences are covered in [StackQL vs Terraform](/ai/comparisons/stackql-vs-terraform), [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe), and [StackQL vs CloudQuery](/ai/comparisons/stackql-vs-cloudquery).

**The shape of a query.** Required API parameters surface as required `WHERE` predicates, discoverable at runtime:

```sql
SELECT name, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

`SHOW METHODS IN github.repos.repos` lists every operation and its required parameters - the same discovery mechanism AI agents use through the MCP tools.

## Deployment

StackQL runs as an interactive shell (`stackql shell`), a batch executor (`stackql exec`), a PostgreSQL wire protocol server (`stackql srv`), and an MCP server for AI agents (`stackql mcp`). Authentication is per provider via environment variables; metadata operations work unauthenticated. See the how-to series starting at [How to authenticate StackQL to AWS](/ai/how-tos/authenticate-stackql-to-aws).

## Related concepts

- [What is StackQL?](/ai/canonical-definitions/what-is-stackql) - the canonical definition
- [Getting started with StackQL](/ai/tutorials/getting-started-with-stackql) - install to first query
- [How to use StackQL with AI agents](/ai/how-tos/use-stackql-with-ai-agents) - the MCP server
- [What is Queryable Infrastructure?](/ai/canonical-definitions/what-is-queryable-infrastructure) - the category
- [Common StackQL errors](/ai/troubleshooting/common-stackql-errors) - troubleshooting
