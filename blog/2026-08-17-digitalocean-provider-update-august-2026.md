---
slug: digitalocean-provider-update-august-2026
title: DigitalOcean Provider Update - August 2026
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-digitalocean-provider-featured-image.png"
description: Update to the StackQL DigitalOcean provider adding four new services including Serverless and Dedicated Inference, Vector Databases, Managed NFS and the Security Center, a major expansion of GradientAI platform coverage, plus built-in pagination and LIMIT pushdown.
keywords: [stackql, digitalocean, provider, gradientai, inference, vector databases, nfs, security, droplets, kubernetes]
tags: [stackql, digitalocean, provider, gradientai, inference]
---

We've released an update to the [__StackQL DigitalOcean provider__](https://digitalocean-provider.stackql.io/), regenerated from the latest DigitalOcean OpenAPI specification. The `digitalocean` provider now covers __19 services, 257 resources and 657 operations__ - up from 15 services, 192 resources and 516 operations in the previous release. The headline theme of this release is AI: DigitalOcean has shipped a substantial build-out of its GradientAI platform since the last release, and all of it is now queryable with SQL.

## New Services

Four services are new in this release:

| Service | Description |
|---------|-------------|
| `inference` | GradientAI serverless inference (chat completions, messages, embeddings, image generation, responses), batch inference jobs, dedicated inference deployments with GPU accelerators and access tokens, and agent inference |
| `storage` | Managed NFS - shares, access points, snapshots and share actions |
| `security` | DigitalOcean Security Center - scans, scan rules, findings, settings and suppressions |
| `addons` | Marketplace add-on applications and SaaS add-on resources |

The serverless inference resources are served from DigitalOcean's dedicated inference endpoint (`inference.do-ai.run`) and are wired accordingly in the provider, so operations like listing the model catalog or invoking a model route to the right host with the same `DIGITALOCEAN_TOKEN` authentication:

~~~sql
-- available serverless inference models (includes Anthropic, OpenAI,
-- Llama, Mistral and Qwen model families)
SELECT id FROM digitalocean.inference.models;
~~~

## GradientAI Platform Expansion

The `genai` service picked up 13 new resources covering the newest GradientAI platform capabilities:

- `model_routers` and `model_router_presets` - route requests across models with configurable strategies
- `custom_models` - import and manage your own models
- `model_evaluation_runs`, `model_evaluation_presets`, `model_evaluation_metrics` and `custom_evaluation_metrics` - model evaluation pipelines
- `model_catalog` - the full GradientAI model catalog with model cards
- `scheduled_indexing` - scheduled knowledge base indexing
- agent guardrail attach/detach operations on `agents`

Also new in the AI space: the `databases` service now includes `vector_databases` (with backups, restores, resizing and credentials) - DigitalOcean's managed vector database offering for RAG and semantic search workloads:

~~~sql
SELECT id, name, status,
  JSON_EXTRACT(size, '$.storage_gib') as storage_gib
FROM digitalocean.databases.vector_databases;
~~~

## Expanded Coverage in Existing Services

- `apps` - App Platform `events` (with event logs and cancellation) and `job_invocations` (with logs and cancellation)
- `monitoring` - 11 new managed MySQL database metrics resources (CPU, load, memory, disk, threads, operation rates, schema latency and throughput)
- `kubernetes` - `registries` (attach and detach container registries to and from clusters)
- `compute` - `image_account_transfers` (transfer custom images between accounts), and the Reserved IPv6 and VPC NAT Gateway resources graduated from public preview
- `billing` - `insights` (billing insights over date ranges)
- `serverless` - Functions `namespace_access_keys`
- `account` - Organization `teams`
- `databases` - cluster `do_settings`

## Pagination and LIMIT Pushdown

The provider now ships with pagination configured across all services - multi-page result sets (droplets, images, sizes, and so on) are traversed transparently using DigitalOcean's `links.pages.next` tokens, so `SELECT` returns complete result sets without any manual paging. SQL `LIMIT` clauses are also pushed down to the API as `per_page` parameters, avoiding overfetching:

~~~sql
-- fetches all pages transparently
SELECT count(*) FROM digitalocean.compute.images;

-- pushed down as ?per_page=5
SELECT slug, price_monthly FROM digitalocean.compute.sizes LIMIT 5;
~~~

## Simplified INSERT and UPDATE Columns

Request body properties now bind directly to their API names in `INSERT` and `UPDATE` statements - the `data__` column prefix used in previous releases is gone:

~~~sql
INSERT INTO digitalocean.compute.droplets(name, region, size, image)
SELECT 'my-droplet', 'syd1', 's-1vcpu-512mb-10gb', 'ubuntu-24-04-x64';
~~~

If you have existing scripts using `data__`-prefixed columns (e.g. `data__name`), update them to the unprefixed names when you upgrade - the resource documentation shows the exact insert template for every resource.

## Named Lifecycle Methods

DigitalOcean models lifecycle mutations (power on/off, reboot, resize, snapshot, attach, assign and so on) as generic actions endpoints discriminated by a `type` field. The provider now surfaces these as named lifecycle methods on the entity resources themselves - consistent with how lifecycle operations work in other StackQL providers like `aws.ec2.instances`:

~~~sql
-- droplets: enable_backups, disable_backups, reboot, power_cycle, shutdown,
-- power_off, power_on, password_reset, enable_ipv6, restore, resize, rebuild,
-- rename, change_kernel, snapshot, change_backup_policy (+ _by_tag variants)
EXEC digitalocean.compute.droplets.reboot @droplet_id = '123456789';

EXEC digitalocean.compute.droplets.power_off_by_tag @tag_name = 'web-fleet';

-- volumes: attach, detach, resize
EXEC digitalocean.compute.volumes.attach
  @volume_id = '7724db7c-e098-11e5-b522-000f53304e51',
  @@json = '{"droplet_id": 11612190, "region": "nyc1"}';
~~~

The same treatment applies to images (`convert`, `transfer`), reserved IPs, reserved IPv6 and floating IPs (`assign`, `unassign`), and NFS shares (`resize`, `snapshot`, `attach`, `detach`, `reassign`, `switch_performance_tier`). The generic `*_actions_post` methods remain available for backwards compatibility, and action status remains queryable via the corresponding actions resources (e.g. `compute.droplet_actions`).

## Removed Operations

One operation was removed: creating GradientAI model API keys (`genai_create_model_api_key`), which DigitalOcean has retired upstream (the endpoint now returns `410 Gone`). The legacy single-registry container registry API (`/v2/registry`) is now marked deprecated by DigitalOcean in favor of the multi-registry API, but remains available in the provider for backwards compatibility - plan migrations to `digitalocean.container_registry.registries` accordingly.

## Example: Droplet Cost Audit

~~~sql
SELECT
  name,
  status,
  size_slug,
  JSON_EXTRACT(size, '$.price_monthly') as price_monthly
FROM digitalocean.compute.droplets
ORDER BY JSON_EXTRACT(size, '$.price_monthly') DESC;
~~~

## Get Started

Pull the latest provider from the public registry:

~~~bash
stackql registry pull digitalocean;
~~~

Authenticate with a personal access token in the `DIGITALOCEAN_TOKEN` environment variable, then explore:

~~~sql
SELECT slug, name FROM digitalocean.compute.regions WHERE available = 1;
~~~

Provider docs, including getting-started queries for every resource, are at [digitalocean-provider.stackql.io](https://digitalocean-provider.stackql.io/). Visit us on [__GitHub__](https://github.com/stackql/stackql) and let us know how you're using it.
