---
title: GitHub repositories in an organization
description: Lists all repositories in a GitHub organization with visibility, archive state and activity signals.
format: md
verb: select
status: stable
providers: [github]
services: [repos]
tags: [github, repos, inventory]
keywords: [repo list, org repositories, repository inventory]
intent_keywords:
  - list repos in my github org
  - what repositories does the organization have
  - github repository inventory
auth: [STACKQL_GITHUB_USERNAME, STACKQL_GITHUB_PASSWORD]
params:
  - name: org
    type: identifier
    required: true
    description: GitHub organization login
    example: stackql
outputs:
  - name: name
    type: string
    description: Repository name
  - name: full_name
    type: string
    description: org/name form
  - name: visibility
    type: string
    description: public, private or internal
  - name: archived
    type: boolean
    description: True when the repository is archived
  - name: default_branch
    type: string
    description: Default branch name
  - name: pushed_at
    type: string
    description: Last push timestamp (staleness signal)
  - name: stargazers_count
    type: integer
    description: Star count
cost:
  fan_out: none
  expensive: false
last_verified: "2026-07-26"
---

Lists every repository in a GitHub organization with the fields that answer
most governance asks: visibility, archive state, default branch and last-push
recency. Auth uses basic credentials, with a personal access token as the
password.

## Query

```sql
SELECT name, full_name, visibility, archived, default_branch, pushed_at, stargazers_count FROM github.repos.repos WHERE org = '{{org}}';
```

## Notes

Unauthenticated calls see only public repositories and are heavily
rate-limited; authenticate to see private and internal repositories the token
can access. Results are paginated transparently by StackQL.
