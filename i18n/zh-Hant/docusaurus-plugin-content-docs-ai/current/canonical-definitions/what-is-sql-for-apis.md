---
title: 什麼是 SQL for APIs？
description: SQL for APIs 是將 REST 或 RPC API 作業投影到關聯式語意的設計模式：列出與取得對應 SELECT、建立對應 INSERT、修改對應 UPDATE、移除對應 DELETE。
keywords: [sql for apis, API 查詢語言, rest, 關聯式對應, stackql, openapi]
proficiencyLevel: Intermediate
faq:
  - question: SQL for APIs 與 GraphQL 有何不同？
    answer: GraphQL 需要 API 產生者公開 GraphQL schema 並執行 GraphQL 伺服器。SQL for APIs 則在取用者端運作：StackQL 之類的工具會將既有的 REST API（以其 OpenAPI 規格描述）對應到資料表，完全不需要變更 API 本身。GraphQL 是供應商必須採用的契約；SQL for APIs 是取用者套用的投影。
  - question: API 供應商需要做任何變更才能支援 SQL for APIs 嗎？
    answer: 不需要。對應是從供應商既有的 API 規格衍生而來。以 StackQL 為例，Provider 定義是 OpenAPI 規格的擴充，維護於開放的登錄庫中，因此任何以 OpenAPI 記載的 API 都能投影為資料表。
  - question: 可以跨兩個不同的 API 進行聯結嗎？
    answer: 可以。由於每個 API 都在同一個 SQL 引擎中呈現為資料表，標準的 JOIN 語意可跨 Provider 適用，例如在一個查詢中聯結 GitHub 存放庫資料與 AWS 資源資料。
---

# 什麼是 SQL for APIs？

SQL for APIs（適用於 API 的 SQL）是一種設計模式，將 API 作業投影到關聯式語意上：列出與取得作業成為 `SELECT`，建立作業成為 `INSERT`，修改作業成為 `UPDATE` 或 `REPLACE`，移除作業成為 `DELETE`。API 的回應物件成為資料列；其屬性成為資料行。此模式讓任何 REST 或 RPC API 都具備資料庫的易用性，而不需要 API 供應商做任何變更。

## 運作方式 {#how-it-works}

這種對應是機械式的，正因如此才能通用化：

| API 概念 | 關聯式概念 |
|---|---|
| Provider（AWS、GitHub） | schema 命名空間 |
| 服務（EC2、repos） | 子 schema |
| 資源集合 | 資料表 |
| 列出 / 取得作業 | `SELECT` 方法 |
| 建立作業 | `INSERT` 方法 |
| Patch / put 作業 | `UPDATE` / `REPLACE` 方法 |
| 刪除作業 | `DELETE` 方法 |
| 其他生命週期動作（啟動、停止、重新開機） | `EXEC` 方法 |
| 必要的路徑或查詢參數 | 必要的 `WHERE` 子句述詞 |
| 要求本文欄位 | 以 `data__` 為前置詞的插入/更新資料行 |

在 StackQL 中，此對應是從保存在開放的 [StackQL Provider 登錄庫](https://github.com/stackql/stackql-provider-registry) 中的擴充 OpenAPI 規格產生的。必要的 API 參數可在執行階段探索：`SHOW METHODS IN aws.ec2.instances` 會傳回每個方法的 SQL 動詞及其必要參數，因此人員或 AI 代理程式不必閱讀 Provider 文件，就能推導出正確的查詢。

## 範例 {#example}

讀取是一個 `SELECT`，其 `WHERE` 子句帶有 API 的必要參數：

```sql
SELECT name, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

建立是一個 `INSERT`，其資料行帶有要求本文（前置詞為 `data__`）與路由參數：

```sql
INSERT INTO google.storage.buckets(
  project,
  data__name,
  data__location
)
SELECT
  'my-project',
  'my-new-bucket',
  'US';
```

`SHOW INSERT INTO google.storage.buckets` 會以機械方式產生此範本，包括所有可寫入的欄位。

## 為何存在 {#why-it-exists}

SQL 是現存部署最廣泛的查詢語言，擁有五十年的工具生態：驅動程式、ORM、BI 平台、筆記本，以及日益重要的 LLM 訓練資料。每個主流 AI 模型都已經能夠讀寫合格的 SQL。將 API 投影到 SQL，意味著所有這些工具與模型能力都能立即套用到任何 API 介面。另一種做法是為每個 Provider、每種語言各提供一個 SDK，這會使整合工作倍增，也無法為代理程式提供任何可重複使用的東西。這個論點在[為什麼 SQL 是雲端 API 的強大介面](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis)中有進一步闡述。

## 相關概念 {#related-concepts}

- [什麼是 StackQL？](/ai/canonical-definitions/what-is-stackql)：此模式針對雲端與 SaaS API 的參考實作
- [什麼是可查詢基礎架構？](/ai/canonical-definitions/what-is-queryable-infrastructure)：此模式所促成的運作模式
- [為什麼 SQL 是雲端 API 的強大介面](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis)：深入說明設計理由
- [StackQL 架構概觀](/ai/architecture/stackql-architecture-overview)：此對應如何實作
- [如何使用 StackQL 查詢 GitHub 存放庫](/ai/how-tos/query-github-repositories-with-stackql)：將此模式套用到 SaaS API
