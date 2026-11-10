---
title: AWS_POLICY_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - Cloud-Inventar
  - aws
  - IAM-Richtlinie
description: Zwei AWS IAM-Richtliniendokumente oder AWS-Tags-Arrays mit SQL auf semantische Äquivalenz vergleichen.
image: "/img/stackql-featured-image.png"
---
Vergleicht zwei JSON-Zeichenketten mit AWS IAM-Richtlinien (oder AWS-Tags-Arrays) und gibt `true` zurück, wenn sie gemäß den Regeln der AWS-Richtlinienauswertung semantisch äquivalent sind, andernfalls `false`.  

Siehe auch:  
[__[` SELECT `]__](/language-spec/select) [__[` DESCRIBE `]__](/language-spec/describe) [__[` JSON_EQUAL `]__](/language-spec/functions/json/json_equal) [__[` JSON_EXTRACT `]__](/language-spec/functions/json/json_extract) [[ Datentypen ]](/language-spec/data-types)
* * *
:::tip
Die Funktion `aws_policy_equal` ist eine benutzerdefinierte StackQL-Erweiterungsfunktion. Sie vergleicht AWS IAM-Richtlinien gemäß den Regeln der AWS-Richtlinienauswertung, nach denen bestimmte Elemente (wie `Action`, `Resource`, `Principal` und `Tags`) als ungeordnete Mengen behandelt werden. Wenn beide Argumente JSON-Arrays auf oberster Ebene sind (z. B. ein rohes AWS-Tags-Array), werden sie ebenfalls als ungeordnete Mengen verglichen. Die Funktion `aws_policy_equal` ist in StackQL ab Version `0.8.175` verfügbar und wird nur mit dem standardmäßigen eingebetteten In-Memory-SQL-Backend von StackQL unterstützt.
:::
## Syntax {#syntax}
```sql
SELECT AWS_POLICY_EQUAL(policy1, policy2);
```
## Argumente {#arguments}
__*policy1*__  
Eine JSON-Zeichenkette, die ein AWS IAM-Richtliniendokument darstellt, oder ein JSON-Array mit AWS-Tags.  
__*policy2*__  
Eine weitere JSON-Zeichenkette, die ein AWS IAM-Richtliniendokument oder Tags-Array darstellt und mit der ersten verglichen wird.  
> Beide Argumente müssen gültiges JSON sein. Beim Vergleich von Richtliniendokumenten müssen beide gültiges AWS IAM-Richtlinien-JSON sein.
## Rückgabewert(e) {#return-values}
Ein ganzzahliger Wert: __1__, wenn die Dokumente semantisch äquivalent sind, __0__, wenn nicht.
* * *
## Beispiele {#examples}
### AWS IAM-Richtlinien vergleichen {#compare-aws-iam-policies}
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
### Richtlinien mit unterschiedlicher Elementreihenfolge vergleichen {#compare-policies-with-different-element-ordering}
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
### Unterschiedliche Richtlinienformate vergleichen (Array vs. Zeichenkette) {#compare-different-policy-formats-array-vs-string}
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
### AWS-Tags-Arrays vergleichen {#compare-aws-tags-arrays}
AWS garantiert nicht die Reihenfolge der von seinen APIs zurückgegebenen Tags. Verwenden Sie `aws_policy_equal`, um Tags-Arrays unabhängig von der Elementreihenfolge zu vergleichen.
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
### In einem Ressourcenobjekt verschachtelte Ressourcen-Tags vergleichen {#compare-resource-tags-nested-in-a-resource-object}
Wenn Tags als Feld `Tags` in ein Ressourcenobjekt eingebettet sind, behandelt `aws_policy_equal` das Tags-Array weiterhin als ungeordnete Menge.
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
Weitere Informationen finden Sie unter [aws_policy_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/sqlpkg/aws_policy_equal.json).
