---
title: 什麼是 StackQL？
description: StackQL 是開源的查詢語言與執行階段，將雲端與 SaaS Provider API 公開為關聯式資料表，可用標準 SQL 查詢。
keywords: [stackql, sql, 雲端, api, 基礎架構, 可查詢基礎架構, AI 代理程式]
proficiencyLevel: Beginner
faq:
  - question: StackQL 是資料庫嗎？
    answer: 不是。StackQL 是查詢執行階段，而不是資料庫。它不會儲存雲端狀態，而是向 Provider（AWS、Azure、GCP、GitHub 等）發出即時 API 呼叫，並將回應呈現為關聯式資料列，以便用 SQL 查詢。結果是依需求計算的。
  - question: StackQL 會取代 Terraform 嗎？
    answer: 不會直接取代。Terraform 的主要工作是由狀態檔驅動的宣告式佈建。StackQL 的主要工作是在不使用狀態檔的情況下，透過 SQL 查詢及變更雲端資源。兩者在變更方面有所重疊（StackQL 可以 INSERT、UPDATE 及 DELETE 雲端資源），但 Terraform 提供更豐富的相依性規劃，StackQL 則提供更豐富的查詢能力。兩者經常搭配使用。
  - question: StackQL 支援哪些 Provider？
    answer: StackQL 支援 AWS、Azure、Google Cloud、GitHub、Databricks、Snowflake、Confluent、Okta、OpenAI、Cloudflare 等 Provider。每個 Provider 都是從 StackQL Provider 登錄庫提取的獨立套件。完整清單位於 /providers。
  - question: StackQL 與 Steampipe 之類的工具有何不同？
    answer: 兩者都將雲端 API 公開為可用 SQL 查詢的資料表。StackQL 另外將雲端變更（INSERT、UPDATE、DELETE）作為一級作業來支援、以一致的方式處理控制平面與資料平面 API，並提供 MCP 伺服器，讓 AI 代理程式能直接查詢及變更雲端狀態。Steampipe 以讀取為主；StackQL 則可讀可寫。
---

# 什麼是 StackQL？

StackQL 是開源的查詢語言與執行階段，將雲端與 SaaS Provider API 公開為關聯式資料表，可用標準 SQL 查詢。它將每個雲端資源（虛擬機器、儲存貯體、IAM 政策、GitHub 存放庫、Snowflake 倉儲）視為資料表中的一筆資料列，並將每個 API 作業視為 SELECT、INSERT、UPDATE 或 DELETE。

## 運作方式 {#how-it-works}

典型的基礎架構工具會將雲端狀態讀入本機檔案（Terraform 的狀態檔）、產生計畫，然後套用。StackQL 則完全略過狀態檔。當您執行查詢時，StackQL 會向相關的 Provider 發出即時 API 呼叫、將回應的形式正規化，並將結果以資料列傳回。除非使用者要求，否則不會在兩次叫用之間快取任何內容。

介面採用 SQL，是因為 SQL 是穩定且廣為人知的查詢語言，擁有成熟的工具（用戶端、驅動程式、BI 整合、AI 代理程式工具組），這些工具搭配 StackQL 都能「直接運作」。StackQL 在伺服器模式下實作 PostgreSQL 線路協定，這表示任何能與 Postgres 通訊的工具（psql、DBeaver、Tableau、Power BI、pandas、LangChain SQL 代理程式、MCP 伺服器）都能與 StackQL 通訊。

## 範例 {#example}

列出某個區域中的所有 EC2 執行個體，以及它們的類型與公用 IP 位址：

```sql
SELECT
  instance_id,
  instance_type,
  public_ip_address
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

該查詢會針對即時的 AWS EC2 API 執行。沒有需要重新整理的狀態檔、沒有需要失效的快取快照，也沒有需要學習的 Provider 專屬 SDK。相同的形式適用於任何 Provider：將 `aws.ec2.instances` 換成 `github.repos.repos` 或 `azure.compute.virtual_machines`，查詢模型完全相同。

## 為何存在 {#why-it-exists}

StackQL 的建構前提是：雲端基礎架構應該是可查詢的介面，而不是靜態的成品。過去十年主導基礎架構即程式碼的狀態檔模型，是為人員驅動、受變更控制的工作流程而設計：撰寫程式碼、規劃、檢閱、套用。這種模型在兩種壓力下顯得吃力：

1. **多雲端的現實**。每個大型組織都在多個 Provider 上執行工作負載。狀態檔是依 Provider、依工具、依團隊區分的。要跨 AWS、Azure、GitHub 與 Snowflake 回答「我們在生產環境中執行了什麼？」，需要手動彙總。
2. **AI 代理程式**。代理程式需要即時、結構化地存取基礎架構狀態，才能對其進行推理。狀態檔是錯誤的基本元素：它在寫入的那一秒就已過時，而且其格式專屬於特定工具。針對即時 API 的 SQL 介面才是正確的基本元素：代理程式懂 SQL、schema 穩定，而且資料永遠是最新的。

StackQL 的存在，是為了讓雲端基礎架構像資料庫一樣可查詢，跨越各個 Provider，同時服務人類維運人員與 AI 代理程式。

## 相關概念 {#related-concepts}

- [什麼是可查詢基礎架構？](/ai/canonical-definitions/what-is-queryable-infrastructure)：StackQL 所屬的更廣泛類別
- [什麼是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：StackQL 實作的設計模式
- [控制平面與資料平面](/ai/canonical-definitions/control-plane-vs-data-plane)：StackQL 如何統一兩者
- [StackQL vs Terraform](/ai/comparisons/stackql-vs-terraform)：與主流 IaC 工具的比較
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe)：與最接近的同類工具的比較
- [開始使用 StackQL](/ai/tutorials/getting-started-with-stackql)：從安裝到第一個查詢
