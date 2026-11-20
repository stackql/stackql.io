---
title: Phân cấp tài nguyên
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Phân cấp tài nguyên của StackQL: cách provider, dịch vụ, tài nguyên và phương thức ánh xạ các API đám mây và SaaS thành các bảng SQL có thể truy vấn."
image: "/img/stackql-featured-image.png"
---
import ImageSwitcher from '/js/ImageSwitcher/ImageSwitcher.js';

Xem thêm:  
[[` SHOW `]](/language-spec/show) [[` DESCRIBE `]](/language-spec/describe)

* * * 

Các đối tượng trong StackQL được biểu diễn theo phân cấp sau, kèm một ví dụ dùng provider Google Cloud:

<ImageSwitcher 
lightImageSrc="/img/resource-heirarchy.png"
darkImageSrc="/img/resource-heirarchy-darkbg.png"
alttext="Phân cấp tài nguyên StackQL"/>

### Sử dụng ký pháp đối tượng StackQL {#using-the-stackql-object-notation}

Phân cấp này được biểu diễn bằng ký pháp đối tượng `<provider>.<service>.<resource>` trong ngữ cảnh của một provider nhất định, dưới đây là một ví dụ về ký pháp đối tượng này được dùng trong câu lệnh [`SELECT`](/language-spec/select):  

```sql
-- Selecting all resources deployed within a service using a basic SELECT statement
SELECT * FROM google.compute.instances
WHERE project = 'stackql-demo'
AND zone = 'australia-southeast1-a';
```

:::note

Nếu tên của một tài nguyên hoặc dịch vụ có chứa dấu chấm (`.`), hãy dùng ký tự backtick (__`__) để bao tên tài nguyên, ví dụ:

```sql
SELECT * FROM google.container.`projects.zones.clusters.nodePools`;
```

:::

### Hiển thị dịch vụ trong một provider {#showing-services-in-a-provider}

Có thể liệt kê các dịch vụ trong một provider bằng lệnh [`SHOW SERVICES`](/language-spec/show) như minh họa ở đây:  

```sql
-- Returns all of the available services in a cloud provider
SHOW SERVICES IN google;
```  

### Hiển thị tài nguyên trong một dịch vụ {#showing-resources-within-a-service}

Tương tự, có thể liệt kê các tài nguyên trong một dịch vụ bằng [`SHOW RESOURCES`](/language-spec/show) như minh họa ở đây:

```sql
-- Returns all of the available resources in a cloud provider service
SHOW RESOURCES IN google.compute;
```  

### Hiển thị trường trong một tài nguyên {#showing-fields-within-a-resource}

Có thể hiển thị các trường trong một tài nguyên bằng câu lệnh [`DESCRIBE`](/language-spec/describe) như minh họa ở đây:

```sql
-- Returns all of the available fields in a resource
DESCRIBE google.compute.instances;
```

### Hiển thị các phương thức có thể thực thi của một tài nguyên {#showing-executable-methods-for-a-resource}

Có thể hiển thị các phương thức khả dụng của một tài nguyên bằng lệnh [`SHOW METHODS`](/language-spec/show) như minh họa ở đây:

```sql
-- Show all of the available methods for a resource
SHOW METHODS IN google.compute.instances;
```
