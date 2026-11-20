---
title: INSERT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "INSERT trong StackQL: tạo một hoặc nhiều instance của tài nguyên đám mây hoặc SaaS bằng câu lệnh SQL, truyền trường bắt buộc và không bắt buộc dưới dạng cột và giá trị."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Tạo một hoặc nhiều instance mới của một tài nguyên.

Xem thêm:  
[[ Phân cấp tài nguyên StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Cú pháp {#syntax}

*insertStatement::=*

<RailroadDiagram 
type="insert"
/>

&nbsp;  
&nbsp;

```sql
INSERT [ /*+ AWAIT  */ ] [ IGNORE ] [ INTO ] <multipartIdentifier>
( field_name [, field_name] ... )
{ VALUES ( value [, value] ... ) | <selectExpression> }
[ RETURNING field_name [, field_name] ... ];
```

* * *

## Mệnh đề `RETURNING` {#returning-clause}

Mệnh đề `RETURNING` cho phép bạn lấy dữ liệu từ phản hồi của thao tác `INSERT`. Điều này đặc biệt hữu ích khi:
- API trả về thông tin quan trọng như ID được tạo, mã trạng thái hoặc siêu dữ liệu khác
- Bạn cần thu thập dữ liệu phản hồi để xử lý thêm
- Bạn muốn xác minh kết quả của thao tác

Các trường được chỉ định trong mệnh đề `RETURNING` sẽ được giải tuần tự hóa từ phản hồi API và hiển thị trong tập kết quả.

* * *

## Ví dụ {#examples}

### Câu lệnh `INSERT` cơ bản {#basic-insert-statement}
Chạy câu lệnh `INSERT` để tạo một tài nguyên Compute Engine Disk. Đây là một lệnh gọi không chặn (bất đồng bộ).

```sql
-- Create a Compute Engine Disk resource
INSERT INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

### Câu lệnh `INSERT` (chặn) {#insert-statement-blocking}
Chạy câu lệnh `INSERT` để tạo một tài nguyên Compute Engine Disk. Đây là một lệnh gọi chặn (đồng bộ).

```sql
-- Create a Compute Engine Disk resource
INSERT /*+ AWAIT  */ INTO google.compute.disks (project, zone, name, sizeGb) 
SELECT 'stackql-demo', 
'australia-southeast1-a', 
'test10gbdisk', 10;
```

### Câu lệnh `INSERT` với `SELECT` {#insert-statement-with-select}
Chạy câu lệnh `INSERT` với `SELECT`.

```sql
-- Create an Azure Network Interface
INSERT INTO azure.network.network_interfaces
  (
    resource_group_name,
    subscription_id,
    network_interface_name,
    data__location,
    data__properties
  )
  SELECT
    'vmss-flex',
    '0123456789',
    'vmss-flex-vnet-nic01-manual-' || '1',
    'eastus',
    json_replace(json_remove(properties,
                                  '$.allowPort25Out',
                                  '$.auxiliarySku',
                                  '$.provisioningState',
                                  '$.resourceGuid',
                                  '$.macAddress',
                                  '$.vnetEncryptionSupported',
                                  '$.enableIPForwarding',
                                  '$.defaultOutboundAccess',
                                  '$.primary',
                                  '$.virtualMachine',
                                  '$.hostedWorkloads',
                                  '$.tapConfigurations',
                                  '$.nicType',
                                  '$.auxiliaryMode',
                                  '$.ipConfigurations[0].id',
                                  '$.ipConfigurations[0].etag',
                                  '$.ipConfigurations[0].type',
                                  '$.ipConfigurations[0].properties.provisioningState',
                                  '$.ipConfigurations[0].properties.privateIPAddress',
                                  '$.ipConfigurations[0].properties.privateIPAllocationMethod',
                                  '$.ipConfigurations[0].properties.primary',
                                  '$.ipConfigurations[0].properties.privateIPAddressVersion'
                                ),
                      '$.ipConfigurations[0].name',
                      'vmss-flex-vnet-nic01-defaultIpConfiguration'
                      )
  FROM
    azure.network.network_interfaces
  WHERE subscription_id = '0123456789'
  AND resource_group_name = 'vmss-flex'
  AND network_interface_name = 'vmss-nic-01';
```

### Câu lệnh `INSERT` với mệnh đề `RETURNING` {#insert-statement-with-returning-clause}
Chạy câu lệnh `INSERT` và lấy các trường cụ thể từ phản hồi API.

```sql
-- Execute a Snowflake SQL statement and return the response data
INSERT INTO snowflake.sqlapi.statements (
    data__statement,
    data__timeout,
    data__database,
    data__schema,
    "User-Agent",
    endpoint
)
SELECT
    'SELECT
        customer_id,
        customer_name,
        email,
        credit_card_number,
        purchase_amount,
        purchase_date
    FROM CUSTOMER_DATA',
    10,
    'MY_DATABASE',
    'MY_SCHEMA',
    'User-Agent',
    'ABCDEF-XY12345'
RETURNING code, sqlState, message, data;
```

Kết quả:
```
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
|  code  | sqlState |            message             |                                    data                                     |
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
| 090001 |    00000 | Statement executed             | [["1","John                                                                 |
|        |          | successfully.                  | Smith","john.smith@example.com","***MASKED***","199.99","20262"],["1","John |
|        |          |                                | Smith","john.smith@example.com","***MASKED***","199.99","20263"]]           |
|--------|----------|--------------------------------|-----------------------------------------------------------------------------|
```
