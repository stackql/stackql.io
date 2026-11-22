---
title: How to query S3 buckets with StackQL
description: List S3 buckets and their regions with the aws.s3.buckets table and retrieve per-bucket configuration - encryption, versioning, public access blocks - from the awscc.s3.buckets Cloud Control table keyed by Identifier.
keywords: [stackql, aws, s3, buckets, sql, cloud security, public access]
proficiencyLevel: Beginner
faq:
  - question: Why do S3 bucket queries use region = 'us-east-1' when buckets are global?
    answer: Bucket names are globally unique, but region is still a required routing parameter of the list method; us-east-1 returns the account's bucket list. Each bucket's home region is the bucket_region column of that list.
  - question: How do I get the configuration of a specific bucket?
    answer: Query the Cloud Control table awscc.s3.buckets with the bucket name as Identifier. The row includes configuration such as encryption, versioning, public access block, logging, and tags.
  - question: Can StackQL create or delete S3 buckets?
    answer: Yes. aws.s3.buckets exposes INSERT (create_bucket) and DELETE (delete_bucket) methods, and the Cloud Control table awscc.s3.buckets exposes INSERT (create_resource), UPDATE (update_resource), and DELETE (delete_resource). SHOW INSERT INTO awscc.s3.buckets generates a template of the writable properties.
---

# How to query S3 buckets with StackQL

S3 buckets are exposed as two related tables: `aws.s3.buckets` lists the buckets in the account, and `awscc.s3.buckets` returns the full configuration of one bucket identified by `Identifier`. The first is built on the S3 API in the `aws` provider, the second on the AWS Cloud Control API in the `awscc` provider.

## Prerequisites {#prerequisites}

- AWS credentials exported - see [How to authenticate StackQL to AWS](/ai/how-tos/authenticate-stackql-to-aws)
- The AWS providers installed: `REGISTRY PULL aws;` and `REGISTRY PULL awscc;`

## List buckets {#list-buckets}

```sql
SELECT name, bucket_region, creation_date
FROM aws.s3.buckets
WHERE region = 'us-east-1';
```

The `region` predicate is a required routing parameter of the underlying API call; `us-east-1` returns the account's buckets.

## Inspect one bucket {#inspect-one-bucket}

The Cloud Control table returns configuration attributes for a named bucket:

```sql
SELECT
  bucket_name,
  bucket_encryption,
  versioning_configuration,
  public_access_block_configuration
FROM awscc.s3.buckets
WHERE region = 'us-east-1'
AND Identifier = 'my-bucket-name';
```

Configuration columns return structured JSON; `DESCRIBE awscc.s3.buckets` lists everything available, including `logging_configuration`, `lifecycle_configuration`, `object_lock_configuration`, `tags`, and `arn`.

## Audit pattern: enumerate, then inspect {#audit-pattern-enumerate-then-inspect}

Account-wide configuration audits combine the two tables: list the bucket names, then query the detail table per bucket. In scripted use (shell loops, or Python via pystackql), iterate over the list result and collect the detail rows - per-bucket checks such as "is versioning enabled?" or "is the public access block complete?" become row predicates over the collected results. This is the documented pattern for S3 inventory in the StackQL AWS tutorials.

## Creating buckets {#creating-buckets}

Writable properties are discoverable the same way readable ones are:

```sql
SHOW INSERT INTO awscc.s3.buckets;
```

This generates an `INSERT` template covering the resource's writable fields, as the desired-state document Cloud Control takes. Restricting to mandatory fields only is done with the `/*+ REQUIRED */` query hint.

## Related concepts {#related-concepts}

- [How to query AWS EC2 instances with StackQL](/ai/how-tos/query-aws-ec2-instances-with-stackql) - the regional-service pattern
- [How to authenticate StackQL to AWS](/ai/how-tos/authenticate-stackql-to-aws) - credential setup
- [What is SQL for APIs?](/ai/canonical-definitions/what-is-sql-for-apis) - why list and detail are separate methods
- [What is Queryable Infrastructure?](/ai/canonical-definitions/what-is-queryable-infrastructure) - the audit use case in context
- [Common StackQL errors](/ai/troubleshooting/common-stackql-errors) - missing required parameters explained
