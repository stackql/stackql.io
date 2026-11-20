---
title: COALESCE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm COALESCE trong StackQL: trả về đối số đầu tiên khác NULL trong hai đối số trở lên, thường để thay giá trị bị thiếu bằng một giá trị mặc định."
image: "/img/stackql-featured-image.png"
---
Trả về đối số đầu tiên không phải `NULL` trong một dãy gồm hai đối số trở lên, có thể dùng để thay thế giá trị `NULL` bằng một giá trị mặc định.  

> Hàm `COALESCE` tương đương với hàm `NVL` trong Oracle hoặc hàm `IFNULL` trong MySQL.

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[` IFNULL `]](/language-spec/functions/general/ifnull) [[` NULLIF `]](/language-spec/functions/general/nullif)

* * * 

## Cú pháp {#syntax}

```sql
SELECT COALESCE(expression1, expression2 [, ..]) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*expression*__  
Hai biểu thức trở lên thuộc kiểu dữ liệu bất kỳ, được phân giải thành giá trị `NULL` hoặc khác `NULL`.

> Nếu tất cả các đối số *expression* đều là `NULL`, hàm `COALESCE` trả về `NULL`.

## Giá trị trả về {#return-values}

Trả về giá trị đầu tiên khác `NULL` trong dãy đối số *expression*.

* * *

## Ví dụ {#examples}

### Thay thế giá trị `NULL` bằng một giá trị mặc định {#replace-a-null-value-with-a-default-value}

```sql
SELECT name, coalesce(website, cors, 'not hosting shared resources') AS website_or_cors_config
FROM google.storage.buckets where project = 'stackql';
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_corefunc.html#coalesce](https://www.sqlite.org/lang_corefunc.html#coalesce)
