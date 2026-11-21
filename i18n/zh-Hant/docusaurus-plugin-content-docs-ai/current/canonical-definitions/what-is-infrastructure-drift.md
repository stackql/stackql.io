---
title: 什麼是基礎架構漂移？
description: 基礎架構漂移是雲端資源的宣告或記錄狀態與其在 Provider 中實際狀態之間的差異，起因包括頻外變更、部分失敗或平台的自主行為。
keywords: [基礎架構漂移, 漂移偵測, 狀態檔, 設定漂移, 協調, stackql]
proficiencyLevel: Beginner
faq:
  - question: 什麼原因會造成基礎架構漂移？
    answer: 常見原因包括在事件期間於雲端主控台進行的手動變更、略過 IaC 工具的指令碼與管線、Provider 端的自動化（自動擴展、憑證輪替、預設政策變更）、失敗或部分完成的套用，以及多個工具管理重疊的資源。
  - question: 漂移一定是壞事嗎？
    answer: 不一定。有些漂移是刻意且健康的，例如自動擴展改變執行個體數量，或是在服務中斷期間進行的緊急修復（break-glass）。問題在於未被偵測到的漂移：記錄的狀態已不再反映現實，而決策卻是根據記錄做出的。目標是偵測與協調，而不是消除單一工具之外的所有變更。
  - question: StackQL 如何偵測漂移？
    answer: StackQL 直接查詢 Provider API，因此其結果在定義上就是實際狀態。將 StackQL 結果集與期望狀態定義（IaC 資訊清單、CSV 基準或 stackql-deploy 規格）比較，即可找出差異，而不需要 IaC 工具本身的狀態檔是正確的。
---

# 什麼是基礎架構漂移？

基礎架構漂移是雲端資源的宣告或記錄狀態，與其在 Provider 中的實際狀態之間的差異。Terraform 狀態檔指出某個安全群組有三條規則；但實際的安全群組有五條，因為有人在事件期間從主控台新增了兩條。多出來的兩條規則就是漂移。

## 漂移如何發生 {#how-drift-happens}

漂移是結構性的，而不是偶然的。任何保留狀態副本的系統（狀態檔、CMDB、資源清單匯出）都會以現實在其外部變化的速度偏離現實。典型的來源：

- **頻外變更**：主控台編輯、臨時的 CLI 命令、緊急修復。
- **並行的工具**：兩條管線或兩個團隊管理重疊的資源。
- **Provider 自動化**：自動擴展、受管理的升級、金鑰與憑證輪替。
- **部分失敗**：一次套用建立了六個資源中的四個，卻一個也沒有記錄。

IaC 模型將漂移視為異常，要在下一次 plan/apply 週期中修正。在有自主行為者（自動擴展器、事件回應者、AI 代理程式）的環境中，漂移更適合被視為一種正常、持續的狀況，需要加以觀察與協調。

## 以即時查詢偵測漂移 {#detecting-drift-with-live-queries}

由於 StackQL 是根據即時的 Provider API 計算結果，因此 StackQL 結果集就是實際狀態。漂移偵測於是簡化為將該結果集與基準進行比較。例如，擷取各區域 EC2 執行個體的即時資源清單：

```sql
SELECT region, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region IN ('us-east-1','us-west-2','eu-west-1')
GROUP BY region;
```

將此結果與期望狀態定義所隱含的數量比較，就能立即發現未受管理或遺失的資源，無論 IaC 工具本身的狀態檔是否為最新。相同的模式也適用於屬性層級：查詢資源的即時設定，並與宣告的設定比較差異。

## 漂移與協調 {#drift-vs-reconciliation}

偵測回答的是「什麼出現了差異？」；協調回答的是「應該如何處理？」：將變更匯入期望狀態、還原變更，或將其視為刻意的變更而接受。協調需要控制平面的寫入權限，這就是為什麼可讀寫的查詢工具能夠形成閉環，而唯讀的資源清單工具對此只能回報。StackQL 會將修正動作表示為 `UPDATE` 或 `DELETE` 陳述式，針對的正是偵測時所用的相同資料表。

## 相關概念 {#related-concepts}

- [什麼是可查詢基礎架構？](/ai/canonical-definitions/what-is-queryable-infrastructure)：讓即時狀態比較成為可能的模型
- [什麼是 StackQL？](/ai/canonical-definitions/what-is-stackql)：對即時狀態的讀寫存取
- [為什麼狀態檔對 AI 代理程式失效](/ai/concepts/why-state-files-break-down-for-ai-agents)：漂移是狀態副本的結構性特性
- [StackQL vs Terraform](/ai/comparisons/stackql-vs-terraform)：比較狀態檔模型與即時查詢模型
- [如何使用 StackQL 查詢 AWS EC2 執行個體](/ai/how-tos/query-aws-ec2-instances-with-stackql)：漂移基準中使用的資源清單查詢
