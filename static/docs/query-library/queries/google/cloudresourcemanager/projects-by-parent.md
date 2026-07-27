---
title: GCP projects under an org or folder
description: Lists projects under a parent organization or folder; audit only ACTIVE projects.
format: md
verb: select
status: stable
providers: [google]
services: [cloudresourcemanager]
tags: [google, gcp, projects, inventory]
keywords: [project list, org descent, gcp projects]
intent_keywords:
  - list gcp projects
  - projects in my organization
  - enumerate google projects
auth: [GOOGLE_CREDENTIALS]
permissions: [resourcemanager.projects.list]
params:
  - name: parent
    type: string
    required: true
    pattern: "^(organizations|folders)/[0-9]+$"
    description: "Parent container: organizations/<org_id> or folders/<folder_id>"
    example: organizations/123456789012
outputs:
  - name: projectId
    type: string
    description: Project identifier
  - name: parent
    type: string
    description: Parent organization or folder
  - name: state
    type: string
    description: ACTIVE or DELETE_REQUESTED
cost:
  fan_out: none
  expensive: false
---

Lists the GCP projects directly under one parent container (an organization or
a folder). Project enumeration is the entry point for any org-wide GCP audit:
the projectId values returned here are the fan-out dimension for per-project
resource queries.

## Query

```sql
SELECT projectId, parent, state FROM google.cloudresourcemanager.projects WHERE parent = '{{parent}}';
```

## Notes

Recurse into ACTIVE folders via google.cloudresourcemanager.folders with the
same parent form to descend a whole organization.
