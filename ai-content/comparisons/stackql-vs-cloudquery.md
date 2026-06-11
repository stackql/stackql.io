---
title: StackQL vs CloudQuery
description: CloudQuery is an ELT framework that syncs cloud asset data into a database on a schedule for later analysis; StackQL queries provider APIs live at execution time and can also mutate resources. The core trade-off is historical snapshots versus current state.
keywords: [stackql vs cloudquery, cloud asset inventory, elt, cspm, cloud query, live state]
proficiencyLevel: Intermediate
faq:
  - question: Is CloudQuery a query engine?
    answer: Not in itself. CloudQuery is a sync (ELT) engine - it extracts cloud asset data via source plugins and loads it into destinations such as PostgreSQL, BigQuery, or S3. Querying happens afterwards in the destination database using that database's SQL. StackQL is the query engine itself, executing SQL directly against provider APIs.
  - question: Which is better for historical analysis?
    answer: CloudQuery. Because it loads snapshots into a database you control, you can retain every sync and analyze changes over time. StackQL returns current state; if you need history with StackQL you materialize query results to a database or files on your own schedule.
  - question: Which gives more current results?
    answer: StackQL. Its results are computed from live API calls at query time, so they cannot be staler than the API itself. CloudQuery results are as fresh as the last completed sync.
---

# StackQL vs CloudQuery

CloudQuery is an open-source ELT framework for cloud assets: source plugins extract resource data from providers and load it into a destination database on a schedule, where you query the snapshots with that database's SQL. StackQL inverts the architecture: SQL is executed directly against the provider APIs at query time, with no sync step and no stored copy - and the same engine can mutate resources, not just read them.

## When to use which

Use **CloudQuery** when:

- You need **history**: point-in-time snapshots retained across syncs for trend analysis, forensics, or compliance evidence.
- You want asset data co-located with other warehouse data for joins in an existing BI/analytics stack (BigQuery, Snowflake, Postgres).
- Query latency matters more than freshness - querying a local database is faster than fanning out API calls.

Use **StackQL** when:

- You need **current state**: security checks, drift detection, operational queries, and any decision an automation or AI agent will act on immediately.
- You do not want to operate a sync pipeline and a destination database to answer questions.
- You need write operations - creating, updating, or deleting resources - in the same tool and language as the queries.
- Agents are the consumer: StackQL's MCP server exposes discovery, validation, and gated execution directly; a CloudQuery destination database knows nothing about the provider API surface.

The two are complementary at scale: StackQL for live interrogation and action, CloudQuery (or StackQL results materialized on a schedule) for longitudinal storage.

## Architectural comparison

| Dimension | CloudQuery | StackQL |
|---|---|---|
| Model | Extract-load, query the copy | Query the source live |
| Freshness | As of last sync | As of query execution |
| History | Native (retained snapshots) | Not retained (materialize manually) |
| Infrastructure required | Sync scheduler + destination database | Single binary |
| Writes to cloud | No | INSERT / UPDATE / DELETE / EXEC |
| Provider definition | Go source plugins | Declarative extended-OpenAPI specs |
| Query dialect | Destination database's SQL | StackQL SQL (Postgres-compatible wire in server mode) |
| Agent interface | None (query the destination DB) | Built-in MCP server with safety modes |

## Example

The freshness difference in one query - this StackQL statement reflects instances launched seconds ago, which a sync-based copy will not contain until the next sync completes:

```sql
SELECT instance_id, instance_type, launch_time
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

For longitudinal needs, the equivalent StackQL pattern is scheduling this query (via cron, CI, or pystackql) and appending results to your own store - the same outcome as ELT, but the live query path remains available for time-sensitive questions.

## Related concepts

- [What is Queryable Infrastructure?](/ai/canonical-definitions/what-is-queryable-infrastructure) - live query as an operating model
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe) - the other live-query peer
- [What is Infrastructure Drift?](/ai/canonical-definitions/what-is-infrastructure-drift) - why freshness matters
- [How to query AWS EC2 instances with StackQL](/ai/how-tos/query-aws-ec2-instances-with-stackql) - the example expanded
- [StackQL Architecture Overview](/ai/architecture/stackql-architecture-overview) - how live execution works
