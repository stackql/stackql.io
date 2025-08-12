---
slug: new-functions-and-azure-provider-available
title: New Functions and Azure Provider Available
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-azure-provider-featured-image.png"
description: The StackQL provider for Azure provides key visibility across the Azure estate for CSPM, asset inventory and analysis, finops and more, as well as our IaC and ops (lifecycle management) functionality.
keywords: [azure, microsoft, microsoft azure, stackql, stackql provider registry, multicloud, asset management, cloud security]
tags: [azure, microsoft, microsoft azure, stackql, stackql provider registry, multicloud, asset management, cloud security]
---

> A new version of the Azure provider for StackQL plus additional built-in functions are available now.  

Version 0.3.0 of the Azure provider for StackQL is available now.  This update includes support for extended resource properties, along with support for Hybrid Azure Kubernetes Services.  The Azure provider allows you to query across your Azure estate for cloud security posture, asset inventory, analysis and reporting, finops, sysops, and more - all using a natural query language (SQL) and a natural object mapping.  

In addition, we have announced the release of several new built-in functions, including `SPLIT_PART()` - to split a string by a delimiter and extract a single element, additional unicode functions, and expanded regular expression support, including `REGEXP_REPLACE()` and more.  

An example StackQL query using the `split_part()` function with the `azure v0.3.0` provider is shown here:  

```sql
SELECT name,  
split_part(id, '/', 3) as subscription,
split_part(id, '/', 5) as resource_group,
json_extract(properties, '$.hardwareProfile.vmSize') as vm_size
FROM azure.compute.virtual_machines WHERE resourceGroupName = 'stackql-ops-cicd-dev-01' AND subscriptionId = '242c6a2d-16f9-4912-90f6-59b1cf85509d';
```

You can find more information on the latest Azure provider [__here__](/providers/azure/).  