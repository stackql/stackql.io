---
title: AWS_POLICY_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - инвентаризация облачных ресурсов
  - aws
  - политика IAM
description: Сравнение двух документов политик AWS IAM или массивов тегов AWS на семантическую эквивалентность с помощью SQL.
image: "/img/stackql-featured-image.png"
---
Сравнивает две JSON-строки с политиками AWS IAM (или массивы тегов AWS) и возвращает `1`, если они семантически эквивалентны согласно правилам оценки политик AWS, и `0` в противном случае.  

См. также:  
[__[` SELECT `]__](/language-spec/select) [__[` DESCRIBE `]__](/language-spec/describe) [__[` JSON_EQUAL `]__](/language-spec/functions/json/json_equal) [__[` JSON_EXTRACT `]__](/language-spec/functions/json/json_extract) [[ Типы данных ]](/language-spec/data-types)
* * *
:::tip
Функция `aws_policy_equal` - это пользовательская функция расширения StackQL. Она сравнивает политики AWS IAM согласно правилам оценки политик AWS, по которым некоторые элементы (например, `Action`, `Resource`, `Principal` и `Tags`) рассматриваются как неупорядоченные множества. Если оба аргумента - JSON-массивы верхнего уровня (например, необработанный массив тегов AWS), они тоже сравниваются как неупорядоченные множества. Функция `aws_policy_equal` доступна в StackQL `0.8.175` и более поздних версиях и поддерживается только со встроенным SQL-бэкендом в памяти, который StackQL использует по умолчанию.
:::
## Синтаксис {#syntax}
```sql
SELECT AWS_POLICY_EQUAL(policy1, policy2);
```
## Аргументы {#arguments}
__*policy1*__  
JSON-строка с документом политики AWS IAM или JSON-массив тегов AWS.  
__*policy2*__  
Другая JSON-строка с документом политики AWS IAM или массивом тегов, которую нужно сравнить с первой.  
> Оба аргумента должны быть допустимым JSON. При сравнении документов политик оба аргумента должны быть допустимым JSON политики AWS IAM.
## Возвращаемое значение {#return-values}
Целое число: __1__, если документы семантически эквивалентны, __0__, если нет.
* * *
## Примеры {#examples}
### Сравнение политик AWS IAM {#compare-aws-iam-policies}
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
### Сравнение политик с разным порядком элементов {#compare-policies-with-different-element-ordering}
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
### Сравнение разных форматов политик (массив и строка) {#compare-different-policy-formats-array-vs-string}
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
### Сравнение массивов тегов AWS {#compare-aws-tags-arrays}
AWS не гарантирует порядок тегов в ответах своих API. Используйте функцию `aws_policy_equal`, чтобы сравнивать массивы тегов без учета порядка элементов.
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
### Сравнение тегов ресурса, вложенных в объект ресурса {#compare-resource-tags-nested-in-a-resource-object}
Если теги встроены в объект ресурса как поле `Tags`, функция `aws_policy_equal` по-прежнему рассматривает массив тегов как неупорядоченное множество.
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
Подробнее см. [aws_policy_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/sqlpkg/aws_policy_equal.json).
