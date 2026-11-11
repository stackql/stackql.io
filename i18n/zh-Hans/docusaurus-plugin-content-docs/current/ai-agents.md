---
slug: /ai-agents
title: AI 智能体
hide_title: false
keywords:
  - stackql
  - AI 智能体
  - mcp
  - model context protocol
  - 智能体式
description: 通过 MCP 服务器、工具、嵌入式集成和 SQL 查询，使用 StackQL 将 AI 智能体连接到云和 SaaS 服务。
image: "/img/stackql-featured-image.png"
---

StackQL 为 AI 智能体提供与人相同的 SQL 接口，用于查询、预配和运维云与 SaaS 服务。智能体发现 Provider 公开的内容，在查询运行前对其进行验证，使用 `SELECT` 读取实时状态，并使用 `INSERT`、`UPDATE` 和 `DELETE` 变更状态，无需状态文件，也无需各云专用的 SDK。

## 智能体如何连接 {#how-agents-connect}

- **基于 stdio 的 MCP**，适用于 Claude Desktop、Cursor 和 Continue 等编辑器和桌面客户端。客户端自行启动 `stackql mcp --mcp.server.type=stdio`。请参阅 [MCP 服务器](/command-line-usage/mcp)。
- **基于 HTTP 的 MCP**，适用于连接到长期运行的 `stackql mcp` 或 `stackql srv` 进程的独立智能体和自动化，支持 bearer 令牌身份验证和可选的 TLS。
- **嵌入式 MCP**，适用于编译型应用程序：服务器在 Go、Rust、Swift、Kotlin、.NET 和 Gleam 中以进程内方式运行。请参阅[嵌入式 MCP](/mcp/embedded)。
- **PostgreSQL 线协议**，通过 [`stackql srv`](/command-line-usage/srv) 提供，适用于已支持 Postgres 的智能体框架、notebook 和 BI 工具。

## 使用 Claude Desktop 快速入门 {#quick-start-with-claude-desktop}

从 Anthropic Connector Directory 安装（推荐），或通过可下载的 MCP Bundle 安装，也可以在 `claude_desktop_config.json` 中手动注册服务器：

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": ["mcp", "--mcp.server.type=stdio", "--tls.allowInsecure"],
      "env": {
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key"
      }
    }
  }
}
```

只需包含你计划使用的 Provider 的凭据。完整说明（包括 Bundle 下载和使用凭据文件）见[在 Claude Desktop 中使用 StackQL](/getting-started/claude-desktop)。

## 智能体可以做什么 {#what-an-agent-can-do}

MCP 服务器公开一组数量不多、带类型的工具。每个工具都为模型返回一个渲染后的视图，并为程序化客户端返回一个结构化负载。

- **发现**：`list_providers`、`list_services`、`list_resources`、`list_methods` 和 `describe_method` 遍历 Provider 层次结构，并返回每个操作的输入和输出字段，因此在编写查询之前就能知道必需的 `WHERE` 参数。
- **验证和读取**：`validate_select_query` 解析并规划一条 `SELECT`，但不运行它；`run_select_query` 执行它。
- **变更**：`run_mutation_query` 运行 `INSERT`、`UPDATE`、`REPLACE` 和 `DELETE`；`run_lifecycle_operation` 运行 `EXEC` 生命周期操作。两者都由下文的服务器模式门控。
- **日常维护**：`server_info`、`list_registry`、`pull_provider` 和 `reload_credentials`。

完整参考（包括每个工具的输入、门控行为和示例提示词）见 [MCP 工具](/mcp)。

## 护栏 {#guardrails}

服务器模式为整个会话设定安全契约：

| 模式 | `SELECT` / 元数据 | `INSERT` / `UPDATE` / `REPLACE` | `DELETE` / `EXEC` |
|--|--|--|--|
| `read_only` | 允许 | 拒绝 | 拒绝 |
| `safe`（默认） | 允许 | 需要审批 | 需要审批 |
| `delete_safe` | 允许 | 允许 | 需要审批 |
| `full_access` | 允许 | 允许 | 允许 |

“需要审批”使用 MCP 征询（elicitation）：支持它的客户端（Claude Desktop、Cursor、Continue）向用户显示工具名称、查询类别和 SQL，并在任何操作运行之前等待用户决定；未声明支持它的客户端会被拒绝。每次工具调用都会写入审计日志，HTTP 监听器要求提供 bearer 令牌，凭据来自环境或 dotenv 文件，机密值绝不会返回给客户端。详情见[服务器模式](/command-line-usage/mcp#server-modes)、[HTTP 客户端身份验证](/command-line-usage/mcp#http-client-authentication)和[审计日志](/command-line-usage/mcp#audit-log)。

## 后续步骤 {#next-steps}

- [MCP 服务器](/command-line-usage/mcp)：部署模式、配置、服务器模式、审计日志和标志。
- [MCP 工具](/mcp)：完整的工具参考，包括输入、门控和示例提示词。
- [嵌入式 MCP](/mcp/embedded)：在你自己的 Go、Rust、Swift、Kotlin、.NET 或 Gleam 应用程序中运行服务器。
- [Query Library](pathname:///docs/query-library/)：针对常见云运维问题的即用型 SQL，按 Provider 组织。
- [AI 参考](/ai)：为智能体及其构建者编写的概念、操作指南、对比和故障排查内容。
