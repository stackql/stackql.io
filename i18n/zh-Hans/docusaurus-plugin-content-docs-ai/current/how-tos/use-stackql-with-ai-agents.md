---
title: 如何将 StackQL 与 AI 智能体配合使用
description: 将 StackQL 作为 Model Context Protocol（MCP）服务器运行，使 AI 智能体能够发现云 Provider 的 schema、验证查询，并对正在运行的基础设施执行受治理的读取和变更。
keywords: [stackql, AI 智能体, mcp, model context protocol, 智能体基础设施, 自动化]
proficiencyLevel: Intermediate
faq:
  - question: StackQL 服务器公开哪些 MCP 工具？
    answer: 发现工具（list_providers、list_services、list_resources、list_methods、describe_method）、注册表工具（list_registry、pull_provider）、验证（validate_select_query）、执行（run_select_query、run_mutation_query、run_lifecycle_operation）、server_info，以及 reload_credentials（重新加载 --env.file 指定的 dotenv 文件，并报告每个 Provider 的凭据状态）。enabled_tools 允许列表可以缩小这一工具集，例如用于发布只读的资源清单服务器。
  - question: 如何保证智能体执行变更的安全？
    answer: 通过服务器模式。默认的 safe 模式允许 SELECT 和元数据调用，但 INSERT、UPDATE、DELETE 和 EXEC 需要人工审批（通过 MCP 征询（elicitation）流程）。read_only 直接拒绝变更；delete_safe 允许创建/更新，但对删除进行门控；full_access 允许一切操作。每次工具调用都会写入 JSONL 审计日志，该日志默认开启。
  - question: 支持哪些传输方式？
    answer: stdio 用于嵌入编辑器的客户端（Claude Desktop、Cursor、Continue），HTTP 用于独立智能体，另有反向代理模式，可将 MCP 请求分发到后端的 StackQL PostgreSQL 服务器，并支持可选的 TLS。
---

# 如何将 StackQL 与 AI 智能体配合使用

StackQL 内置了 Model Context Protocol（MCP）服务器。任何支持 MCP 的智能体（Claude、Cursor、Continue 或自定义智能体框架）都可以连接到它，并获得一个自描述、受治理的 SQL 接口，用于访问 StackQL 注册表中的每个云和 SaaS Provider。

## 步骤 {#steps}

1. **设置 Provider 凭据**，在启动服务器之前以环境变量的形式提供（服务器会继承这些变量）：

```bash
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
```

也可以（在 v0.10.542 之后的 StackQL 发布版本中）使用 `--env.file` 指定一个 dotenv 风格的凭据文件；`reload_credentials` MCP 工具会在会话中途重新加载该文件，因此轮换后的凭据无需重启即可到达正在运行的服务器。

2. **启动 MCP 服务器。** 对于独立的 HTTP 服务器：

```bash
stackql mcp \
  --mcp.server.type=http \
  --mcp.config '{"server": {"transport": "http", "address": "127.0.0.1:9912", "allow_unauthenticated": true}}'
```

从 `v0.12.742` 起，HTTP 传输方式要么需要 `server.auth_token_env_var`（一个环境变量，保存每个请求都必须携带的预共享 bearer 令牌），要么需要像此处所示显式选择启用 `allow_unauthenticated`，否则服务器会拒绝启动；对于其他进程或主机可以访问的任何监听器，请使用令牌。`stdio` 不受影响。

对于嵌入编辑器的客户端，使用 `--mcp.server.type=stdio` 并让客户端启动进程（参阅[如何将 StackQL 与 Claude 配合使用](/ai/how-tos/use-stackql-with-claude)）。要在一个进程中同时提供 MCP 和 PostgreSQL 线协议，使用 `stackql srv`，并附带相同的 MCP 标志和 `--pgsrv.port`。

3. **选择安全模式。** 模式在服务器配置中设置，并对智能体可以执行的操作进行门控：

| 模式 | SELECT / 元数据 | INSERT / UPDATE | DELETE | EXEC |
|---|---|---|---|---|
| `read_only` | 允许 | 拒绝 | 拒绝 | 拒绝 |
| `safe`（默认） | 允许 | 需要审批 | 需要审批 | 需要审批 |
| `delete_safe` | 允许 | 允许 | 需要审批 | 需要审批 |
| `full_access` | 允许 | 允许 | 允许 | 允许 |

“需要审批”使用 MCP 征询（elicitation）流程：客户端向用户显示待执行的 SQL，由用户接受或拒绝。对于资源清单智能体，固定使用 `read_only`；`full_access` 只保留给受信任且审计日志有人审查的流水线。包含多条语句的负载按其中权限最高的语句进行门控（从 `v0.12.742` 起），因此变更无法藏在开头的 `SELECT` 之后绕过门控。

4. **让智能体先发现，再查询。** 典型的智能体会话：调用一次 `server_info`，然后 `list_providers` -> `list_methods`（它会给出必需的 `WHERE` 参数）-> `validate_select_query` -> `run_select_query`：

```sql
SELECT name, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

智能体从不加载 SDK；schema 在运行时发现，查询则是普通的 SQL。

## 审计 {#auditing}

每次工具调用都会向支持轮转的文件接收器写入一条 JSONL 记录：时间戳、工具、模式、决策、查询类别、SQL、耗时。审计默认开启，记录智能体做了什么（不记录结果行）。在默认的 `strict` 失败模式下，审计写入失败会作为错误报告，而不会让未经审计的变更悄悄通过。

## 相关概念 {#related-concepts}

- [如何将 StackQL 与 Claude 配合使用](/ai/how-tos/use-stackql-with-claude)：Claude Desktop 配置
- [StackQL MCP 架构](/ai/architecture/stackql-mcp-architecture)：传输方式、模式和审计的内部机制
- [StackQL vs 自定义 MCP 服务器](/ai/comparisons/stackql-vs-custom-mcp-servers)：何时应改为自行构建
- [什么是智能体基础设施？](/ai/canonical-definitions/what-is-agentic-infrastructure)：由此实现的模式
- [为什么 SQL 是云 API 的强大接口](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis)：智能体与 SQL 为何契合
