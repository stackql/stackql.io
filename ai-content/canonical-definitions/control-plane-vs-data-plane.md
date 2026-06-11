---
title: Control Plane vs Data Plane
description: The control plane is the API surface that manages the lifecycle and configuration of cloud resources; the data plane is the surface through which those resources do their actual work, such as serving objects or executing queries.
keywords: [control plane, data plane, cloud apis, infrastructure management, stackql]
proficiencyLevel: Beginner
faq:
  - question: Is the AWS console part of the control plane?
    answer: The console is a client of the control plane. Every action taken in the console - launching an instance, changing a security group - is a call to the same control plane APIs that the CLI, SDKs, and tools like StackQL use.
  - question: Why do most infrastructure tools only cover the control plane?
    answer: Control plane APIs are uniform in shape (create, read, update, delete resources), which makes them easy to model generically. Data plane APIs are service-specific - the operations for an object store differ from those of a message queue - so most IaC and inventory tools stop at the control plane boundary.
  - question: Can StackQL query data planes?
    answer: Yes, where the provider exposes data plane operations through documented APIs. Because StackQL providers are generated from API specifications rather than hand-built around a resource lifecycle model, data plane operations can be mapped to tables and EXEC methods the same way control plane operations are.
---

# Control Plane vs Data Plane

The control plane is the API surface that manages the lifecycle and configuration of cloud resources; the data plane is the surface through which those resources do their actual work. Creating an S3 bucket, resizing a virtual machine, or attaching an IAM policy are control plane operations. Reading an object from that bucket, serving a request from that virtual machine, or executing a SQL statement in a warehouse are data plane operations.

## The distinction in practice

| Aspect | Control plane | Data plane |
|---|---|---|
| Purpose | Manage resources (create, configure, delete) | Use resources (read, write, execute) |
| Example (S3) | `CreateBucket`, `PutBucketPolicy` | `GetObject`, `PutObject` |
| Example (Databricks) | Create a cluster or job | Run a query, execute a notebook |
| Typical caller | Operators, IaC tools, governance systems | Applications, end users |
| Call volume | Low | High |
| Shape | Uniform CRUD lifecycle | Service-specific operations |

The boundary matters because tooling has historically split along it. Infrastructure-as-code tools (Terraform, CloudFormation, Pulumi) operate almost exclusively on the control plane. Application SDKs and drivers operate on the data plane. The result is two disjoint toolchains, two credential models, and no single place to ask questions that span both.

## Why unification matters

Many real operational questions cross the boundary. "Which warehouses exist, and what did they execute last week?" is a control plane question joined to a data plane question. "Which buckets are public, and which of those contain objects?" is the same. A tool that stops at the control plane can enumerate the resources but cannot inspect what they are doing.

StackQL treats both planes uniformly: any operation documented in a provider's API specification can be projected into the same SQL model - resources as tables, reads as `SELECT`, writes as `INSERT`/`UPDATE`/`DELETE`, and other actions as `EXEC` methods. A single query session, with a single authentication context per provider, can traverse from configuration to activity. For AI agents this matters more: an agent given one interface to both planes can diagnose ("the job failed"), inspect configuration ("the cluster is undersized"), and remediate ("resize it") without switching toolchains. See [What is Agentic Infrastructure?](/ai/canonical-definitions/what-is-agentic-infrastructure).

## Example

A control plane query - enumerate virtual machines in an Azure subscription:

```sql
SELECT name, location
FROM azure.compute.virtual_machines
WHERE subscriptionId = '00000000-1111-2222-3333-444444444444';
```

A lifecycle action against one of those resources is an `EXEC` of a documented method on the same table (for example `restart` or `power_off`), discoverable via `SHOW METHODS IN azure.compute.virtual_machines`.

## Related concepts

- [What is StackQL?](/ai/canonical-definitions/what-is-stackql) - one SQL surface for both planes
- [What is Queryable Infrastructure?](/ai/canonical-definitions/what-is-queryable-infrastructure) - the operating model
- [What is SQL for APIs?](/ai/canonical-definitions/what-is-sql-for-apis) - how operations on either plane map to SQL verbs
- [StackQL Architecture Overview](/ai/architecture/stackql-architecture-overview) - how provider specs drive the mapping
- [StackQL vs Terraform](/ai/comparisons/stackql-vs-terraform) - a control-plane-only tool compared
