---
slug: exploring-the-google-cloud-asset-api
title: Exploring the Google Cloud Asset API
author: Jeffrey Aven
author_title: Cloud Consultant
author_url: https://github.com/stackql
author_image_url: https://s.gravatar.com/avatar/f96573d092470c74be233e1dded5376f?s=80
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-cloud-asset-inventory-blog.png
description: This article shows how to use the Google Cloud Asset Inventory service using SQL.
keywords: [stackql, google cloud, GCP, cloud asset, cloud asset inventory, asset inventory]
tags: [stackql, google cloud, GCP, cloud asset, cloud asset inventory, asset inventory]
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

The [Cloud Asset API](https://cloud.google.com/asset-inventory/docs) has recently gone GA, this is an exceptionally useful service which stores the history and inventory of cloud resources in your GCP org.  Using the Cloud Asset API via StackQL you can enumerate all of the services and resources in your GCP org, including billable resources such as Cloud Storage buckets or Compute Engine instances, as well as other objects such as billing accounts, folders, projects, firewalls, service accounts and much more.  All of this can be done using SQL!

Let’s start by exploring the available fields in this service:

## Explore the API

Use the `DESCRIBE` or `DESCRIBE EXTENDED` to see the fields available in the `google.cloudasset.assets` resource as shown here:

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Response', value: 'resp', },
  ]
}>
<TabItem value="iql">

```jsx
DESCRIBE EXTENDED google.cloudasset.assets;
```

</TabItem>
<TabItem value="resp">

```jsx
|------------------|--------|-----------------------------------------------------------------------------------------------------------|
|       name       |  type  |                                                description                                                |
|------------------|--------|-----------------------------------------------------------------------------------------------------------|
| name             | string | The full name of the asset. Example:                                                                      |
|                  |        | `//compute.googleapis.com/projects/my_project_123/zones/zone1/instances/instance1`                        |
|                  |        | See [Resource names](https://cloud.google.com/apis/design/resource_names#full_resource_name) for          |
|                  |        | more information.                                                                                         |
|------------------|--------|-----------------------------------------------------------------------------------------------------------|
| orgPolicy        | array  | A representation of an [organization                                                                      |
|                  |        | policy](https://cloud.google.com/resource-manager/docs/organization-policy/overview#organization_policy). |
|                  |        | There can be more than one organization policy with different constraints set on a given resource.        |
|------------------|--------|-----------------------------------------------------------------------------------------------------------|
| servicePerimeter | object | `ServicePerimeter` describes a set of Google Cloud resources which can freely  import and export data     |
|                  |        |  amongst themselves, but not export outside of the `ServicePerimeter`. If a request with a source within  |
|                  |        |  this `ServicePerimeter` has a target outside of the `ServicePerimeter`, the request will be blocked.     |
|                  |        |  Otherwise the request is allowed. There are two types of Service Perimeter - Regular and Bridge.         |
|                  |        |  Regular Service Perimeters cannot overlap, a single Google Cloud project can only belong to a single     |
|                  |        |  regular Service Perimeter. Service Perimeter Bridges can contain only Google Cloud projects as members,  |
|                  |        |  a single Google Cloud project may belong to multiple Service Perimeter Bridges.                          |
|------------------|--------|-----------------------------------------------------------------------------------------------------------|
| osInventory      | object | This API resource represents the available inventory data for a Compute Engine virtual                    |
|                  |        | machine (VM) instance at a given point in time. You can use this API resource to determine                |
|                  |        | the inventory data of your VM. For more information, see [Information provided by OS inventory            |
|                  |        | management](https://cloud.google.com/compute/docs/instances/os-inventory-management#data-collected).      |
|------------------|--------|-----------------------------------------------------------------------------------------------------------|
| relatedAssets    | object | The detailed related assets  with the `relationship_type`.                                                |
|------------------|--------|-----------------------------------------------------------------------------------------------------------|
| accessPolicy     | object | `AccessPolicy` is a container for `AccessLevels` (which define the necessary attributes to use Google     |
|                  |        | Cloud services) and `ServicePerimeters` (which define regions of services able to freely pass data        |
|                  |        | within a perimeter). An access policy is globally visible within an organization, and the restrictions    |
|                  |        | it specifies apply to all projects within an organization.                                                |
|------------------|--------|-----------------------------------------------------------------------------------------------------------|
| iamPolicy        | object | An Identity and Access Management (IAM) policy, which specifies access controls for Google Cloud          |
|                  |        | resources. A `Policy` is a collection of `bindings`. A `binding` binds one or more `members`              |
|                  |        | to a single `role`.  Members can be user accounts, service accounts, Google groups, and domains           |
|                  |        | (such as G Suite). A `role` is a named list of permissions; each `role` can be an IAM predefined role     |
|                  |        | or a user-created custom role. For some types of Google Cloud resources, a `binding` can also             |
|                  |        | specify a `condition`, which is a logical expression that allows access to a resource only                |
|                  |        | if the expression evaluates to `true`. A condition can add constraints based on attributes of the request,|
|                  |        | the resource, or both. To learn which resources support conditions in their IAM policies, see the         |
|                  |        | [IAM documentation](https://cloud.google.com/iam/help/conditions/resource-policies).                      |
|------------------|--------|-----------------------------------------------------------------------------------------------------------|
| ancestors        | array  | The ancestry path of an asset in Google Cloud [resource hierarchy]                                        |
|                  |        | (https://cloud.google.com/resource-manager/docs/cloud-platform-resource-hierarchy),                       |
|                  |        | represented as a list of relative resource names. An ancestry path starts with the closest ancestor in    |
|                  |        | the hierarchy and ends at root. If the asset is a project, folder, or organization, the ancestry path     |
|                  |        | starts from the asset itself. Example: `["projects/123456789", "folders/5432", "organizations/1234"]`     |
|------------------|--------|-----------------------------------------------------------------------------------------------------------|
| assetType        | string | The type of the asset. Example: `compute.googleapis.com/Disk` See [Supported asset types]                 |
|                  |        | (https://cloud.google.com/asset-inventory/docs/supported-asset-types) for more information.               |
|------------------|--------|-----------------------------------------------------------------------------------------------------------|
| accessLevel      | object | An `AccessLevel` is a label that can be applied to requests to Google Cloud services, along with a list   |
|                  |        | of requirements necessary for the label to be applied.                                                    |
|------------------|--------|-----------------------------------------------------------------------------------------------------------|
| resource         | object | A representation of a Google Cloud resource.                                                              |
|------------------|--------|-----------------------------------------------------------------------------------------------------------|
| updateTime       | string | The last update timestamp of an asset. update_time is updated when create/update/delete operation         |
|                  |        | is performed.                                                                                             |
|------------------|--------|-----------------------------------------------------------------------------------------------------------|
```

</TabItem>
</Tabs>

As you can see there is some very interesting stuff here, including where the asset fits in the organization hierarchy as well as whether the asset is included in a service perimeter.  

## Run some queries!

To start querying you just need to supply a root node from which you want to start enumerating assets, this can be at an org level, folder level or project level.  

A simple query to group and count all of the different types of assets in a GCP project is shown here:  

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Response', value: 'resp', },
  ]
}>
<TabItem value="iql">

```jsx
SELECT assetType, COUNT(*)
FROM google.cloudasset.assets 
WHERE parent = 'projects/123123123123'
GROUP BY assetType;
```

</TabItem>
<TabItem value="resp">

```jsx
|--------------------------------------------------|----------|
|                    assetType                     | COUNT(*) |
|--------------------------------------------------|----------|
| appengine.googleapis.com/Application             |        4 |
|--------------------------------------------------|----------|
| appengine.googleapis.com/Service                 |       10 |
|--------------------------------------------------|----------|
| appengine.googleapis.com/Version                 |      110 |
|--------------------------------------------------|----------|
| artifactregistry.googleapis.com/DockerImage      |        8 |
|--------------------------------------------------|----------|
| artifactregistry.googleapis.com/Repository       |        1 |
|--------------------------------------------------|----------|
| bigquery.googleapis.com/Dataset                  |        5 |
|--------------------------------------------------|----------|
| bigquery.googleapis.com/Table                    |       12 |
|--------------------------------------------------|----------|
| cloudbilling.googleapis.com/BillingAccount       |        2 |
|--------------------------------------------------|----------|
| cloudfunctions.googleapis.com/CloudFunction      |        1 |
|--------------------------------------------------|----------|
| cloudresourcemanager.googleapis.com/Folder       |        6 |
|--------------------------------------------------|----------|
| cloudresourcemanager.googleapis.com/Organization |        1 |
|--------------------------------------------------|----------|
| cloudresourcemanager.googleapis.com/Project      |       10 |
|--------------------------------------------------|----------|
| compute.googleapis.com/Address                   |        1 |
|--------------------------------------------------|----------|
| compute.googleapis.com/Disk                      |       20 |
|--------------------------------------------------|----------|
| compute.googleapis.com/Firewall                  |       20 |
|--------------------------------------------------|----------|
| compute.googleapis.com/Instance                  |        9 |
|--------------------------------------------------|----------|
| compute.googleapis.com/InstanceGroup             |        2 |
|--------------------------------------------------|----------|
| compute.googleapis.com/InstanceGroupManager      |        2 |
|--------------------------------------------------|----------|
| compute.googleapis.com/InstanceTemplate          |        2 |
|--------------------------------------------------|----------|
| compute.googleapis.com/Network                   |        4 |
|--------------------------------------------------|----------|
| compute.googleapis.com/Project                   |        3 |
|--------------------------------------------------|----------|
| compute.googleapis.com/Route                     |      118 |
|--------------------------------------------------|----------|
| compute.googleapis.com/Subnetwork                |      112 |
|--------------------------------------------------|----------|
| container.googleapis.com/Cluster                 |        1 |
|--------------------------------------------------|----------|
| container.googleapis.com/NodePool                |        1 |
|--------------------------------------------------|----------|
| containerregistry.googleapis.com/Image           |      132 |
|--------------------------------------------------|----------|
| iam.googleapis.com/ServiceAccount                |       22 |
|--------------------------------------------------|----------|
| iam.googleapis.com/ServiceAccountKey             |       27 |
|--------------------------------------------------|----------|
| k8s.io/Namespace                                 |        6 |
|--------------------------------------------------|----------|
| k8s.io/Node                                      |        2 |
|--------------------------------------------------|----------|
| k8s.io/Pod                                       |       22 |
|--------------------------------------------------|----------|
| k8s.io/Service                                   |        5 |
|--------------------------------------------------|----------|
| pubsub.googleapis.com/Topic                      |        3 |
|--------------------------------------------------|----------|
| rbac.authorization.k8s.io/ClusterRole            |      109 |
|--------------------------------------------------|----------|
| rbac.authorization.k8s.io/ClusterRoleBinding     |       99 |
|--------------------------------------------------|----------|
| rbac.authorization.k8s.io/Role                   |       14 |
|--------------------------------------------------|----------|
| rbac.authorization.k8s.io/RoleBinding            |       17 |
|--------------------------------------------------|----------|
| secretmanager.googleapis.com/Secret              |        1 |
|--------------------------------------------------|----------|
| secretmanager.googleapis.com/SecretVersion       |        1 |
|--------------------------------------------------|----------|
| serviceusage.googleapis.com/Service              |      200 |
|--------------------------------------------------|----------|
| sqladmin.googleapis.com/Instance                 |        2 |
|--------------------------------------------------|----------|
| storage.googleapis.com/Bucket                    |       32 |
|--------------------------------------------------|----------|

```

</TabItem>
</Tabs>

or to see the most recent assets to be deployed or modified you could run:  

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Response', value: 'resp', },
  ]
}>
<TabItem value="iql">

```jsx
SELECT name, updateTime
FROM google.cloudasset.assets 
WHERE parent = 'organizations/12312312312' 
ORDER BY updateTime DESC
LIMIT 3;
```

</TabItem>
<TabItem value="resp">

```jsx
+------------------------------------------------------------------+--------------------------+
|                               name                               |        updateTime        |
+------------------------------------------------------------------+--------------------------+
| //appengine.googleapis.com/apps/mycustomapp                      | 2021-06-11T23:43:37.816Z |
| //cloudresourcemanager.googleapis.com/folders/123123123123       | 2020-04-01T01:00:00Z     |
| //cloudresourcemanager.googleapis.com/organizations/12312312312  | 2019-10-22T04:09:06.757Z |
+------------------------------------------------------------------+--------------------------+
```

</TabItem>
</Tabs>

You can go nuts from here with other reports or drill into detail as to anomalies or stray assets, have fun!