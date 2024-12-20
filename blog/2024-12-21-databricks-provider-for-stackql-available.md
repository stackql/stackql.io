---
slug: databricks-provider-for-stackql-available
title: Databricks Provider for StackQL Available
hide_table_of_contents: false
authors:	
  - jeffreyaven
image: "/img/blog/stackql-databricks-provider-featured-image.png"
description: Query and interact with Databricks resources using SQL.
keywords: [stackql, databricks, spark, iac, analytics]
tags: [stackql, databricks, spark, iac, analytics]
---

We are pleased to announce the release of the Databricks provider for StackQL today.  The Databricks provider is two different providers, __`databricks_account`__ and __`databricks_workspace`__.  To get started, pull the providers from the registry as follows:  

```
registry pull databricks_account;
registry pull databricks_workspace;
``` 

## The `databricks_account` provider

The __`databricks_account`__ provider is used for account-level operations, including provisioning or managing users, groups, unity catalog metastores, workspaces, and account-level cloud resources used by workspaces (such as networking resources).  Services include:

```
stackql  >>show services in databricks_account;
|----------------------------|---------------|--------------------------------|
|             id             |     name      |             title              |
|----------------------------|---------------|--------------------------------|
| billing:v00.00.00000       | billing       | Account Billing                |
|----------------------------|---------------|--------------------------------|
| iam:v00.00.00000           | iam           | Identity and Access Management |
|----------------------------|---------------|--------------------------------|
| logging:v00.00.00000       | logging       | Log Delivery                   |
|----------------------------|---------------|--------------------------------|
| oauth:v00.00.00000         | oauth         | OAuth Integrations             |
|----------------------------|---------------|--------------------------------|
| provisioning:v00.00.00000  | provisioning  | Account Provisioning           |
|----------------------------|---------------|--------------------------------|
| settings:v00.00.00000      | settings      | Account Settings               |
|----------------------------|---------------|--------------------------------|
| unity_catalog:v00.00.00000 | unity_catalog | Unity Catalog                  |
|----------------------------|---------------|--------------------------------|
```

Some example databricks_account queries are shown here:

```
stackql  >>select *  from  databricks_account.iam.users where account_id = 'ebfcc5a9-9d49-4c93-b651-b3ee6cf1c9ce' and active = true;
|--------|--------------|-------------------------------------------------------------|------------|------------------|---------------------------------------------|---------------------------------------------|------------------|
| active | displayName  |                           emails                            | externalId |        id        |                    name                     |                    roles         
 |     userName     |
|--------|--------------|-------------------------------------------------------------|------------|------------------|---------------------------------------------|---------------------------------------------|------------------|
| true   | Jeffrey Aven | [{"primary":true,"type":"work","value":"javen@stackql.io"}] | null       | 5728205706991489 | {"familyName":"Aven","givenName":"Jeffrey"} | [{"type":"direct","value":"account_admin"}] | javen@stackql.io |
|--------|--------------|-------------------------------------------------------------|------------|------------------|---------------------------------------------|---------------------------------------------|------------------|
```
or..

```
stackql  >>SELECT applicationId,  displayName
stackql  >>FROM databricks_account.iam.service_principals, JSON_EACH(roles)
stackql  >>WHERE account_id = 'ebfcc5a9-9d49-4c93-b651-b3ee6cf1c9ce'
stackql  >>AND JSON_EXTRACT(json_each.value, '$.value') = 'account_admin';
|--------------------------------------|-------------|
|            applicationId             | displayName |
|--------------------------------------|-------------|
| 0b7b23de-3e7d-4432-812c-cf517e079a22 | stackql     |
|--------------------------------------|-------------|
```

or..

```
stackql  >>select
stackql  >>workspace_id,
stackql  >>workspace_name,
stackql  >>deployment_name,
stackql  >>workspace_status,
stackql  >>pricing_tier,
stackql  >>aws_region,
stackql  >>credentials_id,
stackql  >>storage_configuration_id
stackql  >>from
stackql  >>databricks_account.provisioning.workspaces where account_id = 'ebfcc5a9-9d49-4c93-b651-b3ee6cf1c9ce';
|------------------|----------------|-------------------|------------------|--------------|------------|--------------------------------------|--------------------------------------|
|   workspace_id   | workspace_name |  deployment_name  | workspace_status | pricing_tier | aws_region |            credentials_id            |       storage_configuration_id       |
|------------------|----------------|-------------------|------------------|--------------|------------|--------------------------------------|--------------------------------------|
| 1583879855205171 | stackql-test   | dbc-ddbc0f51-c9cf | RUNNING          | PREMIUM      | us-west-2  | dcacd875-c782-46ea-9d3e-8307975d758a | e52e029f-24bb-4a75-99c3-7796c202dd89 |
|------------------|----------------|-------------------|------------------|--------------|------------|--------------------------------------|--------------------------------------|
```

## The `databricks_workspace` provider

The `databricks_workspace` provider is used for workspace-level operations, such as provisioning and managing clusters, dashboards, and workflow jobs (including delta live table pipelines).  Services include:  

```
stackql  >>show services in databricks_workspace;
|------------------------------|-----------------|-----------------|
|              id              |      name       |      title      |
|------------------------------|-----------------|-----------------|
| apps:v24.12.00279            | apps            | Apps            |
|------------------------------|-----------------|-----------------|
| cleanrooms:v24.12.00279      | cleanrooms      | Cleanrooms      |
|------------------------------|-----------------|-----------------|
| compute:v24.12.00279         | compute         | Compute         |
|------------------------------|-----------------|-----------------|
| dbsql:v24.12.00279           | dbsql           | Dbsql           |
|------------------------------|-----------------|-----------------|
| deltalivetables:v24.12.00279 | deltalivetables | Deltalivetables |
|------------------------------|-----------------|-----------------|
| deltasharing:v24.12.00279    | deltasharing    | Deltasharing    |
|------------------------------|-----------------|-----------------|
| filemanagement:v24.12.00279  | filemanagement  | Filemanagement  |
|------------------------------|-----------------|-----------------|
| iam:v24.12.00279             | iam             | Iam             |
|------------------------------|-----------------|-----------------|
| lakeview:v24.12.00279        | lakeview        | Lakeview        |
|------------------------------|-----------------|-----------------|
| machinelearning:v24.12.00279 | machinelearning | Machinelearning |
|------------------------------|-----------------|-----------------|
| marketplace:v24.12.00279     | marketplace     | Marketplace     |
|------------------------------|-----------------|-----------------|
| realtimeserving:v24.12.00279 | realtimeserving | Realtimeserving |
|------------------------------|-----------------|-----------------|
| repos:v24.12.00279           | repos           | Repos           |
|------------------------------|-----------------|-----------------|
| secrets:v24.12.00279         | secrets         | Secrets         |
|------------------------------|-----------------|-----------------|
| unitycatalog:v24.12.00279    | unitycatalog    | Unitycatalog    |
|------------------------------|-----------------|-----------------|
| vectorsearch:v24.12.00279    | vectorsearch    | Vectorsearch    |
|------------------------------|-----------------|-----------------|
| workflows:v24.12.00279       | workflows       | Workflows       |
|------------------------------|-----------------|-----------------|
| workspace:v24.12.00279       | workspace       | Workspace       |
|------------------------------|-----------------|-----------------|
```

An example query could be:

```
stackql  >>select
stackql  >>cluster_id,
stackql  >>aws_attributes,
stackql  >>node_type_id,
stackql  >>state
stackql  >>from
stackql  >>databricks_workspace.compute.clusters
stackql  >>where deployment_name = 'dbc-ddbc0f51-c9cf';
|----------------------|---------------------------------------------------------------------------------------------------------|--------------|------------|
|      cluster_id      |                                             aws_attributes                                              | node_type_id |   state    |
|----------------------|---------------------------------------------------------------------------------------------------------|--------------|------------|
| 1218-233957-q9v9oi86 | {"availability":"SPOT_WITH_FALLBACK","first_on_demand":1,"spot_bid_price_percent":100,"zone_id":"auto"} | m5d.large    | TERMINATED |
|----------------------|---------------------------------------------------------------------------------------------------------|--------------|------------|
```

To use either provider, set the following environment variables (either locally or as secrets in your preferred CI tool):

- `DATABRICKS_ACCOUNT_ID` - a uuid representing your Databricks account id, you can get this from the Databricks UI
- `DATABRICKS_CLIENT_ID` - obtained after creating a service principal through the Databricks UI
- `DATABRICKS_CLIENT_SECRET` - obtained after creating a service principal secret through the Databricks UI, using the "Generate Secret" function

These are the same variables that Terraform, the Databricks SDKs, and CLI use.  

[__`stackql-deploy`__](https://stackql-deploy.io/docs/) examples coming soon, stay tuned!  

⭐ us on [__GitHub__](https://github.com/stackql/stackql) and join our community!