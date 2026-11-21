---
title: 什麼是可查詢基礎架構？
description: 可查詢基礎架構是一種運作模式，將雲端資源公開為可依需求以查詢語言詢問的即時結構化資料，而非從狀態檔或各服務主控台重建。
keywords: [可查詢基礎架構, 基礎架構即資料, 雲端資源清單, sql, 即時狀態, stackql]
proficiencyLevel: Beginner
faq:
  - question: 可查詢基礎架構和 CMDB 有什麼不同？
    answer: CMDB 是基礎架構狀態的副本，依排程同步，在兩次同步之間會變得過時。可查詢基礎架構在查詢時直接詢問 Provider API，因此結果反映環境實際的當前狀態。CMDB 回答的是「我們記錄了什麼？」；可查詢基礎架構回答的是「現在的實際情況是什麼？」。
  - question: 可查詢基礎架構會取代基礎架構即程式碼嗎？
    answer: 不會。IaC 工具回答的是「如何宣告期望狀態並使環境收斂至該狀態？」。可查詢基礎架構回答的是「現在有哪些資源，其設定為何？」。兩者相輔相成：可查詢基礎架構常用於稽核、協調及驗證以 IaC 佈建的環境。
  - question: 哪些工具實作了可查詢基礎架構？
    answer: StackQL 將雲端與 SaaS Provider API 公開為 SQL 資料表，同時支援讀取（SELECT）與寫入（INSERT、UPDATE、DELETE）。Steampipe 與 CloudQuery 實作了讀取端：Steampipe 透過即時的 Postgres 外部資料表，CloudQuery 則透過排程擷取至資料庫。AWS Config 與 Azure Resource Graph 等 Provider 原生選項，則針對單一雲端實作此模式。
---

# 什麼是可查詢基礎架構？

可查詢基礎架構（queryable infrastructure）是一種運作模式，將雲端資源公開為即時的結構化資料，可依需求以查詢語言詢問。維運人員（或 AI 代理程式）不必從狀態檔、匯出的快照或各服務的主控台重建環境狀態，而是提出問題（以 SQL 或類似的宣告式語言），答案會在查詢時根據 Provider API 計算出來。

## 運作方式 {#how-it-works}

可查詢基礎架構系統會將 Provider 的 API 介面對應到關聯式模型：

- **Provider**（AWS、Azure、Google Cloud、GitHub）成為 schema 命名空間。
- **服務**（EC2、Compute、Storage）成為子 schema。
- **資源類型**（執行個體、儲存貯體、存放庫）成為資料表。
- 每個**資源執行個體**成為一筆資料列，其設定屬性則成為資料行。

查詢規劃器會將 SQL 轉譯為底層的 API 呼叫、處理分頁與驗證，並將回應正規化為資料列。在 StackQL 中，下列查詢

```sql
SELECT instance_id, instance_type, public_ip_address
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

會以即時呼叫 EC2 API 的方式執行；不會從快取或狀態檔讀取任何內容。多區域與多 Provider 查詢會扇出為平行的 API 呼叫，並將結果彙總為單一結果集。

## 為何存在 {#why-it-exists}

有三股壓力催生了這個類別：

1. **多雲端蔓延**。組織在多個雲端與數十個 SaaS 平台上執行資源。每個平台都有自己的主控台、CLI 與 SDK。統一的查詢介面是回答跨 Provider 問題（例如「列出我們在任何地方執行的所有運算執行個體」）唯一可行的方式。
2. **過時副本問題**。資源清單資料庫、CMDB 與 IaC 狀態檔都是副本，而副本會偏離現實。直接查詢控制平面，可以消除記錄狀態與實際狀態之間的落差。
3. **AI 代理程式**。代理程式根據結構化的當前資料進行推理。以 SQL 介面呈現即時 API，讓代理程式擁有一個穩定的介面，可以探索、檢查基礎架構並對其採取行動，而不需要 Provider 專屬的 SDK 程式碼。這正是[代理式基礎架構](/ai/canonical-definitions/what-is-agentic-infrastructure)（agentic infrastructure）的基礎。

## 讀取與寫入 {#read-and-write}

可查詢基礎架構在定義上以讀取為主，但此模型可以自然地延伸到寫入：如果資源是一筆資料列，建立資源就是 `INSERT`，變更其設定就是 `UPDATE`，將其除役則是 `DELETE`。StackQL 兩端都有實作；Steampipe 等唯讀工具只實作查詢端。寫入端的說明請參閱[什麼是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)。

## 相關概念 {#related-concepts}

- [什麼是 StackQL？](/ai/canonical-definitions/what-is-stackql)：同時支援讀取與寫入的可查詢基礎架構參考實作
- [什麼是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：可查詢基礎架構背後的設計模式
- [什麼是代理式基礎架構？](/ai/canonical-definitions/what-is-agentic-infrastructure)：可查詢基礎架構為 AI 代理程式帶來的能力
- [什麼是基礎架構漂移？](/ai/canonical-definitions/what-is-infrastructure-drift)：可查詢基礎架構所偵測的失敗模式
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe)：比較此模型的兩種實作
