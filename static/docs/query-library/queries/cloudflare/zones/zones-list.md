---
title: Cloudflare zones
description: Lists all Cloudflare zones visible to the token with status and pause state.
format: md
verb: select
status: draft
providers: [cloudflare]
services: [zones]
tags: [cloudflare, dns, zones, inventory]
keywords: [zone list, domains on cloudflare, dns zones]
intent_keywords:
  - list cloudflare zones
  - what domains are on cloudflare
  - cloudflare zone inventory
auth: [CLOUDFLARE_API_TOKEN]
params: []
outputs:
  - name: id
    type: string
    description: Zone identifier (needed by every per-zone API)
  - name: name
    type: string
    description: Zone apex domain name
  - name: status
    type: string
    description: active, pending, initializing or moved
  - name: paused
    type: boolean
    description: True when Cloudflare is bypassed for the zone
cost:
  fan_out: account
  expensive: false
---

Lists every Cloudflare zone the API token can see, across all accounts the
token is scoped to. The zone id returned here is the key for every per-zone
follow-up (DNS records, settings, firewall rules).

## Query

```sql
SELECT id, name, status, paused FROM cloudflare.zones.zones;
```

## Notes

No parameters: scoping comes entirely from the token. A zone with status
active but paused = true is proxied through DNS only, with Cloudflare features
bypassed.
