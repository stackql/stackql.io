---
title: JSON_ARRAY_LENGTH
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm JSON_ARRAY_LENGTH trong StackQL: số phần tử trong một mảng JSON, ở cấp cao nhất hoặc tại một đường dẫn trong một giá trị JSON."
image: "/img/stackql-featured-image.png"
---
Trả về số phần tử trong một mảng JSON.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_EXTRACT `]](/language-spec/functions/json/json_extract) [[ Kiểu dữ liệu ]](/language-spec/data-types)

* * * 

:::tip 

Sử dụng câu lệnh [**DESCRIBE**](/language-spec/describe) để tìm các kiểu dữ liệu `array` hoặc `object` có thể dùng với các hàm JSON của StackQL. Sử dụng hàm [**JSON_TYPE**](/language-spec/functions/json/json_type) để xác định kiểu dữ liệu của các đối tượng lồng nhau hoặc phần tử mảng.

:::

## Cú pháp {#syntax}

```sql
SELECT JSON_ARRAY_LENGTH(json_array_or_object [, json_path]) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*json_array_or_object*__  
Một mảng hoặc đối tượng JSON.

> Nếu *json_array_or_object* là một đối tượng JSON (không phải mảng) và *json_path* không được chỉ định, hàm trả về `0`.

__*json_path*__  
Đường dẫn JSON không bắt buộc tới một mảng nằm trong một đối tượng.  Xem [jsonpath.com](https://jsonpath.com/).

> Nếu *json_path* được cung cấp, hàm `json_array_length` tìm đường dẫn đó trong *json_array_or_object* và trả về độ dài của mảng, hoặc `0` nếu *json_path* là một đối tượng (không phải mảng).  Nếu không tìm thấy *json_path* trong *json_array_or_object*, hàm trả về `NULL`.

> Nếu *json_array_or_object* không phải là JSON đúng định dạng, hoặc *json_path* không phải là đường dẫn JSON đúng định dạng, hàm báo lỗi.

## Giá trị trả về {#return-values}
Trả về một số nguyên.

* * *

## Ví dụ {#examples}

### Trả về số đĩa được gắn vào các instance Google Compute Engine {#return-the-number-of-disks-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(disks) as num_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Trả về số giao diện mạng được gắn vào các instance Google Compute Engine {#return-the-number-of-network-interfaces-attached-to-google-compute-engine-instances}

```sql
SELECT name, json_array_length(networkInterfaces) as num_nics
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Trả về số ACL gắn với các bucket Google Storage {#return-the-number-of-acls-associated-with-google-storage-buckets}

```sql
SELECT name, json_array_length(acl) as num_acls
FROM google.storage.buckets 
WHERE project = 'stackql';
```

### Trả về số phạm vi gắn với một tài khoản dịch vụ {#return-the-number-of-scopes-associated-with-a-service-account}

```sql
SELECT name, json_array_length(serviceAccounts, '$[0].scopes') as num_scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/json1.html#jarraylen](https://www.sqlite.org/json1.html#jarraylen)
