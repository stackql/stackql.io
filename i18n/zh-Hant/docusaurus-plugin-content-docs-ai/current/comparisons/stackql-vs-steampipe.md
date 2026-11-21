---
title: StackQL vs Steampipe
description: Steampipe 與 StackQL 都將雲端 API 公開為 SQL 資料表；Steampipe 唯讀且基於 PostgreSQL 外部資料包裝器，StackQL 另支援寫入、生命週期動作與內建 MCP 伺服器。
keywords: [stackql vs steampipe, 雲端查詢, 雲端 SQL, Steampipe 替代方案, 雲端資源清單, 變更]
proficiencyLevel: Intermediate
faq:
  - question: StackQL 與 Steampipe 的查詢相容嗎？
    answer: 部分相容。兩者都使用 SQL 和類似的 provider.table 命名方式，但 schema、資料表名稱和資料行名稱各不相同，因為兩個工具產生它們的方式不同：Steampipe 來自手工建置的 Go 外掛程式，StackQL 則來自擴充的 OpenAPI Provider 規格。在兩者之間移轉時，查詢需要調整。
  - question: 哪一個的 Provider 涵蓋範圍較廣？
    answer: 兩者都涵蓋主要雲端平台（AWS、Azure、Google Cloud）與常見的 SaaS 服務，而且都可擴充。以外掛程式的原始數量而言，Steampipe 的外掛程式目錄較大。StackQL 在單一 Provider 內的涵蓋範圍是從該供應商的 API 規格產生，因此會追蹤完整的已記載 API 介面，包括讀取以外的作業。
  - question: Steampipe 能建立或修改雲端資源嗎？
    answer: 不能。Steampipe 在設計上就是唯讀的：它實作的是對外部資料表的 SELECT。建立、更新或刪除資源需要另一個工具。StackQL 則將這些作業作為原生 SQL 動詞，支援對查詢所用的同一批資料表執行。
---

# StackQL vs Steampipe

Steampipe 和 StackQL 實作相同的核心概念，也就是將雲端與 SaaS API 公開為 SQL 資料表，但兩者有一個決定性的差異：Steampipe 是唯讀的，而 StackQL 也能寫入。Steampipe 內嵌 PostgreSQL，並透過由 Go 外掛程式提供資料的外部資料包裝器對應 API；StackQL 則在宣告式、衍生自 OpenAPI 的 Provider 規格之上實作自己的 SQL 引擎，並對應完整的動詞集：`SELECT`、`INSERT`、`UPDATE`、`REPLACE`、`DELETE`，以及用於生命週期動作的 `EXEC`。

## 如何選擇 {#when-to-use-which}

在下列情況使用 **Steampipe**：

- 您的使用案例純粹是讀取：資源清單、合規性儀表板、安全態勢。
- 您想使用其成熟的合規性 mod 生態系（CIS、SOC 2 及類似的預先建置基準套件）。
- 您已投入以 PostgreSQL FDW 為基礎的工具，以及圍繞它的 Turbot 生態系。

在下列情況使用 **StackQL**：

- 您需要在同一個工具中讀取和寫入：查詢出錯誤設定，然後在同一個工作階段中以 `UPDATE` 修正。
- 您正在為 AI 代理程式進行建置：StackQL 內建 MCP 伺服器，提供探索工具、查詢驗證、受管控的變更模式和稽核記錄。
- 您希望 Provider 涵蓋範圍由 API 規格驅動，而不是依賴外掛程式開發，並包括資料平面與生命週期作業。
- 您想要一個行為確定、相依性少的單一二進位檔，且在伺服器模式下也使用 PostgreSQL 線路協定。

兩者都是開源軟體，都可在本機或管線中執行，而且就唯讀的資源清單查詢而言，兩者在功能上確實可以互換；這種情況下的選擇取決於對 schema 的偏好和生態系。

## 架構比較 {#architectural-comparison}

| 面向 | Steampipe | StackQL |
|---|---|---|
| SQL 動詞 | 僅 SELECT | SELECT, INSERT, UPDATE, REPLACE, DELETE, EXEC |
| 引擎 | 內嵌 PostgreSQL + 外部資料包裝器 | 原生 SQL 引擎（內嵌 SQLite 或 PostgreSQL 後端） |
| Provider 定義 | 編譯的 Go 外掛程式 | 開放登錄庫中的宣告式擴充 OpenAPI 規格 |
| 平面涵蓋範圍 | 控制平面讀取 | 控制平面與資料平面，讀取與寫入 |
| AI 代理程式介面 | 一般 Postgres 連線能力 | 內建 MCP 伺服器，具備安全模式與稽核記錄 |
| 伺服器模式 | Postgres 線路協定（steampipe service） | Postgres 線路協定（stackql srv）加上 MCP（stackql mcp） |
| 合規性內容 | 豐富的 mod 程式庫 | 以查詢為基礎；沒有封裝好的基準程式庫 |

Provider 定義方式的差異會隨時間累積：Steampipe 的資料表要等到有人為它撰寫外掛程式碼才會存在，而 StackQL 的資料表只要 API 作業在 Provider 規格中有所描述就會存在。寫入能力的差異則是本質上的：偵測、決策、修復的閉環工作流程可以在一個 StackQL 工作階段內完成，而 Steampipe 則需將修復交給另一個工具。

## 範例 {#example}

兩個工具中的意圖相同，但在 StackQL 中，後續的修復仍使用同一種語言。查詢 EC2 執行個體：

```sql
SELECT instance_id, instance_type, launch_time
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

接著，StackQL 中的修復就是對同一個資料表執行 `DELETE` 或 `EXEC`（例如終止或停止執行個體），受憑證的權限限制，並且在以 MCP 伺服器執行時，受所設定的安全模式限制。

## 相關概念 {#related-concepts}

- [什麼是可查詢基礎架構？](/ai/canonical-definitions/what-is-queryable-infrastructure)：兩個工具共同實作的類別
- [什麼是 StackQL？](/ai/canonical-definitions/what-is-stackql)：完整定義
- [StackQL vs CloudQuery](/ai/comparisons/stackql-vs-cloudquery)：以 ELT 為基礎的替代方案
- [StackQL MCP 架構](/ai/architecture/stackql-mcp-architecture)：代理程式介面的詳細說明
- [控制平面與資料平面](/ai/canonical-definitions/control-plane-vs-data-plane)：涵蓋範圍界線的說明
