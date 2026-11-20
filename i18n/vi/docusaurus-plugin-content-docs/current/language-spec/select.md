---
title: SELECT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "SELECT trong StackQL: truy vấn tài nguyên đám mây và SaaS bằng SQL, với bộ lọc WHERE ánh xạ tới tham số API, phép nối, gom nhóm, sắp xếp và truy cập trường JSON."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';

Trả về một hoặc nhiều instance của một tài nguyên.  

Xem thêm:
[[ Phân cấp tài nguyên StackQL ]](/getting-started/resource-hierarchy) [[` WITH (CTE) `]](/language-spec/with) [[` Hàm cửa sổ `]](/language-spec/windowing_functions)

* * * 

## Cú pháp {#syntax}

*selectStatement::=*

<RailroadDiagram 
type="select"
/>

&nbsp;  
&nbsp;

```sql
[ WITH [ RECURSIVE ] <cteName> [ ( <columnList> ) ] AS ( <selectStatement> ) [, ...] ]
SELECT { * | <fieldList> | <windowFunctionCall> [ AS <alias> ] }
FROM { <multipartIdentifier> | <joinStatement(s)> }
[ WHERE <expression> ]
[ GROUP BY <fieldList> ]
[ HAVING <expression> ]
[ WINDOW <windowName> AS ( <windowSpec> ) [, ...] ]
[ ORDER BY <fieldList> [ ASC | DESC ] ]
[ LIMIT <integer> ]
[ UNION <selectStatement> ];
```

Một `JOIN` không có mệnh đề `ON` được xử lý như một `CROSS JOIN`, vì vậy có thể đặt điều kiện nối trong mệnh đề `WHERE` thay thế. Các bản phát hành StackQL trước `v0.12.732` không lập được kế hoạch cho một `JOIN` không có điều kiện.

* * *

## Ví dụ {#examples}

### Câu lệnh `SELECT` cơ bản {#basic-select-statement}
Chạy một câu lệnh `SELECT` cơ bản trong một phiên đã xác thực.

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

### Trả về các trường được chỉ định bằng câu lệnh `SELECT` {#returning-specified-fields-using-a-select-statement}
Chạy câu lệnh `SELECT` với một danh sách cột để trả về các trường được chỉ định từ một instance Compute Engine.

```sql
-- Selecting specified fields from a resource
SELECT id, name 
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Trả về tập kết quả được sắp xếp tăng dần theo một cột bằng mệnh đề `ORDER BY` {#return-a-result-set-ordered-by-a-column-in-ascending-order-using-the-order-by-clause}
Chạy câu lệnh `SELECT` để trả về các trường từ tài nguyên bucket Cloud Storage, sắp xếp kết quả theo thứ tự tăng dần (cũ nhất trước). 

```sql
-- Order a list of Cloud Storage buckets by creation time (in ascending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated;
```

### Trả về tập kết quả được sắp xếp giảm dần theo một cột bằng mệnh đề `ORDER BY` {#return-a-result-set-ordered-by-a-column-in-descending-order-using-the-order-by-clause}
Chạy câu lệnh `SELECT` để trả về các trường từ tài nguyên bucket Cloud Storage, sắp xếp kết quả theo thứ tự giảm dần (mới nhất trước).

```sql
-- Order a list of Cloud Storage buckets by creation time (in descending order)
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC;
```

### Lấy kết quả đứng đầu từ một cột bằng mệnh đề `ORDER BY` và `LIMIT` {#get-the-top-result-from-a-column-using-the-order-by-and-limit-clauses}
Chạy câu lệnh `SELECT` để trả về bucket Cloud Storage được tạo gần đây nhất.

```sql
-- Find the most recently created bucket
SELECT name, location, timeCreated 
FROM google.storage.buckets WHERE project = 'stackql'
ORDER BY timeCreated DESC LIMIT 1;
```

### Dùng hàm `COUNT` với mệnh đề `GROUP BY` và `HAVING` để đếm tài nguyên theo nhóm và bộ lọc {#use-the-count-function-with-the-group-by-and-having-clauses-to-count-resources-based-upon-a-grouping-and-filter}
Chạy câu lệnh `SELECT` với hàm `COUNT`, dùng mệnh đề `GROUP BY` và `HAVING` để trả về số bucket ở mọi vị trí ngoại trừ châu Á.

```sql
-- Return a count of the number of buckets in every location other than Asia
SELECT location, count(*) as num_buckets 
FROM google.storage.buckets WHERE project = 'stackql'
GROUP BY location
HAVING location != 'ASIA';
```

Để biết thêm thông tin về hàm `COUNT` và các hàm tổng hợp khác mà StackQL hỗ trợ, hãy xem [Hàm tổng hợp](/language-spec/functions/aggregate/count).

### Lấy danh sách VM On Demand và giao diện mạng tương ứng từ Azure VM Scale Set {#get-list-of-on-demand-vms-and-corresponding-network-interface-from-azure-vm-scaleset}
Chạy câu lệnh `SELECT` dùng các hàm [`SPLIT_PART`](/language-spec/functions/string/split_part) và [`JSON_EXTRACT`](/language-spec/functions/json/json_extract) để lấy danh sách VM On Demand (OD) và NIC của chúng. Lưu ý rằng khi không có tiền tố định danh, `subscription_id` và `resource_group_name` được áp dụng cho cả ba tài nguyên.

```sql
SELECT  c.name AS vm_name,
        SPLIT_PART(JSON_EXTRACT(c.properties,'$.networkProfile.networkInterfaces[0].id'), '/', -1) AS nic_name
FROM
  azure.compute.virtual_machine_scale_sets a
  INNER JOIN  azure.compute.virtual_machine_scale_set_vms b
    ON a.name = b.virtualMachineScaleSetName
  INNER JOIN  azure.compute.virtual_machines c
    ON b.name = c.name
WHERE subscription_id = '0123456789'
AND resource_group_name = 'vmss-flex'
AND JSON_EXTRACT(c.properties,'$.priority') is null;
```

### Dùng biểu thức bảng chung (CTE) với `WITH` {#using-common-table-expressions-ctes-with-with}

CTE cho phép bạn định nghĩa các tập kết quả tạm thời có tên, có thể được tham chiếu trong truy vấn chính.

```sql
-- Combine and rank contributors across multiple repositories
WITH all_contributors AS (
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-deploy'
    UNION ALL
    SELECT login, contributions, html_url
    FROM github.repos.contributors
    WHERE owner = 'stackql' AND repo = 'stackql-provider-registry'
)
SELECT
    DENSE_RANK() OVER (ORDER BY SUM(contributions) DESC) as rank,
    login,
    SUM(contributions) as total_contributions,
    MAX(html_url) as html_url
FROM all_contributors
GROUP BY login
ORDER BY total_contributions DESC;
```

Để biết thêm thông tin về CTE, hãy xem [WITH (CTE)](/language-spec/with).

Để biết thêm thông tin về hàm cửa sổ, hãy xem [Hàm cửa sổ](/language-spec/windowing_functions).
