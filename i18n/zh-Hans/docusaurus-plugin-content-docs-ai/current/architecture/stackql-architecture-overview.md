---
title: StackQL 架构概述
description: StackQL 是基于 Go 的 SQL 引擎，具有 3 个入口点（CLI/shell、PostgreSQL 线协议、MCP 服务器）、基于 Yacc 的 SQL 解析器、编排实时 Provider API 调用的查询引擎，以及用于关系型后处理的嵌入式 SQLite（或外部 PostgreSQL）后端。
keywords: [StackQL 架构, 查询引擎, SQL 解析器, provider sdk, sqlite, postgresql, 注册表]
proficiencyLevel: Expert
faq:
  - question: 实际的 SQL 处理发生在哪里？
    answer: 分 2 个阶段进行。查询引擎将能下推的内容（路由参数、受支持的谓词）下推到 Provider API，然后将 API 响应物化到 SQL 后端（默认是嵌入式 SQLite，也可以是外部 PostgreSQL），连接、聚合、排序和表达式都在其中执行。
  - question: StackQL 如何知道怎样调用每个 Provider 的 API？
    answer: 依据 StackQL Registry 中的 Provider 定义，即带版本的扩展 OpenAPI 规范，其中描述了每个 Provider 的服务、资源、方法、参数、身份验证和分页。定义通过 REGISTRY PULL 按需拉取，并由 Provider SDK（omnisdk）解释；引擎中没有编译任何特定于 Provider 的代码。
  - question: StackQL 是单个二进制文件吗？
    answer: 是。引擎、解析器、Provider SDK、嵌入式 SQL 后端、PostgreSQL 线协议服务器和 MCP 服务器包含在同一个 Go 二进制文件中发布，适用于 macOS、Linux 和 Windows。
---

# StackQL 架构概述

StackQL 是单个 Go 二进制文件，实现了一个构建在云和 SaaS Provider API 之上的 SQL 引擎。在架构上它分为 4 层：接受 SQL 的入口点、对 SQL 进行规划的解析器和查询引擎、执行由声明式 Provider 规范描述的实时 API 调用的 Provider SDK，以及对物化结果执行关系操作的 SQL 后端。

## 组件 {#components}

**入口点**。共有 3 种进入方式，全部汇聚到同一个解析器：

- **CLI / shell**（`stackql shell`、`stackql exec`），用于交互式使用和批处理。
- **PostgreSQL 线协议服务器**（`stackql srv`，基于 psql-wire 构建），使任何 Postgres 客户端（psql、DBeaver、BI 工具、pandas）都可以连接。
- **MCP 服务器**（`stackql mcp`），向 AI 智能体公开发现、验证和受门控的执行工具（详见 [StackQL MCP 架构](/ai/architecture/stackql-mcp-architecture)）。

**SQL 解析器**。基于 Yacc 的语法（`stackql-parser`），生成 AST。该方言涵盖标准动词，以及 StackQL 元命令（`SHOW`、`DESCRIBE`、`REGISTRY`）和用于非 CRUD 生命周期操作的 `EXEC` 动词。

**查询引擎**。对 AST 进行规划：决定哪些内容下推到 Provider API（路由参数、受支持的谓词），哪些在本地处理；协调 API 执行，包括在查询跨越多个区域或多次调用时的并行扇出；并聚合结果。

**Provider SDK（omnisdk）**。通用 API 客户端，在 StackQL v1.0 中取代了 any-sdk。它从注册表加载 Provider 规范，处理每个 Provider 的身份验证，将每条语句规划为一张由 Provider 交换组成的图，根据查询参数构建 HTTP 请求，将受支持的谓词和行数限制下推到这些请求中，遍历分页响应而不论采用何种分页方案（游标、偏移量、令牌），并在 JSON 响应到达时将其关系化为行。

**SQL 后端**。物化后的 API 结果默认写入嵌入式 SQLite 数据库（也可以改为配置外部 PostgreSQL），连接（包括跨 Provider 连接）、聚合、排序、子查询和表达式在其中以完整的 SQL 语义执行。

## 决定性的设计决策：Provider 即数据 {#the-defining-design-decision-providers-as-data}

StackQL 没有编译而成的、特定于 Provider 的插件。Provider 是一组带版本的文档，即开放的 [StackQL Provider 注册表](https://github.com/stackql/stackql-provider-registry)中的扩展 OpenAPI 规范，它将 API 操作映射到资源、SQL 动词和参数。`REGISTRY PULL aws` 下载该规范；SDK 在运行时对其进行解释。

由此带来的结果：Provider 的覆盖范围跟随已文档化的 API 接口面，而不取决于插件开发的投入；数据平面操作和生命周期操作可以用与控制平面 CRUD 相同的方式映射；整个接口面在运行时均可内省（`SHOW PROVIDERS`、`SHOW METHODS IN aws.ec2.instances`、`DESCRIBE github.repos.repos`），正是这一特征使 StackQL 对 AI 智能体而言是自描述的。

## 查询执行流程 {#query-execution-flow}

以 `SELECT instance_id, instance_type FROM aws.ec2.instances WHERE region = 'us-east-1';` 为例：

1. 解析器生成 AST；引擎依据 Provider 规范解析 `aws.ec2.instances`，并选择 `describe` 访问方法，由 `region` 满足其必需参数。
2. SDK 进行身份验证（使用环境变量），发出一次或多次 EC2 API 调用，并遍历分页。
3. 响应被展平为行，并物化到 SQL 后端。
4. 后端应用投影并完成其余的关系处理；结果以请求的格式返回给调用方。

`region IN (...)` 谓词在第 2 步会变为按区域并行的调用；第 4 步的连接可以跨任何提供了表的 Provider 进行。

## 相关概念 {#related-concepts}

- [StackQL MCP 架构](/ai/architecture/stackql-mcp-architecture)：深入介绍面向智能体的入口点
- [什么是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：Provider 规范所编码的映射
- [为什么 SQL 是云 API 的强大接口](/ai/concepts/why-sql-is-a-strong-interface-for-cloud-apis)：该方言的设计依据
- [什么是可查询基础设施？](/ai/canonical-definitions/what-is-queryable-infrastructure)：此架构所服务的运作模式
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe)：与一种基于 FDW 的架构的对照
