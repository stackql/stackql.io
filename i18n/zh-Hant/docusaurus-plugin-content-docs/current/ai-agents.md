---
slug: /ai-agents
title: AI 代理程式
hide_title: false
keywords:
  - stackql
  - AI 代理程式
  - mcp
  - model context protocol
  - 代理式
description: 透過 MCP 伺服器、工具、內嵌式整合與 SQL 查詢，使用 StackQL 將 AI 代理程式連線到雲端與 SaaS 服務。
image: "/img/stackql-featured-image.png"
---

StackQL 為 AI 代理程式提供與人們相同的 SQL 介面，用來查詢、佈建及操作雲端與 SaaS 服務。代理程式會探索 Provider 公開的內容，在執行查詢前先驗證查詢，以 `SELECT` 讀取即時狀態，並以 `INSERT`、`UPDATE` 和 `DELETE` 變更狀態，不需要狀態檔，也不需要各雲端專屬的 SDK。

## 代理程式的連線方式 {#how-agents-connect}

- **透過 stdio 的 MCP**，適用於 Claude Desktop、Cursor 和 Continue 等編輯器與桌面用戶端。用戶端會自行啟動 `stackql mcp --mcp.server.type=stdio`。請參閱 [MCP 伺服器](/command-line-usage/mcp)。
- **透過 HTTP 的 MCP**，適用於連線到長時間執行之 `stackql mcp` 或 `stackql srv` 處理程序的獨立代理程式與自動化，支援 bearer 權杖驗證與選用的 TLS。
- **內嵌式 MCP**，適用於已編譯的應用程式：伺服器在 Go、Rust、Swift、Kotlin、.NET 和 Gleam 應用程式的處理程序內執行。請參閱[內嵌式 MCP](/mcp/embedded)。
- **PostgreSQL 線路協定**，透過 [`stackql srv`](/command-line-usage/srv) 提供，適用於已支援 Postgres 的代理程式架構、筆記本與 BI 工具。

## 搭配 Claude Desktop 快速入門 {#quick-start-with-claude-desktop}

從 Anthropic Connector Directory 安裝（建議），或使用可下載的 MCP Bundle，也可以在 `claude_desktop_config.json` 中手動註冊伺服器：

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": ["mcp", "--mcp.server.type=stdio", "--tls.allowInsecure"],
      "env": {
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key"
      }
    }
  }
}
```

只需包含您打算使用之 Provider 的憑證。完整說明（包括下載安裝套件與使用憑證檔案）請參閱[在 Claude Desktop 中使用 StackQL](/getting-started/claude-desktop)。

## 代理程式可以做什麼 {#what-an-agent-can-do}

MCP 伺服器公開一組精簡且具型別的工具。每個工具都會傳回供模型使用的轉譯檢視，以及供程式化用戶端使用的結構化承載。

- **探索**：`list_providers`、`list_services`、`list_resources`、`list_methods` 和 `describe_method` 會逐層瀏覽 Provider 階層，並傳回每個作業的輸入與輸出欄位，因此在撰寫查詢之前即可得知必要的 `WHERE` 參數。
- **驗證與讀取**：`validate_select_query` 會剖析並規劃 `SELECT` 而不執行；`run_select_query` 則執行該查詢。
- **變更**：`run_mutation_query` 執行 `INSERT`、`UPDATE`、`REPLACE` 和 `DELETE`；`run_lifecycle_operation` 執行 `EXEC` 生命週期作業。兩者都由下方所述的伺服器模式管控。
- **維護**：`server_info`、`list_registry`、`pull_provider` 和 `reload_credentials`。

完整參考（包括每個工具的輸入、管控行為與範例提示詞）請參閱 [MCP 工具](/mcp)。

## 防護措施 {#guardrails}

伺服器模式設定整個工作階段的安全契約：

| 模式 | `SELECT` / 中繼資料 | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` / `EXEC` |
|--|--|--|--|
| `read_only` | 允許 | 拒絕 | 拒絕 |
| `safe`（預設） | 允許 | 需要核准 | 需要核准 |
| `delete_safe` | 允許 | 允許 | 需要核准 |
| `full_access` | 允許 | 允許 | 允許 |

「需要核准」使用 MCP 徵詢（elicitation）：支援此功能的用戶端（Claude Desktop、Cursor、Continue）會向使用者顯示工具名稱、查詢類別與 SQL，並在任何作業執行前等待使用者決定；未宣告此功能的用戶端會遭到拒絕。每次工具呼叫都會寫入稽核記錄，HTTP 接聽程式需要 bearer 權杖，憑證則從環境或 dotenv 檔案取得，祕密值絕不會傳回給用戶端。詳細資訊請參閱[伺服器模式](/command-line-usage/mcp#server-modes)、[HTTP 用戶端驗證](/command-line-usage/mcp#http-client-authentication) 和[稽核記錄](/command-line-usage/mcp#audit-log)。

## 後續步驟 {#next-steps}

- [MCP 伺服器](/command-line-usage/mcp)：部署模式、設定、伺服器模式、稽核記錄與旗標。
- [MCP 工具](/mcp)：完整的工具參考，包括輸入、管控與範例提示詞。
- [內嵌式 MCP](/mcp/embedded)：在您自己的 Go、Rust、Swift、Kotlin、.NET 或 Gleam 應用程式中執行伺服器。
- [Query Library](pathname:///docs/query-library/)：依 Provider 分類、可直接執行的 SQL，用於回答常見的雲端維運問題。
- [AI 參考](/ai)：為代理程式及其建置者撰寫的概念、操作指南、比較與疑難排解。
