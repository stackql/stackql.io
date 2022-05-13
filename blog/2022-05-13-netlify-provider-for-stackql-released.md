---
slug: netlify-provider-for-stackql-released
title: Netlify Provider for StackQL Released
authors:	
  - jeffreyaven
draft: false
image: "/img/blog/stackql-provider-for-netlify-released.png"
description: The StackQL provider for Netlify has been released, you can use this to query sites, builds and more in Netlify.
keywords: [netlify, stackql, stackql provider registry, multicloud, asset management, cloud security]
tags: [netlify, stackql, stackql provider registry, multicloud, asset management, cloud security]
---

Excited to announce the release of the __Netlify provider for StackQL__.  

> StackQL allows you to query and interact with your cloud and SaaS assets using a simple SQL framework

The netlify provider can be used to query, provision, de-provision or update __sites__, __builds__, __deploys__, __functions__, __identities__, __domain_names__ and more.  

Here are the steps to get started with the Netlify provider:  

### Setup

1. Create a personal access token for Netlify (if you don't have one already), go to 
[https://app.netlify.com/user/applications/personal](https://app.netlify.com/user/applications/personal).

2. Export the token to a variable and supply this as the provider authentication for StackQL:  

```bash
export NETLIFY_TOKEN=your_personal_access_token
AUTH_STR='{ "netlify": { "type": "api_key", "valuePrefix": "Bearer ", "credentialsenvvar": "NETLIFY_TOKEN" } }'
./stackql shell --auth="${AUTH_STR}"
```

### Install the `netlify` provider

3. Pull the Netlify provider (you only need to do this the first time you use the provider or when you are updating), the following command can be run from the StackQL interactive shell (`stackql shell`) or using `stackql exec`:  

```
registry pull netlify v0.1.0;
```

### Show available services in `netlify`

4. (Optional) Show the available services in the Netlify provider:  

```
stackql >> show services in netlify;

+----------------------+---------------+-----------------------+
|          id          |     name      |       title           |
|----------------------|---------------|-----------------------|
| builds:v0.1.0        | builds        | Netlify site builds   |
|----------------------|---------------|-----------------------|
| deploys:v0.1.0       | deploys       | Netlify site deploys  |
|----------------------|---------------|-----------------------|
| user_accounts:v0.1.0 | user_accounts | Netlify user accounts |
|----------------------|---------------|-----------------------|
| ...                  | ...           | ...                   |
+----------------------+---------------+-----------------------+
```

### Explore resources in `netlify`

5. (Optional) Explore resources in a service in the Netlify provider:  

```
stackql  >> show resources in netlify.deploys;

+----------------+--------------------------------+
|      name      |               id               |
|----------------|--------------------------------|
| deploy         | netlify.deploys.deploy         |
|----------------|--------------------------------|
| deployKey      | netlify.deploys.deployKey      |
|----------------|--------------------------------|
| deployedBranch | netlify.deploys.deployedBranch |
+----------------+--------------------------------+
```

6. (Optional) List the available fields (or properties) of a resource:  

```
stackql  >> describe netlify.deploys.deploy;

+--------------------+---------+
|        name        |  type   |
|--------------------|---------|
| id                 | string  |
|--------------------|---------|
| name               | string  |
|--------------------|---------|
| url                | string  |
|--------------------|---------|
| review_id          | number  |
|--------------------|---------|
| commit_url         | string  |
|--------------------|---------|
| commit_ref         | string  |
|--------------------|---------|
| ...                | ...     |
+--------------------+---------+
```

7. (Optional) Show the available methods in a resource (the stuff you can do with a resource..):  

```
stackql  >> show methods in netlify.deploys.deploy;

+--------------------+--------------------+
|     MethodName     |   RequiredParams   |
|--------------------|--------------------|
| cancelSiteDeploy   | deploy_id          |
|--------------------|--------------------|
| createSiteDeploy   | site_id            |
|--------------------|--------------------|
| getSiteDeploy      | deploy_id, site_id |
|--------------------|--------------------|
| listSiteDeploys    | site_id            |
|--------------------|--------------------|
| lockDeploy         | deploy_id          |
|--------------------|--------------------|
| ...                | ...                |
+--------------------+--------------------+
```


::: tip
Methods prefixed by `list` and `get` are exposed via `SELECT` verbs, for example:  

```sql
SELECT id, name FROM netlify.deploys.deploy
WHERE site_id = 'ad26d902-9cb1-43be-90d9-284e8c7ac687';
```

this query accesses the `listSiteDeploys` method
:::

### Run some queries!

8. Run some queries...  

```
stackql  >> SELECT created_at, name, state, branch FROM netlify.deploys.deploy
>> WHERE site_id = 'ad26d902-9cb1-43be-90d9-284e8c7ac687'
>> ORDER BY created_at DESC LIMIT 2;

+--------------------------+------------+-------+-------------------------+
|        created_at        |    name    | state |         branch          |
|--------------------------|------------|-------|-------------------------|
| 2022-05-04T22:46:43.015Z | stackql-io | ready | main                    |
|--------------------------|------------|-------|-------------------------|
| 2022-05-04T22:39:34.958Z | stackql-io | ready | feature/content-updates |
+--------------------------+------------+-------+-------------------------+
```

You can also use StackQL to provision resources in Netlify, the methods you saw in step 7 that are prefixed by `create` or `insert`, can be accessed using `INSERT` statements in StackQL, similarly methods prefixed by `delete` or `remove` can be accessed using `DELETE` statements.  

more providers coming soon, if there is anything you are interested in specifically, get in contact and let us know.  

Welcome your feedback by getting in touch or raising issues at [__stackql/stackql-provider-registry__](https://github.com/stackql/stackql-provider-registry), give us some ⭐️ love while you are there!  

enjoy!