---
slug: run-the-stackql-mcp-server-anywhere
title: Run the StackQL MCP Server Anywhere Your Agent Does
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-mcp-server-featured-image.png"
description: The StackQL MCP server now ships via npm, PyPI, Docker, GitHub Actions, prebuilt Claude Desktop bundles, and the Official MCP Registry - one server, every runtime, signed and SHA-pinned.
keywords: [stackql, mcp, model context protocol, ai agents, claude, npm, pypi, docker, github actions, mcp registry, infrastructure-as-code, cloud, agentic ci]
tags: [stackql, mcp, model context protocol, ai agents, claude, infrastructure-as-code, ai]
---

The [__StackQL MCP server__](/docs/command-line-usage/mcp) is now available through every runtime an agent is likely to live in: prebuilt Claude Desktop bundles, [__npm__](https://www.npmjs.com/package/@stackql/mcp-server), [__PyPI__](https://pypi.org/project/stackql-mcp-server/), [__Docker__](https://hub.docker.com/r/stackql/stackql-mcp), a [__GitHub Action__](https://github.com/marketplace/actions/setup-stackql-mcp-server), and the [__Official MCP Registry__](https://registry.modelcontextprotocol.io/v0/servers?search=stackql). It is the same server in each case - one binary, pulled and launched the way your environment prefers.

## What the StackQL MCP server is

StackQL exposes cloud and SaaS providers - AWS, Google Cloud, Azure, GitHub, Kubernetes, Snowflake, Databricks and more - as a single SQL surface. The MCP server puts that surface in front of an AI agent: the agent discovers providers, services, resources and methods, then runs `SELECT` queries to read state and (when you allow it) `INSERT` / `UPDATE` / `DELETE` to change it. Reads and writes are gated by a [server mode](/docs/command-line-usage/mcp#server-modes) and recorded to an [audit log](/docs/command-line-usage/mcp#audit-log), so "what the agent did" is always answerable.

For background on the protocol itself, see the original [__StackQL MCP Server Now Available__](/blog/stackql-mcp-server-now-available) post and the [__MCP command reference__](/docs/command-line-usage/mcp).

## One server, every runtime

Every channel runs the same `stackql` binary. Pick the one that matches your client:

| Channel | Get it | Best for |
|---|---|---|
| Claude Desktop bundle | `stackql-mcp-<platform>.mcpb` from the [release page](https://github.com/stackql/stackql/releases/latest) | One-click install, no separate StackQL on PATH |
| npm | `npx -y @stackql/mcp-server` | Node environments, no global install |
| PyPI | `uvx stackql-mcp-server` or `pip install stackql-mcp-server` | Python environments |
| Docker | `docker run -i --rm stackql/stackql-mcp` | Containerised / isolated runtimes (amd64 + arm64) |
| GitHub Action | `stackql/setup-stackql-mcp@v1` | CI and agentic workflows |
| MCP Registry | `io.github.stackql/stackql-mcp` | Directory-driven discovery and install |

A typical stdio client config is three lines. For npx:

```json
{ "mcpServers": { "stackql": { "command": "npx", "args": ["-y", "@stackql/mcp-server"] } } }
```

Swap `npx` for `uvx stackql-mcp-server` or `docker run -i --rm stackql/stackql-mcp` and you have the Python or Docker form. The npm and PyPI launchers download the signed `stackql` binary on first run, verify its checksum, and share a single cache. The full matrix - including the manual `claude_desktop_config.json` form for an existing binary - is in [__Installing the MCP server__](/docs/installing-stackql#installing-the-mcp-server).

## The approvable MCP server

Letting an agent touch your cloud is a trust decision, so the supply chain is built to be checkable:

- The embedded `stackql` binary is Authenticode-signed (Windows) and Apple-notarised (macOS).
- Every `.mcpb` bundle ships with a published SHA-256 checksum on the release page.
- The npm and PyPI launchers verify the downloaded binary's SHA-256 before first use.
- The [__MCP Registry__](https://registry.modelcontextprotocol.io/v0/servers?search=stackql) entry attests the per-platform hashes, so a directory or marketplace can confirm what it is shipping.

On top of the supply chain, the server defaults to `mode: safe` - reads run freely, mutations and lifecycle operations need approval through the MCP elicitation flow. Pin `read_only` for inventory agents that should never write, or `full_access` for trusted automation. See [__Server modes__](/docs/command-line-usage/mcp#server-modes).

## A worked example: cloud audit in CI

The GitHub Action is where the multi-vector story pays off. [`stackql/setup-stackql-mcp@v1`](https://github.com/marketplace/actions/setup-stackql-mcp-server) installs the binary and writes an MCP config (defaulting to `read_only`), and [`anthropics/claude-code-action`](https://github.com/anthropics/claude-code-action) consumes it through `claude_args`. The result is an agent that audits your AWS account on every run and files an issue with the SQL it used as evidence:

```yaml
- id: stackql
  uses: stackql/setup-stackql-mcp@v1
  env:
    AWS_ACCESS_KEY_ID: ${{ secrets.AWS_ACCESS_KEY_ID }}
    AWS_SECRET_ACCESS_KEY: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
  with:
    mode: read_only

- uses: anthropics/claude-code-action@v1
  with:
    anthropic_api_key: ${{ secrets.ANTHROPIC_API_KEY }}
    prompt: |
      Using the stackql tools, audit our AWS account for: S3 buckets without
      encryption or with public access, security groups open to 0.0.0.0/0 on
      sensitive ports, and IAM users without MFA. Open a GitHub issue
      "Cloud audit <date>" summarising findings WITH the SQL you ran as
      evidence. If nothing is found, do not open an issue.
    claude_args: |
      --mcp-config ${{ steps.stackql.outputs.mcp-config-file }}
      --allowedTools 'mcp__stackql__*'
```

Because the config is pinned to `read_only`, the audit can read everything and change nothing - the safety contract is enforced by the server, not by trust in the prompt. The [action README](https://github.com/stackql/setup-stackql-mcp) has more recipes, including cost estimates on a pull request and a credential-free GitHub inventory.

## What the agent actually sees

Under the hood the agent works the StackQL hierarchy with the same tools whatever the runtime. Pulling the GitHub provider and listing its services looks like this:

```
> pull_provider {"provider": "github"}
github provider, version 'v26.05.00393' successfully installed

> list_services {"provider": "github"}
actions, activity, apps, billing, checks, code_scanning, codespaces,
copilot, dependabot, gists, git, issues, orgs, packages, projects,
pulls, repos, search, secret_scanning, teams, users, ...
```

From there the agent can call `list_resources` and `list_methods` to discover the required `WHERE` parameters, then `run_select_query` to answer a question like "how many public repositories does the stackql org have?" - all without anyone hand-writing SQL.

## Get started

- Install docs for every channel: [__Installing the MCP server__](/docs/installing-stackql#installing-the-mcp-server)
- Command reference, modes and audit log: [__stackql mcp__](/docs/command-line-usage/mcp)
- Registry entry: [__io.github.stackql/stackql-mcp__](https://registry.modelcontextprotocol.io/v0/servers?search=stackql)
- Source and releases: [__github.com/stackql/stackql__](https://github.com/stackql/stackql)

⭐ Star us on [__GitHub__](https://github.com/stackql/stackql) and tell us what your agents build.
