---
title: REGEXP_REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm REGEXP_REPLACE trong StackQL: thay thế các phần của một chuỗi khớp với biểu thức chính quy bằng một chuỗi thay thế."
image: "/img/stackql-featured-image.png"
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

Thay thế chuỗi con khớp bằng chuỗi thay thế.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[ Tham khảo biểu thức chính quy ]](/language-spec/functions/re/reference) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT REGEXP_REPLACE(source, pattern, replacement) FROM <multipartIdentifier>;
```
## Đối số {#arguments}

__*source*__  
Một literal chuỗi hoặc cột chuỗi, trong đó các phần khớp với biểu thức chính quy `pattern` được thay thế bằng `replacement`.  

__*pattern*__  
Mẫu biểu thức chính quy cần tìm.  

__*replacement*__  
Chuỗi thay thế.

> Chuỗi thay thế được chèn nguyên văn: các tham chiếu nhóm bắt như `$1` không được mở rộng.  

## Giá trị trả về {#return-values}
Trả về một chuỗi.

* * *

## Ví dụ {#examples}

### Thay thế một mẫu biểu thức chính quy trong một cột {#replace-a-regular-expression-pattern-in-a-column}

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
regexp_replace(json_extract(properties, '$.timeCreated'), 'T.*', '') as date_created
from azure.compute.virtual_machines 
WHERE subscription_id = '273769f6-545f-45b2-8ab8-2f14ec5768dc';
```

</TabItem>
<TabItem value="results">

```
|------|--------------|
| name | date_created |
|------|--------------|
| vm0  | 2022-09-14   |
|------|--------------|
| vm1  | 2022-09-14   |
|------|--------------|
| vm2  | 2022-09-14   |
|------|--------------|
```

</TabItem>
</Tabs>

Để biết thêm thông tin, hãy xem [https://github.com/google/re2/wiki/Syntax](https://github.com/google/re2/wiki/Syntax)
