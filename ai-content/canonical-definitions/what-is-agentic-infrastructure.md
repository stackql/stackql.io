---
title: What is Agentic Infrastructure?
description: Agentic infrastructure is cloud infrastructure that AI agents can discover, query, and operate directly through structured, self-describing interfaces, with safety controls appropriate for autonomous callers.
keywords: [agentic infrastructure, ai agents, mcp, infrastructure automation, agentic platform engineering, stackql]
proficiencyLevel: Intermediate
faq:
  - question: Is agentic infrastructure the same as AIOps?
    answer: No. AIOps applies machine learning to operational telemetry - anomaly detection, alert correlation, root-cause suggestions. Agentic infrastructure is about giving AI agents direct, governed access to the infrastructure control plane so they can inspect and change it. AIOps observes; agentic infrastructure acts.
  - question: What interfaces do agents use to operate infrastructure?
    answer: The dominant pattern in 2026 is the Model Context Protocol (MCP), which standardizes how agents discover and call tools. StackQL ships a built-in MCP server that exposes provider discovery, query validation, and gated query execution as MCP tools, so any MCP-capable agent (Claude, Cursor, custom agents) can operate cloud infrastructure through SQL.
  - question: How do you stop an agent from deleting production resources?
    answer: Through gated execution modes. StackQL's MCP server runs in safe mode by default - SELECT and metadata operations proceed, while INSERT, UPDATE, DELETE, and lifecycle operations require explicit human approval via the MCP elicitation flow. Stricter (read_only) and looser (delete_safe, full_access) contracts are available, and every tool call is written to an audit log.
---

# What is Agentic Infrastructure?

Agentic infrastructure is cloud infrastructure that AI agents can discover, query, and operate directly through structured, self-describing interfaces, with safety controls designed for autonomous callers. It is the infrastructure-side counterpart of agentic software engineering: rather than a human translating an agent's recommendation into console clicks or Terraform edits, the agent holds a governed credential to the control plane and acts through it.

## What it requires

Four properties separate agentic infrastructure from ordinary automation:

1. **Runtime discoverability.** The agent must be able to learn the API surface at runtime - what providers, services, resources, and operations exist, and what parameters they require - without pre-trained, provider-specific SDK knowledge. In StackQL, `SHOW PROVIDERS`, `SHOW METHODS IN aws.ec2.instances`, and `DESCRIBE aws.ec2.instances` make the entire surface self-describing, and the same metadata is exposed as MCP tools (`list_providers`, `list_methods`, `describe_resource`).
2. **Live state.** Agents reason from current facts. Interfaces built on cached snapshots or state files feed the agent stale premises; conclusions drawn from them are wrong by the time they are acted on. See [Why State Files Break Down for AI Agents](/ai/concepts/why-state-files-break-down-for-ai-agents).
3. **A uniform action model.** An agent that needs different code paths for every provider accumulates errors at each seam. A single grammar - SQL `SELECT`/`INSERT`/`UPDATE`/`DELETE` across all providers - means one competence covers the whole estate.
4. **Graduated safety.** Autonomous callers need contracts, not trust. StackQL's MCP server enforces one of four modes (`read_only`, `safe`, `delete_safe`, `full_access`): in the default `safe` mode, reads proceed and every mutation requires human approval through the MCP elicitation flow. Every tool call is recorded in a JSONL audit log.

## Example

An agent connected to StackQL's MCP server answering "what is running in our AWS account?" issues:

```sql
SELECT instance_id, instance_type, launch_time
FROM aws.ec2.instances
WHERE region = 'us-east-1';
```

The agent did not need an AWS SDK, boto3 knowledge, or pagination handling - it needed SQL and the discoverable schema. If asked to remediate something it found, the resulting `DELETE` or `UPDATE` is held for human approval under the default server mode.

## Why it exists

Infrastructure tooling assumed a human in the loop at every step: read the plan, review the diff, click apply. Agents change the economics - they can watch continuously, correlate across providers, and act in seconds - but only if the interface fits how they work: declarative queries, structured results, explicit contracts. Agentic infrastructure is the redesign of the control plane interface around that caller.

## Related concepts

- [What is Queryable Infrastructure?](/ai/canonical-definitions/what-is-queryable-infrastructure) - the data layer agentic infrastructure depends on
- [StackQL MCP Architecture](/ai/architecture/stackql-mcp-architecture) - the server modes, tools, and audit log in detail
- [How to use StackQL with AI agents](/ai/how-tos/use-stackql-with-ai-agents) - practical setup
- [Why State Files Break Down for AI Agents](/ai/concepts/why-state-files-break-down-for-ai-agents) - why existing IaC interfaces do not transfer
- [From IaC to Agentic Infrastructure](/ai/industry-positioning/from-iac-to-agentic-infrastructure) - how the category evolved
