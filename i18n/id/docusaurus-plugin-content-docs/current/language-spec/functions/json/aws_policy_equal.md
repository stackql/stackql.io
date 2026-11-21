---
title: AWS_POLICY_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - inventaris cloud
  - aws
  - kebijakan IAM
description: Membandingkan dua dokumen kebijakan IAM AWS atau array tag AWS untuk kesetaraan semantik menggunakan SQL.
image: "/img/stackql-featured-image.png"
---
Membandingkan dua string JSON kebijakan IAM AWS (atau array tag AWS) dan mengembalikan `1` jika keduanya setara secara semantik menurut aturan evaluasi kebijakan AWS, atau `0` jika tidak.  

Lihat juga:  
[__[` SELECT `]__](/language-spec/select) [__[` DESCRIBE `]__](/language-spec/describe) [__[` JSON_EQUAL `]__](/language-spec/functions/json/json_equal) [__[` JSON_EXTRACT `]__](/language-spec/functions/json/json_extract) [[ Tipe data ]](/language-spec/data-types)
* * *
:::tip
Fungsi `aws_policy_equal` merupakan fungsi ekstensi kustom StackQL. Fungsi ini menangani perbandingan kebijakan IAM AWS menurut aturan evaluasi kebijakan AWS, yang memperlakukan elemen tertentu (seperti `Action`, `Resource`, `Principal`, dan `Tags`) sebagai himpunan tak berurutan. Jika kedua argumen merupakan array JSON tingkat atas (misalnya array tag AWS mentah), keduanya juga dibandingkan sebagai himpunan tak berurutan. Fungsi `aws_policy_equal` tersedia mulai StackQL versi `0.8.175` dan hanya didukung dengan backend SQL default StackQL yang tersemat dan berjalan di memori.
:::
## Sintaksis {#syntax}
```sql
SELECT AWS_POLICY_EQUAL(policy1, policy2);
```
## Argumen {#arguments}
__*policy1*__  
String JSON yang merepresentasikan dokumen kebijakan IAM AWS, atau array JSON berisi tag AWS.  
__*policy2*__  
String JSON lain yang merepresentasikan dokumen kebijakan IAM AWS atau array tag untuk dibandingkan dengan argumen pertama.  
> Kedua argumen harus berupa JSON yang valid. Saat membandingkan dokumen kebijakan, keduanya harus berupa JSON kebijakan IAM AWS yang valid.
## Nilai yang dikembalikan {#return-values}
Nilai bilangan bulat: __1__ jika dokumen setara secara semantik, __0__ jika tidak.
* * *
## Contoh {#examples}
### Membandingkan kebijakan IAM AWS {#compare-aws-iam-policies}
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
### Membandingkan kebijakan dengan urutan elemen yang berbeda {#compare-policies-with-different-element-ordering}
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
### Membandingkan format kebijakan yang berbeda (array vs string) {#compare-different-policy-formats-array-vs-string}
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
### Membandingkan array tag AWS {#compare-aws-tags-arrays}
API AWS tidak menjamin urutan tag yang dikembalikan. Gunakan `aws_policy_equal` untuk membandingkan array tag tanpa memperhatikan urutan elemen.
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
### Membandingkan tag sumber daya yang bersarang di dalam objek sumber daya {#compare-resource-tags-nested-in-a-resource-object}
Jika tag disematkan di dalam objek sumber daya sebagai field `Tags`, fungsi `aws_policy_equal` tetap memperlakukan array tag sebagai himpunan tak berurutan.
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
Untuk informasi selengkapnya, lihat [aws_policy_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/sqlpkg/aws_policy_equal.json).
