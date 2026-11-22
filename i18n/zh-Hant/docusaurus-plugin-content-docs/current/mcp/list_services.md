---
title: list_services
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_services
  - model context protocol
description: 列出 StackQL Provider 下服務的 MCP 工具
image: "/img/stackql-featured-image.png"
---

傳回單一 Provider 公開的服務，也就是 StackQL [資源階層](/getting-started/resource-hierarchy)的第二層。請在 [`list_providers`](/mcp/list_providers) 之後呼叫此工具，以便在深入查看資源之前縮小範圍。

另請參閱：
[[ MCP 概觀 ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_resources` ]](/mcp/list_resources)

* * *

## 輸入 {#inputs}

| 引數 | 必要 | 描述 |
|--|--|--|
|`provider`|是|Provider 名稱（例如 `google`、`aws`、`azure`、`github`）。|

<br/>

:::info

識別碼不得包含空白字元、分號或反斜線；這類輸入會在建立任何陳述式之前遭到拒絕。

:::

* * *

## 輸出 {#output}

| 轉譯器 | 結構 |
|--|--|
| 表格 | 指定 Provider 下每個服務一個資料列 |

* * *

## 管控 {#gating}

在所有伺服器模式中皆允許。唯讀。

* * *

## 範例 {#example}

請在任何已註冊 StackQL MCP 伺服器的 MCP 用戶端中試用以下提示詞：

```
List the services available in Google Cloud
```

以下是在 Claude Desktop 中的範例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_services_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_services_web.mp4" type="video/mp4" />
</video>
