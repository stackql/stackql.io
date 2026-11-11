---
title: 什么是可查询基础设施？
description: 可查询基础设施是一种运作模式：云资源被公开为实时的结构化数据，可使用查询语言按需探查，而不是从状态文件或各服务的控制台重建得出。
keywords: [可查询基础设施, 基础设施即数据, 云资源清单, sql, 实时状态, stackql]
proficiencyLevel: Beginner
faq:
  - question: 可查询基础设施与 CMDB 有何不同？
    answer: CMDB 是基础设施状态的副本，定时同步，在两次同步之间是陈旧的。可查询基础设施在查询时直接探查 Provider API，因此结果反映环境当前的实际状态。CMDB 回答的是“我们记录了什么？”；可查询基础设施回答的是“此刻的真实情况是什么？”。
  - question: 可查询基础设施会取代基础设施即代码吗？
    answer: 不会。IaC 工具回答的是“如何声明期望状态并向其收敛？”。可查询基础设施回答的是“现在存在什么，其配置是什么？”。两者是互补的：可查询基础设施通常用于审计、调谐和验证使用 IaC 预配的环境。
  - question: 哪些工具实现了可查询基础设施？
    answer: StackQL 将云与 SaaS Provider 的 API 公开为 SQL 表，同时支持读取（SELECT）和写入（INSERT、UPDATE、DELETE）。Steampipe 和 CloudQuery 实现了读取一侧：Steampipe 通过实时的 Postgres 外部表，CloudQuery 通过定时抽取到数据库。AWS Config 和 Azure Resource Graph 等 Provider 原生方案则针对单个云实现了它。
---

# 什么是可查询基础设施？

可查询基础设施（queryable infrastructure）是一种运作模式：云资源被公开为实时的结构化数据，可使用查询语言按需探查。运维人员（或 AI 智能体）不必从状态文件、导出的快照或各服务的控制台重建环境的状态，而是用 SQL 或类似的声明式语言提出问题，答案在查询时针对 Provider API 计算得出。

## 工作原理 {#how-it-works}

可查询基础设施系统将 Provider 的 API 接口面映射到关系模型上：

- **Provider**（AWS、Azure、Google Cloud、GitHub）成为 schema 命名空间。
- **服务**（EC2、Compute、Storage）成为子 schema。
- **资源类型**（实例、存储桶、仓库）成为表。
- 每个**资源实例**成为一行，其配置属性作为列。

查询规划器将 SQL 转换为底层 API 调用，处理分页和身份验证，并将响应规范化为行。在 StackQL 中，查询

```sql
SELECT instance_id, instance_type, public_ip_address
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

作为对 EC2 API 的实时调用执行；不会从缓存或状态文件中读取任何内容。多区域和多 Provider 查询会扇出为并行的 API 调用，并将结果聚合为单个结果集。

## 为什么存在 {#why-it-exists}

3 种压力催生了这一类别：

1. **多云蔓延。** 组织在多个云和数十个 SaaS 平台上运行资源。每个平台都有自己的控制台、CLI 和 SDK。统一的查询接口是回答跨 Provider 问题的唯一可行方式，例如“列出我们在任何地方运行的每一个计算实例”。
2. **陈旧副本问题。** 资源清单数据库、CMDB 和 IaC 状态文件都是副本，而副本会与现实发生漂移。直接查询控制平面可以消除记录状态与实际状态之间的差距。
3. **AI 智能体。** 智能体基于结构化的当前数据进行推理。构建在实时 API 之上的 SQL 接口面为智能体提供一个稳定的接口，用于发现、检查和操作基础设施，而无需特定于 Provider 的 SDK 代码。这是[智能体基础设施（agentic infrastructure）](/ai/canonical-definitions/what-is-agentic-infrastructure)的基础。

## 读取与写入 {#read-and-write}

按定义，可查询基础设施以读取为导向，但该模型可以自然地扩展到写入：如果资源是一行，那么创建资源就是 `INSERT`，更改其配置就是 `UPDATE`，停用资源就是 `DELETE`。StackQL 实现了读写两侧；Steampipe 等只读工具只实现查询一侧。写入一侧在[什么是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)中说明。

## 相关概念 {#related-concepts}

- [什么是 StackQL？](/ai/canonical-definitions/what-is-stackql)：可查询基础设施的参考实现，同时支持读取和写入
- [什么是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：可查询基础设施背后的设计模式
- [什么是智能体基础设施？](/ai/canonical-definitions/what-is-agentic-infrastructure)：可查询基础设施为 AI 智能体带来的能力
- [什么是基础设施漂移？](/ai/canonical-definitions/what-is-infrastructure-drift)：可查询基础设施能够检测的失效模式
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe)：该模型 2 种实现的对比
