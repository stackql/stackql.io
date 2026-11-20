---
title: UPDATE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "UPDATE trong StackQL: thay đổi các trường được chỉ định của một instance tài nguyên, tương đương HTTP PATCH, với đích được xác định bằng mệnh đề WHERE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Cập nhật các trường được chỉ định trong một tài nguyên (đồng nghĩa với thao tác `patch`).

Xem thêm:  
[[ Phân cấp tài nguyên StackQL ]](/getting-started/resource-hierarchy) [[ `REPLACE` ]](/language-spec/replace)

* * * 

## Cú pháp {#syntax}

*updateStatement::=*

<RailroadDiagram 
type="update"
/>

&nbsp;  
&nbsp;

```sql
UPDATE [ /*+ AWAIT  */ ] <multipartIdentifier>
SET field_name = value [, field_name = value ...]
[ WHERE condition ];
```

* * *

## Ví dụ {#examples}

### Câu lệnh `UPDATE` cơ bản {#basic-update-statement}
Chạy câu lệnh `UPDATE` để đặt nhãn cho một bucket Google Cloud Storage.

```sql
-- Set the labels on a Google Cloud Storage bucket
UPDATE google.storage.buckets
SET data__labels = '{"env": "prod", "team": "platform"}'
WHERE bucket = 'stackql-demo-bucket';
```

### Câu lệnh `UPDATE` (chặn) {#update-statement-blocking}
Chạy câu lệnh `UPDATE` để sửa đổi một quy tắc tường lửa Compute Engine. Đây là một lệnh gọi chặn (đồng bộ).

```sql
-- Synchronous update of a google firewall 
UPDATE /*+ AWAIT */ google.compute.firewalls
SET
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```

### Câu lệnh `UPDATE` với hàm JSON {#update-statement-with-json-functions}
Chạy câu lệnh `UPDATE` có giá trị được tạo bằng các hàm JSON. Một hàm không đọc cột nào sẽ được tính trước khi yêu cầu được gửi đi. Giá trị trong `SET` không thể đọc hàng đang được cập nhật, vì vậy để thay đổi một phần của giá trị hiện có, trước tiên hãy đọc giá trị đó bằng `SELECT` rồi đặt giá trị đã sửa đổi.

```sql
-- Set the protocols and ports a firewall rule allows
UPDATE google.compute.firewalls
SET data__allowed = json_array(
  json_object('IPProtocol', 'tcp', 'ports', json_array('22', '443')),
  json_object('IPProtocol', 'icmp')
)
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```
