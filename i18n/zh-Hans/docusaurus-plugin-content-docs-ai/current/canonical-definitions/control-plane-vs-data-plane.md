---
title: 控制平面与数据平面
description: 控制平面是管理云资源生命周期和配置的 API 接口面；数据平面是这些资源完成实际工作（例如提供对象或执行查询）所经由的接口面。
keywords: [控制平面, 数据平面, 云 API, 基础设施管理, stackql]
proficiencyLevel: Beginner
faq:
  - question: AWS 控制台是控制平面的一部分吗？
    answer: 控制台是控制平面的客户端。在控制台中执行的每个操作（如启动实例、更改安全组）都是对同一组控制平面 API 的调用，CLI、SDK 以及 StackQL 等工具使用的也是这组 API。
  - question: 为什么大多数基础设施工具只覆盖控制平面？
    answer: 控制平面 API 的形态是统一的（创建、读取、更新、删除资源），因此易于以通用方式建模。数据平面 API 因服务而异，对象存储的操作不同于消息队列的操作，因此大多数 IaC 和资源清单工具止步于控制平面的边界。
  - question: StackQL 能查询数据平面吗？
    answer: 可以，前提是 Provider 通过有文档记载的 API 公开数据平面操作。由于 StackQL Provider 是根据 API 规范生成的，而不是围绕资源生命周期模型手工构建的，数据平面操作可以像控制平面操作一样映射为表和 EXEC 方法。
---

# 控制平面与数据平面

控制平面是管理云资源生命周期和配置的 API 接口面；数据平面是这些资源完成实际工作所经由的接口面。创建 S3 存储桶、调整虚拟机规格或附加 IAM 策略属于控制平面操作。从该存储桶读取对象、由该虚拟机处理请求或在数据仓库中执行 SQL 语句属于数据平面操作。

## 实践中的区别 {#the-distinction-in-practice}

| 方面 | 控制平面 | 数据平面 |
|---|---|---|
| 目的 | 管理资源（创建、配置、删除） | 使用资源（读取、写入、执行） |
| 示例（S3） | `CreateBucket`、`PutBucketPolicy` | `GetObject`、`PutObject` |
| 示例（Databricks） | 创建集群或作业 | 运行查询、执行 notebook |
| 典型调用方 | 运维人员、IaC 工具、治理系统 | 应用程序、最终用户 |
| 调用量 | 低 | 高 |
| 形态 | 统一的 CRUD 生命周期 | 因服务而异的操作 |

这条边界之所以重要，是因为工具历来沿着它分化。基础设施即代码工具（Terraform、CloudFormation、Pulumi）几乎只在控制平面上运作。应用程序 SDK 和驱动程序在数据平面上运作。结果是 2 套互不相交的工具链、2 套凭据模型，并且没有一个统一的地方可以提出横跨两者的问题。

## 为什么统一很重要 {#why-unification-matters}

许多实际的运维问题会跨越这条边界。“有哪些数据仓库，它们上周执行了什么？”是一个控制平面问题与一个数据平面问题的连接。“哪些存储桶是公开的，其中哪些包含对象？”也是如此。止步于控制平面的工具可以枚举资源，但无法检查这些资源正在做什么。

StackQL 以统一的方式对待两个平面：Provider 的 API 规范中记载的任何操作都可以投影到同一个 SQL 模型中，资源对应表，读取对应 `SELECT`，写入对应 `INSERT`/`UPDATE`/`DELETE`，其他操作对应 `EXEC` 方法。单个查询会话在每个 Provider 上使用单一的身份验证上下文，即可从配置贯通到活动。对 AI 智能体而言，这一点更为重要：智能体获得通往两个平面的同一个接口后，无需切换工具链即可进行诊断（“作业失败了”）、检查配置（“集群规格不足”）和修复（“调整其规格”）。请参阅[什么是智能体基础设施？](/ai/canonical-definitions/what-is-agentic-infrastructure)。

## 示例 {#example}

一条控制平面查询，枚举某个 Azure 订阅中的虚拟机：

```sql
SELECT name, location
FROM azure.compute.virtual_machines
WHERE subscriptionId = '00000000-1111-2222-3333-444444444444';
```

针对其中某个资源的生命周期操作，是对同一张表上某个有文档记载的方法（例如 `restart` 或 `power_off`）执行 `EXEC`，这些方法可通过 `SHOW METHODS IN azure.compute.virtual_machines` 发现。

## 相关概念 {#related-concepts}

- [什么是 StackQL？](/ai/canonical-definitions/what-is-stackql)：覆盖两个平面的同一个 SQL 接口面
- [什么是可查询基础设施？](/ai/canonical-definitions/what-is-queryable-infrastructure)：运作模式
- [什么是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：任一平面上的操作如何映射到 SQL 动词
- [StackQL 架构概述](/ai/architecture/stackql-architecture-overview)：Provider 规范如何驱动这一映射
- [StackQL vs Terraform](/ai/comparisons/stackql-vs-terraform)：与一个仅覆盖控制平面的工具的对比
