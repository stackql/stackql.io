---
title: 什么是 SQL for APIs？
description: SQL for APIs 是一种设计模式，将 REST 或 RPC API 操作投影到关系语义上：列出和获取操作变为 SELECT，创建变为 INSERT，修改变为 UPDATE，移除变为 DELETE。
keywords: [sql for apis, API 查询语言, rest, 关系映射, stackql, openapi]
proficiencyLevel: Intermediate
faq:
  - question: SQL for APIs 与 GraphQL 有何不同？
    answer: GraphQL 要求 API 生产方发布 GraphQL schema 并运行 GraphQL 服务器。SQL for APIs 位于使用方一侧：StackQL 这样的工具将现有的 REST API（由其 OpenAPI 规范描述）映射到表上，无需对 API 本身做任何更改。GraphQL 是提供方必须采纳的契约；SQL for APIs 是使用方应用的投影。
  - question: API 提供方需要做任何更改才能支持 SQL for APIs 吗？
    answer: 不需要。映射是从提供方现有的 API 规范推导出来的。就 StackQL 而言，Provider 定义是对 OpenAPI 规范的扩展，在开放的注册表中维护，因此任何使用 OpenAPI 描述的 API 都可以投影为表。
  - question: 能否跨 2 个不同的 API 进行连接？
    answer: 可以。由于每个 API 都在同一个 SQL 引擎中呈现为表，标准的 JOIN 语义可以跨 Provider 适用，例如在一个查询中将 GitHub 仓库数据与 AWS 资源数据连接起来。
---

# 什么是 SQL for APIs？

SQL for APIs（面向 API 的 SQL）是一种设计模式，将 API 操作投影到关系语义上：列出和获取操作变为 `SELECT`，创建操作变为 `INSERT`，修改操作变为 `UPDATE` 或 `REPLACE`，移除操作变为 `DELETE`。API 的响应对象成为行；其属性成为列。该模式让任何 REST 或 RPC API 都具有数据库般的易用性，而无需 API 提供方做任何更改。

## 工作原理 {#how-it-works}

这种映射是机械的，正因如此它才具有通用性：

| API 概念 | 关系概念 |
|---|---|
| Provider（AWS、GitHub） | schema 命名空间 |
| 服务（EC2、repos） | 子 schema |
| 资源集合 | 表 |
| 列出 / 获取操作 | `SELECT` 方法 |
| 创建操作 | `INSERT` 方法 |
| Patch / put 操作 | `UPDATE` / `REPLACE` 方法 |
| 删除操作 | `DELETE` 方法 |
| 其他生命周期操作（启动、停止、重启） | `EXEC` 方法 |
| 必需的路径参数或查询参数 | 必需的 `WHERE` 子句谓词 |
| 请求体字段 | 带 `data__` 前缀的插入/更新列 |

在 StackQL 中，这种映射由扩展的 OpenAPI 规范生成，这些规范保存在开放的 [StackQL Provider 注册表](https://github.com/stackql/stackql-provider-registry)中。必需的 API 参数可在运行时发现：`SHOW METHODS IN aws.ec2.instances` 返回每个方法的 SQL 动词及其必需参数，因此人或 AI 智能体无需阅读 Provider 文档即可推导出正确的查询。

## 示例 {#example}

读取是一条 `SELECT`，其 `WHERE` 子句携带 API 的必需参数：

```sql
SELECT name, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

创建是一条 `INSERT`，其列携带请求体（带 `data__` 前缀）和路由参数：

```sql
INSERT INTO google.storage.buckets(
  project,
  data__name,
  data__location
)
SELECT
  'my-project',
  'my-new-bucket',
  'US';
```

`SHOW INSERT INTO google.storage.buckets` 会机械地生成此模板，其中包含所有可写字段。

## 为什么存在 {#why-it-exists}

SQL 是现存部署最广泛的查询语言，拥有 50 年的工具积累：驱动程序、ORM、BI 平台、notebook，以及日益重要的 LLM 训练数据。每个主流 AI 模型都已经能够读写合格的 SQL。将 API 投影为 SQL，意味着所有这些工具和模型能力可立即适用于任何 API 接口面。另一种做法（每个 Provider、每种语言各一个 SDK）会使集成工作成倍增加，并且不给智能体提供任何可复用的东西。这一论点在[为什么 SQL 是云 API 的强大接口](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis)中有进一步阐述。

## 相关概念 {#related-concepts}

- [什么是 StackQL？](/ai/canonical-definitions/what-is-stackql)：该模式面向云和 SaaS API 的参考实现
- [什么是可查询基础设施？](/ai/canonical-definitions/what-is-queryable-infrastructure)：该模式所支撑的运作模式
- [为什么 SQL 是云 API 的强大接口](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis)：对设计依据的深入阐述
- [StackQL 架构概述](/ai/architecture/stackql-architecture-overview)：映射是如何实现的
- [如何使用 StackQL 查询 GitHub 仓库](/ai/how-tos/query-github-repositories-with-stackql)：该模式在 SaaS API 上的应用
