---
slug: new-kubernetes-provider-available
title: New Kubernetes Provider Available
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-provider-for-k8s-released.png"
description: A ground-up rebuild of the StackQL k8s provider - every built-in Kubernetes control plane API group as SQL, with subresources, server-side filtering, transparent pagination, and a full write lifecycle, pointable at any conformant cluster - available in the StackQL Provider Registry now.
keywords: [stackql, kubernetes, k8s, provider, cloud native, rbac, audit, gitops, infrastructure as code, kubectl]
tags: [stackql, kubernetes, k8s, provider, cloud-native, rbac, audit]
---

We've rebuilt the StackQL Kubernetes provider from the ground up:

- [__`k8s`__](https://k8s-provider.stackql.io) - every built-in control plane API group in a pinned Kubernetes minor release (currently 1.36): __`core`__, __`apps`__, __`batch`__, __`autoscaling`__, __`networking`__, __`storage`__, __`rbac`__, __`policy`__, __`apiextensions`__, __`admissionregistration`__, __`certificates`__, __`coordination`__, __`discovery`__, __`events`__, __`flowcontrol`__, __`node`__, __`scheduling`__, __`authentication`__, __`authorization`__ and __`apiregistration`__ (20 services, 152 resources, 573 operations)

The provider is generated from the per-group specs published in the Kubernetes repository, so the same provider works against kind, EKS, GKE, AKS, OpenShift or bare metal. Subresources (`status`, `scale`, `log`, `eviction`, `binding`, `approval`) are first-class resources, list pagination is traversed transparently, and `LIMIT` and label/field selectors are pushed down to the API server.

## Connect with kubectl proxy

The provider defaults to `null_auth`, designed for the `kubectl proxy` workflow - the proxy authenticates with your kubeconfig (including the EKS, GKE and AKS credential plugins), and StackQL connects to the local port with no configuration:

```bash
kubectl proxy --port=8001
export KUBE_HOST='localhost:8001'
export KUBE_PROTOCOL='http'
stackql shell
```

`KUBE_HOST` and `KUBE_PROTOCOL` resolve the provider's server variables from the environment, so queries carry no connection clauses at all (an explicit `WHERE protocol = ... AND cluster_addr = ...` still wins when you want to address another cluster in the same session).

## The cluster is a database

Row columns are the top-level fields of each object (`metadata`, `spec`, `status`, `data`); nested values are one `json_extract` away. The pod estate with phase and node placement:

```sql
SELECT json_extract(metadata, '$.namespace') AS namespace,
       json_extract(metadata, '$.name') AS name,
       json_extract(status, '$.phase') AS phase,
       json_extract(spec, '$.nodeName') AS node
FROM k8s.core.pods_all_namespaces;
```

Pods that are not running - a one-line cluster health check:

```sql
SELECT json_extract(metadata, '$.namespace') AS namespace,
       json_extract(metadata, '$.name') AS name,
       json_extract(status, '$.phase') AS phase
FROM k8s.core.pods_all_namespaces
WHERE json_extract(status, '$.phase') NOT IN ('Running', 'Succeeded');
```

Desired versus ready replicas for every deployment in a namespace:

```sql
SELECT json_extract(metadata, '$.name') AS name,
       json_extract(spec, '$.replicas') AS want,
       json_extract(status, '$.readyReplicas') AS ready
FROM k8s.apps.deployments
WHERE namespace = 'default';
```

Node inventory with kubelet version and schedulability:

```sql
SELECT json_extract(metadata, '$.name') AS name,
       json_extract(status, '$.nodeInfo.kubeletVersion') AS kubelet,
       json_extract(status, '$.nodeInfo.osImage') AS os,
       json_extract(spec, '$.unschedulable') AS cordoned
FROM k8s.core.nodes;
```

## RBAC audit and warning events

Column names are snake_case at the SQL surface (`role_ref`, `string_data`, `api_version`), mapped to the API's camelCase on the wire - the same convention as the `aws` and `azure` providers. Who is bound to `cluster-admin`:

```sql
SELECT json_extract(metadata, '$.name') AS binding,
       subjects
FROM k8s.rbac.cluster_role_bindings
WHERE json_extract(role_ref, '$.name') = 'cluster-admin';
```

Recent warning events across the cluster, usually the first place to look when something is off:

```sql
SELECT json_extract(metadata, '$.namespace') AS namespace,
       reason,
       message
FROM k8s.core.events_all_namespaces
WHERE type = 'Warning';
```

## Server-side filtering and pagination

Label and field selectors are ordinary `WHERE` parameters, pushed to the API server so the filtering happens where the data lives:

```sql
SELECT json_extract(metadata, '$.name') AS name
FROM k8s.core.pods_all_namespaces
WHERE label_selector = 'k8s-app=kube-dns';
```

`SELECT ... LIMIT n` lands on the wire as the Kubernetes `limit` parameter, and the `continue` token chain is followed transparently - a `SELECT` returns all rows even when the API server caps page sizes.

## Provision, mutate and tear down

Mutations are the usual SQL verbs - `INSERT` creates an object, `UPDATE` applies a merge patch, `REPLACE` is a full update and `DELETE` removes it. Body columns are the native wire property names:

```sql
-- create
INSERT INTO k8s.core.config_maps(namespace, metadata, data)
SELECT 'default', '{"name": "app-config"}', '{"greeting": "hello"}';

-- partial update (merge patch)
UPDATE k8s.core.config_maps
SET data = '{"mood": "optimistic"}'
WHERE namespace = 'default' AND name = 'app-config';

-- remove it
DELETE FROM k8s.core.config_maps
WHERE namespace = 'default' AND name = 'app-config';
```

Subresources are resources, so scaling a deployment is an `UPDATE` on its `scale` subresource:

```sql
UPDATE k8s.apps.deployments_scale
SET spec = '{"replicas": 3}'
WHERE namespace = 'default' AND name = 'web';
```

and point-in-time pod logs are a queryable column:

```sql
SELECT log FROM k8s.core.pods_log
WHERE namespace = 'default' AND name = 'web-6d5f9c7b8-x2x9k';
```

Streaming operations (`exec`, `attach`, `port-forward`, `watch`) use protocol upgrades and are out of scope for the generated provider.

## Who am I, and can I

The authentication and authorization review kinds are mapped too. The zero-parameter self review is a plain `SELECT`; the parameterized access reviews are an `INSERT` whose verdict comes back with `RETURNING`:

```sql
-- who am I
SELECT json_extract(status, '$.userInfo.username') AS username
FROM k8s.authentication.self_subject_reviews;

-- can I delete pods in prod
INSERT INTO k8s.authorization.self_subject_access_reviews(spec)
SELECT '{"resourceAttributes": {"verb": "delete", "resource": "pods", "namespace": "prod"}}'
RETURNING status;
```

## What changed from the original provider

This is a major update to the previous published `k8s` provider (`v23.03.00121`):

- Coverage expands from 5 services to all 20 built-in API groups, one flat service per group (`networking.k8s.io` is `k8s.networking`)
- Resource names are plural snake_case (`k8s.core.pods`, `k8s.apps.stateful_sets`), consistent with the `aws`, `google` and `databricks` providers
- Request body columns are the native wire property names (`metadata`, `spec`, `data`), not `data__` prefixed; snake_case spellings of camelCase wire names are accepted everywhere
- `SELECT` and `DESCRIBE` columns present as snake_case aliases of the camelCase wire properties
- Namespaced list-all operations are separate `_all_namespaces` resources, and subresources are separate resources (`deployments_scale`, `pods_log`)

The previous provider version remains in the registry for pinning if you need it.

## Direct authentication

To skip the proxy and hit the API server directly, supply a bearer token via the `KUBE_TOKEN` environment variable (the same variable the Terraform Kubernetes provider uses), along with the cluster CA bundle:

```bash
export KUBE_TOKEN=$(kubectl create token my-serviceaccount)
AUTH='{ "k8s": { "type": "bearer", "credentialsenvvar": "KUBE_TOKEN" }}'
stackql shell --auth="${AUTH}" --tls.CABundle cluster-ca.pem
```

For managed clusters, a token from the platform credential helper (`aws eks get-token`, `gke-gcloud-auth-plugin`, `kubelogin`) works the same way; those tokens are short lived, so prefer the proxy vector for long sessions.

## Get started

Pull the provider from the public registry:

```bash
registry pull k8s;
```

Provider docs are at [k8s-provider.stackql.io](https://k8s-provider.stackql.io). Let us know what you build. Star us on [__GitHub__](https://github.com/stackql/stackql).
