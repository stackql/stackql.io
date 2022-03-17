---
slug: introducing-the-stackql-provider-registry
title: Introducing the StackQL Provider Registry
author: Jeffrey Aven
author_title: Cloud Consultant
author_url: https://github.com/stackql
author_image_url: https://s.gravatar.com/avatar/f96573d092470c74be233e1dded5376f?s=80
image: "/img/blog/stackql-registry-featured-image.png"
description: The StackQL provider registry allows contributors to add support for different providers (major cloud, alt cloud and SaaS providers) using a no-code approach.
keywords: [stackql, stackql provider registry, multicloud, asset management, cloud security]
tags: [stackql, stackql provider registry, multicloud, asset management, cloud security]
---

> Multi cloud visibility, SecOps, FinOps, DevOps made easy  

Today marks a significant epoch in the evolution of the InfraQL/StackQL project.  The StackQL provider registry allows contributors to add support for different providers (major cloud, alt cloud and SaaS providers) using a no-code approach.  Developers simply add extensions to the providers OpenAPI spec using configuration documents (currently supporting `yaml` and `json` – with future support for `toml` and `hcl`).  These extensions allow StackQL to map an ORM to provider services, resources, and methods.  

For example, for a future AWS provider you could run discovery commands such as:  

```sql
SHOW SERVICES IN aws;
/* shows the available services in AWS */
SHOW RESOURCES IN aws.ec2;
/* shows the available resources in the AWS EC2 service */
DESCRIBE aws.ec2.instances;
/* show available attributes in the aws.ec2.instances resource schema */
SHOW METHODS IN aws.ec2.instances;
/* shows available lifecycle methods – such as start, stop, etc which can be involved using the EXEC command */
```

Or create a new EC2 instance using:  

```sql
INSERT INTO aws.ec2.instances SELECT …;
```

View and report on instances and their properties using:  

```sql
SELECT col(s) FROM aws.ec2.instances WHERE …;
```

Or clean up resources using:  

```sql
DELETE FROM aws.ec2.instances WHERE …;
```

The StackQL beta version supporting the provider registry is available for [__Mac (arm and amd)__](https://releases.stackql.io/stackql/latest/stackql_darwin_multiarch.pkg) and [__Linux__](https://releases.stackql.io/stackql/latest/stackql_linux_amd64.zip), with a Windows version coming in the next few weeks.  

Providers are currently available for Google and Okta, see [__StackQL Provider Registry repo__](https://github.com/stackql/stackql-provider-registry) and [__Developer Guide__](https://github.com/stackql/stackql/blob/main/docs/registry_contribution.md).  We are encouraging developers to contribute – we would be happy to assist, just raise an issue or a PR.