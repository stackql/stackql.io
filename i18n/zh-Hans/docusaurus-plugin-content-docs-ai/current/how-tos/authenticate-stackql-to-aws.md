---
title: 如何让 StackQL 通过 AWS 的身份验证
description: StackQL 使用标准的 AWS_ACCESS_KEY_ID 和 AWS_SECRET_ACCESS_KEY 环境变量向 AWS 进行身份验证，对于所担任的角色和 AWS CloudShell 还支持会话令牌。
keywords: [stackql, aws 身份验证, aws 访问密钥, 环境变量, cloudshell, iam]
proficiencyLevel: Beginner
faq:
  - question: StackQL 访问 AWS 需要哪些 IAM 权限？
    answer: 只需要你所运行的操作对应的权限。对于只读的清单和审计查询，AWS 托管的 ReadOnlyAccess 策略（或范围更窄的自定义策略）即可满足需要。INSERT、UPDATE、DELETE 和 EXEC 操作需要目标服务上相应的写入权限。
  - question: StackQL 是否支持临时 AWS 凭据？
    answer: 支持。由 sts assume-role 签发的凭据，或在 AWS CloudShell 中自动提供的凭据，只要在启动 StackQL 之前设置标准环境变量（包括会话令牌）即可使用。
  - question: 能否在同一个会话中向 AWS 和其他 Provider 进行身份验证？
    answer: 可以。每个 Provider 读取各自的环境变量，因此同时导出 AWS、GitHub 和 Azure 的凭据后，即可在单个 shell 或服务器会话中进行跨 Provider 查询和连接。
---

# 如何让 StackQL 通过 AWS 的身份验证

StackQL 使用标准的 AWS 凭据环境变量向 AWS 进行身份验证。在启动 StackQL shell、`exec` 命令、服务器或 MCP 服务器之前设置这些变量；无需配置文件。

## 步骤 {#steps}

1. 创建或确定一个 IAM 主体（用户或角色），使其具有查询所需的权限。对于只读查询，`ReadOnlyAccess` 托管策略或范围更窄的同等策略即可满足需要。

2. 将凭据导出为环境变量：

```bash
export AWS_ACCESS_KEY_ID=YOURACCESSKEYID
export AWS_SECRET_ACCESS_KEY=YOURSECRETACCESSKEY
```

对于临时凭据（通过 `sts assume-role` 担任的角色，或会自动设置这些变量的 AWS CloudShell 会话），还需导出会话令牌：

```bash
export AWS_SESSION_TOKEN=YOURSESSIONTOKEN
```

3. 拉取 AWS Provider（仅首次使用时需要）：

```sql
REGISTRY PULL aws;
```

4. 通过查询进行验证：

```sql
SELECT instance_id, instance_type
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

成功返回结果集即表明身份验证成功。请注意，AWS 查询要求在 `WHERE` 子句中提供 `region` 谓词，这是路由参数，而不是凭据设置。

## 变量的适用范围 {#where-the-variables-apply}

这些环境变量在 StackQL 的每种执行模式下都同样适用：

- **交互式 shell**：`stackql shell`
- **批处理**：`stackql exec "..."`
- **服务器模式**：`stackql srv`（PostgreSQL 线协议）
- **MCP 服务器**：`stackql mcp`，或通过 MCP 客户端配置（例如 `claude_desktop_config.json`）的 `env` 块

在 CI 系统中，应从平台的机密存储获取这些变量，而不是将其硬编码。元数据操作（`SHOW`、`DESCRIBE`、`REGISTRY`）无需凭据即可运行；只有数据操作（`SELECT`、`INSERT`、`UPDATE`、`DELETE`、`EXEC`）需要经过身份验证的会话。

## 相关概念 {#related-concepts}

- [如何使用 StackQL 查询 AWS EC2 实例](/ai/how-tos/query-aws-ec2-instances-with-stackql)：完成身份验证后的首批查询
- [如何使用 StackQL 查询 S3 存储桶](/ai/how-tos/query-s3-buckets-with-stackql)：S3 查询模式
- [如何让 StackQL 通过 Azure 的身份验证](/ai/how-tos/authenticate-stackql-to-azure)：Azure 的对应做法
- [如何让 StackQL 通过 Google Cloud 的身份验证](/ai/how-tos/authenticate-stackql-to-google-cloud)：GCP 的对应做法
- [常见 StackQL 错误](/ai/troubleshooting/common-stackql-errors)：包括身份验证失败
