---
title: AWS_POLICY_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - クラウドインベントリ
  - aws
  - IAM ポリシー
description: SQL を使って、2 つの AWS IAM ポリシードキュメントまたは AWS タグ配列が意味的に等価かどうかを比較します。
image: "/img/stackql-featured-image.png"
---
2 つの AWS IAM ポリシーの JSON 文字列（または AWS タグ配列）を比較し、AWS のポリシー評価ルールに従って意味的に等価であれば `1` を、そうでなければ `0` を返します。  

関連項目：  
[__[` SELECT `]__](/language-spec/select) [__[` DESCRIBE `]__](/language-spec/describe) [__[` JSON_EQUAL `]__](/language-spec/functions/json/json_equal) [__[` JSON_EXTRACT `]__](/language-spec/functions/json/json_extract) [[ データ型 ]](/language-spec/data-types)
* * *
:::tip
`aws_policy_equal` 関数は、StackQL 独自の拡張関数です。AWS のポリシー評価ルールに従って AWS IAM ポリシーを比較し、特定の要素（`Action`、`Resource`、`Principal`、`Tags` など）を順序のない集合として扱います。両方の引数がトップレベルの JSON 配列（たとえば AWS タグ配列そのもの）の場合も、順序のない集合として比較されます。`aws_policy_equal` 関数は StackQL バージョン `0.8.175` 以降で利用でき、StackQL のデフォルトである組み込みのインメモリ SQL バックエンドでのみサポートされています。
:::
## 構文 {#syntax}
```sql
SELECT AWS_POLICY_EQUAL(policy1, policy2);
```
## 引数 {#arguments}
__*policy1*__  
AWS IAM ポリシードキュメントを表す JSON 文字列、または AWS タグの JSON 配列。  
__*policy2*__  
最初の引数と比較する、AWS IAM ポリシードキュメントまたはタグ配列を表すもう 1 つの JSON 文字列。  
> 両方の引数が有効な JSON である必要があります。ポリシードキュメントを比較する場合は、両方が有効な AWS IAM ポリシーの JSON である必要があります。
## 戻り値 {#return-values}
整数値です。ドキュメントが意味的に等価であれば __1__、等価でなければ __0__ になります。
* * *
## 例 {#examples}
### AWS IAM ポリシーを比較する {#compare-aws-iam-policies}
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
### 要素の順序が異なるポリシーを比較する {#compare-policies-with-different-element-ordering}
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
### 異なるポリシー形式を比較する（配列と文字列） {#compare-different-policy-formats-array-vs-string}
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
### AWS タグ配列を比較する {#compare-aws-tags-arrays}
AWS は、API が返すタグの順序を保証しません。要素の順序に関係なくタグ配列を比較するには、`aws_policy_equal` を使用します。
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
### リソースオブジェクト内にネストされたリソースタグを比較する {#compare-resource-tags-nested-in-a-resource-object}
タグが `Tags` フィールドとしてリソースオブジェクト内に含まれている場合も、`aws_policy_equal` はタグ配列を順序のない集合として扱います。
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
詳細については、[aws_policy_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/sqlpkg/aws_policy_equal.json) を参照してください。
