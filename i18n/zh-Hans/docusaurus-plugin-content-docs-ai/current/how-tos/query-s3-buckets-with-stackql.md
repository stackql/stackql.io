---
title: 如何使用 StackQL 查询 S3 存储桶
description: 使用 aws.s3.buckets 表列出 S3 存储桶及其区域，并从以 Identifier 为键的 awscc.s3.buckets Cloud Control 表获取每个存储桶的配置（加密、版本控制、公共访问阻止）。
keywords: [stackql, aws, s3, 存储桶, sql, 云安全, 公共访问]
proficiencyLevel: Beginner
faq:
  - question: 既然存储桶是全局的，为什么 S3 存储桶查询要使用 region = 'us-east-1'？
    answer: 存储桶名称全局唯一，但 region 仍然是列表方法的必需路由参数；us-east-1 返回账号的存储桶列表。每个存储桶所在的区域是该列表的 bucket_region 列。
  - question: 如何获取某个特定存储桶的配置？
    answer: 查询 Cloud Control 表 awscc.s3.buckets，并将存储桶名称作为 Identifier。该行包含加密、版本控制、公共访问阻止、日志记录和标签等配置。
  - question: StackQL 能否创建或删除 S3 存储桶？
    answer: 可以。aws.s3.buckets 公开了 INSERT（create_bucket）和 DELETE（delete_bucket）方法，Cloud Control 表 awscc.s3.buckets 公开了 INSERT（create_resource）、UPDATE（update_resource）和 DELETE（delete_resource）。SHOW INSERT INTO awscc.s3.buckets 会生成包含可写属性的模板。
---

# 如何使用 StackQL 查询 S3 存储桶

S3 存储桶以 2 个相关的表的形式公开：`aws.s3.buckets` 列出账号中的存储桶，`awscc.s3.buckets` 返回由 `Identifier` 标识的单个存储桶的完整配置。前者基于 `aws` Provider 中的 S3 API，后者基于 `awscc` Provider 中的 AWS Cloud Control API。

## 前提条件 {#prerequisites}

- 已导出 AWS 凭据，请参阅[如何让 StackQL 通过 AWS 的身份验证](/ai/how-tos/authenticate-stackql-to-aws)
- 已安装 2 个 AWS Provider：`REGISTRY PULL aws;` 和 `REGISTRY PULL awscc;`

## 列出存储桶 {#list-buckets}

```sql
SELECT name, bucket_region, creation_date
FROM aws.s3.buckets
WHERE region = 'us-east-1';
```

`region` 谓词是底层 API 调用的必需路由参数；`us-east-1` 返回账号的存储桶。

## 检查单个存储桶 {#inspect-one-bucket}

Cloud Control 表返回指定名称的存储桶的配置属性：

```sql
SELECT
  bucket_name,
  bucket_encryption,
  versioning_configuration,
  public_access_block_configuration
FROM awscc.s3.buckets
WHERE region = 'us-east-1'
AND Identifier = 'my-bucket-name';
```

配置列返回结构化 JSON；`DESCRIBE awscc.s3.buckets` 会列出所有可用的列，包括 `logging_configuration`、`lifecycle_configuration`、`object_lock_configuration`、`tags` 和 `arn`。

## 审计模式：先枚举，再检查 {#audit-pattern-enumerate-then-inspect}

账号范围的配置审计会结合使用这 2 个表：先列出存储桶名称，再针对每个存储桶查询详情表。在脚本化使用场景中（shell 循环，或通过 pystackql 使用 Python），遍历列表结果并收集详情行，这样“是否启用了版本控制？”或“公共访问阻止是否完整？”这类针对单个存储桶的检查，就成为对所收集结果的行谓词。这是 StackQL AWS 教程中记载的 S3 清单模式。

## 创建存储桶 {#creating-buckets}

可写属性的发现方式与可读属性相同：

```sql
SHOW INSERT INTO awscc.s3.buckets;
```

该语句会生成一个 `INSERT` 模板，以 Cloud Control 接受的期望状态文档的形式涵盖资源的可写字段。使用 `/*+ REQUIRED */` 查询提示可将模板限定为仅包含必需字段。

## 相关概念 {#related-concepts}

- [如何使用 StackQL 查询 AWS EC2 实例](/ai/how-tos/query-aws-ec2-instances-with-stackql)：区域级服务模式
- [如何让 StackQL 通过 AWS 的身份验证](/ai/how-tos/authenticate-stackql-to-aws)：凭据设置
- [什么是 SQL for APIs？](/ai/canonical-definitions/what-is-sql-for-apis)：为什么列表和详情是不同的方法
- [什么是可查询基础设施？](/ai/canonical-definitions/what-is-queryable-infrastructure)：审计用例的背景
- [常见 StackQL 错误](/ai/troubleshooting/common-stackql-errors)：对缺少必需参数的说明
