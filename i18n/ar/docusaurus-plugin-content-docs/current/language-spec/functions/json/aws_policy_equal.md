---
title: AWS_POLICY_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - جرد الموارد السحابية
  - aws
  - سياسة IAM
description: مقارنة مستندي سياسة AWS IAM أو مصفوفتي وسوم AWS للتحقق من تكافئهما الدلالي باستخدام SQL.
image: "/img/stackql-featured-image.png"
---
تقارن بين سلسلتين نصيتين بتنسيق JSON لسياستي AWS IAM (أو مصفوفتي وسوم AWS) وتعيد `1` إذا كانتا متكافئتين دلاليًا وفق قواعد تقييم سياسات AWS، و`0` بخلاف ذلك.  

راجع أيضًا:  
[__[` SELECT `]__](/language-spec/select) [__[` DESCRIBE `]__](/language-spec/describe) [__[` JSON_EQUAL `]__](/language-spec/functions/json/json_equal) [__[` JSON_EXTRACT `]__](/language-spec/functions/json/json_extract) [[ أنواع البيانات ]](/language-spec/data-types)
* * *
:::tip
الدالة `aws_policy_equal` هي دالة امتداد مخصصة في StackQL. وهي تتعامل مع مقارنة سياسات AWS IAM وفق قواعد تقييم سياسات AWS، حيث تُعامل عناصر معينة (مثل `Action` و`Resource` و`Principal` و`Tags`) كمجموعات غير مرتبة. وعندما تكون الوسيطتان كلتاهما مصفوفتي JSON في المستوى الأعلى (مثل مصفوفة وسوم AWS خام)، تجري مقارنتهما أيضًا كمجموعتين غير مرتبتين. الدالة `aws_policy_equal` متاحة في StackQL من الإصدار `0.8.175` فصاعدًا، وهي مدعومة فقط مع واجهة SQL الخلفية الافتراضية المضمنة التي تعمل في الذاكرة لـ StackQL.
:::
## بناء الجملة {#syntax}
```sql
SELECT AWS_POLICY_EQUAL(policy1, policy2);
```
## الوسيطات {#arguments}
__*policy1*__  
سلسلة نصية بتنسيق JSON تمثل مستند سياسة AWS IAM، أو مصفوفة JSON من وسوم AWS.  
__*policy2*__  
سلسلة نصية أخرى بتنسيق JSON تمثل مستند سياسة AWS IAM أو مصفوفة وسوم لمقارنتها بالأولى.  
> يجب أن تكون الوسيطتان كلتاهما بتنسيق JSON صالح. وعند مقارنة مستندات السياسات، يجب أن تكون كلتاهما بتنسيق JSON صالح لسياسات AWS IAM.
## القيمة المرجعة {#return-values}
قيمة من نوع عدد صحيح: __1__ إذا كان المستندان متكافئين دلاليًا، __0__ إذا لم يكونا كذلك.
* * *
## أمثلة {#examples}
### مقارنة سياسات AWS IAM {#compare-aws-iam-policies}
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
### مقارنة سياسات بترتيب مختلف للعناصر {#compare-policies-with-different-element-ordering}
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
### مقارنة تنسيقات مختلفة للسياسات (مصفوفة مقابل سلسلة نصية) {#compare-different-policy-formats-array-vs-string}
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
### مقارنة مصفوفات وسوم AWS {#compare-aws-tags-arrays}
لا تضمن AWS ترتيب الوسوم التي تعيدها واجهات API الخاصة بها. استخدم الدالة `aws_policy_equal` لمقارنة مصفوفات الوسوم دون مراعاة ترتيب العناصر.
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
### مقارنة وسوم الموارد المتداخلة في كائن مورد {#compare-resource-tags-nested-in-a-resource-object}
عندما تكون الوسوم مضمنة داخل كائن مورد كحقل `Tags`، تظل الدالة `aws_policy_equal` تعامل مصفوفة الوسوم كمجموعة غير مرتبة.
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
لمزيد من المعلومات، راجع [aws_policy_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/sqlpkg/aws_policy_equal.json).
