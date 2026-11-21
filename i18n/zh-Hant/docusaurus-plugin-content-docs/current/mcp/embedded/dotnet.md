---
title: .NET / C#
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 內嵌式
  - dotnet
  - csharp
description: 以 sidecar 或 vendored 安裝套件的形式，在 .NET 應用程式中內嵌 StackQL MCP 伺服器
image: "/img/stackql-featured-image.png"
---

# .NET / C#

在 .NET 應用程式中內嵌 StackQL MCP 伺服器，可以是下載的 sidecar，也可以透過 vendoring 納入您的建置，成為獨立的可執行檔。用戶端是官方的 C# MCP SDK。

:::info
內嵌的二進位檔會在首次執行時下載並經過 sha256 驗證，或透過 vendoring 納入您的應用程式。請參閱[共同契約](/mcp/embedded#common-contract)。
:::

* * *

## 安裝 {#install}

```
dotnet add package StackQL.Mcp
```

選用的 `StackQL.Mcp.AgentFramework` 套件提供 Agent Framework 整合。需要 .NET 8 或更新版本。

* * *

## 快速入門 {#quick-start}

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

API 為 `StackqlMcp.CreateBuilder()`，搭配 `.WithMode()`、`.WithAuth()`、`.WithBinary()`、`.WithBundlePath()`、`.WithApproot()` 與 `.WithCommand()`，接著是 `.StartAsync()`、`.ListToolsAsync()` 與 `.CallToolAsync()`。

* * *

## 模式 {#modes}

建立器預設為 `StackqlMode.ReadOnly`。若要提升權限，請以 `StackqlMode.Safe`、`StackqlMode.DeleteSafe` 或 `StackqlMode.FullAccess` 呼叫 `.WithMode()`。權限提升必須明確指定：除非您選擇加入，否則伺服器不會啟用變更或生命週期工具。

* * *

## Vendoring / 內嵌 {#vendoring--embedding}

取得二進位檔的方式有兩種：

- **Sidecar（預設）**：平台安裝套件會在首次執行時下載、經過 sha256 驗證、快取在 `~/.stackql/mcp-server-bin/<version>/<platform-key>/`，並透過 stdio 啟動。
- **Vendored**：將安裝套件內嵌為建置資源，產生獨立的可執行檔：

```
dotnet build -p:StackqlVendorBundle=/path/to/stackql-mcp-windows-x64.mcpb
```

您也可以在執行階段使用 `STACKQL_MCP_BIN` 或 `STACKQL_MCP_BUNDLE` 環境變數覆寫二進位檔或安裝套件。

* * *

## 示範應用程式 {#demo-app}

**driftwatch** 是一個 .NET Worker Service，內嵌唯讀的 StackQL 伺服器，並依排程執行一組 SQL 漂移檢查（公開暴露、缺少標籤或授權、基準漂移），再將檢查結果張貼到 Teams 調適型卡片。

* * *

## 存放庫連結 {#repo-link}

[github.com/stackql/stackql-mcp-dotnet](https://github.com/stackql/stackql-mcp-dotnet)
