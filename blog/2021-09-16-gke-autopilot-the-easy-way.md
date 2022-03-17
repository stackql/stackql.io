---
slug: gke-autopilot-the-easy-way
title: GKE Autopilot - the easy way
author: Jeffrey Aven
author_title: Cloud Consultant
author_url: https://github.com/stackql
author_image_url: https://s.gravatar.com/avatar/f96573d092470c74be233e1dded5376f?s=80
image: /img/blog/infraql-gke-blog.png
description: This article shows how to use StackQL to deploy a GKE Autopilot cluster in a Shared VPC in GCP.
keywords: [stackql, infracoding, IaC, infrastructure as code, gcp, gke, gke autopilot]
tags: [stackql, infracoding, IaC, infrastructure as code, gcp, gke, gke autopilot]
---

I grappled with Terraform for the better part of a day trying to provision a GKE Autopilot cluster in a Shared VPC service project, I was able to do this with StackQL in 2 minutes, this is how...  

Before starting you will need the following to use GKE Autopilot in your Shared VPC:
- control plane IP address range
- control plane authorized networks (if desired)
- the host network and node subnet you intend to use
- pod and services secondary CIDR ranges

(all of the above would typically be pre-provisioned in the Shared VPC design and deployment)

Step 1: Using the GCP console, navigate to your service project, go to **Kubernetes Engine --> Clusters --> Create --> GKE Autopilot --> Configure**.  Enter in all of the desired configuration options (including the network configuration specified above).  Do not select **CREATE**.

Step 2: At the bottom of the dialog used to configure the cluster in the console, use the __Equivalent REST__ button to generate the GKE Autopilot API request body.

Step 3: Supply this as input data to an StackQL [__`INSERT`__](/docs/language-spec/insert) command, either via an `iql` file, on as inline configuration.  Optionally you can convert this to Jsonnet and parameterise for use in other environments.

```jsx
<<<json
{
  "cluster": {
	..from equivalent REST command..
  }
}
>>>

INSERT INTO google.container.`projects.locations.clusters`(
  parent,
  data__cluster
)
SELECT 'projects/my-svc-project/locations/australia-southeast1',
  '{{ .cluster }}'
;
```

easy!
