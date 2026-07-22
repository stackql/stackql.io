---
slug: updated-cloudflare-provider-available
title: Cloudflare Provider - May 2026
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-cloudflare-provider-featured-image.png"
description: Initial release of the StackQL Cloudflare provider with 108 services, 1259 resources, and 2840 operations spanning Zero Trust, Workers, R2, DNS, Zones, Radar, AI, Stream, Magic Transit, and more.
keywords: [stackql, cloudflare, provider, zero trust, workers, r2, dns, radar, ai, stream, magic transit]
tags: [stackql, cloudflare, provider, zero trust, workers, r2, dns, radar, ai, stream, magic transit]
---

We've released the latest version of the [__StackQL Cloudflare provider__](https://stackql.io/docs/providers), including the full Cloudflare V4 API surface under SQL with __108 services__, __1259 resources__, and __2840 operations__.

## Service highlights

Key services in this release include:

| Service | Resources | Operations | What it covers |
|---------|----------:|-----------:|----------------|
| `zero_trust` | 155 | 427 | Access apps, gateway, tunnels, identity providers, device posture |
| `radar` | 266 | 273 | Internet measurement: BGP, traffic, attacks, AS info, quality |
| `cloudforce_one` | 56 | 130 | Threat intel: requests, priorities, scans, threat events |
| `ai` | 114 | 116 | Workers AI inference across 100+ models |
| `workers` | 39 | 107 | Scripts, deployments, KV, Durable Objects, queues, cron triggers |
| `magic_transit` | 21 | 90 | Magic WAN: sites, connectors, GRE/IPsec tunnels, routes |
| `zones` | 26 | 64 | Zone lifecycle, settings, SSL/TLS, page rules, custom hostnames |
| `realtime_kit` | 27 | 63 | RealtimeKit meetings, sessions, recordings, presets |
| `dns` | 19 | 58 | DNS records, zone transfers, firewall, analytics |
| `load_balancers` | 12 | 54 | Pools, monitors, regions, search |
| `aisearch` | 20 | 48 | AI Search indexes, ingest jobs, models |
| `email_security` | 15 | 47 | Area 1: alerts, allow/block policies, investigation |
| `firewall` | 10 | 47 | WAF rules, packages, lockdowns, access rules, UA rules |
| `streams` | 13 | 47 | Stream video uploads, live inputs, signed URLs, captions |
| `api_gateway` | 17 | 43 | API discovery, schemas, operations, settings |
| `r2` | 17 | 42 | Buckets, lifecycle, CORS, custom domains, event notifications |

## Authentication

Authenticate with a Cloudflare API token via the `CLOUDFLARE_API_TOKEN` environment variable:

```bash
export CLOUDFLARE_API_TOKEN=...
```

## Example queries

List your zones:

```sql
SELECT id, name, status, plan
FROM cloudflare.zones.zones
WHERE account.id = '<account_id>';
```

Inspect Workers scripts in an account:

```sql
SELECT id, modified_on, usage_model
FROM cloudflare.workers.scripts
WHERE account_id = '<account_id>';
```

List DNS records for a zone:

```sql
SELECT name, type, content, ttl, proxied
FROM cloudflare.dns.records
WHERE zone_id = '<zone_id>';
```

Pull a Radar BGP summary:

```sql
SELECT *
FROM cloudflare.radar.routes_stats;
```

## Binary and non-JSON responses

A number of Cloudflare endpoints return binary or plain-text payloads - PDF LOAs, PNG screenshots, raw script source, CSV exports. The provider wraps these as a one-row table with a `contents` column so they're still SELECT-able:

```sql
SELECT contents
FROM cloudflare.browser_rendering.screenshot
WHERE account_id = '<account_id>' AND url = 'https://example.com';
```

## Get started

Pull the provider from the public registry:

```bash
registry pull cloudflare
```

Then start querying. Let us know what you build. Star us on [__GitHub__](https://github.com/stackql/stackql).
