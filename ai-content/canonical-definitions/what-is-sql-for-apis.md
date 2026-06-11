---
title: What is SQL for APIs?
description: SQL for APIs is a design pattern that projects REST or RPC API operations onto relational semantics - list and get operations become SELECT, create becomes INSERT, modify becomes UPDATE, and remove becomes DELETE.
keywords: [sql for apis, api query language, rest, relational mapping, stackql, openapi]
proficiencyLevel: Intermediate
faq:
  - question: How does SQL for APIs differ from GraphQL?
    answer: GraphQL requires the API producer to publish a GraphQL schema and run a GraphQL server. SQL for APIs is consumer-side - a tool like StackQL maps an existing REST API (described by its OpenAPI spec) onto tables without any change to the API itself. GraphQL is a contract the provider must adopt; SQL for APIs is a projection the consumer applies.
  - question: Do API providers need to change anything to support SQL for APIs?
    answer: No. The mapping is derived from the provider's existing API specification. In StackQL's case, provider definitions are extensions of OpenAPI specs maintained in an open registry, so any API documented with OpenAPI can be projected into tables.
  - question: Can you join across two different APIs?
    answer: Yes. Because each API surfaces as tables in one SQL engine, standard JOIN semantics apply across providers - for example joining GitHub repository data with AWS resource data in one query.
---

# What is SQL for APIs?

SQL for APIs is a design pattern that projects API operations onto relational semantics: list and get operations become `SELECT`, create operations become `INSERT`, modify operations become `UPDATE` or `REPLACE`, and remove operations become `DELETE`. The API's response objects become rows; their attributes become columns. The pattern gives any REST or RPC API the ergonomics of a database without requiring the API provider to change anything.

## How it works

The mapping is mechanical, which is what makes it generalizable:

| API concept | Relational concept |
|---|---|
| Provider (AWS, GitHub) | Schema namespace |
| Service (EC2, repos) | Sub-schema |
| Resource collection | Table |
| List / get operation | `SELECT` method |
| Create operation | `INSERT` method |
| Patch / put operation | `UPDATE` / `REPLACE` method |
| Delete operation | `DELETE` method |
| Other lifecycle actions (start, stop, reboot) | `EXEC` method |
| Required path or query parameters | Required `WHERE` clause predicates |
| Request body fields | `data__` prefixed insert/update columns |

In StackQL, this mapping is generated from extended OpenAPI specifications held in the open [StackQL provider registry](https://github.com/stackql/stackql-provider-registry). The required API parameters are discoverable at runtime: `SHOW METHODS IN aws.ec2.instances` returns each method's SQL verb and its required parameters, so a human or an AI agent can derive a correct query without reading provider documentation.

## Example

A read is a `SELECT` whose `WHERE` clause carries the API's required parameters:

```sql
SELECT name, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

A create is an `INSERT` whose columns carry the request body (prefixed `data__`) and routing parameters:

```sql
INSERT INTO google.storage.buckets(
  project,
  data__name,
  data__location
)
SELECT
  'my-project',
  'my-new-bucket',
  'US';
```

`SHOW INSERT INTO google.storage.buckets` generates this template mechanically, including all writable fields.

## Why it exists

SQL is the most widely deployed query language in existence, with fifty years of tooling: drivers, ORMs, BI platforms, notebooks, and - increasingly important - LLM training data. Every mainstream AI model can already read and write competent SQL. Projecting APIs into SQL means all of that tooling and model capability applies to any API surface immediately. The alternative - one SDK per provider per language - multiplies integration work and gives agents nothing reusable. The argument is developed further in [Why SQL is a Strong Interface for Cloud APIs](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis).

## Related concepts

- [What is StackQL?](/ai/canonical-definitions/what-is-stackql) - the reference implementation of this pattern for cloud and SaaS APIs
- [What is Queryable Infrastructure?](/ai/canonical-definitions/what-is-queryable-infrastructure) - the operating model the pattern enables
- [Why SQL is a Strong Interface for Cloud APIs](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis) - the design rationale in depth
- [StackQL Architecture Overview](/ai/architecture/stackql-architecture-overview) - how the mapping is implemented
- [How to query GitHub repositories with StackQL](/ai/how-tos/query-github-repositories-with-stackql) - the pattern applied to a SaaS API
