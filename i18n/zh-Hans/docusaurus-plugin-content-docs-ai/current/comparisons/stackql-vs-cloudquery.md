---
title: StackQL vs CloudQuery
description: CloudQuery 是定时将云资产数据同步到数据库以供后续分析的 ELT 框架；StackQL 在执行时实时查询 Provider API，并且还能变更资源，核心取舍是历史快照与当前状态。
keywords: [stackql vs cloudquery, 云资产清单, elt, cspm, 云查询, 实时状态]
proficiencyLevel: Intermediate
faq:
  - question: CloudQuery 是查询引擎吗？
    answer: 它本身不是。CloudQuery 是同步（ELT）引擎：它通过源插件提取云资产数据，并将其加载到 PostgreSQL、BigQuery 或 S3 等目标中。查询在此之后进行，在目标数据库中使用该数据库的 SQL。StackQL 本身就是查询引擎，直接针对 Provider API 执行 SQL。
  - question: 哪一个更适合历史分析？
    answer: CloudQuery。它将快照加载到你控制的数据库中，因此你可以保留每一次同步，并分析随时间发生的变化。StackQL 返回当前状态；如果在使用 StackQL 时需要历史数据，可以按自己的计划将查询结果物化到数据库或文件中。
  - question: 哪一个的结果更能反映当前状态？
    answer: StackQL。它的结果在查询时由实时 API 调用计算得出，因此不会比 API 本身更陈旧。CloudQuery 结果的新鲜度取决于最近一次完成的同步。
---

# StackQL vs CloudQuery

CloudQuery 是面向云资产的开源 ELT 框架：源插件从 Provider 提取资源数据，并定时将其加载到目标数据库，你再用该数据库的 SQL 查询这些快照。StackQL 将这一架构反转过来：SQL 在查询时直接针对 Provider API 执行，没有同步步骤，也没有存储的副本，而且同一个引擎不仅能读取资源，还能变更资源。

## 何时使用哪一个 {#when-to-use-which}

在以下情况下使用 **CloudQuery**：

- 你需要**历史数据**：跨多次同步保留的时间点快照，用于趋势分析、取证或合规证据。
- 你希望资产数据与数据仓库中的其他数据放在一起，以便在现有的 BI/分析技术栈（BigQuery、Snowflake、Postgres）中进行连接。
- 查询延迟比新鲜度更重要：查询本地数据库比扇出 API 调用更快。

在以下情况下使用 **StackQL**：

- 你需要**当前状态**：安全检查、漂移检测、运维查询，以及自动化程序或 AI 智能体会立即据以行动的任何决策。
- 你不想为了回答问题而运维一条同步管道和一个目标数据库。
- 你需要在与查询相同的工具和语言中执行写操作，即创建、更新或删除资源。
- 使用方是智能体：StackQL 的 MCP 服务器直接公开发现、验证和受门控的执行；CloudQuery 的目标数据库对 Provider 的 API 接口面一无所知。

在规模化场景下两者是互补的：StackQL 用于实时探查和行动，CloudQuery（或定时物化的 StackQL 结果）用于跨时间的存储。

## 架构对比 {#architectural-comparison}

| 维度 | CloudQuery | StackQL |
|---|---|---|
| 模型 | 提取并加载，查询副本 | 实时查询源头 |
| 新鲜度 | 截至上次同步 | 截至查询执行时 |
| 历史数据 | 原生支持（保留的快照） | 不保留（需手动物化） |
| 所需基础设施 | 同步调度器 + 目标数据库 | 单个二进制文件 |
| 写入云端 | 否 | INSERT / UPDATE / DELETE / EXEC |
| Provider 定义 | Go 源插件 | 声明式的扩展 OpenAPI 规范 |
| 查询方言 | 目标数据库的 SQL | StackQL SQL（服务器模式下使用兼容 Postgres 的线协议） |
| 智能体接口 | 无（查询目标数据库） | 内置 MCP 服务器，带安全模式 |

## 示例 {#example}

用一条查询体现新鲜度的差异：这条 StackQL 语句能反映几秒钟前启动的实例，而基于同步的副本要到下一次同步完成后才会包含它们：

```sql
SELECT instance_id, instance_type, launch_time
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

对于跨时间的需求，等效的 StackQL 模式是定时运行这条查询（通过 cron、CI 或 pystackql），并将结果追加到你自己的存储中。其结果与 ELT 相同，但实时查询路径仍可用于对时间敏感的问题。

## 相关概念 {#related-concepts}

- [什么是可查询基础设施？](/ai/canonical-definitions/what-is-queryable-infrastructure)：将实时查询作为一种运作模式
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe)：另一个实时查询的同类工具
- [什么是基础设施漂移？](/ai/canonical-definitions/what-is-infrastructure-drift)：新鲜度为何重要
- [如何使用 StackQL 查询 AWS EC2 实例](/ai/how-tos/query-aws-ec2-instances-with-stackql)：对上述示例的展开
- [StackQL 架构概述](/ai/architecture/stackql-architecture-overview)：实时执行的工作原理
