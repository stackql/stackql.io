---
title: validate_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - validate_select_query
  - model context protocol
description: 解析并规划 StackQL SELECT 而不执行它的 MCP 工具
image: "/img/stackql-featured-image.png"
---

解析并规划单条 `SELECT` 语句，而不执行它。返回 `{valid, errors}`。适用于智能体希望在发起真实的 API 调用之前，验证其查询能够针对已发现的方法完成编译和绑定的场景。

:::info

仅限 `SELECT`，且只能有一条语句，包含多条语句的负载会被拒绝，并返回 `validate_select_query accepts exactly one statement`。

:::

另请参阅：
[[ MCP 概述 ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## 输入 {#inputs}

| 参数 | 必填 | 说明 |
|--|--|--|
|`sql`|是|单条 `SELECT` 语句。|

* * *

## 输出 {#output}

| 渲染器 | 结构 |
|--|--|
| KV | `valid`（布尔值）和 `errors`（字符串数组，有效时为空） |

* * *

## 门控 {#gating}

在所有服务器模式下均允许。不会发起任何 API 调用。

* * *

## 示例 {#example}

在任何支持征询（elicitation）并已注册 StackQL MCP 服务器的 MCP 客户端中试用以下提示词：

```
Can you validate this query before I run it:

select name, network  
from google.compute.subnetworks  
where project = 'my-project' and region = 'us-central1';
```

以下是 Claude Desktop 中的示例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_validate_select_query_web.mp4" type="video/mp4" />
</video>
