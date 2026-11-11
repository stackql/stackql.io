---
title: 为什么 SQL 是云 API 的强大接口
description: SQL 适合云 API，因为它是声明式的、面向集合的、拥有普遍的工具支持，而且在智能体时代起决定作用的是，它是每个 AI 模型都已能流畅编写的查询语言，这使它成为智能体与基础设施之间成本最低的可靠接口。
keywords: [sql, 云 API, 接口设计, AI 智能体, 声明式, 查询语言, stackql]
proficiencyLevel: Intermediate
faq:
  - question: 为什么不使用专门构建的基础设施查询语言来代替 SQL？
    answer: 专门构建的语言是用 50 年积累的工具、驱动程序、BI 集成和从业者知识，去换取有限的领域契合度。它们还放弃了智能体时代最大的优势：AI 模型接受过大量 SQL 训练并能可靠地编写 SQL，而一种全新的 DSL 必须在上下文中教授，每次调用都要付出 token 成本。
  - question: SQL 不是不适合层次化的 JSON API 响应吗？
    answer: 不像过去那样不适合了。现代 SQL 通过 JSON 函数和运算符处理嵌套结构，StackQL 将常用于过滤的属性投影为带类型的列，同时将复杂结构保留为使用方可以展开的 JSON 值列。
  - question: 使用 SQL 是否意味着 StackQL 是数据库？
    answer: 不是。SQL 是接口，不是存储。StackQL 解析 SQL，依据 Provider API 元数据对其进行规划，执行实时 API 调用，并将响应关系化。在服务器模式下，它使用 PostgreSQL 线协议，但不存储任何云状态。
---

# 为什么 SQL 是云 API 的强大接口

SQL 适合云 API 有四个原因：它是声明式的（调用方说明要什么，而不是怎么做），面向集合的（云环境是资源的集合），拥有普遍的工具支持（每个驱动程序、BI 平台和 notebook 都支持它），并且它是每个 AI 模型都已能流畅编写的查询语言，这是智能体时代的决定性原因。选择 SQL 作为控制平面接口，意味着整个工具链和模型生态的集成成本已经支付过了。

## 结构上的契合 {#the-structural-fit}

云资源天然是关系型的。每个 Provider 都将其全部资源组织为带类型的集合（实例、存储桶、仓库），集合中是带属性的对象，对象具有标识，彼此之间存在交叉引用。这就是一个 schema，而且 Provider 已经发布了它，形式是 OpenAPI 规范。对这些集合的操作恰好就是 SQL 所命名的那几种形态：枚举和过滤（`SELECT ... WHERE`）、创建（`INSERT`）、修改（`UPDATE`）、移除（`DELETE`）。StackQL 所应用的映射是机械的，而不是比喻性的，正因如此，它才能根据规范跨数百个服务生成。请参阅[什么是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)。

面向集合这一点比乍看起来更重要。基础设施问题是集合问题：“所有没有此标签的实例”、“按区域分组的存储桶”、“关闭了分支保护的仓库”。命令式 SDK 代码用循环、分页处理和累积来回答这些问题。SQL 形式则是一个表达式：

```sql
SELECT region, COUNT(*) as num_functions
FROM aws.lambda.functions
WHERE region IN ('us-east-1','us-west-2','eu-west-1')
GROUP BY region;
```

StackQL 将其作为并行的分区域 API 调用执行；调用方并没有编写扇出逻辑。

## 智能体时代的论点 {#the-agent-era-argument}

对 AI 智能体而言，接口选择是一个关乎 token 经济学和可靠性的决策：

- **零样本能力。** SQL 是每个模型的训练分布中最密集的能力之一。智能体无需上下文内的教学，就能写出带过滤、聚合和连接的正确查询。定制的 DSL 或各 Provider 专用的 SDK 则必须在提示词中反复解释，代价是 token 和错误。
- **用小而封闭的语法覆盖庞大的接口面。** 云服务商公开了数以万计的操作。作为单独的工具或函数发布时，它们会使工具选择不堪重负。表达为基于可发现 schema 的 SQL 时，语法保持不变，而 `SHOW PROVIDERS`、`SHOW METHODS` 和 `DESCRIBE` 让智能体在运行时了解这一接口面，只在需要时为所需的部分付出成本。
- **可验证性。** SQL 的声明式形式在执行前即可审计。人（或门控策略，例如 StackQL 的 MCP safe 模式）可以阅读语句并确切知道它将触及什么，而对于一段由命令式 SDK 调用组成的脚本，这要困难得多。

## SQL 在智能体之外带来的好处 {#what-sql-buys-beyond-agents}

同一选择还将基础设施与分析技术栈连接起来：在服务器模式下，StackQL 使用 PostgreSQL 线协议，因此 psql、DBeaver、Tableau、Power BI、Superset、pandas 和 Jupyter 可以像访问数据库一样访问实时云状态。资源清单仪表板、安全报告和 FinOps 分析可以复用现有的技能和工具，中间不需要导出器或 ETL 管道。

## 相关概念 {#related-concepts}

- [什么是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：映射本身
- [StackQL vs 自定义 MCP 服务器](/ai/comparisons/stackql-vs-custom-mcp-servers)：token 经济学论点的应用
- [什么是智能体基础设施？](/ai/canonical-definitions/what-is-agentic-infrastructure)：有利于 SQL 的要求
- [StackQL 架构概述](/ai/architecture/stackql-architecture-overview)：SQL 引擎如何针对 API 执行
- [什么是可查询基础设施？](/ai/canonical-definitions/what-is-queryable-infrastructure)：运作模式
