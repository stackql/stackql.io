---
title: server_info
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - server_info
  - model context protocol
description: 返回 StackQL MCP 服务器标识和运行时的 MCP 工具
image: "/img/stackql-featured-image.png"
---

返回 StackQL MCP 服务器的标识和运行时：版本、后端 SQL 引擎、Provider 注册表位置、服务器模式以及旧版只读标志。在会话开始时调用一次，以便智能体了解自己连接的是什么。

另请参阅：
[[ MCP 概述 ]](/command-line-usage/mcp) [[ `list_providers` ]](/mcp/list_providers) [[ `list_registry` ]](/mcp/list_registry)

* * *

## 输入 {#inputs}

无。

* * *

## 输出 {#output}

| 渲染器 | 结构 |
|--|--|
| KV（面向 LLM 的 markdown 键/值块） | 包含以下字段的结构化负载 |

<br/><br/>

| 字段 | 说明 |
|--|--|
|`version`|StackQL 构建版本。|
|`commit`|Git 提交的短哈希。|
|`build_date`|RFC3339 构建时间戳。|
|`platform`|`os/arch`（例如 `linux/amd64`）。|
|`transport`|`http` 或 `stdio`。|
|`sql_backend`|后端 SQL 引擎（例如 `sqlite3`）。|
|`provider_registry`|Provider 注册表 URL。|
|`mode`|当前生效的服务器模式：`read_only`、`safe`、`delete_safe` 或 `full_access`。|
|`is_read_only`|旧版布尔标志（当且仅当 `mode == read_only` 时为 `true`）。|

* * *

## 门控 {#gating}

在所有服务器模式下始终允许。不作为变更进行审计。

* * *

## 示例 {#example}

在任何支持征询（elicitation）并已注册 StackQL MCP 服务器的 MCP 客户端中试用以下提示词：

```
What StackQL server am I connected to?  
Show me the version, mode, and where it pulls providers from.
```

以下是 Claude Desktop 中的示例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_server_info_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_server_info_web.mp4" type="video/mp4" />
</video>
