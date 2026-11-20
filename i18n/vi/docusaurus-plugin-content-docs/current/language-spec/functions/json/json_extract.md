---
title: JSON_EXTRACT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm JSON_EXTRACT trong StackQL: lấy một hoặc nhiều giá trị từ đối tượng hoặc mảng JSON theo đường dẫn, công cụ chính để làm phẳng các trường lồng nhau trong phản hồi API."
image: "/img/stackql-featured-image.png"
---
Trả về một hoặc nhiều giá trị từ một đối tượng hoặc mảng JSON đúng định dạng.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[` DESCRIBE `]](/language-spec/describe) [[` JSON_ARRAY_LENGTH `]](/language-spec/functions/json/json_array_length) [[ Kiểu dữ liệu ]](/language-spec/data-types)

* * * 

:::tip 

Sử dụng câu lệnh [**DESCRIBE**](/language-spec/describe) để tìm các kiểu dữ liệu `array` hoặc `object` có thể dùng với các hàm JSON của StackQL. Sử dụng hàm [**JSON_TYPE**](/language-spec/functions/json/json_type) để xác định kiểu dữ liệu của các đối tượng lồng nhau hoặc các phần tử mảng.

:::

:::note 

Hàm `json_extract` được hiện thực bằng hàm `json_extract_path_text` nếu bạn sử dụng stackql với backend SQL `postgres`

:::


## Cú pháp {#syntax}

```sql
SELECT JSON_EXTRACT(json_array_or_object, json_path[, json_path1, .. json_pathN]) 
FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*json_array_or_object*__  
Một mảng hoặc đối tượng JSON.

__*json_path*__  
Đường dẫn JSON đến một mảng hoặc đối tượng nằm trong *json_array_or_object*.  Xem [jsonpath.com](https://jsonpath.com/).

> Nếu chỉ cung cấp một *json_path*, hàm trả về một kết quả vô hướng có kiểu dữ liệu tương ứng với kiểu dữ liệu của đường dẫn được phân giải.

> Nếu cung cấp nhiều đối số *json_path*, kết quả sẽ là một mảng gồm các đối tượng được phân giải theo từng đường dẫn tương ứng.

> Chỉ định `'$'` cho đối số *json_path* sẽ trả về toàn bộ đối tượng JSON tương ứng với cột mà đối số *json_array_or_object* đại diện.

> GHI CHÚ: Bạn có thể dùng các công cụ như [`json-viewer`](https://aarrange.com/json-viewer) để phát triển và kiểm thử các biểu thức đường dẫn JSON

## Giá trị trả về {#return-values}
Một chuỗi, số nguyên, đối tượng, mảng, boolean hoặc null, tùy thuộc vào *json_array_or_object* và *json_path* được cung cấp làm đầu vào.

* * *

## Ví dụ {#examples}

### Trả về các đối tượng ổ đĩa liên kết với từng instance Google Compute Engine {#return-the-disk-objects-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$') as disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Trả về tên thiết bị và kích thước của ổ đĩa đầu tiên được gắn vào từng instance Google Compute Engine {#return-the-device-name-and-size-of-the-first-disk-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(disks, '$[0].deviceName') as device_name,
json_extract(disks, '$[0].diskSizeGb') as disk_size
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Trả về cấp mạng của NIC đầu tiên được gắn vào từng instance Google Compute Engine {#return-the-network-tier-of-the-first-nic-attached-to-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(networkInterfaces, '$[0].accessConfigs[0].networkTier') as network_tier
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Trả về danh sách phạm vi gắn với tài khoản dịch vụ đầu tiên liên kết với từng instance Google Compute Engine {#return-a-list-of-scopes-associated-with-the-first-service-account-associated-with-each-google-compute-engine-instance}

```sql
SELECT name, json_extract(serviceAccounts, '$[0].scopes') as scopes 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
/* returns a list for each service account */
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/json1.html#jex](https://www.sqlite.org/json1.html#jex)
