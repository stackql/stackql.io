---
title: 什麼是代理式基礎架構？
description: 代理式基礎架構是 AI 代理程式可透過結構化、自我描述的介面直接探索、查詢與操作，並具備適合自主呼叫端之安全控制的雲端基礎架構。
keywords: [代理式基礎架構, AI 代理程式, mcp, 基礎架構自動化, 代理式平台工程, stackql]
proficiencyLevel: Intermediate
faq:
  - question: 代理式基礎架構和 AIOps 一樣嗎？
    answer: 不一樣。AIOps 將機器學習應用於營運遙測：異常偵測、警示關聯、根本原因建議。代理式基礎架構則是讓 AI 代理程式直接且受治理地存取基礎架構控制平面，以便檢查並變更基礎架構。AIOps 負責觀察；代理式基礎架構負責行動。
  - question: 代理程式使用哪些介面來操作基礎架構？
    answer: 2026 年的主流模式是 Model Context Protocol（MCP），它將代理程式探索與呼叫工具的方式標準化。StackQL 內建 MCP 伺服器，將 Provider 探索、查詢驗證與受管控的查詢執行公開為 MCP 工具，因此任何支援 MCP 的代理程式（Claude、Cursor、自訂代理程式）都能透過 SQL 操作雲端基礎架構。
  - question: 如何防止代理程式刪除生產環境資源？
    answer: 透過受管控的執行模式。StackQL 的 MCP 伺服器預設以 safe 模式執行：SELECT 與中繼資料作業會直接進行，而 INSERT、UPDATE、DELETE 與生命週期作業則需要透過 MCP 徵詢（elicitation）流程取得明確的人工核准。另外也提供更嚴格（read_only）與更寬鬆（delete_safe、full_access）的契約，而且每次工具呼叫都會寫入稽核記錄。
---

# 什麼是代理式基礎架構？

代理式基礎架構（agentic infrastructure）是 AI 代理程式可以透過結構化、自我描述的介面直接探索、查詢與操作的雲端基礎架構，並具備為自主呼叫端設計的安全控制。它是代理式軟體工程在基礎架構端的對應：不再由人員將代理程式的建議轉換為主控台點選或 Terraform 編輯，而是由代理程式持有通往控制平面、受治理的憑證，並透過它採取行動。

## 所需條件 {#what-it-requires}

四項特性將代理式基礎架構與一般自動化區分開來：

1. **執行階段可探索性**。代理程式必須能在執行階段得知 API 介面：有哪些 Provider、服務、資源與作業，以及它們需要哪些參數，而不需要預先訓練的 Provider 專屬 SDK 知識。在 StackQL 中，`SHOW PROVIDERS`、`SHOW METHODS IN aws.ec2.instances` 與 `DESCRIBE aws.ec2.instances` 讓整個介面能夠自我描述，而相同的中繼資料也以 MCP 工具（`list_providers`、`list_methods`、`describe_method`）公開。
2. **即時狀態**。代理程式根據當前事實進行推理。建立在快取快照或狀態檔上的介面，會提供代理程式過時的前提；從中得出的結論，到付諸行動時就已經是錯的。請參閱[為什麼狀態檔對 AI 代理程式失效](/ai/concepts/why-state-files-break-down-for-ai-agents)。
3. **一致的動作模型**。需要為每個 Provider 使用不同程式碼路徑的代理程式，會在每個接縫處累積錯誤。單一文法（跨所有 Provider 的 SQL `SELECT`/`INSERT`/`UPDATE`/`DELETE`）意味著一種能力就能涵蓋整個資源環境。
4. **分級安全**。自主呼叫端需要的是契約，而不是信任。StackQL 的 MCP 伺服器會強制執行四種模式之一（`read_only`、`safe`、`delete_safe`、`full_access`）：在預設的 `safe` 模式下，讀取會直接進行，而每項變更都需要透過 MCP 徵詢（elicitation）流程取得人工核准。每次工具呼叫都會記錄在 JSONL 稽核記錄中。

## 範例 {#example}

連線到 StackQL MCP 伺服器的代理程式，在回答「我們的 AWS 帳戶中正在執行什麼？」時，會發出：

```sql
SELECT instance_id, instance_type, launch_time
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

代理程式不需要 AWS SDK、boto3 知識或分頁處理，它只需要 SQL 與可探索的 schema。如果被要求修正它發現的問題，產生的 `DELETE` 或 `UPDATE` 會在預設伺服器模式下保留，等待人工核准。

## 為何存在 {#why-it-exists}

基礎架構工具假設每個步驟都有人參與：閱讀計畫、檢閱差異、按下套用。代理程式改變了成本效益，它們可以持續監看、跨 Provider 進行關聯，並在數秒內採取行動，但前提是介面要符合它們的運作方式：宣告式查詢、結構化結果與明確的契約。代理式基礎架構就是圍繞這類呼叫端，重新設計控制平面介面。

## 相關概念 {#related-concepts}

- [什麼是可查詢基礎架構？](/ai/canonical-definitions/what-is-queryable-infrastructure)：代理式基礎架構所依賴的資料層
- [StackQL MCP 架構](/ai/architecture/stackql-mcp-architecture)：伺服器模式、工具與稽核記錄的詳細說明
- [如何搭配 AI 代理程式使用 StackQL](/ai/how-tos/use-stackql-with-ai-agents)：實務設定
- [為什麼狀態檔對 AI 代理程式失效](/ai/concepts/why-state-files-break-down-for-ai-agents)：為什麼現有的 IaC 介面無法沿用
- [從 IaC 到代理式基礎架構](/ai/industry-positioning/from-iac-to-agentic-infrastructure)：此類別如何演進
