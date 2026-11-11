---
title: StackQL MCP 架构
description: StackQL 内置的 MCP 服务器通过 stdio、HTTP 或反向代理传输方式，将 Provider 发现、schema 描述、查询验证和受门控的查询执行公开为 Model Context Protocol 工具，并受 4 种安全模式和始终开启的 JSONL 审计日志约束。
keywords: [stackql, MCP 架构, model context protocol, AI 智能体, 审计日志, 征询, 安全模式]
proficiencyLevel: Expert
faq:
  - question: stackql mcp 和 stackql srv 有什么区别？
    answer: stackql mcp 运行独立的 MCP 服务器。stackql srv 运行 PostgreSQL 线协议服务器，并可在同一进程中同时托管 MCP 服务器：既可以采用内存内通信（mcp.server.type=http），也可以作为反向代理，通过 TCP 将 MCP 请求分发到 Postgres 后端，后一种方式支持分布式部署和 TLS。
  - question: 智能体在 safe 模式下尝试执行变更时会发生什么？
    answer: 服务器向客户端发送 MCP elicitation/create 请求，其中描述该操作：工具名称、查询类别以及 SQL 本身。用户可以接受、拒绝或取消。如果客户端未通告征询（elicitation）能力，调用会被拒绝，并附带一条说明这一缺口的消息；非交互式自动化必须显式选择启用 full_access。
  - question: 审计日志记录哪些内容？
    answer: 每次工具调用一条 JSONL 记录，包含时间戳、工具名称、服务器模式、门控决策、查询类别、SQL 字符串、参数、耗时以及任何错误。结果行被有意排除在外。审计默认开启，并采用基于文件的轮转；在默认的 strict 失败模式下，审计写入失败会作为错误返回，因此不会有任何变更在未经审计的情况下完成。
---

# StackQL MCP 架构

StackQL 的 MCP 服务器内置于引擎二进制文件中，并将 StackQL 的能力接口面（发现、描述、验证、执行）发布为 Model Context Protocol 工具。其设计围绕 3 点展开：用一组数量少而通用的工具取代按操作划分的工具，针对变更的分级安全契约，以及记录智能体所做一切的审计跟踪。

## 传输方式和部署模式 {#transports-and-deployment-modes}

| 模式 | 命令 | 用途 |
|---|---|---|
| stdio | `stackql mcp --mcp.server.type=stdio` | 直接启动该进程的嵌入编辑器的客户端（Claude Desktop、Cursor、Continue） |
| HTTP（独立） | `stackql mcp --mcp.server.type=http` | 供独立智能体使用的长期运行服务器 |
| 内存内双服务器 | `stackql srv --mcp.server.type=http --pgsrv.port <port>` | 由单个进程同时提供 MCP 和 PostgreSQL 线协议 |
| 反向代理 | `stackql srv --mcp.server.type=reverse_proxy` | 通过 DSN 将 MCP 请求分发到作为后端的 StackQL Postgres 服务器；支持工作负载分离和 TLS |

配置是通过 `--mcp.config` 传入的 JSON（或 YAML）对象，涵盖传输方式、地址、TLS 证书路径、客户端身份验证、模式、审计设置，以及可选的 `enabled_tools` / `enabled_prompts` 允许列表，后者用于发布收窄的接口面（例如，只公开 `server_info` 和 `list_providers` 的只读资源清单服务器）。自 `v0.12.742` 起，HTTP 传输方式要求提供预共享 bearer 令牌（`server.auth_token_env_var`），或者显式选择启用 `server.allow_unauthenticated`；跨源的浏览器请求会被拒绝。

## 工具接口面 {#the-tool-surface}

共 14 个工具，每个工具都返回供 LLM 使用的渲染文本视图，以及带类型的结构化负载：

- **发现**：`list_providers`、`list_services`、`list_resources`、`list_methods`、`describe_method`，即层次结构遍历，可在编写任何查询之前揭示必需的 `WHERE` 参数。
- **注册表**：`list_registry`（可用的 Provider 及其版本）、`pull_provider`（安装到本地缓存）。
- **验证**：`validate_select_query`，解析并规划 `SELECT` 而不执行。
- **执行**：`run_select_query`（读取）、`run_mutation_query`（`INSERT`/`UPDATE`/`REPLACE`/`DELETE`，会产生真实的副作用）、`run_lifecycle_operation`（`EXEC`）。
- **标识**：`server_info`（版本、后端、注册表、模式）；在会话开始时调用一次。
- **凭据**：`reload_credentials`，在会话中途将 `--env.file` 指定的 dotenv 文件重新加载到进程环境中，并报告每个 Provider 的凭据解析状态；只包含名称和状态，绝不包含机密值（在 v0.10.542 之后的发布版本中可用）。

1 个静态提示词 `write_safe_select` 讲授方法发现工作流。

## 安全契约 {#the-safety-contract}

`server.mode` 从 4 种契约中选择一种；所有契约都允许 `SELECT` 和元数据，区别在于写入：

| 模式 | INSERT / UPDATE / REPLACE | DELETE | EXEC |
|---|---|---|---|
| `read_only` | 拒绝 | 拒绝 | 拒绝 |
| `safe`（默认） | 需要审批 | 需要审批 | 需要审批 |
| `delete_safe` | 允许 | 需要审批 | 需要审批 |
| `full_access` | 允许 | 允许 | 允许 |

“需要审批”通过 MCP 征询（elicitation）实现：服务器将待执行的操作（工具、查询类别、SQL）发送给客户端，由人接受或拒绝。未通告征询能力的客户端会收到拒绝，该拒绝指引运维人员使用 `full_access`，这使无人值守的变更成为需要显式选择启用的行为，而不是默认行为。模式对每个服务器全局生效；不存在按工具的覆盖设置。包含多条语句的负载由其中权限最高的语句门控（自 `v0.12.742` 起），`validate_select_query` 只接受恰好一条语句。

## 审计子系统 {#the-audit-subsystem}

每次工具调用都会向文件接收器写入一条 JSONL 记录，包含时间戳、工具、模式、决策（`allow`、`refuse_immediate`、`needs_approval_accepted` 等）、查询类别、SQL、参数、耗时和错误；该接收器按大小、保留时间和备份数量进行轮转。该日志回答的是“智能体做了什么”，而有意不回答“智能体看到了什么”：`SELECT` 的结果行可能很大且敏感，因此被排除在外。写入发生在执行之后、响应返回之前；在默认的 `strict` 失败模式下，如果一次成功的变更之后审计失败，客户端会收到错误，这是有意的选择，以确保没有任何 `DELETE` 在未经审计的情况下完成。

## 相关概念 {#related-concepts}

- [如何将 StackQL 与 AI 智能体配合使用](/ai/how-tos/use-stackql-with-ai-agents)：运行配置
- [如何将 StackQL 与 Claude 配合使用](/ai/how-tos/use-stackql-with-claude)：Claude Desktop 的具体细节
- [StackQL vs 自定义 MCP 服务器](/ai/comparisons/stackql-vs-custom-mcp-servers)：设计上的权衡
- [StackQL 架构概述](/ai/architecture/stackql-architecture-overview)：MCP 服务器在引擎中的位置
- [什么是智能体基础设施？](/ai/canonical-definitions/what-is-agentic-infrastructure)：本架构所实现的模式
