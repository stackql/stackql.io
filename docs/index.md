---
title: Welcome to StackQL
hide_title: true
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
description: Query and Deploy Cloud Infrastructure and Resources using SQL
image: https://storage.googleapis.com/stackql-web-assets/blog/stackql-blog-post-featured-image.png
slug: /
---

import ImageSwitcher from '/js/ImageSwitcher/ImageSwitcher.js';

<ImageSwitcher 
lightImageSrc="/img/stackql-banner.png"
darkImageSrc="/img/stackql-banner-darkbg.png"
alttext="StackQL"/>

## Welcome to the Revolution!

---

StackQL is a new approach to Cloud Infrastructure coding. With StackQL you can deploy, query and interact with cloud services and resources from major cloud providers using a familiar language...SQL! Querying cloud services in your account is as easy as writing a [`SELECT`](/docs/language-spec/select) statement...


```sql
SELECT instanceState, COUNT(*) as num_instances 
FROM aws.ec2.instances 
WHERE region = 'us-west-1' 
GROUP BY instanceState
```


Deploying resources using your cloud provider is as easy as writing an [`INSERT`](/docs/language-spec/insert) statement...

Using StackQL you can develop your way: declarative or procedural. With an easy grammar to learn and no state file to manage, you can get started quickly and use StackQL interchangeably with other infrastructure as code tools, cloud native or otherwise. Uses for StackQL include:

- Cloud infrastructure deployment (using SQL)
- Cloud asset inventory and reporting (using SQL)
- Cloud compliance and control attestation (using SQL)
- Configuration drift detection (using SQL)
- and more, only limited by your imagination!

## OK, Let's Get Started!

[[StackQL Resource Hierarchy]](/docs/getting-started/resource-hierarchy))  
[[Authenticating to a Provider]](/docs/getting-started/authenticating))  
[[Using StackQL]](/docs/getting-started/using-stackql))
