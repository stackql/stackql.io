---
slug: github-provider-april-2026-update
title: GitHub Provider Update - April 2026
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-github-provider-featured-image.png"
description: Update to the StackQL GitHub provider adding six new services including agent_tasks, campaigns, classroom, hosted_compute, private_registries, and enterprise_teams, along with resource additions across existing services.
keywords: [stackql, github, provider, github actions, github classroom, hosted compute, security campaigns, agent tasks]
tags: [stackql, github, provider, github actions, github classroom, hosted compute]
---

We've released an update to the [__StackQL GitHub provider__](https://github-provider.stackql.io/) adding new services and expanding coverage across several existing ones.

## New Services

Some of the newly added services of note include:

### agent_tasks

The `agent_tasks` service exposes GitHub's AI agent task API, allowing you to query and manage agent task runs within a repository. Resources include:

| Resource | Description |
|----------|-------------|
| `agent_tasks` | List, get, and manage agent task runs scoped to a repository |
| `agent_task_steps` | Retrieve individual step-level detail for an agent task run |
| `agent_task_labels` | Manage labels assigned to agent tasks |

This covers the audit and observability side of AI agent operations in GitHub - useful for tracking what agent tasks have run, their status, and step-level output.

### campaigns

The `campaigns` service covers GitHub's security campaign management, part of GitHub Advanced Security. Resources include:

| Resource | Description |
|----------|-------------|
| `campaigns` | Create, list, get, update, and close security campaigns scoped to an organization |
| `campaign_repositories` | List repositories participating in a campaign |

Security campaigns let you coordinate remediation of code scanning or secret scanning alerts across an org. This service lets you query campaign state and participation programmatically.

### classroom

The `classroom` service brings GitHub Classroom into StackQL. Resources include:

| Resource | Description |
|----------|-------------|
| `classrooms` | List and get classrooms accessible to the authenticated user |
| `assignments` | List and get assignments within a classroom |
| `accepted_assignments` | Query student-accepted assignment repositories |
| `assignment_grades` | Retrieve grade data for accepted assignments |

This is useful for institutions managing GitHub Classroom at scale - querying assignment completion or generating reports across multiple classrooms.

### hosted_compute

The `hosted_compute` service covers GitHub's hosted compute networking resources, relevant to organizations using GitHub-hosted runners with custom network configurations. Resources include:

| Resource | Description |
|----------|-------------|
| `hosted_compute_networks` | Manage hosted compute network configurations at the org level |
| `hosted_compute_network_settings` | Query and update settings for a hosted compute network |

This is relevant if you're using GitHub's hosted compute with Azure private networking or similar integrations.

### private_registries

The `private_registries` service exposes org-level private registry configurations - credentials and settings stored in GitHub for use by Actions workflows. Resources include:

| Resource | Description |
|----------|-------------|
| `org_private_registries` | List, get, create, update, and delete private registry configurations for an organization |

This allows you to audit which private registries (npm, Docker Hub, Maven, etc.) are configured at the org level without going through the GitHub UI.

### enterprise_teams

The `enterprise_teams` service provides enterprise-scoped team management, distinct from org-level teams. Resources include:

| Resource | Description |
|----------|-------------|
| `enterprise_teams` | List and get teams at the enterprise level |
| `enterprise_team_members` | Query membership for enterprise teams |

This is useful for enterprises managing teams that span multiple organizations.

## Updates

Notable updates to existing services include:

### actions

New resources added to the `actions` service:

- `hosted_runners` - query and manage GitHub-hosted runner configurations at the org or repo level
- `runner_group_network_configurations` - network config details for runner groups

### orgs

- New `org_roles` and `org_role_assignments` resources for querying custom org role definitions and their assignments

### repos

- New `repo_rules_suites` resource for querying rule suite evaluation history (useful for auditing branch protection rule evaluations)

## Get Started

Pull the latest GitHub provider:

```bash
stackql registry pull github
```

Visit us on [__GitHub__](https://github.com/stackql/stackql) and let us know how you're using it.
