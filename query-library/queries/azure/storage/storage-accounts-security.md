---
title: Azure storage accounts security posture
description: Lists storage accounts in a subscription with public access, TLS and HTTPS-only settings flattened.
format: md
verb: select
status: draft
providers: [azure]
services: [storage]
tags: [azure, storage, security, inventory]
keywords: [storage account list, blob public access, tls version, https only]
intent_keywords:
  - list azure storage accounts
  - which storage accounts allow public access
  - storage account security settings
auth: [AZURE_TENANT_ID, AZURE_CLIENT_ID, AZURE_CLIENT_SECRET]
params:
  - name: subscription_id
    type: string
    required: true
    pattern: "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
    description: Azure subscription id (GUID)
    example: 00000000-0000-0000-0000-000000000000
outputs:
  - name: accountName
    type: string
    description: Storage account name
  - name: resourceGroupName
    type: string
    description: Resource group containing the account
  - name: location
    type: string
    description: Azure region
  - name: kind
    type: string
    description: StorageV2, BlobStorage, etc.
  - name: allow_blob_public_access
    type: string
    description: Whether containers may be configured for anonymous access
  - name: minimum_tls_version
    type: string
    description: Minimum TLS version accepted
  - name: supports_https_traffic_only
    type: string
    description: Whether plain HTTP is rejected
  - name: public_network_access
    type: string
    description: Enabled or Disabled at the account level
cost:
  fan_out: subscription
  expensive: false
  notes: One list call per subscription when swept tenant-wide
related: [azure/subscription/subscriptions-list]
---

Lists every storage account in a subscription with the security-relevant
properties already flattened to columns by the provider-defined view, avoiding
manual JSON extraction from the properties object. This one query answers the
common storage exposure asks: public access, TLS floor and HTTPS enforcement.

## Query

```sql
SELECT accountName, resourceGroupName, location, kind, allow_blob_public_access, minimum_tls_version, supports_https_traffic_only, public_network_access FROM azure.storage.vw_accounts_properties WHERE subscriptionId = '{{subscription_id}}';
```

## Notes

vw_accounts_properties is a provider-defined view over azure.storage.accounts
that flattens the properties object. allow_blob_public_access = true only means
containers may be configured for anonymous access; actual exposure also
requires a public container ACL.
