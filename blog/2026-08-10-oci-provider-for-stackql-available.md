---
slug: oci-provider-for-stackql-available
title: Oracle Cloud Infrastructure Provider for StackQL
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-oci-provider-featured-image.png"
description: A new StackQL provider for Oracle Cloud Infrastructure - query and provision OCI compute, networking, storage, database, IAM, usage and audit resources with SQL, completing multicloud inventory and FinOps coverage across oci, aws, azure and google.
keywords: [stackql, oci, oracle, oracle cloud infrastructure, provider, iac, analytics, multicloud, finops]
tags: [stackql, oci, oracle, oracle cloud infrastructure, provider, iac, analytics, multicloud, finops]
---

We've released a new StackQL provider for Oracle Cloud Infrastructure:

- [__`oci`__](https://oci-provider.stackql.io) - the OCI control plane across identity, compute, networking, storage, database, security, observability and cost services (22 services, 452 resources, ~1,540 mapped operations)

This completes StackQL's hyperscaler coverage alongside [__`aws`__](https://aws-provider.stackql.io), [__`azure`__](https://azure-provider.stackql.io) and [__`google`__](https://google-provider.stackql.io) - the same SQL surface now spans all four clouds for inventory, audit and FinOps queries.

The provider covers the full lifecycle on tier-1 resources: `SELECT` across the estate, `INSERT`/`UPDATE`/`DELETE` on VCNs, subnets, instances, buckets, databases and policies, and `EXEC` for actions like instance power actions and Autonomous Database start/stop. Columns and `WHERE`/`INSERT` keys are snake_case over OCI's camelCase wire format, and SQL `LIMIT` pushes down to the OCI `limit` query parameter.

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
| `usage` | Cost and usage summaries |
| `budgets` | Budgets and alert rules |
| `audit` | Audit events |
| `kms`, `vault`, `secrets` | Key management, secret management, secret retrieval |
| `dns`, `monitoring`, `logging`, `events`, `functions`, `resource_manager`, `streaming`, `work_requests` | The remaining tier-1 services |

## The compartment scope pattern

Nearly every list operation in OCI is scoped to a compartment, so `compartment_id` is the universal `WHERE` key. The tenancy OCID is itself a compartment ID, and the `identity.compartments` resource enumerates compartments for use in joins:

```sql
SELECT id, name
FROM oci.identity.compartments
WHERE compartment_id = 'ocid1.tenancy.oc1..aaaa...';
```

Region is set once via the `OCI_CLI_REGION` environment variable (a server variable), or overridden per query in the `WHERE` clause.

## Compute and network estate queries

Nested details objects come back as JSON columns, addressed with `json_extract`. Instance shape configuration is one example:

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

```
|--------------|-----------------------|-------|-----------|-----------------|
| display_name |         shape         | ocpus | memory_gb | lifecycle_state |
|--------------|-----------------------|-------|-----------|-----------------|
| web-1        | VM.Standard.E2.1.Micro|     1 |         1 | RUNNING         |
|--------------|-----------------------|-------|-----------|-----------------|
```

The same pattern applies across the network estate - VCNs, subnets, security lists and NSGs are all queryable per compartment and joinable on OCIDs.

## FinOps across four clouds

Cost governance surfaces are first-class resources. Budget-vs-actual reporting is a `SELECT`:

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

The flagship use case is multicloud inventory and FinOps - a single `SELECT` across all four hyperscalers:

```sql
SELECT 'oci' AS provider, display_name AS name, shape AS instance_type
FROM oci.compute.instances
WHERE compartment_id = 'ocid1.compartment.oc1..aaaa...'
UNION ALL
SELECT 'aws', instance_id, instance_type
FROM aws.ec2.instances
WHERE region = 'us-east-1'
UNION ALL
SELECT 'azure', name, json_extract(properties, '$.hardwareProfile.vmSize')
FROM azure.compute.virtual_machines
WHERE subscriptionId = 'sub-id' AND resourceGroupName = 'my-rg'
UNION ALL
SELECT 'google', name, machineType
FROM google.compute.instances
WHERE project = 'my-project' AND zone = 'us-central1-a';
```

The same union pattern applies to cost data, giving a four-cloud spend picture in one query.

## Authentication

OCI API requests are signed (API key request signing using draft-cavage HTTP signatures), implemented natively in the engine as the `oci_signing_v1` auth type. Two configuration variants are supported.

Environment variables, using the same names the OCI CLI reads - an environment already configured for the OCI CLI works unchanged:

```bash
export OCI_CLI_TENANCY=ocid1.tenancy.oc1..aaaa...
export OCI_CLI_USER=ocid1.user.oc1..aaaa...
export OCI_CLI_FINGERPRINT=aa:bb:cc:...
export OCI_CLI_KEY_FILE=~/.oci/oci_api_key.pem
export OCI_CLI_REGION=ap-sydney-1
```

Alternatively, the standard `~/.oci/config` file (shared with the OCI CLI and Terraform) can be used by setting `config_file_path` and `profile` in the auth configuration.

## Get started

The `oci` provider requires `oci_signing_v1` support in the engine, available in the latest stackql release. Pull the provider from the public registry:

```sql
registry pull oci;
```

Provider docs are at [oci-provider.stackql.io](https://oci-provider.stackql.io/). Let us know what you build. Star us on [__GitHub__](https://github.com/stackql/stackql).
