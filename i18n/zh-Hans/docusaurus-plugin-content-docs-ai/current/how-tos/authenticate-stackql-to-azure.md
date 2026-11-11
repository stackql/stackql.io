---
title: 如何让 StackQL 通过 Azure 的身份验证
description: StackQL 使用 Microsoft Entra ID 服务主体向 Azure 进行身份验证，服务主体通过 AZURE_TENANT_ID、AZURE_CLIENT_ID 和 AZURE_CLIENT_SECRET 环境变量提供。
keywords: [stackql, azure 身份验证, 服务主体, entra id, 环境变量]
proficiencyLevel: Beginner
faq:
  - question: StackQL 需要哪个 Azure 角色？
    answer: 查询所需的角色，其范围应在可行的前提下尽量收窄。订阅范围内的内置 Reader 角色足以满足清单和审计查询；变更需要 Contributor 角色，或针对目标资源类型的自定义角色。
  - question: 为什么 Azure 查询需要在 WHERE 子句中提供 subscriptionId？
    answer: Azure Resource Manager API 按订阅（通常还按资源组）划定范围，因此它们作为底层 API 调用的必需参数出现，而不是身份验证设置。SHOW METHODS IN azure.compute.virtual_machines 会列出每个方法的必需参数。
  - question: StackQL 能否在 Azure Cloud Shell 中运行？
    answer: 可以。StackQL 可以像任何 Linux 二进制文件一样在 Azure Cloud Shell 中运行；通过本页所述的环境变量提供服务主体凭据即可。
---

# 如何让 StackQL 通过 Azure 的身份验证

StackQL 使用 Microsoft Entra ID（原 Azure AD）服务主体向 Azure 进行身份验证，服务主体通过 3 个环境变量提供：`AZURE_TENANT_ID`、`AZURE_CLIENT_ID` 和 `AZURE_CLIENT_SECRET`。

## 步骤 {#steps}

1. 创建一个服务主体，并为其授予目标订阅上的角色（对于只读查询，`Reader` 角色即可满足需要）：

```bash
az ad sp create-for-rbac --name stackql-sp --role Reader \
  --scopes /subscriptions/<subscription-id>
```

2. 导出该命令返回的凭据：

```bash
export AZURE_TENANT_ID=your-tenant-id
export AZURE_CLIENT_ID=your-client-id
export AZURE_CLIENT_SECRET=your-client-secret
```

3. 拉取 Azure Provider（仅首次使用时需要）：

```sql
REGISTRY PULL azure;
```

4. 通过查询进行验证：

```sql
SELECT name, location
FROM azure.compute.virtual_machines
WHERE subscriptionId = '00000000-1111-2222-3333-444444444444';
```

`subscriptionId` 谓词是 Azure API 的必需路由参数，不属于身份验证的一部分。范围限定为资源组的方法还需要 `resourceGroupName`；运行 `SHOW METHODS IN azure.compute.virtual_machines` 可查看每个访问方法的必需参数（`list_all` 只需要 `subscriptionId`；`list` 还需要 `resourceGroupName`；`get` 需要 `vmName`）。

## 变量的适用范围 {#where-the-variables-apply}

这些变量在 `stackql shell`、`stackql exec`、`stackql srv`（PostgreSQL 线协议服务器）和 `stackql mcp` 中的用法完全相同。对于 Claude Desktop 等 MCP 客户端，将它们放在 `claude_desktop_config.json` 中服务器条目的 `env` 块内。在 CI 中，应从管道的机密存储获取这些变量。

## 相关概念 {#related-concepts}

- [如何让 StackQL 通过 AWS 的身份验证](/ai/how-tos/authenticate-stackql-to-aws)：AWS 的对应做法
- [如何让 StackQL 通过 Google Cloud 的身份验证](/ai/how-tos/authenticate-stackql-to-google-cloud)：GCP 的对应做法
- [如何将 StackQL 与 Claude 配合使用](/ai/how-tos/use-stackql-with-claude)：向 MCP 会话传递 Azure 凭据
- [控制平面与数据平面](/ai/canonical-definitions/control-plane-vs-data-plane)：Resource Manager API 涵盖的内容
- [常见 StackQL 错误](/ai/troubleshooting/common-stackql-errors)：包括身份验证失败
