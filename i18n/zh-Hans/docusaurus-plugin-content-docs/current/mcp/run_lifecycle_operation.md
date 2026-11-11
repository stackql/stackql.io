---
title: run_lifecycle_operation
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - run_lifecycle_operation
  - model context protocol
description: 执行 StackQL EXEC 生命周期操作的 MCP 工具
image: "/img/stackql-featured-image.png"
---

执行 StackQL `EXEC` 生命周期操作，即非 CRUD 的 Provider 动作（启动/停止 VM、轮换密钥、触发工作流等）。**会产生实际的副作用。**

由服务器[模式](/command-line-usage/mcp#server-modes)门控：

| 模式 | 生命周期 `EXEC` |
|--|--|
|`read_only`|拒绝|
|`safe`（默认）|需要审批（征询（elicitation））|
|`delete_safe`|需要审批|
|`full_access`|允许|

<br/>

:::info

包含多条语句的负载由其中权限最高的语句门控

:::

当需要审批而客户端未声明支持征询时，调用会被拒绝。

另请参阅：
[[ MCP 概述 ]](/command-line-usage/mcp) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `describe_method` ]](/mcp/describe_method) [[ EXEC 语言规范 ]](/language-spec/exec)

* * *

## 输入 {#inputs}

| 参数 | 必填 | 说明 |
|--|--|--|
|`sql`|是|一条针对生命周期方法的 StackQL `EXEC` 语句。|
|`source`|否|SQL 来自 [`query_library_get`](/mcp/query_library_get) 时的 Query Library 条目 ID。仅用于标注来源；不改变执行或审批。|
|`format`|否|文本渲染方式：`markdown` 或 `json`。省略时，使用服务器的渲染设置。|

* * *

## 输出 {#output}

| 渲染器 | 结构 |
|--|--|
| KV | `{messages: [...], timestamp}` |

* * *

## 示例 {#example}

在任何支持征询并已注册 StackQL MCP 服务器的 MCP 客户端中试用以下提示词：

```
The Compute instance web-01 in us-central1-a  
(project my-project) is stuck.  Please stop it.
```

除非服务器以 `full_access` 模式运行，否则 MCP 客户端会在操作发出之前显示审批请求。

以下是 Claude Desktop 中的示例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_run_lifecycle_operation_web.mp4" type="video/mp4" />
</video>
