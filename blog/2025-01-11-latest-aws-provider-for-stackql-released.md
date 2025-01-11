---
slug: latest-aws-provider-for-stackql-released
title: New AWS Provider Available (Jan 2025)
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-aws-provider-featured-image.png"
description: Query and interact with AWS resources using SQL.
keywords: [stackql, aws, amazon web services, iac, analytics]
tags: [stackql, aws, amazon web services, iac, analytics]
---

:::info

To get started with the `aws` provider for `stackql`, pull the provider from the registry as follows:  

```
registry pull aws;
```

for more detailed provider documentation, see [here](https://aws.stackql.io/providers/aws/).

:::

Happy New Year 🎉.  The latest AWS provider for [__StackQL__](https://github.com/stackql/stackql) is now available.  The StackQL AWS Provider by the numbers:

- __230 services__
- __3174 resources__
- __3917 methods__

with additional new support for the following services:

- `amazonmq` - Managed message broker service for Apache ActiveMQ and RabbitMQ that simplifies setup and operation of open-source message brokers on AWS.
- `applicationsignals` - CloudWatch Application Signals automatically provides a correlated view of application performance that includes real user monitoring data and canaries.
- `apptest` - AWS mainframe modernization ppplication Testing
- `connectcampaignsv2` - Amazon Connect Outbound Campaigns V2
- `invoicing` - Deploy and query invoice units allowing you separate AWS account costs and configures your invoice for each business entity
- `launchwizard` - Easily size, configure, and deploy third party applications on AWS
- `pcaconnectorscep` - AWS Private CA Connector for SCEP
- `pcs` - AWS Parallel Computing Service, easily run HPC workloads at virtually any scale
- `rbin` - Recycle Bin is a resource recovery feature that enables you to restore accidentally deleted snapshots and EBS-backed AMIs. 
- `s3tables` - Amazon S3 Tables enabling Tabular Data Storage At Scale
- `ssmquicksetup` - AWS Systems Manager Quick Setup

And 150 new resources with some notable additions including:

- `aws.apigateway.domain_name_access_associations`
- `aws.appconfig.deployments`, `aws.appconfig.deployment_strategies`
- `aws.batch.job_definitions`
- `aws.bedrock.flows`, `aws.bedrock.prompts`
- `aws.chatbot.custom_actions`
- `aws.cloudformation.guard_hooks`, `aws.cloudformation.lambda_hooks`
- `aws.cloudfront.anycast_ip_lists`
- `aws.cloudtrail.dashboards`, `aws.cloudwatch.dashboards`
- `aws.codepipeline.pipelines`
- `aws.cognito.user_pool_identity_providers`
- `aws.ec2.security_group_vpc_associations`, `aws.ec2.vpc_block_public_access_exclusions`, `aws.ec2.vpc_block_public_access_options`
- `aws.glue.crawlers, aws.glue.databases`, `aws.glue.jobs, aws.glue.triggers`
- `aws.guardduty.malware_protection_plans`
- `aws.iot.commands`
- `aws.memorydb.multi_region_clusters`
- `aws.rds.db_shard_groups`
- `aws.redshift.integrations`
- `aws.sagemaker.clusters`, `aws.sagemaker.endpoints`
- `aws.secretsmanager.resource_policies`, `aws.secretsmanager.rotation_schedules`, `aws.secretsmanager.secret_target_attachments`
- `aws.workspaces.workspaces_pools`
- `aws.wisdom.ai_agents`, `aws.wisdom.ai_prompts`, `aws.wisdom.ai_guardrails`, `aws.wisdom.message_templates`
- and much more!

⭐ us on [__GitHub__](https://github.com/stackql/stackql) and join our community!