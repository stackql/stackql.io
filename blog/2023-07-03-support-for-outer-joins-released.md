---
slug: support-for-outer-joins-released
title: Support for Outer Joins Released for StackQL
hide_table_of_contents: false
authors:	
  - kieranrimmer
image: "/img/blog/stackql-featured-image.png"
description: Announcing the addition of support for OUTER JOIN operations in StackQL queries.
keywords: [stackql, analytics, reporting, dashboards, cloud security, cspm]
tags: [stackql, analytics, reporting, dashboards, cloud security, cspm]
---

:::info

[__`stackql`__](https://github.com/stackql/stackql) is a dev tool that allows you to query, manage, and perform analytics against cloud and SaaS resources in real time using SQL, which developers and analysts can use for CSPM, assurance, user access management reporting, IaC, XOps and more.

:::

We are pleased to announce the addition of support for `OUTER JOIN` operations in StackQL queries.  This is a significant addition to the language, and we are excited to see what our users will do with it!

:::info

An `OUTER JOIN` is a type of `JOIN` operation that returns all records from one table (or StackQL resource) and only those records from a second table or resource where the joined fields are equal (i.e. the `JOIN` condition is met).  If there is no match, the missing side of the `JOIN` is filled with `NULL` values.

`OUTER JOIN` operations are important because they allow you to combine data from two or more resources (within a StackQL provider or across StackQL providers), even when there is no match between the two resources.  This is a common scenario when performing analytics and reporting on user access management (for example between an IdP (like Okta) and a resource provider like AWS or Google). 

:::

## Using `OUTER JOIN` operations in StackQL

If you wanted to find all users in your AWS account that have not logged in to their account in the last 20 days, and compare that to the same information for users in your Google Workspace account, you could use an `OUTER JOIN` operation to do this.  The following query:

```sql
select 
   aws_users.UserName as aws_user_name
  ,aws_users.PasswordLastUsed as aws_last_Login_time
  ,CASE 
    WHEN aws_users.PasswordLastUsed = '' then 'false' 
    WHEN ( strftime('%Y-%m-%d %H:%M:%SZ', aws_users.PasswordLastUsed) > ( datetime('now', '-20 days' ) ) ) then 'true' 
    else 'false' end as aws_is_active
  ,json_extract(google_users.name, '$.fullName') as google_user_name
  ,google_users.lastLoginTime as google_last_Login_time 
  ,CASE 
    WHEN google_users.lastLoginTime is null then 'false' 
    WHEN google_users.lastLoginTime = '' then 'false' 
    WHEN ( strftime('%Y-%m-%d %H:%M:%SZ', google_users.lastLoginTime) > ( datetime('now', '-20 days' ) ) ) then 'true' 
    else 'false' end as google_is_active
from 
  aws.iam.users aws_users 
  LEFT OUTER JOIN 
  googleadmin.directory.users google_users 
  ON lower(substr(aws_users.UserName, 1, 5)) = lower(substr(json_extract(google_users.name, '$.fullName'), 1, 5)) 
WHERE aws_users.region = 'us-east-1' AND google_users.domain = 'stackql.io'
;
```

would produce a result like this:

```
|------------------------|----------------------|---------------|------------------|--------------------------|------------------|
|     aws_user_name      | aws_last_Login_time  | aws_is_active | google_user_name |  google_last_Login_time  | google_is_active |
|------------------------|----------------------|---------------|------------------|--------------------------|------------------|
| demo-stackql-cicd-user | null                 | false         | null             | null                     | false            |
|------------------------|----------------------|---------------|------------------|--------------------------|------------------|
| github_actions         | null                 | false         | null             | null                     | false            |
|------------------------|----------------------|---------------|------------------|--------------------------|------------------|
| jeffrey.aven           | 2023-06-30T04:29:14Z | true          | null             | null                     | false            |
|------------------------|----------------------|---------------|------------------|--------------------------|------------------|
| kieran.rimmer          | 2023-06-03T08:40:49Z | false         | Kieran Rimmer    | 2023-06-23T06:01:46.000Z | true             |
|------------------------|----------------------|---------------|------------------|--------------------------|------------------|
| ...                    | ...                  | ...           | ...              | ...                      | ...              |
|------------------------|----------------------|---------------|------------------|--------------------------|------------------|
```

Currently only `LEFT OUTER JOIN` sppport is available, but we will be adding support for `RIGHT OUTER JOIN` and `FULL OUTER JOIN` in the near future.  Stay tuned!