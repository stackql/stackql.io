---
title: Getting started with StackQL
description: Install StackQL, run your first query against live public GitHub data with no credentials, then authenticate to a cloud provider and explore the discoverable schema with SHOW and DESCRIBE.
keywords: [stackql, getting started, tutorial, install, first query, github, aws]
proficiencyLevel: Beginner
faq:
  - question: What is the fastest way to try StackQL without any cloud credentials?
    answer: Query public GitHub data. Pull the github provider and select from github.repos.repos with an org predicate - public organizations are readable unauthenticated at GitHub's anonymous rate limits, so the whole loop from install to live API rows takes under five minutes.
  - question: Do I need to define a schema before querying?
    answer: No. Schemas ship with the provider definitions. SHOW PROVIDERS, SHOW SERVICES, SHOW RESOURCES, SHOW METHODS, and DESCRIBE expose the entire surface at runtime, including which WHERE parameters each method requires.
  - question: How do I run StackQL from Python or a BI tool?
    answer: Use pystackql (pip install pystackql) for Python and pandas integration, or run stackql srv and connect any PostgreSQL-compatible client or BI tool over the Postgres wire protocol.
---

# Getting started with StackQL

This tutorial goes from installation to live queries in three stages: an unauthenticated query against public GitHub data (no credentials needed), schema discovery with the meta-commands, and an authenticated cloud query against AWS.

## 1. Install

macOS (Homebrew):

```bash
brew install stackql
```

Linux:

```bash
curl -L https://bit.ly/stackql-zip -O && unzip stackql-zip
```

Windows, packages, and Docker images are covered on the [downloads page](https://stackql.io/downloads). Verify with `stackql --version`, then start the interactive shell:

```bash
stackql shell
```

## 2. First query - no credentials required

Pull the GitHub provider and query a public organization:

```sql
REGISTRY PULL github;

SELECT name, description, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

The rows that come back - including the `stackql` engine repository itself - are live GitHub API responses relationalized at query time. Nothing was synced, cached, or imported. (Unauthenticated GitHub access is limited to 60 requests per hour; [a personal access token](/ai/how-tos/authenticate-stackql-to-github) raises this.)

## 3. Discover the schema

The provider surface is self-describing - this is how you (or an AI agent) learn what is queryable without reading provider documentation:

```sql
SHOW PROVIDERS;                        -- installed providers
SHOW SERVICES IN github;               -- services in a provider
SHOW RESOURCES IN github.repos;        -- tables in a service
SHOW METHODS IN github.repos.repos;    -- operations + required parameters
DESCRIBE github.repos.repos;           -- columns
```

`SHOW METHODS` is the one to remember: its `RequiredParams` column tells you exactly what each operation needs in the `WHERE` clause, and which SQL verb (`SELECT`, `INSERT`, `UPDATE`, `DELETE`, `EXEC`) invokes it.

## 4. Query a cloud provider

Export credentials (AWS shown; [Azure](/ai/how-tos/authenticate-stackql-to-azure) and [Google Cloud](/ai/how-tos/authenticate-stackql-to-google-cloud) are equivalent), pull the provider, and query:

```bash
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
```

```sql
REGISTRY PULL aws;

SELECT instance_id, instance_type, launch_time
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

Aggregations fan out automatically - this runs as parallel per-region API calls:

```sql
SELECT region, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region IN ('us-east-1','us-west-2','eu-west-1')
GROUP BY region;
```

## 5. Where to go next

- **Mutations**: `SHOW INSERT INTO <resource>` generates creation templates; `INSERT`/`UPDATE`/`DELETE` operate on the same tables you query.
- **Server mode**: `stackql srv` exposes the PostgreSQL wire protocol for psql, DBeaver, BI tools, and pandas.
- **AI agents**: `stackql mcp` runs the built-in MCP server - see [How to use StackQL with AI agents](/ai/how-tos/use-stackql-with-ai-agents).
- **Python**: `pip install pystackql` for notebook and pipeline use.

## Related concepts

- [What is StackQL?](/ai/canonical-definitions/what-is-stackql) - the canonical definition
- [How to query GitHub repositories with StackQL](/ai/how-tos/query-github-repositories-with-stackql) - the first query, expanded
- [How to query AWS EC2 instances with StackQL](/ai/how-tos/query-aws-ec2-instances-with-stackql) - the cloud query, expanded
- [Common StackQL errors](/ai/troubleshooting/common-stackql-errors) - if something fails
- [StackQL FAQ](/ai/faqs/stackql-faq) - common questions
