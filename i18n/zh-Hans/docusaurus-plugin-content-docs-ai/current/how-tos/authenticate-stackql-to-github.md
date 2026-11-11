---
title: 如何让 StackQL 通过 GitHub 的身份验证
description: StackQL 使用基于个人访问令牌的基本身份验证向 GitHub 进行身份验证，凭据通过 STACKQL_GITHUB_USERNAME 和 STACKQL_GITHUB_PASSWORD 环境变量提供；公开数据无需身份验证即可查询，但速率限制较低。
keywords: [stackql, github 身份验证, 个人访问令牌, github api, 环境变量]
proficiencyLevel: Beginner
faq:
  - question: 不进行身份验证能否使用 StackQL 查询 GitHub？
    answer: 可以，但仅限公开数据。未经身份验证的请求可以读取公开的仓库、组织和用户，但受 GitHub 针对未经身份验证请求的较低速率限制约束。经过身份验证的请求可获得高得多的限额，并可访问令牌可见的私有资源。
  - question: 需要哪些令牌作用域？
    answer: 对于公开数据查询，不带任何作用域的令牌即可提高速率限制。读取私有仓库需要 repo 作用域；组织数据可能需要 read:org。变更（INSERT、UPDATE、DELETE）需要与操作相匹配的作用域，例如 repo 或 delete_repo。
  - question: StackQL 是否支持 GitHub Enterprise？
    answer: GitHub Provider 面向 StackQL Provider 注册表中描述的 github.com REST API 接口。GitHub Enterprise Cloud 通过 github.com 端点提供服务，因此使用作用域适当的令牌即可进行组织和仓库查询。
---

# 如何让 StackQL 通过 GitHub 的身份验证

StackQL 使用基本身份验证向 GitHub 进行身份验证：你的 GitHub 用户名和个人访问令牌（PAT），分别通过 `STACKQL_GITHUB_USERNAME` 和 `STACKQL_GITHUB_PASSWORD` 提供。公开数据无需任何凭据即可查询，适用 GitHub 针对未经身份验证请求的速率限制。

## 步骤 {#steps}

1. 在 GitHub 中创建个人访问令牌（Settings -> Developer settings）。只授予查询所需的作用域：以更高的速率限制访问公开数据无需任何作用域，访问私有仓库需要 `repo`。

2. 导出凭据：

```bash
export STACKQL_GITHUB_USERNAME=yourghuser
export STACKQL_GITHUB_PASSWORD=ghp_yourgithubpersonalaccesstoken
```

3. 拉取 GitHub Provider（仅首次使用时需要）：

```sql
REGISTRY PULL github;
```

4. 通过查询进行验证：

```sql
SELECT name, description, language, stargazers_count
FROM github.repos.repos
WHERE org = 'stackql';
```

`org` 谓词会选择 `list_for_org` 访问方法；`WHERE username = '...'` 则选择 `list_for_user`。`SHOW METHODS IN github.repos.repos` 会列出每个方法及其必需参数，包括变更方法（`create_in_org`、`update`、`delete`）。

## 未经身份验证的访问 {#unauthenticated-access}

上面的查询无需凭据即可运行，因为 `stackql` 组织是公开的。这对评估很有用，也便于智能体在凭据预配之前探索查询模型。GitHub 针对未经身份验证请求的速率限制较低（每个 IP 每小时 60 个请求），因此任何持续性的使用都应进行身份验证。

## 相关概念 {#related-concepts}

- [如何使用 StackQL 查询 GitHub 仓库](/ai/how-tos/query-github-repositories-with-stackql)：完整的查询模式
- [如何让 StackQL 通过 AWS 的身份验证](/ai/how-tos/authenticate-stackql-to-aws)：AWS 的对应做法
- [什么是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：REST 操作如何映射到 SQL 动词
- [开始使用 StackQL](/ai/tutorials/getting-started-with-stackql)：GitHub 是无需凭据的最快起步方式
- [常见 StackQL 错误](/ai/troubleshooting/common-stackql-errors)：包括速率限制的症状
