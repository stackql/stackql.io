---
title: .NET / C#
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - embedded
  - dotnet
  - csharp
description: Embed the StackQL MCP server in a .NET application as a sidecar or vendored bundle
image: "/img/stackql-featured-image.png"
---

# .NET / C#

Embed the StackQL MCP server in a .NET application, either as a downloaded sidecar or vendored into your build as a self-contained executable. The client is the official C# MCP SDK.

:::info
The embedded binary is downloaded and sha256-verified on first run, or vendored into your app. See the [common contract](/docs/mcp/embedded#common-contract).
:::

* * *

## Install

```
dotnet add package StackQL.Mcp
```

An optional `StackQL.Mcp.AgentFramework` package adds Agent Framework integration. Requires .NET 8 or later.

* * *

## Quick start

```csharp
using StackQL.Mcp;

await using var server = await StackqlMcp.CreateBuilder()
    .WithMode(StackqlMode.ReadOnly)
    .WithAuth("github", "null_auth")
    .StartAsync();

var tools = await server.ListToolsAsync();
var result = await server.CallToolAsync("list_services",
    new() { ["provider"] = "github", ["row_limit"] = 5 });
```

The API is `StackqlMcp.CreateBuilder()` with `.WithMode()`, `.WithAuth()`, `.WithBinary()`, `.WithBundlePath()`, `.WithApproot()`, and `.WithCommand()`, then `.StartAsync()`, `.ListToolsAsync()`, and `.CallToolAsync()`.

* * *

## Modes

The builder defaults to `StackqlMode.ReadOnly`. Call `.WithMode()` with `StackqlMode.Safe`, `StackqlMode.DeleteSafe`, or `StackqlMode.FullAccess` to escalate. Escalation is explicit - the server will not enable mutation or lifecycle tools unless you opt in.

* * *

## Vendoring / embedding

Two ways to source the binary:

- **Sidecar (default)** - the platform bundle is downloaded on first run, sha256-verified, cached at `~/.stackql/mcp-server-bin/<version>/<platform-key>/`, and spawned over stdio.
- **Vendored** - embed the bundle as a build resource to produce a self-contained executable:

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

You can also override the binary or bundle at runtime with the `STACKQL_MCP_BIN` or `STACKQL_MCP_BUNDLE` environment variables.

* * *

## Demo app

**driftwatch** is a .NET Worker Service that embeds the read-only StackQL server and, on a schedule, runs a suite of SQL drift checks (public exposure, missing tags or licenses, baseline drift) and posts the findings to a Teams Adaptive Card.

* * *

## Repo link

[github.com/stackql/stackql-mcp-dotnet](https://github.com/stackql/stackql-mcp-dotnet)
