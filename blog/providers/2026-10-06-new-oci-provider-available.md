---
slug: new-oci-provider-available
title: New Oracle Cloud Infrastructure Provider Available
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-oci-provider-featured-image.png"
description: A new StackQL provider for Oracle Cloud Infrastructure - compute, networking, storage, database, identity, budgets, usage and audit as SQL, with OCI request signing built into the engine - completing multicloud inventory and FinOps coverage across oci, aws, azure and google.
keywords: [stackql, oci, oracle, oracle cloud infrastructure, provider, multicloud, finops, cloud inventory, iam audit, sql]
tags: [stackql, oci, oracle, provider, multicloud, finops]
---

We've released a new StackQL provider for Oracle Cloud Infrastructure:

- [__`oci`__](https://oci-provider.stackql.io) - the OCI control plane across identity, compute, networking, storage, database, containers, security, observability and cost services: __`identity`__, __`compute`__, __`network`__, __`block_storage`__, __`object_storage`__, __`database`__, __`container_engine`__, __`load_balancer`__, __`dns`__, __`kms`__, __`vault`__, __`secrets`__, __`monitoring`__, __`logging`__, __`events`__, __`functions`__, __`resource_manager`__, __`streaming`__, __`budgets`__, __`usage`__, __`audit`__ and __`work_requests`__ (22 services, 452 resources, 1,544 operations)

This completes StackQL's hyperscaler coverage alongside [__`aws`__](https://aws-provider.stackql.io), [__`azure`__](https://azure-provider.stackql.io) and [__`google`__](https://google-provider.stackql.io): the same SQL surface now spans all four clouds for inventory, audit and FinOps queries.

The provider covers the full lifecycle on the tier-1 services: `SELECT` across the estate, `INSERT`, `UPDATE` and `DELETE` on VCNs, subnets, instances, volumes, buckets, databases and policies, and `EXEC` for actions such as instance power actions and Autonomous Database start and stop. Columns and `WHERE`/`INSERT` keys are snake_case over OCI's camelCase wire format, nested detail objects are JSON columns addressed with `json_extract`, and SQL `LIMIT` pushes down to the OCI `limit` query parameter.

| Service | Description |
|---------|-------------|
| `identity` | Compartments, users, groups, policies, dynamic groups, domains |
| `compute` | Instances, images, instance pools and configurations |
| `network` | VCNs, subnets, security lists, NSGs, gateways, route tables |
| `block_storage` | Volumes, backups, volume groups |
| `object_storage` | Namespaces, buckets, object metadata, preauthenticated requests |
| `database` | DB systems, Autonomous Databases, backups |
| `container_engine` | OKE clusters and node pools |
| `load_balancer` | Load balancers, backend sets, listeners |
| `usage` | Cost and usage summaries, carbon emissions |
| `budgets` | Budgets, alert rules, cost anomaly monitors |
| `audit` | Audit events |
| `kms`, `vault`, `secrets` | Key management, secret management, secret retrieval |
| `dns`, `monitoring`, `logging`, `events`, `functions`, `resource_manager`, `streaming`, `work_requests` | The remaining tier-1 services |

## Connect

OCI API requests are signed with an API key (the draft-cavage HTTP signature scheme). The signing is built into the engine as the `oci_signing_v1` auth type, available from stackql `v0.12.732`, so there is nothing to install beyond stackql itself. The provider reads the same API key credential the OCI CLI and Terraform use:

```bash
export OCI_TENANCY=ocid1.tenancy.oc1..aaaa...
export OCI_USER=ocid1.user.oc1..aaaa...
export OCI_FINGERPRINT=aa:bb:cc:...
export OCI_KEY_FILE=~/.oci/oci_api_key.pem
export OCI_REGION=ap-sydney-1
stackql shell
```

These names are the provider's defaults, so no `--auth` argument is needed. With none of them set, the standard `~/.oci/config` file is read (`DEFAULT` profile), which is the file the OCI CLI and Terraform already share, so an environment configured for either tool works unchanged. A `--auth` context can point the `*_env_var` keys at other names (Terraform's `TF_VAR_*`, for example) or at a different config file and profile; a runtime context always wins over the defaults. `OCI_REGION` resolves the regional service endpoints and can be overridden per query in the `WHERE` clause.

## The compartment scope pattern

Nearly every list operation in OCI is scoped to a compartment, so `compartment_id` is the universal `WHERE` key. The tenancy OCID is itself a compartment ID (the root), and the `identity.compartments` resource enumerates the compartments beneath it, the natural driving table for estate-wide joins:

```sql
SELECT id, name, lifecycle_state
FROM oci.identity.compartments
WHERE compartment_id = 'ocid1.tenancy.oc1..aaaa...';
```

## Compute and network estate queries

Nested details come back as JSON columns, one `json_extract` away. Instance shape configuration is the usual example:

```sql
SELECT
  display_name,
  shape,
  json_extract(shape_config, '$.ocpus') AS ocpus,
  json_extract(shape_config, '$.memoryInGBs') AS memory_gb,
  lifecycle_state
FROM oci.compute.instances
WHERE compartment_id = 'ocid1.compartment.oc1..aaaa...';
```

| display_name | shape | ocpus | memory_gb | lifecycle_state |
|---|---|---|---|---|
| web-1 | VM.Standard.E2.1.Micro | 1 | 1 | RUNNING |

The same pattern applies across the network estate. VCNs, subnets, security lists and network security groups are all queryable per compartment and joinable on OCIDs:

```sql
SELECT display_name, cidr_block, dns_label, lifecycle_state
FROM oci.network.vcns
WHERE compartment_id = 'ocid1.compartment.oc1..aaaa...';
```

## IAM audit

Policy statements are a column, so a tenancy-wide policy review is one query:

```sql
SELECT name, description, statements
FROM oci.identity.policies
WHERE compartment_id = 'ocid1.tenancy.oc1..aaaa...';
```

and so is the user review that usually follows it:

```sql
SELECT name, email, is_mfa_activated, last_successful_login_time
FROM oci.identity.users
WHERE compartment_id = 'ocid1.tenancy.oc1..aaaa...'
  AND is_mfa_activated = 0;
```

## Provision, mutate and tear down

Mutations are the usual SQL verbs. Request body attributes use the same snake_case names as the columns:

```sql
-- create
INSERT INTO oci.network.vcns (compartment_id, cidr_block, display_name)
SELECT 'ocid1.compartment.oc1..aaaa...', '10.0.0.0/16', 'my-vcn';

-- partial update
UPDATE oci.network.vcns
SET display_name = 'my-vcn-renamed'
WHERE vcn_id = 'ocid1.vcn.oc1.ap-sydney-1.aaaa...';

-- remove it
DELETE FROM oci.network.vcns
WHERE vcn_id = 'ocid1.vcn.oc1.ap-sydney-1.aaaa...';
```

Actions map to `EXEC`, with the wire parameter names:

```sql
EXEC oci.compute.instances.instance_action
  @instanceId = 'ocid1.instance.oc1.ap-sydney-1.aaaa...',
  @action = 'STOP';
```

Mutating operations return an `opc-work-request-id`, and the `work_requests` service exposes the work request and its log for polling.

## FinOps across four clouds

Cost governance surfaces are first-class resources. Budget versus actual is a `SELECT`:

```sql
SELECT
  display_name,
  amount,
  actual_spend,
  forecasted_spend,
  alert_rule_count
FROM oci.budgets.budgets
WHERE compartment_id = 'ocid1.tenancy.oc1..aaaa...'
ORDER BY actual_spend DESC;
```

The usage API is a `POST`, so a cost-by-service summary is an `EXEC` whose request body is the same JSON the API takes:

```sql
EXEC oci.usage.usages.request_summarized_usages
  @@json = '{
    "tenantId": "ocid1.tenancy.oc1..aaaa...",
    "timeUsageStarted": "2026-09-01T00:00:00Z",
    "timeUsageEnded": "2026-10-01T00:00:00Z",
    "granularity": "MONTHLY",
    "groupBy": ["service"]
  }';
```

and the audit trail is a time-bounded query against `audit.events`:

```sql
SELECT
  event_time,
  event_type,
  json_extract(data, '$.identity.principalName') AS principal,
  json_extract(data, '$.resourceName') AS resource
FROM oci.audit.events
WHERE compartment_id = 'ocid1.tenancy.oc1..aaaa...'
  AND start_time = '2026-10-01T00:00:00Z'
  AND end_time = '2026-10-06T00:00:00Z';
```

The query this provider exists for is the four-hyperscaler estate in one statement:

```sql
SELECT 'oci' AS provider, display_name AS name, shape AS size, lifecycle_state AS state
FROM oci.compute.instances
WHERE compartment_id = 'ocid1.compartment.oc1..aaaa...'
UNION ALL
SELECT 'aws', instance_id, instance_type, json_extract(state, '$.name')
FROM aws.ec2.instances
WHERE region = 'us-east-1'
UNION ALL
SELECT 'azure', name, json_extract(properties, '$.hardwareProfile.vmSize'), json_extract(properties, '$.provisioningState')
FROM azure.compute.virtual_machines
WHERE subscriptionId = '00000000-0000-0000-0000-000000000000' AND resourceGroupName = 'my-rg'
UNION ALL
SELECT 'google', name, machineType, status
FROM google.compute.instances
WHERE project = 'my-project' AND zone = 'us-central1-a';
```

The same union shape applies to cost data, giving a four-cloud spend picture from one query, in one tool, with one audit log.

## Scope

This release ships the tier-1 services listed above. OCI has dozens more (AI services, integration, analytics, GoldenGate and the rest of the long tail); those are catalogued and will be added per release cycle. Two things are deliberately out of scope: the object storage data plane (object bodies are streaming operations; object listing and metadata are in) and the per-vault KMS crypto and management endpoints, which live on dedicated hosts the provider cannot route to today. One known engine limit: list operations that paginate with OCI's `opc-next-page` response header currently return the first page, sized by `LIMIT` up to the service maximum, while body-token lists such as object listing paginate fully; the engine fix is tracked and the provider needs no change when it lands.

## Get started

The provider needs stackql `v0.12.732` or later. Pull it from the public registry:

```bash
registry pull oci;
```

Provider docs are at [oci-provider.stackql.io](https://oci-provider.stackql.io). Let us know what you build. Star us on [__GitHub__](https://github.com/stackql/stackql).
