---
title: list_resources
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - list_resources
  - model context protocol
description: 列出某个 StackQL provider.service 下的资源的 MCP 工具
image: "/img/stackql-featured-image.png"
---

返回某个 `provider.service` 下的资源，即 StackQL [资源层次结构](/getting-started/resource-hierarchy)的第三层。资源是出现在 StackQL 查询的 `FROM` 子句中的目标。

另请参阅：
[[ MCP 概述 ]](/command-line-usage/mcp) [[ `list_services` ]](/mcp/list_services) [[ `list_methods` ]](/mcp/list_methods) [[ `describe_method` ]](/mcp/describe_method)

* * *

## 输入 {#inputs}

| 参数 | 必填 | 说明 |
|--|--|--|
|`provider`|是|Provider 名称。|
|`service`|是|该 Provider 下的服务。|

<br/>

:::info

标识符不能包含空白字符、分号或反斜杠；此类输入会在构建任何语句之前被拒绝。

:::

* * *

## 输出 {#output}

| 渲染器 | 结构 |
|--|--|
| 表格 | `provider.service` 下的每个资源一行 |

* * *

## 门控 {#gating}

在所有服务器模式下均允许。只读。

* * *

## 示例 {#example}

在任何支持征询（elicitation）并已注册 StackQL MCP 服务器的 MCP 客户端中试用以下提示词：

```
List the resources in the google.storage service
```

以下是 Claude Desktop 中的示例：

<video autoPlay loop muted playsInline style={{maxWidth: '600px', width: '100%', display: 'block', margin: '0 auto', border: 'none', outline: 'none'}}>
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.webm" type="video/webm" />
  <source src="/img/videos/claude_desktop_mcp_list_resources_web.mp4" type="video/mp4" />
</video>
