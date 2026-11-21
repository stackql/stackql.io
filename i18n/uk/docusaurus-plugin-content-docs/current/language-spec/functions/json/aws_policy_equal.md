---
title: AWS_POLICY_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - інвентаризація хмарних ресурсів
  - aws
  - політика IAM
description: Порівняння двох документів політик AWS IAM або масивів тегів AWS на семантичну еквівалентність за допомогою SQL.
image: "/img/stackql-featured-image.png"
---
Порівнює два рядки JSON із політиками AWS IAM (або масиви тегів AWS) і повертає `1`, якщо вони семантично еквівалентні згідно з правилами оцінювання політик AWS, і `0` в іншому разі.  

Див. також:  
[__[` SELECT `]__](/language-spec/select) [__[` DESCRIBE `]__](/language-spec/describe) [__[` JSON_EQUAL `]__](/language-spec/functions/json/json_equal) [__[` JSON_EXTRACT `]__](/language-spec/functions/json/json_extract) [[ Типи даних ]](/language-spec/data-types)
* * *
:::tip
Функція `aws_policy_equal` - це власна функція розширення StackQL. Вона порівнює політики AWS IAM згідно з правилами оцінювання політик AWS, за якими певні елементи (як-от `Action`, `Resource`, `Principal` і `Tags`) розглядаються як невпорядковані множини. Якщо обидва аргументи є масивами JSON верхнього рівня (наприклад, необробленим масивом тегів AWS), вони також порівнюються як невпорядковані множини. Функція `aws_policy_equal` доступна в StackQL версії `0.8.175` і новіших і підтримується лише з вбудованим бекендом SQL у пам'яті, який StackQL використовує за замовчуванням.
:::
## Синтаксис {#syntax}
```sql
SELECT AWS_POLICY_EQUAL(policy1, policy2);
```
## Аргументи {#arguments}
__*policy1*__  
Рядок JSON, що містить документ політики AWS IAM, або масив JSON із тегами AWS.  
__*policy2*__  
Інший рядок JSON, що містить документ політики AWS IAM або масив тегів, для порівняння з першим.  
> Обидва аргументи мають бути допустимим JSON. Під час порівняння документів політик обидва мають бути допустимим JSON політики AWS IAM.
## Значення, що повертається {#return-values}
Ціле число: __1__, якщо документи семантично еквівалентні, і __0__, якщо ні.
* * *
## Приклади {#examples}
### Порівняння політик AWS IAM {#compare-aws-iam-policies}
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
### Порівняння політик із різним порядком елементів {#compare-policies-with-different-element-ordering}
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
### Порівняння різних форматів політик (масив і рядок) {#compare-different-policy-formats-array-vs-string}
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
### Порівняння масивів тегів AWS {#compare-aws-tags-arrays}
AWS не гарантує порядку тегів, які повертають API AWS. Скористайтеся функцією `aws_policy_equal`, щоб порівнювати масиви тегів без урахування порядку елементів.
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
### Порівняння тегів ресурсу, вкладених в об'єкт ресурсу {#compare-resource-tags-nested-in-a-resource-object}
Коли теги вбудовано в об'єкт ресурсу як поле `Tags`, функція `aws_policy_equal` все одно розглядає масив тегів як невпорядковану множину.
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
Докладніше див. [aws_policy_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/sqlpkg/aws_policy_equal.json).
