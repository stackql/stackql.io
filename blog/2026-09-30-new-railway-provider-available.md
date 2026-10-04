---
slug: new-railway-provider-available
title: New Railway Provider Available
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-railway-provider-featured-image.png"
description: A new StackQL provider for Railway - query and manage workspaces, projects, environments, services, deployments, variables, domains, volumes and usage using SQL - available in the StackQL Provider Registry now.
keywords: [stackql, railway, provider, paas, deployments, environments, variables, finops, infrastructure as code, cloud inventory, sql]
tags: [provider-announcement, stackql, railway, provider, paas, deployments, finops]
---

We've released a new StackQL provider for Railway:

- [__`railway`__](https://railway-provider.stackql.io) - the Railway public API: __`projects`__, __`environments`__, __`services`__, __`deployments`__, __`variables`__, __`networking`__, __`storage`__, __`billing`__, __`observability`__, __`workspaces`__, __`account`__, __`templates`__, __`integrations`__, __`platform`__ and __`agents`__ (15 services, 151 resources, 396 operations)

The provider reads and writes. Projects, environments, services, variables, domains and volumes can be queried, created, changed and removed, and deployment operations such as redeploy, restart and rollback are available on the resources they act on. The rest of this post is the questions it answers and the tasks it handles.

## Connect

Authentication is a Railway account token or workspace token, read from `RAILWAY_TOKEN` - the same variable the Terraform Railway provider uses:

```bash
export RAILWAY_TOKEN=...
stackql shell
```

Tokens are created under Account settings -> Tokens. A project token does not work with the provider. Railway limits API requests per hour by plan (100 on Free, 1000 on Hobby, 10000 on Pro), which is worth keeping in mind for queries that cover many projects or environments.

## What is deployed where

The workspaces a token can reach, the projects in one, and the environments of a project:

```sql
SELECT id, name, plan
FROM railway.workspaces.workspaces;

SELECT id, name, description, created_at
FROM railway.projects.projects
WHERE workspace_id = '<workspace-id>'
ORDER BY created_at DESC;

SELECT id, name, is_ephemeral
FROM railway.environments.environments
WHERE project_id = '<project-id>';
```

A service instance is a service as it is configured in one environment. Listing the instances of an environment shows what runs there, from which image or repository, and how the last deployment went:

```sql
SELECT service_name, region, num_replicas,
       json_extract(source, '$.image') AS image,
       json_extract(source, '$.repo') AS repo,
       json_extract(latest_deployment, '$.status') AS last_deployment,
       json_extract(latest_deployment, '$.created_at') AS deployed_at
FROM railway.services.service_instances
WHERE environment_id = '<environment-id>';
```

An `IN` list covers several environments in one query, with one request made per environment. Joining to `environments` puts names on the rows:

```sql
SELECT e.name AS environment, s.service_name, s.region, s.num_replicas,
       json_extract(s.latest_deployment, '$.status') AS last_deployment
FROM railway.services.service_instances s
JOIN railway.environments.environments e ON e.id = s.environment_id
WHERE e.project_id = '<project-id>'
  AND s.environment_id IN ('<production-environment-id>', '<staging-environment-id>')
ORDER BY environment, service_name;
```

The same works across projects, here for the services of two projects in a workspace:

```sql
SELECT p.name AS project, s.name AS service, s.created_at
FROM railway.services.services s
JOIN railway.projects.projects p ON p.id = s.project_id
WHERE p.workspace_id = '<workspace-id>'
  AND s.project_id IN ('<project-id>', '<other-project-id>')
ORDER BY project, service;
```

## Deployment health

Deployment outcomes for a project, then the ones that failed or crashed, with who triggered them:

```sql
SELECT status, count(*) AS deployments
FROM railway.deployments.deployments
WHERE project_id = '<project-id>'
GROUP BY status;

SELECT id, service_id, status, created_at,
       json_extract(creator, '$.name') AS deployed_by
FROM railway.deployments.deployments
WHERE project_id = '<project-id>'
  AND status = '{in: [FAILED, CRASHED]}'
ORDER BY created_at DESC;
```

Services in an environment whose latest deployment is not healthy:

```sql
SELECT service_name,
       json_extract(latest_deployment, '$.id') AS deployment_id,
       json_extract(latest_deployment, '$.status') AS status
FROM railway.services.service_instances
WHERE environment_id = '<environment-id>'
  AND json_extract(latest_deployment, '$.status') NOT IN ('SUCCESS', 'SLEEPING');
```

From there, the build and runtime logs of a deployment are tables. `LIMIT` sets how many lines are fetched:

```sql
SELECT timestamp, severity, message
FROM railway.observability.build_logs
WHERE deployment_id = '<deployment-id>'
LIMIT 100;

SELECT timestamp, severity, message
FROM railway.observability.deployment_logs
WHERE deployment_id = '<deployment-id>'
  AND filter = 'error'
LIMIT 100;
```

## Drift between environments

Variables are one row per variable, so comparing two environments is a join. Variables a service has in production that are missing in staging:

```sql
SELECT prod.name
FROM railway.variables.variables prod
LEFT JOIN railway.variables.variables stg
  ON stg.name = prod.name
WHERE prod.project_id = '<project-id>'
  AND prod.environment_id = '<production-environment-id>'
  AND prod.service_id = '<service-id>'
  AND stg.project_id = '<project-id>'
  AND stg.environment_id = '<staging-environment-id>'
  AND stg.service_id = '<service-id>'
  AND stg.name IS NULL
  AND prod.name NOT LIKE 'RAILWAY_%';
```

Variables set in both environments with different values:

```sql
SELECT prod.name
FROM railway.variables.variables prod
JOIN railway.variables.variables stg
  ON stg.name = prod.name
WHERE prod.project_id = '<project-id>'
  AND prod.environment_id = '<production-environment-id>'
  AND prod.service_id = '<service-id>'
  AND stg.project_id = '<project-id>'
  AND stg.environment_id = '<staging-environment-id>'
  AND stg.service_id = '<service-id>'
  AND prod.value <> stg.value
  AND prod.name NOT LIKE 'RAILWAY_%';
```

The variables Railway provides itself are prefixed `RAILWAY_` and differ between environments by design, so they are left out. Both queries return names only. Selecting `value` returns the values in clear, so treat that output as a secret.

The same approach compares how a service is configured in each environment:

```sql
SELECT prod.service_name,
       prod.num_replicas AS prod_replicas, stg.num_replicas AS staging_replicas,
       prod.region AS prod_region, stg.region AS staging_region,
       prod.start_command AS prod_start_command, stg.start_command AS staging_start_command
FROM railway.services.service_instances prod
JOIN railway.services.service_instances stg
  ON stg.service_id = prod.service_id
WHERE prod.environment_id = '<production-environment-id>'
  AND stg.environment_id = '<staging-environment-id>';
```

## Domains and certificates

The generated domains of every service in an environment, without going service by service. The `custom_domains` key of the same column holds the custom ones:

```sql
SELECT s.service_name,
       json_extract(d.value, '$.domain') AS domain,
       json_extract(d.value, '$.target_port') AS target_port
FROM railway.services.service_instances s,
     json_each(json_extract(s.domains, '$.service_domains')) d
WHERE s.environment_id = '<environment-id>';
```

Custom domains carry their verification and certificate state, and the DNS records Railway expects to find:

```sql
SELECT domain,
       json_extract(status, '$.verified') AS verified,
       json_extract(status, '$.certificate_status') AS certificate_status,
       json_extract(status, '$.dns_records') AS dns_records
FROM railway.networking.custom_domains
WHERE project_id = '<project-id>'
  AND environment_id = '<environment-id>'
  AND service_id = '<service-id>';
```

## Usage and cost

Usage for a workspace by measurement, grouped by project, with project names joined in:

```sql
SELECT p.name AS project, u.measurement, u.value
FROM railway.billing.usage u
JOIN railway.projects.projects p
  ON p.id = json_extract(u.tags, '$.project_id')
WHERE u.workspace_id = '<workspace-id>'
  AND u.measurements = '[CPU_USAGE, MEMORY_USAGE_GB, NETWORK_TX_GB, DISK_USAGE_GB]'
  AND u.group_by = '[PROJECT_ID]'
  AND p.workspace_id = '<workspace-id>'
ORDER BY project, measurement;
```

`start_date` and `end_date` narrow the window. The projected usage for the current billing period, and the billing position of the workspace:

```sql
SELECT project_id, measurement, estimated_value
FROM railway.billing.estimated_usage
WHERE workspace_id = '<workspace-id>'
  AND measurements = '[CPU_USAGE, MEMORY_USAGE_GB]';

SELECT state, current_usage, credit_balance,
       json_extract(billing_period, '$.end') AS period_ends,
       json_extract(usage_limit, '$.hard_limit') AS hard_limit
FROM railway.billing.customers
WHERE workspace_id = '<workspace-id>';
```

Volumes report their allocated and used size per environment:

```sql
SELECT json_extract(volume, '$.name') AS volume, mount_path, region, state,
       size_mb, current_size_mb,
       ROUND(100.0 * current_size_mb / size_mb, 1) AS used_pct
FROM railway.storage.volume_instances
WHERE environment_id = '<environment-id>';
```

## Provisioning

A project is created with one environment. `RETURNING` gives back the identifiers the next statements need:

```sql
INSERT INTO railway.projects.projects (name, description, workspace_id)
SELECT 'orders', 'order processing', '<workspace-id>'
RETURNING id, name, primary_environment_id;

INSERT INTO railway.environments.environments (project_id, name)
SELECT '<project-id>', 'staging'
RETURNING id, name;
```

A service from a container image or a repository. A service with a source is deployed when it is created, and a running deployment is billed:

```sql
INSERT INTO railway.services.services (project_id, name, source)
SELECT '<project-id>', 'cache', '{"image": "redis:7-alpine"}'
RETURNING id, name;

INSERT INTO railway.services.services (project_id, name, source, branch)
SELECT '<project-id>', 'api', '{"repo": "my-org/orders-api"}', 'main'
RETURNING id, name;
```

Variables are written one at a time or as a set. Writing a variable that exists replaces its value, and `skip_deploys` keeps the change from triggering a deployment:

```sql
INSERT INTO railway.variables.variables
  (project_id, environment_id, service_id, name, value, skip_deploys)
SELECT '<project-id>', '<environment-id>', '<service-id>', 'LOG_LEVEL', 'info', true;

INSERT INTO railway.variables.variables
  (project_id, environment_id, service_id, variables, skip_deploys)
SELECT '<project-id>', '<environment-id>', '<service-id>',
       '{"SENTRY_DSN": "https://example.ingest.sentry.io/1", "FEATURE_FLAGS": "checkout-v2"}', true;
```

A generated domain and a volume for the service:

```sql
INSERT INTO railway.networking.service_domains (environment_id, service_id)
SELECT '<environment-id>', '<service-id>'
RETURNING id, domain;

INSERT INTO railway.storage.volumes (project_id, environment_id, service_id, mount_path)
SELECT '<project-id>', '<environment-id>', '<service-id>', '/data'
RETURNING id, name;
```

## Day-two operations

Changing how a service runs in an environment is an `UPDATE`. Values in `SET` are written as quoted strings:

```sql
UPDATE railway.services.service_instances
SET num_replicas = '2',
    start_command = 'npm run start',
    healthcheck_path = '/healthz'
WHERE service_id = '<service-id>'
  AND environment_id = '<environment-id>';
```

Deployment operations are `EXEC` methods. The `SHOWRESULTS` hint returns the API's answer, including a refusal:

```sql
EXEC /*+ SHOWRESULTS */ railway.services.service_instances.redeploy
  @service_id = '<service-id>',
  @environment_id = '<environment-id>';

EXEC /*+ SHOWRESULTS */ railway.deployments.deployments.restart
  @id = '<deployment-id>';

EXEC /*+ SHOWRESULTS */ railway.deployments.deployments.rollback
  @id = '<deployment-id>';
```

## Preview environment cleanup

Pull request environments accumulate. The ephemeral environments of a project, with the pull request each one belongs to:

```sql
SELECT id, name, created_at,
       json_extract(meta, '$.pr_number') AS pr_number,
       json_extract(meta, '$.branch') AS branch
FROM railway.environments.environments
WHERE project_id = '<project-id>'
  AND is_ephemeral = true
ORDER BY created_at;
```

Removing one is a `DELETE`. Running the listing again confirms it is gone:

```sql
DELETE FROM railway.environments.environments
WHERE id = '<environment-id>';
```

## A join across providers

Services deployed from a GitHub repository can be matched to the repository itself, for example to find services still deploying from a repository that has been archived or has not been pushed to in some time. This uses the `github` provider alongside `railway`:

```sql
SELECT s.service_name,
       r.full_name AS repo, r.archived, r.pushed_at
FROM railway.services.service_instances s
JOIN github.repos.repos r
  ON r.full_name = json_extract(s.source, '$.repo')
WHERE s.environment_id = '<environment-id>'
  AND r.org = 'my-org'
ORDER BY r.pushed_at;
```

## Get started

Pull the provider from the public registry:

```bash
registry pull railway;
```

Provider docs are at [railway-provider.stackql.io](https://railway-provider.stackql.io). Let us know what you build. Star us on [__GitHub__](https://github.com/stackql/stackql).
