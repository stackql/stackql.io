---
title: StackQL MCP 架構
description: StackQL 內建的 MCP 伺服器透過 stdio、HTTP 或反向代理，將探索、schema 描述、查詢驗證與受管控執行公開為工具，並以四種安全模式與 JSONL 稽核記錄管控。
keywords: [stackql, MCP 架構, model context protocol, AI 代理程式, 稽核記錄, 徵詢, 安全模式]
proficiencyLevel: Expert
faq:
  - question: stackql mcp 和 stackql srv 有什麼差異？
    answer: stackql mcp 會執行獨立的 MCP 伺服器。stackql srv 會執行 PostgreSQL 線路協定伺服器，並可在同一個處理程序中同時裝載 MCP 伺服器：可以使用記憶體內通訊（mcp.server.type=http），或作為反向代理，透過 TCP 將 MCP 要求分派到 Postgres 後端，後者支援分散式部署與 TLS。
  - question: 代理程式在 safe 模式下嘗試變更時會發生什麼事？
    answer: 伺服器會向用戶端傳送 MCP elicitation/create 要求，說明該動作：工具名稱、查詢類別與 SQL 本身。使用者可以接受、拒絕或取消。如果用戶端未宣告徵詢（elicitation）功能，呼叫會遭到拒絕，並附上說明缺少何種功能的訊息；非互動式自動化必須明確選擇加入 full_access。
  - question: 稽核記錄會記錄哪些內容？
    answer: 每次工具呼叫一筆 JSONL 記錄：時間戳記、工具名稱、伺服器模式、管控決策、查詢類別、SQL 字串、引數、持續時間，以及任何錯誤。結果資料列刻意不予記錄。稽核預設為啟用，並採用以檔案為基礎的輪替；在預設的 strict 失敗模式下，稽核寫入失敗會以錯誤呈現，因此不會有任何變更在未經稽核的情況下完成。
---

# StackQL MCP 架構

StackQL 的 MCP 伺服器內建於引擎二進位檔中，將 StackQL 的功能介面（探索、描述、驗證、執行）公開為 Model Context Protocol 工具。其設計以三件事為核心：以少量通用工具取代每個作業各一個工具、針對變更的分級安全契約，以及記錄代理程式一切行為的稽核軌跡。

## 傳輸方式與部署模式 {#transports-and-deployment-modes}

| 模式 | 命令 | 用途 |
|---|---|---|
| stdio | `stackql mcp --mcp.server.type=stdio` | 直接啟動處理程序、內嵌於編輯器的用戶端（Claude Desktop、Cursor、Continue） |
| HTTP（獨立） | `stackql mcp --mcp.server.type=http` | 供獨立代理程式使用的長時間執行伺服器 |
| 雙伺服器、記憶體內 | `stackql srv --mcp.server.type=http --pgsrv.port <port>` | 由單一處理程序提供 MCP 與 PostgreSQL 線路協定 |
| 反向代理 | `stackql srv --mcp.server.type=reverse_proxy` | MCP 要求透過 DSN 分派到後端的 StackQL Postgres 伺服器；支援工作負載分離與 TLS |

設定是透過 `--mcp.config` 傳入的 JSON（或 YAML）物件，涵蓋傳輸方式、位址、TLS 憑證路徑、用戶端驗證、模式、稽核設定，以及選用的 `enabled_tools` / `enabled_prompts` 允許清單，用來公開範圍縮小的介面（例如只公開 `server_info` 與 `list_providers` 的唯讀資源清單伺服器）。從 `v0.12.742` 起，HTTP 傳輸方式需要預先共用的 bearer 權杖（`server.auth_token_env_var`），或明確選擇加入 `server.allow_unauthenticated`；跨來源的瀏覽器要求會遭到拒絕。

## 工具介面 {#the-tool-surface}

共有十四個工具，每個工具都會傳回供 LLM 使用的轉譯文字檢視，以及具型別的結構化承載：

- **探索**：`list_providers`、`list_services`、`list_resources`、`list_methods`、`describe_method`，逐層走訪階層，在撰寫任何查詢之前就揭示必要的 `WHERE` 參數。
- **登錄庫**：`list_registry`（可用的 Provider 與版本）、`pull_provider`（安裝到本機快取）。
- **驗證**：`validate_select_query`，剖析並規劃 `SELECT` 而不執行。
- **執行**：`run_select_query`（讀取）、`run_mutation_query`（`INSERT`/`UPDATE`/`REPLACE`/`DELETE`，會產生實際的副作用）、`run_lifecycle_operation`（`EXEC`）。
- **身分**：`server_info`，提供版本、後端、登錄庫、模式；在需要時呼叫（用於診斷遭拒絕或受管控的作業，或檢查特定版本的行為），而不是在工作階段開始時呼叫。
- **憑證**：`reload_credentials`，在工作階段進行中將 `--env.file` dotenv 檔案重新載入處理程序環境，並回報各 Provider 的憑證解析狀態；只包含名稱與狀態，絕不包含祕密值（於 v0.10.542 之後的發行版本提供）。

另有一個靜態提示詞 `write_safe_select`，用來教導方法探索的工作流程。

## 安全契約 {#the-safety-contract}

`server.mode` 會選取四種契約之一；所有契約都允許 `SELECT` 與中繼資料，差異在於寫入：

| 模式 | INSERT / UPDATE / REPLACE | DELETE | EXEC |
|---|---|---|---|
| `read_only` | 拒絕 | 拒絕 | 拒絕 |
| `safe`（預設） | 需要核准 | 需要核准 | 需要核准 |
| `delete_safe` | 允許 | 需要核准 | 需要核准 |
| `full_access` | 允許 | 允許 | 允許 |

「需要核准」是以 MCP 徵詢（elicitation）實作：伺服器將待處理的動作（工具、查詢類別、SQL）傳送給用戶端，由人員接受或拒絕。未宣告徵詢功能的用戶端會收到拒絕，並指引維運人員改用 `full_access`，這讓無人值守的變更成為明確的選擇加入，而不是預設行為。模式是每部伺服器的全域設定；沒有個別工具的覆寫。包含多個陳述式的承載會由其中權限最高的陳述式管控（自 `v0.12.742` 起），而 `validate_select_query` 只接受一個陳述式。

## 稽核子系統 {#the-audit-subsystem}

每次工具呼叫都會將一筆 JSONL 記錄寫入檔案接收端，並依大小、存留時間與備份份數輪替。記錄內容包括時間戳記、工具、模式、決策（`allow`、`refuse_immediate`、`needs_approval_accepted` 等）、查詢類別、SQL、引數、持續時間與錯誤。記錄回答的是「代理程式做了什麼」，刻意不回答「代理程式看到了什麼」：`SELECT` 結果資料列可能龐大且敏感，因此不予記錄。寫入發生在執行之後、回應傳回之前；在預設的 `strict` 失敗模式下，成功的變更若稽核失敗，會向用戶端傳回錯誤，這是刻意的選擇，確保沒有任何 `DELETE` 會在未經稽核的情況下完成。

## 相關概念 {#related-concepts}

- [如何搭配 AI 代理程式使用 StackQL](/ai/how-tos/use-stackql-with-ai-agents)：實務設定
- [如何搭配 Claude 使用 StackQL](/ai/how-tos/use-stackql-with-claude)：Claude Desktop 的細節
- [StackQL vs 自訂 MCP 伺服器](/ai/comparisons/stackql-vs-custom-mcp-servers)：設計上的取捨
- [StackQL 架構概觀](/ai/architecture/stackql-architecture-overview)：MCP 伺服器在引擎中的位置
- [什麼是代理式基礎架構？](/ai/canonical-definitions/what-is-agentic-infrastructure)：此實作所對應的模式
