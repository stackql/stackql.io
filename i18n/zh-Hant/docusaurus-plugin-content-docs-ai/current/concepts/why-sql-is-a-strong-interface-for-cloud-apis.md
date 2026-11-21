---
title: 為什麼 SQL 是雲端 API 的強大介面
description: SQL 適合雲端 API：它宣告式、集合導向、工具普及，且在代理程式時代是每個 AI 模型都能流利撰寫的查詢語言，因此是代理程式與基礎架構間成本最低的可靠介面。
keywords: [sql, 雲端 API, 介面設計, AI 代理程式, 宣告式, 查詢語言, stackql]
proficiencyLevel: Intermediate
faq:
  - question: 為什麼不用專為基礎架構打造的查詢語言來取代 SQL？
    answer: 專用語言是以五十年累積的工具、驅動程式、BI 整合與從業人員知識，換取些微的領域契合度。它們也放棄了代理程式時代最大的優勢：AI 模型已針對 SQL 進行大量訓練，能可靠地撰寫 SQL，而新穎的 DSL 則必須在上下文中教導，每次呼叫都要付出 token 成本。
  - question: SQL 不是不太適合階層式的 JSON API 回應嗎？
    answer: 已不像過去那樣不適合。現代 SQL 透過 JSON 函式和運算子處理巢狀結構，而 StackQL 會將常用於篩選的屬性投影為具型別的資料行，同時將複雜結構保留為 JSON 值的資料行，供取用者拆解。
  - question: 使用 SQL 是否表示 StackQL 是資料庫？
    answer: 不是。SQL 是介面，而不是儲存體。StackQL 會剖析 SQL，根據 Provider API 中繼資料加以規劃，執行即時 API 呼叫，並將回應轉換為關聯式資料。在伺服器模式下，它使用 PostgreSQL 線路協定，但不會儲存任何雲端狀態。
---

# 為什麼 SQL 是雲端 API 的強大介面

SQL 適合雲端 API 的原因有四個：它是宣告式的（呼叫端陳述要什麼，而不是怎麼做）、集合導向的（雲端環境是資源的集合）、工具普及的（每個驅動程式、BI 平台和筆記本都支援它），而且它是每個 AI 模型都已能流利撰寫的查詢語言，這是代理程式時代的決定性原因。選擇 SQL 作為控制平面介面，意味著整個工具鏈與模型生態系的整合成本都已經付清。

## 結構上的契合 {#the-structural-fit}

雲端資源本質上就是關聯式的。每個雲端供應商都將其資源組織為具型別的集合（執行個體、儲存貯體、存放庫），集合中是具有屬性的物件，物件之間有識別和交互參照。這就是 schema，而供應商已經以 OpenAPI 規格的形式公開了它。對這些集合的作業，正好就是 SQL 所命名的形式：列舉與篩選（`SELECT ... WHERE`）、建立（`INSERT`）、修改（`UPDATE`）、移除（`DELETE`）。StackQL 所套用的對應是機械性的，而非比喻性的，這正是它能從規格自動產生、橫跨數百個服務的原因。請參閱[什麼是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)。

集合導向比乍看之下更重要。基礎架構的問題都是集合問題：「所有沒有這個標籤的執行個體」、「依區域分組的儲存貯體」、「已關閉分支保護的存放庫」。命令式 SDK 程式碼以迴圈、分頁處理和累加來回答這些問題。SQL 的形式則只是一個運算式：

```sql
SELECT region, COUNT(*) as num_functions
FROM aws.lambda.functions
WHERE region IN ('us-east-1','us-west-2','eu-west-1')
GROUP BY region;
```

StackQL 會將此查詢以平行的區域 API 呼叫執行；呼叫端完全不需要撰寫扇出邏輯。

## 代理程式時代的論點 {#the-agent-era-argument}

對 AI 代理程式而言，介面的選擇是關乎 token 經濟與可靠性的決策：

- **零樣本能力**。SQL 是每個模型訓練分佈中最密集的能力之一。代理程式不需任何上下文教導，就能寫出正確的篩選、彙總、聯結查詢。量身打造的 DSL 或各供應商專屬的 SDK 則必須在提示詞中反覆說明，付出 token 和錯誤的代價。
- **以小而封閉的文法涵蓋龐大的介面**。雲端服務供應商提供數以萬計的作業。若將它們公開為個別的工具或函式，會壓垮工具選擇。若以 SQL 搭配可探索的 schema 來表達，文法始終不變，而 `SHOW PROVIDERS`、`SHOW METHODS` 和 `DESCRIBE` 讓代理程式在執行階段學習整個介面，只在需要時為所需的部分付出成本。
- **可驗證性**。SQL 的宣告式形式可在執行前接受稽核。人類（或管控政策，例如 StackQL 的 MCP safe 模式）可以閱讀陳述式並確切知道它會影響哪些資源；若是一連串命令式 SDK 呼叫的指令碼，就困難得多。

## SQL 在代理程式之外帶來的好處 {#what-sql-buys-beyond-agents}

同樣的選擇也讓基礎架構與分析堆疊接軌：在伺服器模式下，StackQL 使用 PostgreSQL 線路協定，因此 psql、DBeaver、Tableau、Power BI、Superset、pandas 和 Jupyter 都能像存取資料庫一樣存取即時的雲端狀態。資源清單儀表板、安全性報告和 FinOps 分析可以重複使用既有的技能和工具，中間不需要任何匯出程式或 ETL 管線。

## 相關概念 {#related-concepts}

- [什麼是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：對應方式本身
- [StackQL vs 自訂 MCP 伺服器](/ai/comparisons/stackql-vs-custom-mcp-servers)：token 經濟學論點的應用
- [什麼是代理式基礎架構？](/ai/canonical-definitions/what-is-agentic-infrastructure)：有利於 SQL 的需求
- [StackQL 架構概觀](/ai/architecture/stackql-architecture-overview)：SQL 引擎如何對 API 執行
- [什麼是可查詢基礎架構？](/ai/canonical-definitions/what-is-queryable-infrastructure)：營運模式
