---
title: Privacy Policy
description: How StackQL handles cloud credentials and query data.
hide_table_of_contents: true
---

# Privacy Policy

*Last updated: 16 June 2026*  

This policy explains how the StackQL engine and the StackQL MCP server handle
credentials and data. It covers the `stackql` binary and its Model Context
Protocol (MCP) server (distributed as `.mcpb` bundles, npm, PyPI, Docker, and
via the GitHub release), operated by StackQL Studios Pty Ltd.

## Where StackQL runs

StackQL and its MCP server run locally - on your machine, your CI runner, or
your container. StackQL Studios does not operate a hosted service that receives
your queries or credentials. Requests go directly from the StackQL process you
run to the cloud and SaaS provider APIs you target.

## Cloud provider credentials

- Credentials are read locally, from environment variables or credential files
  that you nominate via StackQL's `--auth` configuration.
- They are used solely to authenticate requests to the provider APIs you query
  (for example AWS, Azure, Google, GitHub).
- They are never transmitted to StackQL Studios or to any third party, and are
  not written to StackQL's cache or registry.

## Query data

- Queries you run, and the results returned by provider APIs, are processed
  locally and returned to your MCP client. They are not sent to StackQL Studios.
- StackQL's audit log is disabled by default in the MCP server bundles. If you
  enable it, audit records are written locally under the application root
  (`~/.stackql` by default) and remain on your machine.

## Provider registry

To use a provider, StackQL pulls its definition from the StackQL provider
registry (`registry.stackql.app`). These are public OpenAPI-based provider
definitions. Pulling a provider makes a standard web request to the registry;
no credentials or query data are sent in that request.

## Telemetry

The StackQL binary and MCP server do not send usage telemetry or analytics to
StackQL Studios.

## Data retention

StackQL Studios retains no query data or credentials, because none is
transmitted to us. Locally cached provider definitions and any audit logs live
under your application root and are under your control.

## Contact

Questions about this policy: privacy@stackql.io

## Changes

We may update this policy; material changes will be reflected by the "Last
updated" date above.