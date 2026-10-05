---
slug: confluent-provider-may-2026-update
title: Confluent Provider Update - May 2026
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-confluent-provider-featured-image.png"
description: Update to the StackQL Confluent provider adding eight new services including ccl, ccpm, endpoints, pipelines, share_group, streams_group, tableflow, and usm, along with 40 additional resources across existing services.
keywords: [stackql, confluent, provider, kafka, tableflow, stream designer, share groups, unified stream manager, ccpm]
tags: [stackql, confluent, provider, kafka, tableflow, stream-designer]
---

We've released an update to the [__StackQL Confluent provider__](https://confluent-provider.stackql.io/) adding eight new services and 40 additional resources across existing services.

## New Services

The eight new services added in this update are:

| Service | Description |
|---------|-------------|
| `ccl` | Custom Code Logging - manage log topics that capture stdout/stderr and worker process logs from custom connectors running in Confluent Cloud |
| `ccpm` | Custom Connect Plugin Management - upload, version, and manage custom connector plugins at the environment level, including plugin version resources for JAR/ZIP artifacts |
| `endpoints` | Manage PrivateLink access points and private network endpoints used to reach Confluent Cloud clusters and serverless products over private networking |
| `pipelines` | Manage Stream Designer pipelines - the visual SQL/ksqlDB pipeline builder for connecting sources, transforms, and sinks across Kafka topics |
| `share_group` | Manage Kafka share groups (KIP-932 / Queues for Kafka), which provide queue-like consumption semantics with per-message acknowledgement and consumer parallelism beyond partition count |
| `streams_group` | Manage Kafka Streams groups - the broker-side coordination resource for Kafka Streams applications introduced alongside the next-generation consumer rebalance protocol |
| `tableflow` | Materialize Kafka topics as Apache Iceberg or Delta Lake tables, including catalog integrations, storage configuration, and table maintenance settings |
| `usm` | Unified Stream Manager - register and govern self-managed Confluent Platform clusters from Confluent Cloud, including agent deployment and hybrid cluster monitoring |

## Updates

This release also adds 40 additional resources across existing services, expanding coverage for:

- `kafka` - additional cluster configuration and topic-level resources
- `connect` - new connector status, offset, and task management resources
- `flink` - expanded coverage for Flink statements, compute pools, and artifacts
- `iam` - new resources for service accounts, identity providers, and role bindings
- `networking` - additional resources for transit gateways, peerings, and DNS forwarders
- `schema_registry` - new resources for schema exporters, modes, and compatibility
- `billing` - new cost and usage resources
- `metrics` - additional query and descriptor resources

## Get Started

Pull the latest Confluent provider:

```bash
stackql registry pull confluent
```

Visit us on [__GitHub__](https://github.com/stackql/stackql) and let us know how you're using it.
