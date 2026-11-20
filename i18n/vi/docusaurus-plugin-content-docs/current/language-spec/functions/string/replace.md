---
title: REPLACE
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm REPLACE trong StackQL: thay thế mọi lần xuất hiện của một chuỗi con trong một chuỗi bằng một chuỗi khác."
image: "/img/stackql-featured-image.png"
---
Trả về một bản sao của chuỗi, trong đó mọi lần xuất hiện của một chuỗi con được thay thế bằng một chuỗi khác.

Xem thêm:  
[[` SELECT `]](/language-spec/select) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT REPLACE(character_expression, pattern, replacement) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*character_expression*__  
Một literal chuỗi hoặc một cột chuỗi mà bạn muốn thực hiện thay thế.

__*pattern*__  
Chuỗi con cần tìm trong chuỗi ban đầu.

> Nếu *pattern* là chuỗi rỗng hoặc không có trong *character_expression*, hàm trả về *character_expression* mà không thay đổi. 

__*replacement*__  
Chuỗi thay thế.

> Nếu ban đầu *replacement* không phải là chuỗi, giá trị này được ép kiểu thành chuỗi UTF-8 trước khi xử lý.

> Thứ tự đối chiếu `BINARY` được dùng cho các phép so sánh.

## Giá trị trả về {#return-values}
Trả về một chuỗi.

* * *

## Ví dụ {#examples}

### Thay thế một mẫu chuỗi trong một cột {#replace-a-string-pattern-in-a-column}

```sql
SELECT replace(name, 'prd', 'production') AS replacement_string
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_corefunc.html#replace](https://www.sqlite.org/lang_corefunc.html#replace)
