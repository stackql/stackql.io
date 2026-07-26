---
title: Azure subscriptions
description: Lists subscriptions visible to the credential, tenant-wide; audit only Enabled subscriptions.
format: md
verb: select
status: stable
providers: [azure]
services: [subscription]
tags: [azure, subscriptions, inventory]
keywords: [subscription list, tenant subscriptions]
intent_keywords:
  - list azure subscriptions
  - enumerate subscriptions
auth: [AZURE_TENANT_ID, AZURE_CLIENT_ID, AZURE_CLIENT_SECRET]
params: []
outputs:
  - name: subscriptionId
    type: string
    description: Subscription identifier
  - name: state
    type: string
    description: Enabled, Disabled, Deleted, PastDue or Warned
cost:
  fan_out: none
  expensive: false
---

Lists every Azure subscription visible to the credential across the tenant.
Subscription enumeration is the entry point for any tenant-wide Azure audit:
the subscriptionId values returned here are the fan-out dimension for
per-subscription resource queries.

## Query

```sql
SELECT subscriptionId, state FROM azure.subscription.subscriptions;
```

## Notes

For management-group scoping use azure.management_groups.descendants with
groupId.
