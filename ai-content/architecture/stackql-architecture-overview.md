---
title: StackQL Architecture Overview
description: StackQL is a Go-based SQL engine with three entry points (CLI/shell, PostgreSQL wire protocol, MCP server), a Yacc-based SQL parser, a query engine that orchestrates live provider API calls, and an embedded SQLite (or external PostgreSQL) backend for relational post-processing.
keywords: [stackql architecture, query engine, sql parser, provider sdk, sqlite, postgresql, registry]
proficiencyLevel: Expert
faq:
  - question: Where does the actual SQL processing happen?
    answer: In two stages. The query engine pushes whatever it can to the provider API (routing parameters, supported predicates), then materializes API responses into the SQL backend - embedded SQLite by default, optionally an external PostgreSQL - where joins, aggregates, sorting, and expressions are executed.
  - question: How does StackQL know how to call each provider's API?
    answer: From provider definitions in the StackQL Registry - versioned, extended-OpenAPI specifications describing each provider's services, resources, methods, parameters, authentication, and pagination. Definitions are pulled on demand with REGISTRY PULL and interpreted by the provider SDK (any-sdk); no provider-specific code is compiled into the engine.
  - question: Is StackQL a single binary?
    answer: Yes. The engine, parser, provider SDK, embedded SQL backend, PostgreSQL wire server, and MCP server ship in one Go binary for macOS, Linux, and Windows.
---

# StackQL Architecture Overview

StackQL is a single Go binary implementing a SQL engine over cloud and SaaS provider APIs. Architecturally it is four layers: entry points that accept SQL, a parser and query engine that plan it, a provider SDK that executes live API calls described by declarative provider specs, and a SQL backend that performs relational operations on the materialized results.

## Components

**Entry points.** Three ways in, all converging on the same parser:

- **CLI / shell** (`stackql shell`, `stackql exec`) for interactive and batch use.
- **PostgreSQL wire protocol server** (`stackql srv`, built on psql-wire) so any Postgres client - psql, DBeaver, BI tools, pandas - can connect.
- **MCP server** (`stackql mcp`) exposing discovery, validation, and gated execution tools to AI agents (detailed in [StackQL MCP Architecture](/ai/architecture/stackql-mcp-architecture)).

**SQL parser.** A Yacc-based grammar (`stackql-parser`) producing an AST. The dialect covers the standard verbs plus StackQL meta-commands (`SHOW`, `DESCRIBE`, `REGISTRY`) and the `EXEC` verb for non-CRUD lifecycle operations.

**Query engine.** Plans the AST: decides what is pushed down to the provider API (routing parameters, supported predicates) versus what is processed locally, coordinates API execution - including parallel fan-out when a query spans regions or multiple calls - and aggregates results.

**Provider SDK (any-sdk).** The generic API client. It loads provider specifications from the registry, handles per-provider authentication, builds HTTP requests from query parameters, walks paginated responses regardless of pagination scheme (cursor, offset, token), and relationalizes JSON responses into rows.

**SQL backend.** Materialized API results land in an embedded SQLite database by default (an external PostgreSQL can be configured instead), where joins - including cross-provider joins - aggregates, sorting, subqueries, and expressions execute with full SQL semantics.

## The defining design decision: providers as data

StackQL has no compiled, provider-specific plugins. A provider is a versioned document set - an extended OpenAPI specification in the open [StackQL provider registry](https://github.com/stackql/stackql-provider-registry) - mapping API operations to resources, SQL verbs, and parameters. `REGISTRY PULL aws` downloads the spec; the SDK interprets it at runtime.

Consequences: provider coverage tracks the documented API surface rather than plugin development effort; data plane and lifecycle operations are mappable the same way control plane CRUD is; and the entire surface is introspectable at runtime (`SHOW PROVIDERS`, `SHOW METHODS IN aws.ec2.instances`, `DESCRIBE github.repos.repos`) - the property that makes StackQL self-describing for AI agents.

## Query execution flow

For `SELECT instance_id, instance_type FROM aws.ec2.instances WHERE region = 'us-east-1';`:

1. The parser produces the AST; the engine resolves `aws.ec2.instances` against the provider spec and selects the `describe` access method, with `region` satisfying its required parameter.
2. The SDK authenticates (from environment variables), issues the EC2 API call(s), and walks pagination.
3. Responses are flattened to rows and materialized into the SQL backend.
4. The backend applies the projection and any residual relational work; results return to the caller in the requested format.

A `region IN (...)` predicate at step 2 becomes parallel per-region calls; a join at step 4 operates across whatever providers contributed tables.

## Related concepts

- [StackQL MCP Architecture](/ai/architecture/stackql-mcp-architecture) - the agent-facing entry point in depth
- [What is SQL for APIs?](/ai/canonical-definitions/what-is-sql-for-apis) - the mapping the provider specs encode
- [Why SQL is a Strong Interface for Cloud APIs](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis) - the rationale for the dialect
- [What is Queryable Infrastructure?](/ai/canonical-definitions/what-is-queryable-infrastructure) - the operating model this architecture serves
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe) - an FDW-based architecture contrasted
