---
title: SIGN
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm SIGN trong StackQL: trả về -1, 0 hoặc 1 tùy theo đối số kiểu số là số âm, bằng 0 hay là số dương."
image: "/img/stackql-featured-image.png"
---
Trả về -1 nếu giá trị đầu vào là số âm, 0 nếu giá trị đầu vào bằng 0 và 1 nếu giá trị đầu vào là số dương.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT SIGN(numeric_expression) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*numeric_expression*__  
Một biểu thức thuộc nhóm kiểu dữ liệu số chính xác hoặc số xấp xỉ.

> Nếu *numeric_expression* là `NULL` hoặc là một `string` hay BLOB không thể chuyển đổi thành số mà không mất thông tin, hàm sẽ trả về `NULL`.

## Giá trị trả về {#return-values}

Trả về `-1`, `0` hoặc `1`.

* * *

## Ví dụ {#examples}

### Xác định dấu của một số {#determine-the-sign-of-a-number}

```sql
SELECT name, sign(strftime('%s', creationTimestamp)-strftime('%s','now')) as sign
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_corefunc.html#sign](https://www.sqlite.org/lang_corefunc.html#sign)
