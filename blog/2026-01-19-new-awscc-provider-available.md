---
slug: new-awscc-provider-available
title: New Dedicated AWS Cloud Control Provider Released
hide_table_of_contents: false
authors:
  - jeffreyaven
image: "/img/blog/stackql-aws-provider-featured-image.png"
description: Introducing the standalone StackQL AWS Cloud Control provider with full CRUDL support, optimized resource naming, and comprehensive coverage of AWS Cloud Control API resources.
keywords: [stackql, aws, awscc, cloud control, provider, infrastructure-as-code, iac]
tags: [stackql, aws, awscc, cloud control, provider, infrastructure-as-code]
---

We've released a new dedicated [__StackQL AWS Cloud Control provider__](https://awscc-provider.stackql.io), providing full CRUDL operations across AWS services via the Cloud Control API including purpose-built resource definitions leveraging Cloud Control's consistent schema.

## Resource Naming Convention

Resources follow a clear pattern to differentiate operations:

| Resource Pattern | Operations | Use Case |
|------------------|------------|----------|
| `{resource}` (e.g., `s3.buckets`) | SELECT, INSERT, UPDATE, DELETE | Full CRUD with complete resource properties |
| `{resource}_list_only` (e.g., `s3.buckets_list_only`) | SELECT | Fast enumeration of resource identifiers |

This separation means listing thousands of resources won't trigger rate limits from individual GET calls:
```sql
-- Fast enumeration (list operation only)
SELECT bucket_name 
FROM awscc.s3.buckets_list_only 
WHERE region = 'us-east-1';

-- Full resource details (get operation)
SELECT * 
FROM awscc.s3.buckets 
WHERE region = 'us-east-1' 
AND data__Identifier = 'my-bucket';
```

## Provider Coverage

The `awscc` provider includes:

- **237 services** and **2371 resources** covering the breadth of AWS
- **Full CRUDL support** for all Cloud Control compatible resources
- **Consistent schema** derived from AWS CloudFormation resource specifications

## Example Operations

### Create an S3 Bucket
```sql
INSERT INTO awscc.s3.buckets (
  BucketName,
  region
)
SELECT 
  'my-new-bucket',
  'us-east-1';
```

### Query EC2 Instances
```sql
SELECT 
  instance_id,
  instance_type,
  tags
FROM awscc.ec2.instances
WHERE region = 'ap-southeast-2'
AND data__Identifier = 'i-1234567890abcdef0';
```

### Delete a Resource
```sql
DELETE FROM awscc.lambda.functions
WHERE data__Identifier = 'my-function'
AND region = 'us-east-1';
```

## Enhanced Documentation

The provider documentation at [awscc.stackql.io](https://awscc.stackql.io/providers/awscc/) now features:

- **Interactive schema explorer** with expandable nested property trees
- **Complete field documentation** including complex object structures
- **Ready-to-use SQL examples** for SELECT, INSERT, and DELETE operations
- **IAM permissions reference** for each resource operation

## Get Started

Pull the new provider:
```bash
stackql registry pull awscc
```

Query your AWS resources:
```bash
stackql shell
>> SELECT region, bucket_name FROM awscc.s3.buckets_list_only WHERE region = 'us-east-1';
```
Let us know your thoughts! Visit us and give us a star on [__GitHub__](https://github.com/stackql/stackql).