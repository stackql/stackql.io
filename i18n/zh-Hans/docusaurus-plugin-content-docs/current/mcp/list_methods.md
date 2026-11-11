---
title: list_methods
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_methods
  - model context protocol
description: 列出某个 StackQL 资源的访问方法的 MCP 工具
image: "/img/stackql-featured-image.png"
---

返回绑定到某个资源的访问方法（HTTP 操作），即可用的读取方法、变更和生命周期操作。这是智能体在**编写任何查询之前**应调用的工具，因为必需的 `WHERE` 参数是根据所选的读取方法推断出来的。选择 SQL 动词与意图相符且其必需参数可以提供的方法，然后对该方法调用 [`describe_method`](/mcp/describe_method) 以获取完整契约，包括输出字段。

另请参阅：
[[ MCP 概述 ]](/command-line-usage/mcp) [[ `list_resources` ]](/mcp/list_resources) [[ `describe_method` ]](/mcp/describe_method) [[ `validate_select_query` ]](/mcp/validate_select_query)

* * *

## 输入 {#inputs}

| 参数 | 必填 | 说明 |
|--|--|--|
|`provider`|是|Provider 名称。|
|`service`|是|该 Provider 下的服务。|
|`resource`|是|`provider.service` 下的资源。|

<br/>

:::info

标识符不能包含空白字符、分号或反斜杠；此类输入会在构建任何语句之前被拒绝。

:::

* * *

## 输出 {#output}

| 渲染器 | 结构 |
|--|--|
| 表格 | 每个访问方法一行，包括其映射到的 SQL 动词和必需参数 |

* * *

## 门控 {#gating}

在所有服务器模式下均允许。只读。

* * *

## 示例 {#example}

在任何支持征询（elicitation）并已注册 StackQL MCP 服务器的 MCP 客户端中试用以下提示词：

```
For Google Compute instances, what methods are available  
and what parameters are required?
```

以下是 Claude Desktop 中的示例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_methods_web.mp4" type="video/mp4" />
</video>
