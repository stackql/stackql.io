---
title: JSON_EACH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm có giá trị bảng JSON_EACH trong StackQL: mở rộng mảng hoặc đối tượng JSON thành một hàng cho mỗi phần tử hoặc thành viên, để nối và lọc phản hồi API lồng nhau."
image: "/img/stackql-featured-image.png"
---
Hàm có giá trị bảng, trả về một bảng gồm một hàng cho mỗi phần tử mảng hoặc thành viên đối tượng.

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Kiểu dữ liệu ]](/language-spec/data-types)

* * * 

:::tip 

Sử dụng câu lệnh [**DESCRIBE**](/language-spec/describe) để tìm các kiểu dữ liệu `array` có thể dùng với hàm `json_each`.

:::

:::note 

Hàm `json_each` được hiện thực bằng hàm `json_array_elements_text` nếu bạn sử dụng stackql với backend SQL `postgres`

:::

## Cú pháp {#syntax}

```sql
SELECT <alias>.<projectedField>, ... , json_each.value  
FROM <multipartIdentifier> <alias>, JSON_EACH(json_array_or_object, json_path);
```
## Đối số {#arguments}

__*json_array_or_object*__  
Một mảng hoặc đối tượng JSON.

__*json_path*__  
Đường dẫn JSON tới một mảng hoặc đối tượng nằm trong *json_array_or_object*.  Xem [jsonpath.com](https://jsonpath.com/).

> Nếu *json_path* không tồn tại trong *json_array_or_object*, hàm không trả về hàng nào.

> Hàm `json_each()` báo lỗi nếu *json_array_or_object* không phải là JSON đúng định dạng.

## Giá trị trả về {#return-values}
Một bảng gồm một hàng cho mỗi phần tử mảng hoặc thành viên đối tượng.

* * *

## Ví dụ {#examples}

### Trải phẳng một mảng chuỗi thành bảng được nối với các trường liên quan {#unnest-an-array-of-strings-into-a-table-joined-with-associated-fields}

để nắm ngữ cảnh:  

```sql
describe google.cloudresourcemanager.projects_iam_policies;
```

trả về...

```
|-----------|--------|                                                                                                                                                                                                     
|   name    |  type  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| condition | object |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| members   | array  |                                                                                                                                                                                                     
|-----------|--------|                                                                                                                                                                                                     
| role      | string |                                                                                                                                                                                                     
|-----------|--------|  
```

Truy vấn `stackql` sau sử dụng hàm `json_each` để trải phẳng mảng `members` thành một bảng được nối với trường `role` và tùy chọn với trường `condition`.  

```sql
select 
iam.role, 
SPLIT_PART(json_each.value, ':', 2) as member 
from google.cloudresourcemanager.projects_iam_policies iam, json_each(members) 
where projectsId = 'stackql';
```

sẽ trả về...

```
|--------------------------------------|-------------------------------------------------------|
|                 role                 |                        member                         |
|--------------------------------------|-------------------------------------------------------|
| roles/cloudbuild.builds.builder      | 1234567890@cloudbuild.gserviceaccount.com             |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890-compute@developer.gserviceaccount.com      |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | 1234567890@cloudservices.gserviceaccount.com          |
|--------------------------------------|-------------------------------------------------------|
| roles/editor                         | stackql@appspot.gserviceaccount.com                   |
|--------------------------------------|-------------------------------------------------------|
| ...                                  | ...                                                   |
|--------------------------------------|-------------------------------------------------------|
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/json1.html#jeach](https://www.sqlite.org/json1.html#jeach)
