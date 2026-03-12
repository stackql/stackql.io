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
description: Compare two AWS IAM policy documents or AWS tags arrays for semantic equivalence using SQL.
image: "/img/stackql-featured-image.png"
---
Compares two AWS IAM policy JSON strings (or AWS tags arrays) and returns `true` if they are semantically equivalent according to AWS policy evaluation rules, `false` otherwise.  
See also:  
[__[` SELECT `]__](/docs/language-spec/select) [__[` DESCRIBE `]__](/docs/language-spec/describe) [__[` JSON_EQUAL `]__](/docs/language-spec/functions/json/json_equal) [__[` JSON_EXTRACT `]__](/docs/language-spec/functions/json/json_extract) [__[ Data Types ]__](/docs/language-spec/data-types)
* * *
:::tip
The `aws_policy_equal` function is a custom StackQL extension function. It handles AWS IAM policy comparison according to AWS policy evaluation rules, where certain elements (like `Action`, `Resource`, `Principal`, and `Tags`) are treated as unordered sets. When both arguments are top-level JSON arrays (e.g. a raw AWS tags array), they are also compared as unordered sets. The `aws_policy_equal` function is available in StackQL versions `0.8.175` and above and is only supported with the default embedded, in-memory SQL backend for StackQL.
:::
## Syntax
```sql
SELECT AWS_POLICY_EQUAL(policy1, policy2);
```
## Arguments
__*policy1*__  
A JSON string representing an AWS IAM policy document, or a JSON array of AWS tags.  
__*policy2*__  
Another JSON string representing an AWS IAM policy document or tags array to compare against the first.  
> Both arguments must be valid JSON. When comparing policy documents, both must be valid AWS IAM policy JSON.
## Return Value(s)
An integer value: __1__ if the documents are semantically equivalent, __0__ if they are not.
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
### Compare AWS Tags Arrays
AWS does not guarantee the order of tags returned by its APIs. Use `aws_policy_equal` to compare tags arrays without regard to element order.
```sql
SELECT
  aws_policy_equal(
    tags,
    '[{"Key":"stackql:stack-name","Value":"stackql-serverless"},{"Key":"stackql:stack-env","Value":"dev"},{"Key":"stackql:resource-name","Value":"aws_s3_workspace_bucket"}]'
  ) as tags_match
FROM aws.s3.buckets
WHERE region = 'us-east-1'
AND name = 'my-workspace-bucket';
/* returns...
|------------|
| tags_match |
|------------|
|          1 |
|------------|
-- Returns 1 regardless of the order AWS returns the tags array
*/
```
### Compare Resource Tags Nested in a Resource Object
When tags are embedded inside a resource object as a `Tags` field, `aws_policy_equal` still treats the tags array as an unordered set.
```sql
SELECT
  aws_policy_equal(
    '{"BucketName":"my-bucket","Tags":[{"Key":"env","Value":"prod"},{"Key":"team","Value":"platform"}]}',
    '{"BucketName":"my-bucket","Tags":[{"Key":"team","Value":"platform"},{"Key":"env","Value":"prod"}]}'
  ) as nested_tags_equal
;
/* returns...
|--------------------|
| nested_tags_equal  |
|--------------------|
|                  1 |
|--------------------|
*/
```
For more information, see [aws_policy_equal](__https://github.com/stackql/sqlite-ext-functions/blob/main/docs/aws_policy_equal.md__).