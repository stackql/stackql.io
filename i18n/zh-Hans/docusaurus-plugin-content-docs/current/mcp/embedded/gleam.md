---
title: Gleam
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - mcp
  - model context protocol
  - 嵌入式
  - gleam
  - erlang
  - beam
description: 在 Erlang/BEAM 目标上将 StackQL MCP 服务器嵌入 Gleam 应用
image: "/img/stackql-featured-image.png"
---

# Gleam

在 Erlang/BEAM 目标上将 StackQL MCP 服务器嵌入 Gleam 应用。该库与 OTP 监督机制集成，下载并校验已签名的二进制文件，并通过 stdio 启动它。

:::info
嵌入的二进制文件在首次运行时下载并经 sha256 校验，或以 vendoring 方式打包进你的应用。参阅[通用契约](/mcp/embedded#common-contract)。
:::

* * *

## 安装 {#install}

```
gleam add stackql_mcp
```

仅支持 Erlang/BEAM 目标（不支持 JavaScript 目标）。服务器通过 `child_spec()` 以 OTP 子进程的形式提供，因此可以放入你自己的监督树中。

* * *

## 快速入门 {#quick-start}

```gleam
import gleam/io
import gleam/int
import gleam/list
import envoy
import stackql_mcp

pub fn main() {
  let config =
    stackql_mcp.Config(
      ..stackql_mcp.default_config(),
      auth: [stackql_mcp.auth_for("github", "null_auth")],
    )

  let assert Ok(server) =
    stackql_mcp.start(
      config: config,
      home: "/home/u",
      os: "linux",
      arch: "x86_64",
      getenv: envoy.get,
    )

  let assert Ok(tools) = stackql_mcp.list_tools(server)
  io.println(int.to_string(list.length(tools)) <> " tools available")

  stackql_mcp.stop(server)
}
```

API 为：用于生命周期的 `stackql_mcp.start()`、`stop()` 和 `child_spec()`；用于操作的 `list_tools()` 和 `call_tool()`（另有在监督者下使用的 `supervised_list_tools()` 和 `supervised_call_tool()` 变体）；以及用于配置的 `Config`、`default_config()` 和 `auth_for()`。

* * *

## 模式 {#modes}

模式通过 `Config.mode` 设置，默认为 `read_only`。将其设置为 `safe`、`delete_safe` 或 `full_access` 即可提升权限：

```gleam
stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)
```

权限提升必须显式进行：除非你明确选择启用，否则服务器不会启用变更或生命周期工具。

* * *

## Vendoring / 嵌入 {#vendoring--embedding}

启动时，该库会下载对应平台的 StackQL bundle，对照内置的 sha256 固定哈希值进行校验，并将其缓存到该库系列共享的缓存中。解析优先级：

1. 显式指定的 `Config.binary`。
2. `STACKQL_MCP_BIN` 环境变量。
3. 位于 `~/.stackql/mcp-server-bin/<version>/<platform-key>/` 的共享缓存。

对于离线或物理隔离的部署，将 `STACKQL_MCP_BUNDLE` 指向本地 bundle。

* * *

## 演示应用 {#demo-app}

**pipewatch** 是为该库计划的演示应用。它已列入路线图，尚未实现。

* * *

## 仓库链接 {#repo-link}

[github.com/stackql/stackql-mcp-gleam](https://github.com/stackql/stackql-mcp-gleam)
