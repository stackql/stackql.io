---
slug: exploring-gcp-services-and-resources-using-stackql
title: Exploring GCP services and resources using StackQL
authors:	
  - jeffreyaven
hide_table_of_contents: false
image: "/img/blog/infraql-blog-post-featured-image.png"
description: This article will walk through examples of exploring services and resources in GCP using StackQL - a SQL based language for working with cloud and SaaS assets.
keywords: [stackql, google cloud, GCP, infracoding, IaC, infrastructure as code]
tags: [stackql, google cloud, GCP, infracoding, IaC, infrastructure as code]
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

<head>
<meta name="author" content="Jeffrey Aven" />
</head>

This article will walk through examples of exploring services and resources in GCP using StackQL - a SQL based language for working with cloud and SaaS assets.  If you are new to StackQL it may be helpful to start [__here__](/docs/getting-started/resource-hierarchy).

## List available services using `SHOW SERVICES`  

To list all of the services available in the google provider, use the [`SHOW SERVICES`](/docs/language-spec/show) command as shown below:  

<Tabs
  defaultValue="shell"
  values={[
    { label: 'Interactive Shell', value: 'shell', },
    { label: 'Command Line', value: 'cmd', },
  ]
}>
<TabItem value="shell">

```jsx
StackQL**>>SHOW SERVICES IN google;
|---------------|-----------|-----------------------|
|      id       |   name    |         title         |
|---------------|-----------|-----------------------|
| appengine__v1 | appengine | App Engine Admin API  |
|---------------------------------------------------|
| bigquery__v2  | bigquery  | BigQuery API          |
|---------------------------------------------------|
| compute__v1   | compute   | Compute Engine API    |
|---------------------------------------------------|
| container__v1 | container | Kubernetes Engine API |
|---------------------------------------------------|
| ...           | ...       | ...                   |
|---------------------------------------------------|
```
</TabItem>
<TabItem value="cmd">

```bash
# returns results to stdout as a json array
stackql exec "show services in google" --output json
# returns results as csv to a file
stackql exec "show services in google" --output csv --outfile services.csv
# return results to stdout as a table
stackql exec "show services in google"
```
</TabItem>
</Tabs>

> For more information on running StackQL commands interactively or non-ineractively, see the [`shell`](/docs/command-line-usage/shell) and [`exec`](/docs/command-line-usage/exec) docs.

To search for a specific service or services using the `LIKE` or `WHERE` operator as shown here:  

```jsx
StackQL**>>SHOW SERVICES IN google WHERE name = 'compute';
|-------------|---------|--------------------|
|     id      |  name   |       title        |
|-------------|---------|--------------------|
| compute__v1 | compute | Compute Engine API |
|-------------|---------|--------------------|
StackQL**>>SHOW SERVICES IN google LIKE '%container%';
|----------------------------|-------------------|------------------------|
|             id             |       name        |         title          |
|----------------------------|-------------------|------------------------|
| container__v1              | container         | Kubernetes Engine API  |
|----------------------------|-------------------|------------------------|
| containeranalysis__v1beta1 | containeranalysis | Container Analysis API |
|----------------------------|-------------------|------------------------|
```

## List available resources for a service using `SHOW RESOURCES`

Once you have identfied the service you want to work with, you can use the SHOW RESOURCES to list all of the available resources in the given service, as shown here:  

```jsx
StackQL**>>SHOW RESOURCES IN google.storage;
|-----------------------------|---------------------|---------------------|
|            name             |        title        |         id          |
|-----------------------------|---------------------|---------------------|
| buckets                     | Bucket              | storage__v1.buckets |
|-----------------------------|---------------------|---------------------|
| objects                     | Object              | storage__v1.objects |
|-----------------------------|---------------------|---------------------|
| ...                         | ...                 | ...                 |
|-----------------------------|---------------------|---------------------|
```

As with `SHOW SERVICES`, you can narrow your search by using the `LIKE` or `WHERE` operators with the `SHOW RESOURCES` command as well.  

There is also an `EXTENDED` token which can be used to get descriptive information about the resource as shown here:  

```jsx
StackQL**>>SHOW EXTENDED RESOURCES IN google.storage WHERE name = 'notifications';
|---------------|---------------------------|--------------|--------------------------------|
|     name      |            id             |    title     |          description           |
|---------------|---------------------------|--------------|--------------------------------|
| notifications | storage__v1.notifications | Notification | A subscription to receive      |
|               |                           |              | Google PubSub notifications.   |
|---------------|---------------------------|--------------|--------------------------------|
```

## List the fields in a service using `DESCRIBE`

If you have identified the resource you want to work with, you can use the [`DESCRIBE`](/docs/language-spec/describe) command to see the available fields for a [`SELECT`](/docs/language-spec/select) operation as shown here:  

```jsx
StackQL**>>DESCRIBE google.storage.buckets;
|-----------------------|---------|
|         name          |  type   |
|-----------------------|---------|
| id                    | string  |
|-----------------------|---------|
| name                  | string  |
|-----------------------|---------|
| location              | string  |
|-----------------------|---------|
| ...                   | ...     |
|-----------------------|---------|
```

As with the `SHOW` command, you can use `EXTENDED` to provide descriptive information about each field as shown here:  

```jsx
StackQL**>>DESCRIBE EXTENDED google.storage.buckets;
|-----------------------|---------|--------------------------------|
|         name          |  type   |          description           |
|-----------------------|---------|--------------------------------|
| id                    | string  | The ID of the bucket. For      |
|                       |         | buckets, the id and name       |
|                       |         | properties are the same.       |
|-----------------------|---------|--------------------------------|
| name                  | string  | The name of the bucket.        |
|-----------------------|---------|--------------------------------|
| location              | string  | The location of the bucket.    |
|                       |         | Object data for objects        |
|                       |         | in the bucket resides in       |
|                       |         | physical storage within this   |
|                       |         | region. Defaults to US. See    |
|                       |         | the developer's guide for the  |
|                       |         | authoritative list.            |
|-----------------------|---------|--------------------------------|
| ...                   | ...     | ...                            |
|-----------------------|---------|--------------------------------|
```

## List the available methods for a service using `SHOW METHODS`  

To see the available methods (operations) available for a given resource (beyond [`INSERT`](/docs/language-spec/insert), [`SELECT`](/docs/language-spec/select) and [`DELETE`](/docs/language-spec/delete)) use the `SHOW METHODS` command as shown here:  

```jsx
StackQL**>>SHOW METHODS IN google.compute.instances;
|-----------------------|----------------------------|
|      MethodName       |   RequiredParams           |
|-----------------------|----------------------------|
| getEffectiveFirewalls | zone, instance,            |
|                       | networkInterface, project  |
|-----------------------|----------------------------|
| setServiceAccount     | instance, project, zone    |
|-----------------------|----------------------------|
| getIamPolicy          | project, resource, zone    |
|-----------------------|----------------------------|
| stop                  | instance, project, zone    |
|-----------------------|----------------------------|
| testIamPermissions    | project, resource, zone    |
|-----------------------|----------------------------|
| attachDisk            | instance, project, zone    |
|-----------------------|----------------------------|
| start                 | instance, project, zone    |
|-----------------------|----------------------------|
| detachDisk            | instance, project, zone,   |
|                       | deviceName                 |
|-----------------------|----------------------------|
| ...                   | ...                        |
|-----------------------|----------------------------|
```

> The operations shown by `SHOW METHODS` (which are reffered to as provider stored procedures) can be invoked using the [`EXEC`](/docs/language-spec/exec) command.

`EXTENDED` can be used like the other meta commands to see a description for the method as shown here:  

```jsx
StackQL**>>SHOW EXTENDED METHODS IN google.compute.instances;
|--------------|--------------------------|--------------------------------|
|  MethodName  |   RequiredParams         |          description           |
|------------- |--------------------------|--------------------------------|
| attachDisk   | instance, project, zone  | Attaches an existing Disk      |
|              |                          | resource to an instance. You   |
|              |                          | must first create the disk     |
|              |                          | before you can attach it.      |
|              |                          | It is not possible to create   |
|              |                          | and attach a disk at the same  |
|              |                          | time. For more information,    |
|              |                          | read Adding a persistent disk  |
|              |                          | to your instance.              |
|--------------|--------------------------|--------------------------------|
| ...          | ...                      | ...                            |
|--------------|--------------------------|--------------------------------|
```

Next up we will start creating infrastructure templates using [`SHOW INSERT`](/docs/language-spec/show#generating-an-insert-template-using-the-show-insert-command), creating  resources using [`INSERT`](/docs/language-spec/insert) and querying resources using [`SELECT`](/docs/language-spec/select), stay tuned!  