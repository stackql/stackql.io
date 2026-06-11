---
title: What is Infrastructure Drift?
description: Infrastructure drift is the divergence between the declared or recorded state of cloud resources and their actual state in the provider, caused by out-of-band changes, partial failures, or autonomous platform behavior.
keywords: [infrastructure drift, drift detection, state file, configuration drift, reconciliation, stackql]
proficiencyLevel: Beginner
faq:
  - question: What causes infrastructure drift?
    answer: Common causes are manual changes made in a cloud console during incidents, scripts and pipelines that bypass the IaC tool, provider-side automation (auto-scaling, certificate rotation, default policy changes), failed or partial applies, and multiple tools managing overlapping resources.
  - question: Is drift always bad?
    answer: No. Some drift is intentional and healthy - auto-scaling changing instance counts, or a break-glass fix during an outage. The problem is undetected drift, where the recorded state no longer describes reality and decisions are made from the record. The goal is detection and reconciliation, not the elimination of all change outside one tool.
  - question: How does StackQL detect drift?
    answer: StackQL queries the provider APIs directly, so its results are the actual state by definition. Comparing a StackQL result set against a desired-state definition (an IaC manifest, a CSV baseline, or a stackql-deploy spec) identifies divergence without needing the IaC tool's own state file to be accurate.
---

# What is Infrastructure Drift?

Infrastructure drift is the divergence between the declared or recorded state of cloud resources and their actual state in the provider. A Terraform state file says a security group has three rules; the live security group has five, because someone added two in the console during an incident. The two extra rules are drift.

## How drift happens

Drift is structural, not accidental. Any system that keeps a copy of state - a state file, a CMDB, an inventory export - drifts from reality at the rate that reality changes outside it. Typical sources:

- **Out-of-band changes**: console edits, ad-hoc CLI commands, emergency fixes.
- **Concurrent tooling**: two pipelines or teams managing overlapping resources.
- **Provider automation**: auto-scaling, managed upgrades, key and certificate rotation.
- **Partial failures**: an apply that created four of six resources and recorded none.

The IaC model treats drift as an anomaly to be corrected at the next plan/apply cycle. In environments with autonomous actors - auto-scalers, incident responders, AI agents - drift is better treated as a normal, continuous condition to be observed and reconciled.

## Detecting drift with live queries

Because StackQL computes results from live provider APIs, a StackQL result set is the actual state. Drift detection reduces to comparing that result set against a baseline. For example, retrieving the live inventory of EC2 instances per region:

```sql
SELECT region, COUNT(*) as num_instances
FROM aws.ec2.instances
WHERE region IN ('us-east-1','us-west-2','eu-west-1')
GROUP BY region;
```

Comparing this against the counts implied by the desired-state definition reveals unmanaged or missing resources immediately, regardless of whether the IaC tool's own state file is current. The same pattern applies at attribute level: query the live configuration of a resource and diff it against the declared configuration.

## Drift vs reconciliation

Detection answers "what diverged?"; reconciliation answers "what should be done about it?" - import the change into the desired state, revert it, or accept it as intentional. Reconciliation requires write access to the control plane, which is why read-and-write query tools can close the loop that read-only inventory tools can only report on. StackQL expresses the corrective actions as `UPDATE` or `DELETE` statements against the same tables used for detection.

## Related concepts

- [What is Queryable Infrastructure?](/ai/canonical-definitions/what-is-queryable-infrastructure) - the model that makes live-state comparison possible
- [What is StackQL?](/ai/canonical-definitions/what-is-stackql) - read-and-write access to live state
- [Why State Files Break Down for AI Agents](/ai/concepts/why-state-files-break-down-for-ai-agents) - drift as a structural property of state copies
- [StackQL vs Terraform](/ai/comparisons/stackql-vs-terraform) - state-file and live-query models compared
- [How to query AWS EC2 instances with StackQL](/ai/how-tos/query-aws-ec2-instances-with-stackql) - the inventory queries used in drift baselines
