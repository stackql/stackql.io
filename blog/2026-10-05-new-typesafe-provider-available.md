---
slug: new-typesafe-provider-available
title: New TypeSafe Provider Available
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-typesafe-provider-featured-image.png"
description: A new StackQL provider for TypeSafe AI that brings Jev, TypeSafe's System One decision model, into StackQL agent routines over MCP - rows from aws, k8s, okta or github become the state, Jev returns a typed decision with a probability or confidence, and a StackQL mutation runs only when the decision clears the routine's threshold. Tagging hygiene, rightsizing, incident triage, access review, public ingress review and audit findings, as SQL.
keywords: [stackql, typesafe, jev, system one, provider, mcp, ai agents, agentic infrastructure, finops, greenops, sre, cspm, access review, audit, sql]
tags: [provider-announcement, stackql, typesafe, jev, provider, mcp, ai-agents]
---

We've released a new StackQL provider for TypeSafe AI:

- [__`typesafe`__](https://typesafe-provider.stackql.io) - the TypeSafe API: __`systemone`__ (the evaluation endpoint behind Jev, TypeSafe's flagship model) and __`models`__ (the models and aliases an API key can use) - 2 services, 2 resources, 2 operations, both `SELECT`

Jev is a System One model. Instead of generating text it answers typed questions about a `state` you supply and returns a calibrated probability or distribution for each one: a Noul question is a yes/no with the probability of yes, a Choice picks one option from a set you define with a probability per option and a confidence, and a Score rates the state against an ordered rubric. Any mix of the three runs against the same state in one request, in about a hundred milliseconds, for a fraction of a cent. There is nothing to parse and nothing to coax into JSON.

That matters most for agents that already run on the [StackQL MCP server](/docs/command-line-usage/mcp). Those agents have every provider's control plane as SQL, and the audit log and approval gates that come with it. What a `WHERE` clause cannot do is make the judgment call: is this instance production or scratch, does this rule's description justify public ingress, does this account still need an admin role, is a restart the right response to this event. In this provider that call is a `SELECT` too, so a decision sits between a context query and a mutation as one more governed tool call. The rest of this post is that routine, in the shapes platform, FinOps, SRE and security teams actually run.

## Connect

Authentication is a TypeSafe API key, created in the TypeSafe console and read from `TYPESAFE_API_KEY`, the same variable the TypeSafe SDKs use (there is no Terraform provider for TypeSafe):

```bash
export TYPESAFE_API_KEY=...
stackql shell
```

Pricing is per input token (`$0.042` per million at the time of writing; output tokens are free), and the `usage` column of every row reports what a decision cost. Jev 1.13 allows 80 requests per second and 100K tokens per second; the provider carries the retry policy the vendor SDKs apply by default, so a 429 or a 529 on the first attempt is retried with backoff before it surfaces.

## The routine

An evaluation is a `SELECT` from `typesafe.systemone.evaluations`: the `state`, the `model` and the `questions` are the `WHERE` clause, and the row that comes back carries the versioned model that answered, the `answers` keyed by the names you chose, and the token usage. Over MCP the routine is three tool calls:

1. **Context** - `run_select_query` against the provider that owns the facts (`aws`, `k8s`, `okta`, `github`, ...). The agent keeps the columns the decision needs.
2. **Decision** - `run_select_query` against `typesafe.systemone.evaluations`, with the record serialised as the JSON `state` and the routine's questions as `questions`.
3. **Action** - `run_mutation_query` or `run_lifecycle_operation` against the owning provider, only when the answer clears the threshold the routine sets: a Noul at or above 0.9 (or at or below 0.1), a Choice with `confidence` at or above 0.9, a Score above a level.

Every evaluation is a read, so the decision step is allowed in every MCP server mode, including `read_only`; in the default `safe` mode the action still goes through the client's approval prompt, and in `read_only` the routine stops at the recommendation. Every tool call lands in the server's audit log, and because the row names the model version that answered, every automated change can record which model decided it.

The examples below were run live against the API before publication. Each shows the context query, the decision over one representative record, and the action; the answer Jev gave for that record is under each block.

## Tagging hygiene

Platform engineering: classify an instance from its name and existing tags, then write the missing tags back.

```sql
-- 1. context (run_select_query)
SELECT instance_id, instance_type, launch_time, tags
FROM aws.ec2.instances
WHERE region = 'us-east-1';

-- 2. decision (run_select_query): one record from the result as the state
SELECT json_extract(answers, '$.environment.choice') AS environment,
       json_extract(answers, '$.environment.confidence') AS confidence,
       json_extract(answers, '$.owner_team.choice') AS owner_team,
       model
FROM typesafe.systemone.evaluations
WHERE state = '{"instance_id": "i-0a1b2c3d4e5f67890", "instance_type": "m5.2xlarge", "launch_time": "2026-03-02T09:14:00Z", "tags": [{"Key": "Name", "Value": "jenkins-agent-prod-2"}, {"Key": "created-by", "Value": "ci-platform@example.com"}]}'
  AND model = 'jev-latest'
  AND questions = '{
    "environment": {"type": "choice", "instructions": "Which environment does this instance belong to? Use the Name tag and anything else in the record.",
                    "criteria": {"production": "Serves live traffic or production pipelines", "staging": "Pre-production, UAT or release testing", "development": "Developer, sandbox or test use", "unknown": "The record does not say"}},
    "owner_team": {"type": "choice", "instructions": "Which team most likely owns this instance?",
                   "criteria": {"platform": "CI, build and shared infrastructure", "data": "Analytics and data pipelines", "product": "Customer-facing applications", "unknown": "Cannot tell from the record"}}
  }';

-- 3. action (run_mutation_query), when confidence >= 0.9
INSERT INTO aws.ec2.tags (ResourceId, Tag, region)
SELECT 'i-0a1b2c3d4e5f67890',
       '[{"Key": "environment", "Value": "production"}, {"Key": "owner", "Value": "platform"}]',
       'us-east-1';
```

| environment | confidence | owner_team | model |
|---|---|---|---|
| production | 1.0 | platform | jev-1.13.0 |

A routine that walks every untagged instance in the account is a loop over the context rows: one decision call per record, one tag write per record that clears the bar, and the rest left for a person.

## Rightsizing and off-hours scheduling

FinOps and GreenOps: given an instance record and the utilisation the agent has gathered, decide between keeping it, stopping it outside business hours, downsizing it or referring it to the owner. A second question asks whether the workload could run at another time or in another region at all, which is the lever carbon-aware scheduling needs.

```sql
-- 1. context (run_select_query)
SELECT instance_id, instance_type, state, launch_time, tags
FROM aws.ec2.instances
WHERE region = 'us-east-1';

-- 2. decision (run_select_query)
SELECT json_extract(answers, '$.action.choice') AS action,
       json_extract(answers, '$.action.confidence') AS confidence,
       json_extract(answers, '$.schedulable.noul') AS p_schedulable,
       model
FROM typesafe.systemone.evaluations
WHERE state = '{"instance_id": "i-0b9c8d7e6f5a43210", "instance_type": "r5.4xlarge", "state": "running", "launch_time": "2025-11-18T03:00:00Z", "tags": [{"Key": "Name", "Value": "nightly-report-builder"}, {"Key": "owner", "Value": "data"}], "avg_cpu_14d_pct": 3.1, "max_cpu_14d_pct": 61.0, "busy_hours_utc": "01:00-03:00"}'
  AND model = 'jev-latest'
  AND questions = '{
    "action": {"type": "choice", "instructions": "What should a cost review do with this instance?",
               "criteria": {"keep": "Utilisation and purpose justify running it as is", "stop_outside_hours": "It only works in a known window and can be stopped the rest of the time", "downsize": "It is oversized for its sustained load", "review_with_owner": "The record is not enough to decide"}},
    "schedulable": {"type": "noul", "instructions": "Could this workload run at a different time of day or in a different region without affecting users?",
                    "criteria": {"true": "A batch or scheduled job with no interactive users", "false": "Serves users or other systems on demand"}}
  }';

-- 3. action (run_lifecycle_operation), when action = 'stop_outside_hours' and confidence >= 0.9
EXEC aws.ec2.instances.stop_instances
  @InstanceId = 'i-0b9c8d7e6f5a43210',
  @region = 'us-east-1';
```

| action | confidence | p_schedulable |
|---|---|---|
| stop_outside_hours | 0.9 | 0.87 |

The confidence sat between 0.86 and 0.91 across repeated runs of the same record, which is the point of putting a threshold in the routine rather than in the prompt: at 0.86 this instance goes to the owner instead of being stopped.

## Incident triage

SRE: read the cluster's warning events, let Jev name the likely cause, rate the severity and say whether rescheduling the pod is the right first response.

```sql
-- 1. context (run_select_query)
SELECT json_extract(involved_object, '$.namespace') AS namespace,
       json_extract(involved_object, '$.kind') AS kind,
       json_extract(involved_object, '$.name') AS name,
       reason, message, count, last_timestamp
FROM k8s.core.events_all_namespaces
WHERE type = 'Warning';

-- 2. decision (run_select_query)
SELECT json_extract(answers, '$.cause.choice') AS cause,
       json_extract(answers, '$.severity.score') AS severity,
       json_extract(answers, '$.reschedule_helps.noul') AS p_reschedule_helps,
       model
FROM typesafe.systemone.evaluations
WHERE state = '{"namespace": "payments", "kind": "Pod", "name": "checkout-api-7c9d6f8b5-xk2lp", "reason": "BackOff", "message": "Back-off restarting failed container checkout-api in pod checkout-api-7c9d6f8b5-xk2lp", "count": 14, "last_timestamp": "2026-10-05T02:41:07Z", "recent_log_tail": "FATAL: connection pool exhausted after 30s waiting for a connection to payments-db"}'
  AND model = 'jev-latest'
  AND questions = '{
    "cause": {"type": "choice", "instructions": "What is the most likely cause of this event?",
              "criteria": {"crash_loop": "The container exits repeatedly on its own error", "out_of_memory": "Killed for exceeding its memory limit", "image_pull": "The image cannot be pulled", "scheduling": "No node can place the pod", "dependency": "A dependency the container needs is unavailable or saturated", "other": "None of the above"}},
    "severity": {"type": "score", "instructions": "How severe is this for users?",
                 "criteria": ["Informational, no user impact", "Degraded, users may notice", "Outage of a user-facing capability", "Outage with data or payment risk"]},
    "reschedule_helps": {"type": "noul", "instructions": "Would deleting the pod so the controller reschedules it most likely resolve this?",
                         "criteria": {"true": "A transient or node-local fault that a fresh pod clears", "false": "A code, configuration or dependency fault a new pod would hit again"}}
  }';

-- 3. action (run_mutation_query), when p_reschedule_helps >= 0.9 and severity < 2
DELETE FROM k8s.core.pods
WHERE namespace = 'payments' AND name = 'checkout-api-7c9d6f8b5-xk2lp';
```

| cause | severity | p_reschedule_helps |
|---|---|---|
| dependency | 2.2 | 0.21 |

This one is worth reading closely. The event alone looks like a crash loop, and a reflexive runbook would delete the pod. Jev read the log tail, named the saturated database pool as the cause, rated it an outage with payment risk and gave the restart a 0.21, so the routine does not touch the pod and the agent escalates instead.

## Public ingress review

CSPM: list the security group rules open to the internet, let Jev judge whether each rule's description and port make the exposure a deliberate public endpoint or an exposed management or database port, then revoke a rule only when it is judged unjustified.

```sql
-- 1. context (run_select_query)
SELECT security_group_rule_id, group_id, ip_protocol, from_port, to_port, cidr_ipv_4, is_egress, description
FROM aws.ec2.security_group_rules
WHERE region = 'us-east-1'
  AND cidr_ipv_4 = '0.0.0.0/0';

-- 2. decision (run_select_query)
SELECT json_extract(answers, '$.justified.noul') AS p_justified,
       json_extract(answers, '$.exposure.choice') AS exposure,
       json_extract(answers, '$.exposure.confidence') AS confidence,
       model
FROM typesafe.systemone.evaluations
WHERE state = '{"security_group_rule_id": "sgr-0f1e2d3c4b5a69788", "group_id": "sg-0123456789abcdef0", "group_name": "analytics-db", "ip_protocol": "tcp", "from_port": 5432, "to_port": 5432, "cidr_ipv_4": "0.0.0.0/0", "is_egress": false, "description": "temp access for vendor demo"}'
  AND model = 'jev-latest'
  AND questions = '{
    "justified": {"type": "noul", "instructions": "Do the description, group name and port justify ingress from the whole internet?",
                  "criteria": {"true": "A deliberate public endpoint such as a load balancer or web tier on 80 or 443", "false": "A management, database or internal service port, or a temporary exception"}},
    "exposure": {"type": "choice", "instructions": "What kind of exposure is this?",
                 "criteria": {"public_endpoint": "Intended internet-facing service", "management_port": "SSH, RDP, WinRM or similar", "database_port": "A database or cache listener", "unknown": "Cannot tell from the record"}}
  }';

-- 3. action (run_lifecycle_operation), when p_justified <= 0.1
EXEC aws.ec2.security_groups.revoke_security_group_ingress
  @GroupId = 'sg-0123456789abcdef0',
  @SecurityGroupRuleId = 'sgr-0f1e2d3c4b5a69788',
  @region = 'us-east-1';
```

| p_justified | exposure | confidence |
|---|---|---|
| 0.04 | database_port | 1.0 |

## Audit findings

Audit and compliance: the context comes from one provider and the action goes to another. Read the privilege grants from the identity provider's system log, let Jev check each against the change policy written into the question, and open a tracked finding for the ones that fall outside it.

```sql
-- 1. context (run_select_query)
SELECT published, eventType, displayMessage, outcome, actor, target
FROM okta.logs.system_log_events
WHERE subdomain = 'my-org'
  AND since = '2026-10-04T00:00:00Z'
  AND filter = 'eventType eq "user.account.privilege.grant"';

-- 2. decision (run_select_query)
SELECT json_extract(answers, '$.in_policy.noul') AS p_in_policy,
       json_extract(answers, '$.risk.score') AS risk,
       model
FROM typesafe.systemone.evaluations
WHERE state = '{"published": "2026-10-04T22:47:13Z", "eventType": "user.account.privilege.grant", "displayMessage": "Grant user privilege", "outcome": "SUCCESS", "actor": {"alternateId": "j.doe@example.com", "type": "User"}, "target": [{"alternateId": "svc-reporting@example.com", "type": "User"}, {"displayName": "Super Administrator", "type": "ROLE"}], "note": "no change ticket referenced"}'
  AND model = 'jev-latest'
  AND questions = '{
    "in_policy": {"type": "noul", "instructions": "Does this privilege grant comply with the change policy?",
                  "criteria": {"true": "Granted by an identity administrator, between 09:00 and 17:00 UTC on a weekday, with a change ticket referenced", "false": "Outside the change window, by a non-administrator, to a service account, or without a ticket"}},
    "risk": {"type": "score", "instructions": "How much risk does this grant carry?",
             "criteria": ["Routine, scoped role", "Elevated role to a person", "Tenant-wide administrative role, or any role to a service account"]}
  }';

-- 3. action (run_mutation_query), when p_in_policy <= 0.2
INSERT INTO github.issues.issues (owner, repo, title, body, labels)
SELECT 'my-org',
       'security-audit',
       'Privilege grant outside change policy: svc-reporting@example.com granted Super Administrator',
       'Granted 2026-10-04T22:47:13Z by j.doe@example.com with no change ticket referenced. Flagged by jev-1.13.0 (in_policy 0.02, risk 2.0). Review and revoke or document.',
       '["compliance", "iam"]';
```

| p_in_policy | risk |
|---|---|
| 0.02 | 2.0 |

The policy lives in the `criteria` of one question. Changing the change window or the ticket rule is a diff in that text, reviewed like any other change to the routine, not a retrained classifier.

## Writing the questions

Two things came out of putting these routines together. First, ask Jev the judgment and compute the facts yourself. An access-review draft asked "Is this account dormant?" with a 90-day rule in the criteria, and the answer drifted between 0.54 and 0.88 however plainly the dates were stated; Jev treats dormancy as a judgment about the person. Asked instead "Do the title and department justify the roles this account holds?" the same record answered 0.16 every time, and "What kind of account is this?" answered contractor with confidence 1.0. Days since the last sign-in is arithmetic, so it stays in SQL, and the suspension (`EXEC okta.users.users.suspend_user`) runs only when the arithmetic and the judgment agree. The full example is on the [provider docs](https://typesafe-provider.stackql.io).

Second, keep the thresholds in the routine and the model version in the record. Choice confidences and Scores drift a little between identical calls, which is a reason to gate on them rather than to read a single number as a verdict. The `model` column records which version answered, so an action log can say "stopped by jev-1.13.0 at 0.91", and once a routine's thresholds are tuned the versioned id can be pinned in place of the `jev-latest` alias.

## What the provider is and is not

The TypeSafe API is two operations, and the provider maps both. There is no administrative or usage API to map: keys are created and revoked in the TypeSafe console, and usage is reported per request in the `usage` column rather than through a reporting endpoint. Everything in the API is a read, so the provider has no `INSERT`, `UPDATE` or `DELETE` methods of its own; the mutations in this post belong to the providers that own the resources. The models catalog is the one plain inventory query:

```sql
SELECT name, description, release_date
FROM typesafe.models.models
ORDER BY name;
```

Every statement in this post was checked before publication: the typesafe statements ran live, and the aws, k8s, okta and github statements were routed through their providers to the wire against a scratch account. The complete smoke run of the provider costs a fraction of a cent.

## Get started

Pull the provider from the public registry:

```bash
registry pull typesafe;
```

Provider docs, including the request shape of each question type, the error contract and the full set of agent-routine examples, are at [typesafe-provider.stackql.io](https://typesafe-provider.stackql.io). For the MCP server itself, start with [How to use StackQL with AI agents](/ai/how-tos/use-stackql-with-ai-agents). Let us know what you build. Star us on [__GitHub__](https://github.com/stackql/stackql).
