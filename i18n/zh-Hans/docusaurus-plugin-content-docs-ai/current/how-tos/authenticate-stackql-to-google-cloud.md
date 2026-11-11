---
title: 如何让 StackQL 通过 Google Cloud 的身份验证
description: StackQL 使用服务账号密钥向 Google Cloud 进行身份验证，密钥通过保存 JSON 密钥内容的 GOOGLE_CREDENTIALS 环境变量提供。
keywords: [stackql, google cloud 身份验证, gcp, 服务账号, google 凭据]
proficiencyLevel: Beginner
faq:
  - question: GOOGLE_CREDENTIALS 接受的是文件路径还是密钥内容？
    answer: 密钥内容。文档中给出的用法是 export GOOGLE_CREDENTIALS=$(cat /path/to/key.json)，它将 JSON 文档本身放入该变量。此外，--auth 标志也可以通过 credentialsfilepath 设置按文件路径引用密钥。
  - question: 服务账号需要哪个 IAM 角色？
    answer: 由查询决定的角色。项目范围内的 roles/viewer 可满足只读的清单和审计查询；INSERT 和 DELETE 等变更需要相应的 editor 角色或特定服务的 admin 角色。
  - question: 为什么 Google 查询需要在 WHERE 子句中提供 project（通常还有 zone）？
    answer: Google Cloud API 按项目划定范围，可用区级服务则按可用区划定范围。它们是 API 调用的必需参数。对于 google.compute.instances，aggregated_list 方法只需要 project，而 list 需要 project 和 zone；运行 SHOW METHODS IN google.compute.instances 可查看这两个方法。
---

# 如何让 StackQL 通过 Google Cloud 的身份验证

StackQL 使用服务账号密钥向 Google Cloud 进行身份验证，密钥以 JSON 内容的形式通过 `GOOGLE_CREDENTIALS` 环境变量提供。

## 步骤 {#steps}

1. 创建一个服务账号，并为其授予目标项目上的角色（对于只读查询，`roles/viewer` 即可满足需要），然后创建并下载 JSON 密钥：

```bash
gcloud iam service-accounts keys create stackql-key.json \
  --iam-account=stackql-sa@my-project.iam.gserviceaccount.com
```

2. 导出密钥内容：

```bash
export GOOGLE_CREDENTIALS=$(cat ./stackql-key.json)
```

另一种方式是使用 `--auth` 标志按路径传入密钥，这适用于服务器和 MCP 部署：

```bash
stackql shell \
  --auth='{"google": {"type": "service_account", "credentialsfilepath": "/path/to/stackql-key.json"}}'
```

3. 拉取 Google Provider（仅首次使用时需要）：

```sql
REGISTRY PULL google;
```

4. 通过查询进行验证：

```sql
SELECT name, status, machineType
FROM google.compute.instances
WHERE project = 'my-project';
```

此查询使用 `aggregated_list` 访问方法，该方法只需要 `project`，并返回所有可用区中的实例。可用区级的 `list` 方法同时需要 `project` 和 `zone`；`SHOW METHODS IN google.compute.instances` 会显示每个方法的必需参数。

## 凭据的适用范围 {#where-the-credentials-apply}

同一个变量（或 `--auth` 对象）在 `stackql shell`、`stackql exec`、`stackql srv` 和 `stackql mcp` 中均适用。对于 Claude Desktop 及类似的 MCP 客户端，在服务器配置的 `env` 块中设置 `GOOGLE_CREDENTIALS`。在 CI 中，应从平台的机密存储获取密钥，切勿将其提交到仓库。

## 相关概念 {#related-concepts}

- [如何让 StackQL 通过 AWS 的身份验证](/ai/how-tos/authenticate-stackql-to-aws)：AWS 的对应做法
- [如何让 StackQL 通过 Azure 的身份验证](/ai/how-tos/authenticate-stackql-to-azure)：Azure 的对应做法
- [如何将 StackQL 与 AI 智能体配合使用](/ai/how-tos/use-stackql-with-ai-agents)：MCP 部署中的凭据
- [什么是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：为什么必需参数会出现在 WHERE 子句中
- [常见 StackQL 错误](/ai/troubleshooting/common-stackql-errors)：包括身份验证失败
