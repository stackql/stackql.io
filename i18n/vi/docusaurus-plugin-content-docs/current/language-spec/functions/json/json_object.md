---
title: JSON_OBJECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm JSON_OBJECT trong StackQL: tạo một đối tượng JSON từ các đối số khóa và giá trị, ví dụ để dựng phần thân yêu cầu cho INSERT và UPDATE."
image: "/img/stackql-featured-image.png"
---
Trả về một đối tượng được tạo từ các đối số đã cung cấp.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Kiểu dữ liệu ]](/language-spec/data-types)

* * * 

:::tip 

Sử dụng câu lệnh [**DESCRIBE**](/language-spec/describe) để tìm các kiểu dữ liệu `array` hoặc `object` có thể dùng với các hàm JSON của StackQL.

:::

:::note 

Nếu bạn sử dụng stackql với backend SQL `postgres`, hãy dùng hàm `jsonb_build_object` thay cho `json_object`: StackQL chuyển nguyên `json_object` đến PostgreSQL, và hàm cùng tên của PostgreSQL nhận các khóa và giá trị dưới dạng mảng văn bản.

:::

## Cú pháp {#syntax}

```sql
SELECT JSON_OBJECT(key1, value1, key2, value2, ...) 
FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*key1, key2, ...*__  
Các nhãn của đối tượng JSON.

__*value1, value2, ...*__  
Các giá trị tương ứng với các nhãn trong đối tượng JSON.

## Giá trị trả về {#return-values}
Một chuỗi biểu diễn đối tượng JSON đúng định dạng.

* * *

## Ví dụ {#examples}

### Tạo một mảng các đối tượng JSON từ một cột {#build-an-array-of-json-objects-from-a-column}

```sql
SELECT JSON_GROUP_ARRAY(json_object('instance', selfLink)) as instance_links
FROM google.compute.instances 
WHERE project = 'stackql-k8s-the-hard-way-demo'
AND zone = 'australia-southeast1-a'
AND name like '%-dev-controller-%';
/* returns a data structure required for a targetPool in GCP */
```

Để biết thêm thông tin, hãy xem [https://sqlite.org/json1.html#jobj](https://sqlite.org/json1.html#jobj)
