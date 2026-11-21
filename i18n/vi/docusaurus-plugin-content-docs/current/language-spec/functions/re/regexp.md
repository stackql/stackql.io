---
title: REGEXP
hide_title: false
hide_table_of_contents: false
keywords:
  - stackql
  - infrastructure-as-code
  - configuration-as-data
  - kiểm kê đám mây
description: Dùng biểu thức chính quy để khớp mẫu trong chuỗi bằng SQL trong StackQL.
image: "/img/stackql-featured-image.png"
---
Kiểm tra xem chuỗi nguồn có khớp với mẫu biểu thức chính quy được cung cấp hay không.

Xem thêm:  
[[`SELECT`]](/language-spec/select)

* * * 

## Cú pháp {#syntax}

```sql
SELECT source_string REGEXP pattern_expression AS match_result FROM <multipartIdentifier>;
```

## Đối số {#arguments}

__*source_string*__  
Chuỗi cần tìm kiếm mẫu trong đó.

__*pattern_expression*__  
Mẫu biểu thức chính quy để so khớp với chuỗi nguồn.

## Giá trị trả về {#return-values}
Trả về `1` nếu chuỗi nguồn khớp với mẫu biểu thức chính quy, ngược lại trả về `0`.

* * *

## Ví dụ {#examples}

### Kiểm tra xem một chuỗi có chứa số hay không {#check-if-a-string-contains-numbers}

Ví dụ này minh họa cách kiểm tra xem chuỗi 'the year is 2021' có chứa một hoặc nhiều ký tự số hay không:

```sql
SELECT 'the year is 2021' REGEXP '[0-9]+' AS match_result;
```

Truy vấn này sẽ trả về `1` vì chuỗi chứa chuỗi con '2021', khớp với mẫu `[0-9]+`, biểu thị một hoặc nhiều chữ số.

### Kiểm tra định dạng email {#validate-email-format}

Để kiểm tra xem một chuỗi có đúng định dạng email hợp lệ hay không:

```sql
SELECT 'user@example.com' REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' AS match_result;
```

Truy vấn này trả về `1`, cho biết 'user@example.com' có định dạng giống một địa chỉ email thông thường, khớp với mẫu biểu thức chính quy đã chỉ định.
