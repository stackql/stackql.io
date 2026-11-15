---
title: AWS_POLICY_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 클라우드 인벤토리
  - aws
  - IAM 정책
description: SQL로 두 AWS IAM 정책 문서 또는 AWS 태그 배열이 의미상 동등한지 비교합니다.
image: "/img/stackql-featured-image.png"
---
두 AWS IAM 정책 JSON 문자열(또는 AWS 태그 배열)을 비교하여 AWS 정책 평가 규칙에 따라 의미상 동등하면 `true`를, 그렇지 않으면 `false`를 반환합니다.  

참고 항목:  
[__[` SELECT `]__](/language-spec/select) [__[` DESCRIBE `]__](/language-spec/describe) [__[` JSON_EQUAL `]__](/language-spec/functions/json/json_equal) [__[` JSON_EXTRACT `]__](/language-spec/functions/json/json_extract) [[ 데이터 형식 ]](/language-spec/data-types)
* * *
:::tip
`aws_policy_equal` 함수는 StackQL 사용자 지정 확장 함수입니다. 이 함수는 AWS 정책 평가 규칙에 따라 AWS IAM 정책을 비교하며, 이 규칙에서는 특정 요소(`Action`, `Resource`, `Principal`, `Tags` 등)를 순서가 없는 집합으로 처리합니다. 두 인수가 모두 최상위 JSON 배열(예: 원시 AWS 태그 배열)이면 이 경우에도 순서가 없는 집합으로 비교합니다. `aws_policy_equal` 함수는 StackQL `0.8.175` 이상에서 사용할 수 있으며, StackQL의 기본 임베디드 인메모리 SQL 백엔드에서만 지원됩니다.
:::
## 구문 {#syntax}
```sql
SELECT AWS_POLICY_EQUAL(policy1, policy2);
```
## 인수 {#arguments}
__*policy1*__  
AWS IAM 정책 문서를 나타내는 JSON 문자열 또는 AWS 태그의 JSON 배열입니다.  
__*policy2*__  
첫 번째 인수와 비교할 AWS IAM 정책 문서 또는 태그 배열을 나타내는 또 다른 JSON 문자열입니다.  
> 두 인수는 모두 유효한 JSON이어야 합니다. 정책 문서를 비교할 때는 두 인수 모두 유효한 AWS IAM 정책 JSON이어야 합니다.
## 반환 값 {#return-values}
정수 값입니다. 문서가 의미상 동등하면 __1__ 값, 그렇지 않으면 __0__ 값입니다.
* * *
## 예제 {#examples}
### AWS IAM 정책 비교 {#compare-aws-iam-policies}
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
### 요소 순서가 다른 정책 비교 {#compare-policies-with-different-element-ordering}
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
### 서로 다른 정책 형식 비교(배열 대 문자열) {#compare-different-policy-formats-array-vs-string}
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
### AWS 태그 배열 비교 {#compare-aws-tags-arrays}
AWS는 API가 반환하는 태그의 순서를 보장하지 않습니다. 요소 순서와 관계없이 태그 배열을 비교하려면 `aws_policy_equal` 함수를 사용하세요.
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
### 리소스 객체에 중첩된 리소스 태그 비교 {#compare-resource-tags-nested-in-a-resource-object}
태그가 리소스 객체 안에 `Tags` 필드로 포함된 경우에도 `aws_policy_equal` 함수는 태그 배열을 순서가 없는 집합으로 처리합니다.
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
자세한 내용은 [aws_policy_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/sqlpkg/aws_policy_equal.json)을 참조하세요.
