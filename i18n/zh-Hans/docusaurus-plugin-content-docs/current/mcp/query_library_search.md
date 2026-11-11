---
title: query_library_search
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - query_library_search
  - query library
  - model context protocol
description: 按自然语言意图、Provider、服务和标签搜索精选的 StackQL 查询模板的 MCP 工具
image: "/img/stackql-featured-image.png"
---

在 [StackQL Query Library](pathname:///docs/query-library/) 中搜索与自然语言意图匹配的模板。在从头编写 SQL 之前先查阅它，然后将某条结果的 `id` 传给 [`query_library_get`](/mcp/query_library_get)，以查看模板并验证其参数。

该工具仅检索库中的内容。它不执行 SQL，不调用云 API，也不需要 Provider 凭据。

另请参阅：
[[ MCP 概述 ]](/command-line-usage/mcp) [[ `query_library_get` ]](/mcp/query_library_get) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## 输入 {#inputs}

| 参数 | 必填 | 说明 |
|--|--|--|
|`intent`|是|任务的自然语言描述。|
|`provider`|否|按 Provider 过滤，例如 `aws`。|
|`service`|否|按服务过滤，例如 `s3`。|
|`tags`|否|标签数组。条目必须匹配所提供的每个标签。|
|`include_mutations`|否|包含变更模板。默认为 `false`；在 `read_only` 模式下被忽略，该模式下变更条目始终被排除。|
|`limit`|否|最大命中数。省略或为非正数时，默认为 `5`；上限为 `20`。|
|`format`|否|文本渲染方式：`markdown` 或 `json`。省略时，使用服务器的渲染设置（默认为 `markdown`）。|

* * *

## 输出 {#output}

默认文本视图是一张按排名列出 `id`、`title`、`mutation`、`required_params` 和 `score` 的表格，其后是来源引用和未命中指引（如有）。结构化负载包含：

| 字段 | 说明 |
|--|--|
|`hits`|按排名排列的条目，包含 `id`、`title`、可选的 `description`、`providers`、`services` 和 `required_params`，以及 `mutation` 和 `score`。如需完整的参数声明，请接着调用 `query_library_get`。|
|`miss`|没有命中或最高得分低于匹配阈值时为 `true`。仍可能返回得分较低的最接近匹配项。|
|`guidance`|未命中时，引导智能体借助服务器说明、发现工具和 `validate_select_query` 编写查询。|
|`source_tier`|内容来源：`primary`、`fallback` 或 `snapshot`。|
|`source_url`|所用目录的 URL 或嵌入式路径，供引用。|
|`stale`|为 `true` 时，表示内容是在获取失败后由缓存或嵌入式快照提供的。|

已弃用的条目会被排除。服务器会缓存目录，并可从托管的库回退到其 GitHub 源和嵌入式快照。

* * *

## 门控 {#gating}

在所有服务器模式下均允许，但受所配置的工具允许列表约束。无需审批或 Provider 凭据。搜索变更模板并不授权执行它们。

* * *

## 示例 {#example}

工具参数：

```json
{
  "intent": "List S3 bucket names and regions",
  "provider": "aws",
  "service": "s3",
  "limit": 5
}
```

将 `hits` 中的某个 `id`（例如 `aws/s3/buckets-list`）用于 [`query_library_get`](/mcp/query_library_get)。如果 `miss` 为 `true`，可查看最接近的模板作为参考，或在编写 SQL 之前使用 [`list_methods`](/mcp/list_methods) 和 [`describe_method`](/mcp/describe_method)。
