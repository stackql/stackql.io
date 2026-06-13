---
title: How to embed the StackQL MCP server in a .NET application
description: Embed the StackQL MCP server in a .NET / C# app as a sidecar or vendored bundle, so a worker service or agent can run cloud queries and drift checks over SQL with no runtime dependency.
keywords: [stackql, mcp, dotnet, csharp, .net, nuget, embedded mcp server, model context protocol, c# mcp sdk, modelcontextprotocol.core, worker service, driftwatch]
proficiencyLevel: Intermediate
faq:
  - question: How do I add an embedded StackQL MCP server to a .NET app?
    answer: Run `dotnet add package StackQL.Mcp`, then call `await StackqlMcp.CreateBuilder().WithMode(StackqlMode.ReadOnly).StartAsync()`. The client is the official C# MCP SDK. By default the platform bundle is downloaded, sha256-verified, and cached at ~/.stackql/mcp-server-bin/ on first run. Requires .NET 8 or later.
  - question: Can I ship a self-contained .NET executable with StackQL embedded?
    answer: Yes. Vendor the bundle as a build resource with `dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb`, which embeds the binary for a self-contained executable. You can also override the binary or bundle at runtime with the STACKQL_MCP_BIN or STACKQL_MCP_BUNDLE environment variables.
  - question: What .NET version does the StackQL MCP library require?
    answer: .NET 8 or later. The shipping packages StackQL.Mcp and StackQL.Mcp.AgentFramework multi-target net8.0 and net9.0, and the .NET 8 floor is set by the official C# MCP SDK, ModelContextProtocol.Core.
  - question: How do I keep an embedded StackQL MCP server read-only in .NET?
    answer: Call .WithMode(StackqlMode.ReadOnly) on the builder, which is the default. ReadOnly refuses INSERT, UPDATE, DELETE, and EXEC. Escalate to StackqlMode.Safe, StackqlMode.DeleteSafe, or StackqlMode.FullAccess only when the app needs to provision - escalation is always explicit.
---

# How to embed the StackQL MCP server in a .NET application

StackQL's MCP server can run inside a .NET application with no separate install and no runtime dependency. The `StackQL.Mcp` package spawns the signed StackQL binary over stdio behind the official C# MCP SDK client, so a worker service or agent gets a governed, self-describing SQL interface to every cloud and SaaS provider in the StackQL registry. This is the pattern behind **driftwatch**, a scheduled drift-check worker service.

## Steps

1. **Add the package:**

```
dotnet add package StackQL.Mcp
```

An optional `StackQL.Mcp.AgentFramework` package adds Agent Framework integration. Requires .NET 8 or later (the packages multi-target `net8.0` and `net9.0`).

2. **Start the server and call a tool.** The builder defaults to `StackqlMode.ReadOnly`:

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

The builder also exposes `.WithBinary()`, `.WithBundlePath()`, `.WithApproot()`, and `.WithCommand()`.

3. **Choose a mode.** `.WithMode()` defaults to `StackqlMode.ReadOnly`. Set `StackqlMode.Safe`, `StackqlMode.DeleteSafe`, or `StackqlMode.FullAccess` only when the app needs to provision. Escalation is explicit.

## Ship a self-contained executable

Two ways to source the binary:

- **Sidecar (default)** - the platform bundle is downloaded on first run, sha256-verified, cached at `~/.stackql/mcp-server-bin/<version>/<platform-key>/`, and spawned over stdio.
- **Vendored** - embed the bundle as a build resource:

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

Override the binary or bundle at runtime with the `STACKQL_MCP_BIN` or `STACKQL_MCP_BUNDLE` environment variables.

## Demo app

**driftwatch** is a .NET Worker Service that embeds the read-only StackQL server and, on a schedule, runs a suite of SQL drift checks (public exposure, missing tags or licenses, baseline drift) and posts the findings to a Teams Adaptive Card.

## Related concepts

- [Embedded MCP: .NET / C# reference](/docs/mcp/embedded/dotnet) - install, API, and vendoring
- [How to use StackQL with AI agents](/ai/how-tos/use-stackql-with-ai-agents) - the MCP model and safety modes
- [StackQL MCP Architecture](/ai/architecture/stackql-mcp-architecture) - transports, modes, and audit internals
- [What is Agentic Infrastructure?](/ai/canonical-definitions/what-is-agentic-infrastructure) - the pattern this enables
