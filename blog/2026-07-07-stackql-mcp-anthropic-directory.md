---
slug: stackql-mcp-anthropic-directory
title: "StackQL MCP server now available in the Anthropic MCP Directory"
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-mcp-server-featured-image.png"
description: "The StackQL MCP server is listed in the Anthropic MCP Directory. It is now discoverable and installable directly from Claude Desktop, with future releases flowing through automatically from GitHub."
keywords: [stackql, mcp, anthropic, claude, claude-desktop, connectors]
tags: [stackql, mcp, anthropic, claude]
---

The StackQL MCP server has been reviewed by Anthropic and is now listed in the [Anthropic MCP Directory](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql). StackQL is a member of the Claude Partner Network, and the directory listing makes the MCP server discoverable and installable directly from within Claude Desktop - no manual bundle download, no custom connector configuration.  

<!-- truncate -->

## Finding it in Claude Desktop

In Claude Desktop, go to __Settings__ -> __Connectors__ -> __Add__ -> __Browse Connectors__ and search for __"stackql"__:  

![stackql anthropic mcp server directory listing](/img/blog/stackql-mcp-browse-connectors.png)

You can also view the listing directly at [claude.ai/directory/stackql](https://claude.ai/directory/connectors/ant.dir.gh.stackql.stackql).  

Installation is one click. 

![stackql anthropic mcp server directory listing](/img/blog/stackql-mcp-install-connector.png)

The listing is backed by the signed MCPB bundle published with each StackQL MCP release on GitHub, so the version you install from the directory is the same artifact you would get from a GitHub release - reviewed once, then kept current automatically as new releases are published.  

## What the StackQL MCP server does

The StackQL MCP server exposes cloud and SaaS provider APIs to Claude as data sources accessed via SQL. Claude can discover providers, inspect resource schemas, and run queries against live control planes and data planes - AWS, Google, Azure, GitHub, Databricks, Snowflake, Confluent, Cloudflare, Okta, EntraID and others - using the same grammar across all of them.  

That makes the loop in a Claude conversation look like this: ask a question about your estate, Claude formulates a `SELECT` against the relevant provider, the query runs against the live API, and the answer reflects reality at the moment you asked - not a cached inventory or a state file. For provisioning and remediation workflows, `INSERT` and `DELETE` follow the same pattern.  

## More Information

Documentation for the StackQL MCP server is available at [StackQL MCP Tools](/docs/mcp), [StackQL MCP Server Usage](/docs/command-line-usage/mcp) and [StackQL MCP Server Installation](/docs/installing-stackql#installing-the-mcp-server).  Credential setup per provider, is available via the [__StackQL registry__](https://stackql.io/registry).
