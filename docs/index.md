---
title: Welcome to StackQL
hide_title: true
hide_table_of_contents: true
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: "/img/stackql-featured-image.png"
slug: /
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import DocsHero from '@site/src/components/DocsHero/DocsHero';

<DocsHero
  lightSrc="/img/stackql-logo-bold-light-mode.png"
  darkSrc="/img/stackql-logo-bold-dark-mode.png"
  alt="StackQL"
  title={
  <>
  Open Source<br/>
  Multi Cloud Analytics,<br/>
  IaC, Automation and more
  </>
  }
  byline={
    <>
    Provision. Query. Secure.
    </>
    }
/>

---

StackQL is a new approach to Cloud Infrastructure coding. With StackQL you can deploy, query and interact with cloud services and resources from major cloud providers using a familiar language...SQL! Querying cloud services in your account is as easy as writing a [`SELECT`](/docs/language-spec/select) statement...

<Tabs
  defaultValue="iql"
  values={[
    { label: 'Query', value: 'iql', },
    { label: 'Results', value: 'data', },
  ]
}>
<TabItem value="iql">

```sql
SELECT instance_state, COUNT(*) as num_instances 
FROM aws.ec2.instances 
WHERE region = 'us-west-1' 
GROUP BY instance_state;
```

</TabItem>
<TabItem value="data">

```
|---------------------------------|
|  instanceState  | num_instances |
|---------------------------------|
|     running     |     342       |
|---------------------------------|
|     pending     |       4       |
|---------------------------------|
|     stopped     |      38       |
|---------------------------------|
```

</TabItem>
</Tabs>

Deploying resources using your cloud provider is as easy as writing an [`INSERT`](/docs/language-spec/insert) statement...

```sql
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

Using StackQL you can develop your way: declarative or procedural. With an easy grammar to learn and no state file to manage, you can get started quickly and use StackQL interchangeably with other infrastructure as code tools, cloud native or otherwise. Uses for StackQL include:

- Cloud infrastructure deployment (using SQL)
- Cloud asset inventory and reporting (using SQL)
- Cloud compliance and control attestation (using SQL)
- Configuration drift detection (using SQL)
- and more, only limited by your imagination!

## OK, Let's Get Started!

[Installing StackQL](/docs/installing-stackql)  
[StackQL Resource Hierarchy](/docs/getting-started/resource-hierarchy)  
[Using Providers](/docs/getting-started/using-a-provider)  
[Using StackQL](/docs/getting-started/using-stackql)  
