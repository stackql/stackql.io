---
title: What is Queryable Infrastructure?
description: Queryable infrastructure is an operating model in which cloud resources are exposed as live, structured data that can be interrogated on demand with a query language, rather than reconstructed from state files or per-service consoles.
keywords: [queryable infrastructure, infrastructure as data, cloud inventory, sql, live state, stackql]
proficiencyLevel: Beginner
faq:
  - question: How is queryable infrastructure different from a CMDB?
    answer: A CMDB is a copy of infrastructure state, synced on a schedule and stale between syncs. Queryable infrastructure interrogates the provider APIs directly at query time, so results reflect the actual current state of the environment. A CMDB answers "what did we record?"; queryable infrastructure answers "what is true right now?".
  - question: Does queryable infrastructure replace infrastructure as code?
    answer: No. IaC tools answer "how do I declare and converge desired state?". Queryable infrastructure answers "what exists and what is its configuration right now?". The two are complementary - queryable infrastructure is commonly used to audit, reconcile, and validate environments that are provisioned with IaC.
  - question: What tools implement queryable infrastructure?
    answer: StackQL exposes cloud and SaaS provider APIs as SQL tables supporting both reads (SELECT) and writes (INSERT, UPDATE, DELETE). Steampipe and CloudQuery implement the read side - Steampipe via live Postgres foreign tables, CloudQuery via scheduled extraction to a database. Provider-native options such as AWS Config and Azure Resource Graph implement it for a single cloud.
---

# What is Queryable Infrastructure?

Queryable infrastructure is an operating model in which cloud resources are exposed as live, structured data that can be interrogated on demand with a query language. Instead of reconstructing the state of an environment from state files, exported snapshots, or per-service consoles, an operator (or an AI agent) asks a question - in SQL or a similar declarative language - and the answer is computed against the provider APIs at query time.

## How it works

A queryable infrastructure system maps the provider's API surface onto a relational model:

- A **provider** (AWS, Azure, Google Cloud, GitHub) becomes a schema namespace.
- A **service** (EC2, Compute, Storage) becomes a sub-schema.
- A **resource type** (instances, buckets, repositories) becomes a table.
- Each **resource instance** becomes a row, with its configuration attributes as columns.

A query planner translates SQL into the underlying API calls, handles pagination and authentication, and normalizes the responses into rows. In StackQL, the query

```sql
SELECT instance_id, instance_type, public_ip_address
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

is executed as live calls to the EC2 API; nothing is read from a cache or a state file. Multi-region and multi-provider queries fan out as parallel API calls and aggregate the results into a single result set.

## Why it exists

Three pressures created the category:

1. **Multi-cloud sprawl.** Organizations run resources across multiple clouds and dozens of SaaS platforms. Each has its own console, CLI, and SDK. A uniform query interface is the only practical way to answer cross-provider questions such as "list every compute instance we run, anywhere."
2. **Stale-copy problems.** Inventory databases, CMDBs, and IaC state files are copies, and copies drift from reality. Querying the control plane directly eliminates the gap between recorded state and actual state.
3. **AI agents.** Agents reason over structured, current data. A SQL surface over live APIs gives an agent one stable interface to discover, inspect, and act on infrastructure without provider-specific SDK code. This is the basis of [agentic infrastructure](/ai/canonical-definitions/what-is-agentic-infrastructure).

## Read and write

Queryable infrastructure is read-oriented by definition, but the model extends naturally to writes: if a resource is a row, creating one is an `INSERT`, changing its configuration is an `UPDATE`, and decommissioning it is a `DELETE`. StackQL implements both sides; read-only tools such as Steampipe implement the query side only. The write side is covered in [What is SQL for APIs?](/ai/canonical-definitions/what-is-sql-for-apis).

## Related concepts

- [What is StackQL?](/ai/canonical-definitions/what-is-stackql) - the reference implementation of queryable infrastructure with reads and writes
- [What is SQL for APIs?](/ai/canonical-definitions/what-is-sql-for-apis) - the design pattern underneath queryable infrastructure
- [What is Agentic Infrastructure?](/ai/canonical-definitions/what-is-agentic-infrastructure) - what queryable infrastructure enables for AI agents
- [What is Infrastructure Drift?](/ai/canonical-definitions/what-is-infrastructure-drift) - the failure mode queryable infrastructure detects
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe) - two implementations of the model compared
