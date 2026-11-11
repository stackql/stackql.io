---
title: 常见 StackQL 错误
description: 最常见的 StackQL 故障的解决方法：Provider 未安装、缺少必需的 WHERE 参数、身份验证失败、GitHub 速率限制、空结果以及 MCP 变更被拒绝。
keywords: [stackql, 错误, 故障排查, 身份验证, 注册表, 必需参数, mcp]
proficiencyLevel: Beginner
faq:
  - question: 为什么我的查询提示找不到表或 Provider？
    answer: 该 Provider 未在本地安装。Provider 定义按需拉取：运行 REGISTRY PULL <provider>（例如 REGISTRY PULL aws），然后重新运行查询。SHOW PROVIDERS 列出当前已安装的内容。
  - question: 为什么我的查询会失败，并要求提供我不知道的参数？
    answer: 云 API 有必需的路由参数（AWS 的 region、Azure 的 subscriptionId、Google 的 project），StackQL 将它们呈现为必需的 WHERE 谓词。运行 SHOW METHODS IN provider.service.resource 查看每个访问方法及其 RequiredParams，然后以等值谓词的形式提供这些参数。
  - question: 为什么已经设置了凭据，我的查询仍然返回授权错误？
    answer: 依次检查 3 点：变量已在启动 StackQL 的同一 shell 或进程环境中导出（服务器和 MCP 进程继承的是其启动环境，而不是你当前的 shell）；变量名称与该 Provider 的约定完全一致；以及主体确实拥有执行该操作的权限，因为 Provider 的 401/403 响应会原样经 StackQL 透传。
---

# 常见 StackQL 错误

大多数 StackQL 故障可归为 5 类：缺少 Provider、缺少必需参数、身份验证问题、Provider 侧的限制，以及受门控的操作。每一类都有快速的诊断方法。

## 找不到 Provider 或表 {#provider-or-table-not-found}

**现象**：查询引用了 `aws.ec2.instances`（或任何资源），但无法解析该 Provider 或表。

**原因**：Provider 定义并未随安装捆绑提供；它们需要在每台机器上从注册表拉取。

**解决方法**：

```sql
SHOW PROVIDERS;          -- what is installed
REGISTRY LIST;           -- what is available
REGISTRY PULL aws;       -- install (repeat per provider)
```

`REGISTRY LIST` 也会列出 Provider 版本；重新拉取会升级到最新发布的规范。

## 缺少必需参数 {#missing-required-parameters}

**现象**：一条语法有效的 `SELECT` 执行失败或不返回任何内容，原因是未提供某个必需参数（常见的有 `region`、`subscriptionId`、`project`、`org` 或父资源标识符）。

**原因**：底层 API 操作有必需的路由参数；StackQL 将它们呈现为必需的 `WHERE` 谓词。

**解决方法**：先发现这些要求，然后以等值（或 `IN`）谓词的形式提供：

```sql
SHOW METHODS IN aws.ec2.instances;
```

`RequiredParams` 列按访问方法准确说明 `WHERE` 子句必须包含的内容，例如对于 `aws.ec2.instances`，`describe` 方法需要 `region`。这一发现步骤是文档中为人和智能体共同规定的做法（MCP 服务器通过 `list_methods` 发布相同的数据）。

## 身份验证失败 {#authentication-failures}

**现象**：元数据命令（`SHOW`、`DESCRIBE`）可以使用，但 `SELECT` 或变更失败，并返回 Provider 的 401/403 错误。

**原因**：元数据由本地的 Provider 规范提供，不需要凭据；数据操作则会调用 Provider。凭据在进程环境中不存在、命名有误或权限不足。

**解决方法**：确认变量已在启动 StackQL 的环境中导出（服务器或 MCP 进程继承其启动环境，设置变量后重启该进程这一步常被遗漏），名称与 Provider 的约定一致（`AWS_ACCESS_KEY_ID`/`AWS_SECRET_ACCESS_KEY`、`AZURE_TENANT_ID`/`AZURE_CLIENT_ID`/`AZURE_CLIENT_SECRET`、`GOOGLE_CREDENTIALS`、`STACKQL_GITHUB_USERNAME`/`STACKQL_GITHUB_PASSWORD`），并且主体拥有该操作所需的权限。[/ai/how-tos](/ai/how-tos) 下按 Provider 划分的操作指南涵盖了每种约定。

## GitHub 速率限制 {#github-rate-limiting}

**现象**：未经身份验证的 GitHub 查询在短时间内可以使用，随后失败。

**原因**：GitHub 对未经身份验证的请求的限制是每个 IP 每小时 60 次请求，而一个 StackQL 查询可能发出多次分页 API 调用。

**解决方法**：使用个人访问令牌进行身份验证（即使是不带任何作用域的令牌也能大幅提高限制），请参阅[如何让 StackQL 通过 GitHub 的身份验证](/ai/how-tos/authenticate-stackql-to-github)。

## 本不应为空的空结果 {#empty-results-that-should-not-be-empty}

**现象**：查询成功，但对于已知存在的资源返回 0 行。

**检查清单**：路由谓词指向正确的范围（正确的区域、项目、订阅或组织）；谓词所选中的访问方法是预期的那一个（`SHOW METHODS` 显示哪些谓词组合选中哪个方法，例如 `github.repos.repos` 上的 `org =` 与 `username =`）；以及凭据能够看到这些资源（云 IAM 可能会静默地过滤列表结果）。

## MCP 变更被拒绝 {#mcp-mutation-refused}

**现象**：智能体通过 MCP 服务器执行的 `INSERT`/`UPDATE`/`DELETE` 被拒绝，拒绝消息中提到征询（elicitation）或服务器模式。

**原因**：这是安全契约在起作用，而不是错误。默认的 `safe` 模式要求变更经过人工审批；未通告 MCP 征询能力的客户端无法给予审批。

**解决方法**：使用支持征询的客户端（Claude Desktop、Cursor、Continue）并在审批请求中批准；或者，仅针对受信任的非交互式自动化，使用 `"mode": "full_access"` 运行服务器。详情参阅 [StackQL MCP 架构](/ai/architecture/stackql-mcp-architecture)。

## 相关概念 {#related-concepts}

- [如何让 StackQL 通过 AWS 的身份验证](/ai/how-tos/authenticate-stackql-to-aws)：以及对应的 Azure、Google Cloud 和 GitHub 指南
- [StackQL MCP 架构](/ai/architecture/stackql-mcp-architecture)：对模式和征询的说明
- [开始使用 StackQL](/ai/tutorials/getting-started-with-stackql)：从干净环境开始的配置过程，可避免上述大多数问题
- [什么是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：必需参数为何存在
- [StackQL 常见问题](/ai/faqs/stackql-faq)：范围更广的问题
