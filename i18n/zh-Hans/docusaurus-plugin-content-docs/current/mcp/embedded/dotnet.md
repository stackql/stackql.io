---
title: .NET / C#
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 嵌入式
  - dotnet
  - csharp
description: 将 StackQL MCP 服务器以 sidecar 或 vendored bundle 的形式嵌入 .NET 应用
image: "/img/stackql-featured-image.png"
---

# .NET / C#

将 StackQL MCP 服务器嵌入 .NET 应用，可作为下载的 sidecar 运行，或以 vendoring 方式打包进你的构建，生成自包含的可执行文件。客户端是官方的 C# MCP SDK。

:::info
嵌入的二进制文件在首次运行时下载并经 sha256 校验，或以 vendoring 方式打包进你的应用。参阅[通用契约](/mcp/embedded#common-contract)。
:::

* * *

## 安装 {#install}

```
dotnet add package StackQL.Mcp
```

可选的 `StackQL.Mcp.AgentFramework` 包提供 Agent Framework 集成。需要 .NET 8 或更高版本。

* * *

## 快速入门 {#quick-start}

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

API 为 `StackqlMcp.CreateBuilder()`，搭配 `.WithMode()`、`.WithAuth()`、`.WithBinary()`、`.WithBundlePath()`、`.WithApproot()` 和 `.WithCommand()`，然后是 `.StartAsync()`、`.ListToolsAsync()` 和 `.CallToolAsync()`。

* * *

## 模式 {#modes}

构建器默认使用 `StackqlMode.ReadOnly`。调用 `.WithMode()` 并传入 `StackqlMode.Safe`、`StackqlMode.DeleteSafe` 或 `StackqlMode.FullAccess` 即可提升权限。权限提升必须显式进行：除非你明确选择启用，否则服务器不会启用变更或生命周期工具。

* * *

## Vendoring / 嵌入 {#vendoring--embedding}

获取二进制文件有两种方式：

- **Sidecar（默认）**：平台 bundle 在首次运行时下载，经 sha256 校验后缓存到 `~/.stackql/mcp-server-bin/<version>/<platform-key>/`，并通过 stdio 启动。
- **Vendored**：将 bundle 作为构建资源嵌入，生成自包含的可执行文件：

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

也可以在运行时通过 `STACKQL_MCP_BIN` 或 `STACKQL_MCP_BUNDLE` 环境变量覆盖二进制文件或 bundle。

* * *

## 演示应用 {#demo-app}

**driftwatch** 是一个 .NET Worker Service，它嵌入只读的 StackQL 服务器，定时运行一组 SQL 漂移检查（公开暴露、缺少标签或许可证、基线漂移），并将检查结果发布到 Teams Adaptive Card。

* * *

## 仓库链接 {#repo-link}

[github.com/stackql/stackql-mcp-dotnet](https://github.com/stackql/stackql-mcp-dotnet)
