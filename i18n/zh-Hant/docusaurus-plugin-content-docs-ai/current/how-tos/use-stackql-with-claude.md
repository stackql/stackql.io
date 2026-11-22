---
title: 如何搭配 Claude 使用 StackQL
description: 在 Claude Desktop 或 Claude Code 中將 StackQL 註冊為 MCP 伺服器，讓 Claude 以自然語言查詢及管理 AWS、Azure、Google Cloud、GitHub 等 Provider 的雲端基礎架構。
keywords: [stackql, claude, claude desktop, claude code, mcp, anthropic, 雲端基礎架構]
proficiencyLevel: Beginner
faq:
  - question: Claude 需要了解 StackQL 語法嗎？
    answer: 不需要特別訓練。Claude 會在執行階段透過 MCP 探索工具（list_providers、list_methods、describe_method）探索 Provider schema，並撰寫標準 SQL，而這本來就是它擅長的事。您以自然語言互動，Claude 會將其轉換為 StackQL 查詢。
  - question: Claude 在變更我的基礎架構之前會先詢問嗎？
    answer: 會，在預設設定下會先詢問。StackQL MCP 伺服器預設為 safe 模式，而 Claude Desktop 宣告了 MCP 徵詢（elicitation）功能，因此每個 INSERT、UPDATE、DELETE 或生命週期作業在執行前，都會先觸發核准要求並顯示該 SQL。將 mode 設為 read_only 即可完全禁止變更。
  - question: 我的雲端憑證要放在哪裡？
    answer: 放在 MCP 設定檔中 stackql 項目的 env 區塊，或放在伺服器處理程序所繼承的環境中。在 v0.10.542 之後的 StackQL 發行版本中，也可以改用 --env.file 旗標傳入 dotenv 格式的憑證檔案，reload_credentials 工具可在不重新啟動 Claude Desktop 的情況下重新讀取該檔案。憑證會與 StackQL 二進位檔一起留在您的電腦上，不會傳送給模型。
---

# 如何搭配 Claude 使用 StackQL

Claude 透過 Model Context Protocol 連線到 StackQL：在 Claude Desktop（或 Claude Code）中將 `stackql` 註冊為 MCP 伺服器後，Claude 就能以自然語言驅動，對 StackQL 支援的每個 Provider 盤點及稽核雲端資源，並在您核准後修改這些資源。

## 步驟 {#steps}

1. **安裝 StackQL**，並確認它位於您的 PATH 中：

```bash
stackql --version
```

2. **編輯 Claude Desktop 設定檔**：

- Windows: `%APPDATA%\Claude\claude_desktop_config.json`
- macOS: `~/Library/Application Support/Claude/claude_desktop_config.json`
- Linux: `~/.config/Claude/claude_desktop_config.json`

3. **新增伺服器項目**，包括您所使用 Provider 的憑證：

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--mcp.config",
        "{\"server\": {\"transport\": \"stdio\", \"mode\": \"safe\"}}"
      ],
      "env": {
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key",
        "STACKQL_GITHUB_USERNAME": "your-github-username",
        "STACKQL_GITHUB_PASSWORD": "your-github-personal-access-token"
      }
    }
  }
}
```

只需包含您需要的 Provider（Azure 使用 `AZURE_TENANT_ID`/`AZURE_CLIENT_ID`/`AZURE_CLIENT_SECRET`，Google Cloud 使用 `GOOGLE_CREDENTIALS`，Databricks 使用 `DATABRICKS_CLIENT_ID`/`DATABRICKS_CLIENT_SECRET`）。切勿將此檔案提交至版本控制。

4. **重新啟動 Claude Desktop**。對話開始時，伺服器會自動啟動。

5. **以自然語言提問**。以下範例可直接對應到 StackQL 作業：

- 「列出 us-east-1 中所有具有公用 IP 位址的 EC2 執行個體」
- 「我們的 GitHub 組織中，哪些存放庫停用了分支保護？」
- 「在 us-west-2 建立名為 analytics-data 的 S3 儲存貯體」

對於讀取，Claude 會探索 schema 並立即執行 `SELECT` 陳述式。對於建立儲存貯體，`safe` 模式會在任何作業執行之前觸發核准要求，顯示確切的 `INSERT`。

## 控制 Claude 可以執行的作業 {#controlling-what-claude-can-do}

設定中的 `mode` 欄位就是契約：`read_only` 適用於僅限資源清單的助理，`safe`（預設）適用於需經核准的變更，`delete_safe` 允許建立與更新但管控刪除和 `EXEC` 生命週期作業，`full_access` 僅適用於受信任的自動化。無論模式為何，Claude 進行的每次呼叫都會記錄在伺服器的 JSONL 稽核記錄中。

## 相關概念 {#related-concepts}

- [如何搭配 AI 代理程式使用 StackQL](/ai/how-tos/use-stackql-with-ai-agents)：一般的 MCP 設定，包括 HTTP 傳輸方式
- [StackQL MCP 架構](/ai/architecture/stackql-mcp-architecture)：模式、徵詢（elicitation）與稽核的詳細說明
- [如何讓 StackQL 向 AWS 進行驗證](/ai/how-tos/authenticate-stackql-to-aws)：各 Provider 的憑證參考
- [什麼是代理式基礎架構？](/ai/canonical-definitions/what-is-agentic-infrastructure)：更廣泛的模式
- [StackQL vs 自訂 MCP 伺服器](/ai/comparisons/stackql-vs-custom-mcp-servers)：為什麼一個通用伺服器勝過許多客製化伺服器
