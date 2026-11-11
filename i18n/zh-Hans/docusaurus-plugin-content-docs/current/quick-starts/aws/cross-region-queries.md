---
title: 跨区域查询
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
  - cspm
  - aws
  - amazon web services
description: "用一条 StackQL 语句跨多个区域查询和聚合 AWS 资源，使用区域列表和异步执行，无需逐区域循环。"
image: "/img/tutorials/aws/stackql-aws-provider-featured-image.png"
---

# 使用 StackQL 进行异步跨区域查询

在本指南中，我们将演示 StackQL 的一项强大能力：跨 AWS 多个区域查询和聚合资源。这涉及向不同端点发出的大量 API 请求，这些请求以异步方式执行。这一过程对用户而言是无缝且透明的。  

> StackQL 是一款功能强大的开发工具，可使用 SQL 语法查询和部署云基础设施与资源。  

测试环境：<span class="tutorial_tested_on">嵌入式 SQL 后端</span> <span class="tutorial_tested_on">postgres SQL 后端</span> <span class="tutorial_tested_on">macos</span> <span class="tutorial_tested_on">linux</span> <span class="tutorial_tested_on">powershell</span>

## 了解 AWS 区域和资源 {#understanding-aws-regions-and-resources}

AWS 资源托管在全球多个位置。这些位置由区域和可用区组成，旨在让用户可以在不同位置放置实例和存储数据。

## AWS 区域的问题…… {#the-problem-with-aws-regions}

使用 AWS 时，必须了解 AWS Management Console 提供的是按区域划分的资源视图，用户一次只能与某个特定区域内的资源交互并对其进行管理。同样，AWS Command Line Interface（CLI）命令和软件开发工具包（SDK）以区域作为操作的入口，因此必须指定区域才能执行命令或运行程序。  

> 当资源跨越多个区域时，云服务的区域化框架会带来复杂性。此外，包括加密货币挖矿攻击在内的安全入侵事件经常利用多个区域的资源，超出组织通常使用的主要区域。

## 在 StackQL 查询中指定区域 {#specifying-regions-in-stackql-queries}

使用 StackQL，可以通过指定区域来定位不同地理位置的资源。这对于管理分布在全球各地的资源尤其有用。

### 示例：列出多个区域中的 EC2 实例 {#example-listing-ec2-instances-across-multiple-regions}

```sql
SELECT instance_id, region
FROM aws.ec2.instances
WHERE region IN ('us-east-1', 'eu-west-1');
```

此查询返回 `us-east-1`（N. Virginia）和 `eu-west-1`（Ireland）区域中的 EC2 实例列表，展示了 StackQL 如何在一条查询中跨区域获取数据。这种方式同样可用于聚合查询，例如：  

```sql
SELECT region, COUNT(*) as num_functions
FROM aws.lambda.functions
WHERE region IN (
  'us-east-1','us-east-2','us-west-1','us-west-2',
  'ap-south-1','ap-northeast-3','ap-northeast-2',
  'ap-southeast-1','ap-southeast-2','ap-northeast-1',
  'ca-central-1','eu-central-1','eu-west-1',
  'eu-west-2','eu-west-3','eu-north-1','sa-east-1')
GROUP BY region;
```

## 异步查询的威力 {#the-power-of-asynchronous-queries}

StackQL 以异步方式执行这些跨区域查询，向 AWS 并行发出多个 API 请求。这样可以高效地获取数据，无需等待一个区域的请求完成后再处理下一个区域。

## 总结 {#conclusion}

StackQL 中的异步跨区域查询为管理不同地理位置的 AWS 资源提供了一种稳健而高效的方法。StackQL 通过类 SQL 语法执行这些查询，简化了复杂的云管理任务，对于希望优化其 AWS 基础设施的开发者和云管理员而言，是一款非常有价值的工具。
