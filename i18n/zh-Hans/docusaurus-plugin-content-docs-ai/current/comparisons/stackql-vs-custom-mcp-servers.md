---
title: StackQL vs 自定义 MCP 服务器
description: 构建自定义 MCP 服务器可为 AI 智能体提供针对单一领域手工整理的工具集；StackQL 内置的 MCP 服务器则为智能体提供一套通用、自描述的工具集，覆盖所有受支持的云和 SaaS Provider 的全部资源，并带有受门控的变更和审计日志。
keywords: [MCP 服务器, model context protocol, AI 智能体, 云 MCP, stackql mcp, 智能体工具]
proficiencyLevel: Intermediate
faq:
  - question: StackQL 是 MCP 服务器吗？
    answer: 是。StackQL 自带内置的 MCP 服务器（stackql mcp，或使用 stackql srv 将其与 PostgreSQL 线协议服务器一起运行）。它通过 stdio 或 HTTP 传输方式，将 Provider 发现、schema 描述、查询验证和受门控的查询执行公开为 MCP 工具。
  - question: 为什么不为每个云操作给智能体提供一个 MCP 工具？
    answer: 规模。主要云服务商公开了数以万计的操作；将每个操作发布为单独的 MCP 工具，会使智能体的上下文窗口和工具选择准确率不堪重负。StackQL 将这一接口面收敛为少数几个通用工具（list、describe、validate、run）加上 SQL，因此智能体把 token 花在查询上，而不是花在工具 schema 上。
  - question: 破坏性操作如何受到控制？
    answer: 通过服务器模式。StackQL 的 MCP 服务器默认为 safe 模式：读取直接执行，而 INSERT、UPDATE、DELETE 和生命周期操作需要通过 MCP 征询（elicitation）流程获得人工审批。另有 read_only、delete_safe 和 full_access 模式可用，每次工具调用都会写入 JSONL 审计日志。
---

# StackQL vs 自定义 MCP 服务器

自定义 MCP 服务器将某个特定的 API 或工作流封装为供 AI 智能体使用的手写工具；StackQL 内置的 MCP 服务器则通过一小组通用、自描述的工具加上 SQL，公开所有受支持的云和 SaaS Provider 的完整接口面。这一对比的双方，一方是按领域整理工具，另一方是为智能体提供一个覆盖所有领域的可查询抽象。

## 何时使用哪一个 {#when-to-use-which}

在以下情况下构建**自定义 MCP 服务器**：

- 领域较窄且操作较少：对于该特定工作流，十几个命名清晰、schema 严谨的工具在可靠性上可以胜过通用接口。
- 你需要在智能体和 API 之间加入大量业务逻辑：审批、数据富化，以及在智能体看来应当是一个原子工具的多步骤事务。
- 目标 API 是内部的或专有的，并且没有公开规范对其进行描述。

在以下情况下使用 **StackQL 的 MCP 服务器**：

- 领域是云和 SaaS 基础设施，即 AWS、Azure、Google Cloud、GitHub、Databricks、Snowflake、Okta 以及 Provider 注册表中的其余部分，其操作数量使得按操作提供工具不切实际。
- 你希望智能体在运行时发现能力（`list_providers` -> `list_services` -> `list_resources` -> `list_methods` -> `describe_method`），而不是局限于某人预先设想到的工具。
- 你需要受治理的写操作：四种模式的安全契约（`read_only`、`safe`、`delete_safe`、`full_access`），配有基于征询（elicitation）的人工审批和始终开启的审计日志，而不是由你自己编写和维护的安全逻辑。
- 你希望用一个凭据和策略接口面覆盖整个环境，而不是为每个 Provider 定制一个服务器。

这两种方法可以组合使用：团队通常运行 StackQL MCP 来提供通用的基础设施访问，同时为专有的内部系统运行小型的自定义 MCP 服务器。

## token 经济学 {#the-token-economics}

智能体工具的实际约束是上下文。工具定义在任何工作开始之前就会占用智能体的上下文窗口，而且工具选择的准确率会随着工具数量的增长而下降。仅将 AWS 按操作发布为工具，就意味着数千个定义。StackQL 的设计将发布的工具集控制在不到 15 个（发现、描述、验证、执行、注册表管理），并将表达能力转移到 SQL 中，这是当前每个模型都已能流畅编写的语言。智能体每个任务的 token 成本是一段简短的 SQL 字符串，而不是一次工具 schema 协商。这一论点在[为什么 SQL 是云 API 的强大接口](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis)中展开。

## 示例 {#example}

针对 StackQL 的 MCP 服务器的一次智能体会话，在使用 `list_methods` 发现必需参数之后：

```sql
SELECT name, visibility, default_branch
FROM github.repos.repos
WHERE org = 'stackql';
```

同一个工具（`run_select_query`）既处理这条查询，也处理针对任何 Provider 的任何其他读取；变更请求则经由 `run_mutation_query` 路由，由服务器模式决定它们是继续执行、需要审批还是被拒绝。

## 相关概念 {#related-concepts}

- [StackQL MCP 架构](/ai/architecture/stackql-mcp-architecture)：传输方式、模式、工具和审计的详细说明
- [如何将 StackQL 与 AI 智能体配合使用](/ai/how-tos/use-stackql-with-ai-agents)：设置演练
- [如何将 StackQL 与 Claude 配合使用](/ai/how-tos/use-stackql-with-claude)：Claude Desktop 配置
- [什么是智能体基础设施？](/ai/canonical-definitions/what-is-agentic-infrastructure)：更广泛的模式
- [为什么 SQL 是云 API 的强大接口](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis)：设计依据
