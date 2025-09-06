---
slug: latest-digitalocean-provider-available
title: Latest DigitalOcean Provider Available
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-digitalocean-provider-featured-image.png"
description: Query and interact with DigitalOcean resources using SQL.
keywords: [stackql, digitalocean, iac, analytics]
tags: [stackql, digitalocean, iac, analytics]
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

The latest `digitalocean` provider for StackQL is available now (`v25.09.00345`), featuring a comprehensive reorganization of services and resources that better aligns with DigitalOcean's API structure. This update improves discoverability, logical grouping, and overall usability when working with DigitalOcean resources through StackQL, while adding exciting new capabilities like support for __Gradient AI Platform__ through the `genai` service.

## What's Changed

The updated DigitalOcean provider features a more granular and logical organization of services that mirrors DigitalOcean's API architecture more closely. Here is a summary of the services included in the latest `digitalocean` provider:

| Service | Description |
|---------|-------------|
| `account` | Manages account information, settings, and team configurations |
| `apps` | Manages App Platform deployments for static sites, APIs, and web applications |
| `billing` | Handles billing configurations, invoices, and payment methods |
| `compute` | Controls virtual machines (Droplets), snapshots, and compute resources |
| `container_registry` | Manages container registries and image repositories |
| `databases` | Manages managed database services and configurations |
| `genai` | Provides access to AI capabilities including the Gradient AI Platform |
| `kubernetes` | Controls Kubernetes clusters, node pools, and related resources |
| `monitoring` | Manages monitoring solutions, alerts, and metrics |
| `networking` | Controls VPCs, load balancers, and network configurations |
| `oneclick` | Provides one-click application installations and marketplace solutions |
| `projects` | Organizes resources into logical groups for better management and billing |
| `serverless` | Manages Functions as a Service (FaaS) for event-driven, scalable computing |
| `spaces` | Controls object storage for storing and serving large files and static assets |
| `vpcs` | Manages Virtual Private Cloud networks for isolated resource communications |

:::note

The improved organization including service and resource naming and mapping will introduce changes to queries against the previous provider version (`v24.11.00274`). You can pin the previous version in `stackql-deploy` or via `registry pull` in the interim while you make necessary query modifications.

:::

## Enhanced Documentation

The new [__DigitalOcean Provider Docs__](https://digitalocean-provider.stackql.io/) provide comprehensive documentation on how to use the new `digitalocean` provider including ready-to-use SQL examples for each resource and method. A standout feature is the copy-paste functionality for all SQL queries, making it incredibly easy to:

1. **Compose Infrastructure-as-Code workflows**: Each method documentation includes working SQL examples that can be directly copied into your deployment scripts or CI/CD pipelines. Simply click the copy button next to any example to get production-ready SQL code.

2. **Build analytics dashboards**: Create sophisticated cross-service queries by combining examples from different resources. The documentation's consistent query formatting makes it simple to join related data across multiple DigitalOcean services.

3. **Develop governance reports**: Copy baseline queries and customize them for your specific compliance needs. The pre-formatted SQL provides the perfect starting point for custom reporting.

## Gradient AI Platform Support

A standout addition to this release is comprehensive support for DigitalOcean's Gradient AI Platform through the new `genai` service. This enables developers to:

- Deploy and manage AI models using familiar SQL syntax
- Monitor AI workloads alongside other cloud resources
- Incorporate AI capabilities into infrastructure-as-code workflows
- Create cross-service orchestrations that leverage AI capabilities

## Getting Started

To start using the updated `digitalocean` provider, simply pull the latest version from `stackql shell` or `stackql registry` command:

```sql
registry pull digitalocean;
```

Then you can begin querying your DigitalOcean resources with SQL:

<Tabs
  defaultValue="droplets"
  values={[
    { label: 'Droplet inventory', value: 'droplets', },
    { label: 'Kubernetes clusters', value: 'kubernetes', },
  ]
}>
<TabItem value="droplets">

```sql
-- List all droplets
SELECT
  id,
  name,
  status,
  size_slug,
  JSON_EXTRACT(size, '$.vcpus') as vcpus,
  JSON_EXTRACT(size, '$.memory') as memory,
  JSON_EXTRACT(size, '$.disk') as disk_size_gb,
  JSON_EXTRACT(size, '$.price_hourly') as price_hourly,
  JSON_EXTRACT(size, '$.price_monthly') as price_monthly
FROM digitalocean.compute.droplets;
```

Results:
```
|-----------|---------------------------------------|--------|------------------------|-------|--------|--------------|--------------|---------------|
|    id     |                 name                  | status |       size_slug        | vcpus | memory | disk_size_gb | price_hourly | price_monthly |
|-----------|---------------------------------------|--------|------------------------|-------|--------|--------------|--------------|---------------|
| 457265395 | ubuntu-s-1vcpu-1gb-35gb-intel-syd1-01 | active | s-1vcpu-1gb-35gb-intel |     1 |   1024 |           35 |       0.0119 |             8 |
|-----------|---------------------------------------|--------|------------------------|-------|--------|--------------|--------------|---------------|
| 510398669 | pool-urzofvwy7-l5vvz                  | active | s-2vcpu-4gb            |     2 |   4096 |           80 |      0.03571 |            24 |
|-----------|---------------------------------------|--------|------------------------|-------|--------|--------------|--------------|---------------|
```

</TabItem>
<TabItem value="kubernetes">

```sql
-- List all Kubernetes clusters
SELECT 
  id,
  name,
  cluster_subnet,
  region,
  JSON_EXTRACT(status, '$.state') as state
FROM 
  digitalocean.kubernetes.clusters;
```

Results:
```
|--------------------------------------|------------------------------------|----------------|--------|---------|
|                  id                  |                name                | cluster_subnet | region |  state  |
|--------------------------------------|------------------------------------|----------------|--------|---------|
| 52d8fa14-eca0-46c8-80a8-8aae70a2c82a | k8s-1-33-1-do-2-syd1-1753698369839 | 10.150.0.0/16  | syd1   | running |
|--------------------------------------|------------------------------------|----------------|--------|---------|
```

</TabItem>
</Tabs>

## Using the App Platform Functions

The `apps` services provide access to DigitalOcean's modern deployment platforms. Here's an example of how to list your deployed applications:

```sql
-- List all deployed applications
SELECT
  id,
  default_ingress,
  active_deployment,
  created_at,
  updated_at,
  region,
  tier_slug
FROM
  digitalocean.apps.apps;
```

## Using the GenAI Service

The new `genai` service provides access to DigitalOcean's Gradient AI Platform capabilities. Here's an example of how to list deployed agents:

```sql
-- List all deployed AI agents
SELECT
  name,
  uuid,
  url,
  project_id,
  created_at,
  updated_at,
  region,
  temperature
FROM
  digitalocean.genai.agents;
```

## Use Cases for the DigitalOcean Provider

1. **Infrastructure as Code**: Manage your DigitalOcean resources alongside other cloud providers in a unified IaC approach, see [__`stackql-deploy`__](https://stackql-deploy.io/).

2. **Cost Optimization**: Identify unused resources and opportunities for cost savings with queries that expose pricing information across all resource types.

3. **Security and Compliance**: Audit firewall rules, network configurations, and access patterns to ensure compliance with security policies.

4. **Performance Monitoring**: Track resource utilization, database performance, and identify optimization opportunities.

5. **Cross-Provider Orchestration**: Build workflows that span DigitalOcean and other resource providers, enabling sophisticated data and infrastructure pipelines.

6. **AI-Enhanced Infrastructure**: Leverage the new GenAI service to incorporate AI capabilities into your infrastructure management workflows.

7. **Automated Reporting**: Create automated reports on DigitalOcean usage, performance, and costs.

## Provider Metrics

The latest DigitalOcean provider includes:
- __15__ services
- __192__ resources
- __516__ total methods
- __254__ selectable methods

This comprehensive coverage ensures you can manage and query virtually all aspects of your DigitalOcean infrastructure using familiar SQL syntax.

⭐ us on [__GitHub__](https://github.com/stackql/stackql) and join our community!