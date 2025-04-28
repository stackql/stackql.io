---
slug: quickly-identify-old-node-runtimes-in-aws-lambda
title: (Quickly) Identify Old Node Runtimes in AWS Lambda
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-aws-provider-featured-image.png"
description: Query and interact with AWS resources using SQL.
keywords: [stackql, aws, amazon web services, iac, analytics, lambda]
tags: [stackql, aws, amazon web services, iac, analytics, lambda]
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Have you been sent one of these?

> ## [Action Required] AWS Lambda end of support for Node.js 18 [AWS Account: 824123456789] [EU-CENTRAL-1]

If you are like me and manage AWS accounts with numerous Lambda functions potentially deployed across multiple regions, you need to identify affected resources, in this case, Lambda node runtimes, which will be discontinued later this year.  

With [__`stackql`__](https://github.com/stackql/stackql) this task is easy...

1. Open AWS cloud shell in your AWS account (any region - it doesn't matter)
2. Download `stackql`
```bash
curl -L https://bit.ly/stackql-zip -O && unzip stackql-zip
```
3. Open an authenticated `stackql` command shell
```bash
sh stackql-aws-cloud-shell.sh
```
4. Run some analytic queries using `stackql`; here are some examples...

## 🔍 List all functions and runtimes across regions

Run a stackql query to get the details about functions, runtimes, etc, deployed at any given time across one or more AWS regions.  You can include all 25 AWS regions; each query will be performed asynchronously - speeding up the results.

<Tabs
  defaultValue="query"
  values={[
    { label: 'Query', value: 'query', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="query">

```sql
select 
  function_name, 
  region,
  runtime 
FROM aws.lambda.functions 
WHERE region IN ('us-east-1', 'eu-west-1');
```

</TabItem>
<TabItem value="results">

```bash
|----------------------------------------------|---------------|------------|
| function_name                                | region        | runtime    |
|----------------------------------------------|---------------|------------|
| order-processing-handler                     | us-east-1     | nodejs16.x |
|----------------------------------------------|---------------|------------|
| payment-service-lambda                       | us-east-1     | nodejs18.x |
|----------------------------------------------|---------------|------------|
| user-notifications-service                   | us-east-1     | python3.8  |
|----------------------------------------------|---------------|------------|
| generate-invoice-pdf                         | eu-west-1     | nodejs18.x |
|----------------------------------------------|---------------|------------|
| realtime-data-stream-processor               | eu-west-1     | java11     |
|----------------------------------------------|---------------|------------|
| chat-message-router                          | eu-west-1     | nodejs20.x |
|----------------------------------------------|---------------|------------|
| scheduled-maintenance-checker                | ap-southeast-2| python3.11 |
|----------------------------------------------|---------------|------------|
| s3-image-thumbnail-generator                 | ap-southeast-2| nodejs16.x |
|----------------------------------------------|---------------|------------|
| email-delivery-status-updater                | ap-southeast-2| python3.8  |
|----------------------------------------------|---------------|------------|
```

</TabItem>
</Tabs>

## 📊 Group by runtime and region

Perform an analytic query like a `group by aggregate` query such as...

<Tabs
  defaultValue="query"
  values={[
    { label: 'Query', value: 'query', },
    { label: 'Results', value: 'results', },
  ]
}>
<TabItem value="query">

```sql
select 
  runtime, 
  region, 
  count(*) as num_functions 
FROM aws.lambda.functions 
WHERE region IN ('us-east-1', 'eu-west-1', 'ap-southeast-2')
GROUP BY runtime, region;
```

</TabItem>
<TabItem value="results">


</TabItem>
</Tabs>

```bash
|--------------|----------------|---------------|
| runtime      | region         | num_functions |
|--------------|----------------|---------------|
| nodejs16.x   | us-east-1      | 102           |
|--------------|----------------|---------------|
| nodejs18.x   | us-east-1      | 98            |
|--------------|----------------|---------------|
| python3.8    | us-east-1      | 138           |
|--------------|----------------|---------------|
| nodejs18.x   | eu-west-1      | 40            |
|--------------|----------------|---------------|
| java11       | eu-west-1      | 113           |
|--------------|----------------|---------------|
| nodejs20.x   | eu-west-1      | 120           |
|--------------|----------------|---------------|
| python3.11   | ap-southeast-2 | 384           |
|--------------|----------------|---------------|
| nodejs16.x   | ap-southeast-2 | 88            |
|--------------|----------------|---------------|
| python3.8    | ap-southeast-2 | 18            |
|--------------|----------------|---------------|
```

:::tip

You can easily visualise this data using a notebook; see [__`stackql-codespaces-notebook`__](https://github.com/stackql/stackql-codespaces-notebook) or [__`stackql-jupyter-demo`__](https://github.com/stackql/stackql-jupyter-demo).

:::

Using StackQL you can:
- *Quickly spot functions running on runtimes like `nodejs18.x` that are approaching end of support.*
- *Plan your upgrades region-by-region with confidence.*

⭐ us on [__GitHub__](https://github.com/stackql/stackql) and join our community!