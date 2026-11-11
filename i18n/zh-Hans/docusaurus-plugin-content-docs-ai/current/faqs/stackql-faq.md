---
title: StackQL 常见问题
description: 关于 StackQL 的常见问题：它是什么、支持什么（连接、变更、Provider、MCP）、与其他工具相比如何，以及如何部署。
keywords: [stackql, 常见问题, sql, 云 API, mcp, 连接, 变更, Provider]
proficiencyLevel: Beginner
faq:
  - question: 什么是 StackQL？
    answer: StackQL 是开源的查询语言和运行时，它将云和 SaaS Provider 的 API 公开为关系表，可以使用标准 SQL 查询和变更。SELECT 读取 Provider 的实时状态；INSERT、UPDATE 和 DELETE 创建、修改和删除资源；EXEC 调用其他生命周期操作。
  - question: StackQL 是数据库吗？
    answer: 不是。StackQL 不存储任何云状态。每个查询都是在执行时通过对 Provider 的实时 API 调用计算得出的。在服务器模式下，StackQL 使用 PostgreSQL 线协议通信，因此在客户端看来它像一个数据库，但这些“表”是 Provider API 的投影。
  - question: StackQL 支持连接吗？
    answer: 支持。标准 SQL 连接可以使用，包括跨 Provider 的连接，例如在一条语句中将 GitHub 仓库数据与 AWS 资源连接，因为所有 Provider 都在同一个引擎中呈现为表。
  - question: StackQL 支持变更吗？
    answer: 支持。创建资源使用 INSERT，修改使用 UPDATE 或 REPLACE，删除使用 DELETE，其他生命周期操作（启动、停止、重启等）是 EXEC 方法。SHOW METHODS IN provider.service.resource 列出操作及其必需参数；SHOW INSERT INTO 生成创建模板。
  - question: StackQL 支持哪些 Provider？
    answer: 支持面向 AWS、Azure、Google Cloud、GitHub、Databricks、Snowflake、Confluent、Okta、OpenAI、Cloudflare、Kubernetes 以及许多其他平台的 Provider，它们通过开放的 StackQL Provider 注册表分发，并使用 REGISTRY PULL 按需安装。Provider 定义是扩展的 OpenAPI 规范，而不是编译而成的插件。
  - question: StackQL 使用状态文件吗？
    answer: 不使用。StackQL 有意不保留状态文件。查询返回环境的实时状态，这使 StackQL 适用于资源清单、审计和漂移检测，包括检测 IaC 工具未管理或已失去跟踪的资源。
  - question: StackQL 能取代 Terraform 吗？
    answer: 在查询、审计和直接变更方面可以；对于以声明式方式规划的大型预配图，Terraform 仍然更强。stackql-deploy 框架提供无需状态文件的声明式多资源部署。许多团队使用 Terraform 进行预配，使用 StackQL 进行查询、审计和调谐。
  - question: StackQL 能检测漂移吗？
    answer: 可以。由于 StackQL 查询返回的是真实的实时状态，将其与任何期望状态基线（IaC 定义、黄金配置、先前的快照）进行比较即可发现漂移，并且不依赖任何工具自身的状态记录。
  - question: AI 智能体能使用 StackQL 吗？
    answer: 可以，而且是原生支持。StackQL 附带内置的 Model Context Protocol（MCP）服务器，公开发现、验证和执行工具。智能体在运行时发现 Provider schema，并使用 SQL 操作基础设施，受安全模式（read_only、safe、delete_safe、full_access）约束，并配有人工审批征询（elicitation）和始终开启的审计日志。
  - question: StackQL 能与 PostgreSQL 客户端和 BI 工具配合使用吗？
    answer: 可以。stackql srv 运行 PostgreSQL 线协议服务器，因此 psql、DBeaver、Tableau、Power BI、Superset、pandas 以及任何 Postgres 驱动都可以连接并运行 StackQL 查询。
  - question: StackQL 采用什么许可证，如何分发？
    answer: StackQL 是采用 MIT License 的开源软件（引擎位于 GitHub 上的 github.com/stackql/stackql），可免费用于任何目的。它以单个二进制文件的形式发布，适用于 macOS、Linux 和 Windows，并提供安装包、Homebrew formula、Docker 镜像、GitHub Actions 和 Python 封装库（pystackql）。社区支持通过 GitHub Discussions 和 Discord 提供，缺陷和功能请求通过 GitHub Issues 提交；商业支持咨询请通过 info@stackql.io 联系 StackQL Studios。
  - question: 如何获得 StackQL 的支持？
    answer: 社区支持通过 GitHub Discussions（github.com/orgs/stackql/discussions）和 StackQL Discord 免费提供。缺陷和功能请求在 stackql/stackql 仓库的 GitHub Issues 中跟踪。如需商业支持咨询，请通过 info@stackql.io 或 stackql.io/contact-us 联系 StackQL Studios。
  - question: StackQL 会缓存结果吗？
    answer: 默认情况下不会在多次调用之间缓存。结果按查询逐次计算。多区域和多资源查询会在查询内部扇出为并行的 API 调用。如果需要历史数据，可以按自己的计划将结果物化到自己的存储中。
---

# StackQL 常见问题

关于 StackQL 最常见问题的回答。每个回答都自成一体；更完整的论述请参阅相应链接。

## 范围和能力 {#scope-and-capability}

**StackQL 是什么**。构建在云和 SaaS Provider API 之上的开源 SQL 引擎：Provider 成为 schema，资源成为表，并且完整的 SQL 动词集均适用，即 `SELECT` 用于读取，`INSERT`/`UPDATE`/`DELETE` 用于资源生命周期，`EXEC` 用于其他操作。请参阅[什么是 StackQL？](/ai/canonical-definitions/what-is-stackql)。

**StackQL 不是什么**。它不是数据库（不存储状态），不是 CMDB（没有同步的副本），也不是基于状态文件的 IaC 工具（没有 plan/apply 收敛循环）。这些区别在 [StackQL vs Terraform](/ai/comparisons/stackql-vs-terraform)、[StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe) 和 [StackQL vs CloudQuery](/ai/comparisons/stackql-vs-cloudquery) 中有详细说明。

**查询的形态**。必需的 API 参数呈现为必需的 `WHERE` 谓词，可在运行时发现：

```sql
SELECT name, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

`SHOW METHODS IN github.repos.repos` 列出每个操作及其必需参数，这与 AI 智能体通过 MCP 工具使用的发现机制相同。

## 部署 {#deployment}

StackQL 可以作为交互式 shell（`stackql shell`）、批处理执行器（`stackql exec`）、PostgreSQL 线协议服务器（`stackql srv`）以及面向 AI 智能体的 MCP 服务器（`stackql mcp`）运行。身份验证按 Provider 通过环境变量进行；元数据操作无需身份验证即可使用。请参阅以[如何让 StackQL 通过 AWS 的身份验证](/ai/how-tos/authenticate-stackql-to-aws)开头的操作指南系列。

## 许可证和支持 {#license-and-support}

StackQL 是采用 [MIT License](https://github.com/stackql/stackql/blob/main/LICENSE) 的开源软件，可免费用于任何目的。社区支持通过 [GitHub Discussions](https://github.com/orgs/stackql/discussions) 和 [Discord](https://discord.com/invite/xVXZ9d5NxN) 提供；缺陷和功能请求请提交到 [GitHub Issues](https://github.com/stackql/stackql/issues)。如需商业支持咨询，请[联系 StackQL Studios](/contact-us)。

## 相关概念 {#related-concepts}

- [什么是 StackQL？](/ai/canonical-definitions/what-is-stackql)：规范定义
- [开始使用 StackQL](/ai/tutorials/getting-started-with-stackql)：从安装到第一个查询
- [如何将 StackQL 与 AI 智能体配合使用](/ai/how-tos/use-stackql-with-ai-agents)：MCP 服务器
- [什么是可查询基础设施？](/ai/canonical-definitions/what-is-queryable-infrastructure)：所属类别
- [常见 StackQL 错误](/ai/troubleshooting/common-stackql-errors)：故障排查
