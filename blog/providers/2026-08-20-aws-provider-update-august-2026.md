---
slug: aws-provider-update-august-2026
title: AWS Provider Update - August 2026
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-aws-provider-featured-image.png"
description: Update to the StackQL AWS provider adding nine new services including Resilience Hub V2, Lambda MicroVMs, Agent Registry and Support AuthZ, expanded coverage across EC2, ACM, QuickSight, Bedrock AgentCore and more, plus full S3 object content lifecycles - read, write, replace and delete object contents with SQL.
keywords: [stackql, aws, amazon web services, provider, s3, objects, terraform state, resilience hub, lambda microvms, agent registry, sql]
tags: [stackql, aws, amazon web services, provider, s3]
---

We've released an update to the [__StackQL AWS provider__](https://aws-provider.stackql.io/), regenerated from the latest AWS service definitions. Significant additions include:

- 9 new services
- Over 200 new resources
- Over 500 new operations
- Full support for S3 object level CRUD operations

## New Services

Nine services are new in this release:

| Service | Description |
|---------|-------------|
| `resiliencehubv2` | The next generation of AWS Resilience Hub - assess and improve the resilience of critical applications at scale, the largest new service in this release |
| `lambda_microvms` | Create, manage and operate AWS Lambda MicroVMs and their associated MicroVM image environments |
| `lambda_core` | Shared infrastructure resources for Lambda, including network connectors that give MicroVMs access to resources in your VPC |
| `agent_registry_control` | Managed catalog for publishing and discovering MCP servers, agents and agent skills - control plane for registries and records |
| `agent_registry` | Data-plane discovery of approved records published to an Agent Registry |
| `account_access` | Account access manager - manage applications and entitlements that grant IAM Identity Center principals access to IAM roles across accounts |
| `supportauthz` | Support authorization - cryptographically signed support permits controlling which actions AWS support operators can perform on your resources |
| `pricing_plan_manager` | Flat-rate pricing subscriptions - create, approve and cancel subscriptions and associate resources with them |
| `sagemakerjobruntime` | Agentic RFT runtime - trajectory and transition data for reinforcement fine-tuning jobs |

## Expanded Coverage in Existing Services

- `ec2` - the largest operation count increase in this release: account-level VPC encryption controls (`account_vpc_encryption_controls`), application status checks, capacity reservation cancellation quotes, and a major IPAM build-out covering internet registry associations, route origin authorizations, route protection findings and discovered routes
- `acm` - public ACME issuance: `acme_accounts`, `acme_endpoints`, `acme_domain_validations` and external account bindings, plus per-domain certificate validation status
- `quicksight` - agentic BI: `agents`, `spaces`, `knowledge_bases`, approval policies, DLP settings and OAuth client applications
- `bedrock_agentcore_control` - `datasets`, dataset versions and examples, evaluation `harness_endpoints` and versions, capacity providers and gateway rate limits
- `wellarchitected` - AI-assisted reviews: agent profiles, goals, contexts and `agent_recommendations` with per-item detail
- `odb` (Oracle Database@AWS) - autonomous database coverage: `autonomous_databases`, backups, clones, peers, wallet details, Exadata VM clusters and Exascale storage vaults
- `iotsitewise` - industrial data pipelines: `pipelines`, pipeline executions, enrichment jobs, dataset export jobs, ad hoc `queries` and search
- `glue` - business catalog additions: `assets`, `asset_types`, `glossaries` and `glossary_terms`
- `drs` - orchestrated disaster recovery: `recovery_plans`, plan steps and execution tracking
- `billing` - `credits`, credit allocation histories, billing preferences and enterprise support charge summaries
- `appconfig` - feature experiments: `experiment_definitions`, runs and run events
- `healthlake` - FHIR data transformation profiles and jobs

Another 180+ services picked up incremental operations and resources, including `connect`, `securityagent`, `cleanrooms`, `socialmessaging`, `devops_agent` and `artifact`.

## S3 Object Content Lifecycles

The headline feature in this release: `aws.s3.objects` now supports the full content lifecycle for text objects. The object body is projected as a single `contents` column, so you can read, write, overwrite and delete object contents with standard SQL verbs - no SDK code, no presigned URLs.

The motivating case is reading a Terraform state file straight out of S3:

```sql
SELECT contents FROM aws.s3.objects
WHERE region = 'ap-southeast-2'
AND bucket = 'my-bucket'
AND key = 'env/terraform.tfstate';
```

Writing works through the same resource. `INSERT` creates an object, and since S3 `PutObject` is create-or-overwrite, `REPLACE` updates it in place:

```sql
-- create an object
INSERT INTO aws.s3.objects(region, bucket, key, contents)
SELECT 'ap-southeast-2', 'my-bucket', 'app/config.json',
'{"feature_flags": {"dark_mode": true}, "log_level": "info"}';

-- overwrite its contents
REPLACE aws.s3.objects
SET contents = '{"feature_flags": {"dark_mode": false}, "log_level": "warn"}'
WHERE region = 'ap-southeast-2'
AND bucket = 'my-bucket'
AND key = 'app/config.json';

-- delete it
DELETE FROM aws.s3.objects
WHERE region = 'ap-southeast-2'
AND bucket = 'my-bucket'
AND key = 'app/config.json';
```

Listing is unchanged - a `WHERE` clause with just `bucket` and `region` routes to the list operation, adding `key` routes to the object read:

```sql
SELECT key, size, last_modified FROM aws.s3.objects
WHERE region = 'ap-southeast-2' AND bucket = 'my-bucket';
```

Content round-trips byte-for-byte, including multi-MB objects and objects uploaded via multipart upload. Text objects only for now - binary content is not supported and base64 support is deferred.


## Get Started

Pull the latest provider from the public registry:

```bash
stackql registry pull aws;
```

Authenticate with `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` environment variables, then explore:

```sql
SELECT vpc_id, state, cidr_block, is_default
FROM aws.ec2.vpcs
WHERE region = 'us-east-1';
```

Provider docs, including required parameters and example queries for every resource, are at [aws-provider.stackql.io](https://aws-provider.stackql.io/). Visit us on [__GitHub__](https://github.com/stackql/stackql) and let us know how you're using it.
