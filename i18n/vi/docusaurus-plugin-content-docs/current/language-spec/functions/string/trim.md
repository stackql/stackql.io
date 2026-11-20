---
title: TRIM
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: "Hàm TRIM trong StackQL: xóa dấu cách, hoặc bất kỳ ký tự nào trong một tập ký tự cho trước, khỏi cả hai đầu của một chuỗi."
image: "/img/stackql-featured-image.png"
---
Trả về một chuỗi được tạo bằng cách xóa dấu cách, hoặc bất kỳ ký tự nào trong một tập ký tự cho trước, khỏi cả hai đầu của chuỗi đầu vào.  

Xem thêm:  
[[` SELECT `]](/language-spec/select) [[` LTRIM `]](/language-spec/functions/string/ltrim) [[` RTRIM `]](/language-spec/functions/string/rtrim) 

* * * 

## Cú pháp {#syntax}

```sql
SELECT TRIM(character_expression[, character ]) FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*character_expression*__  
Một literal chuỗi hoặc một cột chuỗi.

__*character*__  
Một chuỗi ký tự không bắt buộc: mọi ký tự trong chuỗi này đều được xóa khỏi phần đầu và phần cuối của *character_expression*.  Nếu bỏ qua, hàm xóa dấu cách (không xóa ký tự tab hoặc ký tự xuống dòng).

## Giá trị trả về {#return-values}
Trả về một chuỗi.

* * *

## Ví dụ {#examples}

### Xóa dấu cách ở đầu và ở cuối khỏi một cột chuỗi {#remove-leading-and-trailing-spaces-from-a-string-column}

```sql
SELECT trim(description) AS leading_and_trailing_whitespace_removed
FROM google.compute.instances 
WHERE project = 'stackql-demo' 
AND zone = 'australia-southeast1-a';
```

Để biết thêm thông tin, hãy xem [https://www.sqlite.org/lang_corefunc.html#trim](https://www.sqlite.org/lang_corefunc.html#trim)
