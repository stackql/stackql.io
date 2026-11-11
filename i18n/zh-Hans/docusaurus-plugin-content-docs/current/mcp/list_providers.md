---
title: list_providers
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_providers
  - model context protocol
description: 列出 StackQL MCP 服务器可用的云和 SaaS Provider 的 MCP 工具
image: "/img/stackql-featured-image.png"
---

返回已拉取到本地 StackQL 缓存中的 Provider，即 StackQL [资源层次结构](/getting-started/resource-hierarchy)的顶层。当智能体需要发现自己可以查询哪些内容时，将此工具用作入口点。

如需了解*注册表*中可用的 Provider（包括尚未拉取的 Provider），请参阅 [`list_registry`](/mcp/list_registry)。

另请参阅：
[[ MCP 概述 ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_registry` ]](/mcp/list_registry) [[ `pull_provider` ]](/mcp/pull_provider)

* * *

## 输入 {#inputs}

无。

* * *

## 输出 {#output}

| 渲染器 | 结构 |
|--|--|
| 表格（面向 LLM 的 markdown 表格） | 每个 Provider 一行 |

<br />

每一行都带有注册表发布的 Provider 名称和版本元数据。

* * *

## 门控 {#gating}

在所有服务器模式（`read_only`、`safe`、`delete_safe`、`full_access`）下均允许。只读。

* * *

## 示例 {#example}

在任何支持征询（elicitation）并已注册 StackQL MCP 服务器的 MCP 客户端中试用以下提示词：

```
Which cloud providers are available for you to query right now?
```

以下是 Claude Desktop 中的示例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_providers_web.mp4" type="video/mp4" />
</video>
