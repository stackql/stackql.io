---
title: query_library_search
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_search
  - query library
  - model context protocol
description: 依自然語言意圖、Provider、服務與標籤搜尋精選 StackQL 查詢範本的 MCP 工具
image: "/img/stackql-featured-image.png"
---

在 [StackQL Query Library](pathname:///docs/query-library/) 中搜尋符合自然語言意圖的範本。從頭撰寫 SQL 之前請先查閱它，然後將結果的 `id` 傳給 [`query_library_get`](/mcp/query_library_get)，以檢查範本並驗證其參數。

此工具只會擷取 Query Library 內容。它不會執行 SQL、不會呼叫雲端 API，也不需要 Provider 憑證。

另請參閱：
[[ MCP 概觀 ]](/command-line-usage/mcp) [[ `query_library_get` ]](/mcp/query_library_get) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## 輸入 {#inputs}

| 引數 | 必要 | 描述 |
|--|--|--|
|`intent`|是|任務的自然語言描述。|
|`provider`|否|依 Provider 篩選，例如 `aws`。|
|`service`|否|依服務篩選，例如 `s3`。|
|`tags`|否|標籤陣列。項目必須符合所有提供的標籤。|
|`include_mutations`|否|包含變更範本。預設為 `false`；在 `read_only` 模式中會忽略此引數，該模式一律排除變更項目。|
|`limit`|否|命中結果的數量上限。省略或為非正數時預設為 `5`；上限為 `20`。|
|`format`|否|文字轉譯格式：`markdown` 或 `json`。省略時，會使用伺服器的轉譯設定（預設為 `markdown`）。|

* * *

## 輸出 {#output}

預設的文字檢視是一個依排名排序的表格，包含 `id`、`title`、`mutation`、`required_params` 與 `score`，後面接著來源引用與任何未命中時的指引。結構化承載包含：

| 欄位 | 描述 |
|--|--|
|`hits`|依排名排序的項目，包含 `id`、`title`、選用的 `description`、`providers`、`services` 與 `required_params`，以及 `mutation` 與 `score`。如需完整的參數宣告，請接著使用 `query_library_get`。|
|`miss`|沒有命中結果或最高分數低於比對門檻時為 `true`。仍可能傳回低分的最接近結果。|
|`guidance`|未命中時，將代理程式導向伺服器指示、探索工具與 `validate_select_query`，以撰寫查詢。|
|`source_tier`|內容來源：`primary`、`fallback` 或 `snapshot`。|
|`source_url`|所用目錄的 URL 或內嵌路徑，供引用之用。|
|`stale`|為 `true` 時，表示在擷取失敗後，內容是從快取或內嵌快照提供。|

已淘汰的項目會被排除。伺服器會快取目錄，並可從託管的 Query Library 退回到其 GitHub 來源與內嵌快照。

* * *

## 管控 {#gating}

在所有伺服器模式中皆允許，但受所設定的工具允許清單限制。不需要核准，也不需要 Provider 憑證。搜尋變更範本並不代表授權執行這些範本。

* * *

## 範例 {#example}

工具引數：

```json
{
  "intent": "List S3 bucket names and regions",
  "provider": "aws",
  "service": "s3",
  "limit": 5
}
```

將 `hits` 中的 `id`（例如 `aws/s3/buckets-list`）用於 [`query_library_get`](/mcp/query_library_get)。若 `miss` 為 `true`，請檢查最接近的範本以取得指引，或在撰寫 SQL 之前使用 [`list_methods`](/mcp/list_methods) 與 [`describe_method`](/mcp/describe_method)。
