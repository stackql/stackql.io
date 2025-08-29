---
slug: updated-google-providers-available
title: Updated Google Providers for StackQL Available
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-googleadmin-provider-featured-image.png"
description: Query and interact with Google resources using SQL.
keywords: [stackql, google, firebase, iac, analytics]
tags: [stackql, google, firebase, iac, analytics]
---

The latest versions of the Google-related providers for StackQL: `google`, `googleadmin`, `googleworkspace`, and `firebase` are available now. These updates include the latest services, resources and methods available from Google.

## What's New

The latest release introduces several new services to the `google` provider, expanding your ability to manage and query Google Cloud resources:

- **API Hub**: Centrally manage and discover APIs across your organization
- **Area Insights**: Access location-based insights and analytics
- **Cloud Location Finder**: Identify optimal Google Cloud regions for your workloads
- **Gemini Cloud Assist**: Leverage Google's AI assistant for cloud operations
- **Managed Kafka**: Work with Google's fully-managed Apache Kafka service
- **Observability**: Enhanced monitoring and observability services
- **Parallel Store**: Interact with Google's high-performance storage solution
- **Parameter Manager**: Manage configuration parameters across services
- **SaaS Service Management**: Tools for managing SaaS offerings on Google Cloud
- **Secure Source Manager**: Google's secure, fully-managed source control service
- **Security Posture**: Assess and improve your cloud security posture
- **Storage Batch Operations**: Perform batch operations on Cloud Storage resources

## Enhanced Documentation

We've also released enhanced user documentation to help you get the most out of these providers. Check out our comprehensive docs:

- [Firebase Provider](https://firebase-provider.stackql.io/)
- [Google Provider](https://google-provider.stackql.io/)
- [Google Admin Provider](https://googleadmin-provider.stackql.io/)
- [Google Workspace Provider](https://googleworkspace-provider.stackql.io/)

## Getting Started

To start using these updated providers, simply pull the latest version from `stackql shell` or `stackql registry` command:

```sql
registry pull google;
registry pull googleadmin;
registry pull googleworkspace;
registry pull firebase;
```

Then you can begin querying your Google resources with SQL:

```sql
SELECT name, region, status 
FROM google.compute.instances
WHERE project = 'my-project';
```

## Use Cases for the Google Provider

The Google provider for StackQL opens up numerous possibilities:

1. **Infrastructure as Code**: Manage your Google resources alongside other cloud providers in a unified IaC approach, see [__`stackql-deploy`__](https://stackql-deploy.io/).

2. **Cost Optimization**: Identify unused resources and opportunities for cost savings.

3. **Security and Compliance**: Audit account roles, permissions, and access patterns to ensure compliance with security policies.

4. **Performance Monitoring**: Track query performance, warehouse utilization, and identify optimization opportunities.

5. **Cross-Provider Orchestration**: Build workflows that span Google and other cloud providers, enabling sophisticated data and infrastructure pipelines.

6. **Automated Reporting**: Create automated reports on Google usage, performance, and costs.

⭐ us on [__GitHub__](https://github.com/stackql/stackql) and join our community!