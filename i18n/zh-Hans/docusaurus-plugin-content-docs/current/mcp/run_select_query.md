---
title: run_select_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_select_query
  - model context protocol
description: 对 Provider 执行 StackQL SELECT 的 MCP 工具
image: "/img/stackql-featured-image.png"
---

对已配置的 Provider 执行 `SELECT` 语句并返回结果行。只读：不接受变更和生命周期操作；这些请使用 [`run_mutation_query`](/mcp/run_mutation_query) 或 [`run_lifecycle_operation`](/mcp/run_lifecycle_operation)。

结果行会返回给客户端，但被有意排除在审计日志之外。

另请参阅：
[[ MCP 概述 ]](/command-line-usage/mcp) [[ `validate_select_query` ]](/mcp/validate_select_query) [[ `list_methods` ]](/mcp/list_methods) [[ SELECT 语言规范 ]](/language-spec/select)

* * *

## 输入 {#inputs}

| 参数 | 必填 | 说明 |
|--|--|--|
|`sql`|是|一条 `SELECT` 语句。元数据语句（`SHOW`、`DESCRIBE`、`DESC`、`EXPLAIN`）被策略门控归类为只读，同样被接受；`DESC` 在 `v0.12.732` 及之后的 StackQL 发布版本中可识别。|
|`row_limit`|否|返回给客户端的行数上限。`0`（默认）表示客户端侧不设上限。|
|`source`|否|SQL 来自 [`query_library_get`](/mcp/query_library_get) 时的 Query Library 条目 ID。仅用于标注来源；不改变执行或审批。|
|`format`|否|文本渲染方式：`markdown` 或 `json`。省略时，使用服务器的渲染设置。|

* * *

## 输出 {#output}

| 渲染器 | 结构 |
|--|--|
| 表格 | `{rows: [...]}`：每行一个对象，以列名为键 |

* * *

## 门控 {#gating}

当负载中的每条语句都是只读时，在所有服务器模式（`read_only`、`safe`、`delete_safe`、`full_access`）下均允许。自 `v1.0` 起，本工具在所有模式（包括 `full_access`）下都会拒绝 `INSERT`、`UPDATE`、`REPLACE`、`DELETE` 和 `EXEC`，并在错误中指明 [`run_mutation_query`](/mcp/run_mutation_query) 或 [`run_lifecycle_operation`](/mcp/run_lifecycle_operation)。包含多条语句的负载按其中权限最高的语句归类，因此位于开头的 `SELECT` 之后的变更同样会被拒绝。该工具声明了 `destructiveHint: false` 和 `openWorldHint: true`。

* * *

## 示例 {#example}

在任何支持征询（elicitation）并已注册 StackQL MCP 服务器的 MCP 客户端中试用以下提示词：

```
List the VPC networks in my Google Cloud project my-project  
and tell me which ones use auto subnet creation.
```

以下是 Claude Desktop 中的示例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_select_query_web.mp4" type="video/mp4" />
</video>
