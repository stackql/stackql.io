---
title: list_services
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_services
  - model context protocol
description: 列出某个 StackQL Provider 下的服务的 MCP 工具
image: "/img/stackql-featured-image.png"
---

返回单个 Provider 公开的服务，即 StackQL [资源层次结构](/getting-started/resource-hierarchy)的第二层。在 [`list_providers`](/mcp/list_providers) 之后调用此工具，可在深入查看资源之前缩小范围。

另请参阅：
[[ MCP 概述 ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_resources` ]](/mcp/list_resources)

* * *

## 输入 {#inputs}

| 参数 | 必填 | 说明 |
|--|--|--|
|`provider`|是|Provider 名称（例如 `google`、`aws`、`azure`、`github`）。|

<br/>

:::info

标识符不能包含空白字符、分号或反斜杠；此类输入会在构建任何语句之前被拒绝。

:::

* * *

## 输出 {#output}

| 渲染器 | 结构 |
|--|--|
| 表格 | 给定 Provider 下的每个服务一行 |

* * *

## 门控 {#gating}

在所有服务器模式下均允许。只读。

* * *

## 示例 {#example}

在任何支持征询（elicitation）并已注册 StackQL MCP 服务器的 MCP 客户端中试用以下提示词：

```
List the services available in Google Cloud
```

以下是 Claude Desktop 中的示例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_services_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_services_web.mp4" type="video/mp4" />
</video>
