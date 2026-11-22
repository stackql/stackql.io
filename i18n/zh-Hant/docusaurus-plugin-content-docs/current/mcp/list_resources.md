---
title: list_resources
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_resources
  - model context protocol
description: 列出 StackQL provider.service 下資源的 MCP 工具
image: "/img/stackql-featured-image.png"
---

傳回 `provider.service` 下的資源，也就是 StackQL [資源階層](/getting-started/resource-hierarchy)的第三層。資源是出現在 StackQL 查詢 `FROM` 子句中的目標。

另請參閱：
[[ MCP 概觀 ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_methods` ]](/mcp/list_methods) [[ `describe_method` ]](/mcp/describe_method)

* * *

## 輸入 {#inputs}

| 引數 | 必要 | 描述 |
|--|--|--|
|`provider`|是|Provider 名稱。|
|`service`|是|該 Provider 下的服務。|

<br/>

:::info

識別碼不得包含空白字元、分號或反斜線；這類輸入會在建立任何陳述式之前遭到拒絕。

:::

* * *

## 輸出 {#output}

| 轉譯器 | 結構 |
|--|--|
| 表格 | `provider.service` 下每個資源一個資料列 |

* * *

## 管控 {#gating}

在所有伺服器模式中皆允許。唯讀。

* * *

## 範例 {#example}

請在任何已註冊 StackQL MCP 伺服器的 MCP 用戶端中試用以下提示詞：

```
List the resources in the google.storage service
```

以下是在 Claude Desktop 中的範例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.mp4" type="video/mp4" />
</video>
