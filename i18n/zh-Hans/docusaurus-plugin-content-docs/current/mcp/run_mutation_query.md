---
title: run_mutation_query
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_mutation_query
  - model context protocol
description: 通过 StackQL 对 Provider 执行 INSERT、UPDATE、REPLACE 或 DELETE 的 MCP 工具
image: "/img/stackql-featured-image.png"
---

对 Provider 执行 `INSERT`、`UPDATE`、`REPLACE` 或 `DELETE`。**会产生实际的副作用**：调用成功会改变云端状态。

由服务器[模式](/command-line-usage/mcp#server-modes)门控：

| 模式 | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` |
|--|--|--|
|`read_only`|拒绝|拒绝|
|`safe`（默认）|需要审批（征询（elicitation））|需要审批|
|`delete_safe`|允许|需要审批|
|`full_access`|允许|允许|

<br/>

:::info

包含多条语句的负载由其中权限最高的语句门控

:::

当变更需要审批而客户端未声明 MCP 征询能力时，调用会被拒绝。

另请参阅：
[[ MCP 概述 ]](/command-line-usage/mcp) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation) [[ `describe_method` ]](/mcp/describe_method) [[ INSERT 语言规范 ]](/language-spec/insert) [[ DELETE 语言规范 ]](/language-spec/delete)

* * *

## 输入 {#inputs}

| 参数 | 必填 | 说明 |
|--|--|--|
|`sql`|是|一条 `INSERT`、`UPDATE`、`REPLACE` 或 `DELETE` 语句。|
|`source`|否|SQL 来自 [`query_library_get`](/mcp/query_library_get) 时的 Query Library 条目 ID。仅用于标注来源；不改变执行或审批。|
|`format`|否|文本渲染方式：`markdown` 或 `json`。省略时，使用服务器的渲染设置。|

* * *

## 输出 {#output}

| 渲染器 | 结构 |
|--|--|
| KV | `{messages: [...], timestamp}`：请求已发出的确认，而非 API 响应体 |

* * *

## 示例 {#example}

在任何支持征询并已注册 StackQL MCP 服务器的 MCP 客户端中试用以下提示词：

```
The firewall rule allow-test-old in project my-project  
is no longer needed.  Please remove it.
```

除非服务器以 `full_access` 模式运行，否则 MCP 客户端会在删除操作发出之前显示审批请求。

以下片段展示了 StackQL MCP 服务器以默认的 `safe` 模式运行时的征询流程：在向云端发出任何更改之前，客户端会被要求批准该 `DELETE`。你可以使用官方的 [MCP Inspector](https://github.com/modelcontextprotocol/inspector)，通过 `npx @modelcontextprotocol/inspector` 在自己的 StackQL MCP 服务器上复现这一流程：


<video autoPlay loop muted playsInline style={{width: 'calc(100% - 4rem)', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.webm" type="video/webm" />
  <source src="/img/videos/mcp_inspector_run_mutation_query_web.mp4" type="video/mp4" />
</video>
