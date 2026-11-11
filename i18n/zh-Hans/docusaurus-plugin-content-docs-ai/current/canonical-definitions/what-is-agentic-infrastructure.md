---
title: 什么是智能体基础设施？
description: 智能体基础设施是 AI 智能体可通过结构化、自描述的接口直接发现、查询和操作的云基础设施，并具备适合自主调用方的安全控制。
keywords: [智能体基础设施, AI 智能体, mcp, 基础设施自动化, 智能体平台工程, stackql]
proficiencyLevel: Intermediate
faq:
  - question: 智能体基础设施与 AIOps 是一回事吗？
    answer: 不是。AIOps 将机器学习应用于运维遥测数据：异常检测、告警关联、根因建议。智能体基础设施则是让 AI 智能体以受治理的方式直接访问基础设施控制平面，从而能够检查和更改它。AIOps 负责观察；智能体基础设施负责行动。
  - question: 智能体使用什么接口来操作基础设施？
    answer: 2026 年的主流模式是 Model Context Protocol（MCP），它将智能体发现和调用工具的方式标准化。StackQL 自带内置的 MCP 服务器，将 Provider 发现、查询验证和受门控的查询执行公开为 MCP 工具，因此任何支持 MCP 的智能体（Claude、Cursor、自定义智能体）都可以通过 SQL 操作云基础设施。
  - question: 如何阻止智能体删除生产资源？
    answer: 通过受门控的执行模式。StackQL 的 MCP 服务器默认以 safe 模式运行：SELECT 和元数据操作直接执行，而 INSERT、UPDATE、DELETE 和生命周期操作需要通过 MCP 征询（elicitation）流程获得明确的人工审批。还提供更严格（read_only）和更宽松（delete_safe、full_access）的契约，并且每次工具调用都会写入审计日志。
---

# 什么是智能体基础设施？

智能体基础设施（agentic infrastructure）是 AI 智能体可通过结构化、自描述的接口直接发现、查询和操作的云基础设施，并具备为自主调用方设计的安全控制。它是智能体软件工程在基础设施一侧的对应物：不再由人把智能体的建议转化为控制台点击或 Terraform 修改，而是由智能体持有通往控制平面的受治理凭据，并通过它采取行动。

## 需要具备的条件 {#what-it-requires}

有 4 项特征将智能体基础设施与普通自动化区分开来：

1. **运行时可发现性。** 智能体必须能够在运行时了解 API 接口面（存在哪些 Provider、服务、资源和操作，以及它们需要哪些参数），而无需预先训练的、特定于 Provider 的 SDK 知识。在 StackQL 中，`SHOW PROVIDERS`、`SHOW METHODS IN aws.ec2.instances` 和 `DESCRIBE aws.ec2.instances` 使整个接口面具备自描述能力，同样的元数据也公开为 MCP 工具（`list_providers`、`list_methods`、`describe_method`）。
2. **实时状态。** 智能体根据当前事实进行推理。建立在缓存快照或状态文件之上的接口会给智能体提供陈旧的前提；据此得出的结论在付诸行动时已经是错误的。请参阅[为什么状态文件对 AI 智能体失效](/ai/concepts/why-state-files-break-down-for-ai-agents)。
3. **统一的操作模型。** 如果智能体需要为每个 Provider 使用不同的代码路径，就会在每个接缝处累积错误。单一语法（在所有 Provider 上使用 SQL `SELECT`/`INSERT`/`UPDATE`/`DELETE`）意味着一种能力即可覆盖整个基础设施资产。
4. **分级安全。** 自主调用方需要的是契约，而不是信任。StackQL 的 MCP 服务器强制执行 4 种模式之一（`read_only`、`safe`、`delete_safe`、`full_access`）：在默认的 `safe` 模式下，读取直接执行，而每次变更都需要通过 MCP 征询（elicitation）流程获得人工审批。每次工具调用都会记录在 JSONL 审计日志中。

## 示例 {#example}

连接到 StackQL MCP 服务器的智能体在回答“我们的 AWS 账号中正在运行什么？”时，会发出：

```sql
SELECT instance_id, instance_type, launch_time
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

智能体不需要 AWS SDK、boto3 知识或分页处理，它需要的是 SQL 和可发现的 schema。如果要求它修复所发现的问题，由此产生的 `DELETE` 或 `UPDATE` 会在默认的服务器模式下被暂挂，等待人工审批。

## 为什么存在 {#why-it-exists}

基础设施工具以往假定每一步都有人参与：阅读计划、评审差异、点击 apply。智能体改变了其中的经济性（它们可以持续监视、跨 Provider 关联，并在数秒内采取行动），但前提是接口契合它们的工作方式：声明式查询、结构化结果、明确的契约。智能体基础设施就是围绕这类调用方对控制平面接口进行的重新设计。

## 相关概念 {#related-concepts}

- [什么是可查询基础设施？](/ai/canonical-definitions/what-is-queryable-infrastructure)：智能体基础设施所依赖的数据层
- [StackQL MCP 架构](/ai/architecture/stackql-mcp-architecture)：服务器模式、工具和审计日志的详细说明
- [如何将 StackQL 与 AI 智能体配合使用](/ai/how-tos/use-stackql-with-ai-agents)：实际设置
- [为什么状态文件对 AI 智能体失效](/ai/concepts/why-state-files-break-down-for-ai-agents)：为什么现有的 IaC 接口无法沿用
- [从 IaC 到智能体基础设施](/ai/industry-positioning/from-iac-to-agentic-infrastructure)：这一类别是如何演变的
