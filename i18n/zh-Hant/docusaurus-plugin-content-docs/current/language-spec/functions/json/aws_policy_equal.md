---
title: AWS_POLICY_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - 雲端資源清單
  - aws
  - IAM 政策
description: 使用 SQL 比較兩份 AWS IAM 政策文件或 AWS 標籤陣列在語意上是否相等。
image: "/img/stackql-featured-image.png"
---
比較兩個 AWS IAM 政策 JSON 字串（或 AWS 標籤陣列），若依據 AWS 政策評估規則在語意上相等則傳回 `1`，否則傳回 `0`。  

另請參閱：  
[__[` SELECT `]__](/language-spec/select) [__[` DESCRIBE `]__](/language-spec/describe) [__[` JSON_EQUAL `]__](/language-spec/functions/json/json_equal) [__[` JSON_EXTRACT `]__](/language-spec/functions/json/json_extract) [[ 資料型別 ]](/language-spec/data-types)
* * *
:::tip
`aws_policy_equal` 函式是 StackQL 的自訂擴充函式。它依據 AWS 政策評估規則比較 AWS IAM 政策，其中某些元素（例如 `Action`、`Resource`、`Principal` 與 `Tags`）會被視為無序集合。當兩個引數都是最上層 JSON 陣列（例如原始的 AWS 標籤陣列）時，也會以無序集合進行比較。`aws_policy_equal` 函式適用於 StackQL `0.8.175` 及以上版本，且僅支援 StackQL 預設的內嵌式記憶體內 SQL 後端。
:::
## 語法 {#syntax}
```sql
SELECT AWS_POLICY_EQUAL(policy1, policy2);
```
## 引數 {#arguments}
__*policy1*__  
表示 AWS IAM 政策文件的 JSON 字串，或 AWS 標籤的 JSON 陣列。  
__*policy2*__  
另一個表示 AWS IAM 政策文件或標籤陣列的 JSON 字串，用來與第一個引數比較。  
> 兩個引數都必須是有效的 JSON。比較政策文件時，兩者都必須是有效的 AWS IAM 政策 JSON。
## 傳回值 {#return-values}
整數值：文件在語意上相等時為 __1__，不相等時為 __0__。
* * *
## 範例 {#examples}
### 比較 AWS IAM 政策 {#compare-aws-iam-policies}
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
### 比較元素順序不同的政策 {#compare-policies-with-different-element-ordering}
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
### 比較不同的政策格式（陣列與字串） {#compare-different-policy-formats-array-vs-string}
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
### 比較 AWS 標籤陣列 {#compare-aws-tags-arrays}
AWS 不保證其 API 傳回之標籤的順序。請使用 `aws_policy_equal` 比較標籤陣列，而不考慮元素順序。
```sql
SELECT
  aws_policy_equal(
    tags,
    '[{"Key":"stackql:stack-name","Value":"stackql-serverless"},{"Key":"stackql:stack-env","Value":"dev"},{"Key":"stackql:resource-name","Value":"aws_s3_workspace_bucket"}]'
  ) as tags_match
FROM awscc.s3.buckets
WHERE region = 'us-east-1'
AND Identifier = 'my-workspace-bucket';
/* returns...
|------------|
| tags_match |
|------------|
|          1 |
|------------|
-- Returns 1 regardless of the order AWS returns the tags array
*/
```
### 比較巢狀於資源物件中的資源標籤 {#compare-resource-tags-nested-in-a-resource-object}
當標籤以 `Tags` 欄位內嵌於資源物件中時，`aws_policy_equal` 仍會將標籤陣列視為無序集合。
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
如需詳細資訊，請參閱 [aws_policy_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/sqlpkg/aws_policy_equal.json)。
