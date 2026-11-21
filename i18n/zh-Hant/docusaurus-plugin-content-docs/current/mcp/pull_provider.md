---
title: pull_provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - pull_provider
  - model context protocol
description: 從登錄庫將 StackQL Provider 安裝到本機快取的 MCP 工具
image: "/img/stackql-featured-image.png"
---

從 StackQL 登錄庫將單一 Provider 安裝到本機 approot 快取。省略 `version` 時，會提取最新發行的版本。

只會寫入本機快取狀態，對雲端控制平面或資料平面沒有任何影響，因此此工具在所有伺服器模式（包括 `read_only`）中皆允許。提取之前，請使用 [`list_registry`](/mcp/list_registry) 找出 Provider 名稱與可用的版本。

另請參閱：
[[ MCP 概觀 ]](/command-line-usage/mcp) [[ `list_registry` ]](/mcp/list_registry) [[ `list_providers` ]](/mcp/list_providers) [[ Registry 命令 ]](/command-line-usage/registry)

* * *

## 輸入 {#inputs}

| 引數 | 必要 | 描述 |
|--|--|--|
|`provider`|是|登錄庫中發行的 Provider 名稱（例如 `aws`、`google`、`cloudflare`）。|
|`version`|否|要提取的已發行版本。省略時，會安裝最新發行的版本。|

* * *

## 輸出 {#output}

| 轉譯器 | 結構 |
|--|--|
| KV | 確認 Provider（及版本）已安裝到本機快取 |

* * *

## 管控 {#gating}

在所有伺服器模式中皆允許。沒有雲端側的影響；只有本機快取狀態會變更。

* * *

## 範例 {#example}

請在任何支援徵詢（elicitation）且已註冊 StackQL MCP 伺服器的 MCP 用戶端中試用以下提示詞：

```
I want to start querying Google resources with StackQL.  
Can you install the Google provider for me?
```

以下是在 Claude Desktop 中的範例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.mp4" type="video/mp4" />
</video>
