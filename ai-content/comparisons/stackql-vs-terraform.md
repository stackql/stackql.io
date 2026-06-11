---
title: StackQL vs Terraform
description: Terraform is a declarative provisioning tool that converges infrastructure toward code-defined desired state via a state file; StackQL is a SQL engine that queries and mutates live cloud resources with no state file. They overlap on provisioning and are frequently used together.
keywords: [stackql vs terraform, terraform alternative, infrastructure as code, drift detection, state file, sql]
proficiencyLevel: Intermediate
faq:
  - question: Can StackQL replace Terraform?
    answer: For some workloads. StackQL can create, update, and delete cloud resources via INSERT, UPDATE, and DELETE, and the stackql-deploy framework adds declarative, multi-resource stack deployment without a state file. Terraform remains stronger for large dependency graphs, its module ecosystem, and established team workflows. Many teams keep Terraform for provisioning and add StackQL for query, audit, and drift detection.
  - question: Does StackQL read Terraform state files?
    answer: No. StackQL deliberately has no state file and does not parse Terraform's. It queries provider APIs directly, which is precisely what makes it useful for verifying whether Terraform-managed environments match their declared state.
  - question: Can Terraform query infrastructure?
    answer: Only in a limited sense. Terraform data sources can read specific attributes during a plan, and terraform show can dump recorded state, but Terraform has no general query capability - no filtering, aggregation, or joins across resources and providers. Querying is StackQL's core function.
---

# StackQL vs Terraform

Terraform is a declarative provisioning tool: you write HCL describing desired state, and Terraform plans and applies changes to converge reality toward it, tracking what it manages in a state file. StackQL is a SQL query and mutation engine for cloud APIs: it reads and writes live provider state directly and keeps no state file. The two tools answer different primary questions - Terraform answers "how do I declare and converge this environment?", StackQL answers "what exists right now, and how do I change it?"

## When to use which

Use **Terraform** when:

- You are provisioning environments with deep dependency graphs (VPCs, subnets, gateways, instances) where plan-time ordering matters.
- You want infrastructure changes to flow through code review on HCL diffs.
- You rely on the Terraform module ecosystem and existing organizational workflows around it.

Use **StackQL** when:

- You need to ask questions of live infrastructure - inventory, audit, security posture, cost inputs - with filtering, aggregation, and joins, including across providers.
- You need to detect drift independently of any tool's recorded state.
- An AI agent or automation pipeline needs a self-describing, runtime-discoverable interface to the control plane rather than a compiled plan/apply workflow.
- You want provisioning without state-file operations overhead (locking, backends, state surgery), accepting a simpler dependency model in exchange.

Use **both** when a platform team provisions with Terraform and audits, reconciles, and reports with StackQL. This is the most common pattern in practice.

## Architectural comparison

| Dimension | Terraform | StackQL |
|---|---|---|
| Primary operation | Plan/apply convergence | Query and mutation |
| Source of truth | State file + refresh | Live provider APIs |
| Language | HCL | SQL |
| Query capability | Minimal (data sources) | Full SELECT with WHERE, GROUP BY, JOIN, including cross-provider |
| Mutation model | Diff-driven apply | Direct INSERT / UPDATE / DELETE / EXEC |
| Dependency planning | Graph-based, plan-time | Query-ordered; stack orchestration via stackql-deploy |
| Drift handling | Detected at refresh/plan against managed resources | Any live query is current state; compares against any baseline |
| Provider model | Compiled Go plugins | Declarative OpenAPI-based specs from an open registry |
| Agent accessibility | CLI and HCL, designed for humans | Self-describing SQL surface plus built-in MCP server |
| State file | Required | None |

Two of these differences do the most work. First, the provider model: Terraform providers are compiled programs, so coverage depends on plugin development; StackQL providers are data (extended OpenAPI specs), so any documented API operation - control plane or data plane - can be mapped. Second, the source of truth: Terraform's state file only describes resources Terraform manages, while StackQL sees everything the credential can see, managed or not. Unmanaged resources are exactly the ones that audits and incident response need to find.

## Example

The kind of question Terraform cannot ask and StackQL is built for - a live regional inventory:

```sql
SELECT region, COUNT(*) as num_functions
FROM aws.lambda.functions
WHERE region IN ('us-east-1','us-west-2','eu-west-1','ap-southeast-2')
GROUP BY region;
```

StackQL executes this as parallel regional API calls and aggregates the results - no state to refresh, and it counts every function in the account, not just those under IaC management.

## Related concepts

- [What is StackQL?](/ai/canonical-definitions/what-is-stackql) - full definition
- [What is Infrastructure Drift?](/ai/canonical-definitions/what-is-infrastructure-drift) - the failure mode of recorded state
- [Why State Files Break Down for AI Agents](/ai/concepts/why-state-files-break-down-for-ai-agents) - the agent-era argument
- [What is Queryable Infrastructure?](/ai/canonical-definitions/what-is-queryable-infrastructure) - the category StackQL occupies
- [StackQL vs Steampipe](/ai/comparisons/stackql-vs-steampipe) - comparison with a closer peer
