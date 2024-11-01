---
slug: confluent-provider-for-stackql-released
title: Confluent provider for stackql released
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-confluent-provider-featured-image.png"
description: Query and interact with Confluent Cloud resources using SQL.
keywords: [stackql, confluent, kafka, flink, iac, analytics]
tags: [stackql, confluent, kafka, flink, iac, analytics]
---

We're excited to announce the release of the new Confluent provider for StackQL! With this new provider, users can now seamlessly query, manage, and integrate Confluent Cloud resources using familiar SQL syntax. The Confluent provider opens up possibilities for managing Kafka clusters, environments, organizations, and more, providing unparalleled flexibility for building data and event-driven architectures as infrastructure-as-code.

### Quick Start Example

To start, set the `CONFLUENT_CLOUD_API_KEY` and `CONFLUENT_CLOUD_API_SECRET` environment variables and then pull the Confluent provider from the StackQL registry:

```sql
registry pull confluent;
```

### Querying Confluent Resources

The Confluent provider includes access to a range of services covering resources like billing, catalog, managed Kafka clusters, environments, and more. Let’s look at a few examples of querying these resources.

#### Listing Organizations

To view the organization associated with your Confluent account, use the following query:

```
stackql >> select * from confluent.org.vw_organizations;
|--------------------------------------|----------------|-------------------------------------------------------------------------|-----------------------------|-----------------------------|-------------|-------------|--------------|
|                  id                  |  display_name  |                              resource_name                              |         created_at          |         updated_at          | jit_enabled | api_version |     kind     |
|--------------------------------------|----------------|-------------------------------------------------------------------------|-----------------------------|-----------------------------|-------------|-------------|--------------|
| 73ea43f0-1685-4a78-bc90-fa63ef8102fe | Aven Solutions | crn://confluent.cloud/organization=73ea43f0-1685-4a78-bc90-fa63ef8102fe | 2024-09-06T21:51:43.895116Z | 2024-09-08T09:23:53.147453Z | false       | org/v2      | Organization |
|--------------------------------------|----------------|-------------------------------------------------------------------------|-----------------------------|-----------------------------|-------------|-------------|--------------|
```

#### Listing Environments

To list the available environments in your organization, use this query:

```
select * from confluent.org.vw_environments;
|------------|--------------|---------------------------|------------------------------------------------------------------------------------------------|-----------------------------|-----------------------------|------------------------------------------------------------|-------------|-------------|
|     id     | display_name | stream_governance_package |                                         resource_name                                          |         created_at          |         updated_at          |                            self                            | api_version |    kind     |
|------------|--------------|---------------------------|------------------------------------------------------------------------------------------------|-----------------------------|-----------------------------|------------------------------------------------------------|-------------|-------------|
| env-1wz7pv | default      | null                      | crn://confluent.cloud/organization=73ea43f0-1685-4a78-bc90-fa63ef8102fe/environment=env-1wz7pv | 2024-09-06T21:51:43.901757Z | 2024-09-06T21:51:43.901757Z | https://api.confluent.cloud/org/v2/environments/env-1wz7pv | org/v2      | Environment |
|------------|--------------|---------------------------|------------------------------------------------------------------------------------------------|-----------------------------|-----------------------------|------------------------------------------------------------|-------------|-------------|
| env-216dqo | stackql      | ESSENTIALS                | crn://confluent.cloud/organization=73ea43f0-1685-4a78-bc90-fa63ef8102fe/environment=env-216dqo | 2024-10-29T03:47:21.577972Z | 2024-10-29T03:47:21.577972Z | https://api.confluent.cloud/org/v2/environments/env-216dqo | org/v2      | Environment |
|------------|--------------|---------------------------|------------------------------------------------------------------------------------------------|-----------------------------|-----------------------------|------------------------------------------------------------|-------------|-------------|
```

#### Fetching Kafka Clusters in a Specific Environment

To list Kafka clusters available within a specific environment, modify the `WHERE` clause to target your desired environment:

```
stackql >> select * 
stackql >> from confluent.managed_kafka_clusters.vw_clusters 
stackql >> where environment = 'env-216dqo';
|------------|-------------|--------------|--------------|------------------------------------------------------------------------------------------------------------------------------------------|--------------|--------------|-------|-------------|----------------|------------------------------------------------------------|------------------------------------------------------------------------------------------------|-----------------------------------------------------|---------------------------------------------------------|-----------|-----------------------------|-----------------------------|--------------------------------------------------------|-------------|---------|
|     id     | environment | display_name | status_phase |                                                              resource_name                                                   
            | api_endpoint | availability | cloud | config_kind | environment_id |                    environment_related                     |                                   environment_resource_name                                    |                    http_endpoint                    |                kafka_bootstrap_endpoint                 |  region   |         created_at          |         updated_at          |                          self                          | api_version |  kind   |
|------------|-------------|--------------|--------------|------------------------------------------------------------------------------------------------------------------------------------------|--------------|--------------|-------|-------------|----------------|------------------------------------------------------------|------------------------------------------------------------------------------------------------|-----------------------------------------------------|---------------------------------------------------------|-----------|-----------------------------|-----------------------------|--------------------------------------------------------|-------------|---------|
| lkc-ov720o | env-216dqo  | cluster_0    | PROVISIONED  | crn://confluent.cloud/organization=73ea43f0-1685-4a78-bc90-fa63ef8102fe/environment=env-216dqo/cloud-cluster=lkc-ov720o/kafka=lkc-ov720o |              | LOW          | AWS   | Basic       | env-216dqo     | https://api.confluent.cloud/org/v2/environments/env-216dqo | crn://confluent.cloud/organization=73ea43f0-1685-4a78-bc90-fa63ef8102fe/environment=env-216dqo | https://pkc-p11xm.us-east-1.aws.confluent.cloud:443 | SASL_SSL://pkc-p11xm.us-east-1.aws.confluent.cloud:9092 | us-east-1 | 2024-10-29T03:48:00.562964Z | 2024-10-29T03:48:00.562964Z | https://api.confluent.cloud/cmk/v2/clusters/lkc-ov720o | cmk/v2      | Cluster |
|------------|-------------|--------------|--------------|------------------------------------------------------------------------------------------------------------------------------------------|--------------|--------------|-------|-------------|----------------|------------------------------------------------------------|------------------------------------------------------------------------------------------------|-----------------------------------------------------|---------------------------------------------------------|-----------|-----------------------------|-----------------------------|--------------------------------------------------------|-------------|---------|
```

With these examples, you can see how StackQL makes it easy to interact with Confluent Cloud resources directly through SQL.

### Confluent Services Supported in StackQL

The new Confluent provider for StackQL includes the following services:

- **Billing**: Manage Confluent Cloud billing and view cost metrics.
- **Catalog**: Explore available Confluent Cloud components.
- **Managed Kafka Clusters**: Query and manage Kafka clusters.
- **Flink Artifacts and Compute Pools**: Manage Flink environments and compute resources.
- **IAM**: Configure access controls and permissions.
- **Networking**: Set up and view networking configurations.
- **Schema Registry and Clusters**: Register, manage, and monitor schemas and clusters.
- **Stream Sharing**: Configure shared data streams.
  
See the full provider documentation at [Confluent Provider for StackQL](https://confluent.stackql.io/providers/confluent/) for more details on each service.

### Building Composable Infrastructure Stacks

The Confluent provider for StackQL allows you to compose infrastructure stacks with Confluent resources as part of a broader data infrastructure, integrating seamlessly with other cloud providers. With simple SQL queries, you can pull in resources, monitor usage, and manage configurations across Confluent and other clouds for a cohesive multi-cloud or hybrid cloud setup.

More examples to follow.  Let us know what you think! ⭐ us on [__GitHub__](https://github.com/stackql/stackql).
