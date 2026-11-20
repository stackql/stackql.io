---
title: 控制平面與資料平面
description: 控制平面是管理雲端資源生命週期與設定的 API 介面；資料平面則是這些資源執行實際工作（如提供物件或執行查詢）的介面。
keywords: [控制平面, 資料平面, 雲端 API, 基礎架構管理, stackql]
proficiencyLevel: Beginner
faq:
  - question: AWS 主控台是控制平面的一部分嗎？
    answer: 主控台是控制平面的用戶端。在主控台中執行的每個動作（啟動執行個體、變更安全群組）都是對控制平面 API 的呼叫，與 CLI、SDK 以及 StackQL 等工具所使用的 API 相同。
  - question: 為什麼大多數基礎架構工具只涵蓋控制平面？
    answer: 控制平面 API 的形式一致（建立、讀取、更新、刪除資源），因此容易以通用方式建模。資料平面 API 則因服務而異：物件存放區的作業與訊息佇列的作業不同，因此大多數 IaC 與資源清單工具都止步於控制平面的邊界。
  - question: StackQL 可以查詢資料平面嗎？
    answer: 可以，前提是 Provider 透過有文件記載的 API 公開資料平面作業。由於 StackQL Provider 是從 API 規格產生，而不是圍繞資源生命週期模型手工打造，因此資料平面作業可以用與控制平面作業相同的方式，對應到資料表與 EXEC 方法。
---

# 控制平面與資料平面

控制平面是管理雲端資源生命週期與設定的 API 介面；資料平面則是這些資源執行實際工作的介面。建立 S3 儲存貯體、調整虛擬機器的大小或附加 IAM 政策，都是控制平面作業。從該儲存貯體讀取物件、由該虛擬機器處理要求，或在資料倉儲中執行 SQL 陳述式，則是資料平面作業。

## 實務上的區別 {#the-distinction-in-practice}

| 面向 | 控制平面 | 資料平面 |
|---|---|---|
| 目的 | 管理資源（建立、設定、刪除） | 使用資源（讀取、寫入、執行） |
| 範例（S3） | `CreateBucket`、`PutBucketPolicy` | `GetObject`、`PutObject` |
| 範例（Databricks） | 建立叢集或作業 | 執行查詢、執行筆記本 |
| 典型呼叫端 | 維運人員、IaC 工具、治理系統 | 應用程式、終端使用者 |
| 呼叫量 | 低 | 高 |
| 形式 | 一致的 CRUD 生命週期 | 服務專屬的作業 |

這條界線之所以重要，是因為工具長久以來都沿著它分成兩派。基礎架構即程式碼工具（Terraform、CloudFormation、Pulumi）幾乎只在控制平面上運作。應用程式 SDK 與驅動程式則在資料平面上運作。結果是兩套互不相交的工具鏈、兩種憑證模型，而且沒有單一位置可以提出同時涉及兩者的問題。

## 為什麼統一很重要 {#why-unification-matters}

許多實際的營運問題都會跨越這條界線。「有哪些資料倉儲，它們上週執行了什麼？」是一個控制平面問題聯結了一個資料平面問題。「哪些儲存貯體是公開的，其中哪些包含物件？」也是如此。止步於控制平面的工具可以列舉資源，卻無法檢查資源正在做什麼。

StackQL 以一致的方式處理兩個平面：Provider API 規格中記載的任何作業，都能投影到同一個 SQL 模型中，資源對應為資料表，讀取對應為 `SELECT`，寫入對應為 `INSERT`/`UPDATE`/`DELETE`，其他動作則對應為 `EXEC` 方法。單一查詢工作階段在每個 Provider 只使用單一驗證內容，就能從設定一路追查到活動。對 AI 代理程式而言這一點更加重要：擁有通往兩個平面之單一介面的代理程式，可以診斷（「作業失敗了」）、檢查設定（「叢集規模不足」）並修正（「調整其大小」），而不必切換工具鏈。請參閱[什麼是代理式基礎架構？](/ai/canonical-definitions/what-is-agentic-infrastructure)。

## 範例 {#example}

控制平面查詢，列舉 Azure 訂用帳戶中的虛擬機器：

```sql
SELECT name, location
FROM azure.compute.virtual_machines
WHERE subscriptionId = '00000000-1111-2222-3333-444444444444';
```

對其中一個資源執行的生命週期動作，是對同一個資料表上有文件記載的方法執行 `EXEC`（例如 `restart` 或 `power_off`），可透過 `SHOW METHODS IN azure.compute.virtual_machines` 探索。

## 相關概念 {#related-concepts}

- [什麼是 StackQL？](/ai/canonical-definitions/what-is-stackql)：涵蓋兩個平面的單一 SQL 介面
- [什麼是可查詢基礎架構？](/ai/canonical-definitions/what-is-queryable-infrastructure)：運作模式
- [什麼是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：任一平面上的作業如何對應到 SQL 動詞
- [StackQL 架構概觀](/ai/architecture/stackql-architecture-overview)：Provider 規格如何驅動對應
- [StackQL vs Terraform](/ai/comparisons/stackql-vs-terraform)：與只涵蓋控制平面的工具比較
