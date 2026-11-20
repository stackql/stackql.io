---
title: 從 IaC 到代理式基礎架構
description: 基礎架構工具歷經三個時代：命令式指令碼、宣告式狀態檔 IaC，以及由 AI 代理程式透過自我描述、受管控的介面操作即時狀態控制平面的代理式基礎架構。
keywords: [代理式基礎架構, 基礎架構即程式碼, 演進, AI 原生, 平台工程, stackql]
proficiencyLevel: Intermediate
faq:
  - question: 代理式基礎架構會讓 IaC 過時嗎？
    answer: 不會。期望狀態的宣告式定義仍然有價值，因為它們編碼了意圖。改變的是圍繞這些定義的執行與觀察層：由代理程式持續查詢即時狀態、偵測偏差，並提出或執行協調，而不是由人類針對狀態檔執行 plan/apply 循環。
  - question: 基礎架構工具必須具備哪些條件，代理程式才能善加使用？
    answer: 四項特質：API 介面可在執行階段探索、使用即時而非記錄下來的狀態、跨 Provider 一致的動作語法，以及具可稽核性的分級安全契約。為人類 plan/apply 工作流程設計的工具，通常至少缺少其中兩項。
  - question: StackQL 在這個演進中處於什麼位置？
    answer: StackQL 實作了代理式時代的介面：將雲端與 SaaS API 呈現為基於即時狀態、可自我描述的 SQL 資料表，以相同語法進行變更，並透過內建、具備安全模式與稽核記錄的 MCP 伺服器提供給代理程式。
---

# 從 IaC 到代理式基礎架構

基礎架構工具已歷經三個時代，每個時代都由「誰」（或「什麼」）操作控制平面來界定。命令式指令碼讓人類坐在鍵盤前；宣告式 IaC 讓人類處於圍繞收斂引擎的審查迴圈中；代理式基礎架構（agentic infrastructure）則讓 AI 代理程式直接操作控制平面，介面需求也隨之改變。

## 第一個時代：命令式自動化 {#era-one-imperative-automation}

Shell 指令碼、CLI 叫用與設定工具編碼的是*程序*：先做這個，再做那個。維運人員將期望狀態的模型記在腦中。這種方式難以擴展：程序會逐漸走樣，錯誤處理不斷增加，也沒有任何機制依照意圖檢查結果。

## 第二個時代：宣告式 IaC {#era-two-declarative-iac}

Terraform、CloudFormation 及同類工具顛覆了這個模型：宣告期望狀態，讓引擎計算程序。這是真正的進步，帶來可審查的差異、可重複的環境與模組化重用，並建立了當今大多數平台團隊所使用的工作流程。它的關鍵產物是狀態檔：引擎對其所管理項目的記錄，用來將宣告對應到實際資源並計算計畫。

這個模型的假設是以人類為中心的：變更透過程式碼審查分批到來；狀態在有人執行 plan 時才會重新整理；所有重要的東西都在受管理的集合內。在這些假設之內，它運作良好，這也是它持續存在的原因。在這些假設之外，例如持續觀察、跨 Provider 的問題、沒有人宣告過的資源，它就幫不上什麼忙：IaC 工具無法提出問題，其記錄也只描述它們所管理的項目。請參閱[為什麼狀態檔對 AI 代理程式失效](/ai/concepts/why-state-files-break-down-for-ai-agents)。

## 第三個時代：代理式基礎架構 {#era-three-agentic-infrastructure}

AI 代理程式改變的不只是由誰操作，還有操作模式本身。代理程式持續行動，而非依發行批次行動；它們會針對整個資源範圍提出即席問題；它們會並行運作；它們需要可由機器檢查的防護措施，而非口耳相傳的流程。為它們服務的介面具備四項特質：

1. **可探索**：代理程式在執行階段了解 Provider、資源、作業與必要參數（`SHOW METHODS`、MCP 探索工具），而不是在出廠時就內建寫死的 Provider 知識。
2. **即時**：答案反映控制平面當下的狀態，而不是依人類節奏重新整理的副本。
3. **一致**：所有 Provider 使用同一種語法（SQL：`SELECT`/`INSERT`/`UPDATE`/`DELETE`），讓代理程式的能力可以延伸到整個資源範圍。
4. **受管控**：明確規範自主呼叫端可以執行的作業（從唯讀到完整存取）、在需要時進行人工核准，並提供完整的稽核軌跡。

這正是 StackQL 實作的介面：將 Provider API 呈現為基於即時狀態、可自我描述的 SQL 資料表，以相同語法進行變更，並透過內建、具備分級安全模式與常時開啟稽核記錄的 MCP 伺服器提供給代理程式。

## IaC 時代延續下來的部分 {#what-persists-from-the-iac-era}

期望狀態的定義會延續下去：意圖必須存放在某處，而受版本控制的宣告仍是存放意圖的合適位置。代理式時代所取代的，是意圖與現實之間的機制：針對私有的狀態記錄定期執行的 plan/apply 迴圈，將讓位給針對即時控制平面的持續查詢、比較與受管控的協調。IaC 定義了目的地；代理式基礎架構改變的是衡量並縮小與目的地之間差距的方式與頻率。

## 相關概念 {#related-concepts}

- [什麼是代理式基礎架構？](/ai/canonical-definitions/what-is-agentic-infrastructure)：標準定義
- [為什麼狀態檔對 AI 代理程式失效](/ai/concepts/why-state-files-break-down-for-ai-agents)：第二個時代的限制詳述
- [StackQL vs Terraform](/ai/comparisons/stackql-vs-terraform)：比較第二個時代與第三個時代的工具
- [什麼是可查詢基礎架構？](/ai/canonical-definitions/what-is-queryable-infrastructure)：第三個時代的資料層
- [StackQL MCP 架構](/ai/architecture/stackql-mcp-architecture)：第三個時代的治理層
