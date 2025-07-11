---
title: AWS_POLICY_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - cloud inventory
  - aws
  - iam policy
description: Compare two AWS IAM policy documents for semantic equivalence using SQL.
image: "/img/stackql-featured-image.png"
---
Compares two AWS IAM policy JSON strings and returns `true` if they are semantically equivalent according to AWS policy evaluation rules, `false` otherwise.

See also:  
[[` SELECT `]](/docs/language-spec/select) [[` DESCRIBE `]](/docs/language-spec/describe) [[` JSON_EQUAL `]](/docs/language-spec/functions/json/json_equal) [[` JSON_EXTRACT `]](/docs/language-spec/functions/json/json_extract) [[ Data Types ]](/docs/language-spec/data-types)

* * * 

:::tip 

The `aws_policy_equal` function is a custom StackQL extension function. It handles AWS IAM policy comparison according to AWS policy evaluation rules, where certain elements (like Action, Resource, and Principal) are treated as unordered sets. The `aws_policy_equal` function is available in StackQL versions `0.8.175` and above and is only supported with the default embedded, in-memory SQL backend for StackQL.

:::

## Syntax

```sql
SELECT AWS_POLICY_EQUAL(policy1, policy2);
```

## Arguments

__*policy1*__  
A JSON string representing an AWS IAM policy document.

__*policy2*__  
Another JSON string representing an AWS IAM policy document to compare against the first.

> Both arguments must be valid AWS IAM policy JSON documents.

## Return Value(s)
An integer value: 1 if the policy documents are semantically equivalent, 0 if they are not.

* * *

## Examples

### Compare AWS IAM Policies

```sql
SELECT 
assume_role_policy_document as current_policy,
'{"Version":"2012-10-17","Statement":[{"Effect":"Allow","Principal":{"Service":"lambda.amazonaws.com"},"Action":"sts:AssumeRole"}]}' as desired_policy,
AWS_POLICY_EQUAL(assume_role_policy_document, '{"Version":"2012-10-17","Statement":[{"Effect":"Allow","Principal":{"Service":"lambda.amazonaws.com"},"Action":"sts:AssumeRole"}]}') as compliant
FROM aws.iam.roles
WHERE role_name = 'my-lambda-role';
/* returns...
|------------------------------------------------------------------|------------------------------------------------------------------|-----------|
|                            current_policy                        |                           desired_policy                         | compliant |
|------------------------------------------------------------------|------------------------------------------------------------------|-----------|
| {"Version":"2012-10-17","Statement":[{"Effect":"Allow",          | {"Version":"2012-10-17","Statement":[{"Effect":"Allow",          |         1 |
|  "Principal":{"Service":"lambda.amazonaws.com"},                 |  "Principal":{"Service":"lambda.amazonaws.com"},                 |           |
|  "Action":"sts:AssumeRole"}]}                                    |  "Action":"sts:AssumeRole"}]}                                    |           |
|------------------------------------------------------------------|------------------------------------------------------------------|-----------|
*/
```

### Compare Policies with Different Element Ordering

```sql
SELECT 
  aws_policy_equal(
    '{"Version":"2012-10-17","Statement":[{"Effect":"Allow","Action":["s3:GetObject","s3:PutObject"],"Resource":"*"}]}',
    '{"Version":"2012-10-17","Statement":[{"Effect":"Allow","Action":["s3:PutObject","s3:GetObject"],"Resource":"*"}]}'
  ) as unordered_action_equal,
  aws_policy_equal(
    '{"Version":"2012-10-17","Statement":[{"Condition":{"StringEquals":{"sts:ExternalId":"0000"}},"Action":"sts:AssumeRole","Effect":"Allow","Principal":{"AWS":"arn:aws:iam::123456789012:role/role1"}}]}',
    '{"Version":"2012-10-17","Statement":[{"Effect":"Allow","Principal":{"AWS":"arn:aws:iam::123456789012:role/role1"},"Action":"sts:AssumeRole","Condition":{"StringEquals":{"sts:ExternalId":"0000"}}}]}'
  ) as reordered_elements_equal
; 
/* returns...
|------------------------|--------------------------|
| unordered_action_equal | reordered_elements_equal |
|------------------------|--------------------------|
|                      1 |                        1 |
|------------------------|--------------------------|
*/
```

### Compare Different Policy Formats (Array vs String)

```sql
SELECT 
  aws_policy_equal(
    '{"Version":"2012-10-17","Statement":[{"Effect":"Allow","Action":["s3:GetObject"],"Resource":"*"}]}',
    '{"Version":"2012-10-17","Statement":[{"Effect":"Allow","Action":"s3:GetObject","Resource":"*"}]}'
  ) as array_string_equal
; 
/* returns...
|--------------------|
| array_string_equal |
|--------------------|
|                  1 |
|--------------------|
*/
```

For more information, see [aws_policy_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/docs/aws_policy_equal.md).