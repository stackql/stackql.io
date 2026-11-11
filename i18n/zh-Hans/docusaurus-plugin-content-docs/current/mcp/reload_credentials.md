---
title: reload_credentials
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - reload_credentials
  - model context protocol
description: 从 --env.file 指定的 dotenv 文件重新加载凭据并报告各 Provider 解析状态的 MCP 工具
image: "/img/stackql-featured-image.png"
---

将 [`--env.file`](/command-line-usage/global-flags) 标志指定的 dotenv 风格文件重新加载到服务器的进程环境中，然后报告每个 Provider 的凭据解析状态。这解决了环境在进程启动时即被固定的问题：会话中途写入磁盘或在磁盘上轮换的凭据，无需重启即可到达正在运行的服务器。

env 文件叠加在进程环境之上，而不是替换它：凭据解析始终在请求时读取实时的进程环境，因此进程启动时已存在的变量（来自 shell 或客户端的 `env` 块）无论是否使用 `--env.file` 都继续有效。重新加载时，只写入文件中值非空的键，并且绝不会取消设置任何变量，因此失败或部分完成的重新加载会保留此前可用的凭据。

机密值绝不会被返回、写入日志或审计，该工具只报告变量名和状态。未配置 `--env.file` 时，该工具退化为纯粹的状态探测。当查询因凭据解析而失败时，MCP 错误会带有一条提示信息，指示智能体调用 `reload_credentials` 并重试，因此智能体客户端可以自我修复。

:::note

在 `v0.10.542` 之后的 StackQL 发布版本中可用。

:::

另请参阅：
[[ MCP 概述 ]](/command-line-usage/mcp) [[ 凭据（重新）加载 ]](/command-line-usage/mcp#credential-resourcing---envfile--reload_credentials) [[ `server_info` ]](/mcp/server_info) [[ Claude Desktop ]](/getting-started/claude-desktop)

* * *

## 输入 {#inputs}

| 参数 | 必填 | 说明 |
|--|--|--|
|`provider`|否|将状态报告的范围限定为单个 Provider。省略时，报告所有 Provider。|

* * *

## 输出 {#output}

| 渲染器 | 结构 |
|--|--|
| 表格 | env 文件加载结果，外加每个 Provider 一行的凭据解析状态 |

<br/>

结构化负载包含：

| 字段 | 说明 |
|--|--|
|`env_file`|所配置的 `--env.file` 路径（未配置时为空）。|
|`env_file_sourced`|本次调用中找到并加载了该文件时为 `true`。|
|`sourced_vars`|从该文件设置的环境变量的名称，仅有名称，绝不包含值。|
|`providers`|每个 Provider 一行（见下文）。|

<br/>

每个 `providers` 行包含：

| 列 | 说明 |
|--|--|
|`provider`|Provider 名称。|
|`auth_type`|为该 Provider 配置的身份验证类型。|
|`sourced_from`|凭据的解析来源，例如 `env:OKTA_SECRET_KEY`、`file:/path/key.json`、`inline` 或 `none`。|
|`status`|`ok`、`unresolved` 或 `not_checked`。|
|`detail`|状态为 `unresolved` 时的错误文本。|

<br/>

在凭据字节路径之外进行身份验证的身份验证类型（交互式 gcloud、azure CLI、oauth2 客户端凭据、assume-role 链、OCI 签名、null）报告 `not_checked`。

* * *

## 门控 {#gating}

在所有服务器模式下均允许，包括 `read_only`：除服务器自身的进程环境外，该工具不会变更任何内容。

在 `reverse_proxy` 后端模式下不受支持：查询在远程 stackql 服务器进程中执行，因此该工具会返回错误，指示用户在后端服务器上重新加载凭据。

* * *

## 示例 {#example}

在任何已注册 StackQL MCP 服务器（以 `--env.file` 启动）的 MCP 客户端中试用以下提示词：

```
My okta query failed with a credential error - I've just written the key
to the credentials file.  Reload credentials and retry the query.
```
