---
title: 如何在 .NET 應用程式中內嵌 StackQL MCP 伺服器
description: 以 sidecar 或 vendored 安裝套件的方式將 StackQL MCP 伺服器內嵌至 .NET / C# 應用程式，讓 worker 服務或代理程式無需執行階段相依性即可透過 SQL 執行雲端查詢與漂移檢查。
keywords: [stackql, mcp, dotnet, csharp, .net, nuget, 內嵌式 mcp 伺服器, model context protocol, c# mcp sdk, modelcontextprotocol.core, worker 服務, driftwatch]
proficiencyLevel: Intermediate
faq:
  - question: 如何將內嵌式 StackQL MCP 伺服器加入 .NET 應用程式？
    answer: 執行 `dotnet add package StackQL.Mcp`，然後呼叫 `await StackqlMcp.CreateBuilder().WithMode(StackqlMode.ReadOnly).StartAsync()`。用戶端是官方的 C# MCP SDK。預設會在首次執行時下載平台安裝套件、進行 sha256 驗證，並快取於 ~/.stackql/mcp-server-bin/。需要 .NET 8 或更新版本。
  - question: 能否發行內嵌 StackQL 的獨立式 .NET 可執行檔？
    answer: 可以。使用 `dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb` 將安裝套件以建置資源的形式 vendor 進來，這會內嵌二進位檔，產生獨立式可執行檔。您也可以在執行階段使用 STACKQL_MCP_BIN 或 STACKQL_MCP_BUNDLE 環境變數覆寫二進位檔或安裝套件。
  - question: StackQL MCP 程式庫需要哪個 .NET 版本？
    answer: .NET 8 或更新版本。已發行的套件 StackQL.Mcp 與 StackQL.Mcp.AgentFramework 以 net8.0 和 net9.0 為多目標，而 .NET 8 的最低版本要求來自官方 C# MCP SDK ModelContextProtocol.Core。
  - question: 如何在 .NET 中讓內嵌式 StackQL MCP 伺服器保持唯讀？
    answer: 在建立器上呼叫 .WithMode(StackqlMode.ReadOnly)，這是預設值。ReadOnly 會拒絕 INSERT、UPDATE、DELETE 與 EXEC。只有在應用程式需要佈建時，才提升至 StackqlMode.Safe、StackqlMode.DeleteSafe 或 StackqlMode.FullAccess；權限提升一律需明確指定。
---

# 如何在 .NET 應用程式中內嵌 StackQL MCP 伺服器

StackQL 的 MCP 伺服器可以在 .NET 應用程式內執行，無需另外安裝，也沒有執行階段相依性。`StackQL.Mcp` 套件會在官方 C# MCP SDK 用戶端背後，透過 stdio 啟動已簽署的 StackQL 二進位檔，讓 worker 服務或代理程式取得受治理、可自我描述的 SQL 介面，存取 StackQL 登錄庫中的每個雲端與 SaaS Provider。這正是 **driftwatch** 背後的模式，它是一個排程執行漂移檢查的 worker 服務。

## 步驟 {#steps}

1. **新增套件**：

```
dotnet add package StackQL.Mcp
```

另有選用的 `StackQL.Mcp.AgentFramework` 套件，可提供 Agent Framework 整合。需要 .NET 8 或更新版本（套件以 `net8.0` 和 `net9.0` 為多目標）。

2. **啟動伺服器並呼叫工具**。建立器預設為 `StackqlMode.ReadOnly`：

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

建立器還提供 `.WithBinary()`、`.WithBundlePath()`、`.WithApproot()` 與 `.WithCommand()`。

3. **選擇模式**。`.WithMode()` 預設為 `StackqlMode.ReadOnly`。只有在應用程式需要佈建時，才設定 `StackqlMode.Safe`、`StackqlMode.DeleteSafe` 或 `StackqlMode.FullAccess`。權限提升需明確指定。

## 發行獨立式可執行檔 {#ship-a-self-contained-executable}

取得二進位檔的方式有兩種：

- **Sidecar（預設）**：首次執行時下載平台安裝套件，經 sha256 驗證後快取於 `~/.stackql/mcp-server-bin/<version>/<platform-key>/`，並透過 stdio 啟動。
- **Vendored**：將安裝套件內嵌為建置資源：

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

在執行階段使用 `STACKQL_MCP_BIN` 或 `STACKQL_MCP_BUNDLE` 環境變數覆寫二進位檔或安裝套件。

## 示範應用程式 {#demo-app}

**driftwatch** 是一個 .NET Worker Service，它內嵌唯讀的 StackQL 伺服器，並依排程執行一系列 SQL 漂移檢查（公開暴露、缺少標籤或授權、基準漂移），再將檢查結果張貼到 Teams Adaptive Card。

## 相關概念 {#related-concepts}

- [內嵌式 MCP：.NET / C# 參考](/mcp/embedded/dotnet)：安裝、API 與 vendoring
- [如何搭配 AI 代理程式使用 StackQL](/ai/how-tos/use-stackql-with-ai-agents)：MCP 模型與安全模式
- [StackQL MCP 架構](/ai/architecture/stackql-mcp-architecture)：傳輸方式、模式與稽核內部機制
- [什麼是代理式基礎架構？](/ai/canonical-definitions/what-is-agentic-infrastructure)：此做法促成的模式
