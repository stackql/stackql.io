---
title: run_lifecycle_operation
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_lifecycle_operation
  - model context protocol
description: 執行 StackQL EXEC 生命週期作業的 MCP 工具
image: "/img/stackql-featured-image.png"
---

執行 StackQL `EXEC` 生命週期作業，也就是非 CRUD 的 Provider 動作（啟動或停止 VM、輪替金鑰、觸發工作流程等）。**會產生實際的副作用。**

由伺服器[模式](/command-line-usage/mcp#server-modes)管控：

| 模式 | 生命週期 `EXEC` |
|--|--|
|`read_only`|拒絕|
|`safe`（預設）|需要核准（徵詢）|
|`delete_safe`|需要核准|
|`full_access`|允許|

<br/>

:::info

包含多個陳述式的承載，會依其中權限最高的陳述式進行管控

:::

需要核准且用戶端未宣告支援徵詢（elicitation）時，呼叫會遭到拒絕。

另請參閱：
[[ MCP 概觀 ]](/command-line-usage/mcp) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `describe_method` ]](/mcp/describe_method) [[ EXEC 語言規格 ]](/language-spec/exec)

* * *

## 輸入 {#inputs}

| 引數 | 必要 | 描述 |
|--|--|--|
|`sql`|是|以生命週期方法為目標的 StackQL `EXEC` 陳述式。|
|`source`|否|SQL 來自 [`query_library_get`](/mcp/query_library_get) 時的 Query Library 項目 ID。僅用於標示出處；不會改變執行方式或核准。|
|`format`|否|文字轉譯格式：`markdown` 或 `json`。省略時，會使用伺服器的轉譯設定。|

* * *

## 輸出 {#output}

| 轉譯器 | 結構 |
|--|--|
| KV | `{messages: [...], timestamp}` |

* * *

## 範例 {#example}

請在任何支援徵詢且已註冊 StackQL MCP 伺服器的 MCP 用戶端中試用以下提示詞：

```
The Compute instance web-01 in us-central1-a  
(project my-project) is stuck.  Please stop it.
```

除非伺服器以 `full_access` 模式執行，否則 MCP 用戶端會在分派作業之前顯示核准要求。

以下是在 Claude Desktop 中的範例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.mp4" type="video/mp4" />
</video>
