---
title: 如何搭配 AI 代理程式使用 StackQL
description: 將 StackQL 作為 Model Context Protocol（MCP）伺服器執行，讓 AI 代理程式探索雲端供應商 schema、驗證查詢，並對即時基礎架構執行受管控的讀取與變更。
keywords: [stackql, AI 代理程式, mcp, model context protocol, 代理式基礎架構, 自動化]
proficiencyLevel: Intermediate
faq:
  - question: StackQL 伺服器公開了哪些 MCP 工具？
    answer: 探索工具（list_providers、list_services、list_resources、list_methods、describe_method）、登錄庫工具（list_registry、pull_provider）、驗證工具（validate_select_query）、執行工具（run_select_query、run_mutation_query、run_lifecycle_operation）、server_info，以及 reload_credentials（重新讀取 --env.file 指定的 dotenv 檔案，並回報各 Provider 的憑證狀態）。enabled_tools 允許清單可以縮小這組工具，例如用來發佈唯讀的資源清單伺服器。
  - question: 如何確保代理程式的變更安全？
    answer: 透過伺服器模式。預設的 safe 模式允許 SELECT 和中繼資料呼叫，但 INSERT、UPDATE、DELETE 和 EXEC 需要透過 MCP 徵詢（elicitation）流程取得人工核准。read_only 會直接拒絕變更；delete_safe 允許建立與更新，但刪除受管控；full_access 允許所有作業。每次工具呼叫都會寫入 JSONL 稽核記錄，此功能預設為開啟。
  - question: 支援哪些傳輸方式？
    answer: stdio 用於內嵌於編輯器的用戶端（Claude Desktop、Cursor、Continue），HTTP 用於獨立代理程式，另有反向代理模式，可將 MCP 要求分派至後端的 StackQL PostgreSQL 伺服器，並可選擇使用 TLS。
---

# 如何搭配 AI 代理程式使用 StackQL

StackQL 內建 Model Context Protocol（MCP）伺服器。任何支援 MCP 的代理程式（Claude、Cursor、Continue 或自訂代理程式架構）都能連線到此伺服器，取得一個可自我描述且受管控的 SQL 介面，存取 StackQL 登錄庫中的每個雲端與 SaaS Provider。

## 步驟 {#steps}

1. **設定 Provider 憑證**，在啟動伺服器之前將其設為環境變數（伺服器會繼承這些變數）：

```bash
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
```

或者（在 v0.10.542 之後的 StackQL 發行版本中），使用 `--env.file` 指定 dotenv 格式的憑證檔案；`reload_credentials` MCP 工具會在工作階段中途重新讀取該檔案，讓輪替後的憑證無須重新啟動即可送達執行中的伺服器。

2. **啟動 MCP 伺服器**。若要執行獨立的 HTTP 伺服器：

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'
```

從 `v0.12.742` 起，HTTP 傳輸方式需要 `server.auth_token_env_var`（存放預先共用 bearer 權杖的環境變數，每個要求都必須攜帶此權杖），或如此處所示明確選擇加入 `allow_unauthenticated`，否則伺服器會拒絕啟動；凡是其他處理程序或主機可以連線的接聽程式，都應使用權杖。`stdio` 不受影響。

對於內嵌於編輯器的用戶端，請使用 `--mcp.server.type=stdio`，並讓用戶端啟動處理程序（請參閱[如何搭配 Claude 使用 StackQL](/ai/how-tos/use-stackql-with-claude)）。若要從單一處理程序同時提供 MCP 與 PostgreSQL 線路協定，請使用 `stackql srv` 搭配相同的 MCP 旗標，再加上 `--pgsrv.port`。

3. **選擇安全模式**。模式在伺服器設定中指定，並管控代理程式可以執行的作業：

| 模式 | SELECT / 中繼資料 | INSERT / UPDATE / REPLACE | DELETE | EXEC |
|---|---|---|---|---|
| `read_only` | 允許 | 拒絕 | 拒絕 | 拒絕 |
| `safe`（預設） | 允許 | 需要核准 | 需要核准 | 需要核准 |
| `delete_safe` | 允許 | 允許 | 需要核准 | 需要核准 |
| `full_access` | 允許 | 允許 | 允許 | 允許 |

「需要核准」使用 MCP 徵詢（elicitation）流程：用戶端會向使用者顯示待執行的 SQL，由使用者接受或拒絕。資源清單代理程式請固定使用 `read_only`；`full_access` 僅保留給稽核記錄經過審查的受信任管線。包含多個陳述式的承載會依其中權限最高的陳述式管控（從 `v0.12.742` 起），因此變更無法夾帶在開頭的 `SELECT` 之後。

4. **讓代理程式先探索，再查詢**。典型的代理程式工作階段：`list_providers` -> `list_methods`（會顯示必要的 `WHERE` 參數）-> `validate_select_query` -> `run_select_query`：

```sql
SELECT name, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

代理程式完全不需要載入 SDK；schema 在執行階段探索，查詢則是一般的 SQL。

## 稽核 {#auditing}

每次工具呼叫都會將一筆 JSONL 記錄（時間戳記、工具、模式、決策、查詢類別、SQL、持續時間）寫入具備輪替功能的檔案接收端。稽核預設為開啟，記錄的是代理程式做了什麼（而非結果資料列）。在預設的 `strict` 失敗模式下，稽核寫入失敗會以錯誤呈現，而不會讓未經稽核的變更趁隙通過。

## 相關概念 {#related-concepts}

- [如何搭配 Claude 使用 StackQL](/ai/how-tos/use-stackql-with-claude)：Claude Desktop 設定
- [StackQL MCP 架構](/ai/architecture/stackql-mcp-architecture)：傳輸方式、模式與稽核的內部機制
- [StackQL vs 自訂 MCP 伺服器](/ai/comparisons/stackql-vs-custom-mcp-servers)：何時應自行建置
- [什麼是代理式基礎架構？](/ai/canonical-definitions/what-is-agentic-infrastructure)：這套做法所實現的模式
- [為什麼 SQL 是雲端 API 的強大介面](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis)：為什麼代理程式與 SQL 相契合
