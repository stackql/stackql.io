---
title: AWS_POLICY_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventario de nube
  - aws
  - política de IAM
description: Comparar dos documentos de política de IAM de AWS o arrays de tags de AWS para comprobar su equivalencia semántica con SQL.
image: "/img/stackql-featured-image.png"
---
Compara dos cadenas JSON de política de IAM de AWS (o arrays de tags de AWS) y devuelve `1` si son semánticamente equivalentes según las reglas de evaluación de políticas de AWS, o `0` en caso contrario.  

Consulte también:  
[__[` SELECT `]__](/language-spec/select) [__[` DESCRIBE `]__](/language-spec/describe) [__[` JSON_EQUAL `]__](/language-spec/functions/json/json_equal) [__[` JSON_EXTRACT `]__](/language-spec/functions/json/json_extract) [[ Tipos de datos ]](/language-spec/data-types)
* * *
:::tip
La función `aws_policy_equal` es una función de extensión personalizada de StackQL. Compara políticas de IAM de AWS según las reglas de evaluación de políticas de AWS, en las que ciertos elementos (como `Action`, `Resource`, `Principal` y `Tags`) se tratan como conjuntos no ordenados. Cuando ambos argumentos son arrays JSON de nivel superior (por ejemplo, un array de tags de AWS sin procesar), también se comparan como conjuntos no ordenados. La función `aws_policy_equal` está disponible a partir de StackQL `0.8.175` y solo se admite con el backend SQL integrado en memoria predeterminado de StackQL.
:::
## Sintaxis {#syntax}
```sql
SELECT AWS_POLICY_EQUAL(policy1, policy2);
```
## Argumentos {#arguments}
__*policy1*__  
Una cadena JSON que representa un documento de política de IAM de AWS, o un array JSON de tags de AWS.  
__*policy2*__  
Otra cadena JSON que representa un documento de política de IAM de AWS o un array de tags para comparar con la primera.  
> Ambos argumentos deben ser JSON válido. Al comparar documentos de política, ambos deben ser JSON válido de política de IAM de AWS.
## Valor(es) devuelto(s) {#return-values}
Un valor entero: __1__ si los documentos son semánticamente equivalentes, __0__ si no lo son.
* * *
## Ejemplos {#examples}
### Comparar políticas de IAM de AWS {#compare-aws-iam-policies}
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
### Comparar políticas con distinto orden de elementos {#compare-policies-with-different-element-ordering}
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
### Comparar distintos formatos de política (array frente a cadena) {#compare-different-policy-formats-array-vs-string}
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
### Comparar arrays de tags de AWS {#compare-aws-tags-arrays}
AWS no garantiza el orden de los tags que devuelven sus API. Use `aws_policy_equal` para comparar arrays de tags sin tener en cuenta el orden de los elementos.
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
### Comparar tags de recursos anidados en un objeto de recurso {#compare-resource-tags-nested-in-a-resource-object}
Cuando los tags están integrados en un objeto de recurso como un campo `Tags`, `aws_policy_equal` sigue tratando el array de tags como un conjunto no ordenado.
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
Para obtener más información, consulte [aws_policy_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/sqlpkg/aws_policy_equal.json).
