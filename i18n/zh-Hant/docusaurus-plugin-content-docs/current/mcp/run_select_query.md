---
title: run_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_select_query
  - model context protocol
description: 對 Provider 執行 StackQL SELECT 的 MCP 工具
image: "/img/stackql-featured-image.png"
---

對已設定的 Provider 執行 `SELECT` 陳述式，並傳回結果資料列。唯讀：不接受變更與生命週期作業；這些請使用 [`run_mutation_query`](/mcp/run_mutation_query) 或 [`run_lifecycle_operation`](/mcp/run_lifecycle_operation)。

結果資料列會傳回給用戶端，但會刻意排除在稽核記錄之外。

另請參閱：
[[ MCP 概觀 ]](/command-line-usage/mcp) [[ `validate_select_query` ]](/mcp/validate_select_query) [[ `list_methods` ]](/mcp/list_methods) [[ SELECT 語言規格 ]](/language-spec/select)

* * *

## 輸入 {#inputs}

| 引數 | 必要 | 描述 |
|--|--|--|
|`sql`|是|`SELECT` 陳述式。中繼資料陳述式（`SHOW`、`DESCRIBE`、`DESC`、`EXPLAIN`）會被政策閘道歸類為唯讀，因此也會被接受；從 `v0.12.732` 起的 StackQL 發行版本可辨識 `DESC`。|
|`row_limit`|否|傳回給用戶端的資料列數上限。`0`（預設）表示沒有用戶端側上限。|
|`source`|否|SQL 來自 [`query_library_get`](/mcp/query_library_get) 時的 Query Library 項目 ID。僅用於標示出處；不會改變執行方式或核准。|
|`format`|否|文字轉譯格式：`markdown` 或 `json`。省略時，會使用伺服器的轉譯設定。|

* * *

## 輸出 {#output}

| 轉譯器 | 結構 |
|--|--|
| 表格 | `{rows: [...]}`：每個資料列一個物件，以資料行名稱為鍵 |

* * *

## 管控 {#gating}

當承載中的每個陳述式都是唯讀時，在所有伺服器模式（`read_only`、`safe`、`delete_safe`、`full_access`）中皆允許。從 `v1.0` 起，此工具在所有模式（包括 `full_access`）中都會拒絕 `INSERT`、`UPDATE`、`REPLACE`、`DELETE` 與 `EXEC`，並傳回指出 [`run_mutation_query`](/mcp/run_mutation_query) 或 [`run_lifecycle_operation`](/mcp/run_lifecycle_operation) 的錯誤。包含多個陳述式的承載會依其中權限最高的陳述式歸類，因此位於開頭 `SELECT` 之後的變更也會以相同方式遭到拒絕。此工具宣告 `destructiveHint: false` 與 `openWorldHint: true`。

* * *

## 範例 {#example}

請在任何支援徵詢（elicitation）且已註冊 StackQL MCP 伺服器的 MCP 用戶端中試用以下提示詞：

```
List the VPC networks in my Google Cloud project my-project  
and tell me which ones use auto subnet creation.
```

以下是在 Claude Desktop 中的範例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.mp4" type="video/mp4" />
</video>
