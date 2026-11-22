---
title: validate_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - validate_select_query
  - model context protocol
description: 剖析並規劃 StackQL SELECT 但不加以執行的 MCP 工具
image: "/img/stackql-featured-image.png"
---

剖析並規劃單一 `SELECT` 陳述式，但不加以執行。傳回 `{valid, errors}`。當代理程式想在實際發出 API 呼叫之前，確認其查詢能針對已探索到的方法完成編譯與繫結時，此工具很有用。

:::info

僅限 `SELECT`，且只能有一個陳述式：包含多個陳述式的承載會遭到拒絕，並傳回 `validate_select_query accepts exactly one statement`。

:::

另請參閱：
[[ MCP 概觀 ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## 輸入 {#inputs}

| 引數 | 必要 | 描述 |
|--|--|--|
|`sql`|是|單一 `SELECT` 陳述式。|

* * *

## 輸出 {#output}

| 轉譯器 | 結構 |
|--|--|
| KV | `valid`（bool）與 `errors`（字串陣列，有效時為空） |

* * *

## 管控 {#gating}

在所有伺服器模式中皆允許。不會發出任何 API 呼叫。

* * *

## 範例 {#example}

請在任何已註冊 StackQL MCP 伺服器的 MCP 用戶端中試用以下提示詞：

```
Can you validate this query before I run it:

select name, network  
from google.compute.subnetworks  
where project = 'my-project' and region = 'us-central1';
```

以下是在 Claude Desktop 中的範例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.mp4" type="video/mp4" />
</video>
