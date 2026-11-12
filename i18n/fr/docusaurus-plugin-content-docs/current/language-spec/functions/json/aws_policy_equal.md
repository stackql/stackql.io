---
title: AWS_POLICY_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaire cloud
  - aws
  - politique IAM
description: Comparer deux documents de politique AWS IAM ou deux tableaux de tags AWS pour vérifier leur équivalence sémantique à l'aide de SQL.
image: "/img/stackql-featured-image.png"
---
Compare deux chaînes de caractères JSON de politique AWS IAM (ou deux tableaux de tags AWS) et renvoie `true` si elles sont sémantiquement équivalentes selon les règles d'évaluation des politiques AWS, `false` sinon.  

Voir aussi :  
[__[` SELECT `]__](/language-spec/select) [__[` DESCRIBE `]__](/language-spec/describe) [__[` JSON_EQUAL `]__](/language-spec/functions/json/json_equal) [__[` JSON_EXTRACT `]__](/language-spec/functions/json/json_extract) [[ Types de données ]](/language-spec/data-types)
* * *
:::tip
La fonction `aws_policy_equal` est une fonction d'extension personnalisée de StackQL. Elle compare les politiques AWS IAM selon les règles d'évaluation des politiques AWS, où certains éléments (comme `Action`, `Resource`, `Principal` et `Tags`) sont traités comme des ensembles non ordonnés. Lorsque les deux arguments sont des tableaux JSON de premier niveau (par exemple un tableau brut de tags AWS), ils sont également comparés comme des ensembles non ordonnés. La fonction `aws_policy_equal` est disponible dans les versions `0.8.175` et ultérieures de StackQL et n'est prise en charge qu'avec le backend SQL par défaut de StackQL, embarqué et en mémoire.
:::
## Syntaxe {#syntax}
```sql
SELECT AWS_POLICY_EQUAL(policy1, policy2);
```
## Arguments {#arguments}
__*policy1*__  
Une chaîne JSON représentant un document de politique AWS IAM, ou un tableau JSON de tags AWS.  
__*policy2*__  
Une autre chaîne JSON représentant un document de politique AWS IAM ou un tableau de tags, à comparer à la première.  
> Les deux arguments doivent être du JSON valide. Lors de la comparaison de documents de politique, les deux doivent être du JSON de politique AWS IAM valide.
## Valeur(s) renvoyée(s) {#return-values}
Une valeur entière : __1__ si les documents sont sémantiquement équivalents, __0__ sinon.
* * *
## Exemples {#examples}
### Comparer des politiques AWS IAM {#compare-aws-iam-policies}
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
### Comparer des politiques dont les éléments sont dans un ordre différent {#compare-policies-with-different-element-ordering}
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
### Comparer des formats de politique différents (tableau ou chaîne) {#compare-different-policy-formats-array-vs-string}
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
### Comparer des tableaux de tags AWS {#compare-aws-tags-arrays}
AWS ne garantit pas l'ordre des tags renvoyés par ses API. Utilisez `aws_policy_equal` pour comparer des tableaux de tags sans tenir compte de l'ordre des éléments.
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
### Comparer des tags de ressource imbriqués dans un objet de ressource {#compare-resource-tags-nested-in-a-resource-object}
Lorsque les tags sont imbriqués dans un objet de ressource sous la forme d'un champ `Tags`, `aws_policy_equal` traite toujours le tableau de tags comme un ensemble non ordonné.
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
Pour plus d'informations, consultez [aws_policy_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/sqlpkg/aws_policy_equal.json).
