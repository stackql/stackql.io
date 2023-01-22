---
slug: sumologic-provider-for-stackql-now-available
title: Sumologic Provider for StackQL Now Available
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-sumologic-provider-featured-image.png"
description: The StackQL provider for Sumologic is now available, allowing you to query, create, update and delete Sumologic collectors, sources, and more.
keywords: [stackql, sumologic, multicloud, monitoring, logging, observability, cloud security, analysis, analytics]
tags: [stackql, sumologic, multicloud, monitoring, logging, observability, cloud security, analysis, analytics]
---

The StackQL Sumologic provider is now available in the public [StackQL Provider Registry](https://github.com/stackql/stackql-provider-registry).  Docs are available at  [__sumologic provider docs__](https://sumologic-docs.stackql.io/providers/sumologic).  

> [StackQL](https://github.com/stackql/stackql) is an intelligent API client which uses SQL as a front-end language.  StackQL can be used for querying cloud and SaaS providers, as well as provisioning and lifecycle operations.  

The StackQL Sumo provider can query, create, update and delete Sumologic collectors and sources, view and manage ingest budgets, health events, dashboards, user and account access and activity, and more.  

Some example queries include:   

```sql
SELECT id, name FROM sumologic.collectors.collectors WHERE region = 'au';
```
or using built-in functions to simplify and format query outputs, such as:      

```sql
SELECT alive, datetime(lastSeenAlive/1000, 'unixepoch') AS lastSeenAliveUtc,
datetime(lastSeenAlive/1000, 'unixepoch', 'localtime') AS lastSeenAliveLocal
FROM sumologic.collectors.collectors
WHERE region = 'au' AND id = 116208196;
```
another example...   

```sql
SELECT id, email,
firstName || ' ' || lastName AS fullName,
isMfaEnabled,
lastLoginTimestamp,
round(julianday('now') - julianday(lastLoginTimestamp), 0) as daysSinceLastLogin
FROM sumologic.users.users WHERE region = 'au';
```

An example using StackQL with the Sumologic provider to query users and roles and join the results to get a list of users and their roles:   

```sql
SELECT u.email as email, r.name AS role
FROM sumologic.users.users u
JOIN sumologic.roles.roles r 
ON JSON_EXTRACT(u.roleIds, '$[0]') = r.id
WHERE u.region = 'au' AND r.region = 'au';
```

An example using StackQL and Jupyter is shown here (see [__stackql/stackql-jupyter-demo__](https://github.com/stackql/stackql-jupyter-demo)):  

[![Use StackQL and Jupyter to query SumoLogic](/img/blog/sumologic-jupyter-stackql.png)](/img/blog/sumologic-jupyter-stackql.png)

StackQL can also be used to provision objects in Sumologic, the following query can be used to create a collector for instance:  

```sql
INSERT INTO sumologic.collectors.collectors(region, data__collector)
SELECT 'au',
'{ "collectorType":"Hosted", "name":"My Hosted Collector", "description":"An example Hosted Collector", "category":"HTTP Collection" }';
```

Let us know what you think!
