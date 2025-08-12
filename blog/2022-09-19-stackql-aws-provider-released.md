---
slug: stackql-aws-provider-released
title: StackQL Provider for AWS Released
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-aws-provider-featured-image.png"
description: The StackQL provider for AWS provides key visibility across the AWS estate for CSPM, asset inventory and analysis, finops and more, as well as our IaC and ops (lifecycle management) functionality.
keywords: [aws, amazon web services, amazon, stackql, stackql provider registry, multicloud, asset management, cloud security]
tags: [aws, amazon web services, amazon, stackql, stackql provider registry, multicloud, asset management, cloud security]
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Pleased to announce the initial release of the AWS provider for StackQL.  

> StackQL allows you to query, provision, and manage cloud and SaaS resources using a simple, SQL-based framework.

The initial release of the AWS provider covers EC2, S3, and the [Cloud Control API](https://aws.amazon.com/cloudcontrolapi/) - with support for other services to be released soon. The documentation for the StackQL AWS provider is available [__here__](/providers/aws/).  

Follow the steps below to get started querying AWS in the StackQL interactive command shell:  

## Authenticate and Connect

Connect to an authenticated shell using the syntax shown below:   

```bash
# AWS_SECRET_ACCESS_KEY and AWS_ACCESS_KEY_ID should be set as environment variables
AUTH="{ \"aws\": { \"type\": \"aws_signing_v4\", \"credentialsenvvar\": \"AWS_SECRET_ACCESS_KEY\", \"keyID\": \"${AWS_ACCESS_KEY_ID}\" }}"
stackql shell --auth="${AUTH}"
```

## Download the AWS provider

Download the AWS provider from the StackQL Provider Registry:    

```bash
REGISTRY PULL aws v0.1.3;
```

## Explore the AWS provider 

Explore the AWS provider using StackQL metacommands (such as [__`SHOW`__](/docs/language-spec/show) and [__`DESCRIBE`__](/docs/language-spec/describe)), for example...  

### Show available services

Show the services available in the StackQL AWS provider:  

```sql
SHOW SERVICES IN aws;
```

### Show available resources

Show the resources available in the AWS EC2 service (filtered by a fuzzy match on instances):  

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SHOW RESOURCES IN aws.ec2 LIKE '%instances%';
```

</TabItem>
<TabItem value="results">

```
|-----------------------------------|-------------------------------------------|
|               name                |                    id                     |
|-----------------------------------|-------------------------------------------|
| classic_link_instances            | aws.ec2.classic_link_instances            |
|-----------------------------------|-------------------------------------------|
| fleet_instances                   | aws.ec2.fleet_instances                   |
|-----------------------------------|-------------------------------------------|
| instances                         | aws.ec2.instances                         |
|-----------------------------------|-------------------------------------------|
| ...                    						| ...                       								|
|-----------------------------------|-------------------------------------------|
```

</TabItem>
</Tabs>

### Show 'selectable' fields

Show the 'selectable' fields available in a resource:  

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
DESCRIBE EXTENDED aws.ec2.instances;
```

</TabItem>
<TabItem value="results">
</TabItem>
<TabItem value="results">

```
|--------------------|---------|-----------------------------------|
|        name        |  type   |        description                |
|--------------------|---------|-----------------------------------|
| instanceId         | string  | The ID of the instance.           |
|--------------------|---------|-----------------------------------|
| instanceType       | string  | The instance type.                |
|--------------------|---------|-----------------------------------|
| instanceState      | object  | Describes the current state of    |
|                    |         | an instance.                      |
|--------------------|---------|-----------------------------------|
| vpcId              | string  | [EC2-VPC] The ID of the VPC in    |
|                    |         | which the instance is running.    |
|--------------------|---------|-----------------------------------|
| subnetId           | string  | [EC2-VPC] The ID of the subnet    |
|                    |         | in which the instance is          |
|                    |         | running.                          |
|--------------------|---------|-----------------------------------|
| ipAddress          | string  | The public IPv4 address           |
|                    |         | assigned to the instance.         |
|--------------------|---------|-----------------------------------|
| privateIpAddress   | string  | The private IPv4 address          |
|                    |         | assigned to the instance.         |
|--------------------|---------|-----------------------------------|
| launchTime         | string  | The time the instance was         |
|                    |         | launched.                         |
|------------------ -|---------|-----------------------------------|
| ...                | ...     | ...						                   |
|--------------------|---------|-----------------------------------|
```

</TabItem>
</Tabs>

### Show operations available

Show the available operations on a resource:    

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SHOW EXTENDED METHODS IN aws.ec2.instances;
```

</TabItem>
<TabItem value="results">

```
|---------------------|---------------------|---------|-------------------------------------------------------------------------------|
|     MethodName      |   RequiredParams    | SQLVerb |                                description                                    |
|---------------------|---------------------|---------|-------------------------------------------------------------------------------|
| instances_Reboot    | InstanceId          | EXEC    | Requests a reboot of the specified instances. This operation is asynchronous  |
|---------------------|---------------------|---------|-------------------------------------------------------------------------------|
| instances_Start     | InstanceId          | EXEC    | Starts an Amazon EBS-backed instance that you've previously stopped           |
|---------------------|---------------------|---------|-------------------------------------------------------------------------------|
| instances_Stop      | InstanceId          | EXEC    | Stops an Amazon EBS-backed instance					                    						  |
|---------------------|---------------------|---------|-------------------------------------------------------------------------------|
| ...                 | ...                 | ...     | ...   											                                                  |
|---------------------|---------------------|---------|-------------------------------------------------------------------------------|
```

</TabItem>
</Tabs>

## Run some queries

Now that you've identified the available resources and fields let's run some queries!  

### Instances by region (across multiple regions)

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT 'N. Virginia' as region, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region = 'us-east-1'
UNION
SELECT 'N. California' as region, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region = 'us-west-1'
UNION
SELECT 'Sydney' as region, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region = 'ap-southeast-2';
```

</TabItem>
<TabItem value="results">

```
|---------------|---------------|
|    region     | num_instances |
|---------------|---------------|
| N. California |             0 |
|---------------|---------------|
| N. Virginia   |             3 |
|---------------|---------------|
| Sydney        |            12 |
|---------------|---------------|
```

</TabItem>
</Tabs>

### Instances grouped by `instanceType`

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT instanceType, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region = 'ap-southeast-2'
GROUP BY instanceType;
```

</TabItem>
<TabItem value="results">

```
|--------------|---------------|
| instanceType | num_instances |
|--------------|---------------|
| t2.medium    |             2 |
|--------------|---------------|
| t2.micro     |             6 |
|--------------|---------------|
| t2.small     |             4 |
|--------------|---------------|
```

</TabItem>
</Tabs>

### Instances grouped by `instanceState`

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT instanceState, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region = 'ap-southeast-2'
GROUP BY instanceState;
```

</TabItem>
<TabItem value="results">

```
|---------------------|---------------|
|    instanceState    | num_instances |
|---------------------|---------------|
|      pending        |       3       |
|---------------------|---------------|
|      stopping       |       8       |
|---------------------|---------------|
|      stopped        |       4       |
|---------------------|---------------|
|      running        |       6       |
|---------------------|---------------|
```

</TabItem>
</Tabs>

Enjoy!