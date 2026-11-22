---
title: list_providers
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_providers
  - model context protocol
description: 列出 StackQL MCP 伺服器可用之雲端與 SaaS Provider 的 MCP 工具
image: "/img/stackql-featured-image.png"
---

傳回已提取至本機 StackQL 快取的 Provider，也就是 StackQL [資源階層](/getting-started/resource-hierarchy)的最上層。當代理程式需要探索可查詢的內容時，請以此工具作為進入點。

如需*登錄庫*中可用的 Provider（包括尚未提取的 Provider），請參閱 [`list_registry`](/mcp/list_registry)。

另請參閱：
[[ MCP 概觀 ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_registry` ]](/mcp/list_registry) [[ `pull_provider` ]](/mcp/pull_provider)

* * *

## 輸入 {#inputs}

無。

* * *

## 輸出 {#output}

| 轉譯器 | 結構 |
|--|--|
| 表格（提供給 LLM 的 markdown 表格） | 每個 Provider 各一資料列 |

<br />

每一資料列都帶有 Provider 名稱，以及登錄庫所發佈的版本中繼資料。

* * *

## 管控 {#gating}

在所有伺服器模式（`read_only`、`safe`、`delete_safe`、`full_access`）下皆允許。唯讀。

* * *

## 範例 {#example}

請在任何已註冊 StackQL MCP 伺服器的 MCP 用戶端中試用以下提示詞：

```
Which cloud providers are available for you to query right now?
```

以下是在 Claude Desktop 中的範例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.mp4" type="video/mp4" />
</video>
