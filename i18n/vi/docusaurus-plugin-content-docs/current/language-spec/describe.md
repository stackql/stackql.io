---
title: DESCRIBE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "DESCRIBE trong StackQL: liệt kê các trường của một tài nguyên, hoặc các tham số và kiểu trả về của một phương thức cụ thể, trước khi bạn truy vấn hoặc thay đổi nó."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Mô tả một tài nguyên hoặc một phương thức cụ thể của một tài nguyên.

Xem thêm:
[[ Phân cấp tài nguyên StackQL ]](/getting-started/resource-hierarchy)

* * *

## Cú pháp {#syntax}

*describeStatement::=*

<RailroadDiagram
type="describe"
/>

&nbsp;
&nbsp;

```sql
DESCRIBE [ METHOD ] [ EXTENDED ] <multipartIdentifier> ;
DESC [ EXTENDED ] <multipartIdentifier> ;
```

`DESC` được chấp nhận làm bí danh của `DESCRIBE` khi mô tả một tài nguyên; `DESCRIBE METHOD` yêu cầu từ khóa đầy đủ.

## Phản hồi {#response}

<Tabs
  defaultValue="describe-resource"
  values={[
    { label: 'DESCRIBE', value: 'describe-resource', },
    { label: 'DESCRIBE METHOD', value: 'describe-method', },
  ]
}>
<TabItem value="describe-resource">

| Cột | Mô tả |
| --- | --- |
| `name` | Tên trường |
| `type` | Kiểu dữ liệu (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `description` | Mô tả trường (nếu có chỉ định `EXTENDED`) |

</TabItem>
<TabItem value="describe-method">

| Cột | Mô tả |
| --- | --- |
| `name` | Tên trường |
| `type` | Kiểu dữ liệu (`string`, `integer`, `boolean`, `number`, `object`, `array`) |
| `param_type` | `input_required`, `input_optional` hoặc `output` |
| `shape` | Tập con JSON Schema cho các trường đối tượng và mảng; để trống đối với các kiểu vô hướng. Bao gồm `properties`, `items`, `required`, `enum`, `default`, `description` lồng nhau, và có thể bao gồm các giá trị boolean nếu provider cung cấp (`readOnly`, `writeOnly`, `deprecated`) |
| `description` | Mô tả trường (nếu có chỉ định `EXTENDED`) |

</TabItem>
</Tabs>

* * *

## Ví dụ {#examples}

### Câu lệnh `DESCRIBE` cơ bản {#basic-describe-statement}
Chạy một câu lệnh `DESCRIBE` cơ bản để liệt kê các trường trong một tài nguyên từ một phiên đã xác thực.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE google.compute.instances;
```

### Câu lệnh `DESCRIBE` mở rộng {#extended-describe-statement}
Chạy một câu lệnh `DESCRIBE` mở rộng để liệt kê các trường trong một tài nguyên cùng mô tả của chúng từ một phiên đã xác thực.

```sql
-- Show the available fields in a Compute Engine resource
DESCRIBE EXTENDED google.compute.instances;
```

### Bí danh `DESC` {#desc-alias}
`DESC` là dạng viết tắt của `DESCRIBE` trên một tài nguyên, có hoặc không có `EXTENDED`.  Câu lệnh này được coi là câu lệnh chỉ đọc ở mọi nơi `DESCRIBE` được coi như vậy, kể cả công cụ `run_select_query` của máy chủ MCP ở chế độ `read_only` và `safe`.

```sql
-- Same result as DESCRIBE EXTENDED google.compute.instances
DESC EXTENDED google.compute.instances;
```

### Câu lệnh `DESCRIBE METHOD` cơ bản {#basic-describe-method-statement}
Kiểm tra chi tiết một phương thức của một tài nguyên. Kết quả bao gồm các tham số đầu vào và các trường phản hồi, với cấu trúc lồng nhau được hiển thị trong cột `shape`.

```sql
-- Show the inputs and outputs for the buckets.get method
DESCRIBE METHOD google.storage.buckets.get;
```

### Câu lệnh `DESCRIBE METHOD` mở rộng {#extended-describe-method-statement}
Chạy một câu lệnh `DESCRIBE METHOD` mở rộng để có thêm mô tả của từng trường bên cạnh dạng dữ liệu.

```sql
-- Show inputs, outputs, and descriptions for the buckets.insert method
DESCRIBE METHOD EXTENDED google.storage.buckets.insert;
```

### Khám phá payload của `INSERT` {#discovering-an-insert-payload}
`DESCRIBE METHOD` là cách chuẩn để tìm hiểu một `INSERT` (hoặc bất kỳ phương thức thay đổi dữ liệu nào) chấp nhận những trường nào. Cột `shape` chứa cấu trúc lồng nhau mà tác nhân và con người có thể dùng để dựng payload trong một truy vấn.

```sql
-- See what google.compute.instances.insert requires
DESCRIBE METHOD EXTENDED google.compute.instances.insert;
```
