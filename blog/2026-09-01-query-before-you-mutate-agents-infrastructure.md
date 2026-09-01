---
slug: query-before-you-mutate-agents-infrastructure
title: "Query before you mutate: how agents should touch your infrastructure"
authors: [nirmalchhodvadiya]
tags: [tutorial, agentic, mcp, gcp, security]
---

## The problem with plan, review, apply

Infrastructure-as-code was designed around human-speed changes: write a plan, review it, apply it, and use state snapshots to know what was managed before. That works best when writers are few and changes are infrequent.

Agents change those assumptions. They run continuously and multiple actors can touch the same resources at the same time. Drift is no longer exceptional. It is unavoidable and relentless.

Canonical IaC tools like Terraform still matter because they represent intent. What's weakened is their ability to reflect current reality. Agents rarely own the whole picture. They usually operate in narrow scopes, working on a slice of infrastructure that other agents, other tools, or humans are also touching. In that world, an agent will regularly encounter infrastructure that was mutated out of band, outside whatever IaC system nominally manages it.

Query-before-mutation handles this case. Read live cloud state, compare it with policy, apply a bounded policy gate, mutate only what is out of policy, then verify. Each run starts from reality rather than a cached view.

The demo takes about ten minutes and uses Google Cloud Storage bucket encryption, but the pattern is provider-agnostic.

<!-- truncate -->

## Query before mutation

Correctness comes from querying live state before acting. Safety comes from something separate: policy gates that bound what a single run is allowed to do. Location locks, resource caps, budget ceilings, allowlists. Two patterns, two jobs. Agentic query-before-mutation complements centralised IaC approaches leveraging snapshots of state. Policy gates replace the review meeting.

The combined loop is correct by default because it reads reality, and safe by default because it cannot do more than the gate allows. Agents can run it continuously. Humans can run it manually. The output converges either way.

`stackql` implements this pattern. Any resource on any supported cloud can be read with SQL and mutated with SQL where the provider exposes mutation methods. Kubernetes solved reconciliation inside the cluster, but nothing reconciles across your cloud. That gap is where this pattern belongs. The demo uses Google Cloud Storage because the mutation surface is clean, but the shape is the same for AWS S3, Vault secrets, GitHub org settings, or anything else an agent needs to reason about.

## Setup

You need three things:

- Docker (to run `stackql` without installing anything else)
- A Google Cloud project with credentials that can read buckets and update encryption config, plus an existing Cloud KMS key you want to enforce
- About ten minutes

Clone the tutorial folder from the `stackql` repo:

```bash
git clone https://github.com/stackql/stackql.git
cd stackql/docs/tutorials/query-before-mutation-01
```

Copy the sample environment file and add your service account credentials:

```bash
cp .env.sample .env
# Then edit .env to set:
# GOOGLE_CREDENTIALS=<service-account-json-on-one-line>
```

Bring up an interactive `stackql` shell:

```bash
docker compose run --rm --entrypoint bash stackql
# then inside the container:
stackql shell
```

Pull the Google provider from the registry. This only needs to happen once per shell session:

```sql
REGISTRY PULL google v26.07.00432;
```

You're ready.

## Query the live state

Ask the GCS API which buckets exist and what encryption is currently applied to each.

```sql
SELECT name, location, encryption 
FROM google.storage.buckets 
WHERE project = 'your-project-id';
```

Expected output on a project with a mix of encryption configurations:

```
| name                       | location | encryption                                    |
| demo-app-bucket1           | US       | null                                          |
| demo-app-bucket2           | US       | null                                          |
| stackql-demo-src-bucket    | US       | null                                          |
| stackql-encrypted-bucket-1 | US       | {"defaultKmsKeyName":"projects/.../keys/..."} |
```

Three buckets show encryption as null, meaning Google-managed encryption keys (GMEK), the default. One already uses a customer-managed key. Under a policy that requires customer-managed keys, three buckets are out of policy.

A state snapshot can tell you what Terraform previously configured. This query tells you what GCS reports now, regardless of who created or changed the bucket. If ten agents and two humans are all touching this project at various points during the day, the query still returns the current truth every time you run it. That's the property the pattern is built on.

## Apply a policy gate

Three buckets don't match policy. Before mutating, the agent needs to prove it's allowed to. That's what the policy gate does.

A gate is anything that bounds what a single run of the agent can do. Scope by project or location, cap the number of resources changed, or enforce an explicit allowlist. The shape depends on how much freedom the agent has and how much you're willing to lose in one bad run.

For this demo, two gates cover most of the risk. The first is a location lock. The agent should only ever act on buckets in the location it was configured for. This is a WHERE clause on every query and mutation:

```sql
WHERE location = 'US'
```

Trivial when the project's buckets are in one location. In production, agents move between locations based on task, and touching a bucket in the wrong region can mean acting on resources in an environment the agent has no business in. One clause, but it's the difference between a bounded action and an unbounded one.

The second gate is a count cap. Before running a mutation across multiple buckets, ask the API how many the mutation would touch:

```sql
SELECT COUNT(*) 
FROM google.storage.buckets 
WHERE project = 'your-project-id' 
AND location = 'US' 
AND encryption IS NULL;
```

If the query returns 20 buckets and the agent's policy expects to be operating in a project with 3 to 5 non-compliant buckets, something has changed. Maybe an account merger, maybe a script created buckets out of band, maybe the credentials point to the wrong project entirely. The agent shouldn't proceed. It should log the discrepancy and wait for a human to look.

Gates like these aren't clever. They're routine, boring, and easy to skip. They're also the reason a query-before-mutation loop can run without a human review meeting attached to every apply. The gate carries the weight the meeting used to.

## Mutate to converge

The gates passed. For each bucket without a customer-managed key, apply the policy:

```sql
UPDATE google.storage.buckets 
SET data__encryption = '{"defaultKmsKeyName":"projects/your-project-id/locations/us/keyRings/your-ring/cryptoKeys/your-key"}' 
WHERE bucket = 'demo-app-bucket1';
```

Two things worth noting. The `data__encryption` prefix is how `stackql` passes fields into the request body, distinguishing them from URL parameters. The `WHERE` clause uses `bucket` rather than `name`, because `bucket` is the identifier the underlying patch method requires.

Expected output:

```
The operation was despatched successfully
```

In an agent loop, this runs as a bounded iteration over the non-compliant buckets returned by the earlier query, applying the same UPDATE to each one.

## Verify convergence

A successful mutation is not enough. Query the bucket again to verify:

```sql
SELECT name, encryption 
FROM google.storage.buckets 
WHERE bucket = 'demo-app-bucket1';
```

Expected output:

```
| name             | encryption                                                                    |
| demo-app-bucket1 | {"defaultKmsKeyName":"projects/your-project-id/locations/us/keyRings/..."}    |
```

The bucket converged from GMEK to the required customer-managed key.

Run the loop again and something interesting happens: the mutation is a no-op. The SELECT finds no drift, the count check passes trivially, no UPDATE fires. That's idempotence by design. The agent can run every minute, every hour, every day, and it does the same right thing whether the target is already compliant or has just drifted. No state snapshot needed to remember what it did last time. Every run starts from live reality.

## The same pattern from an agent

Everything above is you typing SQL into a shell. The same pattern maps directly to an agent through `stackql`'s MCP server, listed in the Claude directory at [claude.ai/directory/ant.dir.gh.stackql.stackql](https://claude.ai/directory/ant.dir.gh.stackql.stackql).

Install it, add your GCP credentials, and ask Claude in plain English:

```
List all GCS buckets in the project stackql-demo, and for each 
show whether encryption is a customer-managed key or the 
Google-managed default.
```

![Claude Desktop response showing stackql MCP query result with bucket encryption table](/img/blog/query-before-you-mutate-claude-mcp.png)

Claude uses the `stackql` MCP server to run the same underlying SELECT you saw earlier, then formats the result into a readable table. The query, gate, mutate, and verify loop runs the same way an agent runs it, just as reasoning steps in a conversation instead of shell commands.

Notice what the agent adds that the raw shell output doesn't: it separates CMEK buckets from Google-managed default ones, calls out which KMS key each CMEK bucket uses, and flags a caveat you didn't ask about (the difference between bucket-default encryption and per-object encryption). That's the value of running the pattern through an agent rather than a script. Same query, same result, plus the reasoning layer on top.

The shell walkthrough above was for you to see the pattern happen line by line. The MCP integration is how the pattern runs end to end in an actual agent's workflow.

## Where this goes next

Query before mutation is one instance of a broader pattern. Six patterns, really, that hold together as a working model for how agents should operate on infrastructure. Query before mutation is one. Idempotent assertions, bi-temporal views, agents as event sources, universal interfaces, and policy gates are the other five. Together they replace the assumptions the plan-review-apply model was built on.

The full argument is in Jeff Aven's talk from AI Engineer Melbourne, "Treating Infrastructure as Data: Building an AI-Native Control Plane." If the pattern in this tutorial made sense to you, the talk is the map for what else fits with it.

If this tutorial was useful, star the [stackql repo](https://github.com/stackql/stackql). Every star helps other developers and agents find the pattern, and helps the project climb the MCP registry where more agents can discover it.

Next in this series: idempotent assertions with `stackql-deploy`. Same shape, different pattern, applied to declarative infrastructure that runs continuously without breaking on repeat.
