---
slug: linode-provider-for-stackql-released
title: StackQL Linode provider Released
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-linode-provider-featured-image.png"
description: The Linode provider for StackQL enables you to provision, query and manage Linode resources using StackQL SQL.
keywords: [stackql, linode, devops, infrastructure, github actions, cloud security, CI/CD]
tags: [stackql, linode, devops, infrastructure, github actions, cloud security, CI/CD]
---

:::info

[__`stackql`__](https://github.com/stackql/stackql) is a dev tool that allows you to query and manage cloud and SaaS resources using SQL, which developers and analysts can use for CSPM, assurance, user access management reporting, IaC, XOps and more.

:::

The StackQL Linode provider is now available. Using the StackQL Linode provider you can create, query, and manage __Linodes__ (instances), __Volumes__, __NodeBalancers__, __Firewalls__, __StackScripts__, __Databases__, __Kubernetes Clusters__, __Object Storage Buckets__, and much more.  

You can use the StackQL Linode provider with other StackQL providers (such as `aws`, `google`, `azure`, `digitalocean`, and more) to perform multi-cloud CSPM, inventory queries, or multi-provider stack deployments. Documentation for the Linode provider is available at [__StackQL Linode provider docs__](https://registry.stackql.io/providers/linode/).  

Here is an example of creating a Linode (a VM instance), passing variables from a jsonnet config file as well as CI secrets (GitHub Actions Secrets, GitLab CI Secrets, etc.):  

```sql
INSERT INTO linode.instances.linodes(
  data__authorized_keys,
  data__authorized_users,
  data__root_pass,
  data__image,
  data__label,
  data__region,
  data__type
)
SELECT
  '[ "{{ .authorized_key }}" ]',
  '[ "{{ .authorized_user }}" ]',
  '{{ .root_pass }}',
  '{{ .image }}',
  '{{ .label }}',
  '{{ .region }}',
  '{{ .type }}'
;
```

Querying objects in Linode can be done using `SELECT` statements, such as:  

```sql
select id, 
label, 
region, 
JSON_EXTRACT(specs, '$.vcpus') as vcpus,
JSON_EXTRACT(specs, '$.memory') as memory,
JSON_EXTRACT(specs, '$.disk') as disk,
status 
from linode.instances.linodes;
```

Which would return:  

```
|----------|-----------|--------------|-------|--------|-------|---------|                                                                                                                                              
|    id    |   label   |    region    | vcpus | memory | disk  | status  |                                                                                                                                              
|----------|-----------|--------------|-------|--------|-------|---------|                                                                                                                                              
| 46063573 | my-linode | ap-southeast |     1 |   1024 | 25600 | running |                                                                                                                                              
|----------|-----------|--------------|-------|--------|-------|---------| 
```

Summary or aggregate queries such as `GROUP BY` -> `COUNT` or `SUM` are fully supported with StackQL, as are `JOIN` and `UNION` operations (including cross-provider `JOIN` operations).  

StackQL supported outputs include `table`, `csv` (using a comma or user-specified delimiter), and `json`.  

StackQL can be accessed through the interactive shell `stackql shell` as well as noninteractive access using `stackql exec` and server-based access using `stackql srv` - where you can use any Postgres wire protocol client to run StackQL queries. [GitHub actions](https://stackql.io/blog/stackql-github-actions), [Jupyter notebooks](https://stackql.io/blog/cloud-security-and-inventory-analysis-with-stackql-and-jupyter), and Superset dashboards are other options for using StackQL.
