---
title: 如何将 StackQL 与 Claude 配合使用
description: 在 Claude Desktop 或 Claude Code 中将 StackQL 注册为 MCP 服务器，使 Claude 能够用自然语言查询和管理 AWS、Azure、Google Cloud、GitHub 及其他 Provider 上的云基础设施。
keywords: [stackql, claude, claude desktop, claude code, mcp, anthropic, 云基础设施]
proficiencyLevel: Beginner
faq:
  - question: Claude 需要了解 StackQL 语法吗？
    answer: 不需要专门的训练。Claude 在运行时通过 MCP 发现工具（list_providers、list_methods、describe_method）发现 Provider 的 schema，并编写标准 SQL，这是它本来就擅长的。你使用自然语言交互，Claude 将其转换为 StackQL 查询。
  - question: Claude 在更改我的基础设施之前会先询问吗？
    answer: 在默认配置下会。StackQL MCP 服务器默认使用 safe 模式，且 Claude Desktop 通告了 MCP 征询（elicitation）能力，因此每个 INSERT、UPDATE、DELETE 或生命周期操作在运行前都会触发显示该 SQL 的审批请求。将 mode 设置为 read_only 可完全阻止变更。
  - question: 我的云凭据放在哪里？
    answer: 放在 MCP 配置文件中 stackql 条目的 env 块里，或放在服务器进程继承的环境中。在 v0.10.542 之后的 StackQL 发布版本中，也可以改用 --env.file 标志传入 dotenv 风格的凭据文件，reload_credentials 工具无需重启 Claude Desktop 即可重新加载该文件。凭据与 StackQL 二进制文件一起保留在你的机器上，不会发送给模型。
---

# 如何将 StackQL 与 Claude 配合使用

Claude 通过 Model Context Protocol 连接到 StackQL：在 Claude Desktop（或 Claude Code）中将 `stackql` 注册为 MCP 服务器后，Claude 就可以在自然语言的驱动下，对 StackQL 支持的每个 Provider 中的云资源进行清点、审计，并在你批准后进行修改。

## 步骤 {#steps}

1. **安装 StackQL**，并确认它在你的 PATH 中：

```bash
stackql --version
```

2. **编辑 Claude Desktop 配置文件**：

- Windows: `%APPDATA%\Claude\claude_desktop_config.json`
- macOS: `~/Library/Application Support/Claude/claude_desktop_config.json`
- Linux: `~/.config/Claude/claude_desktop_config.json`

3. **添加服务器条目**，其中包含所用 Provider 的凭据：

```json
{
  "mcpServers": {
    "stackql": {
      "command": "stackql",
      "args": [
        "mcp",
        "--mcp.server.type=stdio",
        "--mcp.config",
        "{\"server\": {\"transport\": \"stdio\", \"mode\": \"safe\"}}"
      ],
      "env": {
        "AWS_ACCESS_KEY_ID": "your-aws-access-key-id",
        "AWS_SECRET_ACCESS_KEY": "your-aws-secret-access-key",
        "STACKQL_GITHUB_USERNAME": "your-github-username",
        "STACKQL_GITHUB_PASSWORD": "your-github-personal-access-token"
      }
    }
  }
}
```

只包含你需要的 Provider（Azure 使用 `AZURE_TENANT_ID`/`AZURE_CLIENT_ID`/`AZURE_CLIENT_SECRET`，Google Cloud 使用 `GOOGLE_CREDENTIALS`，Databricks 使用 `DATABRICKS_CLIENT_ID`/`DATABRICKS_CLIENT_SECRET`）。切勿将此文件提交到版本控制系统。

4. **重启 Claude Desktop。** 对话开始时服务器会自动启动。

5. **用自然语言提问。** 以下示例可直接对应到 StackQL 操作：

- “列出 us-east-1 中所有具有公有 IP 地址的 EC2 实例”
- “我们的 GitHub 组织中有哪些仓库禁用了分支保护？”
- “在 us-west-2 中创建一个名为 analytics-data 的 S3 存储桶”

对于读取操作，Claude 会发现 schema 并立即运行 `SELECT` 语句。对于创建存储桶的请求，`safe` 模式会在执行任何操作之前触发审批请求，显示确切的 `INSERT`。

## 控制 Claude 可以执行的操作 {#controlling-what-claude-can-do}

配置中的 `mode` 字段就是契约：`read_only` 用于仅做资源清单的助手，`safe`（默认）用于受审批门控的更改，`delete_safe` 允许创建/更新但对删除进行门控，`full_access` 仅用于受信任的自动化。无论处于哪种模式，Claude 发出的每次调用都会记录在服务器的 JSONL 审计日志中。

## 相关概念 {#related-concepts}

- [如何将 StackQL 与 AI 智能体配合使用](/ai/how-tos/use-stackql-with-ai-agents)：通用的 MCP 设置，包括 HTTP 传输方式
- [StackQL MCP 架构](/ai/architecture/stackql-mcp-architecture)：模式、征询（elicitation）和审计的详细说明
- [如何让 StackQL 通过 AWS 的身份验证](/ai/how-tos/authenticate-stackql-to-aws)：各 Provider 的凭据参考
- [什么是智能体基础设施？](/ai/canonical-definitions/what-is-agentic-infrastructure)：更广泛的模式
- [StackQL vs 自定义 MCP 服务器](/ai/comparisons/stackql-vs-custom-mcp-servers)：为什么一个通用服务器胜过许多定制服务器
