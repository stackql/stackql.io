---
title: REGEXP_LIKE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm REGEXP_LIKE trong StackQL: kiểm tra xem một chuỗi có khớp với biểu thức chính quy hay không, trả về 1 cho true và 0 cho false, để lọc theo mẫu trong mệnh đề WHERE."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Kiểm tra xem chuỗi nguồn có khớp với mẫu hay không và trả về `1` cho true hoặc `0` cho false.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[ Tham khảo biểu thức chính quy ]](/language-spec/functions/re/reference) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT REGEXP_LIKE(source, pattern) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*source*__  
Một literal chuỗi hoặc cột chuỗi mà bạn muốn kiểm tra sự tồn tại của một mẫu được định nghĩa bằng biểu thức chính quy (`pattern`).  

__*pattern*__  
Mẫu biểu thức chính quy cần tìm.  

## Giá trị trả về {#return-values}
Trả về `1` cho true (chuỗi nguồn khớp với mẫu) hoặc `0` cho false.  

* * *

## Ví dụ {#examples}

### Xác định xem một chuỗi có chứa mẫu hay không {#determine-if-a-string-contains-a-pattern}

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
regexp_like(name, '^vm[0-9]+') as name_starts_with_vm 
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|---------------------|
| name | name_starts_with_vm |
|------|---------------------|
| vm0  |                   1 |
|------|---------------------|
| vm1  |                   1 |
|------|---------------------|
| test |                   0 |
|------|---------------------|
```

</TabItem>
</Tabs>

Để biết thêm thông tin, hãy xem [https://github.com/google/re2/wiki/Syntax](https://github.com/google/re2/wiki/Syntax)
