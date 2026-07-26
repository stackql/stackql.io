---
title: Lambda functions in a region
description: Enumerates Lambda function names in one region via the list-only resource.
format: md
verb: select
status: draft
providers: [aws]
services: [lambda]
tags: [aws, lambda, serverless, inventory]
keywords: [function list, lambda inventory, serverless functions]
intent_keywords:
  - list lambda functions
  - what lambda functions exist
  - lambda function inventory
auth: [AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY]
params:
  - name: region
    type: identifier
    required: true
    description: Region to list functions in
    example: us-east-1
outputs:
  - name: function_name
    type: string
    description: Lambda function name
  - name: region
    type: string
    description: Region the function is deployed in
cost:
  fan_out: region
  expensive: false
  notes: One list call per region when swept account-wide
related: [aws/ec2/regions-enabled]
---

Enumerates every Lambda function in a single region. This is the cheap
identifier-only path; per-function configuration (runtime, memory, role,
environment) is a keyed read against aws.lambda.functions with the function
name.

## Query

```sql
SELECT function_name, region FROM aws.lambda.functions_list_only WHERE region = '{{region}}';
```

## Notes

Lambda is regional: fan out over the enabled regions from
aws/ec2/regions-enabled for an account-wide inventory. For configuration
detail, read aws.lambda.functions with data__Identifier set to the function
name, one request per function.
