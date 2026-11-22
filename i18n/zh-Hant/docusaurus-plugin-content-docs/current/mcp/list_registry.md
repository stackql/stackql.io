---
title: list_registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_registry
  - model context protocol
description: 列出 StackQL Provider 登錄庫中可用 Provider 與版本的 MCP 工具
image: "/img/stackql-featured-image.png"
---

傳回所設定的 StackQL Provider 登錄庫中可用的 Provider（及其版本）。這與 [`list_providers`](/mcp/list_providers) 不同，後者只會回報已提取到本機快取的 Provider。

傳入選用的 `provider` 引數，即可列出單一 Provider 已發行的版本。當代理程式需要安裝本機尚未具備的內容時，請搭配 [`pull_provider`](/mcp/pull_provider) 使用。

另請參閱：
[[ MCP 概觀 ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `pull_provider` ]](/mcp/pull_provider) [[ Registry 命令 ]](/command-line-usage/registry)

* * *

## 輸入 {#inputs}

| 引數 | 必要 | 描述 |
|--|--|--|
|`provider`|否|Provider 名稱。提供時，結果會縮小為該 Provider 已發行的版本；省略時，會傳回所有登錄庫項目。|

* * *

## 輸出 {#output}

| 轉譯器 | 結構 |
|--|--|
| 表格 | 每個登錄庫項目一個資料列（Provider 加上版本中繼資料） |

* * *

## 管控 {#gating}

在所有伺服器模式中皆允許。對雲端控制平面或資料平面沒有任何影響，只會讀取登錄庫。

* * *

## 範例 {#example}

請在任何已註冊 StackQL MCP 伺服器的 MCP 用戶端中試用以下提示詞：

```
What providers does the StackQL registry offer,  
and which versions of the aws provider are published?
```

以下是在 Claude Desktop 中的範例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.mp4" type="video/mp4" />
</video>
