---
title: AWS_POLICY_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventário de nuvem
  - aws
  - política do IAM
description: Comparar dois documentos de política do IAM da AWS ou arrays de tags da AWS quanto à equivalência semântica com SQL.
image: "/img/stackql-featured-image.png"
---
Compara duas strings JSON de política do IAM da AWS (ou arrays de tags da AWS) e retorna `1` se forem semanticamente equivalentes segundo as regras de avaliação de políticas da AWS, ou `0` caso contrário.  

Veja também:  
[__[` SELECT `]__](/language-spec/select) [__[` DESCRIBE `]__](/language-spec/describe) [__[` JSON_EQUAL `]__](/language-spec/functions/json/json_equal) [__[` JSON_EXTRACT `]__](/language-spec/functions/json/json_extract) [[ Tipos de dados ]](/language-spec/data-types)
* * *
:::tip
A função `aws_policy_equal` é uma função de extensão personalizada do StackQL. Ela compara políticas do IAM da AWS segundo as regras de avaliação de políticas da AWS, nas quais certos elementos (como `Action`, `Resource`, `Principal` e `Tags`) são tratados como conjuntos não ordenados. Quando os dois argumentos são arrays JSON de nível superior (por exemplo, um array de tags da AWS bruto), eles também são comparados como conjuntos não ordenados. A função `aws_policy_equal` está disponível a partir do StackQL `0.8.175` e só é compatível com o backend SQL padrão do StackQL, incorporado e em memória.
:::
## Sintaxe {#syntax}
```sql
SELECT AWS_POLICY_EQUAL(policy1, policy2);
```
## Argumentos {#arguments}
__*policy1*__  
Uma string JSON que representa um documento de política do IAM da AWS, ou um array JSON de tags da AWS.  
__*policy2*__  
Outra string JSON que representa um documento de política do IAM da AWS ou um array de tags, para comparar com o primeiro.  
> Os dois argumentos devem ser JSON válido. Ao comparar documentos de política, ambos devem ser JSON válido de política do IAM da AWS.
## Valor(es) retornado(s) {#return-values}
Um valor inteiro: __1__ se os documentos forem semanticamente equivalentes, __0__ se não forem.
* * *
## Exemplos {#examples}
### Comparar políticas do IAM da AWS {#compare-aws-iam-policies}
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
### Comparar políticas com ordem de elementos diferente {#compare-policies-with-different-element-ordering}
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
### Comparar formatos de política diferentes (array vs. string) {#compare-different-policy-formats-array-vs-string}
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
### Comparar arrays de tags da AWS {#compare-aws-tags-arrays}
A AWS não garante a ordem das tags retornadas pelas suas APIs. Use `aws_policy_equal` para comparar arrays de tags sem considerar a ordem dos elementos.
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
### Comparar tags de recursos aninhadas em um objeto de recurso {#compare-resource-tags-nested-in-a-resource-object}
Quando as tags estão dentro de um objeto de recurso, como um campo `Tags`, `aws_policy_equal` continua tratando o array de tags como um conjunto não ordenado.
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
Para obter mais informações, consulte [aws_policy_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/sqlpkg/aws_policy_equal.json).
