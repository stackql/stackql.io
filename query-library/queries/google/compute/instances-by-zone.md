---
title: GCE instances in a zone
description: Lists Compute Engine instances in one project and zone with status, machine type and creation time.
format: md
verb: select
status: draft
providers: [google]
services: [compute]
tags: [google, gcp, compute, inventory]
keywords: [gce inventory, vm list, compute instances]
intent_keywords:
  - list gce instances
  - what vms are running in gcp
  - compute engine instance inventory
auth: [GOOGLE_CREDENTIALS]
params:
  - name: project
    type: identifier
    required: true
    description: GCP project id
    example: my-project
  - name: zone
    type: identifier
    required: true
    description: Compute zone
    example: us-central1-a
outputs:
  - name: name
    type: string
    description: Instance name
  - name: status
    type: string
    description: RUNNING, TERMINATED, SUSPENDED, etc.
  - name: machineType
    type: string
    description: Machine type URL; the type name is the last path segment
  - name: zone
    type: string
    description: Zone URL
  - name: creationTimestamp
    type: string
    description: Creation timestamp
cost:
  fan_out: project
  expensive: false
  notes: One list call per project and zone when swept org-wide
related: [google/cloudresourcemanager/projects-by-parent]
---

Lists Compute Engine instances in a single project and zone. Compute Engine
lists are zonal, so an org-wide inventory iterates projects (from
google/cloudresourcemanager/projects-by-parent) and zones within each project.

## Query

```sql
SELECT name, status, machineType, zone, creationTimestamp FROM google.compute.instances WHERE project = '{{project}}' AND zone = '{{zone}}';
```

## Notes

machineType and zone are returned as full resource URLs; take the last path
segment for the short name. TERMINATED in GCE means stopped, not deleted -
stopped instances still appear here.
