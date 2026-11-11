---
title: 面向云基础设施、SaaS API 和 AI 智能体的 SQL
sidebar_label: 欢迎使用 StackQL
hide_title: true
hide_last_update: true
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
description: StackQL 是一个开源引擎，让人和 AI 智能体使用 SQL 查询、预配和运维云与 SaaS 服务，无需状态文件，也无需各云专用的 SDK。
image: "/img/stackql-featured-image.png"
slug: /
# SoftwareApplication node on the homepage, from the site-wide defaults in
# themeConfig.structuredData.softwareApplication (name, category, OS,
# license, download URL, free offer)
softwareApplication: true
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import DocsHero from '@site/src/components/DocsHero/DocsHero';
import { FaRobot, FaCode, FaSatelliteDish } from 'react-icons/fa';

<DocsHero
  lightSrc="/img/stackql-logo-bold-light-mode.png"
  darkSrc="/img/stackql-logo-bold-dark-mode.png"
  alt="StackQL"
  title={
  <>
  智能体时代的<br/>
  通用接口
  </>
  }
  byline={
    <>
    配置即数据。基础设施即上下文。
    </>
    }
/>

---

<div className="row margin-top--lg margin-bottom--lg">
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaRobot size={22} className="homeCardIcon" />智能体式平台工程</h3>
      </div>
      <div className="card__body">
        一个通用界面，让智能体和人以机器速度并肩编写变更。多个写入方、实时状态和受策略约束的执行都是一等关注点，这是自主系统安全运行所需的基础。
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaCode size={22} className="homeCardIcon" />面向智能体时代的 IaC</h3>
      </div>
      <div className="card__body">
        代码仍然是意图。现实是数据。StackQL 为智能体和运维人员提供一个声明式接口，用于跨 Provider 查询实时状态、以幂等方式断言期望状态，并通过同一套语法贯通控制平面和数据平面，无需状态文件，也无需各云专用的 SDK。
      </div>
    </div>
  </div>
  <div className="col col--4 margin-bottom--md">
    <div className="card" style={{height: '100%'}}>
      <div className="card__header">
        <h3><FaSatelliteDish size={22} className="homeCardIcon" />自主可观测性</h3>
      </div>
      <div className="card__body">
        把云 API 当作你的数据层，而不是你的部署目标。每次读取都是实时的，每次变更都是一个事件，漂移成为智能体可以推理的信号，而不是需要人去追查的告警。
      </div>
    </div>
  </div>
</div>

<Tabs
  defaultValue="iql"
  values={[
    { label: '查询', value: 'iql', },
    { label: '结果', value: 'data', },
  ]
}>
<TabItem value="iql">

```sql
SELECT state, COUNT(*) as num_instances 
FROM aws.ec2.instances 
WHERE region = 'us-west-1' 
GROUP BY state;
```

</TabItem>
<TabItem value="data">

```
|---------------------------------|
|      state      | num_instances |
|---------------------------------|
|     running     |     342       |
|---------------------------------|
|     pending     |       4       |
|---------------------------------|
|     stopped     |      38       |
|---------------------------------|
```

</TabItem>
</Tabs>

使用你的云服务商部署资源，就像编写一条 [`INSERT`](/language-spec/insert) 语句一样简单……

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

使用 StackQL，你可以按自己的方式开发：声明式或过程式。语法易学，也没有需要管理的状态文件，因此你可以快速开始使用，并将 StackQL 与其他基础设施即代码工具互换使用，无论它们是否云原生。StackQL 为 AI 智能体提供了与云基础设施交互的通用接口。StackQL 的用途包括：

- **AI 智能体集成**：让智能体能够预配和查询云资源
- **控制平面通信**：面向智能体工作流的程序化基础设施预配
- **数据平面可观测性**：面向 AI 驱动运维的实时监控与分析
- 云基础设施部署（使用 SQL）
- 云资产清单与报告（使用 SQL）
- 云合规与控制证明（使用 SQL）
- 配置漂移检测（使用 SQL）
- 以及更多用途，只受你的想象力限制！

## StackQL 适用人群 {#who-stackql-is-for}

StackQL 面向运维云环境的平台工程和 SRE 团队，需要实时资源清单、合规和成本答案的安全和 FinOps 团队，希望将云和 SaaS API 用作可查询数据源的数据工程师，以及正在构建必须安全地查询和变更云与 SaaS 资源的 AI 智能体的开发者。它在工作所在的任何地方运行：在 macOS、Linux 和 Windows 上作为单个二进制文件运行，在 Docker 中运行，在 CI 中通过 GitHub Actions 运行，在 AWS、Azure 和 Google Cloud 的 shell 中运行，嵌入到应用程序中，作为面向 BI 工具和 notebook 的 PostgreSQL 兼容服务器运行，以及作为面向 AI 智能体的 MCP 服务器运行。

## 许可证与支持 {#license-and-support}

StackQL 基于 [MIT License](https://github.com/stackql/stackql/blob/main/LICENSE) 开源，可免费用于任何目的。社区支持通过 [GitHub Discussions](https://github.com/orgs/stackql/discussions) 和 [Discord](https://discord.com/invite/xVXZ9d5NxN) 提供；缺陷报告和功能请求请提交到 [GitHub Issues](https://github.com/stackql/stackql/issues)。如需咨询商业支持，请[联系 StackQL Studios](/contact-us)。

## 开始使用 {#get-started}

- [安装](/installing-stackql)：在 macOS、Linux 或 Windows 上安装 StackQL 二进制文件，或通过 Docker、包管理器或云 shell 运行
- [AI 智能体](/ai-agents)：通过 MCP 连接 AI 智能体、嵌入服务器，并浏览查询和参考内容
- [开始使用](/getting-started)：资源层次结构、你的第一批查询、使用 Provider、输出模式、变量和模板化
- [可用的 Provider](/providers)：可查询和管理的云与 SaaS Provider，每个都有各自的参考站点
- [博客](/blog)：产品公告、Provider 公告和教程
- [命令行用法](/command-line-usage)：`exec`、`shell`、`srv`、`mcp`、`registry` 和全局标志
- [MCP 工具](/mcp)：为 AI 智能体提供相同 SQL 接口的 Model Context Protocol 服务器和工具
- [语言规范](/language-spec/select)：`SELECT`、`INSERT`、`UPDATE`、`DELETE`、`SHOW`、`DESCRIBE`、函数和数据类型
