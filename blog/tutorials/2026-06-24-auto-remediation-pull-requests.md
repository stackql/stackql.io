---
slug: ai-remediation-pull-requests
title: "From finding to fix: cloud auto-remediation with AI and StackQL"
hide_table_of_contents: false
authors:
  - kieranrimmer
image: "/img/blog/stackql-mcp-server-featured-image.png"
description: "An audit tells you what is wrong. This is the other half - a GitHub Actions loop that opens one pull request per finding, checks live state before it mutates, and applies on merge. OIDC only, no agents in the estate, GitHub as the audit trail."
keywords: [stackql, github-actions, finops, remediation, oidc, sre]
tags: [stackql, github-actions, finops, remediation, oidc, sre]
---

A cloud audit tells you what is wrong. The work starts when you have to fix it. Most tooling stops at the findings list and hands a spreadsheet to an engineer, and the findings sit there until someone has a quiet afternoon.

This post walks through the other half: a remediation loop that turns each finding into a reviewable pull request, verifies live state before it changes anything, and applies the fix on merge. It runs entirely in GitHub Actions, authenticates with OIDC, and uses StackQL to talk to cloud control planes. The repo is public at [stackql-labs/stackql-ai-remediation](https://github.com/stackql-labs/stackql-ai-remediation), and the example throughout is FinOps waste (unattached disks, idle IPs, zero-VM projects), though the shape is the same for posture and security checks.

<!-- truncate -->

## The loop

The pipeline is deliberately small:

1. A scheduled or dispatched audit runs against the cloud control plane and writes findings as structured data.
2. For each finding, the loop opens one pull request carrying the exact proposed change.
3. A preflight status check queries live reality before anything can merge.
4. On merge, the change is applied through the vendor CLI.
5. A post-apply check confirms the resource is actually gone.

What is deliberately absent matters as much as what is present. No agents running in the estate. No long-lived cloud keys. No log pipeline, no inventory sync, no external scanner appliance. The data path is GitHub Actions to StackQL to the cloud control-plane APIs, and the audit trail is the pull request history.

## A finding is just data

The audit emits each finding as JSON, and that JSON is the contract the rest of the loop reads. A single FinOps finding looks like this:

```json
{
  "run_id": "27901134862",
  "provider": "aws",
  "check_id": "aws-finops-unattached-volume",
  "check_name": "Unattached EBS volumes",
  "severity": "LOW",
  "category": "waste",
  "region": "ap-southeast-2",
  "suggested_remediation": {
    "type": "delete",
    "tool": "stackql",
    "preflight_query": "SELECT VolumeId FROM aws.ec2.volumes WHERE region = 'ap-southeast-2' AND VolumeId = 'vol-0a1b2c3d4e5f' AND State = 'available'",
    "sql_query": "DELETE FROM aws.ec2.volumes WHERE region = 'ap-southeast-2' AND VolumeId = 'vol-0a1b2c3d4e5f'",
    "description": "Delete the unattached EBS volume (snapshot first if its data may be needed)."
  },
  "fields": {
    "volumeId": "vol-0a1b2c3d4e5f",
    "size": "20",
    "status": "available",
    "estimated_monthly_usd": 1.92
  }
}
```

The finding already carries its own remediation: a preflight query and the SQL that would fix it, both fully substituted with concrete values. The rest of the loop is mechanical from here.

## One pull request per finding

`generate_proposals.py` reads the findings and writes one directory per finding:

```
remediations/proposed/<run-id>/<n>-<check-id>-<resource-id>/
  finding.json      # verbatim copy of the finding
  preflight.sql     # the live-state check, pass = >=1 row
  remediation.sql   # the canonical fix (applied via CLI on merge)
  rationale.md      # deterministic explanation from the finding fields
```

The SQL comes straight from the audit's `suggested_remediation` block, already templated per check type, so the proposal is reproducible and reviewable rather than generated fresh each run. Where a model does help is a separate, optional step: an agent reads the batch and flags the captain's-call risks - the changes a human should look at twice - so reviewers know where to spend attention. Judgment and mutation are kept apart on purpose.

## Query before mutation

The preflight is the part worth dwelling on:

```sql
-- pass criterion: returns >= 1 row
SELECT VolumeId
FROM aws.ec2.volumes
WHERE region = 'ap-southeast-2'
  AND VolumeId = 'vol-0a1b2c3d4e5f'
  AND State = 'available';
```

This runs as a required status check on the pull request. It is a live query against the cloud API, not a read of a cached state file, and it asserts that the resource is still in the state the finding assumed at the moment of merge, not at the moment of audit. Findings go stale. Between the audit run and someone clicking merge, the volume may have been re-attached, deleted by someone else, or moved. A state file would not know. The API does. If the preflight returns zero rows, the check fails and the change cannot land.

This is the single most important property of the loop: the gap between "what the audit saw" and "what is true now" is closed by a live query, every time, automatically.

## The fix is a SQL statement

StackQL treats the cloud control plane as a queryable and mutable surface. You `SELECT` to read it and `DELETE` or `INSERT` to change it, with the same grammar across providers:

```sql
DELETE FROM aws.ec2.volumes
WHERE region = 'ap-southeast-2'
  AND VolumeId = 'vol-0a1b2c3d4e5f';
```

The statement is idempotent by shape. Run it twice and the end state is the same, which is exactly the property you want when a pipeline can retry. On merge, the apply workflow executes the remediation through the vendor CLI; `remediation.sql` is kept as the canonical, human-readable record of what was applied.

## Policy gates instead of a review bottleneck

Branch protection is where the safety model lives. The rule on `main` is:

- Required approvals: `0`
- Require status checks to pass before merging: on, so the preflight is enforced

The preflight check is the gate, not a human sign-off. That sounds aggressive until you see what it replaces: instead of one person eyeballing a diff, the merge is bounded by a machine-enforced envelope. Low-severity waste can auto-merge once the preflight is green; you keep a human approver in the path for higher-severity classes by raising the required-approvals count on those. The same pattern extends to budget ceilings, region locks, and change-rate caps. Policy is the loop's safety mechanism, expressed as checks rather than as a meeting.

## Onboarding without handing over keys

The loop authenticates with OIDC and federated identity only - AWS `AssumeRoleWithWebIdentity`, GCP Workload Identity Federation, Azure federated credentials. There are no static cloud keys in the repo.

Bootstrap stays inside the cloud admin's own console session. The onboarding templates under `cicd/onboarding/` give you a one-click path per cloud: a CloudFormation "Launch Stack" URL for AWS, a `gcloud` script for GCP, an ARM "Deploy to Azure" template for Azure. The admin clicks, authenticates in their own console, and the only thing that comes back to the pipeline is a role ARN or a workload-identity provider. Nothing long-lived crosses a boundary. It is the same integration model SaaS security tools already use.

## Run it

End to end:

1. Clone [stackql-labs/stackql-ai-remediation](https://github.com/stackql-labs/stackql-ai-remediation).
2. Set branch protection on `main`: required approvals `0`, required status check for the preflight.
3. Run the onboarding template for each cloud you want to cover to create the OIDC role or binding.
4. Store the resulting role ARNs and WIF provider as repo secrets.
5. Dispatch the audit workflow, or let the schedule run it.

Findings upload as workflow artifacts, proposals generate, and one pull request opens per finding. Merge a green one and watch the apply and post-check complete. A representative cross-cloud sweep here finished in a couple of minutes and surfaced a handful of unattached volumes, each as its own PR with the saving attached.

## Why this shape

The properties that make this worth running are all consequences of one decision: reach the control plane as data. Findings are data, the safety check is a query, the fix is a statement, and the record is the pull request. No new dashboard, no new vendor, no agent footprint in the estate. It is boring and inspectable, which is what you want from something allowed to change production.

The same loop runs for security posture and access findings by swapping the checks. That is the next post.
