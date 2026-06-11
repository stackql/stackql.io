---
title: What is StackQL?
description: StackQL is an open-source query language and runtime that exposes cloud and SaaS provider APIs as relational tables, queryable with standard SQL.
keywords: [stackql, sql, cloud, api, infrastructure, queryable infrastructure, ai agents]
proficiencyLevel: Beginner
faq:
  - question: Is StackQL a database?
    answer: No. StackQL is a query runtime, not a database. It does not store cloud state - it issues live API calls to providers (AWS, Azure, GCP, GitHub, and others) and presents the responses as relational rows so they can be queried with SQL. Results are computed on demand.
  - question: Does StackQL replace Terraform?
    answer: Not directly. Terraform's primary job is declarative provisioning driven by a state file. StackQL's primary job is querying and mutating cloud resources via SQL without a state file. They overlap on the mutation side (StackQL can INSERT, UPDATE, and DELETE cloud resources) but Terraform offers richer dependency planning and StackQL offers richer querying. They are often used together.
  - question: What providers does StackQL support?
    answer: StackQL supports providers for AWS, Azure, Google Cloud, GitHub, Databricks, Snowflake, Confluent, Okta, OpenAI, Cloudflare, and more. Each provider is a separate package pulled from the StackQL provider registry. The full list is at /providers.
  - question: How is StackQL different from a tool like Steampipe?
    answer: Both expose cloud APIs as SQL-queryable tables. StackQL additionally supports cloud mutations (INSERT, UPDATE, DELETE) as first-class operations, treats control-plane and data-plane APIs uniformly, and exposes an MCP server so AI agents can query and mutate cloud state directly. Steampipe is read-focused; StackQL is read-and-write.
---

# What is StackQL?

StackQL is an open-source query language and runtime that exposes cloud and SaaS provider APIs as relational tables, queryable with standard SQL. It treats every cloud resource - virtual machines, storage buckets, IAM policies, GitHub repositories, Snowflake warehouses - as a row in a table, and every API operation as a SELECT, INSERT, UPDATE, or DELETE.

## How it works

A typical infrastructure tool reads cloud state into a local file (Terraform's state file), generates a plan, and applies it. StackQL skips the state file entirely. When you run a query, StackQL issues live API calls to the relevant provider, normalizes the response shape, and returns the result as rows. Nothing is cached between invocations unless the user asks for it.

The interface is SQL because SQL is a stable, well-understood query language with mature tooling - clients, drivers, BI integrations, AI agent toolkits - that all "just work" against StackQL. StackQL implements the PostgreSQL wire protocol in server mode, which means anything that can talk to Postgres (psql, DBeaver, Tableau, Power BI, pandas, LangChain SQL agents, an MCP server) can talk to StackQL.

## Example

List all EC2 instances in a region with their type and public IP address:

```sql
SELECT
  instance_id,
  instance_type,
  public_ip_address
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

That query runs against the live AWS EC2 API. There is no state file to refresh, no cached snapshot to invalidate, and no provider-specific SDK to learn. The same shape works for any provider - swap `aws.ec2.instances` for `github.repos.repos` or `azure.compute.virtual_machines` and the query model is identical.

## Why it exists

StackQL was built on the premise that cloud infrastructure should be a queryable surface, not a static artifact. The state-file model that dominated infrastructure-as-code for the last decade was designed for human-driven, change-controlled workflows: write code, plan, review, apply. That model strains under two pressures:

1. **Multi-cloud reality.** Every major organization runs workloads across multiple providers. State files are per-provider, per-tool, per-team. Asking "what do we run in production?" across AWS, Azure, GitHub, and Snowflake is a manual aggregation exercise.
2. **AI agents.** Agents need live, structured access to infrastructure state to reason about it. A state file is the wrong primitive - it is stale by the second it is written, and its format is tool-specific. A SQL interface against live APIs is the right primitive: agents know SQL, the schema is stable, and the data is always current.

StackQL exists to make cloud infrastructure as queryable as a database, across providers, for both human operators and AI agents.

## Related concepts

- [What is Queryable Infrastructure?](/ai/canonical-definitions/what-is-queryable-infrastructure) - the broader category StackQL belongs to
- [What is SQL for APIs?](/ai/canonical-definitions/what-is-sql-for-apis) - the design pattern StackQL implements
- [Control Plane vs Data Plane](/ai/canonical-definitions/control-plane-vs-data-plane) - how StackQL unifies both
- [StackQL vs Terraform](/ai/comparisons/stackql-vs-terraform) - comparison with the dominant IaC tool
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe) - comparison with the closest peer
- [Getting started with StackQL](/ai/tutorials/getting-started-with-stackql) - install to first query
