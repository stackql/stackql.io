---
title: StackQL vs Steampipe
description: Steampipe 和 StackQL 都将云 API 公开为可用 SQL 查询的表；Steampipe 只读，基于 PostgreSQL 外部数据包装器构建，StackQL 则增加了写操作（INSERT、UPDATE、DELETE）、生命周期操作和面向 AI 智能体的内置 MCP 服务器。
keywords: [stackql vs steampipe, 云查询, 面向云的 SQL, steampipe 替代方案, 云资源清单, 变更]
proficiencyLevel: Intermediate
faq:
  - question: StackQL 和 Steampipe 的查询兼容吗？
    answer: 部分兼容。两者都使用 SQL 和类似的 provider.table 命名方式，但 schema、表名和列名不同，因为两种工具生成它们的方式不同：Steampipe 根据手工构建的 Go 插件生成，StackQL 根据扩展的 OpenAPI Provider 规范生成。在两者之间迁移时，查询需要调整。
  - question: 哪一个的 Provider 覆盖范围更广？
    answer: 两者都覆盖主要的云（AWS、Azure、Google Cloud）和常见的 SaaS Provider，并且都可扩展。按插件的原始数量计，Steampipe 的插件目录更大。StackQL 在单个 Provider 内的覆盖范围由该 Provider 的 API 规范生成，因此它与完整的、有文档记载的 API 接口面保持同步，包括读取之外的操作。
  - question: Steampipe 能创建或修改云资源吗？
    answer: 不能。Steampipe 在设计上就是只读的：它在外部表上实现 SELECT。创建、更新或删除资源需要使用其他工具。StackQL 将这些操作作为原生 SQL 动词提供支持，作用于查询所用的同一批表。
---

# StackQL vs Steampipe

Steampipe 和 StackQL 实现的是同一个核心思想，即将云和 SaaS API 公开为 SQL 表，但有一个决定性的区别：Steampipe 是只读的，而 StackQL 还能写入。Steampipe 嵌入 PostgreSQL，并通过由 Go 插件提供数据的外部数据包装器映射 API；StackQL 则在声明式的、源自 OpenAPI 的 Provider 规范之上实现了自己的 SQL 引擎，并映射完整的动词集：`SELECT`、`INSERT`、`UPDATE`、`DELETE`，以及用于生命周期操作的 `EXEC`。

## 何时使用哪一个 {#when-to-use-which}

在以下情况下使用 **Steampipe**：

- 你的用例是纯读取：资源清单、合规仪表板、安全态势。
- 你想使用它成熟的合规 mod 生态（CIS、SOC 2 以及类似的预构建基准包）。
- 你已经在基于 PostgreSQL FDW 的工具及其周边的 Turbot 生态上有所投入。

在以下情况下使用 **StackQL**：

- 你需要在一个工具中完成读取和写入：查询出一处错误配置，然后在同一会话中用 `UPDATE` 修复它。
- 你在为 AI 智能体构建：StackQL 自带内置的 MCP 服务器，提供发现工具、查询验证、受门控的变更模式和审计日志。
- 你希望 Provider 覆盖范围由 API 规范驱动，而不是由插件开发驱动，并且包括数据平面和生命周期操作。
- 你想要一个确定性的、依赖少的单个二进制文件，它在服务器模式下还使用 PostgreSQL 线协议。

两者都是开源的，都可以在本地或流水线中运行，对于只读的资源清单查询，两者在能力上确实可以互换；这种情况下的选择取决于对 schema 的偏好和生态。

## 架构对比 {#architectural-comparison}

| 维度 | Steampipe | StackQL |
|---|---|---|
| SQL 动词 | 仅 SELECT | SELECT, INSERT, UPDATE, REPLACE, DELETE, EXEC |
| 引擎 | 嵌入式 PostgreSQL + 外部数据包装器 | 原生 SQL 引擎（嵌入式 SQLite 或 PostgreSQL 后端） |
| Provider 定义 | 编译的 Go 插件 | 开放注册表中的声明式扩展 OpenAPI 规范 |
| 平面覆盖范围 | 控制平面读取 | 控制平面和数据平面，读取和写入 |
| AI 智能体接口 | 通用的 Postgres 连接 | 内置 MCP 服务器，带安全模式和审计日志 |
| 服务器模式 | Postgres 线协议（steampipe service） | Postgres 线协议（stackql srv）加 MCP（stackql mcp） |
| 合规内容 | 丰富的 mod 库 | 基于查询；没有打包的基准库 |

Provider 定义上的差异会随时间累积：只有当有人为其编写插件代码时，Steampipe 的表才存在，而只要 API 操作在 Provider 规范中有描述，StackQL 的表就存在。写入能力上的差异则是类别性的：闭环工作流，即检测、决策、修复，可以在一个 StackQL 会话中完成，而 Steampipe 要把修复交给另一个工具。

## 示例 {#example}

两种工具中的意图相同，但在 StackQL 中，后续的修复仍使用同一种语言。查询 EC2 实例：

```sql
SELECT instance_id, instance_type, launch_time
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

随后，StackQL 中的修复就是针对同一张表的一条 `DELETE` 或 `EXEC`（例如终止或停止实例），受凭据权限的约束，在作为 MCP 服务器运行时还受所配置的安全模式约束。

## 相关概念 {#related-concepts}

- [什么是可查询基础设施？](/ai/canonical-definitions/what-is-queryable-infrastructure)：两种工具共同实现的类别
- [什么是 StackQL？](/ai/canonical-definitions/what-is-stackql)：完整定义
- [StackQL vs CloudQuery](/ai/comparisons/stackql-vs-cloudquery)：基于 ELT 的替代方案
- [StackQL MCP 架构](/ai/architecture/stackql-mcp-architecture)：智能体接口详解
- [控制平面与数据平面](/ai/canonical-definitions/control-plane-vs-data-plane)：覆盖边界的说明
