---
title: AWS_POLICY_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
  - aws
  - chính sách IAM
description: So sánh hai tài liệu chính sách AWS IAM hoặc hai mảng tag AWS về tính tương đương ngữ nghĩa bằng SQL.
image: "/img/stackql-featured-image.png"
---
So sánh hai chuỗi JSON chính sách AWS IAM (hoặc mảng tag AWS) và trả về `1` nếu chúng tương đương về ngữ nghĩa theo quy tắc đánh giá chính sách của AWS, ngược lại trả về `0`.  

Xem thêm:  
[__[` SELECT `]__](/language-spec/select) [__[` DESCRIBE `]__](/language-spec/describe) [__[` JSON_EQUAL `]__](/language-spec/functions/json/json_equal) [__[` JSON_EXTRACT `]__](/language-spec/functions/json/json_extract) [[ Kiểu dữ liệu ]](/language-spec/data-types)
* * *
:::tip
Hàm `aws_policy_equal` là một hàm mở rộng tùy chỉnh của StackQL. Hàm này so sánh chính sách AWS IAM theo quy tắc đánh giá chính sách của AWS, trong đó một số phần tử (như `Action`, `Resource`, `Principal` và `Tags`) được xem là tập hợp không có thứ tự. Khi cả hai đối số đều là mảng JSON cấp cao nhất (ví dụ một mảng tag AWS thô), chúng cũng được so sánh như các tập hợp không có thứ tự. Hàm `aws_policy_equal` có từ StackQL phiên bản `0.8.175` trở đi và chỉ được hỗ trợ với backend SQL nhúng trong bộ nhớ mặc định của StackQL.
:::
## Cú pháp {#syntax}
```sql
SELECT AWS_POLICY_EQUAL(policy1, policy2);
```
## Đối số {#arguments}
__*policy1*__  
Một chuỗi JSON biểu diễn một tài liệu chính sách AWS IAM, hoặc một mảng JSON gồm các tag AWS.  
__*policy2*__  
Một chuỗi JSON khác biểu diễn tài liệu chính sách AWS IAM hoặc mảng tag để so sánh với đối số thứ nhất.  
> Cả hai đối số phải là JSON hợp lệ. Khi so sánh tài liệu chính sách, cả hai phải là JSON chính sách AWS IAM hợp lệ.
## Giá trị trả về {#return-values}
Một giá trị số nguyên: __1__ nếu các tài liệu tương đương về ngữ nghĩa, __0__ nếu không tương đương.
* * *
## Ví dụ {#examples}
### So sánh chính sách AWS IAM {#compare-aws-iam-policies}
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
### So sánh chính sách có thứ tự phần tử khác nhau {#compare-policies-with-different-element-ordering}
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
### So sánh các định dạng chính sách khác nhau (mảng và chuỗi) {#compare-different-policy-formats-array-vs-string}
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
### So sánh mảng tag AWS {#compare-aws-tags-arrays}
AWS không đảm bảo thứ tự của các tag do API của mình trả về. Sử dụng `aws_policy_equal` để so sánh mảng tag mà không xét đến thứ tự phần tử.
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
### So sánh tag tài nguyên lồng trong một đối tượng tài nguyên {#compare-resource-tags-nested-in-a-resource-object}
Khi tag được nhúng bên trong một đối tượng tài nguyên dưới dạng trường `Tags`, `aws_policy_equal` vẫn xem mảng tag là một tập hợp không có thứ tự.
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
Để biết thêm thông tin, hãy xem [aws_policy_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/sqlpkg/aws_policy_equal.json).
