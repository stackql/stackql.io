---
title: GCP service accounts in a project
description: Lists IAM service accounts in one project with email, display name and disabled state.
format: md
verb: select
status: draft
providers: [google]
services: [iam]
tags: [google, gcp, iam, identity, security, inventory]
keywords: [service account list, robot accounts, sa inventory]
intent_keywords:
  - list gcp service accounts
  - what service accounts exist
  - service account inventory
auth: [GOOGLE_CREDENTIALS]
permissions: [iam.serviceAccounts.list]
params:
  - name: project
    type: identifier
    required: true
    description: GCP project id
    example: my-project
outputs:
  - name: email
    type: string
    description: Service account email (the principal identifier)
  - name: displayName
    type: string
    description: Human display name
  - name: disabled
    type: boolean
    description: True when the account is disabled
  - name: uniqueId
    type: string
    description: Stable numeric id
cost:
  fan_out: project
  expensive: false
  notes: One list call per project when swept org-wide
related: [google/cloudresourcemanager/projects-by-parent]
---

Lists the IAM service accounts in a single project. Service accounts are the
workload principals in GCP, so this enumeration is the starting point for key
hygiene and privilege audits. Note the parameter name in this API is
projectsId, not project.

## Query

```sql
SELECT email, displayName, disabled, uniqueId FROM google.iam.service_accounts WHERE projectsId = '{{project}}';
```

## Notes

For key hygiene, follow up per account with google.iam.service_accounts_keys
filtered by the account email to find user-managed keys and their ages.
