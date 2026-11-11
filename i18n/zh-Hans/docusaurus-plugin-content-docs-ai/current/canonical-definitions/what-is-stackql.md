---
title: 什么是 StackQL？
description: StackQL 是一种开源的查询语言和运行时，将云与 SaaS Provider 的 API 公开为关系表，可使用标准 SQL 查询。
keywords: [stackql, sql, 云, api, 基础设施, 可查询基础设施, AI 智能体]
proficiencyLevel: Beginner
faq:
  - question: StackQL 是数据库吗？
    answer: 不是。StackQL 是查询运行时，而不是数据库。它不存储云状态，而是向 Provider（AWS、Azure、GCP、GitHub 等）发起实时 API 调用，并将响应呈现为关系行，以便使用 SQL 查询。结果按需计算。
  - question: StackQL 会取代 Terraform 吗？
    answer: 不直接取代。Terraform 的主要职责是由状态文件驱动的声明式预配。StackQL 的主要职责是在没有状态文件的情况下通过 SQL 查询和变更云资源。两者在变更方面有重叠（StackQL 可以对云资源执行 INSERT、UPDATE 和 DELETE），但 Terraform 提供更丰富的依赖规划，StackQL 提供更丰富的查询能力。两者经常一起使用。
  - question: StackQL 支持哪些 Provider？
    answer: StackQL 支持面向 AWS、Azure、Google Cloud、GitHub、Databricks、Snowflake、Confluent、Okta、OpenAI、Cloudflare 等的 Provider。每个 Provider 都是一个独立的包，从 StackQL Provider 注册表拉取。完整列表见 /providers。
  - question: StackQL 与 Steampipe 这类工具有何不同？
    answer: 两者都将云 API 公开为可用 SQL 查询的表。StackQL 还将云变更（INSERT、UPDATE、DELETE）作为一等操作来支持，以统一的方式对待控制平面和数据平面的 API，并公开 MCP 服务器，使 AI 智能体可以直接查询和变更云状态。Steampipe 侧重读取；StackQL 可读可写。
---

# 什么是 StackQL？

StackQL 是一种开源的查询语言和运行时，将云与 SaaS Provider 的 API 公开为关系表，可使用标准 SQL 查询。它把每个云资源（虚拟机、存储桶、IAM 策略、GitHub 仓库、Snowflake 数据仓库）都视为表中的一行，把每个 API 操作都视为 SELECT、INSERT、UPDATE 或 DELETE。

## 工作原理 {#how-it-works}

典型的基础设施工具将云状态读入本地文件（Terraform 的状态文件），生成计划，然后应用该计划。StackQL 完全跳过状态文件。当你运行查询时，StackQL 向相关 Provider 发起实时 API 调用，规范化响应的结构，并将结果以行的形式返回。除非用户要求，否则两次调用之间不会缓存任何内容。

接口之所以是 SQL，是因为 SQL 是一种稳定且广为理解的查询语言，拥有成熟的工具（客户端、驱动程序、BI 集成、AI 智能体工具包），这些工具对 StackQL 全都“直接可用”。StackQL 在服务器模式下实现了 PostgreSQL 线协议，这意味着任何能与 Postgres 通信的工具（psql、DBeaver、Tableau、Power BI、pandas、LangChain SQL 智能体、MCP 服务器）都能与 StackQL 通信。

## 示例 {#example}

列出某个区域中的所有 EC2 实例及其类型和公网 IP 地址：

```sql
SELECT
  instance_id,
  instance_type,
  public_ip_address
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

该查询针对实时的 AWS EC2 API 运行。没有需要刷新的状态文件，没有需要失效的缓存快照，也没有需要学习的特定于 Provider 的 SDK。同样的形式适用于任何 Provider：将 `aws.ec2.instances` 换成 `github.repos.repos` 或 `azure.compute.virtual_machines`，查询模型完全相同。

## 为什么存在 {#why-it-exists}

StackQL 建立在这样一个前提之上：云基础设施应当是可查询的接口面，而不是静态制品。过去 10 年主导基础设施即代码的状态文件模型，是为人工驱动、受变更控制的工作流设计的：编写代码、plan、评审、apply。该模型在 2 种压力下难以为继：

1. **多云现实。** 每个大型组织都在多个 Provider 上运行工作负载。状态文件按 Provider、按工具、按团队分散存在。跨 AWS、Azure、GitHub 和 Snowflake 回答“我们在生产环境中运行着什么？”，是一项手工汇总工作。
2. **AI 智能体。** 智能体需要以实时、结构化的方式访问基础设施状态，才能对其进行推理。状态文件是错误的原语：它在写入的那一刻就已陈旧，并且其格式因工具而异。针对实时 API 的 SQL 接口才是正确的原语：智能体懂 SQL，schema 稳定，数据始终是最新的。

StackQL 的存在是为了让云基础设施像数据库一样可查询，跨越各个 Provider，既面向人类运维人员，也面向 AI 智能体。

## 相关概念 {#related-concepts}

- [什么是可查询基础设施？](/ai/canonical-definitions/what-is-queryable-infrastructure)：StackQL 所属的更广泛类别
- [什么是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：StackQL 实现的设计模式
- [控制平面与数据平面](/ai/canonical-definitions/control-plane-vs-data-plane)：StackQL 如何统一两者
- [StackQL vs Terraform](/ai/comparisons/stackql-vs-terraform)：与主流 IaC 工具的对比
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe)：与最接近的同类工具的对比
- [开始使用 StackQL](/ai/tutorials/getting-started-with-stackql)：从安装到第一个查询
