---
title: run_mutation_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_mutation_query
  - model context protocol
description: 透過 StackQL 對 Provider 執行 INSERT、UPDATE、REPLACE 或 DELETE 的 MCP 工具
image: "/img/stackql-featured-image.png"
---

對 Provider 執行 `INSERT`、`UPDATE`、`REPLACE` 或 `DELETE`。**會產生實際的副作用**：成功的呼叫會變更雲端狀態。

由伺服器[模式](/command-line-usage/mcp#server-modes)管控：

| 模式 | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` |
|--|--|--|
|`read_only`|拒絕|拒絕|
|`safe`（預設）|需要核准（徵詢）|需要核准|
|`delete_safe`|允許|需要核准|
|`full_access`|允許|允許|

<br/>

:::info

包含多個陳述式的承載，會依其中權限最高的陳述式進行管控

:::

變更需要核准且用戶端未宣告 MCP 徵詢（elicitation）功能時，呼叫會遭到拒絕。

另請參閱：
[[ MCP 概觀 ]](/command-line-usage/mcp) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation) [[ `describe_method` ]](/mcp/describe_method) [[ INSERT 語言規格 ]](/language-spec/insert) [[ DELETE 語言規格 ]](/language-spec/delete)

* * *

## 輸入 {#inputs}

| 引數 | 必要 | 描述 |
|--|--|--|
|`sql`|是|`INSERT`、`UPDATE`、`REPLACE` 或 `DELETE` 陳述式。|
|`source`|否|SQL 來自 [`query_library_get`](/mcp/query_library_get) 時的 Query Library 項目 ID。僅用於標示出處；不會改變執行方式或核准。|
|`format`|否|文字轉譯格式：`markdown` 或 `json`。省略時，會使用伺服器的轉譯設定。|

* * *

## 輸出 {#output}

| 轉譯器 | 結構 |
|--|--|
| KV | `{messages: [...], timestamp}`：分派確認，而非 API 回應本文 |

* * *

## 範例 {#example}

請在任何支援徵詢且已註冊 StackQL MCP 伺服器的 MCP 用戶端中試用以下提示詞：

```
The firewall rule allow-test-old in project my-project  
is no longer needed.  Please remove it.
```

除非伺服器以 `full_access` 模式執行，否則 MCP 用戶端會在分派刪除之前顯示核准要求。

下列短片展示 StackQL MCP 伺服器以預設 `safe` 模式執行時的徵詢流程：在分派任何雲端側變更之前，用戶端會收到核准 `DELETE` 的要求。您可以使用官方的 [MCP Inspector](https://github.com/modelcontextprotocol/inspector)，透過 `npx @modelcontextprotocol/inspector` 對您自己的 StackQL MCP 伺服器重現此流程：


<video autoPlay loop muted playsInline style={{width: 'calc(100% - 4rem)', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.webm" type="video/webm" />
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.mp4" type="video/mp4" />
</video>
