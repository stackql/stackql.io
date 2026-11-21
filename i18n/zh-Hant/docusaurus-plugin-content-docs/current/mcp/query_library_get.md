---
title: query_library_get
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_get
  - query library
  - model context protocol
description: 擷取 StackQL 查詢範本、驗證所提供的參數，並傳回轉譯後 SQL 但不執行的 MCP 工具
image: "/img/stackql-featured-image.png"
---

擷取 [Query Library](pathname:///docs/query-library/) 項目，以 [`query_library_search`](/mcp/query_library_search) 傳回的 `id` 指定要擷取的項目。

省略 `params` 或其為空時，會傳回原始範本、參數宣告與附註。提供非空的 `params` 物件時，伺服器會驗證這些值並將其代入範本。只有在轉譯成功時才會傳回 SQL。模型不需要自行代入值。

此工具不會執行 SQL，也不會檢查雲端憑證。其參數驗證與 [`validate_select_query`](/mcp/validate_select_query) 不同，後者會針對已安裝的 Provider 剖析並規劃 `SELECT`。

另請參閱：
[[ MCP 概觀 ]](/command-line-usage/mcp) [[ `query_library_search` ]](/mcp/query_library_search) [[ `run_select_query` ]](/mcp/run_select_query) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation)

* * *

## 輸入 {#inputs}

| 引數 | 必要 | 描述 |
|--|--|--|
|`id`|是|搜尋結果中的查詢 ID，例如 `aws/s3/buckets-list`。|
|`params`|否|將已宣告參數名稱對應到值的物件。省略此引數或傳入 `{}`，即可檢查範本而不進行轉譯。|
|`format`|否|文字轉譯格式：`markdown` 或 `json`。省略時，會使用伺服器的轉譯設定（預設為 `markdown`）。|

未知的參數會遭到拒絕。未提供值且沒有預設值的必要參數，會在 `missing_params` 中回報。所提供的值會依據宣告的型別與限制條件進行檢查；未解析的預留位置會視為錯誤。

* * *

## 輸出 {#output}

預設的文字檢視是一個 KV 區塊，包含識別資訊、變更狀態、下一個工具、有效性、來源連結，以及任何範本、轉譯後的 SQL 或錯誤。如需完整的中繼資料，請使用結構化承載或 `format: "json"`：

| 欄位 | 描述 |
|--|--|
|`id`、`title`、`description`|項目的識別資訊與摘要。|
|`mutation`、`verb`|項目是否會變更資源，以及有宣告時的作業類型（`select`、`mutation` 或 `lifecycle`）。|
|`next_tool`|項目的執行工具：`run_select_query`、`run_mutation_query` 或 `run_lifecycle_operation`。擷取時不會呼叫此工具。|
|`params`|參數宣告，包含名稱、型別、必要旗標，以及任何預設值、描述、範例、列舉值或模式。|
|`template`、`notes`|原始範本與作者附註，在省略 `params` 或其為空時傳回。|
|`rendered`、`valid`|是否已嘗試轉譯以及是否成功。檢查原始範本時，兩者皆為 `false`。|
|`sql`|轉譯後的 SQL，只在轉譯成功後才會出現。|
|`missing_params`、`unknown_params`、`errors`|轉譯失敗時的驗證詳細資料。在這些問題解決之前，請勿執行 SQL。|
|`auth`、`permissions`|有宣告時，為執行所需的憑證環境變數與 Provider 原生權限。擷取本身兩者皆不需要。|
|`cost`、`outputs`、`related`|有宣告時，為扇出與費用的提示資訊、結果資料行描述，以及相關項目的 ID。|
|`doc_url`|供人閱讀的 Query Library 頁面。|
|`source_tier`、`source_url`、`stale`|內容出處：`primary`、`fallback` 或 `snapshot`，其 URL 或內嵌路徑，以及在擷取失敗後內容是否是從快取或快照提供。|

未知或格式錯誤的 ID 會傳回錯誤。請使用 `query_library_search` 找出有效的 ID。

* * *

## 管控 {#gating}

在所有伺服器模式中皆允許，但受所設定的工具允許清單限制。擷取或轉譯變更或生命週期範本不會對雲端造成影響，也不需要核准。執行傳回的 SQL 是另一個呼叫，由伺服器[模式](/command-line-usage/mcp#server-modes)管控。

* * *

## 範例 {#example}

首先檢查一個項目：

```json
{
  "id": "aws/s3/buckets-list"
}
```

接著提供其必要的路由區域：

```json
{
  "id": "aws/s3/buckets-list",
  "params": {
    "region": "us-east-1"
  }
}
```

確認 `rendered` 與 `valid` 之後，請使用 [`validate_select_query`](/mcp/validate_select_query) 驗證傳回的 `SELECT`。使用 `next_tool` 中指定的工具執行它，傳入傳回的 `sql`，並將 `source` 設為該 Query Library 項目的 `id` 以標示出處。執行時需要 Provider 憑證與權限。
