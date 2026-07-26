---
title: Databricks workspaces in an account
description: Lists all Databricks workspaces in an account with cloud, region and provisioning status.
format: md
verb: select
status: draft
providers: [databricks_account]
services: [provisioning]
tags: [databricks, workspaces, inventory]
keywords: [workspace list, databricks account, workspace inventory]
intent_keywords:
  - list databricks workspaces
  - what workspaces exist in my databricks account
  - databricks workspace inventory
auth: [DATABRICKS_CLIENT_ID, DATABRICKS_CLIENT_SECRET]
params:
  - name: account_id
    type: string
    required: true
    pattern: "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
    description: Databricks account id (GUID)
    example: 00000000-0000-0000-0000-000000000000
outputs:
  - name: workspace_id
    type: integer
    description: Numeric workspace id
  - name: workspace_name
    type: string
    description: Workspace display name
  - name: cloud
    type: string
    description: aws, azure or gcp
  - name: aws_region
    type: string
    description: Region for AWS-hosted workspaces
  - name: location
    type: string
    description: Region for GCP-hosted workspaces
  - name: workspace_status
    type: string
    description: RUNNING, PROVISIONING, FAILED, etc.
  - name: pricing_tier
    type: string
    description: Workspace pricing tier
cost:
  fan_out: account
  expensive: false
---

Lists every workspace in a Databricks account across clouds, with provisioning
status and placement. This is the account-level inventory entry point; the
workspace_id values key all per-workspace follow-ups. Auth is a service
principal with OAuth client credentials at the account level.

## Query

```sql
SELECT workspace_id, workspace_name, cloud, aws_region, location, workspace_status, pricing_tier FROM databricks_account.provisioning.workspaces WHERE account_id = '{{account_id}}';
```

## Notes

The region column differs by cloud: aws_region for AWS workspaces, location
for GCP, and azure_workspace_info for Azure. Per-workspace resources (clusters,
jobs, catalogs) live in the separate databricks_workspace provider keyed by
deployment_name.
