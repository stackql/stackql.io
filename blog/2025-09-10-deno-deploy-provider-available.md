---
slug: deno-deploy-provider-available
title: Deno Deploy Provider Available
hide_table_of_contents: true
authors:	
  - jeffreyaven
image: "/img/blog/stackql-deno-provider-featured-image.png"
description: Query and interact with Deno Deploy resources using SQL.
keywords: [stackql, deno, deno deploy, iac, analytics]
tags: [stackql, deno, deno deploy, iac, analytics]
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

The new `deno` provider for StackQL is now available, enabling developers to query and manage Deno Deploy resources using familiar SQL syntax. This provider allows you to interact with Deno Deploy's control plane API, giving you complete control over your organizations, projects, deployments, domains, and KV databases through a consistent SQL interface.  

The Deno Deploy provider offers comprehensive coverage of the Deno Deploy API, organized into logical services that mirror Deno's resource structure:

| Service | Description |
|---------|-------------|
| `organization` | Manage organizations and analytics |
| `project` | Create and manage projects within organizations |
| `deployment` | Deploy, manage, and monitor serverless applications |
| `domain` | Configure and verify custom domains for your applications |
| `database` | Manage KV databases and KV database backups for your deployments |

  
With the `deno` provider for StackQL, you can now include Deno Deploy in your infrastructure-as-code workflows, analytics dashboards, and cross-cloud orchestration processes using the same SQL syntax you use for other cloud providers.  

For more information on the Deno Deploy provider for StackQL, see the [__Deno Deploy Provider Docs__](https://deno-provider.stackql.io/) which provides detailed documentation for every resource and method, along with ready-to-use SQL examples.  

## Getting Started

To start using the `deno` provider, simply pull it from the StackQL registry:

```sql
registry pull deno;
```

You'll need to set up your authentication by exporting your Deno API token:

```bash
export DENO_DEPLOY_TOKEN="your_deno_api_token"
```

Then you can begin querying your Deno Deploy resources:

<Tabs
  defaultValue="projects"
  values={[
    { label: 'Projects', value: 'projects', },
    { label: 'Deployments', value: 'deployments', },
  ]
}>
<TabItem value="projects">

```sql
-- List all projects in an organization
SELECT 
  id,
  name,
  description,
  createdAt,
  updatedAt
FROM deno.project.projects
WHERE organizationId = 'your-org-id';
```

Results:
```
|-----------------------------|-------------|--------------------------------------|----------------------|-----------------------------|
|          createdAt          | description |                  id                  |         name         |          updatedAt          |
|-----------------------------|-------------|--------------------------------------|----------------------|-----------------------------|
| 2022-12-13T04:20:13.379555Z |             | 34f9440f-3275-40e2-b327-2ea65269feb5 | stackql-dev-registry | 2025-09-06T00:47:04.579279Z |
|-----------------------------|-------------|--------------------------------------|----------------------|-----------------------------|
| 2022-12-13T04:16:22.054548Z |             | 616de983-3e37-4f44-a99f-b6bb5c59e80a | stackql-registry     | 2025-09-06T01:22:46.872203Z |
|-----------------------------|-------------|--------------------------------------|----------------------|-----------------------------|
```

</TabItem>
<TabItem value="deployments">

```sql
-- List deployments with key information
SELECT
  id,
  description,
  status,
  JSON_EXTRACT(domains, '$[0]') as domain,
  JSON_EXTRACT("databases", '$.default') as db_id,
  createdAt,
  updatedAt
FROM deno.deployment.deployments
WHERE projectId = 'your-project-id'
AND "order" = 'desc';
```

Results:
```
|--------------|--------------------------------|---------|----------------------------------------|--------------------------------------|-----------------------------|-----------------------------|
|      id      |          description           | status  |                 domain                 |                db_id                 |          createdAt          |          updatedAt          |
|--------------|--------------------------------|---------|----------------------------------------|--------------------------------------|-----------------------------|-----------------------------|
| pe60nqshxq3g | Latest deployment              | success | example.com                            | 8971aec3-f72f-44c4-bc9f-0b2a513bd9fc | 2025-09-06T01:22:41.740151Z | 2025-09-06T01:22:49.067315Z |
|--------------|--------------------------------|---------|----------------------------------------|--------------------------------------|-----------------------------|-----------------------------|
```

</TabItem>
</Tabs>

⭐ us on [__GitHub__](https://github.com/stackql/stackql) and join our community!