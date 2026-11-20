---
title: DELETE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "DELETE trong StackQL: xóa một hoặc nhiều instance của tài nguyên đám mây hoặc SaaS bằng câu lệnh SQL, lấy các tham số bắt buộc từ mệnh đề WHERE."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Xóa một hoặc nhiều instance của một tài nguyên. 

Xem thêm:  
[[ Phân cấp tài nguyên StackQL ]](/getting-started/resource-hierarchy)

* * * 

## Cú pháp {#syntax}

*deleteStatement::=*

<RailroadDiagram 
type="delete"
/>

&nbsp;  
&nbsp;  

```sql
DELETE FROM <multipartIdentifier>
WHERE <expression> ;
```

* * *

## Ví dụ {#examples}

### Câu lệnh `DELETE` cơ bản {#basic-delete-statement}
Chạy một câu lệnh DELETE cơ bản để xóa một tài nguyên đã triển khai trong một phiên đã xác thực.

```sql
-- Delete a Compute Engine resource instance by name
DELETE FROM google.compute.instances 
WHERE instance = 'demo-instance-1' 
AND project = 'stackql-demo' AND zone = 'australia-southeast1-a';
```
