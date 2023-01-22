---
slug: azure-provider-for-stackql-released
title: StackQL provider for Azure is now available
hide_table_of_contents: true
authors:	
  - jeffreyaven
image: "/img/blog/stackql-azure-provider-featured-image.png"
description: The StackQL provider for Azure provides key visibility across the Azure estate for CSPM, asset inventory and analysis, finops and more, as well as our IaC and ops (lifecycle management) functionality.
keywords: [azure, microsoft, microsoft azure, stackql, stackql provider registry, multicloud, asset management, cloud security]
tags: [azure, microsoft, microsoft azure, stackql, stackql provider registry, multicloud, asset management, cloud security]
---

Proud to announce the release of the [__Microsoft Azure provider for StackQL__](https://registry.stackql.io/providers/azure/).  

> StackQL allows you to query and interact with your cloud and SaaS assets using a simple SQL framework

The StackQL provider for Azure provides key visibility across the Azure estate for CSPM, asset inventory and analysis, finops and more, as well as our IaC and ops (lifecycle management) functionality.

Created using the [Autorest](https://github.com/Azure/autorest) project using Azure specification docs from the [azure-rest-api-specs](https://github.com/Azure/azure-rest-api-specs) repository, the StackQL azure provider exposes __230__ services, __2,450__ resources (of which 1,985 or 81% are available using `SELECT` statements) and __10,140__ methods in total.  

Core services are available in the `azure` provider, all other services are available using the `azure_extras` provider.  

We will be adding integrated interactive authentication, for now this is cli/sdk based, all of the documentation is [here](https://registry.stackql.io/providers/azure/).  

Give it a test run and let us know what you think!