---
title: SHOW
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "SHOW trong StackQL: liệt kê provider, dịch vụ, tài nguyên và phương thức; tạo template INSERT; xem phiên bản, người đóng góp và phần phụ thuộc của tệp nhị phân đang chạy."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Dùng để liệt kê các mục trong một tập hợp, ví dụ hiển thị tất cả dịch vụ có trong một provider, hoặc hiển thị tất cả tài nguyên có trong một dịch vụ nhất định. Câu lệnh này cũng cung cấp phiên bản, danh sách người đóng góp và các phần phụ thuộc phần mềm của tệp nhị phân StackQL đang chạy.

Xem thêm:  
[[ Phân cấp tài nguyên StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Cú pháp {#syntax}

*showStatement::=*

<RailroadDiagram 
type="show"
/>

&nbsp;  
&nbsp;  

```sql
SHOW [ EXTENDED ]
{ PROVIDERS | SERVICES | RESOURCES | METHODS }
[ { IN | FROM } { <provider> | <service> | <resource> } ]
[ { LIKE <pattern> | WHERE <expression> } ];

SHOW [ EXTENDED ] INSERT INTO <resource>;
SHOW [ EXTENDED ] VERSION;
SHOW [ EXTENDED ] CONTRIBUTORS;
SHOW [ EXTENDED ] DEPENDENCIES [ LIKE '<pattern>' ];
```

`VERSION`, `CONTRIBUTORS` và `DEPENDENCIES` không yêu cầu cấu hình provider hay cấu hình xác thực. `DEPENDENCIES` chỉ hỗ trợ một mệnh đề `LIKE` không bắt buộc; `IN`, `FROM`, `WHERE` và `LIMIT` không được hỗ trợ.

* * *

## Ví dụ {#examples}

### Liệt kê tất cả provider {#list-all-providers}
Liệt kê các provider có trong môi trường StackQL cục bộ: các provider đã tải xuống từ registry provider, cùng với các provider tích hợp sẵn trong StackQL.
```sql
-- Returns the providers available locally
SHOW PROVIDERS;
```
<details>
<summary><em><strong>Trả về:</strong></em></summary>

```
|----------------------|--------------|
|         name         |   version    |
|----------------------|--------------|
| aws                  | v26.03.00379 |
|----------------------|--------------|
| databricks_account   | v26.03.00381 |
|----------------------|--------------|
| databricks_workspace | v26.03.00381 |
|----------------------|--------------|
| google               | v25.12.00357 |
|----------------------|--------------|
| k8s                  | v24.12.00276 |
|----------------------|--------------|
```

</details>

### Liệt kê tất cả dịch vụ trong một provider {#list-all-services-in-a-cloud-provider}
Liệt kê tất cả dịch vụ có trong một provider nhất định; dùng từ khóa EXTENDED không bắt buộc để trả về thêm thông tin.
```sql
-- Returns all services available in a provider
SHOW EXTENDED SERVICES IN google;
```
<details>
<summary><em><strong>Trả về:</strong></em></summary>

```
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
|               id               |       name        |          title          |                          description                          |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| compute:v25.12.00357           | compute           | Compute Engine API      | Creates and runs virtual machines on Google Cloud Platform.   |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| storage:v25.12.00357           | storage           | Cloud Storage API       | Stores and retrieves unstructured object data at global sc... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| bigquery:v25.12.00357          | bigquery          | BigQuery API            | A data platform for customers to create, manage, share and... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| aiplatform:v25.12.00357        | aiplatform        | Vertex AI API           | Train high-quality custom machine learning models with min... |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| geminicloudassist:v25.12.00357 | geminicloudassist | Gemini Cloud Assist API | The AI-powered assistant for Google Cloud.                    |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
| ...                            | ...               | ...                     | ...                                                           |
|--------------------------------|-------------------|-------------------------|---------------------------------------------------------------|
```

</details>


### Liệt kê tất cả tài nguyên trong một dịch vụ của provider {#list-all-resources-within-a-cloud-provider-service}
Liệt kê tất cả tài nguyên có trong một dịch vụ nhất định của provider.  Từ khóa `EXTENDED` không bắt buộc trả về mô tả của tài nguyên ngoài tên tài nguyên.
```sql
-- Returns all resources available in a provider service
SHOW EXTENDED RESOURCES IN google.compute;
```
<details>
<summary><em><strong>Trả về:</strong></em></summary>

```
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
|                       name                        |                        id                        |                         description                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| instances                                         | google.compute.instances                         | Virtual machine instances running on Google Cloud Platform...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| disks                                             | google.compute.disks                             | Persistent block storage volumes attached to compute inst... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| networks                                          | google.compute.networks                          | Virtual private cloud (VPC) networking resources and conf... |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| backend_services                                  | google.compute.backend_services                  | Backend services used for load balancing and traffic distr...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| firewalls                                         | google.compute.firewalls                         | Firewall rules controlling ingress and egress network traf...|
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
| ...                                               | ...                                              | ...                                                          |
|---------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------|
```

</details>

### Hiển thị các phương thức hiện có của một tài nguyên trong dịch vụ của provider {#show-available-methods-for-a-resource-within-a-cloud-provider-service}
Liệt kê tất cả phương thức hiện có của một tài nguyên nhất định.
```sql
-- Returns all methods available for a provider resource
SHOW EXTENDED METHODS IN google.compute.instances;
```
<details>
<summary><em><strong>Trả về:</strong></em></summary>

```
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
|               MethodName               |         RequiredParams         | SQLVerb |                 description                 |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| list                                   | project, zone                  | SELECT  | Retrieves the list of instances contained...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| insert                                 | project, zone                  | INSERT  | Creates an instance resource in the speci...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| delete                                 | instance, project, zone        | DELETE  | Deletes the specified Instance resource...  |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| start                                  | instance, project, zone        | EXEC    | Starts an instance that was stopped using...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| stop                                   | instance, project, zone        | EXEC    | Stops a running instance, shutting it dow...|
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
| ...                                    | ...                            | ...     | ...                                         |
|----------------------------------------|--------------------------------|---------|---------------------------------------------|
```

</details>

### Tạo template `INSERT` bằng lệnh `SHOW INSERT` {#generating-an-insert-template-using-the-show-insert-command}

Có thể dùng lệnh `SHOW` để tạo template cho câu lệnh `INSERT` như sau:

```sql
SHOW INSERT INTO google.compute.addresses;
```
<details>
<summary><em><strong>Trả về:</strong></em></summary>

```sql
INSERT INTO google.compute.addresses(
  project,
  region,
  data__address,
  data__addressType,
  data__description,
  data__ipVersion,
  data__name,
  data__network,
  data__networkTier,
  data__prefixLength,
  data__purpose,
  data__subnetwork
)
SELECT
  '{{ .values.project }}',
  '{{ .values.region }}',
  '{{ .values.data__address }}',
  '{{ .values.data__addressType }}',
  '{{ .values.data__description }}',
  '{{ .values.data__ipVersion }}',
  '{{ .values.data__name }}',
  '{{ .values.data__network }}',
  '{{ .values.data__networkTier }}',
   {{ .values.data__prefixLength }},
  '{{ .values.data__purpose }}',
  '{{ .values.data__subnetwork }}'
```

</details>

> Có thể giới hạn các trường được trả về chỉ ở những trường mà provider yêu cầu; xem [__Tạo template hạ tầng__](/getting-started/templating) để biết ví dụ.

> Sau đó, có thể dùng template này cùng với một tệp dữ liệu `json` hoặc `jsonnet` để cung cấp giá trị khi chạy; để biết thêm thông tin, hãy xem [__Sử dụng biến__](/getting-started/variables).

### Hiển thị phiên bản StackQL {#show-the-stackql-version}

Trả về phiên bản của tiến trình StackQL đang chạy. Hữu ích trong các chế độ shell, exec và máy chủ giao thức wire Postgres khi bạn cần xác nhận bản dựng nào đang trả lời truy vấn. Thông tin tương tự cũng được cung cấp qua công cụ MCP `server_info`.

```sql
-- Returns the version of the running StackQL process
SHOW VERSION;
```
<details>
<summary><em><strong>Trả về:</strong></em></summary>

```
+---------+
| version |
+---------+
| 1.2.3   |
+---------+
```

</details>

Dùng từ khóa `EXTENDED` không bắt buộc để trả về thêm commit git, dấu thời gian bản dựng và nền tảng bản dựng:

```sql
-- Returns version plus build provenance
SHOW EXTENDED VERSION;
```
<details>
<summary><em><strong>Trả về:</strong></em></summary>

```
+---------+---------+----------------------+-----------------+
| version | commit  |     build_date       |    platform     |
+---------+---------+----------------------+-----------------+
| 1.2.3   | a1b2c3d | 2026-05-15T04:12:00Z | linux/amd64     |
+---------+---------+----------------------+-----------------+
```

</details>

> `SHOW VERSION` không yêu cầu cấu hình provider, registry hay xác thực, và có thể chạy an toàn trong bất kỳ phiên nào.

### Liệt kê người đóng góp StackQL {#list-stackql-contributors}

Trả về bảng xếp hạng người đóng góp được nhúng trong tệp nhị phân StackQL đang chạy. Ảnh chụp nhanh này ghi nhận những người đóng góp GitHub là con người đã gắn sao cho kho lưu trữ StackQL, sắp xếp theo số lượt đóng góp, cao nhất trước. Dữ liệu này cố định cho mỗi bản dựng và không truy vấn GitHub khi chạy.

```sql
SHOW CONTRIBUTORS;
SHOW EXTENDED CONTRIBUTORS;
```

| Cột | Mô tả |
| --- | --- |
| `contributor` | Tên người dùng GitHub |
| `contributions` | Số lượt đóng góp được ghi trong ảnh chụp nhanh; có khi dùng `EXTENDED` |

### Liệt kê các phần phụ thuộc phần mềm của tệp nhị phân {#list-the-binarys-software-dependencies}

Trả về bản kiểm kê ngoại tuyến các module Go có trong tệp thực thi StackQL đang chạy. Bản kiểm kê này là một danh mục thành phần phần mềm (SBOM) gọn nhẹ cho tệp nhị phân, mà người dùng và tác nhân có thể truy vấn khi chạy. Bản kiểm kê bao gồm các phần phụ thuộc Go trực tiếp và bắc cầu; các định nghĩa provider đã cài đặt, các wrapper riêng biệt, công cụ CI, module chỉ dùng cho kiểm thử, image container và gói hệ điều hành nằm ngoài phạm vi của nó.

```sql
SHOW DEPENDENCIES;
SHOW EXTENDED DEPENDENCIES;
```

Các cột mặc định là `name`, `version`, `license` và `description`. `EXTENDED` thêm các cột còn lại bên dưới mà không thay đổi những hàng được trả về.

| Cột | Mô tả |
| --- | --- |
| `name` | Đường dẫn module Go có hiệu lực, dùng đường dẫn thay thế khi áp dụng thay thế module Go |
| `version` | Phiên bản module chính xác, bao gồm cả phiên bản tiền phát hành và pseudo-version |
| `license` | Định danh giấy phép được phát hiện; `UNKNOWN` khi không xác định được hoặc không rõ ràng |
| `description` | Mô tả thành phần, nếu có |
| `source_url` | URL kho lưu trữ mã nguồn; có khi dùng `EXTENDED` |
| `license_url` | URL tệp giấy phép được ghim vào tag của module hoặc bản sửa đổi mã nguồn; có khi dùng `EXTENDED` |
| `purl` | Package URL xác định module và phiên bản; có khi dùng `EXTENDED` |
| `checksum` | Checksum module Go kèm tiền tố `h1:`, không phải hash SHA-256 của tệp thực thi hay tệp nén; có khi dùng `EXTENDED` |
| `original_name` | Đường dẫn module được yêu cầu ban đầu khi áp dụng thay thế; có khi dùng `EXTENDED` |
| `original_version` | Phiên bản được yêu cầu ban đầu khi áp dụng thay thế; có khi dùng `EXTENDED` |

Các hàng được sắp xếp theo tên module có hiệu lực, sau đó theo phiên bản. Các thay thế dùng phiên bản, checksum và thông tin ghi công của bản thay thế. `original_name` và `original_version` là `NULL` đối với các module không có thay thế. Các siêu dữ liệu khác không khả dụng cũng là `NULL`, ngoại trừ giấy phép không xác định được là `UNKNOWN`. Các thay thế bằng hệ thống tệp cục bộ không có phiên bản, URL hay checksum giả tạo.

Dùng `LIKE` để tìm kiếm trong tên module có hiệu lực, tên module ban đầu hoặc mô tả:

```sql
SHOW DEPENDENCIES LIKE '%stackql%';
SHOW EXTENDED DEPENDENCIES LIKE '%vitess%';
SHOW EXTENDED DEPENDENCIES LIKE 'github.com/chzyer/readline';
```

Việc so khớp không phân biệt chữ hoa chữ thường. `%` khớp với không hoặc nhiều ký tự và `_` khớp với đúng một ký tự. Các cột giấy phép và phiên bản không được tìm kiếm. Viết dấu nháy đơn hai lần bên trong một literal chuỗi SQL; sau khi giải mã chuỗi SQL, dấu gạch chéo ngược được giữ nguyên nghĩa đen và không thoát ký tự đại diện của `LIKE`.

Khi không có module nào khớp, kết quả có 0 hàng. Đầu ra CSV vẫn giữ tiêu đề cột, ứng dụng khách giao thức wire PostgreSQL vẫn giữ siêu dữ liệu cột, còn đầu ra JSON là `null`.

Bản kiểm kê kết hợp siêu dữ liệu được nhúng khi dựng với thông tin bản dựng Go của tệp thực thi. Truy vấn không cần truy cập mạng, bản cài đặt Go, bản checkout mã nguồn hay cache module. Các bản dựng không có siêu dữ liệu được tạo vẫn báo cáo các module được liên kết của chúng, với thông tin ghi công còn thiếu hiển thị là `UNKNOWN` hoặc `NULL`.

Đầu ra chỉ bao gồm các phần phụ thuộc Go của tệp nhị phân. Đầu ra không bao gồm quan hệ phụ thuộc, không xuất tài liệu SPDX hay CycloneDX và không phải là một SBOM triển khai đầy đủ hay một kết luận về tuân thủ giấy phép.

* * *
