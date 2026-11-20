---
title: JSON_EQUAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: So sánh hai chuỗi JSON để xác định tính tương đương bằng SQL.
image: "/img/stackql-featured-image.png"
---
So sánh hai chuỗi JSON và trả về 1 nếu chúng tương đương, ngược lại trả về 0.

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Kiểu dữ liệu ]](/language-spec/data-types)

* * * 

:::tip 

Hàm `json_equal` là một hàm mở rộng tùy chỉnh của StackQL. Hàm này hỗ trợ so sánh đối tượng và mảng JSON, xem các đối tượng có khóa theo thứ tự khác nhau là tương đương theo đặc tả JSON. Hàm `json_equal` có từ StackQL phiên bản `0.5.707` trở đi và chỉ được hỗ trợ với backend SQL nhúng trong bộ nhớ mặc định của StackQL.

:::

## Cú pháp {#syntax}

```sql
SELECT JSON_EQUAL(json1, json2);
```

## Đối số {#arguments}

__*json1*__  
Một chuỗi JSON biểu diễn một đối tượng hoặc mảng.

__*json2*__  
Một chuỗi JSON khác để so sánh với chuỗi thứ nhất.

> Cả hai đối số phải là chuỗi JSON hợp lệ.

## Giá trị trả về {#return-values}
Một giá trị số nguyên: 1 nếu các chuỗi JSON tương đương, 0 nếu không tương đương.

* * *

## Ví dụ {#examples}

### So sánh đối tượng JSON {#compare-json-objects}

```sql
SELECT 
public_access_block_configuration as current_state,
'{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}' as desired_state,
JSON_EQUAL(public_access_block_configuration, '{"BlockPublicAcls": true, "BlockPublicPolicy": true, "IgnorePublicAcls": true, "RestrictPublicBuckets": true}') as comp
FROM awscc.s3.buckets 
WHERE region = 'ap-southeast-2' 
AND Identifier = 'my-bucket';
/* returns...
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
|                                             current_state                                              |         desired_state          | comp |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
| {"RestrictPublicBuckets":true,"BlockPublicPolicy":true,"BlockPublicAcls":true,"IgnorePublicAcls":true} | {"BlockPublicAcls": true,      |    1 |
|                                                                                                        | "BlockPublicPolicy": true,     |      |
|                                                                                                        | "IgnorePublicAcls": true,      |      |
|                                                                                                        | "RestrictPublicBuckets": true} |      |
|--------------------------------------------------------------------------------------------------------|--------------------------------|------|
*/
```

### So sánh mảng JSON {#compare-json-arrays}

```sql
SELECT 
  json_equal('[1, 2, 3]', '[1, 2, 3]') as array_comp_1,
  json_equal('[1, 2, 3]', '[3, 2, 1]') as array_comp_2
; 
/* returns...
|--------------|--------------|
| array_comp_1 | array_comp_2 |
|--------------|--------------|
|            1 |            0 |
|--------------|--------------|
*/
```

Để biết thêm thông tin, hãy xem [json_equal](https://github.com/stackql/sqlite-ext-functions/blob/main/docs/json_equal.md).
