---
title: EXEC
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "EXEC trong StackQL: gọi trực tiếp một phương thức tài nguyên của provider, với gợi ý truy vấn để trả về kết quả hoặc chờ hoàn tất, chẳng hạn khởi động hoặc dừng instance."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Thực thi một phương thức tài nguyên của provider (thủ tục tích hợp sẵn).  

Xem thêm:  
[[ Phân cấp tài nguyên StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Cú pháp {#syntax}

*execStatement::=*

<RailroadDiagram 
type="exec"
/>

&nbsp;  
&nbsp;

```sql
EXEC [ queryHint ] <multipartIdentifier>.<methodName> [ <methodArguments> ];
```

## Gợi ý truy vấn {#query-hints}

Bảng dưới đây tóm tắt các gợi ý truy vấn được hỗ trợ cho lệnh `EXEC`:  

| Gợi ý | Mô tả |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/*+ SHOWRESULTS */` | Trả về tập kết quả, được thiết kế cho các phương thức trả về dữ liệu, tuy nhiên hãy dùng `SELECT` nếu có thể. |
| `/*+ AWAIT  */` | Biến thao tác thành thao tác chặn (đồng bộ), cung cấp khả năng kiểm soát tập lệnh cho các thao tác chạy lâu (ví dụ: thao tác thay đổi dữ liệu).  Lưu ý: tính năng này không được hỗ trợ cho mọi provider |

* * *

## Ví dụ {#examples}

### Dừng một instance Google Compute Engine {#stopping-a-google-compute-engine-instance}
Chạy một câu lệnh `EXEC` để dừng một instance Compute Engine đang chạy trong một phiên đã xác thực.

```sql
-- Stop a running Compute Engine resource instance
EXEC google.compute.instances.stop 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Khởi động một instance Google Compute Engine (tùy chọn đồng bộ) {#starting-a-google-compute-engine-instance-synchronous-option}
Chạy một câu lệnh `EXEC` để khởi động một instance Compute Engine đã dừng trong một phiên đã xác thực, dùng gợi ý truy vấn `/*+ AWAIT  */` để biến thao tác thành thao tác chặn (đồng bộ).

```sql
-- Start a stopped Compute Engine resource instance
EXEC /*+ AWAIT  */ google.compute.instances.start 
@instance = 'demo-instance-1', 
@project = 'stackql-demo', 
@zone = 'australia-southeast1-a';
```

### Khởi động và dừng một instance AWS EC2 {#starting-and-stopping-an-aws-ec2-instance}
Chạy các câu lệnh `EXEC` để khởi động và dừng một instance AWS EC2 trong một phiên đã xác thực.

```sql
-- Start an EC2 instance
EXEC aws.ec2.instances.start_instances 
@InstanceId = 'i-0fc989cf4efcd8b88', 
@region = 'ap-southeast-2';
-- Stop an EC2 instance
EXEC aws.ec2.instances.stop_instances 
@InstanceId = 'i-0fc989cf4efcd8b88', 
@region = 'ap-southeast-2';
```

### Gắn một ổ đĩa vào một instance Google Compute Engine {#attach-a-disk-to-a-google-compute-engine-instance}
Chạy một câu lệnh `EXEC` để gắn một ổ đĩa vào một instance Compute Engine trong một phiên đã xác thực.

```sql
-- Attach a Disk resource to a Compute Engine instance
EXEC google.compute.instances.attach_disk 
@zone='australia-southeast1-a', 
@project='stackql-demo', 
@instance='demo-instance-1' 
@@json=
'{
"source": "projects/stackql-demo/zones/australia-southeast1-a/disks/test10gbdisk2", 
"boot": false 
}';
```

### Mô phỏng việc thu hồi Azure Spot VM {#simulate-azure-spot-vm-eviction}
Chạy một câu lệnh `EXEC` để mô phỏng việc thu hồi một instance Azure Spot VM. 

```sql
exec azure.compute.virtual_machines.simulate_eviction
@subscription_id = '0123456789',
@resource_group_name = 'vmss-flex',
@vm_name = 'vmss-flex-manual-1';
```
