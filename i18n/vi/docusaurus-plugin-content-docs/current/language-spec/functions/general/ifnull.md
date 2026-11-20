---
title: IFNULL
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm IFNULL trong StackQL: trả về đối số đầu tiên khác NULL trong đúng hai đối số, là dạng hai đối số của COALESCE."
image: "/img/stackql-featured-image.png"
---
Nhận đúng hai đối số và trả về đối số đầu tiên khác `NULL`, có thể dùng để thay thế giá trị `NULL` bằng một giá trị mặc định.  

> Hàm `IFNULL` tương đương với hàm [`COALESCE`](/language-spec/functions/general/coalesce) khi chỉ có hai đối số, còn hàm [`COALESCE`](/language-spec/functions/general/coalesce) có thể có nhiều hơn hai đối số.

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[` COALESCE `]](/language-spec/functions/general/coalesce) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## Cú pháp {#syntax}

```sql
SELECT IFNULL(expression1, expression2) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*expression*__  
Hai biểu thức thuộc kiểu dữ liệu bất kỳ, được phân giải thành giá trị `NULL` hoặc khác `NULL`.

> Nếu cả hai đối số *expression* đều là `NULL`, hàm `IFNULL` trả về `NULL`.


## Giá trị trả về {#return-values}

Trả về giá trị đầu tiên khác `NULL` trong hai đối số *expression*. 

* * *

## Ví dụ {#examples}

### Thay thế giá trị `NULL` bằng một giá trị mặc định {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, ifnull(website, 'Not hosting a website') AS website 
FROM google.storage.buckets where project = 'stackql';
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_corefunc.html#ifnull](https://www.sqlite.org/lang_corefunc.html#ifnull)
