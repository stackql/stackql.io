---
title: list_registry
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_registry
  - model context protocol
description: 列出 StackQL Provider 注册表中可用的 Provider 及其版本的 MCP 工具
image: "/img/stackql-featured-image.png"
---

返回已配置的 StackQL Provider 注册表中可用的 Provider（及其版本）。它不同于 [`list_providers`](/mcp/list_providers)，后者只报告已拉取到本地缓存中的 Provider。

传入可选的 `provider` 参数，可列出单个 Provider 的已发布版本。当智能体需要安装尚未在本地的内容时，可与 [`pull_provider`](/mcp/pull_provider) 配合使用。

另请参阅：
[[ MCP 概述 ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `pull_provider` ]](/mcp/pull_provider) [[ registry 命令 ]](/command-line-usage/registry)

* * *

## 输入 {#inputs}

| 参数 | 必填 | 说明 |
|--|--|--|
|`provider`|否|Provider 名称。提供该参数时，结果将限定为该 Provider 的已发布版本；省略时，返回注册表中的所有条目。|

* * *

## 输出 {#output}

| 渲染器 | 结构 |
|--|--|
| 表格 | 每个注册表条目一行（Provider 及版本元数据） |

* * *

## 门控 {#gating}

在所有服务器模式下均允许。不会对云的控制平面或数据平面产生影响，仅读取注册表。

* * *

## 示例 {#example}

在任何支持征询（elicitation）并已注册 StackQL MCP 服务器的 MCP 客户端中试用以下提示词：

```
What providers does the StackQL registry offer,  
and which versions of the aws provider are published?
```

以下是 Claude Desktop 中的示例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_registry_web.mp4" type="video/mp4" />
</video>
