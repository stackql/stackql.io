---
title: pull_provider
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - pull_provider
  - model context protocol
description: 将 StackQL Provider 从注册表安装到本地缓存的 MCP 工具
image: "/img/stackql-featured-image.png"
---

将单个 Provider 从 StackQL 注册表安装到本地 approot 缓存中。省略 `version` 时，拉取最新的已发布版本。

仅写入本地缓存状态，不会对云的控制平面或数据平面产生影响，因此在所有服务器模式（包括 `read_only`）下均允许使用此工具。拉取之前，使用 [`list_registry`](/mcp/list_registry) 查找 Provider 名称和可用的版本。

另请参阅：
[[ MCP 概述 ]](/command-line-usage/mcp) [[ `list_registry` ]](/mcp/list_registry) [[ `list_providers` ]](/mcp/list_providers) [[ registry 命令 ]](/command-line-usage/registry)

* * *

## 输入 {#inputs}

| 参数 | 必填 | 说明 |
|--|--|--|
|`provider`|是|注册表中发布的 Provider 名称（例如 `aws`、`google`、`cloudflare`）。|
|`version`|否|要拉取的已发布版本。省略时，安装最新的已发布版本。|

* * *

## 输出 {#output}

| 渲染器 | 结构 |
|--|--|
| KV | 确认 Provider（及版本）已安装到本地缓存 |

* * *

## 门控 {#gating}

在所有服务器模式下均允许。不会对云端产生影响；仅本地缓存状态发生变化。

* * *

## 示例 {#example}

在任何支持征询（elicitation）并已注册 StackQL MCP 服务器的 MCP 客户端中试用以下提示词：

```
I want to start querying Google resources with StackQL.  
Can you install the Google provider for me?
```

以下是 Claude Desktop 中的示例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_pull_provider_web.mp4" type="video/mp4" />
</video>
