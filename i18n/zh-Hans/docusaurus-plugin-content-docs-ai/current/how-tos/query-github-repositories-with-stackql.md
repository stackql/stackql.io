---
title: 如何使用 StackQL 查询 GitHub 仓库
description: 将 GitHub 仓库作为 SQL 表 github.repos.repos 查询：按组织或用户列出仓库，按语言、可见性和活跃度过滤，并使用 SQL 更新仓库设置。
keywords: [stackql, github, 仓库, sql, github api, devops, 治理]
proficiencyLevel: Beginner
faq:
  - question: 查询公开的 GitHub 仓库是否需要凭据？
    answer: 不需要。公开的组织和仓库无需身份验证即可查询，但受 GitHub 针对未经身份验证请求的速率限制（每小时 60 个请求）约束。设置 STACKQL_GITHUB_USERNAME 和 STACKQL_GITHUB_PASSWORD（个人访问令牌）可获得更高的限额并访问私有数据。
  - question: StackQL 还可以查询哪些 GitHub 资源？
    answer: github Provider 覆盖文档所述的 REST 接口，例如 github.repos 公开了分支、提交、发布版本、协作者、webhook、部署、环境、标签、团队和流量统计等资源。SHOW RESOURCES IN github.repos 会将它们全部列出。
  - question: StackQL 能否更改仓库设置？
    answer: 可以。github.repos.repos 公开了 UPDATE（需要 owner 和 repo）、INSERT（create_in_org）和 DELETE 方法。请求体字段通过带 data__ 前缀的列设置，每个方法的必需参数由 SHOW METHODS IN github.repos.repos 列出。
---

# 如何使用 StackQL 查询 GitHub 仓库

GitHub 仓库以表 `github.repos.repos` 的形式公开。按组织或按用户列出仓库分别映射到相应的 GitHub REST 操作，由 `WHERE` 子句选择：`org = '...'` 使用组织列表操作，`username = '...'` 使用用户列表操作。

## 前提条件 {#prerequisites}

- 已安装 GitHub Provider：`REGISTRY PULL github;`
- 用于访问私有数据和获得更高速率限制的凭据（可选），请参阅[如何让 StackQL 通过 GitHub 的身份验证](/ai/how-tos/authenticate-stackql-to-github)

## 列出组织的仓库 {#list-an-organizations-repositories}

```sql
SELECT name, description, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

针对公开的 `stackql` 组织运行时，此查询返回的行包括 `stackql` 引擎仓库（Go，撰写本文时有 861 个星标）和 `stackql-provider-registry` 等。这些是实时 API 数据，不涉及快照。

## 按仓库属性过滤 {#filter-on-repository-attributes}

标准 SQL 谓词适用于任何列：

```sql
SELECT name, visibility, default_branch, archived
FROM github.repos.repos
WHERE org = 'my-org'
AND archived = false
AND visibility = 'public';
```

对治理有用的列包括 `private`、`visibility`、`archived`、`fork`、`default_branch`、`delete_branch_on_merge`、`has_wiki`、`topics`、`created_at`、`pushed_at` 和 `security_and_analysis`。`DESCRIBE github.repos.repos` 会列出所有列。

## 仓库列表之外 {#beyond-the-repository-list}

`github.repos` 服务将仓库接口的其余部分公开为同级表：`github.repos.branches`、`github.repos.commits`、`github.repos.releases`、`github.repos.collaborators`、`github.repos.webhooks`、`github.repos.deployments` 等。使用以下语句发现这些表：

```sql
SHOW RESOURCES IN github.repos;
```

## 更新仓库 {#updating-repositories}

更改仓库设置使用 `UPDATE` 语句；该方法需要 `owner` 和 `repo`，请求体字段使用 `data__` 前缀：

```sql
UPDATE github.repos.repos
SET data__delete_branch_on_merge = true
WHERE owner = 'my-org' AND repo = 'my-repo';
```

将其应用于列表查询返回的整个结果集，只需 2 条语句即可实现覆盖全部仓库的治理。这种模式与将 GitHub 数据和云资源连接起来的跨 Provider 查询自然契合。

## 相关概念 {#related-concepts}

- [如何让 StackQL 通过 GitHub 的身份验证](/ai/how-tos/authenticate-stackql-to-github)：令牌和速率限制
- [什么是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：此处使用的 REST 到 SQL 的映射
- [开始使用 StackQL](/ai/tutorials/getting-started-with-stackql)：将 GitHub 作为无需凭据的首个 Provider
- [如何将 StackQL 与 AI 智能体配合使用](/ai/how-tos/use-stackql-with-ai-agents)：智能体通过 MCP 运行这些查询
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe)：只读替代方案的比较
