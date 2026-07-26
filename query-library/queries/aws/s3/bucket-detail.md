---
title: S3 bucket security detail
description: "Full security attributes for one bucket: public access block, encryption, versioning, ownership."
format: md
verb: select
status: stable
providers: [aws]
services: [s3]
tags: [aws, s3, storage, security]
keywords: [bucket encryption, public access block, bucket versioning]
intent_keywords:
  - is my bucket public
  - bucket security settings
  - s3 bucket detail
auth: [AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY]
params:
  - name: region
    type: identifier
    required: true
    description: Routing region
    example: us-east-1
  - name: bucket_name
    type: string
    required: true
    description: Bucket name (the data__Identifier key)
    example: my-bucket
outputs:
  - name: bucket_name
    type: string
    description: Bucket name
  - name: region
    type: string
    description: Region the bucket lives in
  - name: public_access_block_configuration
    type: object
    description: Block-public-access settings
  - name: bucket_encryption
    type: object
    description: Default encryption configuration
  - name: versioning_configuration
    type: object
    description: Versioning state
  - name: ownership_controls
    type: object
    description: Object ownership rules
cost:
  fan_out: none
  expensive: true
  notes: One request per bucket when iterated over an inventory
related: [aws/s3/buckets-list]
---

Returns the full security posture of a single S3 bucket: block-public-access
configuration, default encryption, versioning state and ownership controls. Use
it to answer "is this bucket public" style questions, or iterate it over the
output of the cheap enumeration entry for an account-wide audit.

## Query

```sql
SELECT bucket_name, region, public_access_block_configuration, bucket_encryption, versioning_configuration, ownership_controls FROM aws.s3.buckets WHERE region = '{{region}}' AND data__Identifier = '{{bucket_name}}';
```

## Notes

Keyed read: one request per bucket. A 404 with an empty result means the bucket
does not exist - a valid answer, not a failure.
