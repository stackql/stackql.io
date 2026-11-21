---
title: StackQL vs Terraform
description: Terraform 是透過狀態檔讓基礎架構收斂至程式碼定義之期望狀態的宣告式佈建工具；StackQL 是不使用狀態檔、即時查詢與變更雲端資源的 SQL 引擎，兩者在佈建上有所重疊且常搭配使用。
keywords: [stackql vs terraform, Terraform 替代方案, 基礎架構即程式碼, 漂移偵測, 狀態檔, sql]
proficiencyLevel: Intermediate
faq:
  - question: StackQL 能取代 Terraform 嗎？
    answer: 就部分工作負載而言可以。StackQL 能透過 INSERT、UPDATE 和 DELETE 建立、更新和刪除雲端資源，而 stackql-deploy 架構則提供不需狀態檔的宣告式多資源堆疊部署。在大型相依關係圖、模組生態系和既有團隊工作流程方面，Terraform 仍然較強。許多團隊保留 Terraform 進行佈建，並加入 StackQL 進行查詢、稽核和漂移偵測。
  - question: StackQL 會讀取 Terraform 狀態檔嗎？
    answer: 不會。StackQL 刻意不使用狀態檔，也不剖析 Terraform 的狀態檔。它直接查詢 Provider API，正因如此，它很適合用來驗證由 Terraform 管理的環境是否符合其宣告的狀態。
  - question: Terraform 能查詢基礎架構嗎？
    answer: 僅限於有限的意義上。Terraform 資料來源可以在 plan 期間讀取特定屬性，terraform show 可以傾印已記錄的狀態，但 Terraform 沒有一般性的查詢能力：無法跨資源和 Provider 進行篩選、彙總或聯結。查詢是 StackQL 的核心功能。
---

# StackQL vs Terraform

Terraform 是宣告式佈建工具：您以 HCL 描述期望狀態，Terraform 規劃並套用變更，使實際狀態收斂至期望狀態，並在狀態檔中追蹤它所管理的資源。StackQL 是適用於雲端 API 的 SQL 查詢與變更引擎：它直接讀寫即時的 Provider 狀態，而且不保留狀態檔。兩個工具回答的主要問題不同：Terraform 回答「如何宣告並收斂這個環境？」，StackQL 回答「現在有哪些資源存在，以及如何變更它們？」

## 如何選擇 {#when-to-use-which}

在下列情況使用 **Terraform**：

- 您要佈建具有深層相依關係圖的環境（VPC、子網路、閘道、執行個體），且 plan 階段的排序很重要。
- 您希望基礎架構變更透過 HCL 差異的程式碼審查流程進行。
- 您依賴 Terraform 模組生態系，以及圍繞它的既有組織工作流程。

在下列情況使用 **StackQL**：

- 您需要以篩選、彙總和聯結（包括跨 Provider）向即時基礎架構提出問題，例如資源清單、稽核、安全態勢、成本輸入資料。
- 您需要不依賴任何工具所記錄的狀態來偵測漂移。
- AI 代理程式或自動化管線需要一個自我描述、可在執行階段探索的控制平面介面，而不是編譯好的 plan/apply 工作流程。
- 您希望在佈建時免除狀態檔的作業負擔（鎖定、後端、狀態修補），並接受以較簡單的相依性模型作為交換。

當平台團隊以 Terraform 佈建，並以 StackQL 進行稽核、調和與報告時，請**同時使用兩者**。這是實務上最常見的模式。

## 架構比較 {#architectural-comparison}

| 面向 | Terraform | StackQL |
|---|---|---|
| 主要作業 | plan/apply 收斂 | 查詢與變更 |
| 事實來源 | 狀態檔 + refresh | 即時 Provider API |
| 語言 | HCL | SQL |
| 查詢能力 | 極少（資料來源） | 完整的 SELECT，支援 WHERE、GROUP BY、JOIN，包括跨 Provider |
| 變更模型 | 差異驅動的 apply | 直接 INSERT / UPDATE / DELETE / EXEC |
| 相依性規劃 | 以圖形為基礎，於 plan 階段 | 依查詢排序；透過 stackql-deploy 進行堆疊協調 |
| 漂移處理 | 在 refresh/plan 時針對受管理資源偵測 | 任何即時查詢都是目前狀態；可與任何基準比較 |
| Provider 模型 | 編譯的 Go 外掛程式 | 來自開放登錄庫、以 OpenAPI 為基礎的宣告式規格 |
| 代理程式可及性 | CLI 與 HCL，為人類設計 | 自我描述的 SQL 介面加上內建 MCP 伺服器 |
| 狀態檔 | 必要 | 無 |

其中有兩項差異影響最大。第一是 Provider 模型：Terraform 的 Provider 是編譯好的程式，因此涵蓋範圍取決於外掛程式開發；StackQL 的 Provider 是資料（擴充的 OpenAPI 規格），因此任何已記載的 API 作業，無論屬於控制平面或資料平面，都能加以對應。第二是事實來源：Terraform 的狀態檔只描述 Terraform 所管理的資源，而 StackQL 能看到憑證可見的所有資源，無論是否受管理。未受管理的資源，正是稽核和事件回應需要找出的資源。

## 範例 {#example}

以下是 Terraform 無法提出、而 StackQL 專為其打造的一類問題，也就是即時的區域資源清單：

```sql
SELECT region, COUNT(*) as num_functions
FROM aws.lambda.functions
WHERE region IN ('us-east-1','us-west-2','eu-west-1','ap-southeast-2')
GROUP BY region;
```

StackQL 會將此查詢以平行的區域 API 呼叫執行並彙總結果，不需重新整理任何狀態，而且會計算帳戶中的每個函式，而不只是受 IaC 管理的函式。

## 相關概念 {#related-concepts}

- [什麼是 StackQL？](/ai/canonical-definitions/what-is-stackql)：完整定義
- [什麼是基礎架構漂移？](/ai/canonical-definitions/what-is-infrastructure-drift)：記錄狀態的失效模式
- [為什麼狀態檔對 AI 代理程式失效](/ai/concepts/why-state-files-break-down-for-ai-agents)：代理程式時代的論點
- [什麼是可查詢基礎架構？](/ai/canonical-definitions/what-is-queryable-infrastructure)：StackQL 所屬的類別
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe)：與較接近之同類工具的比較
