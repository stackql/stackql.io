---
title: 如何在 Gleam 應用程式中內嵌 StackQL MCP 伺服器
description: 在以 Erlang/BEAM 為目標的 Gleam 應用程式中內嵌由 OTP 監督的 StackQL MCP 伺服器，讓 BEAM 服務無需執行階段相依性即可透過 SQL 執行雲端查詢。
keywords: [stackql, mcp, gleam, erlang, beam, otp, 監督樹, 內嵌式 mcp 伺服器, model context protocol, 代理式基礎架構, pipewatch]
proficiencyLevel: Intermediate
faq:
  - question: 如何將內嵌式 StackQL MCP 伺服器加入 Gleam 應用程式？
    answer: 執行 `gleam add stackql_mcp`，然後以從 `stackql_mcp.default_config()` 建立的 Config 呼叫 `stackql_mcp.start(...)`。此程式庫僅以 Erlang/BEAM 為目標（不含 JavaScript 目標），並透過 child_spec() 將伺服器公開為 OTP 子處理程序。啟動時，它會將已簽署的二進位檔下載到位於 ~/.stackql/mcp-server-bin/ 的共用快取，並進行 sha256 驗證。
  - question: Gleam 程式庫是否與 OTP 監督機制整合？
    answer: 是。伺服器透過 child_spec() 公開為 OTP 子處理程序，因此可以放在您自己的監督樹中；此外還提供 supervised_list_tools() 與 supervised_call_tool() 變體，用於在監督器下呼叫工具。此程式庫僅以 Erlang/BEAM 執行階段為目標。
  - question: 如何在 Gleam 中讓內嵌式 StackQL MCP 伺服器保持唯讀？
    answer: "模式設定於 Config.mode，預設為 read_only，它會拒絕 INSERT、UPDATE、DELETE 與 EXEC。若要提升至 safe、delete_safe 或 full_access，請使用 `stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)` 建立 Config；權限提升一律需明確指定。"
  - question: Gleam 程式庫如何解析 StackQL 二進位檔？
    answer: 先使用明確指定的 Config.binary，其次是 STACKQL_MCP_BIN 環境變數，然後是位於 ~/.stackql/mcp-server-bin/ 的共用快取。對於離線或實體隔離的部署，請將 STACKQL_MCP_BUNDLE 設為指向本機安裝套件。下載的安裝套件會以內建的 sha256 釘選值進行驗證。
---

# 如何在 Gleam 應用程式中內嵌 StackQL MCP 伺服器

StackQL 的 MCP 伺服器可以在以 Erlang/BEAM 為目標的 Gleam 應用程式內執行，無需另外安裝，也沒有執行階段相依性。`stackql_mcp` 程式庫會透過 stdio 啟動已簽署的 StackQL 二進位檔，並與 OTP 監督機制整合，讓 BEAM 服務取得受治理、可自我描述的 SQL 介面，存取 StackQL 登錄庫中的每個雲端與 SaaS Provider。伺服器透過 `child_spec()` 公開為 OTP 子處理程序，因此可以放在您自己的監督樹中。

## 步驟 {#steps}

1. **新增程式庫**：

```
gleam add stackql_mcp
```

僅以 Erlang/BEAM 為目標（不含 JavaScript 目標）。

2. **啟動伺服器並列出工具**。`Config.mode` 預設為 `read_only`：

```gleam
import gleam/io
import gleam/int
import gleam/list
import envoy
import stackql_mcp

pub fn main() {
  let config =
    stackql_mcp.Config(
      ..stackql_mcp.default_config(),
      auth: [stackql_mcp.auth_for("github", "null_auth")],
    )

  let assert Ok(server) =
    stackql_mcp.start(
      config: config,
      home: "/home/u",
      os: "linux",
      arch: "x86_64",
      getenv: envoy.get,
    )

  let assert Ok(tools) = stackql_mcp.list_tools(server)
  io.println(int.to_string(list.length(tools)) <> " tools available")

  stackql_mcp.stop(server)
}
```

使用 `child_spec()` 將伺服器放在監督器下，並使用 `supervised_list_tools()` 與 `supervised_call_tool()` 進行受監督的呼叫。

3. **選擇模式**。`Config.mode` 預設為 `read_only`。建立 `stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)` 即可提升至 `safe`、`delete_safe` 或 `full_access`。權限提升需明確指定。

## 二進位檔來源 {#binary-sourcing}

啟動時，程式庫會下載該平台的 StackQL 安裝套件，以內建的 sha256 釘選值進行驗證，並快取於程式庫系列共用的快取中。解析優先順序：明確指定的 `Config.binary`，其次是 `STACKQL_MCP_BIN`，然後是位於 `~/.stackql/mcp-server-bin/` 的共用快取。對於離線或實體隔離的部署，請將 `STACKQL_MCP_BUNDLE` 設為指向本機安裝套件。

## 示範應用程式 {#demo-app}

**pipewatch** 是此程式庫規劃中的示範應用程式。它已列入開發藍圖，但尚未實作。

## 相關概念 {#related-concepts}

- [內嵌式 MCP：Gleam 參考](/mcp/embedded/gleam)：安裝、API 與二進位檔來源
- [如何搭配 AI 代理程式使用 StackQL](/ai/how-tos/use-stackql-with-ai-agents)：MCP 模型與安全模式
- [StackQL MCP 架構](/ai/architecture/stackql-mcp-architecture)：傳輸方式、模式與稽核內部機制
- [什麼是代理式基礎架構？](/ai/canonical-definitions/what-is-agentic-infrastructure)：此做法促成的模式
