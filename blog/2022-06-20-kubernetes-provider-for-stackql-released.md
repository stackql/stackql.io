---
slug: kubernetes-provider-for-stackql-released
title: Kubernetes Provider for StackQL Released
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-provider-for-k8s-released.png"
description: The StackQL provider for Kubernetes has been released, you can use this to query node, namespaces, pods, services and more in a k8s cluster.
keywords: [kubernetes, k8s, cloud native, stackql, stackql provider registry, multicloud, asset management, cloud security]
tags: [kubernetes, k8s, cloud native, stackql, stackql provider registry, multicloud, asset management, cloud security]
---

Excited to announce the release of the __Kubernetes provider for StackQL__.  

> StackQL allows you to query and interact with your cloud and SaaS assets using a simple SQL framework

The `k8s` provider can be used to query and interact with [__events__](https://registry.stackql.io/providers/k8s/core_v1/event/), [__namespaces__](https://registry.stackql.io/providers/k8s/core_v1/namespace/), [__nodes__](https://registry.stackql.io/providers/k8s/core_v1/node/), [__persistent volumes__](https://registry.stackql.io/providers/k8s/core_v1/persistent_volume/), [__pvcs__](https://registry.stackql.io/providers/k8s/core_v1/persistent_volume_claim/), [__pods__](https://registry.stackql.io/providers/k8s/core_v1/pod/), [__services__](https://registry.stackql.io/providers/k8s/core_v1/service/), [__service accounts__](https://registry.stackql.io/providers/k8s/core_v1/service_account/) and more.  

Complete provider documentation for all of the Kubernetes resources, properties and methods is available [here](https://registry.stackql.io/providers/k8s).

Here are the steps to get started with the Kubernetes provider:  

## Setup

If you are using a proxy ([`kubectl proxy`](https://kubernetes.io/docs/tasks/extend-kubernetes/http-proxy-access-api/)), follow these instructions:

### Using a proxy

1. Open an interactive shell (authentication will be handled using the proxy and your `.kube/config`):  

```bash
AUTH='{ "k8s": { "type": "null_auth" } }'
./stackql shell --auth="${AUTH}"
```

2. Pull the latest `k8s` provider for StackQL:

```
REGISTRY PULL k8s v0.1.1;
```

3. Query away adding the following expressions to `WHERE` clauses in your `k8s` queries:
-  `protocol = 'http'`
-  `cluster_addr = 'localhost:8080'` **(or whatever port your proxy is listening on)**

```sql
select name, namespace, uid, creationTimestamp 
from k8s.core_v1.pod 
where protocol = 'http' 
and cluster_addr = 'localhost:8080'  
order by name asc limit 3;
```

### Direct cluster access

1. Generate an access token for your cluster, see [Access Clusters Using the Kubernetes API](https://kubernetes.io/docs/tasks/administer-cluster/access-cluster-api/#without-kubectl-proxy).

2. Generate a certificate bundle for your cluster using the following code (for MacOS or Linux):  

```bash
kubectl get secret -o jsonpath="{.items[?(@.type==\"kubernetes.io/service-account-token\")].data['ca\.crt']}" | base64 -i --decode > k8s_cert_bundle.pem
```

:::note

Alternatively, you could add the `--tls.allowInsecure=true` argument to the `stackql` command, it is not recommended however. 

:::

3. Export the token to a variable and supply this as the provider authentication for StackQL:  

```bash
export K8S_TOKEN='eyJhbGciOi...'
AUTH='{ "k8s": { "type": "api_key", "valuePrefix": "Bearer ", "credentialsenvvar": "K8S_TOKEN" } }'
./stackql shell --auth="${AUTH}" --tls.CABundle k8s_cert_bundle.pem
```

4. Pull the latest `k8s` provider for StackQL:

```
REGISTRY PULL k8s v0.1.1;
```

5. Run some queries (provide the `cluster_addr` as a `WHERE` clause parameter):  

```sql
select name, namespace, uid, creationTimestamp 
from k8s.core_v1.service_account 
where cluster_addr = '35.244.65.136' 
and namespace = 'kube-system' 
order by name asc;
```

Welcome your feedback by getting in touch or raising issues at [__stackql/stackql-provider-registry__](https://github.com/stackql/stackql-provider-registry), give us some ⭐️ love while you are there!  