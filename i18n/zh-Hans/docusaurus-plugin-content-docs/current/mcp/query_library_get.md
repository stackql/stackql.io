---
title: query_library_get
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_get
  - query library
  - model context protocol
description: 检索 StackQL 查询模板并验证所提供的参数，返回渲染后的 SQL 但不执行的 MCP 工具
image: "/img/stackql-featured-image.png"
---

根据 [`query_library_search`](/mcp/query_library_search) 返回的 `id` 检索 [Query Library](pathname:///docs/query-library/) 条目。

当 `params` 省略或为空时，它返回原始模板、参数声明和备注。当 `params` 对象非空时，服务器会验证这些值并将其代入模板。仅在渲染成功时才返回 SQL。模型无需自行代入值。

该工具不执行 SQL，也不检查云凭据。其参数验证不同于 [`validate_select_query`](/mcp/validate_select_query)，后者针对已安装的 Provider 解析并规划 `SELECT`。

另请参阅：
[[ MCP 概述 ]](/command-line-usage/mcp) [[ `query_library_search` ]](/mcp/query_library_search) [[ `run_select_query` ]](/mcp/run_select_query) [[ `run_mutation_query` ]](/mcp/run_mutation_query) [[ `run_lifecycle_operation` ]](/mcp/run_lifecycle_operation)

* * *

## 输入 {#inputs}

| 参数 | 必填 | 说明 |
|--|--|--|
|`id`|是|搜索结果中的查询 ID，例如 `aws/s3/buckets-list`。|
|`params`|否|将已声明的参数名映射到值的对象。省略它或传入 `{}` 可查看模板而不进行渲染。|
|`format`|否|文本渲染方式：`markdown` 或 `json`。省略时，使用服务器的渲染设置（默认为 `markdown`）。|

未知参数会被拒绝。既未提供值也没有默认值的必填参数会在 `missing_params` 中报告。所提供的值会按已声明的类型和约束进行检查；未解析的占位符视为错误。

* * *

## 输出 {#output}

默认文本视图是一个 KV 块，包含标识、变更状态、下一步工具、有效性、来源链接，以及模板、渲染后的 SQL 或错误（如有）。如需完整的元数据，请使用结构化负载或 `format: "json"`：

| 字段 | 说明 |
|--|--|
|`id`, `title`, `description`|条目的标识和摘要。|
|`mutation`, `verb`|条目是否更改资源，以及在已声明时其操作类型（`select`、`mutation` 或 `lifecycle`）。|
|`next_tool`|该条目的执行工具：`run_select_query`、`run_mutation_query` 或 `run_lifecycle_operation`。检索不会调用它。|
|`params`|参数声明，包含名称、类型、必填标志，以及默认值、说明、示例、枚举或匹配模式（如有）。|
|`template`, `notes`|原始模板和作者备注，在 `params` 省略或为空时返回。|
|`rendered`, `valid`|是否尝试了渲染以及渲染是否成功。查看原始模板时，两者均为 `false`。|
|`sql`|渲染后的 SQL，仅在渲染成功后存在。|
|`missing_params`, `unknown_params`, `errors`|渲染失败时的验证详情。在这些问题解决之前，不要执行 SQL。|
|`auth`, `permissions`|在已声明时，为执行所需的凭据环境变量和 Provider 原生权限。检索本身两者都不需要。|
|`cost`, `outputs`, `related`|在已声明时，为扇出和开销提示、结果列说明以及相关条目 ID。|
|`doc_url`|供人阅读的库页面。|
|`source_tier`, `source_url`, `stale`|内容出处：`primary`、`fallback` 或 `snapshot`，其 URL 或嵌入式路径，以及内容是否是在获取失败后由缓存或快照提供的。|

未知或格式错误的 ID 会返回错误。使用 `query_library_search` 查找有效的 ID。

* * *

## 门控 {#gating}

在所有服务器模式下均允许，但受所配置的工具允许列表约束。检索或渲染变更模板或生命周期模板不会对云端产生影响，也无需审批。执行返回的 SQL 是一次单独的调用，受服务器[模式](/command-line-usage/mcp#server-modes)约束。

* * *

## 示例 {#example}

先查看一个条目：

```json
{
  "id": "aws/s3/buckets-list"
}
```

然后提供其必需的路由区域：

```json
{
  "id": "aws/s3/buckets-list",
  "params": {
    "region": "us-east-1"
  }
}
```

检查 `rendered` 和 `valid` 之后，使用 [`validate_select_query`](/mcp/validate_select_query) 验证返回的 `SELECT`。使用 `next_tool` 中指定的工具执行它，传入返回的 `sql`，并将 `source` 设置为库条目的 `id` 以标注来源。执行时需要 Provider 凭据和权限。
