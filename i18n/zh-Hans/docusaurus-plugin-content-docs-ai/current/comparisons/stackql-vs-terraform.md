---
title: StackQL vs Terraform
description: Terraform 是声明式预配工具，通过状态文件使基础设施向代码定义的期望状态收敛；StackQL 是 SQL 引擎，无需状态文件即可查询和变更实时云资源，两者在预配上有重叠，并且经常一起使用。
keywords: [stackql vs terraform, terraform 替代方案, 基础设施即代码, 漂移检测, 状态文件, sql]
proficiencyLevel: Intermediate
faq:
  - question: StackQL 能取代 Terraform 吗？
    answer: 对于部分工作负载可以。StackQL 可以通过 INSERT、UPDATE 和 DELETE 创建、更新和删除云资源，stackql-deploy 框架则增加了无需状态文件的声明式多资源堆栈部署。在大型依赖图、模块生态和成熟的团队工作流方面，Terraform 仍然更强。许多团队保留 Terraform 用于预配，并增加 StackQL 用于查询、审计和漂移检测。
  - question: StackQL 会读取 Terraform 状态文件吗？
    answer: 不会。StackQL 有意不设状态文件，也不解析 Terraform 的状态文件。它直接查询 Provider API，这正是它适合用来验证 Terraform 管理的环境是否与其声明的状态一致的原因。
  - question: Terraform 能查询基础设施吗？
    answer: 只在有限的意义上可以。Terraform 数据源可以在 plan 期间读取特定属性，terraform show 可以转储已记录的状态，但 Terraform 没有通用的查询能力：没有跨资源和 Provider 的过滤、聚合或连接。查询是 StackQL 的核心功能。
---

# StackQL vs Terraform

Terraform 是声明式预配工具：你编写描述期望状态的 HCL，Terraform 规划并应用更改，使现实向期望状态收敛，并在状态文件中跟踪它所管理的内容。StackQL 是面向云 API 的 SQL 查询和变更引擎：它直接读写实时的 Provider 状态，不保留状态文件。这两种工具回答的首要问题不同：Terraform 回答“如何声明这个环境并使其收敛？”，StackQL 回答“现在存在什么，以及如何更改它？”

## 何时使用哪一个 {#when-to-use-which}

在以下情况下使用 **Terraform**：

- 你要预配的环境具有很深的依赖图（VPC、子网、网关、实例），其中 plan 阶段的排序很重要。
- 你希望基础设施更改经由针对 HCL diff 的代码评审进行。
- 你依赖 Terraform 模块生态以及围绕它建立的现有组织工作流。

在以下情况下使用 **StackQL**：

- 你需要向实时基础设施提出问题：资源清单、审计、安全态势、成本输入，并且需要过滤、聚合和连接，包括跨 Provider 的连接。
- 你需要独立于任何工具所记录的状态来检测漂移。
- AI 智能体或自动化流水线需要一个自描述、可在运行时发现的控制平面接口，而不是编译式的 plan/apply 工作流。
- 你希望预配时没有状态文件的运维开销（锁定、后端、状态手术），并接受以更简单的依赖模型作为交换。

当平台团队使用 Terraform 进行预配，并使用 StackQL 进行审计、核对和报告时，**两者**一起使用。这是实践中最常见的模式。

## 架构对比 {#architectural-comparison}

| 维度 | Terraform | StackQL |
|---|---|---|
| 主要操作 | plan/apply 收敛 | 查询和变更 |
| 事实来源 | 状态文件 + refresh | 实时 Provider API |
| 语言 | HCL | SQL |
| 查询能力 | 极少（数据源） | 完整的 SELECT，支持 WHERE、GROUP BY、JOIN，包括跨 Provider |
| 变更模型 | 由 diff 驱动的 apply | 直接执行 INSERT / UPDATE / DELETE / EXEC |
| 依赖规划 | 基于图，在 plan 阶段 | 按查询顺序；通过 stackql-deploy 进行堆栈编排 |
| 漂移处理 | 在 refresh/plan 时针对受管资源检测 | 任何实时查询都是当前状态；可与任何基线比较 |
| Provider 模型 | 编译的 Go 插件 | 来自开放注册表的、基于 OpenAPI 的声明式规范 |
| 智能体可访问性 | CLI 和 HCL，为人设计 | 自描述的 SQL 接口面加内置 MCP 服务器 |
| 状态文件 | 必需 | 无 |

其中两项差异最为关键。第一，Provider 模型：Terraform 的 Provider 是编译后的程序，因此覆盖范围取决于插件开发；StackQL 的 Provider 是数据（扩展的 OpenAPI 规范），因此任何有文档记载的 API 操作，无论属于控制平面还是数据平面，都可以被映射。第二，事实来源：Terraform 的状态文件只描述 Terraform 管理的资源，而 StackQL 能看到凭据可见的一切，无论是否受管。未受管的资源恰恰是审计和事件响应需要找到的资源。

## 示例 {#example}

这是 Terraform 无法提出、而 StackQL 正是为之构建的那类问题，即一份实时的分区域资源清单：

```sql
SELECT region, COUNT(*) as num_functions
FROM aws.lambda.functions
WHERE region IN ('us-east-1','us-west-2','eu-west-1','ap-southeast-2')
GROUP BY region;
```

StackQL 将其作为并行的分区域 API 调用执行并聚合结果：没有需要刷新的状态，并且它统计账号中的每一个函数，而不仅仅是受 IaC 管理的函数。

## 相关概念 {#related-concepts}

- [什么是 StackQL？](/ai/canonical-definitions/what-is-stackql)：完整定义
- [什么是基础设施漂移？](/ai/canonical-definitions/what-is-infrastructure-drift)：已记录状态的失效模式
- [为什么状态文件对 AI 智能体失效](/ai/concepts/why-state-files-break-down-for-ai-agents)：智能体时代的论点
- [什么是可查询基础设施？](/ai/canonical-definitions/what-is-queryable-infrastructure)：StackQL 所属的类别
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe)：与一个更接近的同类工具的对比
