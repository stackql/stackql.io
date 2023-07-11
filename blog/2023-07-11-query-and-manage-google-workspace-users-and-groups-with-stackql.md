---
slug: query-and-manage-google-workspace-users-and-groups-with-stackql
title: Query and Manage Google Workspace Users and Groups with StackQL
hide_table_of_contents: false
authors:  
  - kieranrimmer
image: "/img/blog/stackql-googleadmin-provider-featured-image.png"
description: Announcing the release of the StackQL provider for the Google Workspace Admin API.
keywords: [stackql, google, google workspace, google admin, analytics, reporting, dashboards, cloud security, cspm]
tags: [stackql, google, google workspace, google admin, analytics, reporting, dashboards, cloud security, cspm]
---

:::info

[__`stackql`__](https://github.com/stackql/stackql) is a dev tool that allows you to query, manage, and perform analytics against cloud and SaaS resources in real time using SQL, which developers and analysts can use for CSPM, assurance, user access management reporting, IaC, XOps and more.

:::

The `googleadmin` StackQL provider is now available, which allows you to query, provision, or manage Google Workspace __users__, __groups__, __devices__, and more using StackQL.  The `googleadmin` provider can be used with the `google` provider or other cloud providers to generate entitlements reports (or user access reviews) where Google Workspace identites are used in identity federation or IAM bindings.  

The full documentation on how to use a Google service account for authentication to the googleadmin provider is available [here](https://googleadmin.stackql.io/providers/googleadmin/).  Information about the directory resources available and their fields and methods, is available in the [StackQL Provider Registry Docs](https://googleadmin.stackql.io/providers/googleadmin/directory/).  

### Simple Query

A simple query using the `googleadmin` provider is shown here:

```sql
SELECT
 primaryEmail, 
 lastLoginTime 
FROM 
 googleadmin.directory.users 
WHERE domain = 'stackql.io'
AND primaryEmail = 'javen@stackql.io';
```

which would return the following results...   

```
|------------------|--------------------------|                                                                                                                                                   
|   primaryEmail   |      lastLoginTime       |                                                                                                                                                   
|------------------|--------------------------|                                                                                                                                                   
| javen@stackql.io | 2023-07-08T23:30:31.000Z |                                                                                                                                                   
|------------------|--------------------------|  
```

### Example Query Using Built-In Functions

Here is an example using built-in functions in StackQL (more information about built-in functions is available in the [StackQL docs](https://stackql.io/docs)):    

```sql
SELECT
 primaryEmail,
 json_extract(name, '$.fullName') as full_name, 
 lastLoginTime 
FROM 
 googleadmin.directory.users 
WHERE domain = 'stackql.io'
AND primaryEmail = 'javen@stackql.io';
```

which would return results like this...  

```
|------------------|--------------|--------------------------|                                                                                                                                    
|   primaryEmail   |  full_name   |      lastLoginTime       |                                                                                                                                    
|------------------|--------------|--------------------------|                                                                                                                                    
| javen@stackql.io | Jeffrey Aven | 2023-07-08T23:30:31.000Z |                                                                                                                                    
|------------------|--------------|--------------------------|  
```

### Example Query Using Aggregate Functions

Here is an example of a summary query that could be useful:

```sql
SELECT
  isAdmin,
  COUNT(*) as num_admins
FROM 
 googleadmin.directory.users 
WHERE domain = 'stackql.io'
GROUP BY isAdmin
```

results in...  

```
|---------|------------|                                                                                                                                                                          
| isAdmin | num_admins |                                                                                                                                                                          
|---------|------------|                                                                                                                                                                          
| false   |          9 |                                                                                                                                                                          
|---------|------------|                                                                                                                                                                          
| true    |          2 |                                                                                                                                                                          
|---------|------------|  
```

### Entitlements Report Using a `LEFT JOIN` with the `google` provider

Using the `LEFT OUTER JOIN` capability with StackQL, you can generate entitlements or user access management reports that span across Google Workspace as an Identity Provider (IdP) and a Google Cloud resource (including Organizations, Folders, Projects, and resources), such as:  

```sql
SELECT 
  split_part(json_extract(iam.members,'$[0]'), ':', 2) as member, 
  iam.role as role,
  users.lastLoginTime
FROM google.cloudresourcemanager.organizations_iam_bindings iam
LEFT OUTER JOIN googleadmin.directory.users users
ON split_part(json_extract(iam.members,'$[0]'), ':', 2) = users.primaryEmail
WHERE users.domain = 'stackql.io' 
AND iam.organizationsId = 141318256085
AND users.primaryEmail = 'javen@stackql.io';
```

which would return...

```
|------------------|------------------------------|--------------------------|                                                                                                                    
|      member      |             role             |      lastLoginTime       |                                                                                                                    
|------------------|------------------------------|--------------------------|                                                                                                                    
| javen@stackql.io | roles/bigquery.resourceAdmin | 2023-07-08T23:30:31.000Z |                                                                                                                    
|------------------|------------------------------|--------------------------|                                                                                                                    
| javen@stackql.io | roles/logging.admin          | 2023-07-08T23:30:31.000Z |                                                                                                                    
|------------------|------------------------------|--------------------------|
```

Let us know what you think!