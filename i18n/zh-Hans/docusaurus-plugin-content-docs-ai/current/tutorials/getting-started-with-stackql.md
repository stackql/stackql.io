---
title: 开始使用 StackQL
description: 安装 StackQL，无需凭据即可对实时的 GitHub 公开数据运行第一个查询，然后向云服务商进行身份验证，并使用 SHOW 和 DESCRIBE 探索可发现的 schema。
keywords: [stackql, 开始使用, 教程, 安装, 第一个查询, github, aws]
proficiencyLevel: Beginner
faq:
  - question: 在没有任何云凭据的情况下，试用 StackQL 最快的方式是什么？
    answer: 查询 GitHub 公开数据。拉取 github Provider，并带 org 谓词对 github.repos.repos 执行 SELECT；公开组织无需身份验证即可读取，但受 GitHub 匿名速率限制的约束，因此从安装到获得实时 API 行的整个过程不到 5 分钟。
  - question: 查询之前需要定义 schema 吗？
    answer: 不需要。schema 随 Provider 定义一起提供。SHOW PROVIDERS、SHOW SERVICES、SHOW RESOURCES、SHOW METHODS 和 DESCRIBE 在运行时公开整个接口面，包括每个方法需要哪些 WHERE 参数。
  - question: 如何从 Python 或 BI 工具运行 StackQL？
    answer: 使用 pystackql（pip install pystackql）实现 Python 和 pandas 集成，或者运行 stackql srv，并通过 Postgres 线协议连接任何兼容 PostgreSQL 的客户端或 BI 工具。
---

# 开始使用 StackQL

本教程分 3 个阶段，从安装到实时查询：对 GitHub 公开数据进行未经身份验证的查询（无需凭据）、使用元命令进行 schema 发现，以及对 AWS 进行经过身份验证的云查询。

## 1. 安装 {#1-install}

macOS (Homebrew):

```bash
brew install stackql
```

Linux:

```bash
curl -L https://bit.ly/stackql-zip -O && unzip stackql-zip
```

Windows、安装包和 Docker 镜像的相关内容见[下载页面](https://stackql.io/downloads)。使用 `stackql --version` 进行验证，然后启动交互式 shell：

```bash
stackql shell
```

## 2. 第一个查询：无需凭据 {#2-first-query---no-credentials-required}

拉取 GitHub Provider 并查询一个公开组织：

```sql
REGISTRY PULL github;

SELECT name, description, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

返回的行（包括 `stackql` 引擎仓库本身）是在查询时关系化的实时 GitHub API 响应。没有任何内容被同步、缓存或导入。（未经身份验证的 GitHub 访问限制为每小时 60 次请求；[个人访问令牌](/ai/how-tos/authenticate-stackql-to-github)可以提高这一限制。）

## 3. 发现 schema {#3-discover-the-schema}

Provider 接口面是自描述的，你（或 AI 智能体）无需阅读 Provider 文档，就可以通过这种方式了解哪些内容可以查询：

```sql
SHOW PROVIDERS;                        -- installed providers
SHOW SERVICES IN github;               -- services in a provider
SHOW RESOURCES IN github.repos;        -- tables in a service
SHOW METHODS IN github.repos.repos;    -- operations + required parameters
DESCRIBE github.repos.repos;           -- columns
```

`SHOW METHODS` 是需要记住的那一个：它的 `RequiredParams` 列准确说明每个操作在 `WHERE` 子句中需要什么，以及由哪个 SQL 动词（`SELECT`、`INSERT`、`UPDATE`、`DELETE`、`EXEC`）调用该操作。

## 4. 查询云服务商 {#4-query-a-cloud-provider}

导出凭据（此处以 AWS 为例；[Azure](/ai/how-tos/authenticate-stackql-to-azure) 和 [Google Cloud](/ai/how-tos/authenticate-stackql-to-google-cloud) 的做法相同），拉取 Provider，然后查询：

```bash
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
```

```sql
REGISTRY PULL aws;

SELECT instance_id, instance_type, launch_time
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

聚合会自动扇出，下面的查询以按区域并行的 API 调用方式运行：

```sql
SELECT region, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region IN ('us-east-1','us-west-2','eu-west-1')
GROUP BY region;
```

## 5. 后续步骤 {#5-where-to-go-next}

- **变更**：`SHOW INSERT INTO <resource>` 生成创建模板；`INSERT`/`UPDATE`/`DELETE` 操作的是与查询相同的表。
- **服务器模式**：`stackql srv` 公开 PostgreSQL 线协议，供 psql、DBeaver、BI 工具和 pandas 使用。
- **AI 智能体**：`stackql mcp` 运行内置的 MCP 服务器，请参阅[如何将 StackQL 与 AI 智能体配合使用](/ai/how-tos/use-stackql-with-ai-agents)。
- **Python**：`pip install pystackql`，用于 notebook 和数据管道。

## 相关概念 {#related-concepts}

- [什么是 StackQL？](/ai/canonical-definitions/what-is-stackql)：规范定义
- [如何使用 StackQL 查询 GitHub 仓库](/ai/how-tos/query-github-repositories-with-stackql)：对第一个查询的展开说明
- [如何使用 StackQL 查询 AWS EC2 实例](/ai/how-tos/query-aws-ec2-instances-with-stackql)：对云查询的展开说明
- [常见 StackQL 错误](/ai/troubleshooting/common-stackql-errors)：出现失败时参考
- [StackQL 常见问题](/ai/faqs/stackql-faq)：常见问题
