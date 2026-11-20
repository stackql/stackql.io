---
title: RTRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm RTRIM trong StackQL: xóa dấu cách, hoặc bất kỳ ký tự nào trong một tập ký tự cho trước, khỏi phía bên phải của một chuỗi."
image: "/img/stackql-featured-image.png"
---
Trả về một chuỗi được tạo bằng cách xóa dấu cách, hoặc bất kỳ ký tự nào trong một tập ký tự cho trước, khỏi phía bên phải của chuỗi đầu vào.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` TRIM `]](/language-spec/functions/string/trim) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT RTRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*character_expression*__  
Một literal chuỗi hoặc một cột chuỗi.

__*character*__  
Một chuỗi ký tự không bắt buộc: mọi ký tự trong chuỗi này đều được xóa khỏi phần cuối của *character_expression*.  Nếu bỏ qua, hàm xóa dấu cách (không xóa ký tự tab hoặc ký tự xuống dòng).

## Giá trị trả về {#return-values}
Trả về một chuỗi.

* * *

## Ví dụ {#examples}

### Xóa dấu cách ở cuối khỏi một cột chuỗi {#remove-trailing-spaces-from-a-string-column}

```sql
SELECT rtrim(description) AS trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_corefunc.html#rtrim](https://www.sqlite.org/lang_corefunc.html#rtrim)
