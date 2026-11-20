---
title: GROUP_CONCAT
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm tổng hợp GROUP_CONCAT trong StackQL: nối các giá trị khác NULL của một cột hoặc một nhóm thành một chuỗi với dấu phân cách tùy chọn."
image: "/img/stackql-featured-image.png"
---
Trả về một chuỗi là kết quả nối tất cả các giá trị khác `NULL` của một cột đầu vào hoặc một nhóm.  

> Thứ tự của các phần tử được nối là tùy ý.

Xem thêm:  
[[` SELECT `]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT GROUP_CONCAT(columnExpression [, separator]) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Đối số {#arguments}

__*columnExpression*__  
Một cột hoặc biểu thức.  

> Phải có ít nhất một giá trị của *columnExpression* khác `NULL`, nếu không hàm sẽ trả về `NULL`.

__*separator*__  
Dấu phân cách dùng cho kết quả nối, giá trị mặc định `,` được dùng nếu không chỉ định *separator*.

__*groupByColumn*__  
Một hoặc nhiều cột được dùng để thực hiện các thao tác tóm tắt hoặc tổng hợp.  Mệnh đề `GROUP BY` trả về một hàng cho mỗi nhóm cột.

## Giá trị trả về {#return-values}

Trả về một chuỗi dưới dạng danh sách các giá trị được phân cách bởi *separator* hoặc `,`.

* * *

## Ví dụ {#examples}

### Trả về danh sách giá trị của một cột trên toàn bộ một tài nguyên {#return-a-list-of-values-for-a-column-over-an-entire-resource}

```sql
SELECT group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Trả về danh sách giá trị của một biểu thức cột được nhóm theo một biểu thức cột khác {#return-a-list-of-values-for-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT SPLIT_PART(machineType, '/', -1) as machine_type, group_concat(name) as instance_list
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY SPLIT_PART(machineType, '/', -1);
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_aggfunc.html#group_concat](https://www.sqlite.org/lang_aggfunc.html#group_concat).
