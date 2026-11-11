---
title: AWS_POLICY_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 云资源清单
  - aws
  - IAM 策略
description: 使用 SQL 比较两个 AWS IAM 策略文档或 AWS 标签数组是否语义等价。
image: "/img/stackql-featured-image.png"
---
比较两个 AWS IAM 策略 JSON 字符串（或 AWS 标签数组），如果按照 AWS 策略评估规则二者语义等价，则返回 `true`，否则返回 `false`。  

另请参阅：  
[__[` SELECT `]__](/language-spec/select) [__[` DESCRIBE `]__](/language-spec/describe) [__[` JSON_EQUAL `]__](/language-spec/functions/json/json_equal) [__[` JSON_EXTRACT `]__](/language-spec/functions/json/json_extract) [[ 数据类型 ]](/language-spec/data-types)
* * *
:::tip
`aws_policy_equal` 函数是自定义的 StackQL 扩展函数。它按照 AWS 策略评估规则比较 AWS IAM 策略，其中某些元素（如 `Action`、`Resource`、`Principal` 和 `Tags`）被视为无序集合。当两个参数都是顶层 JSON 数组（例如原始的 AWS 标签数组）时，也按无序集合进行比较。`aws_policy_equal` 函数在 StackQL `0.8.175` 及以上版本中可用，并且仅支持 StackQL 默认的嵌入式内存 SQL 后端。
:::
## 语法 {#syntax}
```sql
SELECT AWS_POLICY_EQUAL(policy1, policy2);
```
## 参数 {#arguments}
__*policy1*__  
表示 AWS IAM 策略文档的 JSON 字符串，或 AWS 标签的 JSON 数组。  
__*policy2*__  
另一个表示 AWS IAM 策略文档或标签数组的 JSON 字符串，用于与第一个进行比较。  
> 两个参数都必须是有效的 JSON。比较策略文档时，两者都必须是有效的 AWS IAM 策略 JSON。
## 返回值 {#return-values}
一个整数值：如果文档语义等价则为 __1__，否则为 __0__。
* * *
## 示例 {#examples}
### 比较 AWS IAM 策略 {#compare-aws-iam-policies}
```sql
SELECT
AssumeRolePolicyDocument as current_policy,
'{"Version":"2012-10-17","Statement":[{"Effect":"Allow","Principal":{"Service":"lambda.amazonaws.com"},"Action":"sts:AssumeRole"}]}' as desired_policy,
AWS_POLICY_EQUAL(AssumeRolePolicyDocument, '{"Version":"2012-10-17","Statement":[{"Effect":"Allow","Principal":{"Service":"lambda.amazonaws.com"},"Action":"sts:AssumeRole"}]}') as compliant
FROM aws.iam.roles
WHERE RoleName = 'my-lambda-role';
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
### 比较元素顺序不同的策略 {#compare-policies-with-different-element-ordering}
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
### 比较不同的策略格式（数组与字符串） {#compare-different-policy-formats-array-vs-string}
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
### 比较 AWS 标签数组 {#compare-aws-tags-arrays}
AWS 不保证其 API 返回的标签顺序。使用 `aws_policy_equal` 可在不考虑元素顺序的情况下比较标签数组。
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
### 比较嵌套在资源对象中的资源标签 {#compare-resource-tags-nested-in-a-resource-object}
当标签作为 `Tags` 字段嵌入资源对象中时，`aws_policy_equal` 仍将标签数组视为无序集合。
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
有关更多信息，请参阅 [aws_policy_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/sqlpkg/aws_policy_equal.json)。
