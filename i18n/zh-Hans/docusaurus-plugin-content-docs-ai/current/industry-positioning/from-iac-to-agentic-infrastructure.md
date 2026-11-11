---
title: 从 IaC 到智能体基础设施
description: 基础设施工具经历了 3 个时代：命令式脚本、基于状态文件的声明式 IaC，以及如今的智能体基础设施，其中 AI 智能体通过基于实时状态、自描述且受治理的接口操作控制平面。
keywords: [智能体基础设施, 基础设施即代码, 演进, AI 原生, 平台工程, stackql]
proficiencyLevel: Intermediate
faq:
  - question: 智能体基础设施会让 IaC 过时吗？
    answer: 不会。期望状态的声明式定义仍然有价值，因为它们编码了意图。发生变化的是围绕它们的执行层和观测层：由智能体持续查询实时状态、检测偏差并提出或执行调谐，而不是由人针对状态文件运行 plan/apply 周期。
  - question: 基础设施工具需要具备哪些条件，智能体才能用好它？
    answer: 4 项特征：API 接口面的运行时可发现性、实时状态而非记录下来的状态、跨 Provider 统一的操作语法，以及具备可审计性的分级安全契约。为人工 plan/apply 工作流设计的工具通常至少缺少其中 2 项。
  - question: StackQL 在这一演进中处于什么位置？
    answer: StackQL 实现了智能体时代的接口：将云和 SaaS API 呈现为基于实时状态的自描述 SQL 表，变更使用同一套语法，并通过带有安全模式和审计日志的内置 MCP 服务器公开给智能体。
---

# 从 IaC 到智能体基础设施

基础设施工具经历了 3 个时代，每个时代都由谁（或什么）在操作控制平面来界定。命令式脚本让人坐在键盘前；声明式 IaC 让人处于围绕收敛引擎的评审循环中；智能体基础设施（agentic infrastructure）则把 AI 智能体直接放到控制平面上，对接口的要求也随之改变。

## 第一个时代：命令式自动化 {#era-one-imperative-automation}

shell 脚本、CLI 调用和配置工具编码的是*过程*：先做这个，再做那个。期望状态的模型存在于运维人员的头脑中。这种方式难以扩展：过程会漂移，错误处理成倍增加，并且没有任何机制对照意图检查结果。

## 第二个时代：声明式 IaC {#era-two-declarative-iac}

Terraform、CloudFormation 及同类工具把这一模型反转过来：声明期望状态，让引擎计算过程。这是一次真正的进步（可评审的差异、可重复的环境、模块化复用），并且构建了如今大多数平台团队所运行的工作流。支撑它的关键产物是状态文件：引擎对其所管理内容的记录，用于将声明映射到真实资源并计算计划。

这一模型的假设是按人的工作方式设定的：变更通过代码评审成批到达；状态在有人运行 plan 时刷新；所有重要的内容都在受管集合之内。在这些假设之内它运作良好，这也是它得以延续的原因。在这些假设之外（持续观测、跨 Provider 的问题、无人声明的资源），它能提供的很少：IaC 工具无法提出问题，其记录也只描述它们所管理的内容。请参阅[为什么状态文件对 AI 智能体失效](/ai/concepts/why-state-files-break-down-for-ai-agents)。

## 第三个时代：智能体基础设施 {#era-three-agentic-infrastructure}

AI 智能体改变的是运作模式，而不只是运维人员。智能体持续行动，而不是按发布批次行动；它们针对整个基础设施资产提出即席问题；它们并发运行；并且它们需要可由机器检查的护栏，而不是口口相传的流程。为它们服务的接口具有 4 项特征：

1. **可发现**：智能体在运行时了解 Provider、资源、操作和必需参数（`SHOW METHODS`、MCP 发现工具），而不是预置硬编码的 Provider 知识。
2. **实时**：答案反映的是控制平面当前的状态，而不是按人的节奏刷新的副本。
3. **统一**：所有 Provider 使用同一套语法（SQL：`SELECT`/`INSERT`/`UPDATE`/`DELETE`），因此智能体的能力可以在整个基础设施资产中迁移。
4. **受治理**：对自主调用方可以做什么有明确的契约（从只读到完全访问），在需要时由人工审批，并有完整的审计跟踪。

这就是 StackQL 实现的接口：将 Provider API 呈现为基于实时状态的自描述 SQL 表，变更使用同一套语法，并通过带有分级安全模式和始终开启的审计日志的内置 MCP 服务器公开给智能体。

## IaC 时代延续下来的内容 {#what-persists-from-the-iac-era}

期望状态的定义会延续下来：意图必须存放在某处，而受版本控制的声明仍然是它合适的归宿。智能体时代所取代的是意图与现实之间的机制：针对私有状态记录的周期性 plan/apply 循环，让位于针对实时控制平面的持续查询、比较和受治理的调谐。IaC 定义了目的地；智能体基础设施改变的是如何以及以多高的频率度量并弥合与目的地之间的差距。

## 相关概念 {#related-concepts}

- [什么是智能体基础设施？](/ai/canonical-definitions/what-is-agentic-infrastructure)：规范定义
- [为什么状态文件对 AI 智能体失效](/ai/concepts/why-state-files-break-down-for-ai-agents)：详述第二个时代的局限
- [StackQL vs Terraform](/ai/comparisons/stackql-vs-terraform)：第二个时代与第三个时代工具的对比
- [什么是可查询基础设施？](/ai/canonical-definitions/what-is-queryable-infrastructure)：第三个时代的数据层
- [StackQL MCP 架构](/ai/architecture/stackql-mcp-architecture)：第三个时代的治理层
