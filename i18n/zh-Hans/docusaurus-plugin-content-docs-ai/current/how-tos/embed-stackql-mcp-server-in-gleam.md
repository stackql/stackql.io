---
title: 如何在 Gleam 应用中嵌入 StackQL MCP 服务器
description: 在 Erlang/BEAM 目标上将 StackQL MCP 服务器嵌入 Gleam 应用并由 OTP 监督，使 BEAM 服务无需运行时依赖即可通过 SQL 执行云查询。
keywords: [stackql, mcp, gleam, erlang, beam, otp, 监督树, 嵌入式 MCP 服务器, model context protocol, 智能体基础设施, pipewatch]
proficiencyLevel: Intermediate
faq:
  - question: 如何为 Gleam 应用添加嵌入式 StackQL MCP 服务器？
    answer: 运行 `gleam add stackql_mcp`，然后使用由 `stackql_mcp.default_config()` 构建的 Config 调用 `stackql_mcp.start(...)`。该库仅支持 Erlang/BEAM 目标（不支持 JavaScript 目标），并通过 child_spec() 以 OTP 子进程的形式提供服务器。启动时，它会下载已签名的二进制文件，经 sha256 校验后存入位于 ~/.stackql/mcp-server-bin/ 的共享缓存。
  - question: Gleam 库是否与 OTP 监督机制集成？
    answer: 是的。服务器通过 child_spec() 以 OTP 子进程的形式提供，因此可以放入你自己的监督树中，另有 supervised_list_tools() 和 supervised_call_tool() 变体，用于在监督者下调用工具。该库仅支持 Erlang/BEAM 运行时。
  - question: 如何在 Gleam 中让嵌入式 StackQL MCP 服务器保持只读？
    answer: "模式通过 Config.mode 设置，默认为 read_only，该模式拒绝 INSERT、UPDATE、DELETE 和 EXEC。使用 `stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)` 构建 Config，即可提升到 safe、delete_safe 或 full_access，权限提升始终需要显式进行。"
  - question: Gleam 库如何解析 StackQL 二进制文件？
    answer: 首先是显式指定的 Config.binary，然后是 STACKQL_MCP_BIN 环境变量，最后是位于 ~/.stackql/mcp-server-bin/ 的共享缓存。对于离线或物理隔离的部署，将 STACKQL_MCP_BUNDLE 指向本地 bundle。下载的 bundle 会对照内置的 sha256 固定哈希值进行校验。
---

# 如何在 Gleam 应用中嵌入 StackQL MCP 服务器

StackQL 的 MCP 服务器可以在 Erlang/BEAM 目标上的 Gleam 应用内运行，无需单独安装，也没有运行时依赖。`stackql_mcp` 库通过 stdio 启动已签名的 StackQL 二进制文件，并与 OTP 监督机制集成，因此 BEAM 服务可以获得一个受治理、自描述的 SQL 接口，用于访问 StackQL 注册表中的每个云和 SaaS Provider。服务器通过 `child_spec()` 以 OTP 子进程的形式提供，因此可以放入你自己的监督树中。

## 步骤 {#steps}

1. **添加库：**

```
gleam add stackql_mcp
```

仅支持 Erlang/BEAM 目标（不支持 JavaScript 目标）。

2. **启动服务器并列出工具。** `Config.mode` 默认为 `read_only`：

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

使用 `child_spec()` 将服务器置于监督者之下，并使用 `supervised_list_tools()` 和 `supervised_call_tool()` 进行受监督的调用。

3. **选择模式。** `Config.mode` 默认为 `read_only`。构建 `stackql_mcp.Config(..stackql_mcp.default_config(), mode: stackql_mcp.safe)` 即可提升到 `safe`、`delete_safe` 或 `full_access`。权限提升必须显式进行。

## 二进制文件的获取 {#binary-sourcing}

启动时，该库会下载对应平台的 StackQL bundle，对照内置的 sha256 固定哈希值进行校验，并将其缓存到该库系列共享的缓存中。解析优先级：显式指定的 `Config.binary`，然后是 `STACKQL_MCP_BIN`，最后是位于 `~/.stackql/mcp-server-bin/` 的共享缓存。对于离线或物理隔离的部署，将 `STACKQL_MCP_BUNDLE` 指向本地 bundle。

## 演示应用 {#demo-app}

**pipewatch** 是为该库计划的演示应用。它已列入路线图，尚未实现。

## 相关概念 {#related-concepts}

- [嵌入式 MCP：Gleam 参考](/mcp/embedded/gleam)：安装、API 和二进制文件的获取
- [如何将 StackQL 与 AI 智能体配合使用](/ai/how-tos/use-stackql-with-ai-agents)：MCP 模型和安全模式
- [StackQL MCP 架构](/ai/architecture/stackql-mcp-architecture)：传输方式、模式和审计的内部机制
- [什么是智能体基础设施？](/ai/canonical-definitions/what-is-agentic-infrastructure)：由此实现的模式
