---
title: REGEXP_SUBSTR
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm REGEXP_SUBSTR trong StackQL: trích xuất chuỗi con của đầu vào khớp với biểu thức chính quy."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Trả về một chuỗi con từ chuỗi đầu vào khớp với mẫu đã chỉ định.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[ Tham khảo biểu thức chính quy ]](/language-spec/functions/re/reference) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT REGEXP_SUBSTR(source, pattern) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*source*__  
Một literal chuỗi hoặc cột chuỗi mà bạn muốn trích xuất chuỗi con dựa trên một mẫu được định nghĩa bằng biểu thức chính quy (`pattern`).  

__*pattern*__  
Mẫu biểu thức chính quy cần tìm.  

## Giá trị trả về {#return-values}
Trả về một chuỗi.

* * *

## Ví dụ {#examples}

### Trích xuất chuỗi con dựa trên biểu thức chính quy {#extract-a-substring-based-upon-a-regular-expression}

<Tabs
  defaultValue="iql"
  values={[
    { label: 'StackQL', value: 'iql', },
    { label: 'Kết quả', value: 'results', },
  ]
}>
<TabItem value="iql">

```sql
select name,
json_extract(properties, '$.storageProfile.imageReference.offer') as os,
regexp_substr(
  json_extract(properties, '$.storageProfile.imageReference.exactVersion'), 
  '[0-9][0-9]\.[0-9][0-9]') as os_version 
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|---------------|------------|
| name |      os       | os_version |
|------|---------------|------------|
| vm0  | UbuntuServer  |      16.04 |
|------|---------------|------------|
| vm1  | UbuntuServer  |      16.04 |
|------|---------------|------------|
| vm2  | UbuntuServer  |      20.04 |
|------|---------------|------------|
```

</TabItem>
</Tabs>

Để biết thêm thông tin, hãy xem [https://github.com/nalgeon/sqlean/blob/main/docs/re.md](https://github.com/nalgeon/sqlean/blob/main/docs/re.md)
