---
slug: digitalocean-provider-for-stackql-released
title: Digital Ocean provider for StackQL Available
hide_table_of_contents: false
authors:	
  - kieranrimmer
image: "/img/blog/stackql-digitalocean-provider-featured-image"
description: The Digital Ocean provider for StackQL enables you to provision, query and manage Digital Ocean assets using StackQL SQL.
keywords: [stackql, digitalocean, digital ocean, devops, infrastructure, github actions, cloud security, CI/CD]
tags: [stackql, digitalocean, digital ocean, devops, infrastructure, github actions, cloud security, CI/CD]
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

:::info

[__`stackql`__](https://github.com/stackql/stackql) is a dev tool that allows you to query and manage cloud and SaaS resources using SQL, which developers and analysts can use for CSPM, assurance, user access management reporting, IaC, XOps and more.

:::

The Digital Ocean provider is now available for StackQL.  You can use StackQL to provision, manage or report on __Droplets__, __Apps__, __Functions__, __Databases__, __Volumes__, __Spaces__, and more.  

To use the Digital Ocean provider, generate a Personal Access Token from the Digital Ocean Control Panel under the API section.  Export the value of the token created to a variable named DIGITALOCEAN_TOKEN (on your local system or as a CI secret).  You can then run queries against the Digital Ocean provider using StackQL.  

The following example demonstrates the creation of a Droplet in Digital Ocean.  

```sql
INSERT INTO digitalocean.droplets.droplets ( 
data__name, 
data__region, 
data__size, 
data__image, 
data__backups, 
data__ipv6, 
data__monitoring, 
data__tags 
) 
SELECT 
'droplet-1.example.com', 
'nyc3', 
's-1vcpu-1gb', 
'ubuntu-20-04-x64', 
true, 
true, 
true, 
'["env:prod", "web"]';
```

You can use [jsonnet](https://jsonnet.org/) as a configuration, templating language with StackQL to provide variables or parameters to IaC operations in StackQL; this can be done using the `--data` flag in the `stackql exec` command as follows:  

```bash
./stackql exec --infile create_droplets.iql --iqldata vars.jsonnet
```

The code for `create_droplets.iql` and `vars.jsonnet` is shown here:  

<Tabs
  defaultValue="iql"
  values={[
    { label: 'create_droplets.iql', value: 'iql', },
    { label: 'vars.jsonnet', value: 'data', },
  ]}
>
<TabItem value="iql">

```sql
{{range $index, $element := .droplets}}
INSERT INTO digitalocean.droplets.droplets ( 
data__name, 
data__region, 
data__size, 
data__image, 
data__backups, 
data__ipv6, 
data__monitoring, 
data__tags 
) 
SELECT 
'droplet-{{$index}}.stackql.io', 
'nyc3', 
'{{.size}}', 
'ubuntu-20-04-x64', 
true, 
true, 
true, 
'["env:prod", "web"]';
{{end}}
```
</TabItem>
<TabItem value="data">

```javascript
{
	droplets: [
		{ size: 's-1vcpu-512mb-10gb' },
		{ size: 's-1vcpu-1gb' },
		{ size: 's-1vcpu-1gb-amd' },
	]
}
```
</TabItem>
</Tabs>

StackQL is a unified SQL-based framework that can be used for analytics and reporting as well as provisioning, de-provisioning, and lifecycle opertaions.  As a native multi-cloud solution, StackQL can analyze and report across assets across multiple different providers; an example is shown here:  

```sql
SELECT 
name, 
JSON_EXTRACT(region, '$.name') as region, 
JSON_EXTRACT(size, '$.slug') as size,
'digitalocean' as provider
FROM digitalocean.droplets.droplets
UNION
SELECT
instanceId as name,
'us-east-1' as region,
instanceType as size,
'aws' as provider
FROM aws.ec2.instances 
WHERE region = 'us-east-1';
```

Digital Ocean and multi-cloud queries can be visualized using BI tools or notebooks; examples of using StackQL with Jupyter can be found [here](https://stackql.io/blog/cloud-security-and-inventory-analysis-with-stackql-and-jupyter).

More information about the Digital Ocean provider for StackQL can be found [here](https://digitalocean.stackql.io/providers/digitalocean/).  
