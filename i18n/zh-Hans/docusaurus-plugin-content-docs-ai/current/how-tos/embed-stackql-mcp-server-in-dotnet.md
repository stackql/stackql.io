---
title: 如何在 .NET 应用中嵌入 StackQL MCP 服务器
description: 将 StackQL MCP 服务器以 sidecar 或 vendored bundle 的形式嵌入 .NET / C# 应用，使 worker 服务或智能体无需运行时依赖即可通过 SQL 执行云查询和漂移检查。
keywords: [stackql, mcp, dotnet, csharp, .net, nuget, 嵌入式 MCP 服务器, model context protocol, c# mcp sdk, modelcontextprotocol.core, worker 服务, driftwatch]
proficiencyLevel: Intermediate
faq:
  - question: 如何为 .NET 应用添加嵌入式 StackQL MCP 服务器？
    answer: 运行 `dotnet add package StackQL.Mcp`，然后调用 `await StackqlMcp.CreateBuilder().WithMode(StackqlMode.ReadOnly).StartAsync()`。客户端是官方的 C# MCP SDK。默认情况下，平台 bundle 在首次运行时下载，经 sha256 校验后缓存到 ~/.stackql/mcp-server-bin/。需要 .NET 8 或更高版本。
  - question: 能否发布嵌入了 StackQL 的自包含 .NET 可执行文件？
    answer: 可以。使用 `dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb` 将 bundle 作为构建资源以 vendoring 方式打包，这会嵌入二进制文件，生成自包含的可执行文件。也可以在运行时通过 STACKQL_MCP_BIN 或 STACKQL_MCP_BUNDLE 环境变量覆盖二进制文件或 bundle。
  - question: StackQL MCP 库需要哪个 .NET 版本？
    answer: .NET 8 或更高版本。已发布的包 StackQL.Mcp 和 StackQL.Mcp.AgentFramework 同时以 net8.0 和 net9.0 为目标，.NET 8 这一最低要求由官方的 C# MCP SDK（ModelContextProtocol.Core）决定。
  - question: 如何在 .NET 中让嵌入式 StackQL MCP 服务器保持只读？
    answer: 在构建器上调用 .WithMode(StackqlMode.ReadOnly)，这是默认值。ReadOnly 拒绝 INSERT、UPDATE、DELETE 和 EXEC。仅当应用需要预配资源时，才提升到 StackqlMode.Safe、StackqlMode.DeleteSafe 或 StackqlMode.FullAccess，权限提升始终需要显式进行。
---

# 如何在 .NET 应用中嵌入 StackQL MCP 服务器

StackQL 的 MCP 服务器可以在 .NET 应用内运行，无需单独安装，也没有运行时依赖。`StackQL.Mcp` 包在官方 C# MCP SDK 客户端的背后通过 stdio 启动已签名的 StackQL 二进制文件，因此 worker 服务或智能体可以获得一个受治理、自描述的 SQL 接口，用于访问 StackQL 注册表中的每个云和 SaaS Provider。这就是 **driftwatch** 所采用的模式，它是一个定时执行漂移检查的 worker 服务。

## 步骤 {#steps}

1. **添加包：**

```
dotnet add package StackQL.Mcp
```

可选的 `StackQL.Mcp.AgentFramework` 包提供 Agent Framework 集成。需要 .NET 8 或更高版本（这些包同时以 `net8.0` 和 `net9.0` 为目标）。

2. **启动服务器并调用工具。** 构建器默认使用 `StackqlMode.ReadOnly`：

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

构建器还提供 `.WithBinary()`、`.WithBundlePath()`、`.WithApproot()` 和 `.WithCommand()`。

3. **选择模式。** `.WithMode()` 默认为 `StackqlMode.ReadOnly`。仅当应用需要预配资源时，才设置 `StackqlMode.Safe`、`StackqlMode.DeleteSafe` 或 `StackqlMode.FullAccess`。权限提升必须显式进行。

## 发布自包含的可执行文件 {#ship-a-self-contained-executable}

获取二进制文件有两种方式：

- **Sidecar（默认）**：平台 bundle 在首次运行时下载，经 sha256 校验后缓存到 `~/.stackql/mcp-server-bin/<version>/<platform-key>/`，并通过 stdio 启动。
- **Vendored**：将 bundle 作为构建资源嵌入：

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

可在运行时通过 `STACKQL_MCP_BIN` 或 `STACKQL_MCP_BUNDLE` 环境变量覆盖二进制文件或 bundle。

## 演示应用 {#demo-app}

**driftwatch** 是一个 .NET Worker Service，它嵌入只读的 StackQL 服务器，定时运行一组 SQL 漂移检查（公开暴露、缺少标签或许可证、基线漂移），并将检查结果发布到 Teams Adaptive Card。

## 相关概念 {#related-concepts}

- [嵌入式 MCP：.NET / C# 参考](/mcp/embedded/dotnet)：安装、API 和 vendoring
- [如何将 StackQL 与 AI 智能体配合使用](/ai/how-tos/use-stackql-with-ai-agents)：MCP 模型和安全模式
- [StackQL MCP 架构](/ai/architecture/stackql-mcp-architecture)：传输方式、模式和审计的内部机制
- [什么是智能体基础设施？](/ai/canonical-definitions/what-is-agentic-infrastructure)：由此实现的模式
