---
title: 如何使用 StackQL 查询 AWS EC2 实例
description: 将 EC2 实例作为 SQL 表 aws.ec2.instances 查询：按区域过滤，投影实例类型和 IP 地址等属性，并使用 GROUP BY 跨区域聚合。
keywords: [stackql, aws, ec2, 查询实例, sql, 云资源清单, 多区域]
proficiencyLevel: Beginner
faq:
  - question: 为什么查询 EC2 实例时必须提供 region？
    answer: EC2 DescribeInstances API 是区域级 API，因此 region 是底层调用的必需参数。StackQL 将必需的 API 参数呈现为必需的 WHERE 子句谓词。使用 region IN (...) 提供多个区域时，查询会扇出为按区域并行执行的 API 调用，并将结果聚合为一个结果集。
  - question: aws.ec2.instances 有哪些可用的列？
    answer: 列包括 instance_id、instance_type、image_id、launch_time、availability_zone、private_ip_address、public_ip_address、private_dns_name、public_dns_name、subnet_id、vpc_id、key_name，以及 state、security_groups、network_interfaces 和 tag_set 等结构化列。运行 DESCRIBE aws.ec2.instances 可查看完整列表。
  - question: 能否使用 StackQL 停止、启动或终止实例？
    answer: 可以。终止映射到 DELETE，生命周期操作（start、stop、reboot、monitor）是同一资源上的 EXEC 方法。SHOW METHODS IN aws.ec2.instances 会列出每个操作、其 SQL 动词及其必需参数。
---

# 如何使用 StackQL 查询 AWS EC2 实例

EC2 实例以表 `aws.ec2.instances` 的形式公开。EC2 `DescribeInstances` API 返回的任何字段都是可查询的列；由于该 API 是区域级 API，`region` 是必需的 `WHERE` 谓词。

## 前提条件 {#prerequisites}

- 已将 AWS 凭据导出为环境变量，请参阅[如何让 StackQL 通过 AWS 的身份验证](/ai/how-tos/authenticate-stackql-to-aws)
- 已安装 AWS Provider：`REGISTRY PULL aws;`

## 基本查询 {#basic-query}

列出单个区域中的实例及其类型和地址信息：

```sql
SELECT
  instance_id,
  instance_type,
  private_ip_address,
  public_ip_address,
  launch_time
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

## 先发现 schema {#discover-the-schema-first}

2 条元数据命令让资源具备自描述能力，无需查阅 AWS 文档：

```sql
DESCRIBE aws.ec2.instances;
SHOW METHODS IN aws.ec2.instances;
```

`DESCRIBE` 列出可用的列。`SHOW METHODS` 列出各项操作（`describe` SELECT 方法、`terminate` DELETE 方法，以及 `start`、`stop` 和 `reboot` 等 EXEC 生命周期方法）及每项操作的必需参数。这些命令无需凭据即可运行。

## 多区域聚合 {#multi-region-aggregation}

`region IN (...)` 谓词会为每个区域并行运行一次 API 调用并聚合结果，这样区域级服务就可以像全局表一样查询：

```sql
SELECT region, instance_type, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region IN ('us-east-1','us-west-2','eu-west-1','ap-southeast-2')
GROUP BY region, instance_type;
```

这种模式是账号范围的清单和漂移基线的基础：结果是实时的答案，而不是快照。

## 按属性过滤 {#filtering-on-attributes}

标准 SQL 谓词适用于任何列。例如，暴露在互联网上的实例：

```sql
SELECT instance_id, instance_type, public_ip_address
FROM aws.ec2.instances
WHERE region = 'us-east-1'
AND public_ip_address IS NOT NULL;
```

`state` 和 `tag_set` 等结构化列返回 JSON，可以使用所配置 SQL 后端的 JSON 函数将其展开。

## 相关概念 {#related-concepts}

- [如何让 StackQL 通过 AWS 的身份验证](/ai/how-tos/authenticate-stackql-to-aws)：凭据设置
- [如何使用 StackQL 查询 S3 存储桶](/ai/how-tos/query-s3-buckets-with-stackql)：S3 模式，与此略有不同
- [什么是基础设施漂移？](/ai/canonical-definitions/what-is-infrastructure-drift)：将这些查询用作基线
- [什么是可查询基础设施？](/ai/canonical-definitions/what-is-queryable-infrastructure)：表抽象背后的模型
- [开始使用 StackQL](/ai/tutorials/getting-started-with-stackql)：端到端的设置
