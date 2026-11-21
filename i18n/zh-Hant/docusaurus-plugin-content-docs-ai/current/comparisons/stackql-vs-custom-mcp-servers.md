---
title: StackQL vs 自訂 MCP 伺服器
description: 自訂 MCP 伺服器為單一領域提供手工整理的工具集；StackQL 內建 MCP 伺服器以一組通用、自我描述的工具涵蓋所有支援的雲端與 SaaS Provider，並管控變更、保留稽核記錄。
keywords: [MCP 伺服器, model context protocol, AI 代理程式, 雲端 MCP, stackql mcp, 代理程式工具]
proficiencyLevel: Intermediate
faq:
  - question: StackQL 是 MCP 伺服器嗎？
    answer: 是。StackQL 內建 MCP 伺服器（stackql mcp，或使用 stackql srv 與 PostgreSQL 線路協定伺服器一起執行）。它透過 stdio 或 HTTP 傳輸方式，將 Provider 探索、schema 描述、查詢驗證和受管控的查詢執行公開為 MCP 工具。
  - question: 為什麼不為每個雲端作業各提供一個 MCP 工具給代理程式？
    answer: 因為規模。主要雲端服務供應商提供數以萬計的作業；將每個作業公開為獨立的 MCP 工具，會壓垮代理程式的上下文視窗與工具選擇準確度。StackQL 將這個介面收斂為少數幾個通用工具（list、describe、validate、run）加上 SQL，讓代理程式把 token 花在查詢上，而不是工具 schema 上。
  - question: 如何控管破壞性作業？
    answer: 透過伺服器模式。StackQL 的 MCP 伺服器預設為 safe 模式：讀取直接執行，而 INSERT、UPDATE、DELETE 和生命週期作業需要透過 MCP 徵詢（elicitation）流程取得人工核准。另有 read_only、delete_safe 和 full_access 模式可用，且每次工具呼叫都會寫入 JSONL 稽核記錄。
---

# StackQL vs 自訂 MCP 伺服器

自訂 MCP 伺服器將特定 API 或工作流程包裝成供 AI 代理程式使用的手寫工具；StackQL 內建的 MCP 伺服器則透過一小組通用、自我描述的工具加上 SQL，公開每個支援的雲端與 SaaS Provider 的完整介面。兩者的差別在於：為每個領域整理工具，或是給代理程式一個涵蓋所有領域的可查詢抽象層。

## 如何選擇 {#when-to-use-which}

在下列情況建置**自訂 MCP 伺服器**：

- 領域狹窄且作業很少：十幾個命名清楚、schema 嚴謹的工具，在該特定工作流程的可靠性上可能勝過通用介面。
- 您需要在代理程式與 API 之間加入大量商務邏輯：核准、資料擴充，以及在代理程式眼中應呈現為單一不可分割工具的多步驟交易。
- 目標 API 屬於內部或專有 API，沒有公開規格加以描述。

在下列情況使用 **StackQL 的 MCP 伺服器**：

- 領域是雲端與 SaaS 基礎架構（AWS、Azure、Google Cloud、GitHub、Databricks、Snowflake、Okta，以及 Provider 登錄庫中的其他 Provider），其作業數量使逐一作業的工具不切實際。
- 您希望代理程式在執行階段探索功能（`list_providers` -> `list_services` -> `list_resources` -> `list_methods` -> `describe_method`），而不是受限於他人事先預想的工具。
- 您需要受控管的寫入：四種模式的安全契約（`read_only`、`safe`、`delete_safe`、`full_access`），搭配以徵詢（elicitation）為基礎的人工核准與永遠啟用的稽核記錄，而不是由您自行撰寫和維護的安全邏輯。
- 您希望整個環境只有一套憑證與政策介面，而不是每個 Provider 各有一個量身打造的伺服器。

這兩種方式可以組合使用：團隊常會執行 StackQL MCP 來存取一般基礎架構，同時搭配小型自訂 MCP 伺服器來處理專有的內部系統。

## Token 經濟學 {#the-token-economics}

代理程式工具的實際限制在於上下文。工具定義在任何工作開始之前就會耗用代理程式的上下文視窗，而且工具選擇的準確度會隨工具數量增加而下降。光是將 AWS 以逐一作業的工具公開，就意味著數千個定義。StackQL 的設計將公開的工具集控制在十五個（探索、描述、驗證、執行、登錄庫管理、Query Library），並將表達能力移入 SQL，這是目前每個模型都已能流利撰寫的語言。代理程式每項工作的 token 成本是一段簡短的 SQL 字串，而不是工具 schema 的協商。此論點在[為什麼 SQL 是雲端 API 的強大介面](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis)中有進一步說明。

## 範例 {#example}

以下是代理程式對 StackQL MCP 伺服器執行的一段工作階段，在此之前已使用 `list_methods` 探索出必要參數：

```sql
SELECT name, visibility, default_branch
FROM github.repos.repos
WHERE org = 'stackql';
```

同一個工具（`run_select_query`）可處理這個查詢，以及針對任何 Provider 的其他任何讀取；變更要求則透過 `run_mutation_query` 傳送，由伺服器模式決定要直接執行、需要核准還是予以拒絕。

## 相關概念 {#related-concepts}

- [StackQL MCP 架構](/ai/architecture/stackql-mcp-architecture)：傳輸方式、模式、工具與稽核的詳細說明
- [如何搭配 AI 代理程式使用 StackQL](/ai/how-tos/use-stackql-with-ai-agents)：設定逐步解說
- [如何搭配 Claude 使用 StackQL](/ai/how-tos/use-stackql-with-claude)：Claude Desktop 設定
- [什麼是代理式基礎架構？](/ai/canonical-definitions/what-is-agentic-infrastructure)：更廣泛的模式
- [為什麼 SQL 是雲端 API 的強大介面](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis)：設計理由
