---
title: From IaC to Agentic Infrastructure
description: Infrastructure tooling has moved through three eras - imperative scripts, declarative state-file IaC, and now agentic infrastructure, where AI agents operate the control plane through self-describing, governed interfaces over live state.
keywords: [agentic infrastructure, infrastructure as code, evolution, ai-native, platform engineering, stackql]
proficiencyLevel: Intermediate
faq:
  - question: Does agentic infrastructure make IaC obsolete?
    answer: No. Declarative definitions of desired state remain valuable - they encode intent. What changes is the execution and observation layer around them - agents querying live state, detecting divergence, and proposing or executing reconciliation continuously, rather than humans running plan/apply cycles against a state file.
  - question: What has to be true of infrastructure tooling for agents to use it well?
    answer: Four properties - runtime discoverability of the API surface, live rather than recorded state, a uniform action grammar across providers, and graduated safety contracts with auditability. Tools designed for human plan/apply workflows generally lack at least two of these.
  - question: Where does StackQL fit in this evolution?
    answer: StackQL implements the agentic-era interface - cloud and SaaS APIs as self-describing SQL tables over live state, with mutations in the same grammar, exposed to agents through a built-in MCP server with safety modes and audit logging.
---

# From IaC to Agentic Infrastructure

Infrastructure tooling has moved through three eras, each defined by who - or what - operates the control plane. Imperative scripting put humans at the keyboard; declarative IaC put humans in a review loop around a convergence engine; agentic infrastructure puts AI agents on the control plane directly, and the interface requirements change accordingly.

## Era one: imperative automation

Shell scripts, CLI invocations, and configuration tools encoded *procedures*: do this, then this. The operator carried the model of desired state in their head. The approach scaled poorly - procedures drift, error handling multiplies, and nothing checks the result against intent.

## Era two: declarative IaC

Terraform, CloudFormation, and their peers inverted the model: declare desired state, let the engine compute the procedure. This was a genuine advance - reviewable diffs, repeatable environments, modular reuse - and it built the workflows most platform teams run today. Its load-bearing artifact is the state file: the engine's record of what it manages, used to map declarations to real resources and compute plans.

The model's assumptions are human-shaped: changes arrive in batches through code review; the state is refreshed when someone runs plan; everything that matters is inside the managed set. Within those assumptions it works well, which is why it persists. Outside them - continuous observation, cross-provider questions, resources nobody declared - it has little to offer: IaC tools cannot ask questions, and their records describe only what they manage. See [Why State Files Break Down for AI Agents](/ai/concepts/why-state-files-break-down-for-ai-agents).

## Era three: agentic infrastructure

AI agents change the operating pattern, not just the operator. Agents act continuously rather than in release batches; they ask ad-hoc questions across the whole estate; they are concurrent; and they need machine-checkable guardrails rather than tribal process. The interface that serves them has four properties:

1. **Discoverable** - the agent learns providers, resources, operations, and required parameters at runtime (`SHOW METHODS`, MCP discovery tools), instead of shipping with hardcoded provider knowledge.
2. **Live** - answers reflect the control plane now, not a copy refreshed on a human cadence.
3. **Uniform** - one grammar (SQL: `SELECT`/`INSERT`/`UPDATE`/`DELETE`) across every provider, so agent competence transfers across the estate.
4. **Governed** - explicit contracts on what an autonomous caller may do (read-only through full access), human approval where required, and a complete audit trail.

This is the interface StackQL implements: provider APIs as self-describing SQL tables over live state, mutations in the same grammar, exposed to agents through a built-in MCP server with graduated safety modes and always-on audit logging.

## What persists from the IaC era

Desired-state definitions persist - intent must live somewhere, and version-controlled declarations remain the right home for it. What the agentic era replaces is the machinery between intent and reality: the periodic plan/apply loop against a private state record gives way to continuous query, comparison, and governed reconciliation against the live control plane. IaC defined the destination; agentic infrastructure changes how - and how often - the gap to it is measured and closed.

## Related concepts

- [What is Agentic Infrastructure?](/ai/canonical-definitions/what-is-agentic-infrastructure) - the canonical definition
- [Why State Files Break Down for AI Agents](/ai/concepts/why-state-files-break-down-for-ai-agents) - the era-two limits in detail
- [StackQL vs Terraform](/ai/comparisons/stackql-vs-terraform) - era two and era three tools compared
- [What is Queryable Infrastructure?](/ai/canonical-definitions/what-is-queryable-infrastructure) - the data layer of era three
- [StackQL MCP Architecture](/ai/architecture/stackql-mcp-architecture) - the governance layer of era three
