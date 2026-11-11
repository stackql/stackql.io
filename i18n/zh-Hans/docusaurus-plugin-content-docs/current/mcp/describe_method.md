---
title: describe_method
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - describe_method
  - model context protocol
description: 返回单个 StackQL 访问方法的完整 I/O 契约的 MCP 工具
image: "/img/stackql-featured-image.png"
---

返回单个访问方法的完整 I/O 契约，始终为 EXTENDED，即输入、请求体 schema 和完整的输出结构。智能体从这里获取列名：每一行都带有一个 `param_type`，其值为 `input_required`（必需的精确匹配 `WHERE` 谓词）、`input_optional` 或 `output`（`SELECT` 或 `RETURNING` 可以引用的字段）。资源没有单一的字段列表，因为每个方法返回各自的结构，因此在首次查询某个方法之前，应先对从 [`list_methods`](/mcp/list_methods) 中选定的该方法调用此工具。

另请参阅：
[[ MCP 概述 ]](/command-line-usage/mcp) [[ `list_methods` ]](/mcp/list_methods) [[ `run_select_query` ]](/mcp/run_select_query)

* * *

## 输入 {#inputs}

| 参数 | 必填 | 说明 |
|--|--|--|
|`provider`|是|Provider 名称。|
|`service`|是|该 Provider 下的服务。|
|`resource`|是|`provider.service` 下的资源。|
|`method`|是|[`list_methods`](/mcp/list_methods) 返回的方法名称。|

<br/>

:::info

标识符不能包含空白字符、分号或反斜杠；此类输入会在构建任何语句之前被拒绝。

:::

* * *

## 输出 {#output}

| 渲染器 | 结构 |
|--|--|
| KV | 每个输入和输出字段一行：名称、类型、`param_type`（`input_required`、`input_optional` 或 `output`）、结构和说明 |

* * *

## 门控 {#gating}

在所有服务器模式下均允许。只读。

* * *

## 示例 {#example}

在任何支持征询（elicitation）并已注册 StackQL MCP 服务器的 MCP 客户端中试用以下提示词：

```
Show me everything I need to know to call the insert method  
on Google Compute networks -- required parameters,  
optional parameters, and the request body shape.
```

以下是 Claude Desktop 中的示例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_describe_method_web.mp4" type="video/mp4" />
</video>
