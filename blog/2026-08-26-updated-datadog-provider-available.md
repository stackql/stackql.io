---
slug: updated-datadog-provider-available
title: Datadog Provider - August 2026
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-datadog-provider-featured-image.png"
description: The StackQL Datadog provider now covers the v1 and v2 REST APIs together - monitors, dashboards, synthetics, SLOs, downtimes, incidents, cases, on-call, logs configuration, metrics, security monitoring, cloud cost, users, roles and keys, integrations, RUM, LLM Observability and fleet automation.
keywords: [stackql, datadog, provider, monitors, dashboards, synthetics, slo, observability, security monitoring, cloud cost, infrastructure as code]
tags: [stackql, datadog, provider, observability, monitors, security, finops]
---

We've released an updated [__StackQL Datadog provider__](https://datadog-provider.stackql.io) covering the Datadog v1 and v2 REST APIs together: __18 services__, __597 resources__ and __1658 operations__, up from 16 services and 575 operations in the previous release.

## What's new

The previous provider was built from the v2 API alone. Datadog's most-used resources - monitors, dashboards, synthetics, SLOs, hosts, log indexes and pipelines - only exist in the v1 API, so this release merges the two specs into one provider. The v2 surface has also grown considerably since the last build. In summary:

- __The v1 API__: monitors (list, search, create, replace, validate, delete), dashboards and dashboard lists, synthetics tests (API, browser and mobile), locations, private locations and global variables, SLOs and SLO corrections, hosts, host totals and host tags, notebooks, log indexes and pipelines, the Azure, PagerDuty, Slack and webhook integrations, and usage metering.
- __New v2 surfaces__: cases and case projects, on-call schedules, escalation policies and paging, status pages, incident configuration and responders, feature flags, deployment gates, LLM Observability (projects, datasets, experiments, prompts, annotation queues), Fleet Automation, cloud cost budgets, commitments and tag pipelines, security findings automation, static analysis and SCA, agentless scanning, SIEM historical detections, RUM replay and product analytics, reference tables, org groups and personal access tokens, among others.
- __Site from the environment__: the provider addresses `https://api.{site}`, and `site` is resolved from `DD_SITE` when it is set (`datadoghq.eu`, `us5.datadoghq.com`, `ap2.datadoghq.com`, ...), the same convention as the Datadog Agent and API clients. Queries carry no site clause; a `WHERE site = '...'` still wins for one statement.
- __Pagination and pushdown__: cursor-paginated lists (audit events, container images, spans, RUM events, CI events, security signals and findings) are traversed transparently, and a SQL `LIMIT` is sent as the API's page-size parameter.
- __snake_case surface__: columns and `WHERE` / `INSERT` keys are snake_case throughout; the few camelCase wire names are aliased.
- __Terraform-aligned authentication__: `DD_API_KEY` and `DD_APP_KEY`, unchanged.

## Service highlights

| Service | Resources | Operations | What it covers |
|---------|----------:|-----------:|----------------|
| `service_management` | 82 | 281 | incidents, cases, on-call, SLOs, downtimes, events, status pages, change management, error tracking |
| `security` | 93 | 247 | security monitoring rules, signals and suppressions, findings and automation, vulnerabilities, CSM, agentless scanning, static analysis, SIEM historical detections |
| `organization` | 85 | 207 | users, roles, permissions, API and application keys, service accounts, teams, org settings, SAML, audit logs, usage |
| `integrations` | 59 | 192 | AWS, GCP, Azure, OCI, Jira, ServiceNow, Slack, Microsoft Teams, Google Chat, PagerDuty, Opsgenie, webhooks, Cloudflare, Confluent, Fastly, Okta, reference tables |
| `monitoring` | 39 | 100 | monitors, synthetics, monitor policies, notification rules, service checks |
| `digital_experience` | 38 | 98 | RUM applications, events, metrics and retention, replay, product analytics, sourcemaps |
| `llm_observability` | 37 | 83 | projects, datasets, experiments, prompts, annotation queues, evaluators, Model Lab |
| `cloud_costs` | 37 | 73 | budgets, AWS / Azure / GCP / OCI cost configs, commitments, tag pipelines, cost attribution |
| `software_delivery` | 20 | 71 | CI pipelines and tests, DORA, deployment gates, workflows, feature flags, code coverage |
| `dashboards` | 16 | 61 | dashboards, dashboard lists, powerpacks, notebooks, widgets, annotations, scheduled reports |
| `logs` | 14 | 55 | indexes, pipelines, archives, custom destinations, log metrics, restriction queries, observability pipelines |
| `infrastructure` | 28 | 47 | hosts and host tags, containers, processes, network devices, app builder, storage management |
| `metrics` | 18 | 42 | metrics and metadata, tag configurations, timeseries and scalar queries, datasets, DDSQL |
| `apm` | 10 | 27 | retention filters, spans metrics, scorecards, traces |
| `remote_config` | 6 | 27 | CSM Threats agent rules and policies, WAF rules and policies |
| `actions` | 6 | 23 | action connections, datastores, execution policies |
| `fleet` | 6 | 16 | agents, deployments, schedules, tracers |
| `catalog` | 3 | 8 | software catalog entities, kinds, relations |

## Authentication

Export an API key and an application key; set `DD_SITE` if your organization is not on `datadoghq.com`:

```bash
export DD_API_KEY=...
export DD_APP_KEY=...
export DD_SITE=datadoghq.eu     # optional, defaults to datadoghq.com
```

## Monitors

Every monitor with its state:

```sql
SELECT id, name, type, overall_state, tags
FROM datadog.monitoring.monitors;
```

Only alerting monitors, using the API's own filter:

```sql
SELECT id, name, overall_state
FROM datadog.monitoring.monitors
WHERE group_states = 'alert';
```

Monitor search, with the same syntax as the Manage Monitors page:

```sql
SELECT id, name, status, type
FROM datadog.monitoring.monitor_search_results
WHERE query = 'type:metric status:alert';
```

## Dashboards, SLOs and synthetics

```sql
SELECT id, title, layout_type, author_handle, modified_at
FROM datadog.dashboards.dashboards;

SELECT id, name, type, target_threshold, timeframe
FROM datadog.service_management.slos;

SELECT public_id, name, type, status, locations
FROM datadog.monitoring.synthetics_tests;
```

## Users, roles and keys

v2 resources return the JSON:API row shape - `id`, `type`, `attributes`, `relationships` - so attributes are one `json_extract` away. A user audit:

```sql
SELECT id,
       json_extract(attributes, '$.email') AS email,
       json_extract(attributes, '$.status') AS status,
       json_extract(attributes, '$.disabled') AS disabled,
       json_extract(attributes, '$.created_at') AS created_at
FROM datadog.organization.users;
```

API keys by age, the input to a rotation policy:

```sql
SELECT id,
       json_extract(attributes, '$.name') AS name,
       json_extract(attributes, '$.created_at') AS created_at,
       json_extract(attributes, '$.last4') AS last4
FROM datadog.organization.api_keys
ORDER BY created_at;
```

## Infrastructure and logs

Hosts reporting to Datadog, and the log indexes with their retention:

```sql
SELECT host_name, up, is_muted, apps, last_reported_time
FROM datadog.infrastructure.hosts;

SELECT name, num_retention_days, daily_limit
FROM datadog.logs.indexes;
```

## Audit log

The audit event list is cursor-paginated and takes the time window as a query parameter:

```sql
SELECT json_extract(attributes, '$.timestamp') AS timestamp,
       json_extract(attributes, '$.attributes.evt.name') AS event,
       json_extract(attributes, '$.attributes.usr.email') AS actor
FROM datadog.organization.audit_logs
WHERE "filter[from]" = 'now-1d';
```

## Security monitoring rules

Which detection rules are enabled, and who last changed them:

```sql
SELECT id, name, type, is_enabled, is_default, updated_at, update_author_id
FROM datadog.security.monitoring_rules
WHERE is_default = false;
```

## Provisioning

Mutations use the same SQL grammar. v1 resources take their fields as columns; v2 resources take the JSON:API `data` document. A monitor end to end - validate the definition, create it, replace it (the v1 monitor API updates with `PUT`), delete it:

```sql
EXEC datadog.monitoring.monitors.validate_monitor
  @type = 'metric alert',
  @query = 'avg(last_5m):avg:system.cpu.user{env:prod} by {host} > 90',
  @name = 'High CPU on prod hosts';

INSERT INTO datadog.monitoring.monitors (name, type, query, message, tags)
SELECT 'High CPU on prod hosts',
       'metric alert',
       'avg(last_5m):avg:system.cpu.user{env:prod} by {host} > 90',
       'CPU above 90% on {{host.name}} @slack-ops',
       '["team:web", "managed-by:stackql"]';

REPLACE datadog.monitoring.monitors
SET name = 'High CPU on prod hosts', type = 'metric alert',
    query = 'avg(last_5m):avg:system.cpu.user{env:prod} by {host} > 95'
WHERE monitor_id = 12345678;

DELETE FROM datadog.monitoring.monitors
WHERE monitor_id = 12345678;
```

A downtime for a release window, and a role:

```sql
INSERT INTO datadog.service_management.downtimes (data)
SELECT '{"type": "downtime",
         "attributes": {"message": "release window", "scope": "env:prod",
                        "monitor_identifier": {"monitor_tags": ["team:web"]},
                        "schedule": {"start": "2026-09-01T22:00:00Z", "end": "2026-09-01T23:00:00Z"}}}';

INSERT INTO datadog.organization.roles (data)
SELECT '{"type": "roles", "attributes": {"name": "read-only-auditors"}}';

UPDATE datadog.organization.roles
SET data = '{"id": "<role-id>", "type": "roles", "attributes": {"name": "auditors"}}'
WHERE role_id = '<role-id>';
```

## Get started

Pull the provider from the public registry:

```bash
registry pull datadog;
```

Provider docs are at [datadog-provider.stackql.io](https://datadog-provider.stackql.io). Let us know what you build. Star us on [__GitHub__](https://github.com/stackql/stackql).
