---
title: JSON_TYPE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm JSON_TYPE trong StackQL: kiểu (object, array, text, integer, real, true, false hoặc null) của phần tử ngoài cùng trong một giá trị JSON, hoặc của một đường dẫn bên trong."
image: "/img/stackql-featured-image.png"
---
Trả về kiểu dữ liệu của phần tử ngoài cùng trong một giá trị JSON, hoặc của phần tử tại đường dẫn tùy chọn.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Kiểu dữ liệu ]](/language-spec/data-types)

* * * 

:::tip 

Sử dụng câu lệnh [**DESCRIBE**](/language-spec/describe) để tìm các kiểu dữ liệu `array` hoặc `object` có thể dùng với các hàm JSON của StackQL.

:::

:::note 

Nếu bạn sử dụng StackQL với backend SQL `postgres`, hãy dùng hàm `json_typeof` thay thế.

:::

## Cú pháp {#syntax}

```sql
SELECT JSON_TYPE(json_array_or_object [, json_path]) 
FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*json_array_or_object*__  
Một mảng hoặc đối tượng JSON.

__*json_path*__  
Không bắt buộc. Đường dẫn JSON đến một mảng hoặc đối tượng nằm trong *json_array_or_object*.  Xem [jsonpath.com](https://jsonpath.com/).

> Nếu *json_path* không tồn tại trong *json_array_or_object*, hàm trả về `NULL`.

> Hàm `json_type()` báo lỗi nếu bất kỳ đối số nào của hàm không đúng định dạng.

## Giá trị trả về {#return-values}
Một chuỗi biểu diễn kiểu dữ liệu đã xác định.

> Các kiểu dữ liệu gồm `null`, `true`, `false`, `integer`, `real`, `text`, `array` hoặc `object`.

* * *

## Ví dụ {#examples}

### Trả về kiểu dữ liệu của một trường lồng trong đối tượng hoặc mảng JSON {#return-the-datatype-of-a-field-nested-within-a-json-object-or-array}

```sql
SELECT json_type(disks, '$[0].licenses[0]') as datatype
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns text */
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/json1.html#jtype](https://www.sqlite.org/json1.html#jtype)
