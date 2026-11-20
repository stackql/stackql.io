---
title: Tạo template hạ tầng
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Tạo template INSERT cho bất kỳ tài nguyên nào bằng SHOW INSERT trong StackQL, đầy đủ hoặc chỉ với tham số bắt buộc, làm điểm khởi đầu cho định nghĩa hạ tầng."
image: "/img/stackql-featured-image.png"
---
Xem thêm:  
[[` exec `]](/command-line-usage/exec) [[` INSERT `]](/language-spec/insert) [[` SHOW `]](/language-spec/show) [[ Sử dụng biến ]](/getting-started/variables)

Tài nguyên đám mây được triển khai bằng StackQL thông qua các câu lệnh `INSERT`.  Có thể tạo template `INSERT` cho các tài nguyên đám mây khác nhau bằng lệnh `SHOW INSERT`.

### Tạo template `INSERT` bằng lệnh `SHOW INSERT` {#generating-an-insert-template-using-the-show-insert-command}

Dưới đây là một ví dụ về việc tạo template:

```sql
SHOW INSERT INTO google.compute.addresses;
```
Có thể chạy câu lệnh này qua `shell` hoặc bằng lệnh `exec` như minh họa ở đây:

```shell
./stackql exec 'show insert into google.compute.addresses' --output text --hideheaders
```

*__Trả về:__*  

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

> Sau đó, có thể dùng template này cùng với một tệp dữ liệu `json` hoặc `jsonnet` để cung cấp giá trị lúc chạy; để biết thêm thông tin, hãy xem [Sử dụng biến](/getting-started/variables).

### Tạo template `INSERT` chỉ với các tham số bắt buộc {#generating-an-insert-template-with-only-required-parameters}

Để tạo một template `INSERT` của StackQL chỉ với các thuộc tính bắt buộc, hãy dùng gợi ý truy vấn `/*+ REQUIRED */` như minh họa ở đây:

```sql
SHOW INSERT /*+ REQUIRED */ INTO google.compute.instances;
```
Có thể chạy câu lệnh này qua `shell` hoặc bằng lệnh `exec` như minh họa ở đây:

```shell
./stackql exec 'show insert /*+ REQUIRED */ into google.compute.instances' --output text -H
```

*__Trả về:__*  

```sql
INSERT INTO google.compute.instances(
  project,
  zone,
  data__machineType,
  data__name
)
SELECT
  '{{ .values.project }}',
  '{{ .values.zone }}',
  '{{ .values.data__machineType }}',
  '{{ .values.data__name }}'
;
```
