---
title: StackQL vs CloudQuery
description: CloudQuery 是依排程將雲端資產資料同步到資料庫的 ELT 架構，StackQL 則在執行時即時查詢 Provider API 並可變更資源，核心取捨是歷史快照與目前狀態。
keywords: [stackql vs cloudquery, 雲端資產清單, elt, cspm, 雲端查詢, 即時狀態]
proficiencyLevel: Intermediate
faq:
  - question: CloudQuery 是查詢引擎嗎？
    answer: 本身不是。CloudQuery 是同步（ELT）引擎：它透過來源外掛程式擷取雲端資產資料，並載入 PostgreSQL、BigQuery 或 S3 等目的地。查詢在之後於目的地資料庫中以該資料庫的 SQL 進行。StackQL 本身就是查詢引擎，直接對 Provider API 執行 SQL。
  - question: 哪一個較適合歷史分析？
    answer: CloudQuery。由於它將快照載入您控制的資料庫，您可以保留每次同步的結果並分析隨時間的變化。StackQL 傳回目前狀態；若使用 StackQL 時需要歷史資料，您需依自己的排程將查詢結果具體化到資料庫或檔案。
  - question: 哪一個提供較即時的結果？
    answer: StackQL。其結果在查詢時由即時 API 呼叫計算而得，因此不可能比 API 本身更舊。CloudQuery 的結果只與上次完成的同步一樣新。
---

# StackQL vs CloudQuery

CloudQuery 是用於雲端資產的開源 ELT 架構：來源外掛程式從雲端供應商擷取資源資料，並依排程載入目的地資料庫，您再以該資料庫的 SQL 查詢這些快照。StackQL 反轉了這個架構：SQL 在查詢時直接對 Provider API 執行，沒有同步步驟，也沒有儲存的副本；而且同一個引擎還能變更資源，而不只是讀取資源。

## 如何選擇 {#when-to-use-which}

在下列情況使用 **CloudQuery**：

- 您需要**歷史資料**：跨多次同步保留的時間點快照，用於趨勢分析、鑑識或合規性證據。
- 您希望資產資料與資料倉儲中的其他資料放在一起，以便在既有的 BI/分析堆疊（BigQuery、Snowflake、Postgres）中進行聯結。
- 查詢延遲比資料新鮮度更重要：查詢本機資料庫比扇出 API 呼叫更快。

在下列情況使用 **StackQL**：

- 您需要**目前狀態**：安全性檢查、漂移偵測、營運查詢，以及自動化流程或 AI 代理程式會立即據以行動的任何決策。
- 您不想為了回答問題而維運同步管線和目的地資料庫。
- 您需要在與查詢相同的工具和語言中執行寫入作業（建立、更新或刪除資源）。
- 取用者是代理程式：StackQL 的 MCP 伺服器直接提供探索、驗證和受管控的執行；CloudQuery 的目的地資料庫則對 Provider API 介面一無所知。

在大規模環境中，兩者相輔相成：StackQL 用於即時查詢與行動，CloudQuery（或依排程具體化的 StackQL 結果）用於長期歷史儲存。

## 架構比較 {#architectural-comparison}

| 面向 | CloudQuery | StackQL |
|---|---|---|
| 模型 | 擷取並載入，查詢副本 | 即時查詢來源 |
| 資料新鮮度 | 截至上次同步 | 截至查詢執行時 |
| 歷史資料 | 原生支援（保留快照） | 不保留（需手動具體化） |
| 所需基礎架構 | 同步排程器 + 目的地資料庫 | 單一二進位檔 |
| 寫入雲端 | 否 | INSERT / UPDATE / DELETE / EXEC |
| Provider 定義 | Go 來源外掛程式 | 宣告式擴充 OpenAPI 規格 |
| 查詢方言 | 目的地資料庫的 SQL | StackQL SQL（伺服器模式下相容 Postgres 線路協定） |
| 代理程式介面 | 無（查詢目的地資料庫） | 內建 MCP 伺服器，具備安全模式 |

## 範例 {#example}

以一個查詢說明資料新鮮度的差異：這個 StackQL 陳述式會反映數秒前才啟動的執行個體，而基於同步的副本要等到下次同步完成後才會包含它們：

```sql
SELECT instance_id, instance_type, launch_time
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

若需要長期歷史資料，對應的 StackQL 模式是排程執行這個查詢（透過 cron、CI 或 pystackql），並將結果附加到您自己的儲存區。結果與 ELT 相同，但即時查詢途徑仍可用於具時效性的問題。

## 相關概念 {#related-concepts}

- [什麼是可查詢基礎架構？](/ai/canonical-definitions/what-is-queryable-infrastructure)：以即時查詢作為營運模式
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe)：另一個即時查詢的同類工具
- [什麼是基礎架構漂移？](/ai/canonical-definitions/what-is-infrastructure-drift)：為什麼資料新鮮度很重要
- [如何使用 StackQL 查詢 AWS EC2 執行個體](/ai/how-tos/query-aws-ec2-instances-with-stackql)：此範例的延伸說明
- [StackQL 架構概觀](/ai/architecture/stackql-architecture-overview)：即時執行的運作方式
