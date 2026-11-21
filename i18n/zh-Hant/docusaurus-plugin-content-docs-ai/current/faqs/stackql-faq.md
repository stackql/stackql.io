---
title: StackQL 常見問題
description: 關於 StackQL 的常見問題：它是什麼、支援什麼（聯結、變更、Provider、MCP）、與其他工具的比較，以及部署方式。
keywords: [stackql, 常見問題, sql, 雲端 API, mcp, 聯結, 變更, Provider]
proficiencyLevel: Beginner
faq:
  - question: 什麼是 StackQL？
    answer: StackQL 是開源的查詢語言與執行階段，將雲端與 SaaS Provider 的 API 公開為關聯式資料表，可用標準 SQL 查詢與變更。SELECT 讀取即時的 Provider 狀態；INSERT、UPDATE 和 DELETE 建立、修改和移除資源；EXEC 叫用其他生命週期作業。
  - question: StackQL 是資料庫嗎？
    answer: 不是。StackQL 不儲存任何雲端狀態。每個查詢都在執行時由對供應商的即時 API 呼叫計算而得。在伺服器模式下，StackQL 使用 PostgreSQL 線路協定，因此對用戶端而言它看起來像資料庫，但這些「資料表」其實是 Provider API 的投影。
  - question: StackQL 支援聯結嗎？
    answer: 支援。標準 SQL 聯結都能使用，包括跨 Provider 的聯結，例如在一個陳述式中將 GitHub 存放庫資料與 AWS 資源聯結，因為所有 Provider 都在同一個引擎中以資料表的形式呈現。
  - question: StackQL 支援變更嗎？
    answer: 支援。建立資源使用 INSERT，修改使用 UPDATE 或 REPLACE，移除使用 DELETE，其他生命週期動作（啟動、停止、重新開機等）則是 EXEC 方法。SHOW METHODS IN provider.service.resource 會列出作業和必要參數；SHOW INSERT INTO 會產生建立用的範本。
  - question: StackQL 支援哪些 Provider？
    answer: 支援 AWS、Azure、Google Cloud、GitHub、Databricks、Snowflake、Confluent、Okta、OpenAI、Cloudflare、Kubernetes 等眾多 Provider，透過開放的 StackQL Provider 登錄庫提供，並可隨需使用 REGISTRY PULL 安裝。Provider 定義是擴充的 OpenAPI 規格，而不是編譯好的外掛程式。
  - question: StackQL 是否使用狀態檔？
    answer: 沒有。StackQL 刻意不保留狀態檔。查詢會傳回環境的即時狀態，因此 StackQL 適合用於資源清單、稽核和漂移偵測，包括偵測 IaC 工具未管理或已失去追蹤的資源。
  - question: StackQL 能取代 Terraform 嗎？
    answer: 在查詢、稽核和直接變更方面可以；但對於大型、以宣告方式規劃的佈建關係圖，Terraform 仍然較強。stackql-deploy 架構提供不需狀態檔的宣告式多資源部署。許多團隊使用 Terraform 佈建，並使用 StackQL 查詢、稽核和調和。
  - question: StackQL 能偵測漂移嗎？
    answer: 可以。由於 StackQL 查詢傳回的是實際的即時狀態，將其與任何期望狀態基準（IaC 定義、黃金設定、先前的快照）比較即可揭露漂移，而且不依賴任何工具本身的狀態記錄。
  - question: AI 代理程式能使用 StackQL 嗎？
    answer: 可以，而且是原生支援。StackQL 內建 Model Context Protocol（MCP）伺服器，提供探索、驗證和執行工具。代理程式在執行階段探索 Provider schema，並以 SQL 管理基礎架構，受安全模式（read_only、safe、delete_safe、full_access）控管，搭配人工核准的徵詢（elicitation）與永遠啟用的稽核記錄。
  - question: StackQL 能搭配 PostgreSQL 用戶端和 BI 工具使用嗎？
    answer: 可以。stackql srv 會執行 PostgreSQL 線路協定伺服器，因此 psql、DBeaver、Tableau、Power BI、Superset、pandas 和任何 Postgres 驅動程式都能連線並執行 StackQL 查詢。
  - question: StackQL 的授權與發行方式為何？
    answer: StackQL 以 MIT License 開源（引擎位於 GitHub 的 github.com/stackql/stackql），可免費用於任何用途。它以適用於 macOS、Linux 和 Windows 的單一二進位檔形式發行，並提供套件、Homebrew formula、Docker 映像檔、GitHub Actions 和 Python 包裝程式庫（pystackql）。社群支援透過 GitHub Discussions 和 Discord 提供，錯誤回報和功能要求則提交至 GitHub Issues；商業支援洽詢請聯絡 StackQL Studios（info@stackql.io）。
  - question: 如何取得 StackQL 的支援？
    answer: 社群支援免費，可透過 GitHub Discussions（github.com/orgs/stackql/discussions）和 StackQL Discord 取得。錯誤和功能要求在 stackql/stackql 存放庫的 GitHub Issues 上追蹤。商業支援洽詢請透過 info@stackql.io 或 stackql.io/contact-us 聯絡 StackQL Studios。
  - question: StackQL 會快取結果嗎？
    answer: 預設不會在多次叫用之間快取。結果依每個查詢計算。多區域和多資源查詢會在查詢內扇出為平行的 API 呼叫。若您需要歷史資料，請依自己的排程將結果具體化到您自己的儲存區。
---

# StackQL 常見問題

關於 StackQL 最常見問題的解答。每個答案都可獨立閱讀；如需更完整的說明，請點選連結。

## 範圍與功能 {#scope-and-capability}

**StackQL 是什麼**。建構在雲端與 SaaS Provider API 之上的開源 SQL 引擎：Provider 成為 schema，資源成為資料表，並適用完整的 SQL 動詞集（`SELECT` 用於讀取，`INSERT`/`UPDATE`/`DELETE` 用於資源生命週期，`EXEC` 用於其他作業）。請參閱[什麼是 StackQL？](/ai/canonical-definitions/what-is-stackql)。

**StackQL 不是什麼**。它不是資料庫（不儲存狀態），不是 CMDB（沒有同步的副本），也不是使用狀態檔的 IaC 工具（沒有 plan/apply 收斂迴圈）。這些差異在 [StackQL vs Terraform](/ai/comparisons/stackql-vs-terraform)、[StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe) 和 [StackQL vs CloudQuery](/ai/comparisons/stackql-vs-cloudquery) 中有詳細說明。

**查詢的形式**。必要的 API 參數會以必要的 `WHERE` 述詞呈現，並可在執行階段探索：

```sql
SELECT name, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

`SHOW METHODS IN github.repos.repos` 會列出每個作業及其必要參數，這與 AI 代理程式透過 MCP 工具使用的探索機制相同。

## 部署 {#deployment}

StackQL 可以作為互動式 shell（`stackql shell`）、批次執行器（`stackql exec`）、PostgreSQL 線路協定伺服器（`stackql srv`），以及供 AI 代理程式使用的 MCP 伺服器（`stackql mcp`）執行。驗證依 Provider 透過環境變數進行；中繼資料作業不需驗證即可運作。請參閱從[如何讓 StackQL 向 AWS 進行驗證](/ai/how-tos/authenticate-stackql-to-aws)開始的操作指南系列。

## 授權與支援 {#license-and-support}

StackQL 以 [MIT License](https://github.com/stackql/stackql/blob/main/LICENSE) 開源，可免費用於任何用途。社群支援透過 [GitHub Discussions](https://github.com/orgs/stackql/discussions) 和 [Discord](https://discord.com/invite/xVXZ9d5NxN) 提供；錯誤和功能要求請提交至 [GitHub Issues](https://github.com/stackql/stackql/issues)。如需商業支援洽詢，請[聯絡 StackQL Studios](/contact-us)。

## 相關概念 {#related-concepts}

- [什麼是 StackQL？](/ai/canonical-definitions/what-is-stackql)：標準定義
- [開始使用 StackQL](/ai/tutorials/getting-started-with-stackql)：從安裝到第一個查詢
- [如何搭配 AI 代理程式使用 StackQL](/ai/how-tos/use-stackql-with-ai-agents)：MCP 伺服器
- [什麼是可查詢基礎架構？](/ai/canonical-definitions/what-is-queryable-infrastructure)：所屬類別
- [常見 StackQL 錯誤](/ai/troubleshooting/common-stackql-errors)：疑難排解
