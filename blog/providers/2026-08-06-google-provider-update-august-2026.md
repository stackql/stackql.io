---
slug: google-provider-update-august-2026
title: Google Provider Update - August 2026
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-google-provider-featured-image.png"
description: Update to the StackQL Google provider adding eleven new services including Agent Registry, Agent Identity, Cluster Director, Database Center and Google Threat Intelligence, a major expansion of Compute Engine and Vertex AI coverage, and removal of APIs retired by Google.
keywords: [stackql, google cloud, gcp, provider, agent registry, vertex ai, compute engine, cluster director, threat intelligence, dataplex]
tags: [stackql, google cloud, gcp, provider, vertex-ai, compute-engine]
---

We've released an update to the [__StackQL Google provider__](https://google-provider.stackql.io/), regenerated from the latest Google API discovery documents. The `google` provider now covers __187 services, 2,183 resources and over 9,100 operations__ - up from 179 services, 1,966 resources and 8,423 operations in the previous release. The companion providers in the google family (`googleworkspace`, `googleadmin` and `firebase`) were regenerated in the same pass.

## New Services

Eleven services are new in this release:

| Service | Description |
|---------|-------------|
| `agentregistry` | Centralized catalog to store, discover and govern MCP servers, tools and AI agents within Google Cloud |
| `agentidentity` | Identities and authorization for AI agents - auth providers, authorizations and access summaries |
| `agentidentitycredentials` | Short-lived credential issuance for agent identities |
| `ces` | Gemini Enterprise for Customer Experience - agents, apps, deployments, conversations, guardrails and tool schemas |
| `hypercomputecluster` | Cluster Director - deploy, manage and monitor AI/ML and HPC clusters |
| `databasecenter` | Organization-wide database fleet health monitoring across projects and folders |
| `metastore` | Dataproc Metastore - services, backups, federations, metadata imports and migrations |
| `threatintelligence` | Google Threat Intelligence - alerts, findings, documents and configurations |
| `cloudnumberregistry` | IP address management - realms, registry books, custom and discovered ranges |
| `developerknowledge` | Programmatic access to Google developer documentation |
| `health` | Google Health API (v4) - health and fitness metrics, data points, devices and subscriptions |

## Expanded Coverage in Existing Services

- `compute` - the largest expansion in this release, 73 new resources: capacity planning (`future_reservations`, `reservation_slots`, `reservation_blocks` and `reservation_sub_blocks` versions), `instant_snapshot_groups` and regional snapshot resources, `cross_site_networks` and `wire_groups`, `organization_security_policies`, `network_firewall_policies`, `rollouts` and `rollout_plans`, plus per-resource IAM policy resources across addresses, firewalls, health checks, routes, target proxies and more
- `aiplatform` (Vertex AI) - agent engine build-out: `agents`, `memory_banks`, `sandbox_environments` (with templates and snapshots), `online_evaluators`, `evaluation_metrics`, `responses` and semantic governance policy resources
- `oracledatabase` - GoldenGate integration: deployments, deployment environments/types/versions, connections and connection assignments, plus autonomous database refreshable clones
- `dataplex` - universal catalog governance: `data_domains`, `data_products`, `data_assets`, `metadata_feeds` and `change_requests`
- `redis` - token-based auth: `token_auth_users`, `auth_tokens` and `acl_policies`
- `networksecurity` - `dns_threat_detectors` and Secure Access Connect realms and attachments
- `cloudkms` - `single_tenant_hsm_instances`, key deletion `proposals` and `retired_resources`
- `observability` - log analytics `buckets`, `datasets`, `links`, `views` and scope settings

Another 50+ services picked up incremental resources and operations, including `discoveryengine`, `contactcenterinsights`, `netapp`, `artifactregistry`, `assuredworkloads` and `dataproc`.

## Removed Services

Google has retired several APIs since the last release, and they are removed from the provider accordingly:

- `datalabeling` - AI Platform Data Labeling was shut down
- `integrations` - Application Integration no longer publishes a discovery document
- `lifesciences` - Cloud Life Sciences was shut down
- `dataplex` Explore resources (`content`, `environments`, `sessions`) were removed upstream

The duplicate `*_aggregated` helper resources in `compute` were also consolidated - cross-zone queries are served by the primary resources, so a zone-less `SELECT` fans out across all zones:

~~~sql
SELECT name, status, machineType, zone
FROM google.compute.instances
WHERE project = 'my-project';
~~~

## Example: Open Firewall Audit

Ingress rules open to the entire internet, and what they allow:

~~~sql
SELECT
  name,
  direction,
  sourceRanges,
  allowed
FROM google.compute.firewalls
WHERE project = 'my-project'
AND sourceRanges LIKE '%0.0.0.0/0%';
~~~

## Get Started

Pull the latest provider from the public registry:

~~~bash
stackql registry pull google;
~~~

Authenticate with a service account key in the `GOOGLE_CREDENTIALS` environment variable (or interactively via `gcloud auth login`), then explore:

~~~sql
SELECT
  json_extract(config, '$.name') AS api,
  state
FROM google.serviceusage.services
WHERE parent = 'my-project'
AND parentType = 'projects'
AND filter = 'state:ENABLED';
~~~

Provider docs, including getting-started queries for each provider in the family, are at [google-provider.stackql.io](https://google-provider.stackql.io/), [googleworkspace-provider.stackql.io](https://googleworkspace-provider.stackql.io/), [googleadmin-provider.stackql.io](https://googleadmin-provider.stackql.io/) and [firebase-provider.stackql.io](https://firebase-provider.stackql.io/). Visit us on [__GitHub__](https://github.com/stackql/stackql) and let us know how you're using it.
