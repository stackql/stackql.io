---
title: S3 buckets cheap enumeration
description: Enumerates S3 bucket names and regions via the list-only resource; identifiers only, no detail.
format: md
verb: select
status: stable
providers: [aws]
services: [s3]
tags: [aws, s3, storage, inventory]
keywords: [bucket list, s3 inventory, list buckets]
intent_keywords:
  - list all s3 buckets
  - bucket inventory
  - enumerate buckets
auth: [AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY]
params:
  - name: region
    type: identifier
    required: true
    description: Routing region
    example: us-east-1
outputs:
  - name: bucket_name
    type: string
    description: Bucket name
  - name: region
    type: string
    description: Region the bucket lives in
cost:
  fan_out: none
  expensive: false
  notes: Single account-global list call
related: [aws/s3/bucket-detail]
---

Enumerates every S3 bucket in the account in a single call using the list-only
resource. This is the cheap inventory path: it returns identifiers only, without
the per-bucket security attributes.

## Query

```sql
SELECT bucket_name, region FROM aws.s3.buckets_list_only WHERE region = '{{region}}';
```

## Notes

S3 bucket listing is account-global; the region routes the request. Pair with
aws/s3/bucket-detail for per-bucket attributes, one request per bucket.
