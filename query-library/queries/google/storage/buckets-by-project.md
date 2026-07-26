---
title: GCS buckets in a project
description: Lists Cloud Storage buckets in one project with location, storage class and creation time.
format: md
verb: select
status: draft
providers: [google]
services: [storage]
tags: [google, gcp, storage, inventory]
keywords: [gcs inventory, bucket list, cloud storage buckets]
intent_keywords:
  - list gcs buckets
  - what buckets exist in my project
  - cloud storage bucket inventory
auth: [GOOGLE_CREDENTIALS]
params:
  - name: project
    type: identifier
    required: true
    description: GCP project id
    example: my-project
outputs:
  - name: name
    type: string
    description: Bucket name (globally unique)
  - name: location
    type: string
    description: Bucket location (region or multi-region)
  - name: storageClass
    type: string
    description: STANDARD, NEARLINE, COLDLINE or ARCHIVE
  - name: timeCreated
    type: string
    description: Creation timestamp
cost:
  fan_out: project
  expensive: false
  notes: One list call per project when swept org-wide
related: [google/cloudresourcemanager/projects-by-parent]
---

Lists every Cloud Storage bucket in a single project. Bucket listing is
per-project: for an org-wide storage inventory, iterate the projects from
google/cloudresourcemanager/projects-by-parent.

## Query

```sql
SELECT name, location, storageClass, timeCreated FROM google.storage.buckets WHERE project = '{{project}}';
```

## Notes

For bucket security posture (public access prevention, uniform bucket-level
access) select the iamConfiguration field from the same resource and inspect it
with JSON extraction.
