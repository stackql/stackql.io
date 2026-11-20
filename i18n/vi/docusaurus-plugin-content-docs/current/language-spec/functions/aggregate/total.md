---
title: TOTAL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm tổng hợp TOTAL trong StackQL: tổng các giá trị khác NULL trong một cột hoặc một nhóm, luôn trả về dạng số dấu phẩy động."
image: "/img/stackql-featured-image.png"
---
Trả về tổng của tất cả các giá trị khác `NULL` trong một cột hoặc một nhóm cột.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[` SUM `]](/language-spec/functions/aggregate/sum)

* * * 

## Cú pháp {#syntax}

```sql
SELECT TOTAL(columnExpression) FROM <multipartIdentifier>
[ GROUP BY groupByColumn ];
```

## Đối số {#arguments}

__*columnExpression*__  
Một cột hoặc biểu thức.

> Nếu tất cả các giá trị trong *columnExpression* đều là `NULL`, `TOTAL` trả về `0.0`.  

__*groupByColumn*__  
Một hoặc nhiều cột được dùng để thực hiện các thao tác tóm tắt hoặc tổng hợp.  Mệnh đề `GROUP BY` trả về một hàng cho mỗi nhóm cột.

## Giá trị trả về {#return-values}

Trả về một giá trị dấu phẩy động biểu thị tổng của *columnExpression*.

* * *

## Ví dụ {#examples}

### Trả về tổng của một biểu thức cột trên toàn bộ một tài nguyên {#return-the-sum-of-a-column-expression-over-an-entire-resource}

```sql
SELECT total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

### Trả về tổng của một biểu thức cột được nhóm theo một biểu thức cột khác {#return-the-sum-of-a-column-expression-grouped-by-another-column-expression}

```sql
SELECT status, total(json_array_length(disks)) as total_disks
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a'
GROUP BY status;
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_aggfunc.html#sumunc](https://www.sqlite.org/lang_aggfunc.html#sumunc).
