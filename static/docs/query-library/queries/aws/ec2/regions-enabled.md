---
title: Enabled AWS regions
description: Lists AWS regions with their opt-in status; exclude not-opted-in regions from sweeps.
format: md
verb: select
status: stable
providers: [aws]
services: [ec2]
tags: [aws, ec2, regions, inventory]
keywords: [enabled regions, region sweep, opt-in status]
intent_keywords:
  - list enabled aws regions
  - which regions are enabled
  - enumerate aws regions
auth: [AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY]
permissions: ["ec2:DescribeRegions"]
params:
  - name: seed_region
    type: identifier
    required: false
    default: us-east-1
    description: Routing region for the describe-regions call; any enabled region works
    example: us-east-1
outputs:
  - name: regionName
    type: string
    description: Region identifier
  - name: optInStatus
    type: string
    description: opt-in-not-required, opted-in or not-opted-in
cost:
  fan_out: none
  expensive: false
---

Returns every AWS region visible to the account along with its opt-in status.
Run this first when planning any multi-region sweep: querying a region the
account has not opted into fails, so the result set here defines the valid
fan-out list for every other AWS query.

## Query

```sql
SELECT regionName, optInStatus FROM aws.ec2_native.regions WHERE region = '{{seed_region}}';
```

## Notes

region is a routing parameter, not a filter. Exclude rows with optInStatus =
'not-opted-in' before fanning out with WHERE region IN (...).
