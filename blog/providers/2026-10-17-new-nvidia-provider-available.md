---
slug: new-nvidia-provider-available
title: New NVIDIA NGC Provider Available
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-nvidia-provider-featured-image.png"
description: A new StackQL provider for the NVIDIA NGC control plane - query and manage NVIDIA Cloud Functions (functions, versions, deployments, cluster groups and GPU inventory, sharing grants, invocation), the NGC private registry and public catalog, and NGC organizations, teams and users using SQL - available in the StackQL Provider Registry now.
keywords: [stackql, nvidia, ngc, nvcf, nvidia cloud functions, gpu inventory, ai infrastructure, private registry, provider, infrastructure as code, cloud inventory, sql]
tags: [stackql, nvidia, ngc, nvcf, provider, gpu, ai-infrastructure]
---

We've released a new StackQL provider for NVIDIA NGC:

- [__`nvidia`__](https://nvidia-provider.stackql.io) - the NVIDIA NGC control plane: NVIDIA Cloud Functions (__`nvcf_functions`__, __`nvcf_deployments`__, __`nvcf_queues`__, __`nvcf_invocation`__), the NGC private registry (__`private_registry`__), the public catalog (__`catalog`__) and organization administration (__`orgs`__) (7 services, 82 resources plus a GPU capacity view, 335 operations)

<!-- truncate -->

The provider covers the control plane served from `api.ngc.nvidia.com` and, for queue details and invocation, `api.nvcf.nvidia.com`: NVCF functions and versions, deployments and their GPU specifications, cluster groups and the GPU and instance types they offer, request queues, function sharing grants and the HTTP-polling invocation form; registry models, resources, recipes, helm charts and other artifacts as metadata, with their versions, files, collections, shares and encryption keys; the public catalog, including the GPU catalog, which reads without an account; and organizations, teams, users, roles and invitations. The surface is generated from the published NVCF OpenAPI document and the NGC API definitions behind the NGC API explorer, with the registry's response schemas taken from the `ngcsdk` Python SDK. Artifact binary push and pull is the registry data plane and is not mapped, and container images live under a container registry API whose definition NVIDIA does not publish. NVIDIA's own Terraform provider covers NVCF functions and telemetry endpoints.

## Connect

Authentication is an NGC Personal API Key sent as a bearer token, read from `NGC_API_KEY` - the same variable the NGC CLI and the NVIDIA Terraform provider use. The key needs the Private Registry service scope for the registry and Cloud Functions at the organization level for NVCF. Registry and organization resources are scoped to an organization by its name (`org_name`); setting `NGC_ORG` resolves it for the session, and a `WHERE org_name = '...'` value takes precedence when given. NVCF paths carry no organization, the key implies it:

```bash
export NGC_API_KEY=nvapi-...
export NGC_ORG=0123456789ab   # optional
stackql shell
```

Columns, parameters and body attributes are snake_case; the provider maps them to the API's camelCase and kebab-case names on the wire, and NGC's paginated collections are walked transparently.

## GPU capacity by cluster group

What GPU capacity exists, and where: one row per cluster group, GPU type and instance type the organization can deploy NVCF functions to, through the `vw_gpu_capacity` view:

```sql
SELECT cluster_group, gpu, instance_type, is_default, cluster
FROM nvidia.nvcf_deployments.vw_gpu_capacity
ORDER BY cluster_group, gpu, instance_type;
```

The same estate straight from the `cluster_groups` resource, with `json_each` over the `gpus` column:

```sql
SELECT cg.name AS cluster_group,
       json_extract(g.value, '$.name') AS gpu,
       json_extract(g.value, '$.instanceTypes[0].name') AS instance_type
FROM nvidia.nvcf_deployments.cluster_groups cg,
     json_each(cg.gpus) g
ORDER BY cluster_group, gpu;
```

## NVCF fleet state

Every function version with its status, the container or helm chart it runs and its GPU deployment specification:

```sql
SELECT f.name, f.status, f.version_id,
       f.container_image, f.helm_chart,
       json_extract(d.deployment_specifications, '$[0].gpu') AS gpu,
       json_extract(d.deployment_specifications, '$[0].instanceType') AS instance_type,
       json_extract(d.deployment_specifications, '$[0].minInstances') AS min_instances,
       json_extract(d.deployment_specifications, '$[0].maxInstances') AS max_instances
FROM nvidia.nvcf_functions.functions f
LEFT JOIN nvidia.nvcf_deployments.deployments d
  ON d.function_id = f.id AND d.function_version_id = f.version_id
WHERE f.status = 'ACTIVE';
```

A deployed function's minimum instance count is the GPU capacity it holds whether or not it is invoked:

```sql
SELECT function_name, function_status,
       json_extract(deployment_specifications, '$[0].gpu') AS gpu,
       json_extract(deployment_specifications, '$[0].minInstances') AS min_instances
FROM nvidia.nvcf_deployments.deployments
WHERE function_id = 'fe97aa46-c8ea-4237-ba56-1212036f4d0f'
  AND function_version_id = '868d2192-6819-4b53-89f5-3c7fb1df2a72';
```

## Function sharing audit

Which NVIDIA Cloud Accounts may invoke which function - the authorization grants as data, one row per function across the fleet:

```sql
SELECT id AS function_id, version_id, nca_id AS owner,
       json_extract(authorized_parties, '$[*].ncaId') AS authorized_accounts
FROM nvidia.nvcf_functions.authorizations;
```

## Registry estate and organization membership

Models in the private registry by framework, with size and update recency (with `NGC_ORG` set):

```sql
SELECT name, framework, precision,
       latest_version_id_str AS latest_version,
       latest_version_size_in_bytes / 1048576 AS latest_version_mb,
       updated_date
FROM nvidia.private_registry.models
ORDER BY updated_date DESC;
```

Helm charts, endpoints, blueprints, agents, playbooks, APIs and skills come through the generic artifacts resource (`artifact_type` takes the plural path segment); team-scoped registry paths are the same resources with `team_name` in the `WHERE` clause:

```sql
SELECT name, display_name, latest_version_id_str, updated_date
FROM nvidia.private_registry.artifacts
WHERE artifact_type = 'helm-charts';

SELECT name, updated_date
FROM nvidia.private_registry.models
WHERE team_name = 'ml-platform';
```

Users in the organization with their roles:

```sql
SELECT email, name, is_active, last_login_date,
       json_extract(roles, '$[0].orgRoles') AS org_roles
FROM nvidia.orgs.users;
```

## Public catalog, no account needed

Public model metadata and the GPU catalog, readable with any value in `NGC_API_KEY`:

```sql
SELECT name, org_name, display_name, framework
FROM nvidia.catalog.models
LIMIT 20;

SELECT display_name, pci_device_id, memory_size_gb, form_factor
FROM nvidia.catalog.gpus;
```

## Provisioning and invocation

Mutations use the same SQL grammar - `INSERT` creates, `UPDATE` patches, `DELETE` removes, and lifecycle actions are `EXEC` methods. A function with a first version, its deployment on the smallest instance, a sharing grant and the teardown:

```sql
INSERT INTO nvidia.nvcf_functions.functions (name, inference_url, container_image, api_body_format)
SELECT 'echo-demo', '/echo', 'nvcr.io/0123456789ab/echo:latest', 'CUSTOM';

INSERT INTO nvidia.nvcf_deployments.deployments (function_id, function_version_id, deployment_specifications)
SELECT 'fe97aa46-...', '868d2192-...',
       '[{"gpu": "L40", "backend": "GFN", "instanceType": "GCP.GPU.L40_1x", "minInstances": 1, "maxInstances": 1}]';

INSERT INTO nvidia.nvcf_functions.authorizations (function_id, authorized_party)
SELECT 'fe97aa46-...', '{"ncaId": "nca-partner-id"}';

DELETE FROM nvidia.nvcf_deployments.deployments
WHERE function_id = 'fe97aa46-...' AND function_version_id = '868d2192-...';

DELETE FROM nvidia.nvcf_functions.function_versions
WHERE function_id = 'fe97aa46-...' AND function_version_id = '868d2192-...';
```

The HTTP-polling invocation form is an `EXEC` whose opaque JSON body is the function's own request, and the result is read back by request id:

```sql
EXEC nvidia.nvcf_invocation.invocations.invoke
  @function_id = 'fe97aa46-...',
  @body = '{"prompt": "hello"}';

SELECT response
FROM nvidia.nvcf_invocation.invocation_status
WHERE request_id = '<NVCF-REQID from the invoke>';
```

Registry metadata is provisioned the same way (`UPDATE` sends every `SET` value as a string):

```sql
INSERT INTO nvidia.private_registry.models (name, display_name, framework, precision, application, short_description)
SELECT 'resnet50-finetuned', 'ResNet-50 fine-tuned', 'PyTorch', 'FP16', 'Classification', 'fine-tuned on internal data';

UPDATE nvidia.private_registry.models
SET display_name = 'ResNet-50 fine-tuned v2'
WHERE model_name = 'resnet50-finetuned';

DELETE FROM nvidia.private_registry.models
WHERE model_name = 'resnet50-finetuned';
```

## The whole GPU estate in one statement

NVCF cluster-group GPU types alongside the hyperscaler GPU instance inventory, using the `aws` and `google` providers in the same session:

```sql
SELECT 'nvcf' AS platform, cluster_group AS location, gpu
FROM nvidia.nvcf_deployments.vw_gpu_capacity
UNION ALL
SELECT 'aws', region, json_extract(gpu_info, '$.Gpus[0].Name')
FROM aws.ec2.instance_types
WHERE region = 'us-east-1' AND gpu_info IS NOT NULL
UNION ALL
SELECT 'google', json_extract(zone, '$'), json_extract(accelerators, '$[0].guestAcceleratorType')
FROM google.compute.machine_types
WHERE project = 'my-project' AND zone = 'us-central1-a' AND accelerators IS NOT NULL;
```

## Get started

Pull the provider and start querying:

```sql
REGISTRY PULL nvidia;
```

The full reference is at [nvidia-provider.stackql.io](https://nvidia-provider.stackql.io), and the source is at [stackql-registry/stackql-provider-nvidia](https://github.com/stackql-registry/stackql-provider-nvidia). Questions and requests are welcome in the [StackQL Discussions](https://github.com/orgs/stackql/discussions).
