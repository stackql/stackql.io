---
title: SPLIT_PART
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm SPLIT_PART trong StackQL: tách một chuỗi theo dấu phân cách và trả về phần ở vị trí đánh số từ 1, hữu ích để tách ID tài nguyên và ARN."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Tách chuỗi nguồn dựa trên dấu phân cách và trả về phần được chỉ định (đếm từ một).  

Xem thêm:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT SPLIT_PART(source, sep, part) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*source*__  
Một literal chuỗi hoặc một cột chuỗi mà bạn muốn tách.

__*sep*__  
Một hoặc nhiều ký tự mà bạn muốn dùng làm dấu phân cách.  

> Nếu `sep` gồm nhiều ký tự, mỗi ký tự được coi là một dấu phân cách.  

> Chỉ các ký hiệu ASCII (1 byte) được hỗ trợ làm dấu phân cách.  

__*part*__  
Phần tử (đánh số từ một) của chuỗi đã tách cần trả về.  Giá trị âm dùng để đếm ngược từ phần tử cuối cùng.  

## Giá trị trả về {#return-values}
Trả về một chuỗi.

* * *

## Ví dụ {#examples}

### Trích xuất các phần tử từ một chuỗi được phân cách bằng dấu gạch chéo {#extract-elements-from-a-string-separated-by-a-forward-slash}


<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Kết quả', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT name,  
 split_part(id, '/', 3) as subscription,
 split_part(id, '/', 5) as resource_group,
 json_extract(properties, '$.hardwareProfile.vmSize') as vm_size
FROM azure.compute.virtual_machines 
 WHERE resource_group_name = 'stackql-ops-cicd-dev-01' 
 AND subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|--------------------------------------|-------------------------|-----------------|
| name |             subscription             |     resource_group      |     vm_size     |
|------|--------------------------------------|-------------------------|-----------------|
| test | 273769f6-545f-45b2-8ab8-2f14ec5768dc | stackql-ops-cicd-dev-01 | Standard_D2s_v3 |
|------|--------------------------------------|-------------------------|-----------------|
```

</TabItem>
</Tabs>

### Trích xuất phần tử cuối cùng từ một URL {#extract-the-last-element-from-a-url}


<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Kết quả', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
SELECT
name,
SPLIT_PART(network, '/', -1) as network,
SPLIT_PART(region, '/', -1) as region
FROM google.compute.vpn_gateways WHERE project = 'stackql-demo';
```

</TabItem>
<TabItem value="results">

```
+------------------------+-----------------------+----------------------+
| name                   | network               | region               |
+------------------------+-----------------------+----------------------+
| gcp-aws-vpn-gateway    | stackql-shrd-vpc-nw   | australia-southeast1 |
+------------------------+-----------------------+----------------------+
```

</TabItem>
</Tabs>

Để biết thêm thông tin, hãy xem [https://github.com/nalgeon/sqlean/blob/main/docs/text.md](https://github.com/nalgeon/sqlean/blob/main/docs/text.md)
