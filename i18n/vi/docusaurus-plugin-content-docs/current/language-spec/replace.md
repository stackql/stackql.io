---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "REPLACE trong StackQL: thay thế mọi trường của một instance tài nguyên, tương đương HTTP PUT, khác với cập nhật một phần do UPDATE thực hiện."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Cập nhật mọi trường trong một tài nguyên (đồng nghĩa với thao tác `put`).

Xem thêm:  
[[ Phân cấp tài nguyên StackQL ]](/getting-started/resource-hierarchy) [[ `UPDATE` ]](/language-spec/update)

* * * 

## Cú pháp {#syntax}

*replaceStatement::=*

<RailroadDiagram 
type="replace"
/>

&nbsp;  
&nbsp;

```sql
REPLACE [ /*+ AWAIT  */ ] <multipartIdentifier>
SET field_name = value [, field_name = value ...]
[ WHERE condition ];
```

* * *

## Ví dụ {#examples}

### Câu lệnh `REPLACE` cơ bản {#basic-replace-statement}
Chạy câu lệnh `REPLACE` để thay thế toàn bộ topic trên một kho lưu trữ GitHub.

```sql
-- Replace all of the topics on a GitHub repository
REPLACE github.repos.topics
SET data__names = '["sql", "cloud-infrastructure"]'
WHERE owner = 'my-org'
AND repo = 'my-repo';
```

### Câu lệnh `REPLACE` (chặn) {#replace-statement-blocking}
Chạy câu lệnh `REPLACE` để thay thế một quy tắc tường lửa Compute Engine. Đây là một lệnh gọi chặn (đồng bộ).

```sql
-- Synchronous update of a google firewall 
REPLACE /*+ AWAIT */ google.compute.firewalls
SET
 data__name = 'test-fw',
 data__network = 'https://www.googleapis.com/compute/v1/projects/stackql-demo/global/networks/my-vpc',
 data__direction = 'INGRESS',
 data__sourceRanges = '["0.0.0.0/0"]',
 data__allowed = '[{"IPProtocol": "tcp", "ports": ["22"]}, {"IPProtocol": "tcp", "ports": ["6443"]},{"IPProtocol": "icmp"}]'
WHERE firewall = 'test-fw'
AND project = 'stackql-demo';
```

### Câu lệnh `REPLACE` với hàm JSON {#replace-statement-with-json-functions}
Chạy câu lệnh `REPLACE` có giá trị được dựng bằng các hàm JSON. Hàm không đọc cột nào sẽ được tính giá trị trước khi yêu cầu được gửi đi. Giá trị `SET` không thể đọc hàng đang được thay thế, vì vậy để thay đổi một phần của giá trị hiện có, trước tiên hãy đọc giá trị đó bằng `SELECT` rồi đặt giá trị đã sửa đổi.

```sql
-- Replace the topics on a GitHub repository with a list built by json_array
REPLACE github.repos.topics
SET data__names = json_array('sql', 'cloud-infrastructure', 'infrastructure-as-code')
WHERE owner = 'my-org'
AND repo = 'my-repo';
```
