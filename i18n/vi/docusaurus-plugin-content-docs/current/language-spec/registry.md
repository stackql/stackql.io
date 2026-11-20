---
title: REGISTRY
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "REGISTRY trong StackQL: liệt kê các provider và phiên bản trong registry provider, rồi tải xuống phiên bản mới nhất hoặc một phiên bản cụ thể vào bản cài đặt của bạn."
image: "/img/stackql-featured-image.png"
---
import RailroadDiagram from '/js/RailroadDiagram/RailroadDiagram.js';
//import Gist from 'react-gist';

Liệt kê các provider hiện có trong registry provider đã cấu hình, hoặc tải xuống một provider vào bản cài đặt cục bộ.  

Xem thêm:  
[[ Phân cấp tài nguyên StackQL ]](/getting-started/resource-hierarchy) [[` --registry `]](/command-line-usage/global-flags#--registry)

> Từ `v1.0`, có thể yêu cầu tài liệu provider mang chữ ký ML-DSA-65 hợp lệ khi được đọc. Việc xác minh tắt theo mặc định và được bật bằng khóa `verifyConfig` của cờ [`--registry`](/command-line-usage/global-flags#--registry).

* * * 

## Cú pháp {#syntax}

*registryStatement::=*

<RailroadDiagram 
type="registry"
/>

&nbsp;  
&nbsp;  

```sql
REGISTRY { LIST [<provider>] | PULL <provider> [<version>]};
```

* * *

## Ví dụ {#examples}

### Liệt kê các provider hiện có {#list-available-providers}
Lấy danh sách phiên bản mới nhất của từng provider hiện có.  
```sql
REGISTRY LIST;
```
### Liệt kê mọi phiên bản của một provider cụ thể {#list-all-versions-for-a-specific-provider}
Lấy danh sách mọi phiên bản hiện có của một provider cụ thể.    
```sql
REGISTRY LIST google;
```

### Cài đặt phiên bản mới nhất của một provider {#install-the-latest-version-of-a-provider}
```sql
REGISTRY PULL okta;
```

### Cài đặt một phiên bản cụ thể của một provider {#install-a-specific-version-of-a-provider}
```sql
REGISTRY PULL azure v23.01.00104;
```
