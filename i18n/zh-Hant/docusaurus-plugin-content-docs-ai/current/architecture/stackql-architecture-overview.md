---
title: StackQL 架構概觀
description: StackQL 是以 Go 撰寫的 SQL 引擎，含三個進入點、Yacc 剖析器、呼叫即時 Provider API 的查詢引擎，以及內嵌 SQLite 或外部 PostgreSQL 後端。
keywords: [StackQL 架構, 查詢引擎, SQL 剖析器, provider sdk, sqlite, postgresql, 登錄庫]
proficiencyLevel: Expert
faq:
  - question: 實際的 SQL 處理在哪裡進行？
    answer: 分為兩個階段。查詢引擎先將所有能下推的部分推送給 Provider API（路由參數、支援的述詞），再將 API 回應具體化到 SQL 後端（預設為內嵌的 SQLite，也可選用外部 PostgreSQL），並在其中執行聯結、彙總、排序與運算式。
  - question: StackQL 如何得知該怎麼呼叫各個 Provider 的 API？
    answer: 來自 StackQL Registry 中的 Provider 定義，也就是具版本控制的擴充 OpenAPI 規格，描述各個 Provider 的服務、資源、方法、參數、驗證與分頁。定義會在需要時以 REGISTRY PULL 提取，並由 Provider SDK（omnisdk）解譯；引擎中不會編譯任何 Provider 專屬的程式碼。
  - question: StackQL 是單一二進位檔嗎？
    answer: 是。引擎、剖析器、Provider SDK、內嵌的 SQL 後端、PostgreSQL 線路協定伺服器與 MCP 伺服器都包含在單一 Go 二進位檔中，提供 macOS、Linux 與 Windows 版本。
---

# StackQL 架構概觀

StackQL 是單一 Go 二進位檔，在雲端與 SaaS Provider 的 API 之上實作 SQL 引擎。在架構上它分為四層：接受 SQL 的進入點、規劃 SQL 的剖析器與查詢引擎、依宣告式 Provider 規格執行即時 API 呼叫的 Provider SDK，以及對具體化結果執行關聯式運算的 SQL 後端。

## 元件 {#components}

**進入點**。共有三種進入方式，最終都交由同一個剖析器處理：

- **CLI / shell**（`stackql shell`、`stackql exec`），用於互動式與批次作業。
- **PostgreSQL 線路協定伺服器**（`stackql srv`，以 psql-wire 建構），讓任何 Postgres 用戶端（psql、DBeaver、BI 工具、pandas）都能連線。
- **MCP 伺服器**（`stackql mcp`），向 AI 代理程式公開探索、驗證與受管控執行的工具（詳見 [StackQL MCP 架構](/ai/architecture/stackql-mcp-architecture)）。

**SQL 剖析器**。以 Yacc 為基礎的文法（`stackql-parser`），產生 AST。此方言涵蓋標準動詞，以及 StackQL 中繼命令（`SHOW`、`DESCRIBE`、`REGISTRY`）和用於非 CRUD 生命週期作業的 `EXEC` 動詞。

**查詢引擎**。規劃 AST：決定哪些部分下推到 Provider API（路由參數、支援的述詞），哪些在本機處理；協調 API 的執行（包括查詢跨越多個區域或需要多次呼叫時的平行扇出），並彙總結果。

**Provider SDK（omnisdk）**。通用的 API 用戶端，在 StackQL v1.0 中取代了 any-sdk。它從登錄庫載入 Provider 規格、處理各 Provider 的驗證、將每個陳述式規劃為 Provider 交換的圖形、根據查詢參數建立 HTTP 要求、將支援的述詞與筆數限制推送到這些要求中、不論採用哪種分頁機制（資料指標、位移、權杖）都能逐頁走訪分頁回應，並在 JSON 回應抵達時將其關聯化為資料列。

**SQL 後端**。具體化的 API 結果預設會存入內嵌的 SQLite 資料庫（也可改為設定外部 PostgreSQL），並在其中以完整的 SQL 語意執行聯結（包括跨 Provider 聯結）、彙總、排序、子查詢與運算式。

## 決定性的設計決策：Provider 即資料 {#the-defining-design-decision-providers-as-data}

StackQL 沒有經過編譯的 Provider 專屬外掛程式。Provider 是具版本控制的文件集，也就是開放的 [StackQL Provider 登錄庫](https://github.com/stackql/stackql-provider-registry) 中的擴充 OpenAPI 規格，將 API 作業對應到資源、SQL 動詞與參數。`REGISTRY PULL aws` 會下載規格；SDK 則在執行階段解譯它。

這帶來幾項結果：Provider 的涵蓋範圍取決於有文件記載的 API 介面，而不是外掛程式的開發投入；資料平面與生命週期作業可以用與控制平面 CRUD 相同的方式對應；整個介面都能在執行階段自我檢查（`SHOW PROVIDERS`、`SHOW METHODS IN aws.ec2.instances`、`DESCRIBE github.repos.repos`），正是這項特性讓 StackQL 對 AI 代理程式而言能夠自我描述。

## 查詢執行流程 {#query-execution-flow}

以 `SELECT instance_id, instance_type FROM aws.ec2.instances WHERE region = 'us-east-1';` 為例：

1. 剖析器產生 AST；引擎根據 Provider 規格解析 `aws.ec2.instances`，並選取 `describe_instances` 存取方法，由 `region` 滿足其必要參數。
2. SDK 進行驗證（根據環境變數）、發出 EC2 API 呼叫，並逐頁走訪分頁。
3. 回應會攤平成資料列，並具體化到 SQL 後端。
4. 後端套用投影與其餘的關聯式處理；結果會以要求的格式傳回給呼叫端。

在步驟 2，`region IN (...)` 述詞會變成依區域平行發出的呼叫；在步驟 4，聯結可跨越所有提供資料表的 Provider 運作。

## 相關概念 {#related-concepts}

- [StackQL MCP 架構](/ai/architecture/stackql-mcp-architecture)：深入介紹面向代理程式的進入點
- [什麼是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：Provider 規格所編碼的對應方式
- [為什麼 SQL 是雲端 API 的強大介面](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis)：此方言的設計理由
- [什麼是可查詢基礎架構？](/ai/canonical-definitions/what-is-queryable-infrastructure)：此架構所服務的運作模式
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe)：與以 FDW 為基礎的架構對照
