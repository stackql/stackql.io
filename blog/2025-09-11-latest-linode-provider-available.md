---
slug: latest-linode-provider-available
title: Latest Linode Provider for StackQL Available
hide_table_of_contents: true
authors:	
  - jeffreyaven
image: "/img/blog/stackql-linode-provider-featured-image.png"
description: Query and interact with Linode resources using SQL.
keywords: [stackql, linode, iac, analytics]
tags: [stackql, linode, iac, analytics]
---

Latest release or the Linode provider for StackQL is available, enabling SQL-based querying and management of Linode cloud resources.

## Provider Overview

The Linode provider offers comprehensive access to 20 services covering the entire Linode platform, with 136 resources and 425 methods, allowing you to extract data and insights from your Linode environment using familiar SQL syntax.

## Key Services

The provider includes access to:

- **Compute**: Manage Linode instances and configurations
- **Storage**: Control volumes and object storage
- **Networking**: Handle IPs, VLANs, VPCs, and firewall rules
- **Kubernetes**: Administer LKE clusters and node pools
- **Databases**: Manage Linode's database service
- **Account & Billing**: Access account information and billing details

## Example Queries

### Get Account Information
```sql
SELECT 
  company,
  country,
  balance,
  balance_uninvoiced,
  active_since
FROM linode.account.account;
```

### List Database Engines
```sql
SELECT
  id,
  engine,
  version
FROM linode.databases.engines;
```

### View Regional Availability
```sql
SELECT
  available,
  plan,
  region
FROM linode.regions.availability;
```

### List Linode Instances
```sql
SELECT
  id,
  label,
  region,
  status,
  type,
  ipv4,
  tags
FROM linode.linode.instances;
```

## Getting Started

To use the Linode provider, simply register it and set your authentication:

```sql
-- Pull the provider
registry pull linode;

-- Set your authentication
SET linode.global.auth.credentialsenvvar = 'LINODE_TOKEN';
```

⭐ us on [__GitHub__](https://github.com/stackql/stackql) and join our community!